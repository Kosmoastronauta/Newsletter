package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.domain.EmailToGroup;
import com.kosmoastronauta.newsletter.domain.GroupAction;
import com.kosmoastronauta.newsletter.repository.ActionRepository;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import com.kosmoastronauta.newsletter.repository.EmailToGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.*;
import java.util.*;
import java.util.logging.Logger;

@Service
public class EmailService
{
    private final static Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailToGroupRepository emailToGroupRepository;

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    EmailGroupRepository groupRepository;

    @Autowired
    SendEmailService sendEmailService;

    public List<EmailAddress> getAllEmails()
    {
        List<EmailAddress> emails = new ArrayList<>();
        emailRepository.findAll().forEach(emails::add);

        return emails;
    }

    public void addEmail(String address, String groupName, String startAction) throws NoSuchFieldException
    {
        EmailAddress emailAddress = new EmailAddress(address);
        if(emailValidation(emailAddress.getAddress()))
        {
            EmailGroup emailGroup = groupRepository.getEmailGroupByNameEquals(groupName);

            if(emailGroup == null) throw new NoSuchFieldException("There is no group with that name !");

            emailAddress.setGroupId(emailGroup.getId());
            try
            {
                emailAddress.setPubKey(generatePublicKey());
            } catch(NoSuchElementException e)
            {
                logger.info(e.getMessage());
            }
            emailRepository.save(emailAddress);
            this.addEmailToGroup(emailAddress);

            GroupAction groupAction = actionRepository.getGroupActionByGroupName(groupName);
            sendEmailService.sendEmail(emailAddress,groupAction.getSubject(),groupAction.getContent());

        }
        else throw new InvalidParameterException("Email address is invalid");
    }

    private String generatePublicKey()
    {
        KeyPairGenerator kpg;
        try
        {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            KeyPair keyPair = kpg.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            String keyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            keyString = removeSlashes(keyString);
            return keyString;
        }catch(NoSuchAlgorithmException e)
        {
            logger.info(e.getMessage());
            throw new NoSuchElementException("Can't generate key!");
        }
    }

    public boolean unsubscribe(String address, long groupId, String gettedPublicKey)
    {
        EmailAddress emailAddress = emailRepository.getEmailAddressByPubKeyEquals(gettedPublicKey);

        if(!address.equals(emailAddress.getAddress())) throw new InvalidParameterException(); // if key is ok but
        // for another address
        if(verifyKeys(emailAddress, gettedPublicKey))
        {
            EmailToGroup emailToGroup = emailToGroupRepository.getEmailToGroupByEmailIdEqualsAndGroupIdEquals(emailAddress.getId(), groupId);
            emailToGroup.setActive(false);
            emailToGroupRepository.save(emailToGroup);

            return true;
        }
        return false;
    }

    private static boolean emailValidation(String address)
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(address == null || address.equals(""))
            return false;

        return address.matches(regex);
    }

    private boolean verifyKeys(EmailAddress emailAddress, String gettedPublicKey)
    {
        String publicKey = emailAddress.getPubKey();
        return gettedPublicKey.equals(publicKey);
    }

    public void deleteEmailAddressById(long id)
    {
        emailRepository.deleteById(id);
    }

    private String removeSlashes(String word)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < word.length(); i++)
        {
            if(word.charAt(i) != '/')
                sb.append(word.charAt(i));
        }

        return sb.toString();
    }

    private void addEmailToGroup(EmailAddress emailAddress)
    {
        EmailToGroup emailToGroup = emailToGroupRepository.getEmailToGroupByEmailIdEqualsAndGroupIdEquals(emailAddress.getId(), emailAddress.getGroupId());
        if(emailToGroup == null)
        {
            emailToGroup = new EmailToGroup(emailAddress.getId(), emailAddress.getGroupId());
            emailToGroup.setActive(true);
            emailToGroupRepository.save(emailToGroup);
        }
        else
        {
            emailToGroup.setActive(true);
            emailToGroupRepository.save(emailToGroup);
        }
    }
}
