package com.wenka.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    /**
     * 获取消息发送对象
     *
     * @return
     */
//    @Bean
//    private JavaMailSenderImpl createMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(mailHost);
//        mailSender.setPort(Integer.valueOf(mailPort));
//        mailSender.setUsername(mailUsername);
//        mailSender.setPassword(mailPassword);
//        Properties pt = new Properties();
//        pt.setProperty("mail.smtp.auth", "true");
//        mailSender.setJavaMailProperties(pt);
//        return mailSender;
//    }

    /**
     * 获取邮件
     *
     * @param to      收件地址
     * @param subject 标题
     * @param text    内容
     */
//    @Transactional
    public void sendMail(String to, String subject, String text) {
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(Integer.valueOf(mailPort));
        javaMailSender.setUsername(mailUsername);
        javaMailSender.setPassword(mailPassword);
        Properties pt = new Properties();
        pt.setProperty("mail.smtp.auth", "true");
        javaMailSender.setJavaMailProperties(pt);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailSendder);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setSentDate(new Date());
        javaMailSender.send(msg);
    }
}
