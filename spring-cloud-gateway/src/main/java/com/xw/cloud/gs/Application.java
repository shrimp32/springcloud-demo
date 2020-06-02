package com.xw.cloud.gs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author : 夏玮 Created on 2018-12-26 16:18
 */
@SpringBootApplication
@EnableDiscoveryClient
// @EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				// 将/get请求都转发到百度,并修改header内容
				.route(p -> p.path("/get").filters(f -> f.addRequestHeader("Hello", "World"))
						.uri("http://www.baidu.com"))
				.build();
	}

	/**
	 * 路由配置 官网例子
	 * 
	 * @param builder
	 * @return
	 */
	// @Bean
	// public RouteLocator myRoutes(RouteLocatorBuilder builder,UriConfiguration
	// uriConfiguration) {
	// String httpUri = uriConfiguration.getHttpbin();
	// return builder.routes()
	// // 访问http://localhost:8080/get
	// .route(p -> p
	// .path("/get")
	// .filters(f -> f.addRequestHeader("Hello", "World"))
	// .uri("http://httpbin.org:80"))
	// //容错curl --dump-header - --header 'Host: www.hystrix.com'
	// http://localhost:8080/delay/3
	// .route(p -> p
	// .host("*.hystrix.com")
	// .filters(f -> f.hystrix(config -> config
	// .setName("mycmd")
	// .setFallbackUri("forward:/fallback")))
	// .uri("http://httpbin.org:80"))
	//// .uri(httpUri))
	// .build();
	// }

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}

	// @Bean
	// public AdaptCachedBodyGlobalFilter adaptCachedBodyGlobalFilter() {
	// return new AdaptCachedBodyGlobalFilter();
	// }
}

// @ConfigurationProperties
// class UriConfiguration {
//
// private String httpbin = "http://httpbin.org:80";
//
// public String getHttpbin() {
// return httpbin;
// }
//
// public void setHttpbin(String httpbin) {
// this.httpbin = httpbin;
// }
//
// /**
// * 应用AuthorizeGatewayFilter
// * @param builder
// * @return
// */
// @Bean
// public RouteLocator routeLocator(RouteLocatorBuilder builder) {
// return builder.routes().route(r ->
// r.path("/user/list")
// .uri("http://localhost:8077/api/user/list")
// .filters(new AuthorizeGatewayFilter())
// .id("user-service"))
// .build();
// }
// }