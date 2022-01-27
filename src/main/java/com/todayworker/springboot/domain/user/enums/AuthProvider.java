package com.todayworker.springboot.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {
    NONE("none", "이메일"),
    GOOGLE("google", "구글"),
    NAVER("naver", "네이버"),
    FACEBOOK("facebook", "페이스북");

    private final String key;
    private final String title;
}
