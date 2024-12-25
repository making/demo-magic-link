package com.example.ott.authentication;

import com.example.account.Account;

import java.net.URI;

public interface MagicLinkOneTimeTokenSender {

	void sendMagicLink(URI magicLink, Account account);

}
