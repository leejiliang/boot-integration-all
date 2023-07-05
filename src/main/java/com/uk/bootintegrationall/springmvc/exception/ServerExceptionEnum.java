package com.uk.bootintegrationall.springmvc.exception;

/**
 * @Description TODO
 */
public enum ServerExceptionEnum implements ExceptionEnumInterface {
    SERVER_ERROR(500, "500", "服务器内部错误，请联系网管", "未捕获的未知异常"),
    ;
    /**
     * 响应码
     */
    private int status;
    /**
     * 错误代码
     */
    private String code;

    /**
     * 客户看见的提示信息
     */
    private String clientMessage;
    /**
     * 服务器日志信息
     */
    private String serverMessage;

    ServerExceptionEnum(int status, String code, String clientMessage, String serverMessage){
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getClientMessage() {
        return clientMessage;
    }

    @Override
    public String getServerMessage() {
        return serverMessage;
    }
}
