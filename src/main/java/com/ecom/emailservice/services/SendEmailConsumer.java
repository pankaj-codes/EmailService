package com.ecom.emailservice.services;

import com.ecom.emailservice.dtos.SendEmailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.internals.Sender;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Service
public class SendEmailConsumer {

    private final ObjectMapper objectMapper;
    private final EmailUtil emailUtil;

    public SendEmailConsumer(ObjectMapper objectMapper, EmailUtil emailUtil) {
        this.objectMapper = objectMapper;
        this.emailUtil = emailUtil;
    }

    @KafkaListener(topics = "sendEmail",groupId = "emailService")
    public void handleSendEmailMessage(String message){
        //Code to send an email to the user.
        try {
            SendEmailDto emailDto = objectMapper.readValue(message, SendEmailDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Send an email. using SMTP - Simple mail transfer protocol.
        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply.temp2@gmail.com", "qrog urmd oxqu ryub");
            }
        };
        Session session = Session.getInstance(props, auth);
    }
}
