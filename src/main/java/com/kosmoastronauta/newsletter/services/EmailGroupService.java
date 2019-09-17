package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.domain.EmailToGroup;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
import com.kosmoastronauta.newsletter.repository.EmailToGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.SoundbankResource;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailGroupService
{
    @Autowired
    EmailGroupRepository emailGroupRepository;

    @Autowired
    EmailToGroupRepository emailToGroupRepository;

    public void addGroup(EmailGroup emailGroup)
    {
        if(!groupNameValidation(emailGroup.getName()))
            throw new InvalidParameterException("group name can't be empty");

        emailGroupRepository.save(emailGroup);

    }

    private boolean groupNameValidation(String name)
    {
       if(name==null || name.equals("")) return false;
        return true;
    }

    public List<EmailGroup> getAllGroups() throws NoSuchFieldException
    {
        List<EmailGroup> groups = new ArrayList<>();
        emailGroupRepository.findAll().forEach(groups::add);

        if(groups.isEmpty()) throw new NoSuchFieldException("There is no groups!");
        return groups;
    }


    public void addEmailToGroup(String address, String groupName)
    {
        EmailToGroup emailToGroup = emailGroupRepository.
    }
}
