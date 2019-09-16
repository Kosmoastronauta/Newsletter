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
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    private List<EmailAddress> getListOfUniqueEmailAddressesByGroups(List<Integer> idsOfGroups)
    {
        int currentGroup;
        Set<EmailAddress> allAddressesSet = new HashSet<>();
        List<EmailToGroup> addressesInOneGroup;
        EmailAddress currentEmailAddress;
        for(int i = 0; i < idsOfGroups.size(); i++)
        {
            currentGroup = idsOfGroups.get(i);
            addressesInOneGroup = emailToGroupRepository.getEmailToGroupByGroupIdEqualsAndActiveTrue(currentGroup);

            for(int j = 0; j < addressesInOneGroup.size();  j++)
            {
                currentEmailAddress = emailRepository.getEmailAddressesByIdEquals(addressesInOneGroup.get(j).getEmailId());

                if(currentEmailAddress!=null)
                {
                    currentEmailAddress.setGroupId(idsOfGroups.get(i));
                    allAddressesSet.add(currentEmailAddress);
                }
            }
        }
        return new ArrayList<>(allAddressesSet);
    }

    public void sendEmailToGroups(MesssageContent message)
    {

        List<EmailAddress> allEmailAddressesList = getListOfUniqueEmailAddressesByGroups(message.getGroups());
        //sending
        sendToListOfEmailAddresses(allEmailAddressesList, message.getSubject(),message.getContent());
    }

    private void sendToListOfEmailAddresses(List<EmailAddress> emailAddresses, String subject, String content)
    {
        for(EmailAddress emailAddress : emailAddresses)
        {
            System.out.println(emailAddress.getAddress());
            sendEmail(emailAddress, subject, content);
        }
    }

    public void sendEmail(EmailAddress emailAddress,String subject, String content ) throws MailException
    {
        try
        {
            MimeMessage mail = prepareData(emailAddress, subject, content);
            javaMailSender.send(mail);

        }catch(MailException e)
        {
            logger.info("Invalid address");
        }
    }

    private MimeMessage prepareData(EmailAddress emailAddress,String subject, String content )
    {
        MimeMessage mail = javaMailSender.createMimeMessage();
        Properties properties = new Properties();
        try
        {
            InputStream input = new FileInputStream("/home/mateusz/PropertiesFile/application-dev.properties");
            properties.load(input);

        }catch(FileNotFoundException e)
        {
            logger.info(e.getMessage());
        }
        catch(IOException e)
        {
            logger.info(e.getMessage());
        }
        try
        {
            String mailFrom = properties.getProperty("spring.mail.username");

            MimeMessageHelper helper = new MimeMessageHelper(mail);
            helper.setTo(emailAddress.getAddress());
            helper.setFrom(mailFrom);
            helper.setSubject(subject);
            helper.setText(content + "</br> <p>Sent To: " + emailAddress.getAddress() + ".</p>" + "<a href=http://localhost:8181/unsubscribe/" + emailAddress.getAddress() + "/" + emailAddress.getGroupId() + "/" + emailAddress + ".getPubKey()" + ">Unsubscribe</a>", true);
        }catch(MessagingException e)
        {
            logger.info(e.getMessage());
        }
        return mail;
    }

    public void sendEmailToAll(MesssageContent message)
    {
        List<EmailAddress> emails = new ArrayList<>();
        emailRepository.findAll().forEach(emails::add);
        List<EmailAddress> emailsToSend = new ArrayList<>();

        for(EmailAddress email : emails)
        {
            if(emailToGroupRepository.existsEmailToGroupByEmailIdEqualsAndActiveTrue(email.getId()))
            {
                emailsToSend.add(email);
            }
        }
        sendToListOfEmailAddresses(emailsToSend,message.getSubject(),message.getContent());
    }
}
