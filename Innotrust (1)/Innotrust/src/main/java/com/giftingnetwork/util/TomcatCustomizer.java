package com.giftingnetwork.util;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {

        factory.addConnectorCustomizers((connector) -> {

            connector.setAttribute("relaxedPathChars", "<>[\\]^`{|}%");
            connector.setAttribute("relaxedQueryChars", "<>[\\]^`{|}%");
          
        });
    }

}