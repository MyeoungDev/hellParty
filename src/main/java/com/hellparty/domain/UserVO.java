package com.hellparty.domain;

import lombok.Data;

@Data
public class UserVO {

    /* user 인덱스 (PK) */
    private int userIdx;
    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    /* user 등록일자 */
    private String regDate;
    /* admin 체크 여부 0: user, 1: admin */
    private int adminCk;

    /* TODO -> 전체적으로 회원가입 마무리 Id 중복체크, Pw 확인 체크, MailSend 전체 결과 true 일때 회원가입 완료*/
    /* TODO -> git repository 만들기 and git ignore properties 추가히기 */
}
