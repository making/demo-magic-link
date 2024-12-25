package com.example.ott.authentication;

import com.example.account.Account;
import com.example.sendgrid.SendGridSender;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class SendGridMagicLinkOneTimeTokenSender implements MagicLinkOneTimeTokenSender {

	private final SendGridSender sendGridSender;

	public SendGridMagicLinkOneTimeTokenSender(SendGridSender sendGridSender) {
		this.sendGridSender = sendGridSender;
	}

	@Override
	public void sendMagicLink(URI magicLink, Account account) {
		this.sendGridSender.sendMail(account.email(), "Your One Time Token",
				"Use the following link to sign in into the application: " + magicLink);
	}

}
