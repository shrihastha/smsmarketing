package com.dissertation.smsmarketing.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SendSMSOrEmailUtil {
	Logger log = LoggerFactory.getLogger(SendSMSOrEmailUtil.class);

	private MailSender mailSender;
	private SimpleMailMessage templateMessage;

	public SendSMSOrEmailUtil(MailSender mailSender, SimpleMailMessage templateMessage) {
		super();
		this.mailSender = mailSender;
		this.templateMessage = templateMessage;
	}

	public void sendEmail() {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setTo("shrihastha@gmail.com");
		msg.setText("This is a test.");
		try {
			this.mailSender.send(msg);
		} catch (MailException e) {
			e.printStackTrace();
			log.error("Exception {}",e.getMessage());
		}
	}
}