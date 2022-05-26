# [Spring Boot] @ResponseBody 사용하기 (Spring 에서 Ajax, Json 통신하기)

Java에는 JSON 형식이 존재하지 않는다.
당연하다 JSON은 JavaScript Object Notation 이기 때문이다.
그래서 Spring에서 JSON 형식을 사용하기 위해 Jackson 혹은 Gson을 사용한다.

<br>

그렇다면 Javascript로 Ajax 혹은 Fetch API로 API를 개발 할 때,
Controller에서 데이터를 보내 주려면 JSON 형식으로 보내야 되는데 그 때 사용하는게 `@ResponseBody` 이다.

<br>

혹은 `@RestController`을 사용해도 된다. 
`@RestController`는 단순히 말하자면 `@Controller` + `@ResponseBody`이다.

### @ResponseBody란?

클라이언트 통신간 요청(Request)과 응답(Response)가 존재하는데,
이때 비동기통신(ex: Ajax)을 하기위해서는 Http body에 내용을 담아서 보내야 한다.
그 내용은 대표적으로 앞서 설명했던 JSON이다.

<br>

즉, 그렇다면 `@ResponseBody`은 서버와 클라이언트 **비동기통신에서 JSON 데이터를 보내줄 수 있게 하는 어노테이션이다.**

### @ResponseBody 사용하기

간단한 사용법 예시

**JavaScript Ajax**

```javascript
$.ajax({
            type: 'post',
            enctype: 'multipart/form-data',
            url: '/board/register/imageUpload',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log("data");

            },
            error: function (err) {
                console.log(err);
            }
        });
```

해당 코드는 Ajax 비동기통신을 이용해 이미지 파일을 업로드하는 형태이다.

**Controller**
````java
@ResponseBody
@PostMapping(value = "/register/imageUpload")
public void imageUpload(@RequestParam(name = "uploadFile") MultipartFile attachFile) {

        log.info("imageUpload Controller");

        String uploadFolder = "C:\\upload";
        String uploadFileName = attachFile.getOriginalFilename();

        File saveFile = new File(uploadFolder, uploadFileName);

        try {
            attachFile.transferTo(saveFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
````

위의 Controller 코드는 간단한 파일 업로드 코드이다.
만약 저기에서 `@ResponseBody` 어노테이션이 빠지게 된다면?

<br>

비동기통신에서 전달되어야 할 데이터가 전달이 되지 않고 `Internal Server Error`인 **500 Error**을 맛보게 될 것이다.

그리고 필자의 경우 Thymeleaf 뷰 템플릿을 사용하고 있는데

```org.thymeleaf.exceptions.TemplateInputException: Error resolving template [board/register/imageUpload], template might not exist or might not be accessible by any of the configured Template Resolvers ```

위와 같은 에러로 미친듯이 삽질을 해대며 templates 폴더에 html파일 경로 설정과 
Controller GetMapping의 return 경로 설정 등 미친듯이 손대고 삽질했다.

사실 이 글을 적는 이유도 알고 있던 내용 정처기 공부하고 오랜만에 하니까 까먹고 삽질 한거 화나서 적는다.
이상이다.

