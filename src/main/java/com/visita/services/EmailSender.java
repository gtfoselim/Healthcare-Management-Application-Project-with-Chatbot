package com.visita.services;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String participantEmail) {
        // Email server properties
        String host = "smtp-mail.outlook.com";
        String port = "25";
        String username = "emna.chelly@esprit.tn";
        String password = "211JFT9240.";

        // Recipient email address
        String toAddress = participantEmail;

        // Email content
        String subject = "New Participant Notification";
        String body = "Hello,\n\nA new participant has joined the event.";

        // Set email server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(username));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));

            // Set Subject: header field
            message.setSubject(subject);

            // Set email body
            message.setText(body);

            // Send email
            Transport.send(message);

            System.out.println("Email sent successfully to: " + toAddress);
        } catch (MessagingException e) {
            System.err.println("Failed to send email. Error: " + e.getMessage());
        }
    }
}
