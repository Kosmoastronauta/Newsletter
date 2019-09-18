package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.domain.GroupAction;
import com.kosmoastronauta.newsletter.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ActionController
{
    @Autowired
    ActionService actionService;

    @GetMapping(path = "/groupId/{groupId}/actions/")
    public ResponseEntity<List<GroupAction>> getAllActionsByGroupId(@PathVariable long groupId)
    {
        List<GroupAction> groupActions;
        try
        {
            groupActions = actionService.getAllActionsByGroupId(groupId);
        }catch(NoSuchFieldException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(groupActions, HttpStatus.OK);
    }

    @PostMapping(path = "/addActionToGroup/")
    public ResponseEntity<HttpStatus> addActionToGroupById(@RequestBody GroupAction groupAction)
    {
        try
        {
            actionService.addActionForGroup(groupAction);
        }catch(InvalidParameterException | NoSuchFieldException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "delete/actionId/{actionId}/groupId/{groupId}/")
    public ResponseEntity<HttpStatus> deleteActionIdByActionIdAndGroupId(@PathVariable long actionId,
                                                                         @PathVariable long groupId)
    {
        try
        {
            actionService.deleteActionByActionIdAndGroupId(actionId,groupId);
        }catch(NoSuchFieldException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
