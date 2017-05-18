package com.wenka.domain.service;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Date;
import java.util.Properties;

/**
 * 邮件服务类
 * <p>
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/18.
 */
public class MailService {

    @Test
    public void send(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(587);
        mailSender.setUsername("1149412873");
//        mailSender.setPassword("Wk6986..");
        mailSender.setPassword("mxdnvexoolcvihcc");
        Properties pt = new Properties();
        pt.setProperty("mail.smtp.auth", "true");
        mailSender.setJavaMailProperties(pt);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("1149412873@qq.com");//发件地址
        msg.setTo("15738826986@163.com");//收件地址
        msg.setSubject("这应该是标题");//标题
        msg.setText("这是主体内容");//内容
        msg.setSentDate(new Date());
        mailSender.send(msg);//发送邮件
    }
}
