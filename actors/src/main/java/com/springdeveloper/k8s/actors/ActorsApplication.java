package com.springdeveloper.k8s.actors;

import io.micrometer.core.instrument.binder.system.ProcessorMetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ActorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActorsApplication.class, args);
	}

	@Bean
	ProcessorMetrics cpuMetrics() {
		return new ProcessorMetrics();
	}
}
