package com.graphics.modules.msg.dao;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.msg.entity.SpotFabulous;
import com.graphics.modules.user.entity.StudentUser;
import org.springframework.stereotype.Repository;

@Repository
public class SpotFabulousDao extends BaseDao<SpotFabulous>{


    public SpotFabulous findSpotfabulous(SpotFabulous spotFabulous) {

        return getByHql("from SpotFabulous where msgId = :p1 and spotName = :p2 and delFlag = :p3",
                new Parameter(spotFabulous.getMsgId(), spotFabulous.getSpotName(),spotFabulous.DEL_FLAG_NORMAL));
    }
}
