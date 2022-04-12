package com.hellparty.service;

import com.hellparty.domain.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {

    @Autowired
    JavaMailSender mailSender;

    public void sendMail(MailDTO mailDTO) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            /* 프로젝트를 빌드하면 리소스 파일들은 CALSS_PATH 에 위치하게 된다.
             스프링 프레임워크에서 이러한 CALSS_PATH에 저장된 리소스 파일을 쉽게 가져올 수 있도록 해주는
             CLassPathResource클래스를 제공한다.
             */
            File imgFile = new ClassPathResource("static/img/email_hellparty_img.png").getFile();
            /* FileSystemResource -> 절대경로라던지 파일시스템에서 리소스를 찾는 방식 (거의 사용 X)*/
//            FileSystemResource fsr = new FileSystemResource(file);

            messageHelper.setFrom(mailDTO.getFrom());
            messageHelper.setTo(mailDTO.getTo());
            messageHelper.setSubject(mailDTO.getSubject());
            /* true는 html을 사용하겠다는 의미 즉, <img> 태그를 이용하여 static 안에 이미지를 사용 가능 */
            messageHelper.setText(mailDTO.getContent(), true);
            messageHelper.addInline("email_img", imgFile);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
