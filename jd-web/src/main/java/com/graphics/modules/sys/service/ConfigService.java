package com.graphics.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.sys.dao.ConfigDao;
import com.graphics.modules.sys.entity.Config;
import com.graphics.modules.sys.entity.Dict;
/**
 * 系统配置Service
 * @author hg
 *
 */
@Service
@Transactional(readOnly = true)
public class ConfigService extends BaseService {
	@Autowired
	private ConfigDao configDao;
	@Autowired
	private DictService dictService;
	public Config get(String id) {
		return configDao.get(id);
	}
	
	public Page<Config> find(Page<Config> page, Config config) {
		DetachedCriteria dc = configDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Config.FIELD_DEL_FLAG, Config.DEL_FLAG_NORMAL));
		page = configDao.find(page, dc);
		List<Config> configList = page.getList();
		List<Dict> configTypeDict = dictService.getDictList("config_type");
		for(int i=0;i<configList.size();i++){
			if(StringUtils.isNotBlank(configList.get(i).getType())){
				for(int j=0;j<configTypeDict.size();j++){
					if((configList.get(i).getType()).equals(configTypeDict.get(j).getValue())){
						configList.get(i).setTypeLabel(configTypeDict.get(j).getLabel());
						break;
					}
				}
			}
		}
		return page;
	}
	
	@Transactional(readOnly = false)
	public void delete(String configId) {
		configDao.deleteById(configId);
	}
	
	@Transactional(readOnly = false)
	public void create(String type,String key,String name,String def,String value,String remark)
			throws ServiceException {
		if(StringUtils.isBlank(name) || name.length() > 50){
			throw new ServiceException(ApiCode.INFO_CONFIG_NAME_VERIFY);	
		}
		if(StringUtils.isBlank(type) || StringUtils.isBlank(dictService.getDictLabel(type, "config_type",""))){
			throw new ServiceException(ApiCode.INFO_CONFIG_TYPE_VERIFY);
		}
		if(StringUtils.isBlank(key) || key.length() > 50){
			throw new ServiceException(ApiCode.INFO_CONFIG_KEY_VERIFY);		
		}else{
			DetachedCriteria dc = configDao.createDetachedCriteria();
			dc.add(Restrictions.eq(Config.FIELD_DEL_FLAG, Config.DEL_FLAG_NORMAL));
			dc.add(Restrictions.eq("key",key));
			List<Config> cf = configDao.find(dc);
			if(cf.size() > 0){
				throw new ServiceException(ApiCode.INFO_CONFIG_KEY_EXIST);
			}
		}
		
		if(StringUtils.isNotBlank(def) && def.length() > 100){
			throw new ServiceException(ApiCode.INFO_CONFIG_DEF_VERIFY);		
		}
		if(StringUtils.isBlank(value) || value.length() > 100){
			throw new ServiceException(ApiCode.INFO_CONFIG_VALUE_VERIFY);		
		}
		if(StringUtils.isNotBlank(remark) && remark.length() > 255){
			throw new ServiceException(ApiCode.INFO_CONFIG_REMARK_VERIFY);		
		}
		Config config = new Config();
		config.setName(name);
		config.setType(type);
		config.setKey(key);
		config.setValue(value);
		config.setDef(def);
		config.setRemark(remark);
		configDao.save(config);
	}
	
	@Transactional(readOnly = false)
	public void update(String id,String type,String key,String name,String def,String value,String remark) throws ServiceException {
		Config config = configDao.get(id);
		if(StringUtils.isBlank(name) || name.length() > 50){
			throw new ServiceException(ApiCode.INFO_CONFIG_NAME_VERIFY);	
		}
		if(StringUtils.isBlank(type) || StringUtils.isBlank(dictService.getDictLabel(type, "config_type",""))){
			throw new ServiceException(ApiCode.INFO_CONFIG_TYPE_VERIFY);
		}
		if(StringUtils.isBlank(key) || key.length() > 50){
			throw new ServiceException(ApiCode.INFO_CONFIG_KEY_VERIFY);		
		}else{
			DetachedCriteria dc = configDao.createDetachedCriteria();
			dc.add(Restrictions.eq(Config.FIELD_DEL_FLAG, Config.DEL_FLAG_NORMAL));
			dc.add(Restrictions.eq("key",key));
			List<Config> cf = configDao.find(dc);
//			boolean a = StringUtils.isNotBlank(cf.get(0).getKey()) && key.equals(config.getKey());
			if(cf.size() > 1){
				if(cf.size() != 1 || !(StringUtils.isNotBlank(cf.get(0).getKey()) && key.equals(config.getKey()))){
					throw new ServiceException(ApiCode.INFO_CONFIG_KEY_EXIST);
				}
			}
		}
		
		if(StringUtils.isNotBlank(def) && def.length() > 100){
			throw new ServiceException(ApiCode.INFO_CONFIG_DEF_VERIFY);		
		}
		if(StringUtils.isBlank(value) || value.length() > 100){
			throw new ServiceException(ApiCode.INFO_CONFIG_VALUE_VERIFY);		
		}
		if(StringUtils.isNotBlank(remark) && remark.length() > 255){
			throw new ServiceException(ApiCode.INFO_CONFIG_REMARK_VERIFY);		
		}
		config.setName(name);
		config.setType(type);
		config.setKey(key);
		config.setValue(value);
		config.setDef(def);
		config.setRemark(remark);
		configDao.save(config);
	}
	
	/**
	 * 查询所有已使用的Key值
	 * @return
	 */
	public List<String> keyList() {
		List<Config> configs = configDao.findAll();
		List<String> keyList = new ArrayList<String>();
		for(Config config : configs){
			keyList.add(config.getKey());
		}
		return keyList;
	}
}
