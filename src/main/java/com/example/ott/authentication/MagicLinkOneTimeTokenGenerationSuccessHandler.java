package com.example.ott.authentication;

import com.example.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
public class MagicLinkOneTimeTokenGenerationSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

	private final Logger logger = LoggerFactory.getLogger(MagicLinkOneTimeTokenGenerationSuccessHandler.class);

	private final OneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler(
			"/ott/sent");

	private final AccountService accountService;

	private final MagicLinkOneTimeTokenSender magicLinkOneTimeTokenSender;

	public MagicLinkOneTimeTokenGenerationSuccessHandler(AccountService accountService,
			MagicLinkOneTimeTokenSender magicLinkOneTimeTokenSender) {
		this.accountService = accountService;
		this.magicLinkOneTimeTokenSender = magicLinkOneTimeTokenSender;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken)
			throws IOException, ServletException {
		URI magicLink = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
			.replacePath(request.getContextPath())
			.replaceQuery(null)
			.fragment(null)
			.path("/login/ott")
			.queryParam("token", oneTimeToken.getTokenValue())
			.build()
			.toUri();
		try {
			this.accountService.findByUsername(oneTimeToken.getUsername())
				.ifPresent(account -> this.magicLinkOneTimeTokenSender.sendMagicLink(magicLink, account));
			this.redirectHandler.handle(request, response, oneTimeToken);
		}
		catch (ResponseStatusException e) {
			logger.error("Failed to send a magic link", e);
			response.sendError(e.getStatusCode().value(), e.getReason());
		}
	}

}
