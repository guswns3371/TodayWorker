package com.todayworker.springboot.auth;

import com.todayworker.springboot.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * [PrincipalDetails 를 만든 목적]
 * Security Session의 Authentication 객체가 담을 수 있는 타입에는 OAuth2User, UserDetails 타입이 존재한다.
 * oauth로 로그인할 경우 OAuth2User 타입이 Authentication 객체에 담기고
 * 일반 로그인할 경우 UserDetails 타입이 Authentication 객체에 담긴다.
 * <p>
 * 이 두 경우를 아우르기 위해 PrincipalDetails implements UserDetails, OAuth2User 한다.
 */
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 user의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole().getKey());
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 1년동안 로그인이 안된 회원을 휴면 회원으로 전환하는 기능
        return true;
    }

    public User getUser() {
        return user;
    }

    // oauth로 로그인할 경우, 사용자 정보가 key=value 형태로 전달된다.
    // 그 정보를 Map 객체로 만들어주는 메소드이다.
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getId() + "";
    }
}