package com.hellparty.controller;

import com.hellparty.domain.AttachImageVO;
import com.hellparty.domain.BoardDTO;
import com.hellparty.domain.UserDTO;
import com.hellparty.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping(value = "board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/register")
    public String registerGET(HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.info("getAttribute(loginUser)" + session.getAttribute("loginUser"));
        log.info("Controller registerGET");
        return "board/register";
    }

    /* TODO -> detail view 제목, 지역, 카톡링크 구현하기  */
    /* TODO -> 위의 모든 사항 구현 후 댓글 기능 구현하기 */

    @PostMapping(value = "/register.do")
    public String registerPOST(HttpServletRequest request, BoardDTO boardDTO, RedirectAttributes rttr) {

        log.info("Controller registerPOST");
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("loginUser");

        boardDTO.setUserIdx(userDTO.getUserIdx());
        boardService.boardRegister(boardDTO);

        rttr.addFlashAttribute("register_result", boardDTO.getUserIdx());

        log.info("게시글 등록 성공");

        return "redirect:/index";

    }

    @ResponseBody
    @PostMapping(value = "/register/imageUpload")
    public AttachImageVO imageUploadPOST(
            @RequestParam(name = "uploadFile") MultipartFile attachFile
    ) {

        log.info("Controller imageUpload");
        log.info("attachFile" + attachFile);

        AttachImageVO attachImageVO = new AttachImageVO();

        String uploadFolder = "C:\\upload";
        String uploadFileName = attachFile.getOriginalFilename();
        attachImageVO.setFileName(uploadFileName);

        /* 날짜 디렉토리 생성 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String formatDate = sdf.format(date);

        String datePath = formatDate.replace("-", File.separator);
        attachImageVO.setUploadPath(datePath);

        File uploadPath = new File(uploadFolder, datePath);

        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        /* UUID fileName 변경 */
        String uuid = UUID.randomUUID().toString();
        uploadFileName = uuid + "_" + uploadFileName;
        attachImageVO.setUuid(uuid);

        /* 저장할 파일 경로, 파일이름 지정 */
        File saveFile = new File(uploadPath, uploadFileName);

        /* 디렉토리에 saveFile 저장 */
        try {
            attachFile.transferTo(saveFile);

            /* 썸네일 파일 */
            File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);

            BufferedImage bo_img = ImageIO.read(saveFile);
            double ratio = 3;
            int width = (int) (bo_img.getWidth() / ratio);
            int height = (int) (bo_img.getHeight() / ratio);

            Thumbnails.of(saveFile)
                    .size(width, height)
                    .toFile(thumbnailFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return attachImageVO;
    }

    @ResponseBody
    @GetMapping(value = "/display")
    public ResponseEntity<byte[]> showImageGET(
            @RequestParam("fileName") String fileName
    ) {
        log.info("Controller showImageGET");

        log.info("fileName" + fileName);

        File file = new File("C:\\upload\\" + fileName);

        ResponseEntity<byte[]> result = null;

        try {

            HttpHeaders header = new HttpHeaders();

            /*
            Files.probeContentType() 해당 파일의 Content 타입을 인식(image, text/plain ...)
            없으면 null 반환

            file.toPath() -> file 객체를 Path객체로 변환

            */
            header.add("Content-type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping(value = "/detail")
    public void detailGET(@RequestParam(name = "boardIdx") int boardIdx, Model model) {
        log.info("Controller detailGET");
        BoardDTO boardDTO = boardService.boardDetail(boardIdx);

        model.addAttribute("boardDTO", boardDTO);
    }


}
