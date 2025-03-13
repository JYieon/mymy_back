package com.trip.mymy.config;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // React에서 접근 허용
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        res.setHeader("Access-Control-Allow-Credentials", "true"); // 쿠키 인증 허용
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
