package com.hellparty.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    /* PK */
    private int commentIdx;

    /* FK */
    private int boardIdx;

    /* 작성자 */
    private String userId;

    /* 내용 */
    private String content;

    /* 등록날짜 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regDate;


}
