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
import javax.persistence.criteria.CriteriaBuilder;
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

    private List<EmailAddress> getListOfUniqueEmailAddressesByGroups(List<Integer> idsOfGroups)
    {

        int currentGroup;
        Set<EmailAddress> allAddressesSet = new HashSet<>();
        List<EmailToGroup> addressesInOneGroup;
        EmailAddress currentEmailAddress;
        EmailAddress tempEmailAddress;
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
        sendToListOfEmailAddresses(allEmailAddressesList, message.getSubject(),message.getBody());
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
                    "<a href=http://localhost:8181/unsubscribe/"+emailAddress.getAddress()+"/" +emailAddress.getGroupId() + "/"+emailAddress +
                            ".getPubKey()" +
                            ">Unsubscribe</a>",
                    true);

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
            logger.info(e.getMessage());
        }
        catch(MessagingException e)
        {
            logger.info(e.getMessage());
        }
    }

    public void sendEmailToAll(MesssageContent message)
    {
        List<EmailToGroup> emailToGroups = emailToGroupRepository.getDistinctByEmailIdAndActiveTrue();
        List<EmailAddress> emailAddresses = new ArrayList<>();
        for(int i = 0; i <emailToGroups.size() ; i++)
        {
            emailAddresses.add(emailRepository.getEmailAddressesByIdEquals(emailToGroups.get(i).getEmailId()));
        }

        String subject = message.getSubject();
        String body = message.getBody();
        for(EmailAddress emailAddress : emailAddresses)
        {
            sendEmail(emailAddress, subject, body);
        }
    }
}
