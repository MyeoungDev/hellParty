package com.hellparty.controller;

import com.hellparty.domain.CommentDTO;
import com.hellparty.domain.UserDTO;
import com.hellparty.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/comment/{boardId}")
    public List<CommentDTO> commentListGET(@PathVariable("boardId") int boardId) {
        List<CommentDTO> commentList = commentService.commentList(boardId);

        int commentAmount = commentService.commentCount(boardId);

        return commentList;
    }

    @PostMapping("/comment/register")
    public void commentRegisterPOST(HttpServletRequest request, int boardIdx, String content) {

        log.info("Controller commentRegisterPOST");

        CommentDTO commentDTO = new CommentDTO();

        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        commentDTO.setUserId(loginUser.getUserId());
        commentDTO.setBoardIdx(boardIdx);
        commentDTO.setContent(content);

        commentService.commentRegister(commentDTO);

    }

    @PostMapping("/comment/delete")
    public void commentDeletePOST(@RequestParam("commentIdx") int commentIdx, Model model) {
        log.info("Controller commentDeletePOST");

        commentService.commentDelete(commentIdx);

    }
}
