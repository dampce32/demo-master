package org.lys.demo.javamail.n0001;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
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

import org.junit.Test;
/**
 * @description: QQ邮箱测试
 * @copyright: 福建骏华信息有限公司 (c)2016</p>
 * @createTime: 2016年2月15日下午4:14:52
 * @author：lys
 * @version：1.0
 */
public class JavamailSinaTest {
	
	public static String MAIL_SMTP_HOST="smtp.sina.com";
	public static Integer MAIL_SMTP_PORT=465;
	public static String MAIL_USER="linyisong032@sina.com";
	public static String MAIL_PASSWORD="lys89625";
	
	public static String MAIL_POP3_HOST="pop.sina.com";
	public static Integer MAIL_POP3_PORT=110;
	
	public static String MAIL_IMAP_HOST="imap.sina.com";
	public static Integer MAIL_IMAP_PORT=143;
	
	public static String FROM="linyisong032@sina.com";
	public static String TO="csit_java_test@163.com";

	/**
	 * @description: qq邮箱，只能用ssl发送才能成功
	 * @createTime: 2016年2月15日 下午4:15:16
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testHelloworld() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_SMTP_HOST);
		Session session = Session.getInstance(props);
		MimeMessage msg = new MimeMessage(session);
		
		msg.setFrom(FROM);
		msg.setRecipients(Message.RecipientType.TO, TO);
		msg.setSentDate(new Date());
		
		msg.setSubject("JavaMail SinaMail hello world example");
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
		msg.setSubject("SinaMail Sending a html");
		msg.setSentDate(new Date());
		
		//邮件内容
		 
        MimeBodyPart contentBody = new MimeBodyPart();//用于保存最终正文部分    
        MimeMultipart contentMulti = new MimeMultipart("related"); // 用于组合文本和图片，"related"型的MimeMultipart对象  
        
        String body = "<h4>内含附件、图文并茂的邮件测试！！！</h4> </br>" 
                + "<a href = http://www.csit.cc/><img src = \"cid:logo_jpg\"></a>"; 
        //正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        textBody.setContent(body, "text/html;charset=utf-8"); 
        contentMulti.addBodyPart(textBody);  
        
        // 正文的图片部分  
//        MimeBodyPart jpgBody = createContentAttach("C:/Users/linys/Pictures/Koala.jpg");  
        MimeBodyPart jpgBody = new MimeBodyPart();
        jpgBody.attachFile("C:/Users/linys/Pictures/Koala.jpg");
        jpgBody.setContentID("logo_jpg");  
        contentMulti.addBodyPart(jpgBody);  
 
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);  
        
        //创建邮件的附件部分  
        MimeBodyPart attachment01  = new MimeBodyPart();
        attachment01.attachFile("D:/mail_files/Koala.jpg");
        
        MimeBodyPart attachment02  = new MimeBodyPart();
        attachment02.attachFile("D:/mail_files/8月29日会议记录.docx");
        
        // 将邮件中各个部分组合到一个"mixed"型的 MimeMultipart 对象  
        MimeMultipart allPart = new MimeMultipart("mixed"); //也可不填，不填默认为"mixed"
        allPart.addBodyPart(contentBody);//正文必须放在前面，不然163邮箱，不解析正文，都当成附件处理  
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
