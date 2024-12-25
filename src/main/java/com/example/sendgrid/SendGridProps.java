package com.example.sendgrid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

@ConfigurationProperties(prefix = "sendgrid")
public record SendGridProps(@DefaultValue("https://api.sendgrid.com") URI url, String apiKey,
		@DefaultValue("noreply@example.com") String from) {

}
