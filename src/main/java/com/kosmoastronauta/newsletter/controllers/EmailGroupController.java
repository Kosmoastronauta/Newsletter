package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.services.EmailGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class EmailGroupController
{

    private final EmailGroupService emailGroupService;

    @Autowired
    public EmailGroupController(EmailGroupService emailGroupService)
    {
        this.emailGroupService = emailGroupService;
    }

//    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/createGroup/")
    public ResponseEntity<EmailGroup> addGroup(@RequestBody EmailGroup emailGroup)
    {
        try
        {
            emailGroupService.addGroup(emailGroup);
        }catch(InvalidParameterException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(emailGroup, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/allGroups/")
    public ResponseEntity<List<EmailGroup>> getAllGroups()
    {
        List<EmailGroup> groups;

         try
         {
             groups = emailGroupService.getAllGroups();

         }catch(NoSuchFieldException e)
         {
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping(path = "/addEmail/{address}/ToGroup/{groupName}/")
    public ResponseEntity<HttpStatus> addEmailToGroup(@PathVariable String address, @PathVariable String groupName)
    {
        try
        {
            emailGroupService.addEmailToGroup(address, groupName);
        }catch(NoSuchFieldException e)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/getEmailsByGroupName/{groupName}/")
    public ResponseEntity<List<EmailAddress>> getEmailsByGroupName(@PathVariable String groupName)
    {
        List<EmailAddress> emailAddresses;
        try
        {
            emailAddresses = emailGroupService.getListOEmailAddressesByGroupName(groupName);
        }
        catch(InvalidParameterException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(emailAddresses, HttpStatus.OK);
    }
}
