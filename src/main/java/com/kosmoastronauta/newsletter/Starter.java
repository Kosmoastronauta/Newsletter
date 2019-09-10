package com.kosmoastronauta.newsletter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Scope("singleton")
public class Starter implements CommandLineRunner
{

    public void run(String... args)
    {
        System.out.println("Hello World");
    }
}
