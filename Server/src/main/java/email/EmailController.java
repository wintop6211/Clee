package main.java.email;

import main.java.services.helpers.PathManager;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * The class is the controller for sending emails. Feel free to modify this class if you have your own email servers.
 * Also, if you need to send more kinds of emails, simply add more public methods and helper methods.
 * Feel free to add anything that you think necessary
 */
public class EmailController {

    private static Protocol protocol = Protocol.TLS;
    private final static String USER_NAME = "";
    private final static String PASSWORD = "";
    private final static String HOST = "smtp.gmail.com";
    private final static int PORT = 587;

    /**
     * Different protocols which may be used for sending email requests to email servers
     */
    private enum Protocol {
        SMTPS,
        TLS
    }

    /**
     * Sends the verification link email
     *
     * @param emailAddress    The email address of the receiver
     * @param loginIdentifier The identifier which identifies the user.
     * @throws MessagingException The error will be thrown when the email cannot be delivered
     */
    public static void sendVerificationEmail(String emailAddress, String loginIdentifier) throws MessagingException {
        String link = PathManager.getURL("services/UserServices/verifyEmailAddress/" + loginIdentifier);
        String body = "Please click the link in order to activate your account";
        EmailController.sendClickLinkEmail(emailAddress, "CollegeBuyer VerificationEmail Verification", body, link);
    }

    /**
     * Sends the forgot password email
     *
     * @param emailAddress    The email address of the receiver
     * @param loginIdentifier The identifier which identifies the user
     * @throws MessagingException The error which will be thrown when the email cannot be delivered
     */
    public static void sendForgotPasswordEmail(String emailAddress, String loginIdentifier) throws MessagingException {
        String link = PathManager.getURL("services/UserServices/changePassword/" + loginIdentifier);
        String body = "Please click the link in order to change your password";
        EmailController.sendClickLinkEmail(emailAddress, "CollegeBuyer Forgot Password", body, link);
    }

    /**
     * Sends the email which contains a link
     *
     * @param to      The email address of the receiver
     * @param subject The subject field of the email
     * @param body    The body of the email
     * @param link    The link which needs to be inserted
     * @throws MessagingException The error will be thrown when the email cannot be delivered
     */
    private static void sendClickLinkEmail(String to, String subject, String body, String link) throws MessagingException {
        MimeMessage message = initEmailProtocol();
        String htmlBody = String.format("<body><p>%s: <a href=%s>click here</a></p></body>", body, link);
        send(message, to, subject, htmlBody);
    }

    /**
     * Initializes the email protocol
     *
     * @return MimeMessage object for sending the message
     */
    private static MimeMessage initEmailProtocol() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        switch (protocol) {
            case SMTPS:
                properties.put("mail.smtp.ssl.enable", true);
                break;
            case TLS:
                properties.put("mail.smtp.starttls.enable", true);
                break;
        }
        properties.put("mail.smtp.auth", true);

        Authenticator authenticator;
        authenticator = new Authenticator() {
            private PasswordAuthentication pa = new PasswordAuthentication(USER_NAME, PASSWORD);

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return pa;
            }
        };

        Session session = Session.getInstance(properties, authenticator);
        session.setDebug(true);

        return new MimeMessage(session);
    }

    /**
     * Sends the message to the destination
     *
     * @param message  The message that needs to be sent
     * @param to       The receiver email address
     * @param subject  The subject field of the email
     * @param htmlBody The body of the email
     * @throws MessagingException The error will be thrown when the email cannot be delivered
     */
    private static void send(MimeMessage message, String to, String subject, String htmlBody) throws MessagingException {
        InternetAddress[] address = {new InternetAddress(to)};
        message.setRecipients(Message.RecipientType.TO, address);
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(htmlBody, "UTF-8", "html");
        Transport.send(message);
    }
}
