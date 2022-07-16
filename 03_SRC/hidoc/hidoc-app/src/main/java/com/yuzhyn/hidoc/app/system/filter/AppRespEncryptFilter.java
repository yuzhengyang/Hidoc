//package com.yuzhyn.hidoc.app.system.filter;
//
//import com.alibaba.fastjson.JSONObject;
//import org.reactivestreams.Publisher;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.core.io.buffer.DefaultDataBufferFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
//import org.springframework.web.server.ServerWebExchange;
//import pers.yuzhyn.sugar.gateway.app.constant.FilterConstant;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//import java.nio.charset.Charset;
//import java.util.List;
//import java.util.Map;
//
//public class AppRespEncryptFilter implements GatewayFilter, Ordered {
//
//    private int order;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String bgDebug = exchange.getAttributeOrDefault(FilterConstant.BG_DEBUG_KEY, FilterConstant.REQ_RES_ENCRYPT);
//
//        ServerHttpResponse originalResponse = exchange.getResponse();
//        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                if (body instanceof Flux) {
//                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
//                    return super.writeWith(fluxBody.buffer().map(dataBuffer -> {
//
//                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
//                        DataBuffer join = dataBufferFactory.join(dataBuffer);
//
//                        byte[] content = new byte[join.readableByteCount()];
//                        join.read(content);
//                        //释放掉内存
//                        DataBufferUtils.release(join);
//                        // 正常返回的数据
//                        String rootData = new String(content, Charset.forName("UTF-8"));
//                        byte[] respData = rootData.getBytes();
//
//                        if(FilterConstant.REQ_RES_ENCRYPT.equals(bgDebug)){
//                            // 对数据进行加密
//                            String randomKey = "";//AESUtil.getRandomKey();
//                            String encryptData = randomKey;//AESUtil.AESEncrypt(rootData, randomKey, "CBC");
//                            String encryptRandomKey = randomKey;//RSAUtils.publicEncrypt(randomKey);
//                            JSONObject json = new JSONObject();
//                            json.put("k", encryptRandomKey);
//                            json.put("v", encryptData);
//                            respData = json.toJSONString().getBytes();
//                        }
//
//                        // 加密后的数据返回给客户端
//                        byte[] uppedContent = new String(respData, Charset.forName("UTF-8")).getBytes();
//                        return bufferFactory.wrap(uppedContent);
//                    }));
//                }
//                return super.writeWith(body);
//            }
//        };
//        return chain.filter(exchange.mutate().response(decoratedResponse).build());
//    }
//
//    @Override
//    public int getOrder() {
//        return this.order;
//    }
//
//    public AppRespEncryptFilter(int order){
//        this.order = order;
//    }
//}