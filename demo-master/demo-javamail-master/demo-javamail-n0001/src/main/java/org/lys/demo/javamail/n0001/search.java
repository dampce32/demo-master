package org.lys.demo.javamail.n0001;

import java.io.InputStream;

/*
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SizeTerm;
import javax.mail.search.SubjectTerm;

/*
 * Search the given folder for messages matching the given
 * criteria.
 *
 * @author John Mani
 */

public class search {

	static String protocol = "pop3";
	static String host = "pop.163.com";
	static String user = "csit_java_test@163.com";
	static String password = "csitJava32";
	static String mbox = "INBOX";
	static String url = null;
	static boolean debug = false;

	public static void main(String argv[]) {

		String subject = "javamail";
		String from = null;
		boolean or = false;
		boolean today = false;
		int size = -1;


		try {

			// Get a Properties object
			Properties props = System.getProperties();

			// Get a Session object
			Session session = Session.getInstance(props, null);
			session.setDebug(true);

			// Get a Store object
			Store store = null;
			if (url != null) {
				URLName urln = new URLName(url);
				store = session.getStore(urln);
				store.connect();
			} else {
				if (protocol != null)
					store = session.getStore(protocol);
				else
					store = session.getStore();

				// Connect
				if (host != null || user != null || password != null)
					store.connect(host, user, password);
				else
					store.connect();
			}

			// Open the Folder

			Folder folder = store.getDefaultFolder();
			if (folder == null) {
				System.out.println("Cant find default namespace");
				System.exit(1);
			}

			folder = folder.getFolder(mbox);
			if (folder == null) {
				System.out.println("Invalid folder");
				System.exit(1);
			}

			folder.open(Folder.READ_ONLY);
			SearchTerm term = null;

			if (subject != null)
				term = new SubjectTerm(subject);
			if (from != null) {
				FromStringTerm fromTerm = new FromStringTerm(from);
				if (term != null) {
					if (or)
						term = new OrTerm(term, fromTerm);
					else
						term = new AndTerm(term, fromTerm);
				} else
					term = fromTerm;
			}
			if (today) {
				
				Calendar c = Calendar.getInstance();
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				c.set(Calendar.AM_PM, Calendar.AM);
				ReceivedDateTerm startDateTerm = new ReceivedDateTerm(ComparisonTerm.GE, c.getTime());
				c.add(Calendar.DATE, 1); // next day
				ReceivedDateTerm endDateTerm = new ReceivedDateTerm(ComparisonTerm.LT, c.getTime());
				SearchTerm dateTerm = new AndTerm(startDateTerm, endDateTerm);
				if (term != null) {
					if (or)
						term = new OrTerm(term, dateTerm);
					else
						term = new AndTerm(term, dateTerm);
				} else
					term = dateTerm;
			}

			if (size >= 0) {
				SizeTerm sizeTerm = new SizeTerm(ComparisonTerm.GT, size);
				if (term != null) {
					if (or)
						term = new OrTerm(term, sizeTerm);
					else
						term = new AndTerm(term, sizeTerm);
				} else
					term = sizeTerm;
			}

			Message[] msgs = folder.search(term);
			System.out.println("FOUND " + msgs.length + " MESSAGES");
			if (msgs.length == 0) // no match
				System.exit(1);

			// Use a suitable FetchProfile
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			folder.fetch(msgs, fp);

			for (int i = 0; i < msgs.length; i++) {
				System.out.println("--------------------------");
				System.out.println("MESSAGE #" + (i + 1) + ":");
				dumpPart(msgs[i]);
			}

			folder.close(false);
			store.close();
		} catch (Exception ex) {
			System.out.println("Oops, got exception! " + ex.getMessage());
			ex.printStackTrace();
		}

		System.exit(1);
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public static void dumpPart(Part p) throws Exception {
		if (p instanceof Message) {
			Message m = (Message) p;
			Address[] a;
			// FROM
			if ((a = m.getFrom()) != null) {
				for (int j = 0; j < a.length; j++)
					System.out.println("FROM: " + a[j].toString());
			}

			// TO
			if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
				for (int j = 0; j < a.length; j++)
					System.out.println("TO: " + a[j].toString());
			}

			// SUBJECT
			System.out.println("SUBJECT: " + m.getSubject());

			// DATE
			Date d = m.getSentDate();
			System.out.println("SendDate: " + (d != null ? d.toLocaleString() : "UNKNOWN"));

			// FLAGS:
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
					continue; // skip it
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
			System.out.println("FLAGS = " + sb.toString());
		}

		System.out.println("CONTENT-TYPE: " + p.getContentType());

		/*
		 * Dump input stream InputStream is = ((MimeMessage)m).getInputStream();
		 * int c; while ((c = is.read()) != -1) System.out.write(c);
		 */

		Object o = p.getContent();
		if (o instanceof String) {
			System.out.println("This is a String");
			System.out.println((String) o);
		} else if (o instanceof Multipart) {
			System.out.println("This is a Multipart");
			Multipart mp = (Multipart) o;
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				dumpPart(mp.getBodyPart(i));
		} else if (o instanceof InputStream) {
			System.out.println("This is just an input stream");
			InputStream is = (InputStream) o;
			int c;
			while ((c = is.read()) != -1)
				System.out.write(c);
		}
	}
}
