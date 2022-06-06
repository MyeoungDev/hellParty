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

위처럼 히든타입의 input 태그를 하나 추가해주고, 
Quill.js의 메서드인 on메서드를 통해서 Quill.js안의 내용을 input태그에 넣어주는 것이다.
해당 방법으로 진행하니 DB에 저장되는 것에 이상이 없었다.

### Ajax 콜백 함수



formData.append('item','hi'); // <input name="item" value="hi"> 와 같다.
formData.append('item','hello'); // <input name="item" value="hello">
append() 메소드로 key-value 값을 하나씩 추가해주면 같은 key를 가진 값을 여러 개 넣을 수 있습니다. (덮어씌워지지 않고 추가가 됩니다.)
참고로 값은 "문자열"로 자동 변환 됩니다.
출처: https://inpa.tistory.com/entry/JS-📚-FormData-정리-fetch-api [👨‍💻 Dev Scroll:티스토리]


출처: https://inpa.tistory.com/entry/JS-📚-FormData-정리-fetch-api [👨‍💻 Dev Scroll:티스토리]

-> 어쩐지 값 봐보려고 JSON.stringify 아무리 찍어도 안나오더라...


mulipart/form-data바이너리 데이터를 효율적으로 전송할 수 있습니다.form의 속성중 (이미지나 파일 전송 시) enctype이 반드시 'multipart/form-data'형 이어여만 합니다.


