package com.todayworker.springboot.auth.oauth;

import com.todayworker.springboot.auth.exception.OAuthErrorCode;
import com.todayworker.springboot.auth.exception.OAuthException;
import com.todayworker.springboot.domain.user.enums.AuthProvider;
import com.todayworker.springboot.domain.user.enums.Role;
import com.todayworker.springboot.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.todayworker.springboot.domain.user.enums.AuthProvider.*;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String provider;
    private AuthProvider authProvider;
    private String nameAttributeKey;
    private String username;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String provider, AuthProvider authProvider, String nameAttributeKey, String username, String email, String picture) {
        this.attributes = attributes;
        this.provider = provider;
        this.authProvider = authProvider;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.email = email;
        this.picture = picture;
    }

    /*
        of: OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해야 한다.
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (NAVER.getKey().equals(registrationId)) {
            return ofNaver("id", attributes);
        } else if (FACEBOOK.getKey().equals(registrationId)) {
            return ofFacebook("id", attributes);
        } else if (GOOGLE.getKey().equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }

        throw new OAuthException(
                OAuthErrorCode.of(HttpStatus.BAD_REQUEST, OAuthErrorCode.NOT_SUPPORTED_PROVIDER,
                        "[registerId : " + registrationId + "]")
        );
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .provider((String) attributes.get("sub"))
                .authProvider(GOOGLE)
                .username((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .provider((String) attributes.get("id"))
                .authProvider(NAVER)
                .username((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .provider((String) attributes.get("id"))
                .authProvider(FACEBOOK)
                .username((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /*
        toEntity: User 엔티티를 생성
        OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때이다.
        가입할 때의 기본 권한을 GUEST로 주기 위해서 role 빌더 값에는 Role.GUEST를 사용
     */
    public User toEntity() {
        return User.builder()
                .username(username)
                .email(email)
                .picture(picture)
                .authProvider(authProvider)
                .role(Role.GUEST)
                .build();
    }

}
