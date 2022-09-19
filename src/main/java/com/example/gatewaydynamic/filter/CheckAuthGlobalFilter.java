package com.example.gatewaydynamic.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CheckAuthGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("通过-1号拦截器");
        //从请求Header 获取token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (token==null){
            exchange.getRequest().getHeaders().add("Content-Type","application/json;charset=UTF-8");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            byte[] bytes = HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes();
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
        }

        //请求结束 下一个拦截器
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        //这个order 值越小 过滤器的排名就越靠前
        return -1;
    }
}
