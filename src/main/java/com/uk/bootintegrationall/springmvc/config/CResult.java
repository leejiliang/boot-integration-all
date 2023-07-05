package com.uk.bootintegrationall.springmvc.config;

import com.uk.bootintegrationall.springmvc.exception.BasicException;
import com.uk.bootintegrationall.springmvc.exception.ExceptionEnumInterface;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description MVC返回结果统一封装
 */
public class CResult<T> implements Serializable {
    /**
     * 成功
     */
    public static final String SUCCESS = "1";
    /**
     * 失败
     */
    public static final String FAIL = "0";
    private static final long serialVersionUID = -212122772006061476L;

    /**
     * 状态码
     */
    private String code;
    /**
     * 客户端状态码对应信息
     */
    private String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    private String serverMessage;

    /**
     * 数据
     */
    private T data;

    /**
     * 数据
     */
    private Map dataMap = new HashMap();

    /**
     * 时间戳
     */
    private LocalDateTime timestamp = LocalDateTime.now();

    public CResult() {
    }
    public CResult(String code) {
        this.code = code;
    }
    public CResult(String code, String clientMessage, String serverMessage) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }
    public CResult(String code, String clientMessage, String serverMessage, T t) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
        this.data = t;
    }


    /**
     * 返回成功
     * @return
     */
    public static CResult<Object> ofSuccess() {
        return new CResult(CResult.SUCCESS);
    }
    /**
     * 返回成功,带数据
     * @return
     */
    public static <T> CResult<T> ofSuccess(T t) {
        return new CResult(CResult.SUCCESS, null, null, t);
    }

    /**
     * 返回失败
     * @return
     */
    public static CResult ofFail() {
        return new CResult(CResult.FAIL);
    }
    /**
     * 返回失败，带数据
     * @return
     */
    public static <T> CResult<T> ofFail(T t) {
        return new CResult(CResult.FAIL, null, null, t);
    }

    /**
     * 只返回失败信息，不抛额异常
     * @return
     */
    public static CResult ofFail(ExceptionEnumInterface enumInterface) {
        return  new CResult(enumInterface.getCode(), enumInterface.getClientMessage(), enumInterface.getServerMessage(), null);
    }

    /**
     * 只返回失败信息，不抛额异常
     * @return
     */
    public static CResult ofFail(BasicException basicException) {
        return  new CResult(basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), null);
    }

    /**
     * 400 Bad Request
     * @param clientMessage 客户端显示错误
     * @param serverMessage 服务端错误
     * @return
     */
    public static CResult ofFailByBadRequest(String clientMessage, String serverMessage) {
        return  new CResult("400", clientMessage, HttpStatus.BAD_REQUEST.getReasonPhrase() + " - " + serverMessage);
    }

    /**
     * 404 Not Found
     * @param url 访问的资源地址
     * @return
     */
    public static CResult ofFailByNotFound(String url) {
        return  new CResult("404", "当前请求资源不存在，请稍后再试", HttpStatus.NOT_FOUND.getReasonPhrase() + " - 目标资源资源不存在：" + url);
    }

    /**
     * 405 Method Not Allowed
     * @param url 访问的资源地址
     * @return
     */
    public static CResult ofFailByMethodNotAllowed(String url) {
        return  new CResult("405", "当前资源请求方式错误，请稍后再试", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase() + " - 目标资源资源不存在：" + url);
    }
}
