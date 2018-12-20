package com.xw.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

/**
 * @author : 夏玮
 * Created on 2018/9/29 14:01
 */
//@Component
public class ErrorExtFilter extends SendErrorFilter {

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 30;    // 大于ErrorFilter的值
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

}


