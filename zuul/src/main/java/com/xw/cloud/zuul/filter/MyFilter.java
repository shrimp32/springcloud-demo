package com.xw.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 夏玮
 * 2018/3/8 14:35
 * zuul过滤器的例子
 * 获取request中的token参数
 */
//@Component
public class MyFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);

    /**
     *  filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     - pre：路由之前
     - routing：路由之时
     - post： 路由之后
     - error：发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filterOrder：过滤的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(accessToken == null) {
            log.warn("token is empty");
            //通过ctx.setSendZuulResponse(false)令zuul过滤该请求，不对其进行路由，
            ctx.setSendZuulResponse(false);
            //通过ctx.setResponseStatusCode(401)设置了其返回的错误码
            ctx.setResponseStatusCode(401);
            //还可以通过ctx.setResponseBody(body)对返回body内容进行编辑等。
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}

        }
        log.info("ok");
        return null;
    }
}
