package com.xing.fileserver.common.web;

import com.xing.fileserver.common.annotation.IgnoreCommonResponse;
import com.xing.fileserver.common.exception.BusinessException;
import com.xing.fileserver.common.model.ResultBean;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@IgnoreCommonResponse
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultBean handleBusinessException(BusinessException ex) {
        return ResultBean.error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultBean handleException(Exception ex) {
        return ResultBean.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultBean handleArgumentException(MethodArgumentNotValidException ex) {

        String tip = "参数不合法";
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (Objects.nonNull(errors) && errors.size() > 0) {
            tip = errors.stream().map(item -> item.getDefaultMessage()).collect(Collectors.joining(";"));
        }

        return ResultBean.error(tip);
    }

}
