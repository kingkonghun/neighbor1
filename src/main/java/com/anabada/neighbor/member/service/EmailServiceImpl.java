package com.anabada.neighbor.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;


@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService{
    
    private final JavaMailSender javaMailSender;

    public static final String ePw = createKey();

    private MimeMessage createMessage(String to) throws Exception{//메시지만들기
        System.out.println("받는사람:"+to);
        System.out.println("인증번호"+ePw);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO,to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msg="";
        msg+= "<div style='margin:20px;'>";
        msg+= "<h1> 안녕하세요 아나바다입니다. </h1>";
        msg+= "<br>";
        msg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msg+= "<br>";
        msg+= "<p>감사합니다.<p>";
        msg+= "<br>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "CODE : <strong>";
        msg+= ePw+"</strong><div><br/> ";
        msg+= "</div>";
        message.setText(msg,"utf-8","html");//내용
        message.setFrom(new InternetAddress("wbg99030281@gmail.com", "woo"));

        return message;
    }
    private static String createKey() {//암호키만들기
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for(int i = 0; i < 8;i++){ //인증번호 8자리
            int index = rnd.nextInt(3); //0~2 까지 랜덤

            switch (index){
                case 0:
                    key.append((char) ((int)(rnd.nextInt(26))+97));
                    //a-z (ex 1+97=98 => (char)98 = 'b'
                    break;
                case 1:
                    key.append((char) ((int)(rnd.nextInt(26))+65));
                    //A-Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    //0-9
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {//메시지보내기
        System.out.println(123);
        MimeMessage message = createMessage(to);
        System.out.println(to);
        try {
            javaMailSender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
}
