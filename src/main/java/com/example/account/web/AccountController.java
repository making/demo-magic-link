package com.example.account.web;

import com.example.account.Account;
import com.example.account.AccountService;
import com.example.account.AccountUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping(path = "/")
	public String account(@AuthenticationPrincipal AccountUserDetails userDetails, Model model) {
		model.addAttribute("account", userDetails.getAccount());
		return "account";
	}

	@GetMapping(path = "/signup", params = "form")
	public String signupForm() {
		return "signup";
	}

	@PostMapping(path = "/signup")
	public String signup(Account account) {
		System.out.println(account);
		this.accountService.insert(account);
		return "redirect:/signup?created";
	}

	@GetMapping(path = "/signup", params = "created")
	public String signupCreated() {
		return "account-created";
	}

}
