package com.bb.websrvclient.email;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailSenderUtility {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail(final String to, final String subject, final String msg, final File attachment) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(msg);
			// attach the file
			if(attachment != null && attachment.exists() && attachment.isFile()){
				FileSystemResource file = new FileSystemResource(attachment);
				helper.addAttachment(attachment.getName(), file);
			}
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}