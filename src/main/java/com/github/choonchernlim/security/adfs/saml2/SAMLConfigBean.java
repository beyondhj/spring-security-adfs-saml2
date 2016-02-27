package com.github.choonchernlim.security.adfs.saml2;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;
import com.google.common.base.Optional;
import org.springframework.core.io.Resource;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

public final class SAMLConfigBean {

    /**
     * (REQUIRED) ADFS host name without HTTPS protocol.
     */
    private final String adfsHostName;

    /**
     * (REQUIRED) Keystore containing app's public/private key and ADFS' certificate with public key.
     */
    private final Resource keyStoreResource;

    /**
     * (REQUIRED) Keystore alias.
     */
    private final String keystoreAlias;

    /**
     * (REQUIRED) Keystore password.
     */
    private final String keystorePassword;

    /**
     * (REQUIRED) Where to redirect user on successful login if no saved request is found in the session.
     */
    private final String successLoginDefaultUrl;

    /**
     * (REQUIRED) Where to redirect user on successful logout.
     */
    private final String successLogoutUrl;

    /**
     * Where to redirect user on failed login. This value is set to null, which returns
     * 401 error code on failed login. But, in theory, this will never be used because
     * IdP will handled the failed login on IdP login page.
     * <p/>
     * Default is blank.
     */
    private final String failedLoginDefaultUrl;

    /**
     * For configuring user authorities if needed.
     * <p/>
     * Default is null.
     */
    private final SAMLUserDetailsService userDetailsService;

    SAMLConfigBean(final String adfsHostName,
                   final Resource keyStoreResource,
                   final String keystoreAlias,
                   final String keystorePassword,
                   final String successLoginDefaultUrl,
                   final String successLogoutUrl,
                   final String failedLoginDefaultUrl,
                   final SAMLUserDetailsService userDetailsService) {

        this.adfsHostName = expect(adfsHostName, "ADFS host name").not().toBeBlank().check();

        this.keyStoreResource = (Resource) expect(keyStoreResource, "Key store").not().toBeNull().check();
        this.keystoreAlias = expect(keystoreAlias, "Keystore alias").not().toBeBlank().check();
        this.keystorePassword = expect(keystorePassword, "Keystore password").not().toBeBlank().check();

        this.successLoginDefaultUrl = expect(successLoginDefaultUrl, "Success login URL").not().toBeBlank().check();
        this.successLogoutUrl = expect(successLogoutUrl, "Success logout URL").not().toBeBlank().check();
        this.failedLoginDefaultUrl = Optional.fromNullable(failedLoginDefaultUrl).or("");

        this.userDetailsService = userDetailsService;
    }

    public String getAdfsHostName() {
        return adfsHostName;
    }

    public Resource getKeyStoreResource() {
        return keyStoreResource;
    }

    public String getKeystoreAlias() {
        return keystoreAlias;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getSuccessLoginDefaultUrl() {
        return successLoginDefaultUrl;
    }

    public String getSuccessLogoutUrl() {
        return successLogoutUrl;
    }

    public String getFailedLoginDefaultUrl() {
        return failedLoginDefaultUrl;
    }

    public SAMLUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}
