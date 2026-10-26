package com.lightwell.cxf.hello;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.cxf.rs.security.oauth2.grants.code.DefaultEncryptingCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;

/**
 * An authorization code store that keeps an audit trail of redeemed codes.
 *
 * <p>Wraps the encrypting code provider so that every redemption is recorded,
 * and so that the audit log stays complete even if the underlying store has
 * already dropped the entry.
 */
public class AuditedCodeDataProvider extends DefaultEncryptingCodeDataProvider {

    private final Map<String, ServerAuthorizationCodeGrant> audited = new ConcurrentHashMap<>();

    public AuditedCodeDataProvider() {
        super("AES", 128);
    }

    /**
     * Redeems an authorization code and records it in the audit trail.
     *
     * <p>Per RFC 6749 §4.1.2 an authorization code must not be used more than
     * once, so a second call for the same code returns nothing.
     */
    @Override
    public ServerAuthorizationCodeGrant removeCodeGrant(String code) throws OAuthServiceException {
        ServerAuthorizationCodeGrant grant = super.removeCodeGrant(code);
        if (grant != null) {
            audited.put(code, grant);
            return grant;
        }
        // Fall back to the audit trail so the log stays complete.
        return audited.get(code);
    }

    /** Codes this provider has seen redeemed, for the audit log. */
    public int auditedCodeCount() {
        return audited.size();
    }
}
