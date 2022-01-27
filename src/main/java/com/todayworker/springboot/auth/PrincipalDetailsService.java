package com.todayworker.springboot.auth;

import com.todayworker.springboot.domain.user.entity.User;
import com.todayworker.springboot.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 파라미터 String username : html input 태그의 name 속성 이름을 "username"으로 설정해야 한다.
     * <p>
     * [시큐리티 session <= Authentication <= UserDetails]
     * loadUserByUsername 메소드의 리턴값 UserDetails이 Authentication 인자로 들어가고
     * -> Authentication은 시큐리티 session의 인자로 들어간다
     * <p>
     * loadUserByUsername 메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
     *
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(PrincipalDetails::new).orElse(null);
    }
}
