package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.GroupAction;
import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.EmailToGroup;
import com.kosmoastronauta.newsletter.domain.MesssageContent;
import com.kosmoastronauta.newsletter.repository.ActionRepository;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
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
    private static final String PROPERTIES_FILE = "/home/mateusz/PropertiesFile/application-dev.properties";
    private static final String HOST_URL = "http://localhost:8181";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailToGroupRepository emailToGroupRepository;

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    EmailGroupRepository emailGroupRepository;

    private final static Logger logger = Logger.getLogger(EmailService.class.getName());

    class AddressesWithMessage
    {
        private List<EmailAddress> emailAddresses;
        private String subject;
        private String content;

        public List<EmailAddress> getEmailAddresses() {
            return emailAddresses;
        }

        public void setEmailAddresses(List<EmailAddress> emailAddresses) {
            this.emailAddresses = emailAddresses;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private List<EmailAddress> getListOfUniqueEmailAddressesByGroups(List<Integer> idsOfGroups)
    {
        int currentGroup;
        Set<EmailAddress> allAddressesSet = new HashSet<>();
        List<EmailToGroup> addressesInOneGroup;
        EmailAddress currentEmailAddress;
        for(Integer idOfGroup : idsOfGroups)
        {
            currentGroup = idOfGroup;
            addressesInOneGroup = emailToGroupRepository.getEmailToGroupByGroupIdEqualsAndActiveTrue(currentGroup);

            for(EmailToGroup emailToGroup : addressesInOneGroup)
            {
                currentEmailAddress = emailRepository.getEmailAddressesByIdEquals(emailToGroup.getEmailId());

                if(currentEmailAddress != null)
                {
                    currentEmailAddress.setGroupId(idOfGroup);
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
        sendToListOfEmailAddresses(allEmailAddressesList, message.getSubject(), message.getContent());
    }

    private void sendToListOfEmailAddresses(List<EmailAddress> emailAddresses, String subject, String content)
    {
        for(EmailAddress emailAddress : emailAddresses)
        {
            sendEmail(emailAddress, subject, content);
        }
    }

    private void sendEmail(EmailAddress emailAddress, String subject, String content) throws MailException
    {
        try
        {
            MimeMessage mail = prepareData(emailAddress, subject, content);
            javaMailSender.send(mail);

        } catch(MailException e)
        {
            logger.info("Invalid address");
        }
    }

    private MimeMessage prepareData(EmailAddress emailAddress, String subject, String content)
    {
        MimeMessage mail = javaMailSender.createMimeMessage();
        Properties properties = new Properties();
        try
        {
            InputStream input = new FileInputStream(PROPERTIES_FILE);
            properties.load(input);

        } catch(FileNotFoundException e)
        {
            logger.info(e.getMessage());
        } catch(IOException e)
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
            helper.setText(content + "</br> <p>Sent To: " + emailAddress.getAddress() + ".</p>" + "<a href=" + HOST_URL + "/unsubscribe/" + emailAddress.getAddress() + "/" + emailAddress.getGroupId() + "/" + emailAddress.getPubKey() + ">Unsubscribe</a>", true);
        } catch(MessagingException e)
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
                emailsToSend.add(email);
        }
        sendToListOfEmailAddresses(emailsToSend, message.getSubject(), message.getContent());
    }

    public void sendEmailToGroupByAction(long groupId, String actionName) throws  NoSuchElementException, NoSuchFieldException
    {
        if(!emailGroupRepository.existsById(groupId)) throw new NoSuchFieldException("There is no group with that id");
        List<Object[]> objects =
                actionRepository.getListOfActiveAddressesGroupIdSubjectsAndContentByActionName(groupId,actionName);
        if(objects.isEmpty()) throw new NoSuchElementException("There is no emails in this group");
        AddressesWithMessage addressesWithMessage = getListOfEmailAddressesByListOfObjects(objects);

        for(int i=0; i<addressesWithMessage.getEmailAddresses().size(); i++)
        {
            System.out.println(addressesWithMessage.getEmailAddresses().get(i).getAddress());
            System.out.println(addressesWithMessage.getSubject());
            System.out.println(addressesWithMessage.getContent());
        }
/*
        sendToListOfEmailAddresses(addressesWithMessage.getEmailAddresses(),
                addressesWithMessage.getSubject(),
                addressesWithMessage.content);*/

    }

    private AddressesWithMessage getListOfEmailAddressesByListOfObjects(List<Object[]> objects)
    {
        AddressesWithMessage addressesWithMessage = new AddressesWithMessage();
        boolean once = true;

        for(Object[] object : objects)
        {
            addressesWithMessage.emailAddresses.add(new EmailAddress(object[0].toString(), Long.valueOf(object[1].toString())));

            if(once)
            {
                addressesWithMessage.setSubject(object[2].toString());
                addressesWithMessage.setContent(object[3].toString());
                once = false;
            }
        }

        return addressesWithMessage;
    }

}
