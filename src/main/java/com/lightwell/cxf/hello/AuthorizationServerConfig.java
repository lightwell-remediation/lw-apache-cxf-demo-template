package com.lightwell.cxf.hello;

import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The authorization-code side of the sample.
 *
 * <p>Until this existed the application was a resource server and nothing
 * else: it validated a bearer token and never touched the authorization code
 * machinery. That made it depend on {@code cxf-rt-rs-security-oauth2} without
 * ever executing the part of it the CVEs are about — affected in the bill of
 * materials, not reachable in practice.
 *
 * <p>Issuing and redeeming codes through {@link AuditedCodeDataProvider} makes
 * the flawed class genuinely reachable, which is what gives the remediation
 * demo a defect to point at rather than a version number to bump.
 */
@Configuration
public class AuthorizationServerConfig {

    /** The single client this sample issues codes to. */
    @Bean
    public Client demoClient(@Value("${hello.oauth.client-id:hello-world-client}") String clientId,
                             @Value("${hello.oauth.client-secret:hello-world-secret}") String clientSecret) {
        Client client = new Client(clientId, clientSecret, true);
        client.setRedirectUris(List.of("http://localhost:8080/callback"));
        client.setAllowedGrantTypes(List.of("authorization_code"));
        client.setRegisteredScopes(List.of("read"));
        return client;
    }

    @Bean
    public AuditedCodeDataProvider codeDataProvider(Client demoClient) {
        AuditedCodeDataProvider provider = new AuditedCodeDataProvider();
        provider.setClients(List.of(demoClient));
        provider.init();
        return provider;
    }
}
