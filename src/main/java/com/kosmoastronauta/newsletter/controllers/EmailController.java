package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
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


    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService)
    {
        this.emailService = emailService;
    }

    @GetMapping(path = "/emails/")
    public ResponseEntity<List<EmailAddress>> getEmails()
    {
        List<EmailAddress> emails = emailService.getAllEmails();
        if(emails.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PostMapping(path = "/emails/groupName/{groupName}/startActionName/{actionName}/address/{address}/")
    public ResponseEntity<EmailAddress> addEmail(@PathVariable String groupName,
                                                 @PathVariable String actionName,
                                                 @PathVariable String address)
    {
        try
        {
            emailService.addEmail(address,groupName,actionName);
        }catch(InvalidParameterException | NoSuchFieldException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/unsubscribe/{address}/{groupId}/{key}")
    public ResponseEntity<EmailAddress> unsubscribe(@PathVariable String address,
                                                    @PathVariable int groupId, @PathVariable String key)
    {
        try
        {
            if(emailService.unsubscribe(address,groupId,key)) return new ResponseEntity<>(HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch(InvalidParameterException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/email/{id}")
    public ResponseEntity<EmailAddress> deleteEmail(@PathVariable long id)
    {
        emailService.deleteEmailAddressById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
