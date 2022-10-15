package com.innovation.backend.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.GenericFilterBean;

public class CORSFilter extends GenericFilterBean implements Filter {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
    httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
    httpServletResponse.setHeader("Access-Control-Allow-Credentials", "*");
    httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
    filterChain.doFilter(servletRequest, servletResponse);

  }
  @Bean
  public FilterRegistrationBean filterRegistrationBean(){
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
    registrationBean.setName("CORS FIlter");
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(1);
    return registrationBean;
  }
}

