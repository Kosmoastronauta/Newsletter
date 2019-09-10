package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.controllers.EmailController;
import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.Message;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService
{


    @Autowired
   EmailRepository emailRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public List<EmailAddress> getAllEmails()
    {
        List<EmailAddress> emails = new ArrayList<>();
        emailRepository.findAll().forEach(emails::add);

        return emails;
    }

    public void sendEmailByGroup(Message message)
    {
        List<EmailAddress> emailAddresses;
        for(int i = 0; i < message.getGroups().size(); i++)
        {
            emailAddresses = emailRepository.getEmailAddressesByGroupEmailEquals(message.getGroups().get(i));
            for(int j = 0; j < emailAddresses.size(); j++)
            {

            }
        }



    }

    public void sendEmail(EmailAddress emailAddress) throws MailException
    {
        if(emailValidation(emailAddress))
        {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(emailAddress.getAddress());
            mail.setFrom("bartek@testerprogramuje.pl");
            mail.setSubject("Testing Newsletter");
            mail.setText("Ja nie dam rady?");
            javaMailSender.send(mail);
        }
        else throw new InvalidParameterException("Invalid address!!");
    }

    public void addEmail(EmailAddress emailAddress)
    {
        if(emailValidation(emailAddress))
        {
            emailRepository.save(emailAddress);
        }
        else throw new InvalidParameterException("Email address is invalid");
    }

    private static boolean emailValidation(EmailAddress emailAddress)
    {
        if(emailAddress.getAddress()==null || emailAddress.getAddress().equals("")) return false;

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(emailAddress.getGroup()==null)
            emailAddress.setGroup("standard");
        return emailAddress.getAddress().matches(regex);
    }
}
