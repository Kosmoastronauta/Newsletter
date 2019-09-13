package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.Message;
import com.kosmoastronauta.newsletter.services.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailController
{
    @Autowired
    SendEmailService sendEmailService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/send/groups/", consumes = "application/json")
    public ResponseEntity<EmailAddress> sendEmailToGroups(@RequestBody Message message)
    {
       // sendEmailService.sendEmailToGroups(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/sendToAll/", consumes = "application/json")
    public ResponseEntity<EmailAddress> sendEmailToAll(@RequestBody Message message)
    {
        sendEmailService.sendEmailToAll(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}