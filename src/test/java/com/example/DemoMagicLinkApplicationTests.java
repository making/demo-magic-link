package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class DemoMagicLinkApplicationTests {

	static Playwright playwright;

	static Browser browser;

	BrowserContext context;

	Page page;

	@LocalServerPort
	int port;

	@Value("${maildev.port}")
	int mainDevPort;

	@BeforeAll
	static void before() {
		playwright = Playwright.create();
		browser = playwright.chromium().launch();
	}

	@BeforeEach
	void beforeEach() {
		context = browser.newContext();
		context.setDefaultTimeout(3000);
		page = context.newPage();
	}

	@AfterEach
	void afterEach() {
		context.close();
	}

	@AfterAll
	static void after() {
		playwright.close();
	}

	@Test
	void contextLoads(@Autowired RestClient.Builder restClientBuilder) {
		RestClient restClient = restClientBuilder.build();
		page.navigate("http://localhost:" + port);
		assertThat(page.title()).isEqualTo("Login");
		page.click("#link-to-sign-up");
		assertThat(page.title()).isEqualTo("Sign up");
		page.locator("input[name=username]").fill("johndoe");
		page.locator("input[name=email]").fill("jdoe@example.com");
		page.locator("button[type=submit]").press("Enter");
		assertThat(page.title()).isEqualTo("Account Registration");
		page.click("#link-to-login");
		assertThat(page.title()).isEqualTo("Login");
		page.locator("input[name=username]").fill("johndoe");
		page.locator("button[type=submit]").press("Enter");
		ResponseEntity<JsonNode> emailsResponse = restClient.get()
			.uri("http://127.0.0.1:" + mainDevPort + "/email")
			.retrieve()
			.toEntity(JsonNode.class);
		assertThat(emailsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		JsonNode emails = emailsResponse.getBody();
		assertThat(emails).isNotNull();
		assertThat(emails.size()).isEqualTo(1);
		JsonNode email = emails.get(0);
		assertThat(email.get("subject").asText()).startsWith("Your One Time Token");
		assertThat(email.get("to").get(0).get("address").asText()).startsWith("jdoe@example.com");
		assertThat(email.get("from").get(0).get("address").asText()).startsWith("noreply@example.com");
		assertThat(email.get("text").asText())
			.startsWith("Use the following link to sign in into the application: http://localhost:" + port);
		String magicLink = email.get("text").asText().split(": ")[1];
		page.navigate(magicLink);
		assertThat(page.title()).isEqualTo("One-Time Token Login");
		page.locator("button[type=submit]").press("Enter");
		assertThat(page.title()).isEqualTo("Account");
		assertThat(page.locator("h3").textContent()).isEqualTo("Hello johndoe!");
		assertThat(page.locator("p.message").textContent()).isEqualTo("Email: jdoe@example.com");
	}

}
