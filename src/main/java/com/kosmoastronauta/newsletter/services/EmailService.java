package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class EmailService
{
    private final static Logger logger = Logger.getLogger(EmailService.class.getName());
    @Autowired
   EmailRepository emailRepository;

    public List<EmailAddress> getAllEmails()
    {
        List<EmailAddress> emails = new ArrayList<>();
        emailRepository.findAll().forEach(emails::add);

        return emails;
    }

    public void addEmail(EmailAddress emailAddress)
    {
        if(emailValidation(emailAddress.getAddress()))
        {
            emailAddress.setActive(true);
            emailRepository.save(emailAddress);
        }
        else throw new InvalidParameterException("Email address is invalid");

        emailAddress.setGroupId(1);

        try
        {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.generateKeyPair();
            emailAddress.setPubKey(keyPair.getPublic().getFormat());
            emailAddress.setPrivKey(keyPair.getPrivate().getFormat());
        }
        catch(NoSuchAlgorithmException e)
        {
            logger.info("Invalid hash method");
        }


    }

    private static boolean emailValidation(String address)
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(address==null || address.equals(""))
        {
            return false;
        }

        return address.matches(regex);
    }

    public void deleteEmailAddressById(long id)
    {
        emailRepository.deleteById(id);
    }

}
