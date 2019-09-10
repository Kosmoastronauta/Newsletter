package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class EmailGroupService
{
    @Autowired
    EmailGroupRepository emailGroupRepository;

    public void addGroup(String name)
    {
        if(groupNameValidation(name)) throw new InvalidParameterException("group name can't be empty");
        EmailGroup emailGroup = new EmailGroup(name);
        emailGroupRepository.save(emailGroup);
    }

    private boolean groupNameValidation(String name)
    {
        if(name.equals("") || name==null) return false;
        return true;
    }
}
