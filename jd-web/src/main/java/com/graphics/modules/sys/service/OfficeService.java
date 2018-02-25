package com.graphics.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graphics.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.sys.dao.OfficeDao;
import com.graphics.modules.sys.entity.Office;

/**
 * 部门Service
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	@Autowired
	private OfficeDao officeDao;
	
	public Office get(String id) {
		return officeDao.get(id);
	}
	
	/**
	 * 返回树形结构的office，最上面的节点为pid=0的节点 即总公司
	 * @return
	 */
	public List<Office> allListTree(){
		List<Office> officeList = Lists.newArrayList();
		List<Office> all = officeDao.findAll();
		Map<String, Office> officeMap = new HashMap<String, Office>();
		for(Office office:all){
			officeMap.put(office.getId(), office);
		}
		for(Office office:all){
			if("0".equals(office.getPid())){
				officeList.add(office);
			}else{
				Office parent = officeMap.get(office.getPid());
				if(parent!=null){
					parent.addChild(office);
				}
			}
		}
		return officeList;
	}
	
	public Page<Office> find(Page<Office> page, Office office) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(office.getName())) {
			dc.add(Restrictions.like("name", "%" + office.getName() + "%"));
		}
		if (StringUtils.isNotEmpty(office.getId())) {
			dc.add(Restrictions.or(
					Restrictions.eq("pid", office.getId()),
					Restrictions.eq("id", office.getId())));
		}
		if (StringUtils.isNotEmpty(office.getPid())) {
			dc.add(Restrictions.eq("pid", office.getPid()));
		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		return officeDao.find(page, dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		officeDao.save(office);
	}
	
	/**
	 * 新增部门
	 * @param pid	上级部门
	 * @param sort	排序(从小到大)
	 * @param name	名称
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void create(String pid,int sort,String name) throws ServiceException{
		if(StringUtils.isBlank(pid)){
			pid = "0";
		}
		if(StringUtils.isBlank(name)){
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_NAME_REQUIRED);
		}
		//查找上级部门
		Office parent = officeDao.get(pid);
		if(parent==null){
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_NOT_EXIST);
		}
		
		//设置code值
		String code = parent.getCode();
		Office maxChild = officeDao.findMaxChildByCode(parent.getCode(), true);
		if(maxChild==null){
			code = code+"00";
		}else{
			String childCode = maxChild.getCode().substring(maxChild.getCode().length()-2);
			int cCode = Integer.valueOf(childCode);
			cCode = cCode +1;
			if(cCode<=9){
				code = code +"0"+cCode;
			}else{
				code = code + cCode;
			}
		}
		
		int level = code.length() / 2;
		
		Office newOffice = new Office();
		newOffice.setCode(code);
		newOffice.setLevel(level);
		newOffice.setName(name);
		newOffice.setPid(pid);
		newOffice.setSort(sort);
		
		officeDao.save(newOffice);
		
	}
	
	/**
	 * 更新部门信息，如果更新上级部门，则必须修改code和所有子部门code
	 * @param id
	 * @param name
	 * @param sort
	 * @param pid  如果为空则不修改
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void update(String id,String name,int sort,String pid) throws ServiceException{
		
		//验证
		if(StringUtils.isBlank(id)){
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_ID_REQUIRED);
		}
		
		if(StringUtils.isBlank(name)){
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_NAME_REQUIRED);
		}
		
		Office office = officeDao.get(id);
		if(office==null){
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_NOT_EXIST);
		}
		//是否修改上级部门
		boolean updateParent = false;
		String oldPid = office.getPid();
		if(StringUtils.isNotBlank(pid)&&!pid.equals(oldPid)){
			updateParent = true;
		}
		String oldCode = office.getCode();
		String code = office.getCode();
		if(updateParent){
			Office parent = officeDao.get(pid);
			if(parent==null){
				throw new ServiceException(ApiCode.PERMISSION_OFFICE_NOT_EXIST);
			}
			Office maxChild = officeDao.findMaxChildByCode(parent.getCode(), true);
			if(maxChild==null){
				code = parent.getCode()+"00";
			}else{
				String childCode = maxChild.getCode().substring(maxChild.getCode().length()-2);
				int cCode = Integer.valueOf(childCode);
				cCode = cCode +1;
				if(cCode<=9){
					code = parent.getCode() +"0"+cCode;
				}else{
					code = parent.getCode() + cCode;
				}
			}
		}
		//更新office信息
		office.setName(name);
		office.setSort(sort);
		int level = code.length() / 2;
		if(updateParent){
			office.setCode(code);
			office.setPid(pid);
			office.setLevel(level);
		}
		officeDao.save(office);
		//更新子部门office信息
		if(updateParent){
			List<Office> children = officeDao.findChildren(oldCode);
			if(children!=null){
				for(Office child:children){
					String childCode = child.getCode();
					childCode = childCode.replaceFirst(oldCode, code);
					int childLevel = childCode.length()/2;
					child.setCode(childCode);
					child.setLevel(childLevel);
				}
				officeDao.save(children);
			}
		}
		
	}
	
	@Transactional(readOnly = false)
	public void delete(String officeid){
		Office office = officeDao.get(officeid);
		if(office!=null){
			officeDao.delAllByCode(office.getCode());
		}
	}
	
}
