package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.services.EmailGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RestController
public class EmailGroupController
{
    @Autowired
    EmailGroupService emailGroupService;

    @PostMapping(path = "/groups/")
    public ResponseEntity<EmailGroup> addGroup(@RequestBody EmailGroup emailGroup)
    {
        try
        {
            emailGroupService.addGroup(emailGroup);
        }catch(InvalidParameterException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<EmailGroup>(emailGroup, HttpStatus.OK);
    }
}
