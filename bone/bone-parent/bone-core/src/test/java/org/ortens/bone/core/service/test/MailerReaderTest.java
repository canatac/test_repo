/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.service.test;

import java.net.ProxySelector;
import java.net.URI;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.ortens.bone.core.service.MailReader;

/**
 * 
 * @author canatac
 */
public class MailerReaderTest {

//	@Test
//	public void testProxy(){
//		ProxySelector.getDefault().select(new URI(urlS)) ;
//	}
	
	//@Test
	public void testConnect() {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("socksProxyHost", "sesame.efs.sante.fr");
		props.setProperty("socksProxyPort", "8080");
		props.setProperty("http.proxyHost", "sesame.efs.sante.fr");
		props.setProperty("http.proxyPort", "8080");
		props.setProperty("http.proxyUser", "can.atac.externe");
		props.setProperty("http.proxyPassword", "741soleil!");
		try {
			
			Session session = Session.getInstance(props,new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication("can.atac@ortens.com", "Can01!");
	            }
	    });
			
			//Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("ssl0.ovh.net", "can.atac@ortens.com",
					"Can01!");
			System.out.println("YIPPEEE !!");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

//	@Test
	public void testMailRead() {
		System.getProperties().put("http.proxyHost", "sesame.efs.sante.fr");
		System.getProperties().put("http.proxyPort", "8080");
		System.getProperties().put("http.proxyUser", "can.atac.externe");
		System.getProperties().put("http.proxyPassword", "741soleil!");
//		System.setProperty("socksProxyHost", "sesame.efs.sante.fr");
//		System.setProperty("socksProxyPort", "8080");
		new MailReader();
	}
	
	//@Test
	public void anotherTest() throws MessagingException{
		// Create session
	    
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp"); props.put("mail.smtp.starttls.enable", "true"); 
		props.put("mail.smtp.host", "smtp.gmail.com"); 
		props.put("mail.smtp.auth", "true"); 
		// use your gmail account username here
		props.put("mail.smtp.user", "can.atac@gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.mime.charset", "ISO-8859-1");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session = 
		   Session.getDefaultInstance(props, new PasswordAuthenticator());
		    
		// Create message
		    
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("can.atac@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, 
		   InternetAddress.parse("can.atac@gmail.com", false));
		message.setSubject("TEST");
		message.setText("coucou");

		// Send the message

		Transport.send(message); 

	}
	
	class PasswordAuthenticator extends javax.mail.Authenticator {
	    public PasswordAuthentication getPasswordAuthentication() {
//			String user = "can.atac@gmail.com";
//			String pwd = "19tdskwtre71";
	    	String user = "can.atac@ortens.com";
			String pwd = "Can01!";
			return new PasswordAuthentication(user, pwd);
	    }
	}
	
	@Test
	public void testEncore() throws AddressException, MessagingException{
		Properties props = new Properties();
		props.put("mail.host", "ns0.ovh.net");
		props.put("mail.smtp.port", "587");
		Session mailConnection = Session.getInstance(props, new PasswordAuthenticator());
		
		Message message = new MimeMessage(mailConnection);
		message.setFrom(new InternetAddress("can.atac@ortens.com"));
		message.setRecipients(Message.RecipientType.TO, 
		   InternetAddress.parse("can.atac@gmail.com", false));
		message.setSubject("TEST");
		message.setText("coucou");

		// Send the message

		Transport.send(message);
		
	}
}