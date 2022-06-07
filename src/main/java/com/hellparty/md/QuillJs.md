# [Spring Boot] Quill.js + 이미지 업로드 (위지윅 에디터 이미지 업로드)

[참조 블로그](https://dkfkslsksh.tistory.com/37)

위의 블로그에 나와있는 JS 콜백 함수를 이용한 방법을 참조하여 구현하였습니다.

---

<br>

위지윅 에디터에는 종류가 몇가지 존재한다.
CkEditor, Summernote, 네이버에서 제공하는 스마트 에디터(?) 등이 있다.

위지윅에 대한 글은 전에 작성해 두었다. 참고 바란다.

[위지윅이란? CK Editor 사용하기](https://myeongdev.tistory.com/32?category=890822)

<br>

현재 위지윅 에디터를 이용하여 글을 작성하는 페이지를 구현하고 있는데 내가 공부해서 알고 있던 내용은
input 태그를 통해 이미지를 업로드하고 View에 출력하는 것이었다.

<br>

하지만 위의 방식을 사용하면 위지윅 에디터를 제대로 사용 못한다고 생각이 들었고,
사용자 또한 본인의 게시글에 이미지를 첨부하기에 어려움이 따른다고 생각되었다.

<br>

그래서 일단 비교적 구글에 자료가 많은 CkEditor을 이용해서 이미지 업로드를 구현하려고 했다.
하지만 CkEditor4만 자료가 많을 뿐 내가 원했던 방식은 CkEditor5는 별로 없었다.
그리고 CkEditor5는 npm install 등 설정 방식이 꽤나 복잡하게 느껴졌다.
오히려 Spring을 통한 구현이 가능한 CkEditor4 방식이 훨씬 쉬워보였다.

<br>

하지만 결정적으로 Quill.js를 사용하여 구현한 이유는 CkEditor4의 이미지 업로드 방식이 마음에 들지 않았다.
서버로 전송 -> 이미지 업로드(?) 자세히 기억이 나지 않지만 몇가지 절차를 거친다는 것이 내키지 않았다.
그래서 맨 위 참조 블로그 방식을 보고 직접 구현해 보기로 했다.

### Quill.js 적용하기

**HTML**

```html
<link href="//cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
<script src="//cdn.quilljs.com/1.3.6/quill.js"></script>
```

```html
<div id="editor"></div>
```

<br>

**JavaScript**

```javascript
function quilljsediterInit(){
    var option = {
        modules: {
            toolbar: [
                [{header: [1,2,false] }],
                ['bold', 'italic', 'underline'],
                ['image', 'code-block'],
                [{ list: 'ordered' }, { list: 'bullet' }]
            ]
        },
        placeholder: '자세한 내용을 입력해 주세요!',
        theme: 'snow'
    };

    quill = new Quill('#editor', option);
}

quilljsediterInit();

```

<br>

[Quill.js 공식 사이트](https://quilljs.com)

위와 같이 적용하면 Quill.js 위지윅 에디터는 쉽게 적용이 가능하다.
아마 Quill.js 에서 더 많은 기능을 제공하고 다양한 설정 방법이 존재 할 것이다.

<br>

필자는 해당 기능만을 사용하였고, CDN 방식을 사용하였다.
더 다양한 기능과 공식문서 등은 공식 사이트를 참조 바란다.

<br>

> 서버로 전송하는 것에 대한 문제점

<br>

일단 Quill.js가 div태그에만 적용되는 것인지는 잘 모르겠다.
하지만 내가 찾아본 바로는 div태그에 적용시켜 사용하는 것으로 보였다.

<br>

여기서 나에게 문제점이 존재했다.
위지윅이라고는 CkEditor만 사용하여 순수 텍스트만 서버에 넘겨주고 저장했던것이 다였다.
그런데 div태그를 통해 서버에 전송하려 하니 잘 되지 않았다.
그래서 구글링을 통해 방법을 찾았다.

<br>

[https://stackoverflow.com/questions/40432577/quilljs-doesnt-work-with-textarea](https://stackoverflow.com/questions/40432577/quilljs-doesnt-work-with-textarea)

스택오버플로우에 내가 고민했던 질문 글이 있었고, 그 글에는 친절한 답변이 몇가지 달려 있었다.
그 중 가장 쉽게 적용이 가능한 방법으로 해당 문제점을 해결하였다.

<br>

**HTML**

```html
<div id="editor"></div>
<input type="hidden" id="quill_html" name="content">
```

<br>

**JavaScript**

```javascript
quill = new Quill('#editor', option);
quill.on('text-change', function() {
    document.getElementById("quill_html").value = quill.root.innerHTML;
});
```

<br>

위처럼 히든타입의 input 태그를 하나 추가해주고,
Quill.js의 메서드인 on메서드를 통해서 Quill.js안의 내용을 input태그에 넣어주는 것이다.
해당 방법으로 진행하니 DB에 저장되는 것에 이상이 없었다.

### Ajax 콜백 함수

```java
quill.getModule('toolbar').addHandler('image', function () {
        selectLocalImage();
    });
```

<br>

quill 에디터 선언할 때 해당 코드를 넣어줘 toolbar의 image를 컨트롤 할 수있게 해준다.

[Quill.js API 문서](https://quilljs.com/docs/api/)

<br>

```javascript

function selectLocalImage() {
    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    console.log("input.type " + fileInput.type);

    fileInput.click();

    fileInput.addEventListener("change", function () {  // change 이벤트로 input 값이 바뀌면 실행
        const formData = new FormData();
        const file = fileInput.files[0];
        formData.append('uploadFile', file);

        $.ajax({
            type: 'post',
            enctype: 'multipart/form-data',
            url: '/board/register/imageUpload',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'json',
            success: function (data) {
                const range = quill.getSelection(); // 사용자가 선택한 에디터 범위
                // uploadPath에 역슬래시(\) 때문에 경로가 제대로 인식되지 않는 것을 슬래시(/)로 변환
                data.uploadPath = data.uploadPath.replace(/\\/g, '/');
                
                quill.insertEmbed(range.index, 'image', "/board/display?fileName=" + data.uploadPath +"/"+ data.uuid +"_"+ data.fileName);

            },
            error: function (err) {
                console.log(err);
            }
        });

    });
}

```

<br>

먼저 Ajax 통신시에 `enctype`, `formData` 등에 대한 설명은 전 글에 작성해 두었다.

이미지 업로드에 대한 Ajax 통신에 궁금증이 들면 한번 참고 바란다.

[[Javascript] FormData 란?(Ajax 이미지 첨부)](https://myeongdev.tistory.com/48)


<br>
<br>

`quill.insertEmbed`는 quill에서 제공하는 메서드로 content를 지정 범위 안에 삽입할 때 사용하는 메서드이다.

<br>

```javascript
insertEmbed(index: Number, type: String, value: any, source: String = 'api'): Delta
```

<br>

공식 문서에서 위와 같이 사용하라고 나와있다.

그래서 `quill.getSelection()`을 통해 현재 에디터 안의 위치를 가져와 `image`타입을 설정해 주고, Controller를 통해 처리하도록 만들었다.

<br>

밑에 Javascript에 대한 전체 코드를 붙여 놓았다.

전체 코드가 필요하신 분은 아래 더보기를 눌러 확인하시면 된다.

**전체코드 더보기**

<details markdown="1">
<summary>접기/펼치기</summary>

```javascript

function quilljsediterInit(){
    var option = {
        modules: {
            toolbar: [
                [{header: [1,2,false] }],
                ['bold', 'italic', 'underline'],
                ['image', 'code-block'],
                [{ list: 'ordered' }, { list: 'bullet' }]
            ]
        },
        placeholder: '자세한 내용을 입력해 주세요!',
        theme: 'snow'
    };

    quill = new Quill('#editor', option);
    quill.on('text-change', function() {
        document.getElementById("quill_html").value = quill.root.innerHTML;
    });

    quill.getModule('toolbar').addHandler('image', function () {
        selectLocalImage();
    });
}

/* 이미지 콜백 함수 */

function selectLocalImage() {
    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    console.log("input.type " + fileInput.type);

    fileInput.click();

    fileInput.addEventListener("change", function () {  // change 이벤트로 input 값이 바뀌면 실행
        const formData = new FormData();
        const file = fileInput.files[0];
        formData.append('uploadFile', file);

        $.ajax({
            type: 'post',
            enctype: 'multipart/form-data',
            url: '/board/register/imageUpload',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'json',
            success: function (data) {
                const range = quill.getSelection(); // 사용자가 선택한 에디터 범위
                data.uploadPath = data.uploadPath.replace(/\\/g, '/');
                quill.insertEmbed(range.index, 'image', "/board/display?fileName=" + data.uploadPath +"/"+ data.uuid +"_"+ data.fileName);

            },
            error: function (err) {
                console.log(err);
            }
        });

    });
}

quilljsediterInit();

```
</details>


### Controller

먼저 업로드 파일을 저장하는 부분은 이전 글에 작성하였으므로 여기서 다시 작성하지 않도록 하겠다.

만약 파일 업로드에 대한 부분이 궁금하신 분들은 아래 링크를 참고 바란다.

[[Spring Boot] MultipartResolver 파일 업로드 - 1(Gradle, Ajax)](https://myeongdev.tistory.com/34)

[[Spring Boot] MultipartResolver 파일 업로드 - 2 (Date 폴더 생성, UUID)](https://myeongdev.tistory.com/35)

<br>

```java
@ResponseBody
@GetMapping(value = "/display")
public ResponseEntity<byte[]> showImageGET(
@RequestParam("fileName") String fileName
        ) {
        log.info("Controller showImageGET");

        log.info("fileName" + fileName);

        File file = new File("C:\\upload\\" + fileName);

        ResponseEntity<byte[]> result = null;

        try {

        HttpHeaders header = new HttpHeaders();

        /*
        Files.probeContentType() 해당 파일의 Content 타입을 인식(image, text/plain ...)
        없으면 null 반환

        file.toPath() -> file 객체를 Path객체로 변환

        */
        header.add("Content-type", Files.probeContentType(file.toPath()));

        result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        } catch (IOException e) {
        e.printStackTrace();
        }

        return result;
        }

```

<br>

위의 코드는 간단히 설명하면 `HttpHeader`에 `@Reqeustparam`을 통해 들어온 파일에 대한 `Content-type`,

`byte[]`타입으로 새롭게 `copy`된 파일 정보,

`HttpStatus.OK`라는 Http 상태코드를 반환해주는 Controller이다.

<br>

아래 글은 `ResponseEntity`에 대해 공부하면서 정리한 글이다. 혹시 궁금하다면 참고 바란다.

[[Spring Boot] HttpEntiy, ResponseEntity 란?](https://myeongdev.tistory.com/37)


### 실행 결과

![image](https://user-images.githubusercontent.com/73057935/172329673-323d8535-c753-4e11-ab84-84679f978c29.png)

<br> 

위의 사진은 에디터에 image toolbar를 클릭하여 사진을 첨부한 모습이고,

<br>

![image](https://user-images.githubusercontent.com/73057935/172330389-fe0810ec-b5bd-40d6-9688-d0d223035283.png)

아래는 성공적으로 DB에 저장된 모습이다.

<br>
<br>

이상으로 글을 마치겠다.