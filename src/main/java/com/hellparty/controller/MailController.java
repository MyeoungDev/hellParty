package com.hellparty.controller;

import com.hellparty.domain.MailDTO;
import com.hellparty.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;

    @ResponseBody
    @PostMapping("/mailSend")
    public String sendMail(String email) {
        log.info("Controller MailSend");

        Random random = new Random();
        int num = random.nextInt(888888) + 111111;

        log.info("mailCheckNum" + num);

        String mailCheckNum = Integer.toString(num);


        MailDTO mailDTO = new MailDTO();
        mailDTO.setFrom("lung4536@gmail.com");
        mailDTO.setSubject("hellParty 회원가입 인증번호 입니다.");
//        mailDTO.setContent("<img src='cid:email_img'>");
        mailDTO.setContent(
                "<div style='text-align: center; width: 500px;'>" +
                        "<img src='cid:email_img' style='z-index: 0; margin-bottom: -100px;'>" +
                        "<p style='text-align: center; font-size: 30px;'>Input your code in site</p>" +
                        "<p style='font-weight: bold; font-size: 60px; letter-spacing: 10px; margin: 0; color: #e82d00;'>" +
                        num +
                        "</p>" +
                        "</div>"
        );

        mailDTO.setTo(email);

//        mailService.sendMail(mailDTO);

        return mailCheckNum;
    }


    @GetMapping("/mailCheck")
    public void mailCheck(int input) {

    }
}
