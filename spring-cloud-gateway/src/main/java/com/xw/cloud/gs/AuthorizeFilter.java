//package com.xw.cloud.gs;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * @author : 夏玮
// * Created on 2018-12-27 14:51
// * 全局路由器
// */
//@Component
//@Order(0)
//public class AuthorizeFilter implements GlobalFilter{
//    private static final String AUTHORIZE_TOKEN = "token";
//    private static final String AUTHORIZE_UID = "uid";
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        HttpHeaders headers = request.getHeaders();
//        String token = headers.getFirst(AUTHORIZE_TOKEN);
//        String uid = headers.getFirst(AUTHORIZE_UID);
//        if (token == null) {
//            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
//        }
//        if (uid == null) {
//            uid = request.getQueryParams().getFirst(AUTHORIZE_UID);
//        }
//
//        ServerHttpResponse response = exchange.getResponse();
//        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//        String authToken = stringRedisTemplate.opsForValue().get(uid);
//        if (authToken == null || !authToken.equals(token)) {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//
//        return chain.filter(exchange);
//    }
//}
