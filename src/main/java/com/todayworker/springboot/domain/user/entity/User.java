package com.todayworker.springboot.domain.user.entity;


import com.todayworker.springboot.domain.BaseTimeEntity;
import com.todayworker.springboot.domain.user.enums.AuthProvider;
import com.todayworker.springboot.domain.user.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK의 생성 규칙. 스트링부트 2.0에서는 GenerationType.IDENTITY 옵션을 추가해야지만 auto_increment가 된다
    private Long id;

    @Column(nullable = true)
    private String username;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING) //JPA로 DB에 저장할때 Enum값을 어떤 형태로 저장할지 결정. 기본 int 숫자가 저장. 숫자로 저장되면 그 값이 무슨 코드를 의미하는지 파악x. 그래서 문자열로 EnumType.STRING로 선언
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider = AuthProvider.NONE;

    @Builder
    public User(String username, String email, String password, String picture, Role role, AuthProvider authProvider) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.role = role;
        this.authProvider = authProvider;
    }

    public User update(String name, String picture) {
        if (name != null) {
            this.username = name;
        }
        else if (picture != null) {
            this.picture = picture;
        }

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
