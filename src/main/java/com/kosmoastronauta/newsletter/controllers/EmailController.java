package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.Message;
import com.kosmoastronauta.newsletter.services.EmailService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.security.InvalidParameterException;
import java.util.List;

@RestController
public class EmailController
{

    @Autowired
    EmailService emailService;

    @GetMapping(path = "/emails/")
    public ResponseEntity<List<EmailAddress>> getBooks()
    {
        List<EmailAddress> emails = emailService.getAllEmails();
        if(emails.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PostMapping(path = "/emails/")
    public ResponseEntity<EmailAddress> addEmail(@RequestBody EmailAddress emailAddress)
    {
        try
        {
            emailService.addEmail(emailAddress);
        }catch(InvalidParameterException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(emailAddress, HttpStatus.OK);
    }

    @PostMapping(path = "/send/", consumes = "application/json")
    public ResponseEntity<EmailAddress> sendEmail(@RequestBody Message message)
    {
            emailService.sendEmailToGroup(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
