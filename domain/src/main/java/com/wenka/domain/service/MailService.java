package com.wenka.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Properties;

/**
 * 邮件服务类
 * <p>
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/18.
 */
@Service
public class MailService {

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.port}")
    private String mailPort;

    @Value("${mail.username}")
    private String mailUsername;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${mail.sendder}")
    private String mailSendder;

    /**
     * 获取消息发送对象
     *
     * @return
     */
    @Transactional
    private JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(Integer.valueOf(mailPort));
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        Properties pt = new Properties();
        pt.setProperty("mail.smtp.auth", "true");
        mailSender.setJavaMailProperties(pt);
        return mailSender;
    }

    /**
     * 获取邮件
     *
     * @param to      收件地址
     * @param subject 标题
     * @param text    内容
     */
    @Transactional
    public void sendMail(String to, String subject, String text) {
        JavaMailSenderImpl mailSender = this.createMailSender();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailSendder);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setSentDate(new Date());
        mailSender.send(msg);
    }
}
