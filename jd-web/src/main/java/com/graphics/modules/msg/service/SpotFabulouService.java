package com.graphics.modules.msg.service;

import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.msg.dao.SpotFabulousDao;
import com.graphics.modules.msg.entity.SpotFabulous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class SpotFabulouService extends BaseService{

    @Autowired
    private SpotFabulousDao fabulousDao;

    @Transactional(readOnly = false)
    public void save(SpotFabulous spotFabulous) {
        if (StringUtils.isEmpty(spotFabulous.getMsgId())) {
            throw new ServiceException(ApiCode.MSG_COMMENT_ID_NOT_NULL);
        }else if(StringUtils.isEmpty(spotFabulous.getSpotName())) {
            throw new ServiceException(ApiCode.SPOT_COMMENT_NAME_NOT_NULL);
        }
        SpotFabulous spot = fabulousDao.findSpotfabulous(spotFabulous);
        if(spot != null){
            spot.setSpotStatus(spot.getSpotStatus() == 1 ? 0:1);
            fabulousDao.save(spot);
        }else{
            spotFabulous.setSpotStatus(0);
            fabulousDao.save(spotFabulous);
        }

    }
}
