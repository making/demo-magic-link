package com.example.account;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {

	private final JdbcClient jdbcClient;

	public AccountService(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public Optional<Account> findByUsername(String username) {
		return this.jdbcClient.sql("SELECT username, email FROM account WHERE username = ?")
			.param(username)
			.query(Account.class)
			.optional();
	}

	@Transactional
	public int insert(Account account) {
		return this.jdbcClient.sql("INSERT INTO account(username, email) VALUES (:username, :email)")
			.param("username", account.username())
			.param("email", account.email())
			.update();
	}

}
