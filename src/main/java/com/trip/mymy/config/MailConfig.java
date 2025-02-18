package com.trip.mymy.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@PropertySource("classpath:application.properties") 
public class MailConfig {
	
    @Value("${email.sender}")
    private String emailSender;

    @Value("${email.password}")
    private String emailPassword;
	
	@Bean
    public JavaMailSender mailSender() {
	    JavaMailSenderImpl jms = new JavaMailSenderImpl();
	    jms.setHost("smtp.gmail.com");//google smtp 서버 설정
	    jms.setPort(587);//google smtp 메일 서버 포트
	    jms.setUsername(emailSender);//보내는 계정
	    jms.setPassword(emailPassword);
	
	    //메일 전달 프로토콜 세부 설정
	    Properties prop = new Properties();
	    prop.setProperty("mail.transport.protocol", "smtp");
		//true면 사용자 인증을 시도한다.
	    prop.setProperty("mail.smtp.auth", "true"); 
		//tls 방식으로 처리되면 명시적 보안처리이다. 암시적은ssl이다
	    prop.setProperty("mail.smtp.starttls.enable", "true");
	    jms.setJavaMailProperties(prop);

    return jms;
    }
}
