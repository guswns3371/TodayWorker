package com.todayworker.springboot.auth.oauth;

import com.todayworker.springboot.auth.PrincipalDetails;
import com.todayworker.springboot.auth.dto.SessionUser;
import com.todayworker.springboot.domain.user.entity.User;
import com.todayworker.springboot.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class PrincipalOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    /**
     * 구글 oauth 로그인 후 받은 userRequest 데이터에 대한 후처리되는 함수
     * loadUser 메소드의 반환값 PrincipalDetails이 Security Session 정보로 들어간다.
     * loadUser 메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /*
            provider : 현재 로그인 진행중인 서비스를 구분하는 코드
            지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
         */
        String provider = userRequest.getClientRegistration().getRegistrationId();
        /*
            userNameAttributeName: OAuth 로그인 진행 시 키가 되는 필드값. PK와 같은 의미
            구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원 X. 구글의 기본코드는 "sub"
            이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용
         */
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        /*
            OAuthAttributes: OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담은 클래스
            네이버등 다른 소셜 로그인도 이 클래스를 사용
         */
        OAuthAttributes attributes = OAuthAttributes.of(provider, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        /*
            SessionUser: 세션에 사용자 정보를 저장하기 위한 Dto 클래스. User 클래스 사용 X
         */
        httpSession.setAttribute("user", new SessionUser(user));

        /*
            1. 일반 로그인시 -> Security Session의 Authentication 이 담고 있는 객체 PrincipalDetails에는 (User 정보, null)이 담긴다.
            2. oauth 로그인시 -> PrincipalDetails(User 정보, Map<String, Object>의 유저 정보)가 담긴다.
         */
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getUsername(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
