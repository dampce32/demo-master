package org.lys.demo.javamail.n0001;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPFolder;
/**
 * @description: JavaMail 工具类
 * @copyright: 福建骏华信息有限公司 (c)2016</p>
 * @createTime: 2016年2月4日下午3:08:47
 * @author：lys
 * @version：1.0
 */
public class JavaMailUtil {
	/**
	 * @description: 取得邮件的发件人
	 * @createTime: 2016年2月4日 上午10:12:21
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getFrom(Message m) throws MessagingException{
        InternetAddress[] address = (InternetAddress[]) m.getFrom();
        String from = address[0].getAddress();
        if(from == null){
            from = "";
        }
        String personal = address[0].getPersonal();
        if(personal == null){
            personal = "";
        }
        String fromaddr = personal +"<"+from+">";
        return fromaddr;
    }
	/**
	 * @description: 取得回复人信息
	 * @createTime: 2016年2月4日 上午10:43:48
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getReplyTo(Message m) throws MessagingException{
        InternetAddress[] address = (InternetAddress[]) m.getReplyTo();
        StringBuilder sb = new StringBuilder();
        for (InternetAddress internetAddress : address) {
        	String addr = internetAddress.getAddress();
        	String personal = internetAddress.getPersonal();
        	
            if(addr == null){
            	addr = "";
            }
            if(personal == null){
                personal = "";
            }
            String replayToAddr = personal +"<"+addr+">";
            sb.append(replayToAddr);
		}
        return sb.toString();
    }
	/**
	 * @description: 取得收件人
	 * @createTime: 2016年2月4日 上午11:13:07
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getTo(Message m) throws MessagingException{
        InternetAddress[] address = (InternetAddress[]) m.getRecipients(Message.RecipientType.TO);
        StringBuilder sb = new StringBuilder();
        for (InternetAddress internetAddress : address) {
        	String addr = internetAddress.getAddress();
        	String personal = internetAddress.getPersonal();
        	
            if(addr == null){
            	addr = "";
            }
            if(personal == null){
                personal = "";
            }
            String replayToAddr = personal +"<"+addr+">";
            sb.append(replayToAddr);
		}
        return sb.toString();
    }
	/**
	 * @description: 取得抄送人
	 * @createTime: 2016年2月4日 上午11:17:03
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getCC(Message m) throws MessagingException{
        InternetAddress[] address = (InternetAddress[]) m.getRecipients(Message.RecipientType.CC);//CC:carbon(复写纸) copy
        StringBuilder sb = new StringBuilder();
        if(address!=null){
        	for (InternetAddress internetAddress : address) {
        		String addr = internetAddress.getAddress();
        		String personal = internetAddress.getPersonal();
        		
        		if(addr == null){
        			addr = "";
        		}
        		if(personal == null){
        			personal = "";
        		}
        		String replayToAddr = personal +"<"+addr+">";
        		sb.append(replayToAddr);
        	}
        }
        return sb.toString();
    }
	/**
	 * @description: 取得密送（查询不出来）
	 * @createTime: 2016年2月4日 上午11:28:11
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getBCC(Message m) throws MessagingException{
        InternetAddress[] address = (InternetAddress[]) m.getRecipients(Message.RecipientType.BCC);//BCC:blind（盲目的；瞎的） carbon(复写纸) copy
        StringBuilder sb = new StringBuilder();
        if(address!=null){
        	for (InternetAddress internetAddress : address) {
        		String addr = internetAddress.getAddress();
        		String personal = internetAddress.getPersonal();
        		
        		if(addr == null){
        			addr = "";
        		}
        		if(personal == null){
        			personal = "";
        		}
        		String replayToAddr = personal +"<"+addr+">";
        		sb.append(replayToAddr);
        	}
        }
        return sb.toString();
    }
	/**
	 * @description: 取得邮件发送时间
	 * @createTime: 2016年2月4日 上午11:33:59
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getSendDate(Message m) throws MessagingException{
        Date sendDate = m.getSentDate();
        SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        return smd.format(sendDate);
    }
	/**
	 * @description: 取得系统标记
	 * @createTime: 2016年2月4日 下午12:06:27
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getSystemFlags(Message m) throws MessagingException{
		StringBuffer sb = new StringBuffer();
		Flags flags = m.getFlags();
		
		Flags.Flag[] sf = flags.getSystemFlags();

		boolean first = true;
		for (int i = 0; i < sf.length; i++) {
			Flags.Flag f = sf[i];
			
		    String s;
		    
		    if (f == Flags.Flag.ANSWERED)
			s = "已回复";
		    else if (f == Flags.Flag.DELETED)
			s = "Deleted";
		    else if (f == Flags.Flag.DRAFT)
			s = "Draft";
		    else if (f == Flags.Flag.FLAGGED)
			s = "Flagged";
		    else if (f == Flags.Flag.RECENT)
			s = "Recent";
		    else if (f == Flags.Flag.SEEN)
			s = "已读";
		    else
			continue;	// skip it
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(s);
		}
		return sb.toString();
    }
	/**
	 * @description: 取得用户标记
	 * @createTime: 2016年2月4日 下午12:06:12
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getUserFlags(Message m) throws MessagingException{
		StringBuffer sb = new StringBuffer();
		Flags flags = m.getFlags();
		boolean first = true;
		
		String[] uf = flags.getUserFlags();
		for (int i = 0; i < uf.length; i++) {
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(uf[i]);
		}
		return sb.toString();
	}
	/**
	 * @description: 取得邮寄者
	 * @createTime: 2016年2月4日 下午12:30:29
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static String getMailer(Message m) throws MessagingException{
		String mailer="";
		String[] hdrs = m.getHeader("X-Mailer");
		if (hdrs != null)
			mailer=hdrs[0];
		return mailer;
	}
	
	/**
	 * @description: 是否需要回执，如需回执返回true，否则返回false
	 * @createTime: 2016年2月4日 下午12:48:24
	 * @author: lys
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	public static boolean getReplySign(Message m) throws MessagingException{
        boolean replySign = false;
        String needreply[] = m.getHeader("Disposition-Notification-TO");
        if(needreply != null){
            replySign = true;
        }
        return replySign;
    }
	/**
	 * @description: 是否包含附件：以附件形式添加或正文中添加图片等附件都认为添加了附件
	 * @createTime: 2016年2月4日 下午2:17:05
	 * @author: lys
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean isContainAttch(Part part) throws MessagingException, IOException{
        boolean flag = false;
        /*
         * multipart:related
         */
        if(part.isMimeType("multipart/*")){
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for(int i=0;i<count;i++){
                BodyPart bodypart = multipart.getBodyPart(i);
                String dispostion = bodypart.getDisposition();//Disposition:部署--attachment
                /*
                 * 如果有添加附件，attachment  
                 */
                if((dispostion != null)&&(dispostion.equals(Part.ATTACHMENT)||dispostion.equals(Part.INLINE))){
                    flag = true;
                }else if(bodypart.isMimeType("multipart/*")){
                    flag = isContainAttch(bodypart);
                }else{
                    String conType = bodypart.getContentType();
                    if(conType.toLowerCase().indexOf("appliaction")!=-1){
                        flag = true;
                    }
                    if(conType.toLowerCase().indexOf("name")!=-1){//image/jpeg; name="139ECBDC@F2F76B39.28EBB256.jpg"
                        flag = true;
                    }
                }
            }
        }else if(part.isMimeType("message/rfc822")){
            flag = isContainAttch((Part) part.getContent());
        }
        return flag;
    }
	/**
	 * @description: 解析邮件，将得到的邮件内容保存到一个stringBuffer对象中，解析邮件 主要根据MimeType的不同执行不同的操作，一步一步的解析 
	 * @createTime: 2016年2月4日 下午2:19:59
	 * @author: lys
	 * @param part
	 * @param bodytext
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void getMailContent(Part part,StringBuffer bodytext) throws MessagingException, IOException{
        String contentType = part.getContentType();
        int nameindex = contentType.indexOf("name");
        boolean conname = false;
        if(nameindex != -1){
            conname = true;
        }
        if(part.isMimeType("text/plain")&&!conname){
            bodytext.append((String)part.getContent());
        }else if(part.isMimeType("text/html")&&!conname){
            bodytext.append((String)part.getContent());
        }else if(part.isMimeType("multipart/*")){
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for(int i=0;i<count;i++){
                getMailContent(multipart.getBodyPart(i),bodytext);
            }
        }else if(part.isMimeType("message/rfc822")){
            getMailContent((Part) part.getContent(),bodytext); 
        }
    }
	/**
	 * @description: 保存附件
	 * @createTime: 2016年2月4日 下午2:37:09
	 * @author: lys
	 * @param part
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void saveAttchMent(Part part) throws MessagingException, IOException{
        String filename = "";
        if(part.isMimeType("multipart/*")){
            Multipart mp = (Multipart) part.getContent();
            for(int i=0;i<mp.getCount();i++){
                BodyPart mpart = mp.getBodyPart(i);
                String dispostion = mpart.getDisposition();
                if((dispostion != null)&&(dispostion.equals(Part.ATTACHMENT)||dispostion.equals(Part.INLINE))){
                    filename = mpart.getFileName();
                    if(filename.toLowerCase().indexOf("gb2312")!=-1){
                        filename = MimeUtility.decodeText(filename);
                    }
                    saveFile(filename,mpart.getInputStream());
                }else if(mpart.isMimeType("multipart/*")){
                    saveAttchMent(mpart);
                }else{
                    filename = mpart.getFileName();
                    if(filename != null&&(filename.toLowerCase().indexOf("gb2312")!=-1)){
                        filename = MimeUtility.decodeText(filename);
                        saveFile(filename,mpart.getInputStream());
                    }
                }
            }
        }else if(part.isMimeType("message/rfc822")){
            saveAttchMent((Part) part.getContent());
        }
    }
	
	private static void saveFile(String filename, InputStream inputStream) throws IOException {
        String osname = System.getProperty("os.name");
        String storedir = null;
        String sepatror = "";
        if(osname == null){
            osname = "";
        }
       
        if(osname.toLowerCase().indexOf("win")!=-1){
            sepatror = "//";
            if(storedir==null||"".equals(storedir)){
                storedir = "d://temp";
            }
        }else{
            sepatror = "/";
            storedir = "/temp";
        }
       
        File storefile = new File(storedir+sepatror+filename);
       
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
       
        try {
            bos = new BufferedOutputStream(new FileOutputStream(storefile));
            bis = new BufferedInputStream(inputStream);
            int c;
            while((c= bis.read())!=-1){
                bos.write(c);
                bos.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            bos.close();
            bis.close();
        }
       
    }
	/**
	 * @description: 复制文件夹下的邮件
	 * @createTime: 2016年2月4日 下午3:54:50
	 * @author: lys
	 * @param src
	 * @param dest
	 * @param store
	 * @throws MessagingException
	 */
	public static void copyMsg(String src, String dest, Store store) throws MessagingException {
		Folder folder = store.getFolder(src);
		folder.open(Folder.READ_WRITE);
		if (folder.getMessageCount() == 0) {
			folder.close(false);
			store.close();
			return;
	    }
		
		Folder dfolder = store.getFolder(dest);
		if (!dfolder.exists()){
			dfolder.create(Folder.HOLDS_MESSAGES);
		}
		Message[] msgs = folder.getMessages();
		folder.copyMessages(msgs, dfolder);
		folder.close(false);
        store.close();
	}
	
	/**
	 * @description: 导出文件夹的目录以及文件夹信息
	 * @createTime: 2016年2月4日 下午3:56:19
	 * @author: lys
	 * @param folder
	 * @param recurse
	 * @param tab
	 * @throws Exception
	 */
	public static void dumpFolder(Folder folder, boolean recurse, String tab)
			throws Exception {
		System.out.println(tab + "Name:      " + folder.getName());
		System.out.println(tab + "Full Name: " + folder.getFullName());
		System.out.println(tab + "URL:       " + folder.getURLName());
		
		if (!folder.isSubscribed())
			System.out.println(tab + "Not Subscribed");
		
		if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
			if (folder.hasNewMessages())
				System.out.println(tab + "Has New Messages");
			System.out.println(tab + "Total Messages:  " + folder.getMessageCount());
			System.out.println(tab + "New Messages:    " + folder.getNewMessageCount());
			System.out.println(tab + "Unread Messages: " + folder.getUnreadMessageCount());
		}
		if ((folder.getType() & Folder.HOLDS_FOLDERS) != 0)
			System.out.println(tab + "Is Directory");
		
		/*
		 * Demonstrate use of IMAP folder attributes returned by the IMAP LIST
		 * response.
		 */
		if (folder instanceof IMAPFolder) {
			IMAPFolder f = (IMAPFolder) folder;
			String[] attrs = f.getAttributes();
			if (attrs != null && attrs.length > 0) {
				System.out.println(tab + "IMAP Attributes:");
				for (int i = 0; i < attrs.length; i++)
					System.out.println(tab + "    " + attrs[i]);
			}
		}
		System.out.println();
		if ((folder.getType() & Folder.HOLDS_FOLDERS) != 0) {
			if (recurse) {
				Folder[] f = folder.list();
				for (int i = 0; i < f.length; i++)
					dumpFolder(f[i], recurse, tab + "    ");
			}
		}
	}
	
	public static void moveMsg(String src, String dest, Store store) throws MessagingException {
		Folder folder = store.getFolder(src);
		folder.open(Folder.READ_WRITE);
		if (folder.getMessageCount() == 0) {
			folder.close(false);
			store.close();
			return;
	    }
		
		Folder dfolder = store.getFolder(dest);
		if (!dfolder.exists()){
			dfolder.create(Folder.HOLDS_MESSAGES);
		}
		Message[] msgs = folder.getMessages();
		folder.copyMessages(msgs, dfolder);
		folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
		folder.close(false);
        store.close();
	}
}
