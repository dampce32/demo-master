package org.lys.demo.javamail.n0002;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest {
	
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
	 * @description: 发送复杂邮件，正文中包含附件，整体邮件带附件
	 * @createTime: 2016年2月15日 上午11:04:06
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testWriteEml() throws Exception{
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_SMTP_HOST);
		Session session = Session.getInstance(props);
		
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM));
		InternetAddress[] address = {new InternetAddress(TO)};
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
        textBody.setContent(body, "text/html;charset=utf-8");  
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

		msg.writeTo(new FileOutputStream("D:/mail_files/withAttachmentMail.eml"));  
	}
	
	/**
	 * @description: 发送eml文件
	 * @createTime: 2016年2月15日 下午5:27:13
	 * @author: lys
	 * @throws Exception
	 */
	@Test
	public void testSendEml() throws Exception{
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_SMTP_HOST);
		Session session = Session.getInstance(props);
		
	    InputStream in = new FileInputStream("D:/mail_files/withAttachmentMail.eml");
		
		MimeMessage msg = new MimeMessage(session,in);
		
		Transport.send(msg, MAIL_USER, MAIL_PASSWORD);
	}
	
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
	public void testInternetAddress() throws Exception {
		InternetAddress[] a = InternetAddress.parse("林以宋<lys@csit.cc>,578935829 <578935829@qq.com>,linyisong032<linyisong032@163.com>");
//		for (InternetAddress internetAddress : a) {
//			System.out.println(internetAddress.getPersonal());
//			System.out.println(internetAddress.getAddress());
//		}
		
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_SMTP_HOST);
		Session session = Session.getInstance(props);
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, a);
		
		Address[] b = msg.getRecipients(Message.RecipientType.TO);
		for (Address address : b) {
			InternetAddress c = (InternetAddress)address;
			System.out.println(c.getPersonal());
			System.out.println(c.getAddress());
		}
		
		InternetAddress internetAddress = new InternetAddress(MimeUtility.encodeText("578935829 <578935829@qq.com>"));
	}
	
	@Test
	public void testInternetAddressArray() throws Exception {
		String source = "林以宋<lys@csit.cc>,578935829 <578935829@qq.com>,linyisong032<linyisong032@163.com>";
		InternetAddress[] a = InternetAddress.parse(source);
//		for (InternetAddress internetAddress : a) {
//			System.out.println(internetAddress.getPersonal());
//			System.out.println(internetAddress.getAddress());
//		}
		
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_SMTP_HOST);
		Session session = Session.getInstance(props);
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, a);
		
		Address[] b = msg.getRecipients(Message.RecipientType.TO);
		for (Address address : b) {
			InternetAddress c = (InternetAddress)address;
//			System.out.println(c.getPersonal());
//			System.out.println(c.getAddress());
		}
		InternetAddress internetAddress = new InternetAddress("林以宋<lys@csit.cc>");
		internetAddress.setPersonal(MimeUtility.encodeText(internetAddress.getPersonal()));
		
		String str = MimeUtility.encodeText("林以宋");
		System.out.println(str);
		System.out.println(MimeUtility.decodeText(str));
		
		System.out.println(internetAddress.getPersonal());
		System.out.println(MimeUtility.decodeText(internetAddress.getPersonal()) );
	}
	
	
	@Test
	public void testWriteTo() throws Exception {
		try {
			Properties props = new Properties();
//			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//ssl必须加这个
//			props.put("mail.imap.ssl.enable", "true");
//			 props.setProperty("mail.imap.auth.login.disable", "true"); 
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);
			
			URLName urlname = new URLName("imap","imap.exmail.qq.com",143,null,"sherry@gffairs.com","Ericzhou741015");
			
			Store store = session.getStore(urlname);
			store.connect();
			
			javax.mail.Folder folder = store.getFolder("INBOX");
			folder.open(javax.mail.Folder.READ_WRITE);
			
			Message[] messages = folder.getMessages();
			Message message = messages[messages.length-1];
			System.out.println(message.getSubject());
			message.writeTo(new FileOutputStream("D:/32/11.eml"));
			
			
			folder.close(true);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
