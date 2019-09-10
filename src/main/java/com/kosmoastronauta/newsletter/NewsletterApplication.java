package com.kosmoastronauta.newsletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@SpringBootApplication
public class NewsletterApplication
{


    public static void main(String[] args) {
        SpringApplication.run(NewsletterApplication.class, args);
    }

}
