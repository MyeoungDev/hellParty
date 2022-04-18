# [Spring Boot] 로그인, 로그아웃 Session 처리하기(HttpServletRequest, HttpSession)

로그인 기능을 구현하면서 Session처리를 하려고 한다.
그렇기에 Cookie 와 Session이 무엇인지 부터 바로 알고 싶어 공부하고 정리해 두었다.

[[Network] 간단히 알아보는 Cookie, Session](https://myeongdev.tistory.com/43)

Cookie 와 Session에 대한 개념은 위의 글로 대체하고 바로 진행하도록 하겠다.

### Login Controller

```java

@PostMapping(value = "login")
public String loginPOST(UserDTO userDTO, HttpServletRequest request, RedirectAttributes rttr){
    log.info("Controller loginPOST");

    HttpSession session = request.getSession();

    UserDTO login = userService.userLogin(userDTO);

    String failMessage = "아이디 혹은 비밀번호가 잘못 되었습니다.";

    if (login == null) {
        rttr.addFlashAttribute("loginFail", failMessage);
        return "redirect:/login";
    }

    session.setAttribute("loginUser", login);
    return "redirect:/index";
}

```

<br>

위의 코드에서 로그인 Session에 대해 눈 여겨 볼 것은 `HttpServletRequest`, `HttpSession` 이다.

먼저 `HttpServletRequest`는 HTTP request 메세지를 개발자가 편하게 파싱할 수 있도록 도와주는 인터페이스이다.

![image](https://user-images.githubusercontent.com/73057935/163802937-e0ea1a1f-0cbe-469c-98c9-1d23b29a3c2f.png)

우리는 Session에 대해서 다룰꺼기 때문에 위에 나와있는 `getSession()`을 사용할 것이다.
>Returns the current session associated with this request, or if the request does not have a session, creates one.

리턴 타입은 `HttpSession` 이고, 만약 reqeust에 대한 session이 존재하지 않는다면, 새로운 session을 생성해준다.
여기서 인자로는 boolean인 true, false가 올 수 있으며, true일 경우 session이 없으면 새로운 session을 생성해 반환해준다.
반면에 false이면, 기존 session이 존재하면 반환받는 것은 같지만 없을 경우 새로운 session을 생성하지 않고 null을 반환한다.


`HttpSession`은 request로 부터 들어온 Session을 컨트롤 할 수 있는 인터페이스 이다.
자주 사용되는 메서드 몇가지가 존재한다.

| HttpSession | Method |
| --- | --- |
| setAttribute(String name, Object object) | 세션을 이름과 객체로 매핑한다. |
| getAttribute(String name) | name 으로 매핑된 세션을 가져온다. |
| getId() | 세션 아이디(JSESSIONID)값을 가져온다. |
| getMaxInactiveInterval() | 세션의 최대 유효시간을 가져온다. |
| setMaxInactiveInterval(int millisecond) | 세션의 최대 유효시간을 설정한다. |
| invalidate() | 등록된 세션을 삭제한다. |

등의 다양한 메서드 들이 존재한다.
더 많은 메서드가 궁금한 분은 [여기](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpSession.html)

참고로 `RedirectAttributes`는 redirect 할 경우 데이터를 전달해 주는 클래스로서,
`addFlashAttribute`는 redirect 할 경우 한번만 전달이 된 후 사라진다.

이렇게 위의 Controller에 대한 코드 설명은 끝이 났고, `session.setAttribute`를 통해 보낸 객체를 사용하는 법을 알아보도록 하겠다.

### index Controller

```java

@GetMapping(value = "index")
    public void indexGET(@SessionAttribute(name = "loginUser", required = false)UserDTO loginUser, Model model) {
        log.info("Controller indexGET");

        model.addAttribute("loginUser", loginUser);
    }
    
```

<br>

`@SessionAttribute`는 이 애노테이션에 **설정한 이름에 해당하는 모델 정보를 자동으로 세션에 넣어주는** 애노테이션이다. 
이 애노테이션을 통해 세션이 있으면 자동으로 받아와 Model을 통해 넘겨준다.

### HTML(Thymeleaf)
```html
<th:block th:if="${loginUser == null}">
    <div class="header_columns">
        <div class="header_columns_column">
            <a th:href="@{join}">회원가입</a>
        </div>
        <div class="header_columns_column">
            <a th:href="@{login}">로그인</a>
        </div>
    </div>
</th:block>
<th:block th:unless="${loginUser != null}">
    <div class="header_columns">
        <div class="header_columns_column">
            <a th:href="@{join}">새 글 쓰기</a>
        </div>
        <div class="header_columns_column">
            <a th:href="@{index}" th:text="${loginUser.userId}">userId</a>
        </div>
        <div class="header_columns_column">
            <form th:action="@{logout}" method="post">
                <button type="submit">로그아웃</button>
            </form>
        </div>
    </div>
</th:block>

```

<br>

HTML 코드는 위와 같이 작성 할 수있다. 

**로그인 전**
![image](https://user-images.githubusercontent.com/73057935/163809530-c3fcdee4-c396-41ff-9cb8-b04c3ccbf940.png)

<br>

**로그인 후**
![image](https://user-images.githubusercontent.com/73057935/163809587-d26c84ea-6b68-47ec-ac2e-d5f849e50a9b.png)


### logout Controller

```java

@PostMapping(value = "logout")
public String logoutGET(HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.invalidate();

    return "redirect:/index";
}

```

<br>

로그아웃 컨트롤러는 매우 간단하다.
세션을 가져와서 그 세션은 invalidate 해주면 된다.

<br>

**Good!!**