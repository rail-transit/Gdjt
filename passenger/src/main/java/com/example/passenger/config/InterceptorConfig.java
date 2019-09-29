package com.example.passenger.config;

import com.example.passenger.utils.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jinbin
 * @date 2018-07-08 22:33
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        InterceptorRegistration loginRegistry = registry.addInterceptor(authenticationInterceptor());

        //拦截路径
        loginRegistry.addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录

        //排除拦截
        loginRegistry.excludePathPatterns("/user/home"); //添加排除拦截命名空间
        loginRegistry.excludePathPatterns("/user/login");
        loginRegistry.excludePathPatterns("/schedules/*");
        loginRegistry.excludePathPatterns("/**/*.css");
        loginRegistry.excludePathPatterns("/**/*.js");
        loginRegistry.excludePathPatterns("/**/*.png");
        loginRegistry.excludePathPatterns("/**/*.jpg");
        loginRegistry.excludePathPatterns("/**/*.jpeg");
        loginRegistry.excludePathPatterns("/**/*.gif");
        loginRegistry.excludePathPatterns("/**/*.svg");
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}
