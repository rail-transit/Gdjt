package com.example.passenger.config;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * sql注入过滤器
 */
@WebFilter
@Component
public class CrosXssFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CrosXssFilter.class);

    //初始化init()在容器启动的时候执行
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //doFilter()是过滤器的主要方法
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //跨域设置
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //通过在响应 header 中设置 ‘*’ 来允许来自所有域的跨域请求访问。
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            //通过对 Credentials 参数的设置，就可以保持跨域 Ajax 时的 Cookie
            //设置了Allow-Credentials，Allow-Origin就不能为*,需要指明具体的url域
            //httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            //请求方式
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
            //（预检请求）的返回结果（即 Access-Control-Allow-Methods 和Access-Control-Allow-Headers 提供的信息） 可以被缓存多久
            httpServletResponse.setHeader("Access-Control-Max-Age", "86400");
            //首部字段用于预检请求的响应。其指明了实际请求中允许携带的首部字段
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
            //防止本地缓存漏洞
            httpServletResponse.setHeader("Cache-Control", "no-cache");
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setDateHeader("Expires", 0);
            httpServletResponse.setHeader("Pragma", "no-cache");

        }
        //sql,xss过滤
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (httpServletRequest.getRequestURI().equals("/path/getDate") || httpServletRequest.getRequestURI().equals("/ctrlEvent/getEventState")) {
            //这里是不需要处理的url进入的方法;
            chain.doFilter(request, response);
        } else {
            logger.info("1CrosXssFilter.......orignal url:{},ParameterMap:{}",
                    httpServletRequest.getRequestURI(),
                    JSONObject.toJSONString(httpServletRequest.getParameterMap()));

            XssHttpServletRequestWrapper xssHttpServletRequestWrapper = new XssHttpServletRequestWrapper(
                    httpServletRequest);
            /**
             * chain.doFilter(request，response)方法确保调用的方法（request.getParameter，getParameterValues，getHeader）
             * 都是XssHttpServletRequestWrapper重写的方法，且获取的参数都是已经进行过滤的内容，进而消除了敏感信息
             */
            chain.doFilter(xssHttpServletRequestWrapper, response);
           /* logger.info("2CrosXssFilter..........doFilter url:{},ParameterMap:{}",
                    xssHttpServletRequestWrapper.getRequestURI(),
                    JSONObject.toJSONString(xssHttpServletRequestWrapper.getParameterMap()));*/
        }
    }

    //析构destroy（）:容器紧跟在垃圾收集之前调用 destroy()方法
    @Override
    public void destroy() {

    }
}
