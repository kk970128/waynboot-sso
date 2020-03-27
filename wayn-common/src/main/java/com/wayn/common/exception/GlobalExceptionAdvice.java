package com.wayn.common.exception;

import com.wayn.common.base.BaseController;
import com.wayn.common.util.R;
import com.wayn.common.util.http.HttpUtil;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionAdvice extends BaseController {

    /**
     * 处理登陆认证异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public Object handleAuthorizationException(AuthenticationException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    /**
     * 处理密码错误异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    public Object handleIncorrectCredentialsException(IncorrectCredentialsException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        return R.error("账号或密码不正确");
    }


    /**
     * 处理为授权异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(ShiroException.class)
    public Object handleShiroException(ShiroException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        if (HttpUtil.isAjax(request)) {
            return R.error(e.getMessage());
        }
        return new ModelAndView("error/unauth");
    }

    /**
     * 处理自定义异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler({BusinessException.class})
    public Object handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        if (HttpUtil.isAjax(request)) {
            return R.error(e.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", e.getMessage());
        return new ModelAndView("error/500", map);
    }

    @ExceptionHandler({Exception.class})
    public Object handleException(Exception e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        if (HttpUtil.isAjax(request)) {
            return R.error("服务器内部错误！");
        }
        return new ModelAndView("error/500");
    }
}
