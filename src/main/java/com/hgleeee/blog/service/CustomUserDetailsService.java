package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.CustomUserDetails;
import com.hgleeee.blog.domain.Role;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.exception.EmailPasswordInvalidException;
import com.hgleeee.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(EmailPasswordInvalidException::new);
    }

    private UserDetails createUserDetails(User user) {

        List<? extends GrantedAuthority> authorities = List.of(user.getRole())
                .stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new CustomUserDetails(user.getEmail(), user.getPassword(), authorities);
    }
}
