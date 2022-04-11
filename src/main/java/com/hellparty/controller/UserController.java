package com.hellparty.controller;

import com.hellparty.domain.UserVO;
import com.hellparty.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "index")
    public void indexGET() {
        log.info("Controller indexGET");
    }

    @GetMapping(value = "login")
    public void loginGET() {
        log.info("Controller loginGET");
    }

    @GetMapping(value = "join")
    public void joinGET() {
        log.info("Controller joinGET");
    }

    @PostMapping(value = "joinAction")
    public String joinPOST(UserVO user) throws Exception{
        log.info("Controller joinPOST");

        userService.userJoin(user);

        log.info("join 성공");

        return "redirect:/index";
    }
}
