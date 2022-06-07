package com.hellparty.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class BoardDTO {

    /* UserIdx  FK */
    private int userIdx;

    /* 작성자 */
    private String userId;

    /* BoardIdx  PK */
    private int boardIdx;

    /* 제목 */
    private String title;

    /* 운동 지역 */
    private String area;

    /* 내용 */
    private String content;

    /* 오픈 카톡 링크 */
    private String openLink;

    /* 등록 일자 */
    private Date regDate;

}
