package com.common.tookit.email;

public class EmailTest {
	public static void main(String[] args) {

		String smtpHost = "smtpdm.aliyun.com";
		String smtpPort = "25";
		String mailUser = "noreply@mail.luffysinx.com"; // 发件人账号
		String mailUserNick = "Tlabs"; // 发件人别名
		String mailPassword = "1q2w3e4r5t6y2A4D"; // 访问SMTP服务时需要提供的密码
		MailSender sender = new MailSender(smtpHost, smtpPort, mailUser, mailUserNick, mailPassword);
		// 简单html 邮件
		sender.sendHtmlMail("13526653981@163.com",
				"1296791964@qq.com, 您好!<br/>您的验证码为：<b>**12344**</b><br/>该验证码10分钟内有效.<br/>感谢您对Tlabs的支持。", "Tlabs 验证码提醒");
	}
}
