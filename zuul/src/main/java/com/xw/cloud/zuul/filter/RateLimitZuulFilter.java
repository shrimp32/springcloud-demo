package com.xw.cloud.zuul.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

import java.net.URL;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
//import org.springframework.boot.actuate.endpoint.SystemPublicMetrics;
//import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @author : 夏玮 Created on 2018/9/25 15:24 基于工具类RateLimiter的限流 基于系统负载的动态限流
 */
@Component
public class RateLimitZuulFilter extends ZuulFilter {

	// private final RateLimiter rateLimiter = RateLimiter.create(1000.0);
	private Map<String, RateLimiter> map = Maps.newConcurrentMap();

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Autowired
	// private SystemPublicMetrics systemPublicMetrics;
	/**
	 * 基于Spring Boot Actuator提供的Metrics 能力进行实现基于内存压力的限流——当可用内存低于某个阈值就开启限流，否则不开启限流
	 * 
	 * @return 是否进行限流
	 */
//	@Override
	// public boolean shouldFilter() {
	// // 这里可以考虑弄个限流开启的开关，开启限流返回true，关闭限流返回false
	// Collection<Metric<?>> metrics = systemPublicMetrics.metrics();
	//
	// Optional<Metric<?>> freeMemoryMetric = metrics.stream()
	// .filter(t -> "mem.free".equals(t.getName()))
	// .findFirst();
	// // 如果不存在这个指标，稳妥起见，返回true，开启限流
	// if (!freeMemoryMetric.isPresent()) {
	// return true;
	// }
	// long freeMemory = freeMemoryMetric.get()
	// .getValue()
	// .longValue();
	// // 如果可用内存小于1000000KB，开启流控
	// return freeMemory < 1000000L;
	// }

	private MetricsEndpoint metricsEndpoint;
	private static final String METRIC_NAME = "system.cpu.usage";
	private static final double MAX_USAGE = 0.50D;
	@Override
	/**
	 * 基于Spring Boot Actuator提供的Metrics：cpu使用率，进行限流，
	 */
	public boolean shouldFilter() {
		// 配置限流的规则,cpu使用率大于50%
		Double systemCpuUsage = metricsEndpoint.metric(METRIC_NAME, null).getMeasurements().stream()
				.filter(Objects::nonNull).findFirst().map(MetricsEndpoint.Sample::getValue).filter(Double::isFinite)
				.orElse(0.0D);
		boolean ok = systemCpuUsage < MAX_USAGE;
		return ok;
	}

	// public boolean shouldFilter() {
	// //配置限流的规则
	// return true;
	// }

	@Override
	public Object run() {
		try {
			RequestContext context = RequestContext.getCurrentContext();
			HttpServletResponse response = context.getResponse();

			// 针对不同的服务做不同的限流处理
			String key = "default";
			map.put(key, RateLimiter.create(1000.0));

			// 对于service格式的路由，走RibbonRoutingFilter
			String serviceId = (String) context.get(SERVICE_ID_KEY);
			if (serviceId != null) {
				key = serviceId;
				map.putIfAbsent(serviceId, RateLimiter.create(1000.0));
			}
			// 如果压根不走RibbonRoutingFilter，则认为是URL格式的路由
			else {
				// 对于URL格式的路由，走SimpleHostRoutingFilter
				URL routeHost = context.getRouteHost();
				if (routeHost != null) {
					String url = routeHost.toString();
					key = url;
					map.putIfAbsent(url, RateLimiter.create(2000.0));
				}
			}
			RateLimiter rateLimiter = map.get(key);

			// 具体的处理
			if (!rateLimiter.tryAcquire()) {
				HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;

				response.setContentType(MediaType.TEXT_PLAIN_VALUE);
				response.setStatus(httpStatus.value());
				response.getWriter().append(httpStatus.getReasonPhrase());

				context.setSendZuulResponse(false);

				throw new ZuulException(httpStatus.getReasonPhrase(), httpStatus.value(), httpStatus.getReasonPhrase());
			}
		} catch (Exception e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		return null;
	}
}
