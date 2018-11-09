package com.tlabs.blockchain.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.tlabs.blockchain.model.Mail;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

/**
 * Created by admin on 2018/8/28.
 */
public class MailUtil {
    // 邮件发送协议
    private final static String PROTOCOL = "smtp";

    // SMTP邮件服务器
    private final static String HOST = "smtp.uugty.com";

    // SMTP邮件服务器默认端口
    private final static String PORT = "25";

    // 是否要求身份认证
    private final static String IS_AUTH = "true";

    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    private final static String IS_ENABLED_DEBUG_MOD = "true";

    // 发件人
    private static String from = "wsk@miaoa8.com";

    private static String password="Password01!!";
    // 收件人
    private static String to = "151719271@qq.com";
    //private static String to = "544913465@qq.com";

    // 初始化连接邮件服务器的会话信息
    private static Properties props = null;

    static {
        props = System.getProperties();
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.auth", IS_AUTH);
        props.setProperty("mail.debug",IS_ENABLED_DEBUG_MOD);
    }

    public static void main(String[] args) throws Exception {

        String  str = "测试";

      // 发送文本邮件
        asynchronousJavaMailSend(str,"d://111222.xlsx");//内容

  /*      Mail mail = new Mail();
        // 设置邮件服务器
        mail.setHost("smtp.miaoa8.com");
        // 发件人邮件地址
        mail.setSender("wsk@miao8.com");
        // 发件人名称
        mail.setName("Java.小学生");
        // 登录账号,一般都是和邮箱名一样吧
        mail.setUsername("wsk@miao8.com");
        // 发件人邮箱的登录密码
        mail.setPassword("Password");
        // 接收人
        mail.setReceiver("876791562@qq.com");
        mail.setReceiverName("我要女票");
        mail.setSubject("万里长征最后一步测试");
        String html = "<!DOCTYPE html>";
        html += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
        html += "<title>Insert title here</title>";
        html += "</head><body>";
        html += "<div style=\"width:600px;height:400px;margin:50px auto;\">";
        html += "<h1>我来看看邮件是否发送成功呢</h1><br/><br/>";
        html += "<p>下面是通过该协议可以创建一个指向电子邮件地址的超级链接，通过该链接可以在Internet中发送电子邮件</p><br/>";
        html += "<a href=\"mailto:huntereagle@foxmail.com?subject=test&cc=huntertochen@163.com&body=use mailto sample\">send mail</a>";
        html += "</div>";
        html += "</body></html>";
        mail.setMessage(html);

        new MailUtil().send(mail);*/



    }

    public Boolean send(Mail mail){
        HtmlEmail email = new HtmlEmail();
        try {
            // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
            email.setHostName(mail.getHost());
            // 字符编码集的设置
            email.setCharset(Mail.ENCODEING);
            // 发送人的邮箱
            email.setFrom(mail.getSender(), mail.getName());
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
            email.setAuthentication(mail.getUsername(), mail.getPassword());

            // 设置收件人信息
            setTo(email, mail);
            // 设置抄送人信息
            setCc(email, mail);
            // 设置密送人信息
            setBcc(email, mail);
            // 要发送的邮件主题
            email.setSubject(mail.getSubject());
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
            email.setHtmlMsg(mail.getMessage());
            // 发送
            email.send();
         /*   if (Log.isDebugEnabled()) {
                Log.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver());
            }*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
          /*  Log.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver() + " 失败");*/
            return false;
        }
    }

