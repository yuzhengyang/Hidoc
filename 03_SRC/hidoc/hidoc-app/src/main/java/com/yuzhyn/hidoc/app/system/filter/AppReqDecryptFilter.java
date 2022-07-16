//package com.yuzhyn.hidoc.app.system.filter;
//
//import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import pers.yuzhyn.sugar.common.lib.entity.communication.vos.RequestData;
//import pers.yuzhyn.sugar.gateway.app.constant.FilterConstant;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
////@Component
//public class AppReqDecryptFilter implements GatewayFilter, Ordered {
//
//    private static final Logger logger = LoggerFactory.getLogger(AppReqDecryptFilter.class);
//
//
//    @Override
//    public int getOrder() {
//        return -25;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 设置是否加密标识
//        List<String> bgDebugHeaders = exchange.getRequest().getHeaders().get("bg-debug");
//        String bgDebug = bgDebugHeaders != null ? bgDebugHeaders.get(0) : FilterConstant.REQ_RES_ENCRYPT;
//        exchange.getAttributes().put(FilterConstant.BG_DEBUG_KEY, bgDebug);
//
//        // 获取请求的方法
//        ServerHttpRequest oldRequest = exchange.getRequest();
//        String method = oldRequest.getMethodValue();
//        URI uri = oldRequest.getURI();
//
//        if ("POST".equals(method) && false) {
//            // 尝试从 exchange 的自定义属性中取出缓存到的 body
//            Object cachedRequestBodyObject = exchange.getAttributeOrDefault(FilterConstant.CACHED_REQUEST_BODY_OBJECT_KEY, null);
//            if (cachedRequestBodyObject != null) {
//                byte[] decrypBytes;
//                try {
//                    byte[] body = (byte[]) cachedRequestBodyObject;
//                    String rootData = new String(body); // 客户端传过来的数据
//                    decrypBytes = body;
//
//                    if (FilterConstant.REQ_RES_ENCRYPT.equals(bgDebug)) {
//                        RequestData jsonObject = JSONObject.parseObject(rootData, RequestData.class);
//                        jsonObject.put("op-no", UUID.randomUUID());
//                        String jsonString = JSONObject.toJSONString(jsonObject);
//                        decrypBytes = jsonString.getBytes();
//                    }
//
//                } catch (Exception e) {
//                    logger.error("客户端数据解析异常:{}", e.toString());
//                    throw new NullPointerException("900100-客户端数据解析异常");
//                }
//
//                // 根据解密后的参数重新构建请求
//                DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
//                Flux<DataBuffer> bodyFlux = Flux.just(dataBufferFactory.wrap(decrypBytes));
//                ServerHttpRequest newRequest = oldRequest.mutate().uri(uri).build();
//                newRequest = new ServerHttpRequestDecorator(newRequest) {
//                    @Override
//                    public Flux<DataBuffer> getBody() {
//                        return bodyFlux;
//                    }
//                };
//
//                // 构建新的请求头
//                HttpHeaders headers = new HttpHeaders();
//                headers.putAll(exchange.getRequest().getHeaders());
//                // 由于修改了传递参数，需要重新设置CONTENT_LENGTH，长度是字节长度，不是字符串长度
//                int length = decrypBytes.length;
//                headers.remove(HttpHeaders.CONTENT_LENGTH);
//                headers.setContentLength(length);
//                // headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
//                newRequest = new ServerHttpRequestDecorator(newRequest) {
//                    @Override
//                    public HttpHeaders getHeaders() {
//                        return headers;
//                    }
//                };
//
//                // 把解密后的数据重置到exchange自定义属性中,在之后的日志GlobalLogFilter从此处获取请求参数打印日志
//                exchange.getAttributes().put(FilterConstant.CACHED_REQUEST_BODY_OBJECT_KEY, decrypBytes);
//                return chain.filter(exchange.mutate().request(newRequest).build());
//            }
//        } else if ("GET".equals(method)) { // todo 暂不处理
//            Map requestQueryParams = oldRequest.getQueryParams();
//            return chain.filter(exchange);
//        }
//        return chain.filter(exchange);
//    }
//}