package com.xw.cloud.zuul.filter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : 夏玮
 * Created on 2018/9/27 11:02
 * 后置过滤器，根据错误码来进行统一异常处理
 */
//@Component
public class PostFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PostFilter.class);

    @Override
    public String filterType() {
        //后置过滤器
        return "post";
    }

    @Override
    public int filterOrder() {
        //优先级，数字越大，优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
//        return RequestContext.getCurrentContext().containsKey("throwable");
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getResponseStatusCode()!=200)
            return true;
        else
            return false;
    }

    @Override
    public Object run() {
        log.info("Start ErrorFilter");
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            int statusCode = ctx.getResponseStatusCode();
            ctx.setResponseBody("Error Filter:system error is "+statusCode);

        } catch (Exception e) {
            String error = "Error during filtering[ErrorFilter]";
            log.error(error,e);
        }
        return null;

    }

}
