package com.uk.bootintegrationall.springmvc.exception;

import com.uk.bootintegrationall.springmvc.config.CResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 错误日志模板
     */
    public static final String LOG_ERROR_INFO = "http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}";

    /**
     * 请求对象
     */
    @Resource
    private HttpServletRequest request;

    /**
     * 响应对象
     */
    @Resource
    private HttpServletResponse response;


    /**
     * 全局处理自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {BasicException.class, ServerException.class})
    @ResponseBody
    public CResult<BasicException> clientExceptionDispose(BasicException exception){
        // 设置响应码
        response.setStatus(exception.getStatus());
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, exception.getStatus(), exception.getCode(), exception.getClientMessage(), exception.getServerMessage());
        // 堆栈跟踪
        exception.printStackTrace();

        return CResult.ofFail(exception);
    }

    /**
     * 捕获意料之外的异常Exception
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public CResult<Throwable> otherErrorDispose(Throwable e){
        BasicException serverException = new ServerException(ServerExceptionEnum.SERVER_ERROR);
        this.response.setStatus(serverException.status);
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, serverException.status, serverException.code, serverException.clientMessage, e.getMessage());
        // 堆栈跟踪
        e.printStackTrace();
        serverException.setServerMessage(e.getMessage());
        return CResult.ofFail(serverException);
    }

    /*==========================================================================
        常见自定义异常
    ==========================================================================*/

    /**
     * 400 Bad Request
     * 因发送的请求语法错误,服务器无法正常读取.
     * @param e
     * @return
     */
    @ExceptionHandler(value = {
        BindException.class,
//        ConstraintViolationException.class,
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CResult<Throwable> ValidExceptionDispose(Exception e){
        List<String> messages = new ArrayList<>();
        if (e instanceof BindException) {
            List<ObjectError> list = ((BindException) e).getAllErrors();
            for (ObjectError item : list) {
                messages.add(item.getDefaultMessage());
            }
//        } else if (e instanceof ConstraintViolationException) {
//            for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException)e).getConstraintViolations()) {
//                messages.add(constraintViolation.getMessage());
//            }
        } else if (e instanceof HttpMessageNotReadableException) {
            messages.add("请求参数丢失");
        } else {
            messages.add(((MethodArgumentNotValidException)e).getBindingResult().getFieldError().getDefaultMessage());
        }

        String message = String.join(",", messages);
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, HttpStatus.BAD_REQUEST.value(), "VALIDATION", message, e.getMessage());
        // 堆栈跟踪
        e.printStackTrace();

        return CResult.ofFailByBadRequest(message, e.getMessage());
    }

    /**
     * 404 Not Found
     * 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。
     * 假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。
     * 404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。出现这个错误的最有可能的原因是服务器端没有这个页面。
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public CResult notFound() {
        return CResult.ofFailByNotFound(request.getRequestURL().toString());
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String notFoundPage() {
//        return "404";
//    }

    /**
     * 405 Method Not Allowed
     * 请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
     *
     * 鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public CResult methodNotAllowed() {
        return CResult.ofFailByMethodNotAllowed(request.getRequestURL().toString());
    }


    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
