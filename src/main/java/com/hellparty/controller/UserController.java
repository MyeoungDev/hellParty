package com.hellparty.controller;

import com.hellparty.domain.UserDTO;
import com.hellparty.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "index")
    public void indexGET(@SessionAttribute(name = "loginUser", required = false)UserDTO loginUser, Model model) {
        log.info("Controller indexGET");

        model.addAttribute("loginUser", loginUser);
    }

    @GetMapping(value = "login")
    public void loginGET() {
        log.info("Controller loginGET");
    }

    @PostMapping(value = "login.do")
    public String loginPOST(UserDTO userDTO, HttpServletRequest request, RedirectAttributes rttr){
        log.info("Controller loginPOST");

        HttpSession session = request.getSession();

        UserDTO login = userService.userLogin(userDTO);

        String failMessage = "아이디 혹은 비밀번호가 잘못 되었습니다.";

        if (login == null) {
            rttr.addFlashAttribute("loginFail", failMessage);
            return "redirect:/login";
        }

        session.setAttribute("loginUser", login);

        return "redirect:/index";
    }

    @PostMapping(value = "logout")
    public String logoutGET(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/index";
    }

    @GetMapping(value = "join")
    public void joinGET(UserDTO userDTO) {

        // 여기서 UserDTO를 받아줘야 회원가입 실패시 그 입력값이 그대로 유지된다.
        // 즉, 기존에 처음 페이지에 들어갈 때는 userDTO가 parameter로 들어오지 않으니 무시되고,
        // 회원가입 실패시, UserDTO를 받은 Get요청이 이루어지면서 model을 통해 넘어온 값이 parameter 로 받아지게 된다.
        log.info("Controller joinGET");
    }


    @PostMapping(value = "join")
    public String joinPOST(@Valid UserDTO userDTO, Errors errors, Model model) throws Exception {
        log.info("Controller joinPOST");

        /* post요청시 넘어온 user 입력값에서 Validation에 걸리는 경우 */
        if (errors.hasErrors()) {
            /* 회원가입 실패시 입력 데이터 유지 */
            model.addAttribute("userDTO", userDTO);
            /* 회원가입 실패시 message 값들을 모델에 매핑해서 View로 전달 */
            Map<String, String> validateResult = userService.validateHandler(errors);
            // map.keySet() -> 모든 key값을 갖고온다.
            // 그 갖고온 키로 반복문을 통해 키와 에러 메세지로 매핑

            for (String key : validateResult.keySet()) {
                // ex) model.addAtrribute("valid_id", "아이디는 필수 입력사항 입니다.")
                model.addAttribute(key, validateResult.get(key));
            }
            return "join";
        }

        userService.userJoin(userDTO);

        log.info("join 성공");

        return "redirect:/login";
    }

    @PostMapping(value = "/join/idCheck")
    @ResponseBody
    public String idCheck(String userId) throws Exception{
        log.info("Controller idCheck........");
        log.info("userId" + userId);

        int result = userService.idCheck(userId);

        if (result == 0) {
            return "success";
        } else {
            return "fail";
        }
    }


}
