package org.lys.demo.javamail.n0001;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.junit.Test;

import com.sun.mail.imap.IMAPFolder;

public class JavamailTest {
	
	public static String MAIL_SMTP_HOST="smtp.163.com";
	public static String MAIL_USER="csit_java_test@163.com";
	public static String MAIL_PASSWORD="csitJava32";
	
	public static String MAIL_POP3_HOST="pop.163.com";
	public static Integer MAIL_POP3_PORT=110;
	
	public static String MAIL_IMAP_HOST="imap.163.com";
	public static Integer MAIL_IMAP_PORT=143;
	
	public static String FROM="csit_java_test@163.com";
	public static String TO="lys@csit.cc";

	@Test
	public void testHelloworld() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_SMTP_HOST);
		Session session = Session.getInstance(props);
		MimeMessage msg = new MimeMessage(session);
		
		msg.setFrom(FROM);
		msg.setRecipients(Message.RecipientType.TO, TO);
		msg.setSentDate(new Date());
		
		msg.setSubject("JavaMail hello world example");
		msg.setText("Hello, world!\n");
		Transport.send(msg, MAIL_USER, MAIL_PASSWORD);
	}
	/**
	 * @description: 发送复杂邮件，正文中包含附件，整体邮件带附件
	 * @createTime: 2016年2月15日 上午11:04:06
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testSendHtmlAttachBycid() throws Exception{
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_SMTP_HOST);
		Session session = Session.getInstance(props);
		// create a message
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM));
		InternetAddress[] address = { new InternetAddress(TO) };
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject("Sending a html");
		msg.setSentDate(new Date());
		
		String body = "<h4>内含附件、图文并茂的邮件测试！！！</h4> </br>" 
                + "<a href = http://haolloyin.blog.51cto.com/> 蚂蚁</br>" 
                + "<img src = \"cid:logo_jpg\"></a>"; 
		
		//创建邮件的各个 MimeBodyPart 部分  
        MimeBodyPart attachment01  = new MimeBodyPart();
        attachment01.attachFile("D:/mail_files/Koala.jpg");
        
        MimeBodyPart attachment02  = new MimeBodyPart();
        attachment02.attachFile("D:/mail_files/8月29日会议记录.docx");
        
        //用于保存最终正文部分  
        MimeBodyPart contentBody = new MimeBodyPart();  
        // 用于组合文本和图片，"related"型的MimeMultipart对象  
        MimeMultipart contentMulti = new MimeMultipart("related");  
 
        // 正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        textBody.setContent(body, "text/html;charset=gbk");  
        contentMulti.addBodyPart(textBody);  
 
        // 正文的图片部分  
        MimeBodyPart jpgBody = new MimeBodyPart();  
        jpgBody.attachFile("C:/Users/linys/Pictures/Koala.jpg");
        jpgBody.setContentID("logo_jpg");  
        contentMulti.addBodyPart(jpgBody);  
 
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);  
        
        // 将邮件中各个部分组合到一个"mixed"型的 MimeMultipart 对象  
        MimeMultipart allPart = new MimeMultipart("mixed"); //默认为"mixed"
        allPart.addBodyPart(contentBody);  
        allPart.addBodyPart(attachment01);  
        allPart.addBodyPart(attachment02);  
 
        // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存  
        msg.setContent(allPart);  
        msg.saveChanges();  
		
		// send the message
        Transport.send(msg, MAIL_USER, MAIL_PASSWORD);
		System.out.println("\nMail was sent successfully.");

		msg.writeTo(new FileOutputStream("withAttachmentMail.eml"));  
	}
	
	/**
	 * @description: 列出邮箱中的文件夹(pop3)
	 * @createTime: 2016年2月4日 上午8:42:27
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testListFoldersByPop3() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	
		URLName urlname = new URLName("pop3",MAIL_POP3_HOST,MAIL_POP3_PORT,null,MAIL_USER,MAIL_PASSWORD);
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder rootFolder = store.getDefaultFolder();
		Folder[] folders = rootFolder.list();
		
		for (Folder folder : folders) {
			System.out.println(folder.getName());
		}
		assertEquals(folders.length,1);
		/*
		 *INBOX pop3只能取INBOX文件夹
		 */
        store.close();
	}
	/**
	 * @description: 列出文件夹（imap）
	 * @createTime: 2016年2月4日 上午8:45:33
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testListFoldersByIMAP() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	
		URLName urlname = new URLName("imap",MAIL_IMAP_HOST,MAIL_IMAP_PORT,null,MAIL_USER,MAIL_PASSWORD);
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder rootFolder = store.getDefaultFolder();
		Folder[] folders = rootFolder.list();
		
		for (Folder folder : folders) {
			System.out.println(folder.getName());
		}
		/*
		 *  INBOX
			草稿箱
			已发送
			已删除
			垃圾邮件
			病毒文件夹
			广告邮件
			订阅邮件
			1
			11
		 */
		assertTrue(folders.length>1);
        store.close();
	}
	
	
	/**
	 * @description: 企业邮箱
	 * @createTime: 2016年6月21日 上午11:06:07
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testSearchExmailFoldersByIMAP() {
		try {
			Properties props = new Properties();
//		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//ssl必须加这个
//		props.put("mail.imap.ssl.enable", "true");
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);
			
			URLName urlname = new URLName("imap",MAIL_IMAP_HOST,MAIL_IMAP_PORT,null,MAIL_USER,MAIL_PASSWORD);
			
			Store store = session.getStore(urlname);
			store.connect();
			
			javax.mail.Folder folder = store.getFolder("Sent Messages");
			folder.open(javax.mail.Folder.READ_WRITE);
			
//		String name = folder.getName();
//        int totalMessages = folder.getMessageCount();
//        int newMessages = folder.getNewMessageCount();
//        System.out.printf("Folder=%s,Total messages=%s,New messages=%s\n", name, totalMessages, newMessages);
			
//    	Calendar c = Calendar.getInstance();
//		c.set(Calendar.HOUR, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//		c.set(Calendar.AM_PM, Calendar.AM);
//        
//		SentDateTerm startDateTerm = 
//       		 new SentDateTerm(ComparisonTerm.GE, c.getTime());
//        
//        c.add(Calendar.DATE, 1);	// next day
//       		
//        SentDateTerm endDateTerm = 
//       		 new SentDateTerm(ComparisonTerm.LT, c.getTime());
//       	
//       	SearchTerm dateTerm = new AndTerm(startDateTerm, endDateTerm);
			
			
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.set(Calendar.AM_PM, Calendar.AM);
			
			ReceivedDateTerm startDateTerm = 
				 new ReceivedDateTerm(ComparisonTerm.GE, c.getTime());
			
			c.add(Calendar.DATE, 1);	// next day
				
			ReceivedDateTerm endDateTerm = 
				 new ReceivedDateTerm(ComparisonTerm.LT, c.getTime());
			
			SearchTerm dateTerm = new AndTerm(startDateTerm, endDateTerm);
				
				
			
//        Calendar calendar = Calendar.getInstance();  
//        calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK - (Calendar.DAY_OF_WEEK - 1)) - 1);  
//        calendar.add(Calendar.YEAR, -1);
//        Date mondayDate = calendar.getTime();  
//        SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE, mondayDate);  
//        SearchTerm comparisonTermLe = new SentDateTerm(ComparisonTerm.LE, new Date());  
//        SearchTerm comparisonAndTerm = new AndTerm(comparisonTermGe, comparisonTermLe); 
			
			
			
			Message[] msgs = folder.search(dateTerm);
			System.out.println("FOUND " + msgs.length + " MESSAGES");
			
			for (int i = 0; i < msgs.length; i++) {
				Message msg = msgs[i];
				System.out.println("ReceivedDate："+msg.getReceivedDate());
			}
			
			
			folder.close(true);
			
//		Folder rootFolder = store.getDefaultFolder();
//		Folder[] folders = rootFolder.list();
//		
//		for (Folder folder : folders) {
//			System.out.println(folder.getName());
//		}
//		/*
//		 *  INBOX
//			草稿箱
//			已发送
//			已删除
//			垃圾邮件
//			病毒文件夹
//			广告邮件
//			订阅邮件
//			1
//			11
//		 */
//		assertTrue(folders.length>1);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFolderProperties() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	
		URLName urlname = new URLName("imap",MAIL_IMAP_HOST,MAIL_IMAP_PORT,null,MAIL_USER,MAIL_PASSWORD);
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder rootFolder = store.getDefaultFolder();
		Folder[] folders = rootFolder.list();
		
		for (Folder folder : folders) {
	        folder.open(Folder.READ_WRITE);
	        String name = folder.getName();
	        int totalMessages = folder.getMessageCount();
	        int newMessages = folder.getNewMessageCount();
	        System.out.printf("Folder=%s,Total messages=%s,New messages=%s\n", name, totalMessages, newMessages);
	        folder.close(true);
		}
        store.close();
	}
	/**
	 * @description: 展示邮件信息
	 * @createTime: 2016年2月4日 上午9:24:17
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testShowMsg() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	
		URLName urlname = new URLName("imap",MAIL_IMAP_HOST,MAIL_IMAP_PORT,null,MAIL_USER,MAIL_PASSWORD);
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder folder = store.getFolder("1");
        folder.open(Folder.READ_WRITE);
        String name = folder.getFullName();
        int totalMessages = folder.getMessageCount();
        int newMessages = folder.getNewMessageCount();
        System.out.printf("Folder=%s,Total messages=%s,New messages=%s\n", name, totalMessages, newMessages);
        
        Message[] msgs = folder.getMessages();
		for (int i = 0; i < msgs.length; i++) {
		    System.out.println("MESSAGE #" + msgs[i].getMessageNumber()+ ":");
		    Message m = msgs[i];
		    dumpEnvelope(m);
		    System.out.println("IsContainAttch:"+JavaMailUtil.isContainAttch(m));
		    StringBuffer bodytext = new StringBuffer();
		    JavaMailUtil.getMailContent(m,bodytext);
		    System.out.println("Content:"+bodytext.toString());
		    
		    JavaMailUtil.saveAttchMent(m);
		}
        folder.close(true);
        store.close();
	}
	
	@Test
	public void testStoreListener() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	
		URLName urlname = new URLName("imap",MAIL_IMAP_HOST,MAIL_IMAP_PORT,null,MAIL_USER,MAIL_PASSWORD);
		
		Store store = session.getStore(urlname);
		store.addStoreListener(new StoreListener() {
			public void notification(StoreEvent e) {
			    String s;
			    if (e.getMessageType() == StoreEvent.ALERT)
				s = "ALERT: ";
			    else
				s = "NOTICE: ";
			    System.out.println(s + e.getMessage());
			}
	    });
		store.connect();
        store.close();
	}
	
	@Test
	public void testGetMsgExmailFoldersByIMAP() throws Exception {
			Properties props = new Properties();
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//ssl必须加这个
//			props.put("mail.imap.ssl.enable", "true");//开启ssl：false for the "imap" protocol and true for the "imaps" protocol
			
//			props.put("mail.imap.partialfetch", "false");//禁用fetchsize，防止重复读取附件，文件不断变大，程序不停止
//			props.setProperty("mail.imap.auth.login.disable", "true");
//			props.put("mail.imap.socketFactory.fallback","false");
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);
			
			URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
			
			Store store = session.getStore(urlname);
			store.connect();
			
			javax.mail.Folder folder = store.getFolder("INBOX");
			folder.open(javax.mail.Folder.READ_WRITE);
			IMAPFolder inbox = (IMAPFolder) folder; 
			
//			Message[] msgs = folder.getMessages();
//			for (Message message : msgs) {
//				String uid = Long.toString(inbox.getUID(message));
//				
//				System.out.println("-------------邮件:"+uid+"-----");
//				
//				dumpEnvelope(message);
//				
//			}
			
			Message thisMsg = inbox.getMessageByUID(1454652894);
			
			dumpEnvelope(thisMsg);
			
			folder.close(true);
			store.close();
	} 
	
	public static void dumpEnvelope(Message m) throws Exception {
		System.out.println("FROM: " + JavaMailUtil.getFrom(m));
		System.out.println("REPLY TO: " + JavaMailUtil.getReplyTo(m));
		System.out.println("TO: " + JavaMailUtil.getTo(m));
		System.out.println("CC: " + JavaMailUtil.getCC(m));
		System.out.println("BCC: " + JavaMailUtil.getBCC(m));
		System.out.println("SUBJECT: " + m.getSubject());
		System.out.println("SendDate: " +JavaMailUtil.getSendDate(m));
		System.out.println("system flags: " +JavaMailUtil.getSystemFlags(m));
		System.out.println("user flag: " +JavaMailUtil.getUserFlags(m));
		System.out.println("X-MAILER: " +JavaMailUtil.getMailer(m));
	}
}
