package com.security.JWT.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.security.JWT.Constant.EmailConstant;
import com.security.JWT.Constant.FileConstant;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    public void sendNewPasswordEmail(String firstName, String password, String email, String imgUrl, String loginUrl)
            throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException,
            ParseException, IOException, TemplateException {

        String htmlContent = createEmailContent(firstName, password, imgUrl, loginUrl);
        sendHtmlEmail(email, EmailConstant.EMAIL_SUBJECT, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates HTML format

            // Add CC if configured
            String ccEmail = System.getenv("MAIL_CC");
            if (ccEmail != null && !ccEmail.isEmpty()) {
                helper.addCc(ccEmail);
            }

            String fromEmail = System.getenv("MAIL_FROM");
            log.debug(fromEmail+"System.getenv(\"MAIL_FROM\")"+System.getenv("MAIL_FROM"));
            if (fromEmail != null && !fromEmail.isEmpty()) {
                helper.setFrom(fromEmail);
            }

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("Failed to send email to: " + to);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error while sending email: " + e.getMessage());
            e.printStackTrace();
            throw new MessagingException("Error sending email", e);
        }
    }

    private String createEmailContent(String firstName, String password, String imgUrl, String loginUrl)
            throws TemplateNotFoundException, MalformedTemplateNameException,
            ParseException, IOException, TemplateException {

        Map<String, String> model = new HashMap<>();
        model.put("firstName", firstName);
        model.put("password", password);
        model.put("imgUrl", FileConstant.DEFAULT_USER_IMAGE_URL);
        model.put("loginUrl", loginUrl);

        Template template;
        if (loginUrl == null || loginUrl.isEmpty()) {
            template = freemarkerConfig.getTemplate("email-template.ftl");
        } else {
            template = freemarkerConfig.getTemplate("email-template-reset.ftl");
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }
}



/*
 * package com.security.JWT.Service;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.mail.SimpleMailMessage; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.stereotype.Service; import
 * org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
 * 
 * import com.security.JWT.Constant.FileConstant; import
 * com.security.JWT.Constant.SecurityConstant;
 * 
 * import jakarta.mail.Message; import jakarta.mail.MessagingException; import
 * jakarta.mail.Session; import jakarta.mail.Transport; import
 * jakarta.mail.internet.InternetAddress; import
 * jakarta.mail.internet.MimeMessage;
 * 
 * import freemarker.core.ParseException; import
 * freemarker.template.Configuration; import
 * freemarker.template.MalformedTemplateNameException; import
 * freemarker.template.Template; import freemarker.template.TemplateException;
 * import freemarker.template.TemplateNotFoundException;
 * 
 * import java.io.IOException; import java.util.Date; import java.util.HashMap;
 * import java.util.Map; import java.util.Properties;
 * 
 * import static com.security.JWT.Constant.EmailConstant.*; import static
 * jakarta.mail.Message.RecipientType.CC; import static
 * jakarta.mail.Message.RecipientType.TO;
 * 
 * @Service public class EmailService {
 * 
 * @Autowired private Configuration freemarkerConfig;
 * 
 * @Autowired private JavaMailSender mailSender;
 * 
 * public void sendEmail() { SimpleMailMessage msg = new SimpleMailMessage();
 * msg.setTo("iamarun.ja@gmail.com"); msg.setSubject("Test");
 * msg.setText("Hello");
 * 
 * mailSender.send(msg); }
 * 
 * 
 * public void sendNewPasswordEmail(String firstName, String password, String
 * email, String imgUrl, String loginUrl) throws MessagingException,
 * TemplateNotFoundException, MalformedTemplateNameException, ParseException,
 * IOException, TemplateException {
 * 
 * Message message = createEmail(firstName, password, email, imgUrl, loginUrl);
 * 
 * // Use Transport directly, no casting needed Transport transport =
 * getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
 * transport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
 * transport.sendMessage(message, message.getAllRecipients());
 * transport.close(); }
 * 
 * private Message createEmail(String firstName, String password, String email,
 * String imgUrl, String loginUrl) throws MessagingException,
 * TemplateNotFoundException, MalformedTemplateNameException, ParseException,
 * IOException, TemplateException {
 * 
 * Message message = new MimeMessage(getEmailSession()); message.setFrom(new
 * InternetAddress(FROM_EMAIL)); message.setRecipients(TO,
 * InternetAddress.parse(email, false)); message.setRecipients(CC,
 * InternetAddress.parse(CC_EMAIL, false)); message.setSubject(EMAIL_SUBJECT);
 * 
 * // Prepare Freemarker model Map<String, String> model = new HashMap<>();
 * model.put("firstName", firstName); model.put("password", password);
 * model.put("imgUrl", FileConstant.DEFAULT_USER_IMAGE_URL);
 * model.put("loginUrl", loginUrl);
 * 
 * 
 * Template template = null; if (loginUrl.equals("")) { template =
 * freemarkerConfig.getTemplate("email-template.ftl"); } else { template =
 * freemarkerConfig.getTemplate("email-template-reset.ftl"); } String htmlBody =
 * FreeMarkerTemplateUtils.processTemplateIntoString(template, model); // Set
 * HTML content message.setContent(htmlBody, "text/html; charset=UTF-8");
 * message.setSentDate(new Date()); message.saveChanges(); return message; }
 * 
 * private Session getEmailSession() { Properties properties = new Properties();
 * properties.put(SMTP_HOST, GMAIL_SMTP_SERVER); properties.put(SMTP_AUTH,
 * "true"); properties.put(SMTP_PORT, DEFAULT_PORT);
 * properties.put(SMTP_STARTTLS_ENABLE, "true");
 * properties.put(SMTP_STARTTLS_REQUIRED, "true");
 * 
 * // No authenticator needed if you pass username/password in connect() return
 * Session.getInstance(properties, null); } }
 */