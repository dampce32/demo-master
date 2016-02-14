package org.lys.demo.javamail.n0001;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
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

public class JavamailTest {

	@Test
	public void testHelloworld() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props, null);
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom("csit_java_test@163.com");
			msg.setRecipients(Message.RecipientType.TO, "lys@csit.cc");
			msg.setSubject("JavaMail hello world example");
			msg.setSentDate(new Date());
			msg.setText("Hello, world!\n");
			Transport.send(msg, "csit_java_test@163.com", "csitJava32");
		} catch (MessagingException mex) {
			System.out.println("send failed, exception: " + mex);
		}
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
	
		URLName urlname = new URLName("pop3","pop.163.com",110,null,"csit_java_test@163.com","csitJava32");
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder rootFolder = store.getDefaultFolder();
		Folder[] folders = rootFolder.list();
		
		for (Folder folder : folders) {
			System.out.println(folder.getName());
			folder.close(true);
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder rootFolder = store.getDefaultFolder();
		Folder[] folders = rootFolder.list();
		
		for (Folder folder : folders) {
			System.out.println(folder.getName());
			folder.close(true);
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"csit_java_test@163.com","csitJava32");
		
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
	
	@Test
	public void testSendHtmlAttach() throws Exception{
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		Session session = Session.getInstance(props);
		
		String subject = "Sending a html";
		
		String to = "lys@csit.cc";
		String from = "csit_java_test@163.com";
		
		// create a message
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		InternetAddress[] address = { new InternetAddress(to) };
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		
		String body = "<h4>内含附件、图文并茂的邮件测试！！！</h4> </br>" 
                + "<a href = http://haolloyin.blog.51cto.com/> 蚂蚁</br>" 
                + "<img src = \"cid:logo_jpg\"></a>"; 
		
		 // 创建邮件的各个 MimeBodyPart 部分  
        MimeBodyPart attachment01  = new MimeBodyPart();
        attachment01.attachFile("D:/mail_files/Koala.jpg");
        
        MimeBodyPart attachment02  = new MimeBodyPart();
        attachment02.attachFile("D:/mail_files/8月29日会议记录.docx");
        
        MimeBodyPart content = createContent(body, "C:/Users/linys/Pictures/Koala.jpg");  
 
        // 将邮件中各个部分组合到一个"mixed"型的 MimeMultipart 对象  
        MimeMultipart allPart = new MimeMultipart();  
//        MimeMultipart allPart = new MimeMultipart("mixed");  
        allPart.addBodyPart(attachment01);  
        allPart.addBodyPart(attachment02);  
        allPart.addBodyPart(content);  
 
        // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存  
        msg.setContent(allPart);  
        msg.saveChanges();  
		
		// send the message
        Transport.send(msg, "csit_java_test@163.com", "csitJava32");
		System.out.println("\nMail was sent successfully.");

		msg.writeTo(new FileOutputStream("withAttachmentMail.eml"));  
	}
	
	/**  
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分  
     */ 
    public MimeBodyPart createContent(String body, String fileName)  
            throws Exception {  
        // 用于保存最终正文部分  
        MimeBodyPart contentBody = new MimeBodyPart();  
        // 用于组合文本和图片，"related"型的MimeMultipart对象  
        MimeMultipart contentMulti = new MimeMultipart("related");  
 
        // 正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        textBody.setContent(body, "text/html;charset=gbk");  
        contentMulti.addBodyPart(textBody);  
 
        // 正文的图片部分  
        MimeBodyPart jpgBody = new MimeBodyPart();  
        FileDataSource fds = new FileDataSource(fileName);  
        jpgBody.setDataHandler(new DataHandler(fds));  
        jpgBody.setContentID("logo_jpg");  
        contentMulti.addBodyPart(jpgBody);  
 
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);  
        return contentBody;  
    } 
    
    
    /**  
     * 根据传入的文件路径创建附件并返回  
     */ 
    public MimeBodyPart createAttachment(String fileName) throws Exception {  
        MimeBodyPart attachmentPart = new MimeBodyPart();  
        FileDataSource fds = new FileDataSource(fileName);  
        attachmentPart.setDataHandler(new DataHandler(fds));  
        attachmentPart.setFileName(fds.getName());  
        return attachmentPart;  
    } 
}
