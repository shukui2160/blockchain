package com.common.tookit.email;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	private String smtpHost;

	private String smtpPort;

	private String mailUser; // 发件人账号

	private String mailUserNick; // 发件人别名

	private String mailPassword; // 访问SMTP服务时需要提供的密码

	private boolean supportSSL; // 是否支持SSL

	private Properties mailEnvProps; // 邮件发送环境属性

	private Authenticator authenticator;

	public MailSender(String smtpHost, String smtpPort, String mailUser, String mailUserNick, String mailPassword) {
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.mailUser = mailUser;
		this.mailUserNick = mailUserNick;
		this.mailPassword = mailPassword;

		init();
	}

	private void init() {
		// set mailEnvProps
		mailEnvProps = new Properties();
		mailEnvProps.put("mail.smtp.auth", "true");
		mailEnvProps.put("mail.smtp.host", smtpHost);
		mailEnvProps.put("mail.stmp.port", smtpPort);

		if (supportSSL) {
			mailEnvProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			mailEnvProps.put("mail.smtp.socketFactory.port", smtpPort);
		}

		mailEnvProps.put("mail.user.nick", mailUserNick);
		mailEnvProps.put("mail.user", mailUser);
		mailEnvProps.put("mail.password", mailPassword);

		// set authenticator
		authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				String userName = mailEnvProps.getProperty("mail.user");
				String password = mailEnvProps.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
	}

	/**
	 *
	 * @param toEmail
	 *            收件人邮箱
	 * @param content
	 *            邮件内容
	 * @param subject
	 *            邮件主题
	 */
	public void sendHtmlMail(String toEmail, String content, String subject) {

		try {
			Session session = Session.getInstance(mailEnvProps, authenticator);

			String mailFrom = (String) mailEnvProps.get("mail.user");
			String password = (String) mailEnvProps.get("mail.password");

			MimeMessage message = new MimeMessage(session);

			// 设置自定义发件人昵称
			String nick = (String) mailEnvProps.get("mail.user.nick");

			message.setFrom(new InternetAddress(nick + " <" + mailFrom + ">"));

			message.setSubject(subject);
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toEmail));
			message.setSentDate(new Date());

			message.setContent(content, "text/html;charset=UTF-8");
			message.saveChanges(); // 保存并生成最终的邮件内容

			Transport transport = session.getTransport("smtp");
			transport.connect(mailFrom, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    public static void main (String [] args) {
        String smtpHost = "smtpdm.aliyun.com";
        String smtpPort = "25";
        String mailUser = "noreply@mail.luffysinx.com";           //发件人账号
        String mailUserNick = "Tlabs";       //发件人别名
        String mailPassword = "1q2w3e4r5t6y2A4D";       //访问SMTP服务时需要提供的密码
        MailSender sender = new MailSender(smtpHost, smtpPort, mailUser, mailUserNick, mailPassword);
        //简单html 邮件
        sender.sendHtmlMail("1296791964@qq.com",
                "1296791964@qq.com, 您好!<br/>您的验证码为：<b>**12344**</b><br/>该验证码10分钟内有效.<br/>感谢您对Tlabs的支持。",
                "Tlabs 验证码提醒" );
    }

}
