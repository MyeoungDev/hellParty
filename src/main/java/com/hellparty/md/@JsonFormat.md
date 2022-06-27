# [Spring Boot] @JsonFormat으로 Date 타입 직렬화(Json Date 타입 포맷)

댓글 기능을 구현하던 중 댓글 등록 날짜를 표시하고 싶었다.
Json으로 데이터를 View에 보내 Ajax로 구현하던 중 View에서 내가 원하는 format 형식의 데이터가 잘 출력이 되지 않았다.
Controller에서 Date 타입을 `SimpleDateFormat`을 이용하여 가공하여 보내려고 해도 잘 되지 않았다.
그렇기에 구글링을 통해 `@JsonFormat`이라는 어노테이션을 알게 되었고 적용해 보았다.

<br>

[공식문서](https://www.javadoc.io/doc/com.fasterxml.jackson.core/jackson-annotations/2.9.8/com/fasterxml/jackson/annotation/JsonFormat.html)

<br>

> Common uses include choosing between alternate representations -- for example, whether Date is to be serialized as number (Java timestamp) or String (such as ISO-8601 compatible time value) -- as well as configuring exact details with pattern() property.

공식문서의 내용으로 일반적으로 **Date타입을 Number 혹은 String 형식으로 직렬화** 하는데 사용하고,
**pattern() property**을 통해 정확한 세부설정을 하기위해 사용한다고 한다.

<br>

### @JsonFormat 사용하기


**CommentDTO**

```java
package com.hellparty.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    /* PK */
    private int commentIdx;

    /* FK */
    private int boardIdx;

    /* 작성자 */
    private String userId;

    /* 내용 */
    private String content;

    /* 등록날짜 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regDate;


}

```

<br>

`JsonFormat`은 `Jackson` 라이브러리 어노테이션으로 
`spring-boot-stater-web` 의존성 주입이 되어 있으면 기본적으로 사용 가능하기에 따로 추가해줄 필요가 없다.
사용방법은 매우 간단하게 위와같이 Date 타입의 속성위에 `@JsonFormat` 어노테이션을 추가해 주면 된다.

### 적용하기

**@JsonFormat 적용 전**

![image](https://user-images.githubusercontent.com/73057935/175289104-69ef5865-ac34-4a95-a093-22fcc1a57293.png)

<br>

**@JsonFormat 적용 후**

![image](https://user-images.githubusercontent.com/73057935/175289370-31ac8969-028f-4ed9-9aa2-6d3e01ad9bc9.png)


<br>
<br>

다음 글을 내가 구현한 댓글 기능을 작성할지 아니면 계속 공부하면서 새롭게 알게 된 부분을 작성할지 고민이 된다.


