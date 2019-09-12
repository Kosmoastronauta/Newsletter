package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.Message;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class SendEmailService
{
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    EmailRepository emailRepository;

    private final static Logger logger = Logger.getLogger(EmailService.class.getName());

    public void sendEmailToGroups(Message message)
    {
        for(int i = 0; i < message.getGroups().size(); i++)
        {
            try
            {
                sendEmailToGroup(message.getGroups().get(i), message.getSubject(), message.getBody());
            }catch(NoSuchElementException e)
            {
                logger.info(e.getMessage());
            }
        }
    }

    public void sendEmailToGroup(int groupId, String subject, String content) throws NoSuchElementException
    {
        List<EmailAddress> emailAddresses;
        emailAddresses = emailRepository.getEmailAddressesByGroupIdEquals(groupId);

        if(emailAddresses.isEmpty()) throw new NoSuchElementException("There is no email with group ID: " + groupId);

        for(int i = 0; i < emailAddresses.size(); i++)

        {
            try
            {
                sendEmail(emailAddresses.get(i),subject,content);
            }catch(MailException e)
            {
                logger.info("Error: Mail to: +" + emailAddresses.get(i).getGroupId() + " wasn't sent !");
            }
        }
    }

    public void sendEmail(EmailAddress emailAddress,String subject, String body ) throws MailException
    {
        try
        {
            Properties properties = new Properties();
            InputStream input = new FileInputStream("/home/mateusz/PropertiesFile/application-dev.properties");
            properties.load(input);
            String mailFrom = properties.getProperty("spring.mail.username");
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(emailAddress.getAddress());
            mail.setFrom(mailFrom);
            mail.setSubject(subject);
            mail.setText(body);

            if(emailAddress.isActive())
                javaMailSender.send(mail);

        }catch(MailException e)
        {
            logger.info("Invalid address");
        }
        catch(IOException e)
        {
            throw new NoSuchElementException("There is no mailFrom data !!!");
        }
    }

    public void sendEmailToAll(Message message)
    {
        List<EmailAddress> emailAddresses = emailRepository.getEmailAddressesByActiveIsTrue();

        String subject = message.getSubject();
        String body = message.getBody();
        for(EmailAddress emailAddress : emailAddresses)
        {
            sendEmail(emailAddress, subject, body);
        }
    }
}
