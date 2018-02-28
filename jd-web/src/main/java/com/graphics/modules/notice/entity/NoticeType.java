package com.graphics.modules.notice.entity;

import com.graphics.common.persistence.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "class_notice_type")
public class NoticeType extends IdEntity<NoticeType>{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