    /**
     * 设置收件人信息
     *
     * @param email
     * @param mail
     * @throws EmailException
     */
    private void setTo(HtmlEmail email, Mail mail) throws EmailException {
        // 收件人不为空
        if (StringUtils.isNotEmpty(mail.getReceiver())) {
            // 收件人名称不为空
            if (StringUtils.isNotEmpty(mail.getReceiverName())) {
                email.addTo(mail.getReceiver(), mail.getReceiverName());
            } else {
                email.addTo(mail.getReceiver());
            }
        }
        // 收件人 Map 不为空
        if (mail.getTo() != null) {
            for (Map.Entry<String, String> entry : mail.getTo().entrySet()) {
                // 收件人名称不为空
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    email.addTo(entry.getKey(), entry.getValue());
                } else {
                    email.addTo(entry.getKey());
                }
            }
        }
    }

    /**
     * 设置抄送人信息
     *
     * @param email
     * @param mail
     * @throws EmailException
     */
    private void setCc(HtmlEmail email, Mail mail) throws EmailException{
        // 抄送人 Map 不为空
        if (mail.getCc() != null) {
            for (Map.Entry<String, String> entry : mail.getCc().entrySet()) {
                // 抄送人名称不为空
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    email.addCc(entry.getKey(), entry.getValue());
                } else {
                    email.addCc(entry.getKey());
                }
            }
        }
    }

    /**
     * 设置密送人信息
     *
     * @param email
     * @param mail
     * @throws EmailException
     */
    private void setBcc(HtmlEmail email, Mail mail) throws EmailException{
        // 密送人 Map 不为空
        if (mail.getBcc() != null) {
            for (Map.Entry<String, String> entry : mail.getBcc().entrySet()) {
                // 密送人名称不为空
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    email.addBcc(entry.getKey(), entry.getValue());
                } else {
                    email.addBcc(entry.getKey());
                }
            }
        }
    }



    /**
     * 发送简单的文本邮件
     */
    public static void sendTextEmail(String content,String fileName) throws Exception {
        // 创建Session实例对象
        Session session = Session.getDefaultInstance(props);

        // 创建MimeMessage实例对象
        //MimeMessage message = new MimeMessage(session);
        MimeMessage message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(from));
        // 设置邮件主题
        message.setSubject("渠道统计邮件_市场");//邮件标题
        // 设置收件人
        //String toList="468106998@qq.com,w_ruobing@126.com,344089249@qq.com,664259993@qq.com,287446175@qq.com";
        String toList=to;
        InternetAddress[] iaToList = new InternetAddress().parse(toList);

        //================附件
        Multipart multipart = new MimeMultipart();
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setHeader("Content-Type", "text/html; charset=GBK");
        multipart.addBodyPart(contentPart);
        contentPart.setText(content);
        File usFile = new File(fileName);
        MimeBodyPart fileBody = new MimeBodyPart();
        DataSource source = new FileDataSource(fileName);
        fileBody.setDataHandler(new DataHandler(source));
        sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
        fileBody.setFileName("=?GBK?B?"
                + enc.encode(usFile.getName().getBytes()) + "?=");
        multipart.addBodyPart(fileBody);


        message.setRecipients(RecipientType.TO,iaToList); //收件人
        //message.setRecipient(RecipientType.TO,  new InternetAddress(to));
        // 设置发送时间
        //message.setSentDate(new Date());
        // 设置纯文本内容为邮件正文
        // message.setText(content);
        // 保存并生成最终的邮件内容
        message.setContent(multipart);
        message.setSentDate(new Date());
        message.saveChanges();

        // 获得Transport实例对象
        Transport transport = session.getTransport();
        // 打开连接
        transport.connect(from, password);
        // 将message对象传递给transport对象，将邮件发送出去
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }

    /**
     *
     * @param content
     *            邮件内容
     * @param sendContentType
     *            sendContentType=0简单文本 sendContentType=1 超链接
     * @param subject
     *            标题
     * @param to
     *            收件人 多个用,号隔开
     * @throws Exception
     */
    public static boolean sendTextEmail(String content, int sendContentType, String subject, String to) {
        try {
            long startTime = System.currentTimeMillis();
            // 创建Session实例对象
            Session session = Session.getDefaultInstance(props);

            // 创建MimeMessage实例对象
            MimeMessage message = new MimeMessage(session);
            // 设置发件人
            // message.setFrom(new InternetAddress(from));
            // 设置自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText("Dididu");
            } catch (UnsupportedEncodingException e) {
                System.err.println("83 sendTextEmail UnsupportedEncodingException=" + e + "   收件人to=" + to);
                return false;
            }
            message.setFrom(new InternetAddress(nick + " <" + from + ">"));
            // 设置邮件主题
            message.setSubject(subject);
            // 设置收件人
            String toList = to;
            // String
            InternetAddress[] iaToList = new InternetAddress().parse(toList);

            message.setRecipients(RecipientType.TO, iaToList); // 收件人
            // message.setRecipient(RecipientType.TO, new InternetAddress(to));
            // 设置发送时间
            message.setSentDate(new Date());
            // 设置纯文本内容为邮件正文
            if (sendContentType == 0) {
                message.setText(content);
            } else {
                message.setContent(content, "text/html;charset=utf-8");
            }
            // 保存并生成最终的邮件内容
            message.saveChanges();

            // 获得Transport实例对象
            Transport transport = session.getTransport();
            // 打开连接
            transport.connect(from, password);
            // 将message对象传递给transport对象，将邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            // 关闭连接
            transport.close();
            System.err.println("115 sendTextEmail 邮件发送成功, 收件人=" + to + " 内容: " + content);
            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
            System.out.println(Float.toString(seconds) + " seconds.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("118 sendTextEmail 邮件发送失败" + " 收件人=" + to);
            return false;
        }

    }


    public static void asynchronousJavaMailSend(final String content,final String fileName){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    sendTextEmail (content,fileName);
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
