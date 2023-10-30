package com.hgleeee.blog.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = null;
        try {
            user = retrieveUser(authentication.getPrincipal().toString());
        } catch (AuthenticationException e) {
            throw e;
        }
        CustomAuthenticationToken customAuthenticationToken =
                new CustomAuthenticationToken(
                        user,
                        authentication.getCredentials(),
                        authoritiesMapper.mapAuthorities(user.getAuthorities()));

        additionalAuthenticationChecks(user, customAuthenticationToken);
        customAuthenticationToken.setDetails(authentication.getDetails());
        return customAuthenticationToken;
    }

    protected final UserDetails retrieveUser(String username) {
        try {
            UserDetails loadedUser = userDetailsService.loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException ex) {
            log.debug("error in retrieveUser = {}", ex.getMessage());
            throw ex;
        }
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  CustomAuthenticationToken authentication) {
        if (authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException("Bad credentials");
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            log.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
