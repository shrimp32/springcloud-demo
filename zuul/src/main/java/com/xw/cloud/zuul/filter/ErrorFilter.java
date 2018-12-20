package com.xw.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : 夏玮
 * Created on 2018/9/27 11:30
 */
//@Component
public class ErrorFilter extends ZuulFilter {
    private final Logger log = LoggerFactory.getLogger(ErrorFilter.class);
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() {
        log.info("Start ErrorFilter");
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        if (throwable!=null){
            log.error("this is a ErrorFilter" , throwable);
            ctx.set("sendErrorFilter.ran", true);
            ctx.set("error.exception", throwable.getCause());
        }
//        try {
//            RequestContext ctx = RequestContext.getCurrentContext();
//            Throwable e = ctx.getThrowable();
//            log.error("this is a ErrorFilter : {}", e.getCause().getMessage());
//
//            if(e!=null){
//                // 删除该异常信息,不然在下一个过滤器中还会被执行处理
//                ctx.remove("throwable");
//                ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
//                ctx.setResponseBody("system error");
//            }
//        } catch (Exception ex) {
//            log.error("Exception filtering in custom error filter", ex);
//            ReflectionUtils.rethrowRuntimeException(ex);
//        }
        return null;
    }
}