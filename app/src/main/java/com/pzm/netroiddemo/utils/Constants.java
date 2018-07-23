package com.pzm.netroiddemo.utils;

/**
 * Created by pzm on 2018/7/18
 */

public interface Constants {

    String DOWNLOAD_DATA	= "download_data";

    /**
     *  用户操作状态
     */

    int	DOWNLOAD_START	= 1;
    int	DOWNLOAD_PAUSE	= 2;
    int	DOWNLOAD_RESUME	= 3;
    int	DOWNLOAD_DELETE	= 4;


    /**
     *  下载监听状态
     */

    int STATUS_PRE_EXECUTE = 5;
    int STATUS_PROGRESS_CHANGE = 6;
    int STATUS_CANCEL = 7;
    int STATUS_SUCCESS = 8;
    int STATUS_ERROR = 9;
    int STATUS_FINISH = 10;


    /**
     *  进度条状态
     */

    int PROGRESS_PRE = 11;
    int PROGRESS_PAUSE = 12;
    int PROGRESS_CONTINUED = 13;


}
