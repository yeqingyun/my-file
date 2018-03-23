package com.gionee.gnif.file.web.message;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = -5984608183764665778L;
    //是否系统异常
    private Boolean isSuccess;
    //返回信息
    private String message;
    //结果是否成功
    private Boolean isOk;

    private Boolean isReceiveMerged;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsOk() {
        return isOk;
    }

    public void setIsOk(Boolean isOk) {
        this.isOk = isOk;
    }

    public Boolean getIsReceiveMerged() {
        return isReceiveMerged;
    }

    public void setIsReceiveMerged(Boolean isReceiveMerged) {
        this.isReceiveMerged = isReceiveMerged;
    }

    public Message(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
