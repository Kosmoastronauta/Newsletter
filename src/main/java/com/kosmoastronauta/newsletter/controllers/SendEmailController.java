package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.MesssageContent;
import com.kosmoastronauta.newsletter.services.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SendEmailController
{
    @Autowired
    SendEmailService sendEmailService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/send/groups/", consumes = "application/json")
    public ResponseEntity<EmailAddress> sendEmailToGroups(@RequestBody MesssageContent messageContent)
    {
        sendEmailService.sendEmailToGroups(messageContent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/sendToAll/", consumes = "application/json")
    public ResponseEntity<EmailAddress> sendEmailToAll(@RequestBody MesssageContent messageContent)
    {
        sendEmailService.sendEmailToAll(messageContent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/sendToGroupId/{groupId}/actionName/{actionName}")
    public ResponseEntity<HttpStatus> sendEmailToGroupByActionName(@PathVariable long groupId,
                                                                   @PathVariable String actionName)
    {
        try
        {
            sendEmailService.sendEmailToGroupByAction(groupId,actionName);
        }
    }
}
