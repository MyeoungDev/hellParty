package com.hellparty.interceptor;

import com.hellparty.domain.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("LoginInterceptor preHandler ");

        HttpSession session = request.getSession();

        UserDTO lvo = (UserDTO) session.getAttribute("loginUser");
        if (lvo != null) {  /* 로그인 되어 있는 경우 */
            response.sendRedirect("/index");
        }
        return true;    /* 로그인 되지 않은 경우 */
    }
}
