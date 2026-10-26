package com.lightwell.cxf.hello;

import java.util.List;

import jakarta.ws.rs.core.MultivaluedMap;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenValidation;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenValidator;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

/**
 * Accepts the single bearer token configured for this local sample.
 */
public class SampleBearerTokenValidator implements AccessTokenValidator {

    private final String accessToken;

    public SampleBearerTokenValidator(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public List<String> getSupportedAuthorizationSchemes() {
        return List.of(OAuthConstants.BEARER_AUTHORIZATION_SCHEME);
    }

    @Override
    public AccessTokenValidation validateAccessToken(MessageContext messageContext,
                                                     String authScheme,
                                                     String authSchemeData,
                                                     MultivaluedMap<String, String> extraProps) {
        if (!accessToken.equals(authSchemeData)) {
            return null;
        }
        AccessTokenValidation validation = new AccessTokenValidation();
        validation.setTokenKey(authSchemeData);
        validation.setTokenType(OAuthConstants.BEARER_TOKEN_TYPE);
        validation.setTokenLifetime(0L);
        return validation;
    }
}
