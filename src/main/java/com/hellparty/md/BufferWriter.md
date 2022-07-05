# [Java] BufferReader, BufferWriter 사용하기(System.out.print 와 Scanner 가 느린 이유)

백준 알고리즘 공부하던 중 [빠른 A+B 15552번](https://www.acmicpc.net/problem/15552) 문제를 접하게 됐다.
그런데 일반적으로 사용한더 `Scanner` 와 `System.out.print`를 사용하게 되면 시간초과로 실패하게 되었다.
그렇기에 `BufferReader` 와 `BufferWriter`을 공부하고 사용하기로 마음 먹었다.

### System.out.println() 이 느린 이유

[https://donggov.tistory.com/53](https://donggov.tistory.com/53)

위의 글에 잘 정리되어 있다. 

간단히 축약하자면 `System`은 `Object` 클래스를 상속받은 final 클래스 이며,
`out`은 `PrintStream`의 인스턴스이고,
`println()`은 `PrintStream`의 메서드이기 때문이다.

<br>

즉, `System.out.println()`이 위의 과정을 모두 거치게 되기 때문인 것이다.

또한 `print()`함수와 `println()`함수의 시간차이도 존재하는데,
그 이유는 `println()`함수는 println -> print -> write() -> newLine() 식으로 진행되기 때문이라고 한다.

### Scanner 가 느린 이유

[https://st-lab.tistory.com/30](https://st-lab.tistory.com/30)
위 블로그 글에서 Scanner가 느린 이유에 대해서 찾을 수 있었다.

`Scanner`자체가 regular expression 정규식을 남발하기 때문이다.
`Scanner`를 통해 입력을 받으면 정규식을 거쳐 내부 메서드를 거쳐 리턴받기 때문이다.
실제로 Scanner 안에 `nextInt()`에 대한 정규식 메서드만해도 엄청나게 길었다.

<br>

그에 반해 `BufferReader`은 데이터를 입력받은것을 그대로 받기만 하기 때문에 속도 차이가 존재했다.

<br>

[[백준]여러가지 언어 입력속도 비교](https://www.acmicpc.net/blog/view/56)

위의 사이트에서 찾아보아도 기존에 사용하던 `System.out.print` 와 `Scanner`가 
`BufferWriter`, `BufferReader`에 비해 현저히 느리다는것을 확인할 수 있다.

### BufferReader 사용하기

먼저 `BufferReader` 와 `BufferWriter`은 버퍼(buffer)을 사용한다.
즉, 한번에 모아서 데이터를 보낸다고 생각하면 된다.

<br>

`BufferReader`는 입력받을 때 `readLine()`이라는 메서드를 사용하는데 리턴값이 String이다.
그래서 다른타입을 사용하기 위해서는 형변환을 사용해야 한다.

<br>

또한 예외처리를 필수적으로 해줘야 한다.
try catch 문을 사용하던가 혹은 throw IOException을 해줘야 한다.

<br>

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();
        int i = Integer.parseInt(br.readLine());

        StringTokenizer st = new StringTokenizer(str);
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
    }
}
```

<br>

또한 `BufferReader`는 Line 단위 즉, 한 줄 단위로 입력을 받는다.
공백단위로 입력을 받으려고 한다면 `StringTokenizer`을 사용해야 한다.

### BufferWriter 사용하기

`BufferWriter`은 출력시 `write()`라는 메서드를 사용한다.
하지만, 자동 개행이 존재하지 않기 때문에 "\n"을 사용해 직접 개행을 해줘야 한다.

또한 `BufferWriter`의 경우 마지막에 반드시 `flush()` 와 `close()`를 통하여 버퍼를 비운뒤에  닫아줘야 한다.
그리고 IOException을 발생하기 때문에 에외처리를 해줘야한다.

<br>

```java
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        
        String str = "abcdefg";
        
        bw.write(str);
        bw.flush();
        bw.close();
    }
}
```



