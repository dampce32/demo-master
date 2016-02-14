package org.lys.demo.javamail.n0001;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.HeaderTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.mail.util.ByteArrayDataSource;

import org.junit.Test;

import com.sun.mail.imap.IMAPFolder;

public class SampleTest {
	/**
	 * @description: 测试将一个文件夹下的邮件，复制到另一个文件夹下
	 * @createTime: 2016年2月4日 下午3:26:21
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testCopier() throws Exception {
		String src = "1";	// source folder
	    String dest = "2";	// dest folder
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		Store store = session.getStore(urlname);
		store.connect();
		JavaMailUtil.copyMsg(src, dest, store);
	}
	
	@Test
	public void testFolderlist() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder rf = store.getDefaultFolder();
//		JavaMailUtil.dumpFolder(rf, true, "");
		
		if ((rf.getType() & Folder.HOLDS_FOLDERS) != 0) {
		    Folder[] f = rf.list("1");
		    for (int i = 0; i < f.length; i++){
		    	JavaMailUtil.dumpFolder(f[i], true, "    ");
		    }
		}
	}
	
	@Test
	public void testMonitor() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder folder = store.getFolder("1");
		folder.open(Folder.READ_WRITE);
		
		folder.addMessageCountListener(new MessageCountAdapter() {
			
			public void messagesAdded(MessageCountEvent ev) {
				Message[] msgs = ev.getMessages();
				System.out.println("Got " + msgs.length + " new messages");

				// Just dump out the new messages
				for (int i = 0; i < msgs.length; i++) {
					try {
						System.out.println("-----");
						System.out.println("Message " + msgs[i].getMessageNumber() + ":");
						msgs[i].writeTo(System.out);
					} catch (IOException ioex) {
						ioex.printStackTrace();
					} catch (MessagingException mex) {
						mex.printStackTrace();
					}
				}
			}
		});
		int freq = 100;
		boolean supportsIdle = false;
		try {
			if (folder instanceof IMAPFolder) {
				IMAPFolder f = (IMAPFolder) folder;
				f.idle();
				supportsIdle = true;
			}
		} catch (FolderClosedException fex) {
			throw fex;
		} catch (MessagingException mex) {
			supportsIdle = false;
		}
		for (;;) {
			if (supportsIdle && folder instanceof IMAPFolder) {
				IMAPFolder f = (IMAPFolder) folder;
				f.idle();
				System.out.println("IDLE done");
			} else {
				Thread.sleep(freq); // sleep for freq milliseconds

				// This is to force the IMAP server to send us
				// EXISTS notifications.
				folder.getMessageCount();
			}
		}
	}
	
	@Test
	public void testMover() throws Exception {
		String src = "1";	// source folder
	    String dest = "2";	// dest folder
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		URLName urlname = new URLName("smtp","smtp.163.com",143,null,"csit_java_test@163.com","csitJava32");
		Store store = session.getStore(urlname);
		store.connect();
		JavaMailUtil.moveMsg(src, dest, store);
	}
	
	@Test
	public void testMsgMultiSendSample() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);
		
		String msgText1 = "This is a message body.\nHere's line two.";
		String msgText2 = "This is the text in the message attachment.";
		String to = "lys@csit.cc";
		String from = "csit_java_test@163.com";

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		InternetAddress[] address = { new InternetAddress(to) };
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject("JavaMail APIs Multipart Test");
		msg.setSentDate(new Date());

		// create and fill the first message part
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText(msgText1);

		// create and fill the second message part
		MimeBodyPart mbp2 = new MimeBodyPart();
		// Use setText(text, charset), to show it off !
		mbp2.setText(msgText2, "us-ascii");

		// create the Multipart and its parts to it
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);

		// add the Multipart to the message
		msg.setContent(mp);

		// send the message
		Transport.send(msg, "csit_java_test@163.com", "csitJava32");
	}
	
	@Test
	public void testMsgsend() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);
		
		String text = "This is a message body.\nHere's line two.";
		String to = "lys@csit.cc";
		String from = "csit_java_test@163.com";
		String mailer = "msgsend";
		
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		InternetAddress[] address = { new InternetAddress(to) };
		
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject("JavaMail APIs Msgsend ");
		msg.setSentDate(new Date());
		msg.setHeader("X-Mailer", mailer);
		
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText(text);
		//添加附件1
		MimeBodyPart mbp2 = new MimeBodyPart();
		mbp2.attachFile("C:/Users/linys/Pictures/Jellyfish.jpg");
		//添加附件2
		MimeBodyPart mbp3 = new MimeBodyPart();
		mbp3.attachFile("C:/Users/linys/Pictures/Koala.jpg");
		
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		mp.addBodyPart(mbp3);
		msg.setContent(mp);
		
		// send the message
		Transport.send(msg, "csit_java_test@163.com", "csitJava32");
	
		//把信息添加到文件夾"1"中
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder folder = store.getFolder("1");
		if (!folder.exists()){
			folder.create(Folder.HOLDS_MESSAGES);
		}

		Message[] msgs = new Message[1];
		msgs[0] = msg;
		folder.appendMessages(msgs);

		System.out.println("Mail was recorded successfully.");
	}
	
	@Test
	public void testNamespace() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);
		//把信息添加到文件夾"1"中
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		Store store = session.getStore(urlname);
		store.connect();
		
		namespace.printFolders("Personal", store.getPersonalNamespaces());
		namespace.printFolders("User \"" + "other" + "\"", store.getUserNamespaces("other"));
		namespace.printFolders("Shared", store.getSharedNamespaces());
	}
	/**
	 * @description: 文件夾迁移
	 * @createTime: 2016年2月5日 下午2:24:47
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testPopulate() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);
		
		URLName srcURLName = new URLName("imap","imap.163.com",143,null,"linyisong032@163.com","linyisong89625");
		Store  srcStore = session.getStore(srcURLName);
		srcStore.connect();
		Folder srcFolder = srcStore.getFolder("1");
		
		URLName dstURLName = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		Store  dstStore = session.getStore(dstURLName);
		dstStore.connect();
		Folder dstFolder = dstStore.getFolder("1");
		
		populate.copy(srcFolder, dstFolder);
		
		srcFolder.getStore().close();
		dstFolder.getStore().close();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testSearch() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);
		session.setDebug(false);
		
//		URLName urlName = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");//不能查询
		URLName urlName = new URLName("pop3","pop.163.com",110,null,"csit_java_test@163.com","csitJava32");//可查询
		Store  store = session.getStore(urlName);
		store.connect();
		Folder  folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);

		Message[] allMsgs =folder.getMessages();
		System.out.println("FOUND " + allMsgs.length + " MESSAGES");

		
		SearchTerm term =  new HeaderTerm("X-Tmail-Type","IMAP");;
		
		String subject = "javamail";
		String from = null;
		

		if (subject != null){
			term = new SubjectTerm(subject);
		}
		
		if (from != null) {
			FromStringTerm fromTerm = new FromStringTerm(from);
			if (term != null) {
					term = new AndTerm(term, fromTerm);
			} else{
				term = fromTerm;
			}
		}
		
		Message[] msgs = folder.search(term);
		System.out.println("FOUND " + msgs.length + " MESSAGES");
		
		store.close();
	}
	/**
	 * @description: 添加附件
	 * @createTime: 2016年2月14日 上午10:07:13
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testSendfile() throws Exception{
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);

		String msgText1 = "Sending a file.\n";
		String subject = "Sending a file";

		String to = "lys@csit.cc";
		String from = "csit_java_test@163.com";

		// create a message
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		InternetAddress[] address = { new InternetAddress(to) };
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject(subject);

		// create and fill the first message part
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText(msgText1);

		// create the second message part
		MimeBodyPart mbp2 = new MimeBodyPart();

		// attach the file to the message
		mbp2.attachFile("C:/Users/linys/Pictures/Koala.jpg");

		/*
		 * Use the following approach instead of the above line if you want to
		 * control the MIME type of the attached file. Normally you should never
		 * need to do this.
		 *
		 * FileDataSource fds = new FileDataSource(filename) { public String
		 * getContentType() { return "application/octet-stream"; } };
		 * mbp2.setDataHandler(new DataHandler(fds));
		 * mbp2.setFileName(fds.getName());
		 */

		// create the Multipart and add its parts to it
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);

		// add the Multipart to the message
		msg.setContent(mp);

		// set the Date: header
		msg.setSentDate(new Date());

		// send the message
		Transport.send(msg, "csit_java_test@163.com", "csitJava32");
	}
	@Test
	public void testSendhtml() throws Exception{
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);
		
		String msgText1 = "Sending a html.\n";
		String subject = "Sending a html";
		
		String to = "lys@csit.cc";
		String from = "csit_java_test@163.com";
		String mailer = "sendhtml";
		
		// create a message
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		InternetAddress[] address = { new InternetAddress(to) };
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject(subject);
		msg.setHeader("X-Mailer", mailer);
		msg.setSentDate(new Date());
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append(subject + "\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");

		sb.append("<BODY>\n");
		sb.append("<H1>" + subject + "</H1>" + "\n");
	    sb.append(msgText1);
	    sb.append("\n");

		sb.append("</BODY>\n");
		sb.append("</HTML>\n");

		msg.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html")));
		
		// send the message
		Transport.send(msg, "csit_java_test@163.com", "csitJava32");
		System.out.println("\nMail was sent successfully.");
	}
	
}
