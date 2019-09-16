package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.EmailToGroup;
import com.kosmoastronauta.newsletter.domain.MesssageContent;
import com.kosmoastronauta.newsletter.repository.EmailRepository;
import com.kosmoastronauta.newsletter.repository.EmailToGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

@Service
public class SendEmailService
{
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailToGroupRepository emailToGroupRepository;

    private final static Logger logger = Logger.getLogger(EmailService.class.getName());

    public void sendEmailToGroups(MesssageContent message)
    {
        int currentGroup;
        Set<EmailAddress> allAddresses = new HashSet<>();
        List<EmailToGroup> addressesInOneGroup;
        EmailAddress currentEmailAddress;
        for(int i = 0; i < message.getGroups().size(); i++)
        {
            currentGroup = message.getGroups().get(i);
            addressesInOneGroup = emailToGroupRepository.getEmailToGroupByGroupIdEqualsAndActiveTrue(currentGroup);

            for(int j = 0; j < addressesInOneGroup.size();  j++)
            {
                currentEmailAddress = emailRepository.getEmailAddressesByIdEquals(addressesInOneGroup.get(i).getId());
                allAddresses.add(currentEmailAddress);
            }
        }

        List<EmailAddress> allEmailAddressesList = new ArrayList<>(allAddresses);
        //sending
        sendToListOfEmailAddresses(allEmailAddressesList, message.getSubject(),message.getBody());
    }

    public void sendEmailToGroup(int groupId, String subject, String content) throws NoSuchElementException
    {
        List<EmailAddress> emailAddresses;
        ////// Important here could not work
        emailAddresses = emailRepository.getEmailAddressesByGroupsContains(groupId);
        for(int i = 0; i < emailAddresses.size(); i++)
        {
            logger.info(emailAddresses.get(i).getGroups().toString());
        }

//        if(emailAddresses.isEmpty()) throw new NoSuchElementException("There is no email with group ID: " + groupId);
//
//        for(int i = 0; i < emailAddresses.size(); i++)
//        {
//            try
//            {
//                sendEmail(emailAddresses.get(i),subject,content);
//            }catch(MailException e)
//            {
//                logger.info("Error: Mail to group "  + " wasn't " +
//                        "sent !");
//            }
//        }
    }

    public void sendToListOfEmailAddresses(List<EmailAddress> emailAddresses, String subject, String content)
    {
        for(EmailAddress emailAddress : emailAddresses)
        {
            sendEmail(emailAddress, subject, content);
        }
    }

    public void sendEmail(EmailAddress emailAddress,String subject, String content ) throws MailException
    {
        try
        {
            Properties properties = new Properties();
            InputStream input = new FileInputStream("/home/mateusz/PropertiesFile/application-dev.properties");
            properties.load(input);
            String mailFrom = properties.getProperty("spring.mail.username");
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);

            helper.setTo(emailAddress.getAddress());
        //    mail.setRecipient(Message.RecipientType.TO,new InternetAddress(emailAddress.getAddress(),false));
            helper.setFrom(mailFrom);
            helper.setSubject(subject);
            helper.setText(content + "</br> <p>Sent To: "+ emailAddress.getAddress() + ".</p>"+
                    "<a href=http://localhost:8181/unsubscribe/"+emailAddress.getAddress()+"/"+emailAddress.getPubKey()+
                            ">Unsubscribe</a>",
                    true);

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
        catch(AddressException e)
        {

        }
        catch(MessagingException e)
        {

        }
    }

    public void sendEmailToAll(MesssageContent message)
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
