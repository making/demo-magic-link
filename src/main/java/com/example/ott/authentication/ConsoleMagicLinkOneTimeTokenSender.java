package com.example.ott.authentication;

import com.example.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Fallback
public class ConsoleMagicLinkOneTimeTokenSender implements MagicLinkOneTimeTokenSender {

	private final Logger logger = LoggerFactory.getLogger(ConsoleMagicLinkOneTimeTokenSender.class);

	@Override
	public void sendMagicLink(URI magicLink, Account account) {
		logger.info("Send magicLink={} to {}", magicLink, account);
	}

}
