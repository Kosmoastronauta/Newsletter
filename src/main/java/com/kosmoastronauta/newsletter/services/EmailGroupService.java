package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailGroupService
{
    @Autowired
    EmailGroupRepository emailGroupRepository;

    public void addGroup(EmailGroup emailGroup)
    {
        if(groupNameValidation(emailGroup.getName())) throw new InvalidParameterException("group name can't be empty");
        emailGroupRepository.save(emailGroup);
    }

    private boolean groupNameValidation(String name)
    {
        return name != null && !name.equals("");
    }

    public List<EmailGroup> getAllGroups() throws NoSuchFieldException
    {
        List<EmailGroup> groups = new ArrayList<>();
        emailGroupRepository.findAll().forEach(groups::add);

        if(groups.isEmpty()) throw new NoSuchFieldException("There is no groups!");
        return groups;
    }
}
