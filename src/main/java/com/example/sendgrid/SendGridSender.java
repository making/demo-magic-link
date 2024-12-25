package com.example.sendgrid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * See <a href=
 * "https://www.twilio.com/docs/sendgrid/api-reference/mail-send/mail-send">SendGrid v3
 * API Documentation</a> for full api
 */
@Component
public class SendGridSender {

	private final RestClient restClient;

	private final SendGridProps props;

	public SendGridSender(RestClient.Builder restClientBuilder, SendGridProps props) {
		this.restClient = restClientBuilder.baseUrl(props.url())
			.defaultHeaders(headers -> headers.setBearerAuth(props.apiKey()))
			.defaultStatusHandler(__ -> true, (req, res) -> {
			})
			.build();
		this.props = props;
	}

	public void sendMail(String to, String subject, String content) {
		ResponseEntity<String> response = this.restClient.post()
			.uri("/v3/mail/send")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Map.of("personalizations", List.of(Map.of("to", List.of(Map.of("email", to)), "subject", subject)),
					"from", Map.of("email", this.props.from()), "content",
					List.of(Map.of("type", "text/plain", "value", content))))
			.retrieve()
			.toEntity(String.class);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
					"Failed to send a mail: " + response.getBody());
		}
	}

}
