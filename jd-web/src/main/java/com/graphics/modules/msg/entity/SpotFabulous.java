package com.graphics.modules.msg.entity;


import com.graphics.common.persistence.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 点赞表
 */
@Entity
@Table(name = "class_spot_fabulous")
public class SpotFabulous extends IdEntity<SpotFabulous>{

    private String msgId;
    private String spotName;
    private int spotStatus;

    public int getSpotStatus() {
        return spotStatus;
    }

    public void setSpotStatus(int spotStatus) {
        this.spotStatus = spotStatus;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
