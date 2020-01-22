package com.github.hairless.plink.web.handler;

import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/1/16
 */

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handlerNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return new Result(ResultCode.FAILURE, fieldErrors.get(0).getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handlerAllException(Exception exception) {
        return new Result(ResultCode.EXCEPTION, exception);
    }
}
