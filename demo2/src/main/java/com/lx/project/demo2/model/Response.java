package com.lx.project.demo2.model;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private static final String OK = "ok";
    private static final String ERROR = "error";

    private Object data;
    private int count;
    private int code;
    private String msg;

    public Response success() {
        this.code = 0;
        this.msg = "操作成功";
        return this;
    }

    public Response success(String message) {
        this.code = 0;
        this.msg = message;
        return this;
    }

    public Response success(Object data) {
        this.code = 0;
        this.msg = "操作成功";
        if(data instanceof ArrayList<?>){
            List<?> list = (ArrayList)data;
            if(data == null || list.size() == 0){
                this.data = null;
            }else{
                this.data = data;
            }
        }else{
            this.data = data;
        }
        return this;
    }
    public Response failure(Object data) {
        this.code = 1;
        this.msg = "操作失败";
        if(data instanceof ArrayList<?>){
            List<?> list = (ArrayList)data;
            if(data == null || list.size() == 0){
                this.data = null;
            }else{
                this.data = data;
            }
        }else{
            this.data = data;
        }
        return this;
    }

    public Response failure() {
        this.code = 1;
        this.msg = "操作失败";
        return this;
    }

    public Response failure(String message) {
        this.code = 1;
        this.msg = message;
        return this;
    }

    //弱性提示异常，有确认/取消操作 add 20171204
    /**
     * code:0 表示请求成功
     * code:1 表示请求异常，页面弹窗报错
     * code:2 表示异常捕获后弹出可选择的弹出框
     **/
    public Response ConfirmException(int code,String message) {
        this.code = code;
        this.msg = message;
        return this;
    }


    public Object getData() {
        return data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}