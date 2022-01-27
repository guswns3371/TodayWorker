package com.todayworker.springboot.web.controller;

import com.todayworker.springboot.auth.anno.LoginUser;
import com.todayworker.springboot.auth.dto.SessionUser;
import com.todayworker.springboot.domain.config.ResultVO;
import com.todayworker.springboot.domain.user.repository.UserRepository;
import com.todayworker.springboot.web.service.AuthServiceIF;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("login/")
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    private final AuthServiceIF loginService;
    private final UserRepository userRepository;

    @ResponseBody
    @RequestMapping("login.do")
    public ResultVO loginUser(@LoginUser SessionUser user) {
        LOG.info("Login User");

        ResultVO result = new ResultVO(false, null);

        try {
            result.setData(user);
            result.setSuccess(true);
        } catch (Exception e) {
            LOG.error("[Login] Login : " + e.getMessage(), e);
        }

        return result;
    }
}
