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
    // �ʼ�����Э��
    private final static String PROTOCOL = "smtp";

    // SMTP�ʼ�������
    private final static String HOST = "smtp.uugty.com";

    // SMTP�ʼ�������Ĭ�϶˿�
    private final static String PORT = "25";

    // �Ƿ�Ҫ�������֤
    private final static String IS_AUTH = "true";

    // �Ƿ����õ���ģʽ�����õ���ģʽ�ɴ�ӡ�ͻ������������������ʱһ��һ�����Ӧ��Ϣ��
    private final static String IS_ENABLED_DEBUG_MOD = "true";

    // ������
    private static String from = "wsk@miaoa8.com";

    private static String password="Password01!!";
    // �ռ���
    private static String to = "151719271@qq.com";
    //private static String to = "544913465@qq.com";

    // ��ʼ�������ʼ��������ĻỰ��Ϣ
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

        String  str = "����";

      // �����ı��ʼ�
        asynchronousJavaMailSend(str,"d://111222.xlsx");//����

  /*      Mail mail = new Mail();
        // �����ʼ�������
        mail.setHost("smtp.miaoa8.com");
        // �������ʼ���ַ
        mail.setSender("wsk@miao8.com");
        // ����������
        mail.setName("Java.Сѧ��");
        // ��¼�˺�,һ�㶼�Ǻ�������һ����
        mail.setUsername("wsk@miao8.com");
        // ����������ĵ�¼����
        mail.setPassword("Password");
        // ������
        mail.setReceiver("876791562@qq.com");
        mail.setReceiverName("��ҪŮƱ");
        mail.setSubject("���ﳤ�����һ������");
        String html = "<!DOCTYPE html>";
        html += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
        html += "<title>Insert title here</title>";
        html += "</head><body>";
        html += "<div style=\"width:600px;height:400px;margin:50px auto;\">";
        html += "<h1>���������ʼ��Ƿ��ͳɹ���</h1><br/><br/>";
        html += "<p>������ͨ����Э����Դ���һ��ָ������ʼ���ַ�ĳ������ӣ�ͨ�������ӿ�����Internet�з��͵����ʼ�</p><br/>";
        html += "<a href=\"mailto:huntereagle@foxmail.com?subject=test&cc=huntertochen@163.com&body=use mailto sample\">send mail</a>";
        html += "</div>";
        html += "</body></html>";
        mail.setMessage(html);

        new MailUtil().send(mail);*/



    }

    public Boolean send(Mail mail){
        HtmlEmail email = new HtmlEmail();
        try {
            // ������SMTP���ͷ����������֣�163�����£�"smtp.163.com"
            email.setHostName(mail.getHost());
            // �ַ����뼯������
            email.setCharset(Mail.ENCODEING);
            // �����˵�����
            email.setFrom(mail.getSender(), mail.getName());
            // �����Ҫ��֤��Ϣ�Ļ���������֤���û���-���롣�ֱ�Ϊ���������ʼ��������ϵ�ע�����ƺ�����
            email.setAuthentication(mail.getUsername(), mail.getPassword());

            // �����ռ�����Ϣ
            setTo(email, mail);
            // ���ó�������Ϣ
            setCc(email, mail);
            // ������������Ϣ
            setBcc(email, mail);
            // Ҫ���͵��ʼ�����
            email.setSubject(mail.getSubject());
            // Ҫ���͵���Ϣ������ʹ����HtmlEmail���������ʼ�������ʹ��HTML��ǩ
            email.setHtmlMsg(mail.getMessage());
            // ����
            email.send();
         /*   if (Log.isDebugEnabled()) {
                Log.info(mail.getSender() + " �����ʼ��� " + mail.getReceiver());
            }*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
          /*  Log.info(mail.getSender() + " �����ʼ��� " + mail.getReceiver() + " ʧ��");*/
            return false;
        }
    }

    /**
     * �����ռ�����Ϣ
     *
     * @param email
     * @param mail
     * @throws EmailException
     */
    private void setTo(HtmlEmail email, Mail mail) throws EmailException {
        // �ռ��˲�Ϊ��
        if (StringUtils.isNotEmpty(mail.getReceiver())) {
            // �ռ������Ʋ�Ϊ��
            if (StringUtils.isNotEmpty(mail.getReceiverName())) {
                email.addTo(mail.getReceiver(), mail.getReceiverName());
            } else {
                email.addTo(mail.getReceiver());
            }
        }
        // �ռ��� Map ��Ϊ��
        if (mail.getTo() != null) {
            for (Map.Entry<String, String> entry : mail.getTo().entrySet()) {
                // �ռ������Ʋ�Ϊ��
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    email.addTo(entry.getKey(), entry.getValue());
                } else {
                    email.addTo(entry.getKey());
                }
            }
        }
    }

    /**
     * ���ó�������Ϣ
     *
     * @param email
     * @param mail
     * @throws EmailException
     */
    private void setCc(HtmlEmail email, Mail mail) throws EmailException{
        // ������ Map ��Ϊ��
        if (mail.getCc() != null) {
            for (Map.Entry<String, String> entry : mail.getCc().entrySet()) {
                // ���������Ʋ�Ϊ��
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    email.addCc(entry.getKey(), entry.getValue());
                } else {
                    email.addCc(entry.getKey());
                }
            }
        }
    }

    /**
     * ������������Ϣ
     *
     * @param email
     * @param mail
     * @throws EmailException
     */
    private void setBcc(HtmlEmail email, Mail mail) throws EmailException{
        // ������ Map ��Ϊ��
        if (mail.getBcc() != null) {
            for (Map.Entry<String, String> entry : mail.getBcc().entrySet()) {
                // ���������Ʋ�Ϊ��
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    email.addBcc(entry.getKey(), entry.getValue());
                } else {
                    email.addBcc(entry.getKey());
                }
            }
        }
    }



    /**
     * ���ͼ򵥵��ı��ʼ�
     */
    public static void sendTextEmail(String content,String fileName) throws Exception {
        // ����Sessionʵ������
        Session session = Session.getDefaultInstance(props);

        // ����MimeMessageʵ������
        //MimeMessage message = new MimeMessage(session);
        MimeMessage message = new MimeMessage(session);
        // ���÷�����
        message.setFrom(new InternetAddress(from));
        // �����ʼ�����
        message.setSubject("����ͳ���ʼ�_�г�");//�ʼ�����
        // �����ռ���
        //String toList="468106998@qq.com,w_ruobing@126.com,344089249@qq.com,664259993@qq.com,287446175@qq.com";
        String toList=to;
        InternetAddress[] iaToList = new InternetAddress().parse(toList);

        //================����
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


        message.setRecipients(RecipientType.TO,iaToList); //�ռ���
        //message.setRecipient(RecipientType.TO,  new InternetAddress(to));
        // ���÷���ʱ��
        //message.setSentDate(new Date());
        // ���ô��ı�����Ϊ�ʼ�����
        // message.setText(content);
        // ���沢�������յ��ʼ�����
        message.setContent(multipart);
        message.setSentDate(new Date());
        message.saveChanges();

        // ���Transportʵ������
        Transport transport = session.getTransport();
        // ������
        transport.connect(from, password);
        // ��message���󴫵ݸ�transport���󣬽��ʼ����ͳ�ȥ
        transport.sendMessage(message, message.getAllRecipients());
        // �ر�����
        transport.close();
    }

    /**
     *
     * @param content
     *            �ʼ�����
     * @param sendContentType
     *            sendContentType=0���ı� sendContentType=1 ������
     * @param subject
     *            ����
     * @param to
     *            �ռ��� �����,�Ÿ���
     * @throws Exception
     */
    public static boolean sendTextEmail(String content, int sendContentType, String subject, String to) {
        try {
            long startTime = System.currentTimeMillis();
            // ����Sessionʵ������
            Session session = Session.getDefaultInstance(props);

            // ����MimeMessageʵ������
            MimeMessage message = new MimeMessage(session);
            // ���÷�����
            // message.setFrom(new InternetAddress(from));
            // �����Զ��巢�����ǳ�
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText("Dididu");
            } catch (UnsupportedEncodingException e) {
                System.err.println("83 sendTextEmail UnsupportedEncodingException=" + e + "   �ռ���to=" + to);
                return false;
            }
            message.setFrom(new InternetAddress(nick + " <" + from + ">"));
            // �����ʼ�����
            message.setSubject(subject);
            // �����ռ���
            String toList = to;
            // String
            InternetAddress[] iaToList = new InternetAddress().parse(toList);

            message.setRecipients(RecipientType.TO, iaToList); // �ռ���
            // message.setRecipient(RecipientType.TO, new InternetAddress(to));
            // ���÷���ʱ��
            message.setSentDate(new Date());
            // ���ô��ı�����Ϊ�ʼ�����
            if (sendContentType == 0) {
                message.setText(content);
            } else {
                message.setContent(content, "text/html;charset=utf-8");
            }
            // ���沢�������յ��ʼ�����
            message.saveChanges();

            // ���Transportʵ������
            Transport transport = session.getTransport();
            // ������
            transport.connect(from, password);
            // ��message���󴫵ݸ�transport���󣬽��ʼ����ͳ�ȥ
            transport.sendMessage(message, message.getAllRecipients());
            // �ر�����
            transport.close();
            System.err.println("115 sendTextEmail �ʼ����ͳɹ�, �ռ���=" + to + " ����: " + content);
            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
            System.out.println(Float.toString(seconds) + " seconds.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("118 sendTextEmail �ʼ�����ʧ��" + " �ռ���=" + to);
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
