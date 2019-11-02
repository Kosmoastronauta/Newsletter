package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.domain.EmailToGroup;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import com.kosmoastronauta.newsletter.repository.EmailToGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class EmailGroupService
{
    @Autowired
    EmailGroupRepository emailGroupRepository;

    @Autowired
    EmailToGroupRepository emailToGroupRepository;

    @Autowired
    EmailRepository emailRepository;

    private final static Logger logger = Logger.getLogger(EmailService.class.getName());

    public void addGroup(EmailGroup emailGroup)
    {
        if(!groupNameValidation(emailGroup.getName()))
            throw new InvalidParameterException("group name can't be empty");

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

    public void addEmailToGroup(String address, String groupName) throws NoSuchFieldException
    {
        EmailToGroup emailToGroup = emailToGroupRepository.getEmailToGroupByEmailAddressEqualsAndGroupNameEquals(address, groupName);

        if(emailToGroup == null)
        {   EmailGroup emailGroup = emailGroupRepository.getEmailGroupByNameEquals(groupName);
            EmailAddress emailAddress = emailRepository.getEmailAddressByAddressEquals(address);

            if(emailGroup==null || emailAddress==null)
            {
                throw new NoSuchFieldException("There is no such email or group!");
            }
            emailToGroup = new EmailToGroup();
            emailToGroup.setEmailId(emailAddress.getId());
            emailToGroup.setGroupId(emailGroup.getId());
        }
        emailToGroup.setActive(true);
        emailToGroupRepository.save(emailToGroup);
    }

    public List<EmailAddress> getListOEmailAddressesByGroupName(String groupName)
    {
        if(emailGroupRepository.existsById())
        List<EmailAddress> emails;
        emails = emailRepository.getListOfEmailAddressesByGroupNameEquals(groupName);

        if(emails.isEmpty())
            throw new NoSuchElementException("There is no emails in this group");

        return emails;
    }
}
