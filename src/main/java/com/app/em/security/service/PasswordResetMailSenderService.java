package com.app.em.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;


@Service
public class PasswordResetMailSenderService
{
    private static final String EMAIL_SUBJECT = "ClubEventManager - password resetting";
    private static final String EMAIL_TITLE = "Hello :)\n\n";
    private static final String EMAIL_CONTENT = "Please click the link below to go to the password reset form:\n";
    private static final String EMAIL_FOOTER = "\nThank you,\nThe ClubEventManager Team";

    private String SENDER_EMAIL_ADDRESS;
    private String SENDER_EMAIL_PASSWORD;
    private String MAIL_SMTP_HOST;
    private String MAIL_SMTP_PORT;
    private String FRONTEND_RESET_PASSWORD_COMPONENT_PATH;
    private String FRONTEND_SERVER_DOMAIN;

    private Session session;


    public PasswordResetMailSenderService(
            @Value("${mail-sender.sender-email-address}") String SENDER_EMAIL_ADDRESS,
            @Value("${mail-sender.sender-email-password}") String SENDER_EMAIL_PASSWORD,
            @Value("${mail-sender.mail-smtp-host}") String MAIL_SMTP_HOST,
            @Value("${mail-sender.mail-smtp-port}") String MAIL_SMTP_PORT,
            @Value("${mail-sender.frontend-reset-password-component-path}") String FRONTEND_RESET_PASSWORD_COMPONENT_PATH,
            @Value("${mail-sender.frontend-server-domain}") String FRONTEND_SERVER_DOMAIN
            )
    {
        this.SENDER_EMAIL_ADDRESS = SENDER_EMAIL_ADDRESS;
        this.SENDER_EMAIL_PASSWORD = SENDER_EMAIL_PASSWORD;
        this.MAIL_SMTP_HOST = MAIL_SMTP_HOST;
        this.MAIL_SMTP_PORT = MAIL_SMTP_PORT;
        this.FRONTEND_RESET_PASSWORD_COMPONENT_PATH = FRONTEND_RESET_PASSWORD_COMPONENT_PATH;
        this.FRONTEND_SERVER_DOMAIN = FRONTEND_SERVER_DOMAIN;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", MAIL_SMTP_HOST );
        properties.put("mail.smtp.port", MAIL_SMTP_PORT );

        session = Session.getInstance(properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(SENDER_EMAIL_ADDRESS, SENDER_EMAIL_PASSWORD);
            }
        });
    }

    public void sendMail(Message message)
    {
        try
        {
            Transport.send(message);
        }
        catch(MessagingException e) {
            System.out.println(e);
        }
    }

    public Message prepareMessage(String recipient, String token, HttpServletRequest request)
    {
        String passwordResetUrl = request.getScheme() + "://" +
                FRONTEND_SERVER_DOMAIN + "/" +
                FRONTEND_RESET_PASSWORD_COMPONENT_PATH + "/" +
                token;
        Message message = new MimeMessage(session);

        try
        {
            message.setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(EMAIL_SUBJECT);
            message.setText(EMAIL_TITLE +
                    EMAIL_CONTENT +
                    passwordResetUrl +
                    EMAIL_FOOTER);
        }
        catch(AddressException e) {
            System.out.println(e);
        }
        catch(MessagingException e) {
            System.out.println(e);
        }

        return message;
    }
}