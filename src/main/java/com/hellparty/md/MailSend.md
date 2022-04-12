# [Spring Boot] 스프링 이메일 이미지 전송

[[Spring Boot] 스프링 이메일 전송 사용하기 (SMTP)](https://myeongdev.tistory.com/26)
<br>
위의 링크는 필자가 이 전에 JavaMailSender을 이용하여 간단한 텍스트만 전송했던 글이다.
우선 필자가 위의 SMTP를 이용한 이메일 전송처리를 사용하는 이유는 아주 간단하다.
단지 회원가입시 이메일 인증을 위한 사용이다.
현재 해보고싶다고 생각이 든 첫 나만의 프로젝트를 진행하며 다시 JavaMailSender 을 이용하면서,
이미지 파일을 Mail Content에 넣어서 보내고 싶다는 생각을 하게 되어,
다시 공부하면서 코드를 다시 짜보았다.
그렇기에 기록의 용도로 작성한다.

기본적인 Spring Boot SMTP 사용은 위의 글에 정리해 놓았다. 참고 바란다.

이번 글은 위의 글에서 아래와 같은 내용을 추가하고 바꿀 것이다.

<br>

첫번째로는 **Controller에 모든 것들이 구현되어 있다.**

위의 문제는 MVC패턴을 사용하는 객체지향에 올바르지 않다고 생각되고, 코드 재사용에도 별로 좋지 않다고 생각되었다.
하지만 일단 해보는것이 중요하다 생각되어 위의 상황에는 진행하게 되었다.

그리도 두번째로는 **이미지 전송의 부재** 이다.

현재 많이 사용되는 서비스들을 보면 Email 인증 시 간단한 이미지와 텍스트가 날라온다.
이처럼 나도 간단한 이미지 하나 넣어서 전송하고 싶었다.

<br>
<br>

------

<br>
<br>

먼저 SMTP 메일 이미지 첨부에는 3가지 방법이 있다고 한다.

> **Inline embedding - 이미지를 base64로 인코딩 하는 방법으로 메일 무거워지는 단점으로 잘 사용하지 않는다.**
>
> **CID(Content-ID) - 이미지를 파일에 첨부하듯이 하여 메일을 열때 같이 HTML 태그와 같이 보여주는 방식이다.**
>
> **Linked images - 메일안의 HTML 태그 안에 이미지 주소를 넣어주는 방식이다.**

<br>

필자는 이 중 CID 방식으로 진행했다.

조금 더 자세한 설명과 간단한 코드는 아래 참고한 블로그에 있다.

[[JAVA] SMTP 메일 이미지 첨부 방법 3가지](https://zangzangs.tistory.com/46)


### HTML, JS

**HTML**

<br>

```html

<div class="join_columns_column">
    <p class="join_column_text">이메일</p>
    <div class="join_mail_box">
    <input type="text" name="userEmail" class="join_email">
    <button type="button" class="join_email_btn submit">인증번호 전송</button>
    </div>
    <div class="email_mailCheck_box">
        <input type="text" class="join_email check">
        <button type="button" class="join_email_btn check">확인</button>
        <p class="join_warn">인증번호가 일치하지 않습니다.</p>
        <p class="join_warn success">인증번호가 일치합니다.</p>
    </div>
</div>

```

<br>

**JS**

<br>

```javascript

document.querySelector(".join_email_btn.submit").addEventListener("click", function () {
    console.log("test");
    let email = document.querySelector("input[name=userEmail]").value;
    $.ajax({
        url: '/mailSend',
        type: 'get',
        data: {email, email},
        success: function (result) {
            console.log("성공");
            console.log(result);
        },
        error: function (error) {
            console.log(error);
        }

    });
});

```

<br>

위의 JS 방식처럼 Jquery를 사용하여 ajax 통신을 통해 비동기 처리를 하였다.

### MailDTO 생성

간단한 Mail에 대한 정보를 전달해줄 클래스를 하나 생성해 준다.

<br>

```java

package com.hellparty.domain;

import lombok.Data;

@Data
public class MailDTO {

    private String from;
    private String to;
    private String subject;
    private String content;

}

```

<br>


위의 MailDTO 클래스를 통해 Serivce 와 Controller 사이에서 간단하게 정보를 교환할 생각이었다.

### Controller ajax test

```java

package com.hellparty.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class MailController {

    @ResponseBody
    @PostMapping Mapping("/mailSend")
    public void sendTestMail(String email) {

        log.info("Controller MailSend");

    }
}

```

<br>

Mail에 관련된 처리만을 하는 Controller를 새롭게 생성해 주었다.
그리고 위와같이 간단하게 테스트를 진행하여 비동기 처리가 완료되는지 확인하였다.


### Service

```java
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
             CLassPathResource클래스를 제공한다. */
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
```

<br>

위처럼 Mail에 관련된 Service만을 다루기위한 MailService라는 클래스를 만들어 주었다.
본래는 Service 밑에 implement를 두어 구현받아 사용하지만, 한개의 메서드 밖에 존재하지 않아 바로 Service에 구현하였다.
전의 글과 코드가 달라진 점은

<br>

```java
...
File  imgFile = new ClassPathResource("static/img/email_hellparty_img.png").getFile();

...
messageHelper.addInline("email_img", imgFile);

```

<br>

위의 두 부분이다.

처음 메일에 이미지를 첨부하기위해 `messageHelper.addInline`의 두번째 파라미터에
이미지에 대한 절대 경로를 바로 집어 넣었다.
하지만, 기대와 달리 정상적으로 작동하지 않았고 더 많은 구글링을 진행하였다.
그래서 해답을 얻게 되었다.
위의 코드에 주석으로 설명을 달아놨지만 다시 정리하는 느낌으로 설명해 보겠다.
<br>
먼저 `messageHelper.addInline` 에 관한 것이다.
첫번째 파라미터는 Content-ID 이고, 두번째는 이미지에 대한 경로이다.

Content-ID에 대해서는 위쪽 부분에 짧게 설명을 해 놓았다.

그리고 두번째 파라미터에 대한 것이다.
메일 이미지 첨부를 위해 많은 블로그분들의 코드를 보았다.
많은분들이 `FileSystemResource` 를 이용하여 경로를 다시한번 변경하였다.
그 이유가 궁금하여 `MimeMessageHelper` 공식문서에 들어가 해당 메서드를 찾아보았다

![image](https://user-images.githubusercontent.com/73057935/162966753-fa89c3e7-c07d-48ec-84fe-41e037def530.png)

위처럼 두번째 파라미터에는 Resource 타입 뿐만 아니라 File, InputStream 등이 올 수 있었다.
그렇게 하여 필자는 저 코드를 주석처리하였다.
물론 정상적으로 실행되었다.

[MimeMessageHelper 공식문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/mail/javamail/MimeMessageHelper.html)

<br>

그래도 생소한 `ClassPathResource` 클래스가 존재 한다.

프로젝트가 빌드되면 리소스 파일들은 CLASS_PATH 에 위치하게 된다.
스프링 프레임워크에서 이러한 CALSS_PATH에 저장된 리소스 파일을 쉽게 가져올 수 있도록 해주는
`ClassPathResource` 클래스를 제공해 준다.
그래서 위의 클래스를 통해 File 객체로 변환 후 지정해 주니 정상적으로 되었다.


[Spring Resources에 대한 참고](https://always-develop.tistory.com/37)

[ClassPathResource 참고](http://june0313.github.io/2018/08/11/read-resource-file-on-spring/)


### Controller


```java

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

```

<br>

다시 Controller로 돌아와 MailService를 Autowired해주고
MailDTO의 값을 설정해준 뒤 실행하면 정상적으로 완료된다.

![image](https://user-images.githubusercontent.com/73057935/162969887-db76c1ed-a306-4b6b-9813-c66f9eab4d0b.png)

**Good!**

이제 저는 정처기 실기 준비하러 가보겠습니다.

~~보고있나 elCode~~