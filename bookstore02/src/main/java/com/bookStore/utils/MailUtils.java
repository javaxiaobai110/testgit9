package com.bookStore.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.sun.mail.util.MailSSLSocketFactory;


/**
 * 发送邮件的工具类
 */
public class MailUtils {

	public static void sendMail(String email, String emailMsg)
			throws AddressException, MessagingException, Exception {
		// 1.创建一个程序与邮件服务器会话对象 Session

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", "smtp.qq.com");
		props.setProperty("mail.smtp.auth", "true");// 指定验证为true
		
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
	    sf.setTrustAllHosts(true);
	    props.put("mail.smtp.ssl.enable", "true");
	    props.put("mail.smtp.ssl.socketFactory", sf);


		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("1667851989", "ysbovvbgnitseaef");
			}
		};

		Session session = Session.getInstance(props, auth);

		// 2.创建一个Message，它相当于是邮件内容
		Message message = new MimeMessage(session);

		message.setFrom(new InternetAddress("1667851989@qq.com")); // 设置发送者

		message.setRecipient(RecipientType.TO, new InternetAddress(email)); // 设置发送方式与接收者

		message.setSubject("用户激活");
		// message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

		message.setContent(emailMsg, "text/html;charset=utf-8");

		// 3.创建 Transport用于将邮件发送
		Transport transport = session.getTransport();
		transport.connect("smtp.qq.com", "1667851989@qq.com", "ysbovvbgnitseaef");

		Transport.send(message);
		
		transport.close();
	}
}















/*import java.util.Date;
import java.util.Properties;
 
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
import com.sun.mail.util.MailSSLSocketFactory;
 
public class MailUtils {
 
 
  public static void sendMail(String toEmailAddress,String emailTitle,String emailContent)throws Exception{
    Properties props = new Properties();
 
    // 开启debug调试
    props.setProperty("mail.debug", "true");
    // 发送服务器需要身份验证
    props.setProperty("mail.smtp.auth", "true");
    // 设置邮件服务器主机名
    props.setProperty("mail.host", "smtp.qq.com");
    // 发送邮件协议名称
    props.setProperty("mail.transport.protocol", "smtp");
 
    *//**SSL认证，注意腾讯邮箱是基于SSL加密的，所有需要开启才可以使用**//*
    MailSSLSocketFactory sf = new MailSSLSocketFactory();
    sf.setTrustAllHosts(true);
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.smtp.ssl.socketFactory", sf);
 
    //创建会话
    Session session = Session.getInstance(props);
 
    //发送的消息，基于观察者模式进行设计的
    Message msg = new MimeMessage(session);
    msg.setSubject(emailTitle);
    //使用StringBuilder，因为StringBuilder加载速度会比String快，而且线程安全性也不错
    StringBuilder builder = new StringBuilder();
    builder.append("\n"+emailContent);
    builder.append("\n时间 " + new Date());
    msg.setText(builder.toString());
    msg.setFrom(new InternetAddress("1667851989@qq.com"));
 
    Transport transport = session.getTransport();
    transport.connect("smtp.qq.com", "1667851989@qq.com", "ysbovvbgnitseaef");
    //发送消息
    transport.sendMessage(msg, new Address[] { new InternetAddress(toEmailAddress) });
    transport.close();
  }
}
*/