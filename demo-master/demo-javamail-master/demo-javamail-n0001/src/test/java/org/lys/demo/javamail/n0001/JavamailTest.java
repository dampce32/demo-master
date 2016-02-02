package org.lys.demo.javamail.n0001;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

public class JavamailTest {

	@Test
	public void testHelloworld() {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props, null);
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom("linyisong032@163.com");
			msg.setRecipients(Message.RecipientType.TO, "lys@csit.cc");
			msg.setSubject("JavaMail hello world example");
			msg.setSentDate(new Date());
			msg.setText("Hello, world!\n");
			Transport.send(msg, "linyisong032@163.com", "lys89625");
		} catch (MessagingException mex) {
			System.out.println("send failed, exception: " + mex);
		}
	}

}
