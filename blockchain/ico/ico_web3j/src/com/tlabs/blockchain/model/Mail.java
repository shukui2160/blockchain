package com.tlabs.blockchain.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by admin on 2018/8/28.
 */
public class Mail implements Serializable {

    private static final long serialVersionUID = -6390720891150157552L;
    public static final String ENCODEING = "UTF-8";

    // ��������ַ
    private String host;
    // �����˵�����
    private String sender;
    // �������ǳ�
    private String name;
    // �˺�
    private String username;
    // ����
    private String password;
    // �ռ��˵�����
    private String receiver;
    // �ռ��˵�����
    private String receiverName;
    // �ռ��˵�����(key)������(value)
    private Map<String, String> to;
    // �����˵�����(key)������(value)
    private Map<String, String> cc;
    // ���ܳ����˵�����(key)������(value)
    private Map<String, String> bcc;
    // ����
    private String subject;
    // ��Ϣ(֧��HTML)
    private String message;

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public Map<String, String> getTo() {
        return to;
    }
    public void setTo(Map<String, String> to) {
        this.to = to;
    }
    public Map<String, String> getCc() {
        return cc;
    }
    public void setCc(Map<String, String> cc) {
        this.cc = cc;
    }
    public Map<String, String> getBcc() {
        return bcc;
    }
    public void setBcc(Map<String, String> bcc) {
        this.bcc = bcc;
    }
}
