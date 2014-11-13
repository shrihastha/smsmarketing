package com.dissertation.smsmarketing.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {
	private String userName;
	private String password;

	public SmtpAuthenticator(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
