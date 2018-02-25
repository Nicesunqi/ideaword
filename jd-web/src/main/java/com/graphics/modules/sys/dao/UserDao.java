package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.User;

/**
 * 系统用户DAO接口
 */
@Repository
public class UserDao extends BaseDao<User> {
	
	public List<User> findAllList() {
		return find("from User where delFlag=:p1 ", new Parameter(User.DEL_FLAG_NORMAL));
	}
	
	public User findByAccount(String account){
		return getByHql("from User where account = :p1 and delFlag = :p2", new Parameter(account, User.DEL_FLAG_NORMAL));
	}
	
	public User findByToken(String token){
		return getByHql("from User where token = :p1 and delFlag = :p2", new Parameter(token, User.DEL_FLAG_NORMAL));
	}
	
	public int updatePasswordById(String newPassword, String id){
		return update("update User set password=:p1 where id = :p2", new Parameter(newPassword, id));
	}
}
