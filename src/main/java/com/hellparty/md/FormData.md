#[Javascript] FormData 란? (Ajax 이미지 첨부)

Ajax를 이용한 비동기처리로 이미지 첨부를 개발하다가 궁금하게 되어 알아보았고,
내가 궁금했던 내용들을 기록한 것이다.

### FormData란?

[FormData - Web API | MDN](https://developer.mozilla.org/ko/docs/Web/API/FormData)

`FormData`란 form 필드와 그 값을 나타내는 일련의 key/values 쌍을 쉽게 생성할 수 있는 방법을 제공하는 인터페이스 이다.


### 그렇다면 언제 왜 사용하느냐?

보통의 Ajax 통신에는 `FormData`를 잘 사용하지 않는다고 한다.
왜냐하면 주로 `JSON`을 이용한 `Key-Values` 데이터 전송을 하기 때문이다.

<br>

하지만 Ajax를 통한 비동기 통신(페이지 전환 없이)으로 이미지를 업로할 경우 `FormData`객체를 사용한다.
혹은 페이지 전환 없이 폼 데이터를 사용하고 싶은 경우에도 사용한다고 한다.

### FormData Method

| FormData | Method |
| --- | --- |
| FormData.append(name, value) | FormData 객체안에 이미 키가 존재하면 그 키에 새로운 값을 추가하고, 키가 없으면 추가 |
| FormData.delete(name) | FormData 객체로부터 Key-Values 쌍을 삭제 |
| FormData.entries() | 객체에 담긴 모든 Key-Values 쌍을 순회할 수 있는 iterator을 반환 |
| FormData.get(name) | 객체 내의 값들 중 주어진 키와 연관된 첫번째 값을 반환 |
| FormData.getAll(name) | 객체 내의 값들 중 주어진 키와 연관된 모든 값이 담긴 배열을 반환 |
| FormData.has(name) | 객체에 특정 키가 있는지 확인 boolean 반환 |
| FormData.keys() | 객체에 담긴 모든 Key-Values 순회 할 수 있는 iterator 반환 |
| FormData.set(name, value) | 객체 내에 있는 기존 키에 새 값을 설정 혹은 존재하지 않을 경우 Key-Values 추가 |
| FormData.values() | 객체에 포함된 모든 Values 순회 할 수 있는 iterator 반환 |

<br>

해당 메서들의 예시는 구글링 혹은 공식 사이트에 들어가면 자세하게 나와있다.

### FormData Ajax 예시

```javascript

fileInput.addEventListener("change", function () {
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
                console.log(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    });
```

<br>

위의 코드에서 봐야 할 부분은 `enctype: 'multipart/form-data`, `processData: false`, `contentType: false` 세가지이다.

<br>

일반적으로 폼에 `enctype`을 별도로 지정하지 않을 경우 `application/x-www-form-urlencoded` 타입으로 전송된다.
`application/x-www-form-urlencoded`란 데이터 형식이 **key=value&key=value**의 형태로 전달되는 HTML form의 기본 Content-Type이다.
참고로 자주 사용되는 `application/json`은 데이터 형식이 **{key: value}** 형태이다.

<br>

따라서 MDN에서도 바이너리 데이터를 사용하기에 적절하지 않기에 `multipart/form-data`를 사용해야 한다고 한다.
`multipart/form-data`는 파일 업로드가 있는 양식요소에 사용되는 enctype 속성 값들 중 하나로, multipart는 폼 데이터가 여러 부분으로 나뉘어 서버로 전송되는 것을 의미한다.

<br>

`processData: false`는 Jquery 내부적으로 쿼리 스트링을 만들어 data 파라미터를 전송하는데, 파일 전송에서는 이를 피해야 한다고 한다.
`contentType: false`는 default 값이 앞서 이야기한 `application/x-www-form-urlencoded`이므로 false를 설정해 주는 것이 맞다.

### FormData가 궁금했던 이유

내가 궁금했던 이유는 어떠한 이유에서 FormData 객체를 사용하는가 첫번째 이고,
두번째로는 Ajax 통신에 성공한 후 FormData 객체에 내가 원하는 값이 제대로 설정 되었는지 확인하는 방법이다.

<br>

 Ajax 통신에 관한 부분은 아직 부족하므로 항상 알아가야 되는 것으로 위에 이야기 했던 부분으로 충분히 충족하였다.
첫번째 궁금증 또한 이 글을 작성하면서 해결이 되었다.

<br>

실제 서버를 가동시켜 이미지 업로드를 하고
`formData.append("uploadFile", file)` 이 코드를 통해 formData에  key가 uploadFile 이고, 업로드한 file 객체를 확인하고 싶었다.
그래서 console.log 를 통해 열심히 console 창에 들어온 file 객체 값을 찍어보려 했지만 실패하였다.
그 이유는 한 블로그에서 찾을 수 있었다.

[https://inpa.tistory.com/entry/JS-%F0%9F%93%9A-FormData-%EC%A0%95%EB%A6%AC-fetch-api](https://inpa.tistory.com/entry/JS-%F0%9F%93%9A-FormData-%EC%A0%95%EB%A6%AC-fetch-api)

<br>

> 자바스크립트(Javascript)에서 formdata의 데이터를 조작할 때가 있습니다. FormData 객체란 단순한 객체가 아니며 XMLHttpRequest 전송을 위하여 설계된 특수한 객체 형태입니다. 그러기에 문자열 화할 수 없는 객체이며 Console.log를 사용하여 확인이 불가능합니다.

<br>

**FormData console 확인 방법**

<br>

```javascript
//FormData의 메소드 확인하기
var formData = new FormData();
formData.append('key1', 'value1');
formData.append('key2', 'value2');
 
// Display the key/value pairs
for (var pair of formData.entries()) {
    console.log(pair[0]+ ', ' + pair[1]); 
}
```

<br>
<br>
<br>

이상으로 글을 마치겠다.

---

<br>
<br>

**참조 블로그**

[https://inpa.tistory.com/entry/JS-%F0%9F%93%9A-FormData-%EC%A0%95%EB%A6%AC-fetch-api](https://inpa.tistory.com/entry/JS-%F0%9F%93%9A-FormData-%EC%A0%95%EB%A6%AC-fetch-api)
[https://2ham-s.tistory.com/307](https://2ham-s.tistory.com/307)
[https://jw910911.tistory.com/117](https://jw910911.tistory.com/117)