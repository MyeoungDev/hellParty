let userIdCheck = false;
let userPwCheck = false;
let userEmailCheck = false;


/* id 중복 체크 ajax*/
function idCheck() {
    let userId = document.querySelector("input[name=userId]").value;

    $.ajax({
        url: '/join/idCheck',
        type: 'post',
        data: {userId, userId},
        success: function (result) {
            console.log("성공여부 result " + result);
            if (result !== 'fail') {
                document.querySelector(".idCheck_fail").style.display = "none";
                document.querySelector(".idCheck_success").style.display = "block";
                userIdCheck = true;
            } else {
                document.querySelector(".idCheck_success").style.display = "none";
                document.querySelector(".idCheck_fail").style.display = "block";
                userIdCheck = false;
            }
        },
        error: function () {
            console.log("error");
        }
    });
}

/* Pw Check */

function pwCheck(obj) {

    let pw = document.querySelector("input[name=userPw]").value;

    console.log("obj " + obj.value);
    if (obj.value === pw) {
        console.log("성공");
        document.querySelector(".pwCheck_fail").style.display = "none";
        document.querySelector(".pwCheck_success").style.display = "block";
        userPwCheck = true;
    } else {
        console.log("실패");
        document.querySelector(".pwCheck_fail").style.display = "block";
        document.querySelector(".pwCheck_success").style.display = "none";
        userPwCheck = false;
    }
}


/* mail send And check*/

let mailCheckNum = "";

document.querySelector(".join_email_btn.submit").addEventListener("click", function () {
    console.log("test");
    let email = document.querySelector("input[name=userEmail]").value;

    if (email === '' || email == null) {
        alert("이메일을 입력해 주세요.");
    } else {
        $.ajax({
            url: '/mailSend',
            type: 'post',
            data: {email, email},
            success: function (result) {
                console.log("성공");
                console.log("mailCheckNum: " + result);
                mailCheckNum = result;
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
});

/* mailCheck */

document.querySelector(".join_email_btn.check").addEventListener("click", function () {

    if (mailCheckNum === "" || mailCheckNum == null) {
        return;
    } else {
        let userInput = document.querySelector(".join_email.check").value;
        if (userInput === mailCheckNum) {
            document.querySelector(".mailCheck_fail").style.display = "none";
            document.querySelector(".mailCheck_success").style.display = "block";
            userEmailCheck = true;
        } else {
            document.querySelector(".mailCheck_fail").style.display = "block";
            document.querySelector(".mailCheck_success").style.display = "none";
            userEmailCheck = false;
        }
    }
});

/* Validation */

/* TODO -> 현재 문제
*   1. 아래의 JS로 드디어 아이디 중복, 비밀번호 확인, 이메일 인증 3가지가 안되면 submit이 안되게 막음
*   2. 그런데 문제는 새로고침하면 저런 체크들이 모두 false로 돌아가서 사용자가 다시 진행해야 됨....
*   3. 우선 이걸로 만족하고 로그인 기능으로 넘어가자.....
* 
* */

const submitBtn = document.getElementById("joinBtn");

submitBtn.addEventListener("click", function () {
    let userId = document.querySelector("input[name=userId]").value;
    let userPw = document.querySelector("input[name=userPw]").value;
    let userEmail = document.querySelector("input[name=userEmail]").value;

    if (userId !== "" && userIdCheck === false) {
        alert("아이디 중복을 확인해 주세요.")
        event.preventDefault();
        event.stopPropagation();
        event.stopImmediatePropagation();
        return;
    }

    if (userPw !== "" && userPwCheck === false) {
        alert("비밀번호 확인이 잘못 되었습니다.");
        event.preventDefault();
        event.stopPropagation();
        event.stopImmediatePropagation();
        return;
    }

    if (userEmail !== "" && userEmailCheck === false) {
        alert("이메일 인증을 확인해 주세요.");
        event.preventDefault();
        event.stopPropagation();
        event.stopImmediatePropagation();
        return;
    }
});