package com.pzm.netroiddemo.bean;

import java.io.Serializable;

/**
 * Created by pzm on 2018/7/18
 * @author mk0202-01023
 */

public class ProgramInfoData implements Serializable {
    private static final long	serialVersionUID		= 1L;

    private int id;
    private String name;
    private int iconId;
    private String url;
    private String filePath;
    private long fileSize;

    public ProgramInfoData(int id, String name,int iconId,  String url) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
        this.url = url;
    }

    public ProgramInfoData(int id, String name,int iconId,  String url, String filePath) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
        this.url = url;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
