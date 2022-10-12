package com.caiqiu.app.utils;

import lombok.Data;

/**
 * 全局统一返回结果类
 */
@Data
public class Result<T> {

    private Integer code;//状态码
    private String msg;//返回消息
    private T result;//返回数据

    /**
     * 私有化构造方法，禁止在其它类创建对象
     */
    private Result() {
    }

    /**
     * 成功执行，不返回数据
     * @return
     */
    public static <T> Result<T> ok() {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.SUCCESS);
        //result.setMsg("执行成功");
        return result;
    }

    /**
     * 成功执行，并返回数据
     * @param data
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.SUCCESS);
        //result.setMsg("执行成功");
        result.setResult(data);
        return result;
    }

    /**
     * 失败
     *
     * @return
     */
    public static <T> Result<T> error() {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.ERROR);
        result.setMsg("执行失败");
        return result;
    }



    /**
     * 设置状态码
     *
     * @param code
     * @return
     */
    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 设置返回消息
     *
     * @param message
     * @return
     */
    public Result<T> msg(String message) {
        this.setMsg(message);
        return this;
    }


}