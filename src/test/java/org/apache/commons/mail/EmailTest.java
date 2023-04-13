/*
 * Author       : Jaishree Jaikumar
 * Title        : CIS 376 Assignment 3;
 * 		          Unit Testing for Apache
 * 				  Commons Email class
 * Date Created : 18/3/2023
 * Date Modified: 21/03/2023
*/

package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	
	private Email email;
	// Setup
	@Before
	public void SetUp() {
		email = new EmailDummy();
	}
	
	// Testing getHostName method using object with non-null session
	@Test
	public void GetHostNameWithSessionTest() {
		Properties prop = new Properties();
		prop.setProperty(EmailConstants.MAIL_HOST, "localhost");
		Session scn = Session.getInstance(prop);
		email.setMailSession(scn);
		
		String hostname = email.getHostName();
		assertEquals("localhost", hostname);
	}
	
	// Testing getHostName method using object with null session
	// but with a non-null host name
	@Test
	public void GetHostNameWithoutSessionTest() {
		email.setHostName("localhost");
		String hostname = email.getHostName();
		assertEquals("localhost", hostname);
	}
	
	// Testing getHostName method using object with null session
	// and host name
	@Test
	public void GetHostNameNullTest() {
		email.setHostName(null);
		assertEquals(null, email.getHostName());
	}
	
	// Testing addCc method on the size and instances
	@Test
	public void AddCcTest() throws EmailException {
	    email.addCc("cc1@example.com");
	    email.addCc("cc2@example.com");
	    email.addCc("cc3@example.com");

	    assertEquals(3, email.getCcAddresses().size());

	    assertEquals("cc1@example.com", email.getCcAddresses().get(0).toString());
	    assertEquals("cc2@example.com", email.getCcAddresses().get(1).toString());
	    assertEquals("cc3@example.com", email.getCcAddresses().get(2).toString());
		
	}
	
	// Testing addBcc method on the size and instances
	@Test
	public void AddBccValidTest() throws EmailException {
		String[] arrBcc = new String[] {"bcc1@example.com", "bcc2@example.com", 
										"bcc3@example.com"};
		email.addBcc(arrBcc);

	    assertEquals(3, email.getBccAddresses().size());

	    assertEquals("bcc1@example.com", email.getBccAddresses().get(0).toString());
	    assertEquals("bcc2@example.com", email.getBccAddresses().get(1).toString());
	    assertEquals("bcc3@example.com", email.getBccAddresses().get(2).toString());
	}
	
	// Testing expected error for addBcc method with
	// null array parameter provided
	@Test(expected = EmailException.class)
	public void AddBccInvalidTest() throws EmailException {
		String[] arrBcc = new String[] {};
		email.addBcc(arrBcc);
	}
	
	// Testing addReplyTo method on the size and instances
	@Test
	public void AddReplyToTest() throws EmailException {
		email.addReplyTo("reply1@example.com");
		email.addReplyTo("reply2@example.com");
		email.addReplyTo("reply3@example.com");
	    assertEquals(3, email.getReplyToAddresses().size());

	    assertEquals("reply1@example.com", email.getReplyToAddresses().get(0).toString());
	    assertEquals("reply2@example.com", email.getReplyToAddresses().get(1).toString());
	    assertEquals("reply3@example.com", email.getReplyToAddresses().get(2).toString());
	}
	
	// Testing addHeader method on the size and instances
	@Test
	public void AddHeaderNotEmptyTest() {
		email.addHeader("mailer1", "priority1");
		email.addHeader("mailer2", "priority2");
		email.addHeader("mailer3", "priority3");
		assertEquals(3, email.headers.size());
		
		assertEquals("priority1", email.headers.get("mailer1"));
		assertEquals("priority2", email.headers.get("mailer2"));
		assertEquals("priority3", email.headers.get("mailer3"));
	}
	
	// Testing expected error for addHeader method with
	// null name parameter provided
	@Test(expected = IllegalArgumentException.class)
	public void AddHeaderEmptyNameTest() {
		email.addHeader("", "priority1");
	}
	
	// Testing expected error for addHeader method with
	// null value parameter provided
	@Test(expected = IllegalArgumentException.class)
	public void AddHeaderEmptyValueTest() {
		email.addHeader("mailer1", "");
	}
	
	// Testing getSentDate method on its functionality
	@SuppressWarnings({ "deprecation" })
	@Test
	public void GetSentDateTest() {
		Date sentDate = new Date();
		sentDate.setYear(2001);
		sentDate.setMonth(1);
		sentDate.setDate(5);
		email.setSentDate(sentDate);
		assertEquals(sentDate, email.getSentDate());
	}
	
	// Testing getSentDate method using object with null sentDate
	@Test
	public void GetSentDateNullTest() {
		Date sentDate = null;
		email.setSentDate(sentDate);
		assertEquals(new Date(), email.getSentDate());
	}
	
	// Testing setFrom method on its functionality
	@Test
	public void SetFromTest() throws EmailException {
		email.setFrom("reply1@example.com");
		assertEquals("reply1@example.com", email.fromAddress.toString());
	}
	
	// Testing getSocketConnectionTimeout method on its functionality
	@Test
	public void GetSocketConnectionTimeoutTest() {
		email.setSocketConnectionTimeout(5);
		assertEquals(5, email.getSocketConnectionTimeout());
	}
	
	// Testing expected error for getMailSession method using object
	// with null session and host name
	@Test(expected = EmailException.class)
	public void GetMailSessionNullEmptyHostTest() throws NamingException, EmailException {
		try {
			Session scn = null;
			email.setMailSession(scn);
		}
		catch(IllegalArgumentException e) {}
		assertEquals(null, email.getMailSession());
	}
	
	// Testing getMailSession method using object with
	// non-null session and host name
	@Test
	public void GetMailSessionTest() throws NamingException, EmailException {
		Properties prop = new Properties();
		prop.setProperty(EmailConstants.MAIL_SMTP_AUTH, "true");
		Session scn = Session.getInstance(prop);
		email.setMailSession(scn);
		assertEquals(scn, email.getMailSession());
	}
	
	// Testing getMailSession method using object with
	// null session but non-null host name
	@Test
	public void GetMailSessionNullTest() throws NamingException, EmailException, 
	IllegalArgumentException {

		try {
			Session scn = null;
			email.setMailSession(scn);
		}
		catch(IllegalArgumentException e) {
			email.setHostName("localhost");
			email.setAuthentication("example_username","example_password");
			email.setSSLOnConnect(true);
			email.setSSLCheckServerIdentity(true);
			email.setBounceAddress("abc@example.com");
			email.setStartTLSEnabled(true);
			email.setStartTLSRequired(true);
			assertNotNull(email.getMailSession());
		}
	}
	
	// Testing expected error for buildMimeMessage method
	// using object with non-null message
	@Test(expected = IllegalStateException.class)
	public void BuildMimeMessageNotNullMessageTest() throws EmailException, MessagingException,
	IllegalStateException {
		Properties prop = new Properties();
		prop.setProperty(EmailConstants.MAIL_HOST, "localhost");
		Session scn = Session.getInstance(prop);
		MimeMessage msg = new MimeMessage(scn);
		email.message = msg;
		email.buildMimeMessage();
	}
	
	// Testing buildMimeMessage method using object with non-null content
	@Test(expected = EmailException.class)
	public void BuildMimeMessageNotNullContentTest() throws EmailException, MessagingException {
		email.setHostName("localhost");
		email.setFrom("sender@example.com");
		email.addTo("receiver@example.com");
		email.addCc("cc1@example.com");
		email.addBcc("bcc1@example.com");
		email.addReplyTo("reply1@example.com");
		email.addHeader("mailer1", "priority1");
		email.setMsg(null);
		email.setSubject("subject");
		email.setCharset("US-ASCII");
		email.setContent(email, "content");
		email.setPopBeforeSmtp(true, "", "", "");
		email.buildMimeMessage();
		assertNotNull(email.message);

	}
	
	// Testing buildMimeMessage method using object with
	// non-null content and null charset
	@Test(expected = EmailException.class)
	public void BuildMimeMessageNullCharsetTest() throws EmailException, MessagingException {
		email.setHostName("localhost");
		email.setFrom("sender@example.com");
		email.addTo("receiver@example.com");
		email.addCc("cc1@example.com");
		email.addBcc("bcc1@example.com");
		email.addReplyTo("reply1@example.com");
		email.addHeader("mailer1", "priority1");
		email.setMsg(null);
		email.setSubject("subject");
		email.setContent(email, "content");
		email.setPopBeforeSmtp(true, "", "", "");
		email.buildMimeMessage();
		assertNotNull(email.message);

	}
	
	// Testing buildMimeMessage method using object with
	// non-null content and text plain type
	@Test(expected = EmailException.class)
	public void BuildMimeMessageNotNullContentTextTypeTest() throws EmailException,
	MessagingException {
		email.setHostName("localhost");
		email.setFrom("sender@example.com");
		email.addTo("receiver@example.com");
		email.addCc("cc1@example.com");
		email.addBcc("bcc1@example.com");
		email.addReplyTo("reply1@example.com");
		email.addHeader("mailer1", "priority1");
		email.setMsg(null);
		email.setContent("content", EmailConstants.TEXT_PLAIN);
		email.setPopBeforeSmtp(true, "", "", "");
		email.buildMimeMessage();
		assertNotNull(email.message);
	}
	
	// Testing buildMimeMessage method using object with
	// non-null email body
	@Test(expected = EmailException.class)
	public void BuildMimeMessageNotNullEmailBodyTest() throws EmailException, MessagingException {
		email.setHostName("localhost");
		email.setFrom("sender@example.com");
		email.addTo("receiver@example.com");
		email.addCc("cc1@example.com");
		email.addBcc("bcc1@example.com");
		email.addReplyTo("reply1@example.com");
		email.addHeader("mailer1", "priority1");
		email.setMsg(null);
		email.setSubject("subject");
		email.setCharset("US-ASCII");
		MimeMultipart body = new MimeMultipart();
		email.emailBody = body;
		email.setPopBeforeSmtp(true, "", "", "");
		email.buildMimeMessage();
		assertNotNull(email.message);

	}
	
	// Testing buildMimeMessage method using object with
	// non-null email body and content type
	@Test(expected = EmailException.class)
	public void BuildMimeMessageNotNullContentTypeTest() throws EmailException, MessagingException {
		email.setHostName("localhost");
		email.setFrom("sender@example.com");
		email.addTo("receiver@example.com");
		email.addCc("cc1@example.com");
		email.addBcc("bcc1@example.com");
		email.addReplyTo("reply1@example.com");
		email.addHeader("mailer1", "priority1");
		email.setMsg(null);
		email.setSubject("subject");
		email.setCharset("US-ASCII");
		MimeMultipart body = new MimeMultipart();
		email.emailBody = body;
		email.updateContentType("content-type");
		email.setPopBeforeSmtp(true, "", "", "");
		email.buildMimeMessage();
		assertNotNull(email.message);

	}
	

	// Testing buildMimeMessage method using object with
	// null email body and content
	@Test(expected = EmailException.class)
	public void BuildMimeMessagNullBodyContentTest() throws EmailException, MessagingException {
		email.setHostName("localhost");
		email.setFrom("sender@example.com");
		email.addTo("receiver@example.com");
		email.addCc("cc1@example.com");
		email.addBcc("bcc1@example.com");
		email.addReplyTo("reply1@example.com");
		email.addHeader("mailer1", "priority1");
		email.setMsg(null);
		email.setSubject("subject");
		email.setCharset("US-ASCII");
		email.setPopBeforeSmtp(true, "", "", "");
		email.buildMimeMessage();
		assertNotNull(email.message);

	}
	
	// Testing expected error for buildMimeMessage method
	// using object with non-null from address
	@Test(expected = EmailException.class)
	public void BuildMimeMessageNotNullFromAddressTest() throws EmailException, MessagingException {
		email.setHostName("localhost");
		email.addTo("receiver@example.com");
		email.addCc("cc1@example.com");
		email.addBcc("bcc1@example.com");
		email.addReplyTo("reply1@example.com");
		email.addHeader("mailer1", "priority1");
		email.setMsg(null);
		email.setSubject("subject");
		email.setCharset("US-ASCII");
		MimeMultipart body = new MimeMultipart();
		email.emailBody = body;
		email.setPopBeforeSmtp(true, "", "", "");
		email.buildMimeMessage();
		assertNotNull(email.message);
	}
	
	// Testing expected error for buildMimeMessage method
	// using object with no receiver
	@Test(expected = EmailException.class)
	public void BuildMimeMessageNoReceiverTest() throws EmailException, MessagingException {
		email.setHostName("localhost");
		email.setFrom("sender@example.com");
		email.setMsg(null);
		email.setSubject("subject");
		email.setCharset("US-ASCII");
		MimeMultipart body = new MimeMultipart();
		email.emailBody = body;
		email.buildMimeMessage();
		assertNotNull(email.message);
	}
	
	// Teardown
	@After
	public void TearDown() {
		email = null;
	}
	
}
