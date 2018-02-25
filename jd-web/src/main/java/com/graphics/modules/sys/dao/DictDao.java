package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@Repository
public class DictDao extends BaseDao<Dict> {

	public List<Dict> findAllList(){
		return find("from Dict where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}

	public List<String> findTypeList(){
		return find("select type from Dict where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
	}

}
