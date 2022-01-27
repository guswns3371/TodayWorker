package com.todayworker.springboot.web.service;

import com.todayworker.springboot.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthServiceIF {

    private final UserRepository userRepository;
}
