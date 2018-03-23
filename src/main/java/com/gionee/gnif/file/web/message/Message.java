package com.gionee.gnif.file.web.message;

import java.io.Serializable;
import java.util.Map;

public class Message implements Serializable {

    private static final long serialVersionUID = -5984608183764665778L;
    //是否系统异常
    private Boolean isSuccess;
    //返回信息
    private String message;
    //结果是否成功
    private Boolean isOk;
    //权限验证通过
    private Boolean auth;
    //文件是否重复
    private Boolean notRepeat;
    //文件参数
    private Map<String, Object> attributes;// 其他参数

    private String callback;


    public Message(Boolean isSuccess, String message, Boolean isOk, Boolean isReceiveMerged, Map<String, Object> attributes) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.isOk = isOk;
        this.attributes = attributes;
    }

    public Message() {
    }

    public Message(Boolean isSuccess, String message, Boolean isOk) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.isOk = isOk;
    }


    public Message(Boolean isSuccess, Boolean isOk, Boolean auth, Boolean notRepeat, String msg) {
        this.isSuccess = isSuccess;
        this.isOk = isOk;
        this.auth = auth;
        this.notRepeat = notRepeat;
        this.message = msg;
    }

    public Message(Boolean isSuccess, Boolean isOk, Boolean auth, Boolean notRepeat) {
        this.isSuccess = isSuccess;
        this.isOk = isOk;
        this.auth = auth;
        this.notRepeat = notRepeat;
    }

    public Message(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public static Message error(String message, String callback) {
        Message m = new Message();
        m.setAuth(false);
        m.setIsOk(false);
        m.setIsSuccess(false);
        m.setNotRepeat(true);
        m.setMessage(message);
        m.setCallback(callback);
        return m;
    }

    public static Message error(String message) {
        Message m = new Message();
        m.setAuth(false);
        m.setIsOk(false);
        m.setIsSuccess(false);
        m.setNotRepeat(true);
        m.setMessage(message);
        return m;
    }

    public static Message repeatUpload(String callback, Map<String, Object> map) {
        Message m = new Message();
        m.setAuth(false);
        m.setIsOk(false);
        m.setIsSuccess(true);
        m.setNotRepeat(false);
        m.setMessage("文件已存在");
        m.setCallback(callback);
        m.setAttributes(map);
        return m;
    }

    public static Message repeatUpload(Map<String, Object> map) {
        Message m = new Message();
        m.setAuth(false);
        m.setIsOk(false);
        m.setIsSuccess(true);
        m.setNotRepeat(false);
        m.setMessage("文件已存在");
        m.setAttributes(map);
        return m;
    }

    public static Message pass(String callback) {
        Message m = new Message();
        m.setAuth(true);
        m.setIsOk(true);
        m.setIsSuccess(true);
        m.setNotRepeat(true);
        m.setCallback(callback);
        return m;
    }

    public static Message pass(String callback, Map<String, Object> map) {
        Message m = new Message();
        m.setAuth(true);
        m.setIsOk(true);
        m.setIsSuccess(true);
        m.setNotRepeat(true);
        m.setCallback(callback);
        m.setAttributes(map);
        return m;
    }

    public static Message pass(String callback, Map<String, Object> map, String message) {
        Message m = new Message();
        m.setAuth(true);
        m.setIsOk(true);
        m.setIsSuccess(true);
        m.setNotRepeat(true);
        m.setCallback(callback);
        m.setAttributes(map);
        m.setMessage(message);
        return m;
    }

    public static Message pass() {
        Message m = new Message();
        m.setAuth(true);
        m.setIsOk(true);
        m.setIsSuccess(true);
        m.setNotRepeat(true);
        return m;
    }

    public Object get(String key) {
        return attributes.get(key);
    }

    @Override
    public String toString() {
        return "Message{" +
                "isSuccess=" + isSuccess +
                ", message='" + message + '\'' +
                ", isOk=" + isOk +
                ", auth=" + auth +
                ", notRepeat=" + notRepeat +
                ", attributes=" + attributes +
                ", callback='" + callback + '\'' +
                '}';
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
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

    public void setIsOk(Boolean ok) {
        isOk = ok;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public Boolean getNotRepeat() {
        return notRepeat;
    }

    public void setNotRepeat(Boolean notRepeat) {
        this.notRepeat = notRepeat;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
