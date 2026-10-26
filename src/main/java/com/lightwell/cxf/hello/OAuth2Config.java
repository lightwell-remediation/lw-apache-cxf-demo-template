package com.lightwell.cxf.hello;

import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2Config {

    @Bean
    public OAuthRequestFilter oauthRequestFilter(
            @Value("${hello.oauth.access-token}") String accessToken) {
        OAuthRequestFilter filter = new OAuthRequestFilter();
        filter.setTokenValidator(new SampleBearerTokenValidator(accessToken));
        return filter;
    }

    @Bean
    public Server helloServer(Bus bus, OAuthRequestFilter oauthRequestFilter) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("/");
        endpoint.setServiceBeans(List.of(new HelloWorldResource()));
        endpoint.setProvider(oauthRequestFilter);
        return endpoint.create();
    }
}
