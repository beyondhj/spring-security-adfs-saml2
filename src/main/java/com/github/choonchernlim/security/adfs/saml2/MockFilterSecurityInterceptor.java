package com.github.choonchernlim.security.adfs.saml2;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This filter class can be placed before Spring Security's FilterSecurityInterceptor to bypass any security
 * by setting the given user to the security context and HTTP session. This allows developer to do rapid app
 * development without the need to log into the app again and again.
 */
final class MockFilterSecurityInterceptor implements Filter {

    private final User user;

    MockFilterSecurityInterceptor(final User user) {
        expect(user, "user").not().toBeNull().check();
        this.user = user;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {

        // To be consistent with SAML configuration, the `userDetails` is set as `principal` too
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities());

        // setting user details
        authentication.setDetails(user);

        // setting `authentication` to security context
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // setting `authentication` to HTTP session
        ((HttpServletRequest) servletRequest)
                .getSession(true)
                .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
