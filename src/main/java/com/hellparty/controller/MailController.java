package com.hellparty.controller;

import com.hellparty.domain.MailDTO;
import com.hellparty.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;

    @ResponseBody
    @PostMapping("/mailSend")
    public void sendMail(String email){

        log.info("Controller MailSend");

        MailDTO mailDTO = new MailDTO();
        mailDTO.setFrom("lung4536@gmail.com");
        mailDTO.setSubject("test subject");
        mailDTO.setContent("test" + "<img src='cid:email_img'>");
        mailDTO.setTo(email);

        mailService.sendMail(mailDTO);

    }
}
