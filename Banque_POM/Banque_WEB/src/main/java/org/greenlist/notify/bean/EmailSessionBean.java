package org.greenlist.notify.bean;

import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class EmailSessionBean {

	private static final String SMTP = "smtp.gmail.com";
	private static final String SMTPPORT = "465";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String FROM = "from@no-spam.com";

	public void sendEmail(String to, String subject, String body) {

		Properties props = new Properties();

		props.put("mail.smtp.host", SMTP);
		props.put("mail.smtp.socketFactory.port", 465);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);

			System.out.println("Email Sent " + to + " " + subject + " ");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
