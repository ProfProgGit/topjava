package ru.javawebinar.topjava.web;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.DATETIME_DUPLICATION_MESSAGE;
import static ru.javawebinar.topjava.util.ValidationUtil.EMAIL_DUPLICATION_MESSAGE;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        Throwable rootCause;
        Throwable cause = e.getCause();
        if ((cause != null) && (cause instanceof ConstraintViolationException)) {
            rootCause = ValidationUtil.getRootCause(e);
            String message = rootCause.getMessage();
            boolean emailUniqueViolated = message.contains("users_unique_email_idx");
            boolean dateTimeUniqueViolated = message.contains("meals_unique_user_datetime_idx");
            if (emailUniqueViolated || dateTimeUniqueViolated) {
                log(req.getRequestURL(), rootCause, true);
                return new ErrorInfo(req.getRequestURL(), e, emailUniqueViolated ?
                        EMAIL_DUPLICATION_MESSAGE : DATETIME_DUPLICATION_MESSAGE);
            }
        }
        return logAndGetErrorInfo(req, e, true);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public List<ErrorInfo> handleException(HttpServletRequest req, MethodArgumentNotValidException e) {
        return extractErrorsFromBindingResult(e.getBindingResult(), req.getRequestURL(), e);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public List<ErrorInfo> handleException(HttpServletRequest req, BindException e) {
        return extractErrorsFromBindingResult(e.getBindingResult(), req.getRequestURL(), e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private static List<ErrorInfo> extractErrorsFromBindingResult(BindingResult result, CharSequence url, Throwable e) {
        List<ErrorInfo> list = new ArrayList<>();
        Throwable rootCause = ValidationUtil.getRootCause(e);
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (!msg.startsWith(fe.getField())) {
                        msg = fe.getField() + ' ' + msg;
                    }
                    list.add(new ErrorInfo(url, rootCause, msg));
                }
        );
        log(url, rootCause, true);
        return list;
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        log(req.getRequestURL(), rootCause, logException);
        return new ErrorInfo(req.getRequestURL(), rootCause);
    }

    private static void log(CharSequence url, Throwable rootCause, boolean logException) {
        if (logException) {
            LOG.error("Exception at request " + url, rootCause);
        } else {
            LOG.warn("Exception at request " + url + ": " + rootCause.toString());
        }
    }
}