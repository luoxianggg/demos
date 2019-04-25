package com.lx.project.demo3.model;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private static final String OK = "ok";
    private static final String ERROR = "error";

    public static final int VALIDATE_FAILED = 404;

    public static final int UNAUTHORIZED = 401;
    //未授权
    public static final int  FORBIDDEN = 403;

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

    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    public Response validateFailed(String message) {
        this.code = VALIDATE_FAILED;
        this.msg = message;
        return this;
    }

    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    public Response unauthorized(String message) {
        this.code = UNAUTHORIZED;
        this.msg = "暂未登录或token已经过期";
        this.data = message;
        return this;
    }

    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    public Response forbidden(String message) {
        this.code = FORBIDDEN;
        this.msg = "没有相关权限";
        this.data = message;
        return this;
    }

    /**
     * 参数验证失败使用
     * @param result 错误信息
     */
    public Response validateFailed(BindingResult result) {
        validateFailed(result.getFieldError().getDefaultMessage());
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