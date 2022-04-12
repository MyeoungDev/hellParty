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
            } else {
                document.querySelector(".idCheck_success").style.display = "none";
                document.querySelector(".idCheck_fail").style.display = "block";
            }
        },
        error: function () {
            console.log("error");
        }
    });
}

/* Pw 확인 */

function pwCheck(obj) {

    let pw = document.querySelector("input[name=userPw]").value;

    console.log("obj " + obj.value);
    if (obj.value === pw) {
        console.log("성공");
        document.querySelector(".pwCheck_fail").style.display = "none";
        document.querySelector(".pwCheck_success").style.display = "block";
    } else {
        console.log("실패");
        document.querySelector(".pwCheck_fail").style.display = "block";
        document.querySelector(".pwCheck_success").style.display = "none";
    }
}

document.querySelector(".join_email_btn.submit").addEventListener("click", function () {
    console.log("test");
    let email = document.querySelector("input[name=userEmail]").value;
    $.ajax({
        url: '/mailSend',
        type: 'post',
        data: {email, email},
        success: function (result) {
            console.log("성공");
            console.log(result);
        },
        error: function (error) {
            console.log(error);
        }

    });
});