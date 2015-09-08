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

  private static final String APP_ID = "EcXAT5pXyPP4x874uRSPKgC6cMgd3BBNdK6UKKAq";
  private static final String APP_REST_API_ID = "J67tozPklrXNUwlRa3ncawb5IRb6toWYohcGYZ47";

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

  public static void main(String[] args) throws Exception {
    Parse.initialize(APP_ID, APP_REST_API_ID);
    SpringApplication.run(Application.class, args);
  }

}
