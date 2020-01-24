package com.kosmoastronauta.newsletter.services;


import com.github.javafaker.Faker;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class EmailServiceTest
{
    private static Faker faker = new Faker();
    @BeforeAll
    public static void setup() {}

    @Test
    public void emailValidationTestValid()
    {
        Assertions.assertTrue(EmailService.emailValidation(faker.internet().emailAddress()));
    }

    @Test
    public void emailValidationTestInvalid()
    {
        Assertions.assertFalse(EmailService.emailValidation(null));
        Assertions.assertFalse(EmailService.emailValidation("1234"));
        Assertions.assertFalse(EmailService.emailValidation("asd5f@s23af"));
        Assertions.assertFalse(EmailService.emailValidation("safse%.com"));
    }

}
