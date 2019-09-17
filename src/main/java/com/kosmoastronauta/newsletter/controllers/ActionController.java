package com.kosmoastronauta.newsletter.controllers;

import com.kosmoastronauta.newsletter.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActionController
{
    @Autowired
    ActionService actionService;
}
