package com.innovation.backend.security.log;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Configuration
public class CommonsRequestLoggingFilterConfig extends CommonsRequestLoggingFilter {

  CatchLocation catchLocation = CatchLocation.ALL;

  MessageMaker messageMaker;

  enum CatchLocation {
    ALL, BEFORE, AFTER
  }

  @Bean
  public FilterRegistrationBean requestLoggingFilter() {
    CommonsRequestLoggingFilterConfig commonsRequestLoggingFilter = new CommonsRequestLoggingFilterConfig();
    commonsRequestLoggingFilter.setIncludeClientInfo(true);
    commonsRequestLoggingFilter.setIncludeHeaders(true);
    commonsRequestLoggingFilter.setIncludeQueryString(true);
    commonsRequestLoggingFilter.setIncludePayload(true);
    commonsRequestLoggingFilter.setMaxPayloadLength(1024 * 1024);

    FilterRegistrationBean bean = new FilterRegistrationBean(commonsRequestLoggingFilter);
    bean.setOrder(Integer.MIN_VALUE);
    return bean;
  }

  @Override
  protected boolean shouldLog(HttpServletRequest request) {
    return logger.isInfoEnabled();
  }

  public CatchLocation getCatchLocation() {
    return catchLocation;
  }

  public void setCatchLocation(CatchLocation catchLocation) {
    this.catchLocation = catchLocation;
  }

  public void setMessageMaker(MessageMaker messageMaker) {
    this.messageMaker = messageMaker;
  }

  @Override
  protected void beforeRequest(HttpServletRequest request, String message) {
    if (catchLocation == CatchLocation.BEFORE || catchLocation == CatchLocation.ALL) {
      logger.info(message);
    }
  }

  @Override
  protected void afterRequest(HttpServletRequest request, String message) {
    if (request instanceof ContentCachingRequestWrapper) {
      try (ServletInputStream is = request.getInputStream()) {
        if (is.available() >= 0) {
          while (true) {
            int read = is.read(new byte[8096]);
            if (read < 8096) {
              break;
            }
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (catchLocation == CatchLocation.AFTER || catchLocation == CatchLocation.ALL) {
      logger.info(message);
    }
  }

  @Override
  protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
    if (this.messageMaker != null) {
      return this.messageMaker.make(request, prefix, suffix);
    } else {
      return super.createMessage(request, prefix, suffix);
    }
  }

  public interface MessageMaker {

    String make(HttpServletRequest request, String prefix, String suffix);
  }
}

