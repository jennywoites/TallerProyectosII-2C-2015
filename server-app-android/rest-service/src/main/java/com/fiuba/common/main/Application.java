package com.fiuba.common.main;


import org.parse4j.Parse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ComponentScan({"com.fiuba.common.controller", "com.fiuba.common.domain"})
@ImportResource({"beans-configuration.xml"})
public class Application extends SpringBootServletInitializer {

  private static final String APP_ID = "LWDbRYanJdNgmh2Kg5z77PN4VAMjwYW9BiMxKJwU";
  private static final String APP_REST_API_ID = "CLT9a0ceZU8xr4EKTyFpCjLyQEVpFgtjEyt2VLLc";

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

  public static void main(String[] args) throws Exception {
    Parse.initialize(APP_ID, APP_REST_API_ID);
    SpringApplication.run(Application.class, args);
  }

}
