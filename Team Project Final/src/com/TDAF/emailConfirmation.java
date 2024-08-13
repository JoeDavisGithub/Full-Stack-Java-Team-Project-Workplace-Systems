package com.TDAF;

/*import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
*/import java.util.Properties;


public class emailConfirmation {
    // This class is used to send emails but did not work for reasons I dont understand
    // I have tested the password and username on https://www.smtper.net/ (both are correct)

    public static void main(String recepient, String data, String subject)/* throws MessagingException*/ {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "outlook.office365.com");
        properties.put("mail.smtp.port", "587");

/*
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthenticiation() {
                return new javax.mail.PasswordAuthentication("team18tester@outlook.com","123?!xxx");
            }
        });


        try {
            SmtpAuthenticator authenticator = new SmtpAuthenticator();
            //Message message = new MimeMessage(session);
            Message message = new MimeMessage(session.getDefaultInstance(properties,authenticator));
            message.setFrom(new InternetAddress("team18tester@outlook.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject(subject);
            message.setText(data);
            Transport.send(message);
            System.out.println("Done");

        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

    }

 */
    }
}