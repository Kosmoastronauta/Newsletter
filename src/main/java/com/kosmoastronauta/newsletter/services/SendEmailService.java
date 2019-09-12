package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService
{
    @Autowired
    EmailRepository emailRepository;
}
