package com.hellparty.mapper;

import com.hellparty.domain.CommentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void commentEnrollTest() {
        CommentDTO test = new CommentDTO();
        test.setBoardIdx(1);
        test.setUserId("mapperTest3");
        test.setContent("mapperTest3");
        int i = commentMapper.commentRegister(test);
        System.out.println("i = " + i);
    }

    @Test
    public void commentCountTest() {
        int boardIdx = 2;
        commentMapper.commentCount(boardIdx);
    }

    @Test
    public void commentListTest() {
        int boardId = 1;
        System.out.println(commentMapper.commentList(boardId));
    }

    @Test
    public void commentDeleteTest() {
        int commentIdx = 10;
        int i = commentMapper.commentDelete(commentIdx);
        System.out.println("결과 int  = " + i);
    }
}