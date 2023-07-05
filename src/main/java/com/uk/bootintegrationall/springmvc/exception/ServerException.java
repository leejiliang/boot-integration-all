package com.uk.bootintegrationall.springmvc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description TODO
 */
public class ServerException extends BasicException {
    private static Logger log = LoggerFactory.getLogger(ServerException.class);
    public static final String clientMessage = "服务器内部错误";

    public ServerException(ServerExceptionEnum serverExceptionEnum) {
        super(serverExceptionEnum);
    }

    public static BasicException exception(String serverMessage){
        log.error("服务器内部错误：{}", serverMessage);
        throw new BasicException(500, "500500", clientMessage, serverMessage);
    }

    /**
     * 服务之间方法调用 参数错误
     * @param serverMessage
     * @return
     */
    public static BasicException methodParamError(String serverMessage){
        log.error("服务器内部方法传参错误：{}", serverMessage);
        throw new BasicException(400, "500400", clientMessage, serverMessage);
    }
}
