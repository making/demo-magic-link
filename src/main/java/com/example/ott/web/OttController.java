package com.example.ott.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OttController {

	@GetMapping(path = "/login")
	public String login() {
		return "login";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, path = "/login/ott")
	// @formatter:off
	// POST /login/ott is actually handled by AuthenticationFilter but define this mapping to prevent "org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' is not supported"
	// @formatter:on
	public String loginOtt() {
		return "login-ott";
	}

	@GetMapping(path = "/ott/sent")
	public String sentOtt() {
		return "ott-sent";
	}

}
