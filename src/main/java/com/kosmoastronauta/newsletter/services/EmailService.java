package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.Message;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

        System.out.println(emails.size());
        return emails;
    }

    public void sendEmailToGroups(Message message)
    {
        System.out.println("Number Of Groups to send:  " + message.getGroups().size());
        for(int i = 0; i < message.getGroups().size(); i++)
        {
            try
            {
                sendEmailToGroup(message.getGroups().get(i), message.getSubject(), message.getBody());
            }catch(NoSuchElementException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public void sendEmailToGroup(String group, String subject, String body) throws NoSuchElementException
    {
        List<EmailAddress> emailAddresses;
        emailAddresses = emailRepository.getEmailAddressesByGroupEmailEquals(group);

        if(emailAddresses.isEmpty()) throw new NoSuchElementException("There is no email with group named: " + group);

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
            System.out.println("Sending to: " + emailAddress.getAddress());
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(emailAddress.getAddress());
            mail.setFrom("bartek@testerprogramuje.pl");
            mail.setSubject(subject);
            mail.setText(body);

            if(emailAddress.isActive())
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
            emailAddress.setActive(true);
            emailRepository.save(emailAddress);
        }
        else throw new InvalidParameterException("Email address is invalid");
    }

    private static boolean emailValidation(EmailAddress emailAddress)
    {
        if(emailAddress.getAddress()==null || emailAddress.getAddress().equals("")) return false;

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(emailAddress.getGroupEmail()==null)
            emailAddress.setGroupEmail("standard");
        return emailAddress.getAddress().matches(regex);
    }

    public void deleteEmailAddressById(long id)
    {
        emailRepository.deleteById(id);
    }

    public void sendEmailToAll(Message message) {

    }
}
