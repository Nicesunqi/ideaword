package com.graphics.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graphics.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.sys.dao.MenuDao;
import com.graphics.modules.sys.entity.Menu;

/**
 * 菜单Service
 */
@Service
@Transactional(readOnly = true)
public class MenuService extends BaseService {

	@Autowired
	private MenuDao menuDao;
	
	public Menu get(String id) {
		return menuDao.get(id);
	}
	
	public List<Menu> findAll(){
		return menuDao.findAll();
	}
	
	/**
	 * 返回树形结构的menu，最上面的节点为pid=0的节点
	 * @return
	 */
	public List<Menu> allListTree(){
		List<Menu> menuList = Lists.newArrayList();
		List<Menu> all = menuDao.findAll("level asc,sort asc");
		Map<String, Menu> menuMap = new HashMap<String, Menu>();
		for(Menu menu:all){
			menuMap.put(menu.getId(), menu);
		}
		for(Menu menu:all){
			if("0".equals(menu.getPid())){
				menuList.add(menu);
			}else{
				Menu parent = menuMap.get(menu.getPid());
				if(parent!=null){
					parent.addChild(menu);
				}
			}
		}
		return menuList;
	}
	
	
	@Transactional(readOnly = false)
	public void save(Menu menu) {
		menuDao.save(menu);
	}
	
	/**
	 * 新增菜单
	 * @param pid	上级部门
	 * @param sort	排序(从小到大)
	 * @param name	名称
	 * @param permission	权限标识
	 * @param isShow	是否显示
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void create(String pid,int sort,String name,String permission,int isShow) throws ServiceException{
		
		if(StringUtils.isBlank(pid)){
			pid="0";
		}
		
		//验证
		if(StringUtils.isBlank(name)){
			throw new ServiceException(ApiCode.PERMISSION_MENU_NAME_REQUIRED);
		}
		if(StringUtils.isBlank(permission)){
			throw new ServiceException(ApiCode.PERMISSION_MENU_CODE_REQUIRED);
		}
		
		//设置code值
		String code = "";
		Menu parent = menuDao.get(pid);
		if(parent!=null){
			code = parent.getCode();
		}
		String maxCode = menuDao.findMaxCodeById(pid);
		if(StringUtils.isBlank(maxCode)){
			code = code+"00";
		}else{
			String childCode = maxCode.substring(maxCode.length()-2);
			int cCode = Integer.valueOf(childCode);
			cCode = cCode +1;
			if(cCode<=9){
				code = code +"0"+cCode;
			}else{
				code = code + cCode;
			}
		}
		
		int level = code.length() / 2;
		
		Menu menu = new Menu();
		menu.setCode(code);
		menu.setLevel(level);
		menu.setName(name);
		menu.setPid(pid);
		menu.setSort(sort);
		menu.setIsShow(isShow);
		menu.setPermission(permission);
		
		menuDao.save(menu);
		
	}
	
	/**
	 * 更新菜单
	 * @param id
	 * @param name
	 * @param sort
	 * @param permission
	 * @param isShow
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void update(String id,String name,int sort,String permission,int isShow) throws ServiceException{
		
		//验证
		if(StringUtils.isBlank(id)){
			throw new ServiceException(ApiCode.PERMISSION_MENU_ID_REQUIRED);
		}
		
		if(StringUtils.isBlank(name)){
			throw new ServiceException(ApiCode.PERMISSION_MENU_NAME_REQUIRED);
		}
		
		Menu menu = menuDao.get(id);
		if(menu==null){
			throw new ServiceException(ApiCode.PERMISSION_MENU_CODE_REQUIRED);
		}
		
		//更新menu信息
		menu.setName(name);
		menu.setSort(sort);
		menu.setPermission(permission);
		menu.setIsShow(isShow);
		
		menuDao.save(menu);
	}
	
	/**
	 * 删除菜单，同时删除所有子菜单
	 * @param menuId
	 */
	@Transactional(readOnly = false)
	public void delete(String menuId){
		Menu menu = menuDao.get(menuId);
		if(menu!=null){
			menuDao.delAllByCode(menu.getCode());
		}
	}
	
}
