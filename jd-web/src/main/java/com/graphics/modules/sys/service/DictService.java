package com.graphics.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.sys.dao.DictDao;
import com.graphics.modules.sys.entity.Dict;

/**
 * 字典Service
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService {

	@Autowired
	private DictDao dictDao;
	public Dict get(String id) {
		return dictDao.get(id);
	}
	
	public Page<Dict> find(Page<Dict> page, Dict dict) {
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(dict.getType())){
			dc.add(Restrictions.eq("type", dict.getType()));
		}
		
		dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort")).addOrder(Order.desc("id"));
		return dictDao.find(page, dc);
	}
	
	public List<String> findTypeList(){
		return dictDao.findTypeList();
	}
	
	@Transactional(readOnly = false)
	public void save(Dict dict) {
		dictDao.save(dict);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		dictDao.deleteById(id);
	}

	public List<Dict> getDictList(String type) {
		Map<String, List<Dict>> dictMap = Maps.newHashMap();
		
		for (Dict dict : dictDao.findAllList()){
			List<Dict> dictList = dictMap.get(dict.getType());
			if (dictList != null){
				dictList.add(dict);
			}else{
				dictMap.put(dict.getType(), Lists.newArrayList(dict));
			}
		}
		
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	public String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}

	public String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public String getDictLabel(Integer value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value != null && value.toString().equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public Page<Dict> search(Page<Dict> page,Dict dict){
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(dict.getRemark())){
			dc.add(Restrictions.like("remark","%" + StringEscapeUtils.unescapeHtml4(dict.getRemark()) + "%"));
		}
		if(StringUtils.isNotBlank(dict.getType())){
			dc.add(Restrictions.eq("type",StringEscapeUtils.unescapeHtml4(dict.getType())));
		}
		dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		page = dictDao.find(page, dc);
		return page;
	}
	
	@Transactional(readOnly = false)
	public void update(String id,String label,String value,String remark,String type,Integer sort) throws ServiceException {
		if(StringUtils.isBlank(label) || StringEscapeUtils.unescapeHtml4(label).length() > 100){
			throw new ServiceException(ApiCode.PERMISSION_DICT_LABEL_VERIFY);
		}
		if(StringUtils.isBlank(value) || value.length() > 100){
			throw new ServiceException(ApiCode.PERMISSION_DICT_VALUE_VERIFY);
		}
		if(StringUtils.isBlank(type) || type.length() > 100){
			throw new ServiceException(ApiCode.PERMISSION_DICT_TYPE_VERIFY);
		}
		if(StringUtils.isNotBlank(remark) && StringEscapeUtils.unescapeHtml4(remark).length() > 255){
			throw new ServiceException(ApiCode.INFO_CONFIG_REMARK_VERIFY);
		}
		Dict dict = get(id);
		dict.setLabel(StringEscapeUtils.unescapeHtml4(label));
		dict.setRemark(StringEscapeUtils.unescapeHtml4(remark));
		dict.setType(type);
		dict.setValue(value);
		dict.setSort(sort);
		dictDao.save(dict);
	}
	
	@Transactional(readOnly = false)
	public void create(String label,String value,String remark,String type,Integer sort) throws ServiceException {
		if(StringUtils.isBlank(label) || StringEscapeUtils.unescapeHtml4(label).length() > 100){
			throw new ServiceException(ApiCode.PERMISSION_DICT_LABEL_VERIFY);
		}
		if(StringUtils.isBlank(value) || value.length() > 100){
			throw new ServiceException(ApiCode.PERMISSION_DICT_VALUE_VERIFY);
		}
		if(StringUtils.isBlank(type) || type.length() > 100){
			throw new ServiceException(ApiCode.PERMISSION_DICT_TYPE_VERIFY);
		}
		if(StringUtils.isNotBlank(remark) && StringEscapeUtils.unescapeHtml4(remark).length() > 255){
			throw new ServiceException(ApiCode.INFO_CONFIG_REMARK_VERIFY);
		}
		Dict dict = new Dict();
		dict.setLabel(StringEscapeUtils.unescapeHtml4(label));
		dict.setRemark(StringEscapeUtils.unescapeHtml4(remark));
		dict.setType(type);
		dict.setValue(value);
		dict.setSort(sort);
		dictDao.save(dict);
	}
}
