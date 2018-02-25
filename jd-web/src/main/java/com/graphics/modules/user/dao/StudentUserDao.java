package com.graphics.modules.user.dao;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Page;
import com.graphics.common.persistence.Parameter;
import com.graphics.common.utils.StringUtils;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.user.entity.StudentUser;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentUserDao extends BaseDao<StudentUser>{

    public StudentUser findUserByStuNo(String stuNo) {
            return getByHql("from StudentUser where stuNo = :p1 and delFlag = :p2", new Parameter(stuNo, StudentUser.DEL_FLAG_NORMAL));
    }

    public Page<StudentUser> findUserList(Page<StudentUser> page, StudentUser user) {
        DetachedCriteria dc = createDetachedCriteria();
        if (StringUtils.isNotEmpty(user.getName())) {
            dc.add(Restrictions.like("name", "%" + user.getName() + "%"));
        }
        if(StringUtils.isNotEmpty(user.getStuNo())){
            dc.add(Restrictions.like("stuNo", "%" + user.getStuNo() + "%"));
        }
        if(StringUtils.isNotEmpty(user.getId())){
            dc.add(Restrictions.eq("id", user.getId()));
        }
        dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
       page = find(page,dc);
       return page;
    }
}
