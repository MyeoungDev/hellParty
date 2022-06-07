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