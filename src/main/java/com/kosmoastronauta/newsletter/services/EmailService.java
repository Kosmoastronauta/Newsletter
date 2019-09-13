package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
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
            PublicKey publicKey = keyPair.getPublic();
            emailAddress.setPubKey(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        }
        catch(NoSuchAlgorithmException e)
        {
            logger.info("Invalid hash method");
        }

    }

    private boolean unsubscribe(String address, String gettedPublicKey)
    {
            EmailAddress emailAddress = emailRepository.getEmailAddressesByPubKeyEquals(gettedPublicKey);

            if(verifyKeys(emailAddress, gettedPublicKey))
            {
                emailAddress.setActive(false);
                return true;
            }
        return false;
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

    private boolean verifyKeys(EmailAddress emailAddress, String gettedPublicKey)
    {
        String publicKey = emailAddress.getPubKey();
        if(gettedPublicKey.equals(publicKey)) return true;
        else return false;
    }

    public void deleteEmailAddressById(long id)
    {
        emailRepository.deleteById(id);
    }

}
