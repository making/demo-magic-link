package com.example;

import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
	}

	@Bean
	GenericContainer<?> sendgrid() {
		return new GenericContainer<>("ykanazawa/sendgrid-maildev").withEnv("SENDGRID_DEV_API_SERVER", ":3030")
			.withEnv("SENDGRID_DEV_API_KEY", "SG.test")
			.withEnv("SENDGRID_DEV_SMTP_SERVER", "127.0.0.1:1025")
			.withExposedPorts(3030, 1080)
			.waitingFor(new LogMessageWaitStrategy().withRegEx(".*sendgrid-dev entered RUNNING state.*")
				.withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS)))
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("sendgrid-maildev")));
	}

	@Bean
	DynamicPropertyRegistrar dynamicPropertyRegistrar(GenericContainer<?> sendgrid) {
		return registry -> {
			registry.add("sendgrid.url", () -> "http://127.0.0.1:" + sendgrid.getMappedPort(3030));
			registry.add("sendgrid.api-key", () -> "SG.test");
			registry.add("maildev.port", () -> sendgrid.getMappedPort(1080));
		};
	}

}
