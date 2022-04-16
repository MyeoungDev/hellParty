package com.hellparty.domain;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    /* user 인덱스 (PK) */
    private int userIdx;

    @NotBlank(message = "아이디는 필수 입력사항 입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력사항 입니다.")
    private String userPw;

    @NotBlank(message = "이름은 필수 입력사항 입니다.")
    private String userName;

    @NotBlank(message = "이메일은 필수 입력사항 입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    /* user 등록일자 */
    private String regDate;
    /* admin 체크 여부 0: user, 1: admin */
    private int adminCk;

}
