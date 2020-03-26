package com.example.passenger.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        /*错误类型为400，请求中的语法错误，请求无效*/
        ErrorPage e400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400.html");
        /*错误类型为401，用户没有访问权限，需要进行身份认证*/
        ErrorPage e401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401.html");
        /*1、按错误的类型显示错误的网页*/
        /*错误类型为404，找不到网页的，默认显示404.html网页*/
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html");
        /*错误类型为500，表示服务器响应错误，默认显示500.html网页*/
        ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html");
        /*2、按具体某个异常显示错误的网页*/
        /*当某个异常即可以根据错误类型显示错误网页，由可以根据某个具体的异常来显示错误网页时，优先根据具体的某个异常显示错误的网页*/
        ErrorPage error = new ErrorPage(IllegalArgumentException.class, "/error/error.html");
        registry.addErrorPages(e400,e401,e404, e500,error);
    }
}
