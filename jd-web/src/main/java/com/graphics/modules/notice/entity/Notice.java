package com.graphics.modules.notice.entity;

import com.graphics.common.persistence.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "class_notice")
public class Notice extends IdEntity<Notice>{

    private String title;
    private String content;
    private String status;
    private String noticeType;
    private String files;
    private List<NoticeFiles>noticeFiles;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    public List<NoticeFiles> getNoticeFiles() {
        return noticeFiles;
    }

    public void setNoticeFiles(List<NoticeFiles> noticeFiles) {
        this.noticeFiles = noticeFiles;
    }
}
