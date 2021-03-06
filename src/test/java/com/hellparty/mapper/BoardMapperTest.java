package com.hellparty.mapper;

import com.hellparty.domain.BoardDTO;
import com.hellparty.domain.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardMapperTest {

    @Autowired
    private BoardMapper boardMapper;

    @Test
    public void boardRegister() {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setUserIdx(1);
        boardDTO.setUserId("test1");
        boardDTO.setTitle("mapperTest");
        boardDTO.setArea("mapperTest");
        boardDTO.setContent("mapperTest");
        boardDTO.setOpenLink("mapperTest");

        boardMapper.boardRegister(boardDTO);
    }

    @Test
    public void boardDetailTest() {
        int boardIdx = 1;
        boardMapper.boardDetail(boardIdx);
    }

    @Test
    public void heartInsert() {
        int userIdx = 2;
        int boardIdx = 2;
        boardMapper.heartInsert(userIdx, boardIdx);
    }

    @Test
    public void heartFind() {
        int userIdx = 2;
        int boardIdx = 2;
        boolean i = boardMapper.heartFind(userIdx, boardIdx);
        System.out.println("=======result========" + i);
    }

    @Test
    public void heartDelete() {
        int userIdx = 2;
        int boardIdx = 2;
        boardMapper.heartDelete(userIdx, boardIdx);

    }
}