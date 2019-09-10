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

    public void sendEmailToGroups(Message message)
    {
        for(int i = 0; i < message.getGroups().size(); i++)
        {
            sendEmailToGroup(message.getGroups().get(i),message.getSubject(),message.getBody());
        }
    }

    public void sendEmailToGroup(String group, String subject, String body)
    {
        List<EmailAddress> emailAddresses;
        emailAddresses = emailRepository.getEmailAddressesByGroupEmailEquals(group);
        for(int i = 0; i < emailAddresses.size(); i++)
        {
            try
            {
                sendEmail(emailAddresses.get(i),subject,body);
            }catch(MailException e)
            {
                System.out.println("Error: Mail to: +" + emailAddresses.get(i).getAddress() + " wasn't sent !");
            }
        }
    }

    public void sendEmail(EmailAddress emailAddress,String subject, String body ) throws MailException
    {
        try
        {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(emailAddress.getAddress());
            mail.setFrom("bartek@testerprogramuje.pl");
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
        }catch(MailException e)
        {
            throw new InvalidParameterException("Invalid address!!");
        }
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
