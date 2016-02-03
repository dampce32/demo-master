package org.lys.demo.javamail.n0001;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
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
	
	@Test
	public void testFolderByPop3() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	
		URLName urlname = new URLName("pop3","pop.163.com",110,null,"linyisong032@163.com","linyisong89625");
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder folder = store.getFolder("INBOX");//pop3只能取INBOX文件夹
        folder.open(Folder.READ_WRITE);
        
        int totalMessages = folder.getMessageCount();
        int newMessages = folder.getNewMessageCount();
        
        
        System.out.println("Total messages = " + totalMessages);
        System.out.println("New messages = " + newMessages);
        
        folder.close(true);
        store.close();

	}
	
	@Test
	public void testFolderByIMAP() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
	
		URLName urlname = new URLName("imap","imap.163.com",143,null,"linyisong032@163.com","linyisong89625");
		
		Store store = session.getStore(urlname);
		store.connect();
		
		Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        
        int totalMessages = folder.getMessageCount();
        int newMessages = folder.getNewMessageCount();
        
        
        System.out.println("Total messages = " + totalMessages);
        System.out.println("New messages = " + newMessages);
        
        folder.close(true);
        store.close();
	}
	
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
		}
        store.close();
	}
	
	
	
	@Test
	public void testShowMsg() throws Exception {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.pop3.host", "pop.163.com");
		Session session = Session.getInstance(props, null);
	
		URLName urlname = new URLName("pop3","pop.163.com",110,null,"linyisong032@163.com","lys89625");
		
		Store store = session.getStore(urlname);
		store.connect();
		
//		Store store = session.getStore("pop3");
//		store.connect("pop.163.com", 110, "linyisong032@163.com", "lys89625");
//		
		
		Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        
        int totalMessages = folder.getMessageCount();
        int newMessages = folder.getNewMessageCount();
        
        System.out.println("Total messages = " + totalMessages);
        System.out.println("New messages = " + newMessages);
        
		Message[] msgs = folder.getMessages();
		
		// Use a suitable FetchProfile
		FetchProfile fp = new FetchProfile();
		fp.add(FetchProfile.Item.ENVELOPE);
//		fp.add(FetchProfile.Item.FLAGS);
		fp.add("X-mailer");
		folder.fetch(msgs, fp);
				
		for (int i = 0; i < msgs.length; i++) {
		    System.out.println("--------------------------");
		    System.out.println("MESSAGE #" + msgs[i].getMessageNumber()+ ":");
		    dumpEnvelope(msgs[i]);
		    // dumpPart(msgs[i]);
		}
		
		

		
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
