package org.lys.demo.javamail.n0001;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
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
	
	@Test
	public void testShowMsg() throws Exception {

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
	        
	        Message[] msgs = folder.getMessages();
			
			// Use a suitable FetchProfile
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
//			fp.add(FetchProfile.Item.FLAGS);
			fp.add("X-mailer");
			folder.fetch(msgs, fp);
					
			for (int i = 0; i < msgs.length; i++) {
			    System.out.println("MESSAGE #" + msgs[i].getMessageNumber()+ ":");
			    dumpEnvelope(msgs[i]);
			}
	        folder.close(true);
		}
        store.close();
	}
	
	public static void dumpEnvelope(Message m) throws Exception {
		// SUBJECT
		pr("SUBJECT: " + m.getSubject());
		
		// FLAGS
		Flags flags = m.getFlags();
		StringBuffer sb = new StringBuffer();
		Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

		boolean first = true;
		for (int i = 0; i < sf.length; i++) {
		    String s;
		    Flags.Flag f = sf[i];
		    if (f == Flags.Flag.ANSWERED)
			s = "\\Answered";
		    else if (f == Flags.Flag.DELETED)
			s = "\\Deleted";
		    else if (f == Flags.Flag.DRAFT)
			s = "\\Draft";
		    else if (f == Flags.Flag.FLAGGED)
			s = "\\Flagged";
		    else if (f == Flags.Flag.RECENT)
			s = "\\Recent";
		    else if (f == Flags.Flag.SEEN)
			s = "\\Seen";
		    else
			continue;	// skip it
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(s);
		}

		String[] uf = flags.getUserFlags(); // get the user flag strings
		for (int i = 0; i < uf.length; i++) {
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(uf[i]);
		}
		pr("FLAGS: " + sb.toString());
		
		
		// X-MAILER
		String[] hdrs = m.getHeader("X-Mailer");
		if (hdrs != null)
		    pr("X-Mailer: " + hdrs[0]);
		else
		    pr("X-Mailer NOT available");
	}
	
	static String indentStr = "                                               ";
    static int level = 0;

    /**
     * Print a, possibly indented, string.
     */
    public static void pr(String s) {
	    System.out.print(indentStr.substring(0, level * 2));
	    System.out.println(s);
    }
	

}
