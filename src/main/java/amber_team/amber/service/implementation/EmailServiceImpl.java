package amber_team.amber.service.implementation;

import amber_team.amber.dao.interfaces.EmailDao;
import amber_team.amber.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailServiceImpl {


    private final JavaMailSender emailSender;

    private final EmailDao emailDao;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, EmailDao emailDao) {
        this.emailDao = emailDao;
        this.emailSender = javaMailSender;
    }

    public void sendRegistrationMessage(String to, String firstName) {
        String registrationTemplate = emailDao.getRegistrationTemplate();
        String text = String.format(registrationTemplate, firstName);
        String subject = "Registration";
        sendSimpleMessage(to, subject, text);
    }

    public void sendRequestCreated(String to, String firstName, String requestTitle) {
        String requestCreatedTemplate = emailDao.getRequestCreatedTemplate();
        String text = String.format(requestCreatedTemplate, firstName, requestTitle);
        String subject = "Request created!";
        sendSimpleMessage(to, subject, text);
    }

    public void sendRequestStatusChanged(String to, String firstName, String requestTitle, Status existedStatus,
                                         Status currentStatus) {
        String requestStatusChangedTemplate = emailDao.getRequestStatusChangedTemplate();
        String text = String.format(requestStatusChangedTemplate, firstName, requestTitle, existedStatus, currentStatus);
        String subject = "Request status changed!";
        sendSimpleMessage(to, subject, text);
    }

    public void sendUserRolesChanged(String to, String firstName, List<String> currentRoles) {
        String userRolesChangedTemplate = emailDao.getUserRolesChangedTemplate();
        String roles = String.join(", ", currentRoles);
        String text = String.format(userRolesChangedTemplate, firstName, roles);
        String subject = "Your roles list has been changed!";
        sendSimpleMessage(to, subject, text);
    }

    private void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }
}
