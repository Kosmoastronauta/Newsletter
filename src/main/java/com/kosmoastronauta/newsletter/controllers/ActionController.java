package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.Action;
import com.kosmoastronauta.newsletter.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
public class ActionController
{
    @Autowired
    ActionService actionService;

    @GetMapping(path = "/groupId/{groupId}/actions/")
    public ResponseEntity<List<Action>> getAllActionsByGroupId(@PathVariable long groupId)
    {
        List<Action> actions;
        try
        {
            actions = actionService.getAllActionsByGroupId(groupId);
        }catch(NoSuchFieldException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(actions, HttpStatus.OK);
    }

    @PostMapping(path = "/addActionToGroup/")
    public ResponseEntity<HttpStatus> addActionToGroupById(@RequestBody Action action)
    {
        try
        {
            actionService.addActionForGroup(action);
        }catch(InvalidParameterException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(NoSuchFieldException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
