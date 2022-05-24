# [Spring Boot] Interceptor 설정 / 로그인 확인 (Gradle, HandlerInterceptor, WebMvcConfigurer)

게시판을 개발 하던 중, Interceptor를 귀찮아서 설정 안하고 하고 있었다.
그런데, 서버를 재실행하고 다시 로그인창으로 가서 로그인을 하고 게시판 작성 페이지로 가는게 더 귀찮아졌다.
그래서 Interceptor 설정을 한김에 내가 Interceptor 글 작성을 안했길래 글로 남긴다.

<br>

상황은 이미 로그인이 되어 있는 유저가 다시 로그인 페이지에 접속할 경우와
로그인이 안된 유저가 게시글 작성 페이지에 들어올 경우 두 가지이다.

### Interceptor란?

먼저 Interceptor가 무엇인지 간단하게 집고 넘어가자면
`Interceptor`란 컨트롤러에 들어오는 요청 `HttpRequest`와 컨트롤러가 응답하는 `HttpResponse`를 가로채는 역할이다.
예를 들어, 어느 한 사이트를 이용하다 '로그인이 필요한 서비스입니다.'라는 문구와 함께 해당 기능이 동작하지 않거나 해당 페이지에 접속하지 못했던 기억이 누구나 있을 것이다.
이처럼 사용자 인증, 로그인 인증, 관리자 인증 등을 처리하는데 사용하는 것이 Interceptor 이다.

<br>

인터셉터는 크게 3가지로 `preHandler`, `postHandler`, `afterCompletion`이 존재한다.

`preHandler`는 컨트롤러 요청 전에 가로채는 것이고,
`postHandler`는 컨트롤러 요청이 끝난 후 즉, View에 전달되기 전이고,
`afterCompletion`은 마지막에 실행되는 인터셉터로 해당 메서드가 끝나면 최종적으로 클라이언트에게 전달된다.

<br>

인터셉터 작동순서

```
preHandler -> 요청 처리 -> postHandler -> View -> afterCompletion
```



### LoginInterceptor 구현하기

먼저 interceptor 라는 디렉토리를 생성 한 후 LoginInterceptor라는 클래스를 생성해 준다.

<br>

```java

package com.hellparty.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("LoginInterceptor preHandler");

        HttpSession session = request.getSession();

        if (session.getAttribute("loginUser") != null) {    /* 로그인 되어 있는 경우 */
            response.sendRedirect("/index");
        }

        return true;    /* 로그인 되지 않은 경우 */
    }
}

```

<br>

위의 코드는 request로 부터 session을 가져오고, 세션에 등록된 로그인 세션이 존재하지 않다면 바로 메인 화면으로 리다이렉트 해버리는 것이다.
로그인 세션 처리하는 방법은 전에 작성한 글을 참고 해주시길 바란다.

<br>

[[Spring Boot] 로그인 Session 처리하기(HttpServletRequest, HttpSession)](https://myeongdev.tistory.com/44)

<br>

헌데, 이번 글을 작성하면서 Interceptor에 대해서 좀 찾아보는데 몇몇 분들은 로그인 시 Interceptor로 Session을 등록하는 것 같았다.
혼자 찾아보며 공부하다보니 이러한 방법에서 어떤것이 정확히 맞는것이 잘 모르겠다.

### BoardInterceptor

```java

package com.hellparty.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class BoardInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("BoardInterceptor preHandler");

        HttpSession session = request.getSession();
        if (session.getAttribute("loginUser") == null) {
            response.sendRedirect("/login");
        }
        return true;
    }
}

```

<br>

위의 LoginInterceptor 와 비슷한 코드이므로 설명은 생략하도록 하겠다.

### Configuration 설정

```java

package com.hellparty.configuration;

import com.hellparty.interceptor.BoardInterceptor;
import com.hellparty.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/login");


        registry.addInterceptor(new BoardInterceptor())
                .addPathPatterns("/board/**");
    }


}

```

<br>

위의 코드는 `addPathPatterns`을 통해 어디에 해당 인터셉터를 적용 시킬지 설정하는 것으로,
로그인 페이지와 일단 게시판 관련된 모든 페이지에 적용을 시켰다.

만약 제외시키고 싶은게 있다면 `excludePathPatterns()`메서드를 사용하면 된다.

<br>

그리고 필자는 WebConfiguration 설정 시 `WebMvcConfigurer` 를 상속받았다.
전에 `WebMvcConfigurationSupport` 를 extends 해서 구현해보려 했는데 MIME type 오류로 꽤나 애먹었다.

<br>

[[Spring Boot] MIME type ('application/json') is not a supported stylesheet MIME type (Interceptor 설정 시 css 오류)](https://myeongdev.tistory.com/27?category=984292)

<br>

위와 같이 설정해주면 Interceptor에 대한 기본적인것은 끝이났다.

<br>

이상으로 글을 마치겠다.

