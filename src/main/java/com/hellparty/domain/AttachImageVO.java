package com.hellparty.domain;

import lombok.Data;

@Data
public class AttachImageVO {

    /* 경로 */
    private String uploadPath;

    /* UUID */
    private String uuid;

    /* 파일 이름 */
    private String fileName;

    /* boardIdx FK */
    private int boardIdx;

}
