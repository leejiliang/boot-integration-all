package com.uk.bootintegrationall.springmvc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description TODO
 */
public class ClientException extends BasicException {

    private static Logger log = LoggerFactory.getLogger(ClientException.class);
    /**
     * 资源不存在
     * 404 Not Found
     * @param clientMessage 客户端提示信息
     * @return
     */
    public static BasicException resourceNotFound(String clientMessage){
        log.error("资源不存在：{}", clientMessage);
        throw new BasicException(404, "404", clientMessage, "Not Found - 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。");
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum) {
        super(clientExceptionEnum);
    }
}
