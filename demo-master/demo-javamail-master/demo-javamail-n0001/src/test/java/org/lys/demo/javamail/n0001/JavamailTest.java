package org.lys.demo.javamail.n0001;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;
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
			Transport.send(msg, "linyisong032@163.com", "linyisong89625");
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
	
		URLName urlname = new URLName("pop3","pop.163.com",110,null,"linyisong032@163.com","linyisong89625");
		
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"linyisong032@163.com","linyisong89625");
		
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"linyisong032@163.com","linyisong89625");
		
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"linyisong032@163.com","linyisong89625");
		
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
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"linyisong032@163.com","linyisong89625");
		
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
