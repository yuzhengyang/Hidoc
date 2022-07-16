package com.yuzhyn.hidoc.app.system.config;

import com.yuzhyn.hidoc.app.manager.CurrentEnvironmentManager;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyTomcatWebServerFactoryCustomizer extends TomcatWebServerFactoryCustomizer {

    public MyTomcatWebServerFactoryCustomizer(Environment environment, ServerProperties serverProperties) {
        super(environment, serverProperties);
//        serverProperties.setPort(7788);
        CurrentEnvironmentManager.add("server.port", serverProperties.getPort());
        CurrentEnvironmentManager.add("server.tomcat.threads.max", serverProperties.getTomcat().getThreads().getMax());
        CurrentEnvironmentManager.add("server.tomcat.threads.min-spare", serverProperties.getTomcat().getThreads().getMinSpare());
    }
}