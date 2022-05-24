package com.hellparty.configuration;

import com.hellparty.interceptor.BoardInterceptor;
import com.hellparty.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/login");


        registry.addInterceptor(new BoardInterceptor())
                .addPathPatterns("/board/**");
    }


}
