package user.mngm.usermanagement.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMail {

    @Autowired
    JavaMailSender mailSender;

    private String authCode; // 인증번호

    // 메일 내용 작성
    public MimeMessage createMessage(String to, String type) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.addRecipients(RecipientType.TO, to);// 보내는 대상
            message.setSubject(type + " 이메일 인증번호입니다.");// 제목

            String msgg = "";
            msgg += "<div style='margin:100px;'>";
            msgg += "<h1> 안녕하세요</h1>";
            msgg += "<br>";
            msgg += "<p>아래 코드를 인증번호 입력창으로 돌아가 입력해주세요<p>";
            msgg += "<br>";
            msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            msgg += "<br>";
            msgg += "<br>";
            msgg += "<div style='font-size:130%'>";
            msgg += "CODE : <strong>";
            msgg += authCode + "</strong><div><br/> "; // 메일에 인증번호 넣기
            msgg += "</div>";
            message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
            // 보내는 사람의 이메일 주소, 보내는 사람 이름
            message.setFrom(new InternetAddress("sg_7840@naver.com", "admin"));// 보내는 사람

            return message;
        } catch (Exception e) {
            throw e;
        }
    }

    // 랜덤 인증 코드 생성 후 리턴
    public String createKey() throws Exception {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        try {
            for (int i = 0; i < 8; i++) { // 인증코드 8자리
                int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨
                switch (index) {
                    case 0:
                        key.append((char) ((int) (rnd.nextInt(26)) + 97));
                        // a~z (ex. 1+97=98 => (char)98 = 'b')
                        break;
                    case 1:
                        key.append((char) ((int) (rnd.nextInt(26)) + 65));
                        // A~Z
                        break;
                    case 2:
                        key.append((rnd.nextInt(10)));
                        // 0~9
                        break;
                }
            }
            return key.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    // 메일 발송
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다.
    public String sendSimpleMessage(String to, String type) throws Exception {
        authCode = createKey(); // 랜덤 인증번호 생성
        MimeMessage message = createMessage(to, type); // 메일 발송

        try {
            mailSender.send(message);
            return authCode; // 메일로 보냈던 인증 코드를 서버로 반환
        } catch (MailException es) {
            throw es;
        }
    }
}
