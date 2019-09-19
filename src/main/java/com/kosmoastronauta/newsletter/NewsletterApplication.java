package com.kosmoastronauta.newsletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class NewsletterApplication extends SpringBootServletInitializer
{
    public static void main(String[] args) {
        SpringApplication.run(NewsletterApplication.class, args);
    }
}
