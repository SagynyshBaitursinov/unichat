package kz.codingwolves.unichat.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private JavaMailSender mailSender2;
	
	@Autowired
	private JavaMailSender mailSender3;
	
	int whichSender;
	
	public void send(String email, String password, boolean forgot) throws MessagingException {
		JavaMailSender senderInstance = null;
    	synchronized(this) {
    		switch(whichSender) {
    			case 0:
    				senderInstance = mailSender;
    				whichSender++;
    				break;
    			case 1:
    				senderInstance = mailSender2;
    				whichSender++;
    				break;
    			case 2:
    				senderInstance = mailSender3;
    				whichSender = 0;
    				break;
    		}
    	}
        MimeMessage message = senderInstance.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		if (forgot) { 
			message.setContent("<center><h3 style=\"color:#00897b\">Hello!</h3><p style=\"color: #000000\">Here is your password: </p><p style=\"color: #000000\">" + password + "</p><p><span style=\"color: #000000\">Sincerly, your </span><a href=\"http://unichat.kz\">Unichat</a></p></center>", "text/html");
		} else {
			message.setContent("<center><h3 style=\"color:#00897b\">Thank you for registering!</h3><p style=\"color: #000000\">Here is your password: </p><p style=\"color: #000000\">" + password + "</p><p><span style=\"color: #000000\">Sincerly, your </span><a href=\"http://unichat.kz\">Unichat</a></p></center>", "text/html");
		}
		helper.setTo(email);
		if (forgot) {
			helper.setSubject("Unichat service");
		} else {
			helper.setSubject("Welcome to Unichat!");
		}
		senderInstance.send(message);
	}
}
