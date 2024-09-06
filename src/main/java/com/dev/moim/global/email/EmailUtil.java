package com.dev.moim.global.email;

import com.dev.moim.domain.account.dto.InQuiryDTO;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.global.error.handler.EmailException;
import com.dev.moim.global.redis.util.RedisUtil;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.dev.moim.global.common.code.status.ErrorStatus.EMAIL_SEND_FAIL;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public String sendAuthorizationCodeEmail(String receiver) throws Exception {
        String code = createCode();
        MimeMessage message = createAuthorizationCodeMessage(receiver, code);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(EMAIL_SEND_FAIL);
        }
        redisUtil.setValue(receiver, code, this.authCodeExpirationMillis);

        return code;
    }

    private String createCode() {
        StringBuffer code = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    code.append((char) (random.nextInt(26) + 'A'));
                    break;
                case 1:
                    code.append((char) (random.nextInt(26) + 'a'));
                    break;
                case 2:
                    code.append((random.nextInt(10)));
                    break;
            }
        }
        return code.toString();
    }

    private MimeMessage createAuthorizationCodeMessage(String receiver, String code) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        InternetAddress[] recipients = {new InternetAddress(receiver)};
        message.setSubject("MOIM 회원가입 인증 코드");
        message.setRecipients(Message.RecipientType.TO, recipients);

        String msg = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\">"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <title>MOIM 이메일 인증 코드 입니다</title>"
                + "</head>"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 0;\">"
                + "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">"
                + "        <div style=\"background-color: #00F0A1; padding: 20px; text-align: center; color: #ffffff;\">"
                + "            <h1 style=\"margin: 0; font-size: 24px;\">MOIM</h1>"
                + "        </div>"
                + "        <div style=\"padding: 20px;\">"
                + "            <h2 style=\"color: #333333;\">안녕하세요!</h2>"
                + "            <p style=\"color: #555555;\">MOIM에 가입해 주셔서 감사합니다. 아래 인증 코드를 입력하여 회원가입을 완료해 주세요.</p>"
                + "            <br>"
                + "            <div style=\"text-align: center; margin: 20px 0;\">"
                + "                <div style=\"display: inline-block; background-color: #00F0A1; color: #ffffff; padding: 10px 20px; font-size: 24px; letter-spacing: 2px; border-radius: 4px;\">" + code + "</div>"
                + "            </div>"
                + "            <br>"
                + "            <p style=\"color: #555555;\">인증 코드는 발송 시점부터 10분간 유효합니다.</p>"
                + "            <br>"
                + "            <p style=\"color: #555555;\">감사합니다,<br>MOIM 팀</p>"
                + "        </div>"
                + "        <div style=\"background-color: #f6f6f6; padding: 10px; text-align: center; font-size: 12px; color: #999999;\">"
                + "            <p>이 메일은 MOIM 서비스에서 자동으로 발송되었습니다. 회신하지 마세요.</p>"
                + "            <p>Copyright ⓒ MOIM. All Rights Reserved.</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        message.setContent(msg, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress("${spring.mail.username}", "MOIM"));

        return message;
    }

    public void sendInquiryEmail(User user, String receiver, String content) throws Exception {
        MimeMessage message = createInquiryMessage(user, receiver, content);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(EMAIL_SEND_FAIL);
        }
    }

    private MimeMessage createInquiryMessage(User user, String userEmail, String inquiryContent) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setSubject("MOIM 유저 의견 및 문의사항");
        helper.setTo("moim2moim@gmail.com");
        helper.setFrom(new InternetAddress("moim2moim@gmail.com", "MOIM"));

        String msg = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\">"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <title>문의 메일</title>"
                + "</head>"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 0;\">"
                + "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">"
                + "        <div style=\"background-color: #00F0A1; padding: 20px; text-align: center; color: #ffffff;\">"
                + "            <h1 style=\"margin: 0; font-size: 24px;\">의견 및 문의사항</h1>"
                + "        </div>"
                + "        <div style=\"padding: 20px;\">"
                + "            <p style=\"color: #333333; font-weight: bold;\">회신용 이메일: " + userEmail + "</p>"
                + "            <hr style=\"border: 0; height: 1px; background-color: #dddddd;\" />"
                + "            <p style=\"color: #555555; font-weight: bold;\">내용:</p>"
                + "            <p style=\"color: #555555;\">" + inquiryContent + "</p>"
                + "            <br>"
                + "        </div>"
                + "        <div style=\"background-color: #f6f6f6; padding: 10px; text-align: center; font-size: 12px; color: #999999;\">"
                + "            <p>Copyright ⓒ MOIM. All Rights Reserved.</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        helper.setText(msg, true);
        return message;
    }
}
