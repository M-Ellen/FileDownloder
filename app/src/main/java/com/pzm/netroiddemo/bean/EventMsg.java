package com.pzm.netroiddemo.bean;

/**
 * Created by pzm on 2018/7/18
 */

public class EventMsg {


    private int msgType = -1;
    private int percent = 0;
    private ProgramInfoData programInfoData;

    public EventMsg(int msgType, ProgramInfoData programInfoData) {
        this.msgType = msgType;
        this.programInfoData = programInfoData;
    }

    public EventMsg(int msgType, ProgramInfoData programInfoData, int percent) {
        this.msgType = msgType;
        this.programInfoData = programInfoData;
        this.percent = percent;
    }

    public int getMsgTypeMode() {
        return msgType;
    }

    public int getPercent(){
        return percent;
    }

//    public void setMode(int mode) {
//        this.mode = mode;
//    }

    public ProgramInfoData getProgramInfoData() {
        return programInfoData;
    }

    public void setProgramInfoData(ProgramInfoData programInfoData) {
        this.programInfoData = programInfoData;
    }
}
