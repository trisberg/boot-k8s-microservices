package com.springdeveloper.k8s.images;

import io.micrometer.core.instrument.binder.system.ProcessorMetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagesApplication.class, args);
	}

	@Bean
	ProcessorMetrics cpuMetrics() {
		return new ProcessorMetrics();
	}
}
