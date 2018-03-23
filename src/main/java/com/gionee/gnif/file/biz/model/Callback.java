package com.gionee.gnif.file.biz.model;

/**
 * Created by yeqy on 2017/6/7.
 */
public class Callback {
    private String url;
    private String param;

    public Callback(String url, String param) {
        this.url = url;
        this.param = param;
    }

    public Callback() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
