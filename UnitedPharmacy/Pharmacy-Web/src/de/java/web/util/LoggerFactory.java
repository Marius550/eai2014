package de.java.web.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;

import javax.enterprise.inject.spi.InjectionPoint;

public class LoggerFactory {

  @Produces
  public static Logger getLogger(InjectionPoint ip) {
    return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
  }

}
