package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.Message;
import com.kosmoastronauta.newsletter.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.InvalidParameterException;
import java.util.List;

@RestController
public class EmailController
{

    @Autowired
    EmailService emailService;

    @GetMapping(path = "/emails/")
    public ResponseEntity<List<EmailAddress>> getEmails()
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

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/send/groups/", consumes = "application/json")
    public ResponseEntity<EmailAddress> sendEmailToGroups(@RequestBody Message message)
    {
        emailService.sendEmailToGroups(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/send/all/", consumes = "application/json")
    public ResponseEntity<EmailAddress> sendEmailToAll(@RequestBody Message message)
    {
        emailService.sendEmailToAll(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/email/{id}")
    public ResponseEntity<EmailAddress> deleteEmail(@PathVariable long id)
    {
        emailService.deleteEmailAddressById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
