# [Spring Boot] 회원가입 Validation 처리하기

현재 나의 첫 프로젝트를 진행 중, 회원가입 Validation 처리를 하고 싶었다.
Validation 처리를 Javascript로 처리를 할까 생각도 들었지만,
전에 Javascript로 Validation을 처리 해 보았을 때 너무 노가다성이 짙고, 실수로 처리가 부족한 경우 submit 되버려 DB에 등록이 되는 경우도 생겼었다.
그렇기에 Spring Boot에 Validation 라이브러리가 있다는 걸 알고 한번 사용해 보기로 하였다.
내가 구현하고 싶은 것들은 단지 Empty일 때 그리고 Email 형식 Validation을 원했고 그렇게 사용 해보도록 하겠다.
그렇기에 이 글은 **나처럼 처음 사용하는 사람이 따라하기에 적합할 것**이다.

[참고 블로그](https://victorydntmd.tistory.com/332)

### Gradle 의존성 주입

```
implementation 'org.springframework.boot:spring-boot-starter-validation:2.6.3'	/* Validation */
```

<br>

spring boot 2.3 version 이상부터는 `spring-boot-starter-web` 의존성 내부에 있던 `Validation`이 사라졌다고 한다.

즉, version 2.3.x 이상의 버전을 쓰는 분은 위와 같이 의존성을 따로 추가해 주어야 한다.

### UserDTO

```java

package com.hellparty.domain;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    /* user 인덱스 (PK) */
    private int userIdx;

    @NotBlank(message = "아이디는 필수 입력사항 입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력사항 입니다.")
    private String userPw;

    @NotBlank(message = "이름은 필수 입력사항 입니다.")
    private String userName;

    @NotBlank(message = "이메일은 필수 입력사항 입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    /* user 등록일자 */
    private String regDate;
    /* admin 체크 여부 0: user, 1: admin */
    private int adminCk;

}

```

Validation을 처음 사용해보기에 최대한 간단하게만 진행해 보았다.

내가 사용한 Annotation은 `@NotBlank`, `@Email`으로 2가지 이다.
하지만 Validation 라이브러리는 다양한 직관적인 Annotation을 제공해주므로 필요에 찾아보며 사용하는 것이 좋아보인다.
대표적으로 많이 사용되는 몇가지를 같이 알아보겠다.
참고로 여기서 `(message = "")`는 에러일 때 나타낼 메세지이다.

#### Annotation 종류

| Annotation | 제약 조건 |
| --- | --- |
| @AssertTrue | 해당 값이 **True**일 경우만 허용 |
| @AssertFalse | 해당 값이 **False**일 경우만 허용 |
| @Future | 해당 날짜가 현재보다 **미래**일 경우만 허용 |
| @Past | 해당 날짜가 현재보다 **과거**일 경우만 허용 |
| @Max(value) | 지정된 value 보다 **이하**일 경우만 허용 |
| @Min(value) | 지정된 value 보다 **이상**일 경우만 허용 |
| @Positive | 해당 값이 **양수**일 경우만 허용 |
| @Negative | 해당 값이 **음수**일 경우만 허용 |
| @Email | **이메일 형식**만 허용 |
| @Pattern(regex) | 해당 값이 **regex 정규식을 통과**하는 경우만 허용 | 
| @NotNull | 해당 값에 **Null 불가** |
| @Null | 해당 값 **Null만 허용** |
| @NotEmpty | 해당 값 **Null, 빈 문자열 불가** |
| @NotBlank | 해당 값 **Null, 빈 문자열, 스페이스만 있는 문자열 불가**|

그 외에도 실수값에 대한 조정, 양수와 0을 포함한 경우 허용 등등 몇가지의 경우가 더 존재한다.
또한 각 Annotation에 value, regex, message 등 몇가지 설정들도 존재하기에 필요할 경우 구글에 찾아서 사용하는게 적합할 것 같다.

### Controller

**기존 joinController**

```java

@GetMapping(value = "join")
    public void joinGET() {
        log.info("Controller joinGET");
    }


    @PostMapping(value = "join")
    public String joinPOST(UserDTO userDTO) throws Exception {
        log.info("Controller joinPOST");

        userService.userJoin(userDTO);

        log.info("join 성공");

        return "redirect:/index";
    }

```
<br>

아무런 Validation 처리가 되지 않은 Controller 에서부터 하나씩 차근차근 넘어가 볼 것이다.
위의 Controller 코드는 정말 아무것도 없는 컨트롤러다.
우리는 이제 몇줄의 코드를 추가해서 Validation을 완성 시킬 것이다.

처음으로 추가해 줄 코드는 PostMapping의 Parameter들이다.

<br>

```java
    public String joinPOST(@Valid UserDTO userDTO, Errors errors, Model model) throws Exception {
```

<br>

먼저 여기서 주목할 것은 `@Valid`, `Errors` 두 가지이다.
`@Valid`는 클라이언트로 부터 들어오는 `UserDTO`의 정보들에 유효성 검사를 실시하라는 의미의 애노테이션이다.
즉, 들어오는 데이터들이 우리가 앞서 DTO 에서 `@NotEmpty`로 설정했던 Validation 을 검사하며 들어오게 되는 것이다.
그리고 그 유효성 검사에서 통과되지 못하게 되면 `Errors` 객체를 통해 에러가 발생하게 되는 것이다.

[Errors 공식문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Errors.html)

이 Errors 객체에는 Validation 에서의 에러 처리에 대한 많은 메서드 들이 존재하기 때문에 상황에 따라 필요한 메서드들을 찾아 사용하면 될 것이다.

### Validation 적용하기

1. Controller로만 Validation 적용하기.
2. Controller에 있는 코드 Service로 분리하기.

위와 같은 방식으로 진행 하겠다.

**Controller**
```java

@GetMapping(value = "join")
public void joinGET(UserDTO userDTO) {
    // 여기서 UserDTO를 받아줘야 회원가입 실패시 그 입력값이 그대로 유지된다.
    // 즉, 기존에 처음 페이지에 들어갈 때는 userDTO가 parameter로 들어오지 않으니 무시되고,
    // 회원가입 실패시, UserDTO를 받은 Get요청이 이루어지면서 model을 통해 넘어온 값이 parameter 로 받아지게 된다.
    log.info("Controller joinGET");
}
        
@PostMapping(value = "join")
    public String joinPOST(@Valid UserDTO userDTO, Errors errors, Model model) throws Exception {
        log.info("Controller joinPOST");

        /* post요청시 넘어온 user 입력값에서 Validation에 걸리는 경우 */
        if (errors.hasErrors()) {
            /* 회원가입 실패시 입력 데이터 유지 */
            model.addAttribute("userDTO", userDTO);
            /* 회원가입 실패시 message 값들을 모델에 매핑해서 View로 전달 */
            Map<String, String> validateMap = new HashMap<>();

            for (FieldError error : errors.getFieldErrors()) {
                String validKeyName = "valid_" + error.getField();
                validateMap.put(validKeyName, error.getDefaultMessage());
            }

            // map.keySet() -> 모든 key값을 갖고온다.
            // 그 갖고온 키로 반복문을 통해 키와 에러 메세지로 매핑
            for (String key : validateMap.keySet()) {
                    model.addAttribute(key, validateMap.get(key1));
            }
            return "join";
        }
        userService.userJoin(userDTO);
        log.info("join 성공");

        return "redirect:/index";
    }

```

위의 코드에 모든 주석으로 설명을 달아두었다.
먼저 GetMapping 부터 살펴보면 
```java
@GetMapping(value = "join")
public void joinGET(UserDTO userDTO)
 ```
위의 코드와 같이 `UserDTO` 를 인자로 받고있는 모습이다.

그 이유는 유효성 검사를 통과하지 못한 ErrorMessage 를 View에 출력함과 동시에,
회원가입 시 실패하게 되면 유저가 입력해 놓은 값들이 유지되게 하는 것이다.
만약 당신이 어느 사이트에 회원가입을 다 했는데 어떤 오류로 인해 다시 처음부터 해야된다면?
상당히 짜증나는 경험일 것이고, 한번쯤 경험해 보았을 것이라고 생각한다.
그렇기에 그것들을 방지해 주는것이다. 
내가 사용하기 불편하면 다른 사람들 또한 똑같이 불편할 것이다.

<br>
그 이후 PostMapping을 살펴보면

```java
if (errors.hasErrors()) {
```

위와 같은 if문 안에 모든 로직이 처리되어있다.
`errros.hasErrors()`는 유효성 검사를 통과하지 못하면 에러를 반환받게 되는데,
그 에러가 존재하면 즉, 유효성 검사를 통과하지 못하게 되면 아래와 같은 코드들이 실행되게 된다.
아래 코드들은 기존 유저의 입력값을 유지하는 코드와, `Map<>`을 통해 에러 메세지를 매핑하는 코드이다.

여기서 errors의 몇가지 메서드를 알아보겠다.

<br>

**`errors.getFieldErrors()`: 유효성 검사에 실패한 필드 목록을 가져온다.**

**`errors.getField()`: 유효성 검사에 실패한 필드명을 가져온다.**

**`error.getDefaultMessage()`: 유효성 검사에 실패한 필드에 정의된 메시지를 가져온다.**

### Service 분류

위와 같이 Validation이 단순한 경우라면 따로 Service에 분류하지 않아도 된다.
단, Validation이 복잡해지고 코드가 많아지면 Controller 코드가 지저분해지고, 복잡해지게 된다.
그렇기에 Service에 메서드를 만들어 끌어다 쓰는 방법이 옳다고 생각이 된다.

<br>

**Service**

```java
@Override
public Map<String, String> validateHandler(Errors errors) {
    Map<String, String> validateResult = new HashMap<>();

    for (FieldError error : errors.getFieldErrors()) {
        String validKeyName = "valid_" + error.getField();
        validateResult.put(validKeyName, error.getDefaultMessage());
    }


    return validateResult;
}

```

<br>

위의 Service 코드는 단순히 에러와 에러 메세지를 매핑하여 `Map<>`을 반환하게 만들어 두었다.

**Controller**

```java
@PostMapping(value = "join")
public String joinPOST(@Valid UserDTO userDTO, Errors errors, Model model) throws Exception {
    log.info("Controller joinPOST");

    /* post요청시 넘어온 user 입력값에서 Validation에 걸리는 경우 */
    if (errors.hasErrors()) {
        /* 회원가입 실패시 입력 데이터 유지 */
        model.addAttribute("userDTO", userDTO);
        /* 회원가입 실패시 message 값들을 모델에 매핑해서 View로 전달 */
        Map<String, String> validateResult = userService.validateHandler(errors);
        // map.keySet() -> 모든 key값을 갖고온다.
        // 그 갖고온 키로 반복문을 통해 키와 에러 메세지로 매핑
        for (String key : validateResult.keySet()) {
            // ex) model.addAtrribute("valid_id", "아이디는 필수 입력사항 입니다.")
            model.addAttribute(key, validateResult.get(key));
        }
        return "join";
    }

    userService.userJoin(userDTO);

    log.info("join 성공");

    return "redirect:/index";
}

```

그렇게 하면 위와같이 Controller 코드를 작성할 수 있다. 
다르게 변한 로직은 없고 단순히 Controller에서의 코드를 Service에 넘겨준 것 뿐이다.
하지만 이렇게 해서 나중에 Validation 처리가 더 복잡해지고 코드가 길어질 때 유연하게 사용 가능할 것이라고 생각한다.
마지막으로 실제 동작하는 것을 보고 마무리 하겠다.

<br>

![image](https://user-images.githubusercontent.com/73057935/163668611-05f24152-7388-46b6-ba6f-ccb89bc50ead.png)

![image](https://user-images.githubusercontent.com/73057935/163668574-4c807a52-4767-436d-bb0e-ef65373223b8.png)

**Good!!**
