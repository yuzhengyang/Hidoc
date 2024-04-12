package com.yuzhyn.hidoc.app.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.mapping.GeoPointProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.hidoc.app.aarg.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class EsTool {

    private String serverUrl = "https://192.168.14.62:9200";
    private String username = "elastic";
    private String passowrd = "x5676bca-aOEqBZz28K5";
    private String indexPrefix = "hidoc.";
    private ElasticsearchClient esClient = null;
    private Set<String> createIndexList = new HashSet<>();
    private int errorTimes = 0;

    private EsTool() {
    }

    /**
     * 构造函数，设置基础配置参数
     *
     * @param serverUrl
     * @param username
     * @param passowrd
     */
    public EsTool(String serverUrl, String username, String passowrd, String indexPrefix) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.passowrd = passowrd;
        this.indexPrefix = indexPrefix;
    }

    /**
     * 创建客户端
     *
     * @return
     */
    public boolean createClient() {
        // 如果连接错误发生了多次，则重新创建连接客户端
        if(errorTimes>10) {
            esClient = null;
        }

        if (esClient != null) return true;

        try {
            /** 用户认证对象 */
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            /** 设置账号密码 */
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, passowrd));

            RestClient restClient = RestClient
                    .builder(HttpHost.create(serverUrl))
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                    .setHttpClientConfigCallback(httpClientBuilder -> {
                        // 这里设置鉴权需要的用户名elastic和对应密码
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        SSLContextBuilder sscb = SSLContexts.custom();
                        try {
                            sscb.loadTrustMaterial((chain, authType) -> {
                                // 在这里跳过证书信息校验
                                System.out.println("isTrusted|" + authType + "|" + Arrays.toString(chain));
                                return true;
                            });
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (KeyStoreException e) {
                            e.printStackTrace();
                        }
                        try {
                            httpClientBuilder.setSSLContext(sscb.build());
                        } catch (KeyManagementException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        // 这里跳过主机名称校验
                        httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                        return httpClientBuilder;
                    })
                    .build();

            // Create the transport with a Jackson mapper
            ObjectMapper objectMapper = new ObjectMapper();
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatPattern.NORMAL_DATETIME_TIMEZONE.getPattern());
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
            objectMapper.registerModule(javaTimeModule);

            JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);
//            jacksonJsonpMapper.objectMapper().setDateFormat(new SimpleDateFormat(DateTimeFormatPattern.NORMAL_DATETIME_TIMEZONE.getPattern()));
            ElasticsearchTransport transport = new RestClientTransport(
                    restClient, jacksonJsonpMapper);

            // And create the API client
            esClient = new ElasticsearchClient(transport);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 自定义设置index
     *
     * @param docIndex
     * @param properties
     * @return
     */
    public boolean createIndex(String docIndex, Map<String, Property> properties) {
        if (!createClient()) return false;

        try {
            String index = formatIndex(docIndex);
            if (createIndexList.contains(index)) return true;

            // Map<String, Property> properties = new HashMap<>();
            // properties.put("location", Property.of(p -> p.geoPoint(GeoPointProperty.of(g -> g))));

            CreateIndexResponse response = esClient.indices().create(c -> c
                    .index(index)
                    .mappings(m -> m.properties(properties))
            );

            createIndexList.add(index);
            return true;
        } catch (ElasticsearchException exception) {
            if (exception.response().error().type().equals("resource_already_exists_exception")) return true;
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 创建内容
     *
     * @param docIndex
     * @param id
     * @param document
     * @return
     */
    public boolean createDocument(String docIndex, String id, Object document) {
        if (!createClient()) return false;
        try {
            String index = formatIndex(docIndex);

            IndexResponse response = esClient.index(i -> i
                    .index(index)
                    .id(id)
                    .document(document)
            );
            System.out.println("Indexed with version " + response.version());
            return true;
        } catch (IOException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    private String formatIndex(String docIndex) {
        String index = indexPrefix + docIndex;
        return index.toLowerCase();
    }
}
