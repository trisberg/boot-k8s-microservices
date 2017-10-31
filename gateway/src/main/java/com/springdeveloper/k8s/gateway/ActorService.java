package com.springdeveloper.k8s.gateway;

import java.util.Collection;
import java.util.Collections;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Service
public class ActorService {

	private Log log = LogFactory.getLog(getClass());

	@Value("${actors.service.url:http://localhost:8081/actors}")
	String actorServiceUrl;

	private final RestTemplate restTemplate;

	@Autowired
	ActorService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(commandKey = "ActorByName", fallbackMethod = "noActors", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
	})
	Collection<Resource<ActorResource>> getActorByName(@PathVariable String name) {

		String actorsSearchPath = actorServiceUrl + "/search/findByName?name={name}";
		log.info("URL is: " + actorsSearchPath);

		ParameterizedTypeReference<Resources<Resource<ActorResource>>> actorTypeRef =
				new TypeReferences.ResourcesType<Resource<ActorResource>>() {};
		ResponseEntity<Resources<Resource<ActorResource>>> actorResources =
				this.restTemplate.exchange(actorsSearchPath, HttpMethod.GET, null, actorTypeRef,
						Collections.singletonMap("name", name));

		Collection<Resource<ActorResource>> actors = actorResources.getBody().getContent();
		log.info("Found actors: " + actors);

		return actors;
	}

	Collection<Resource<ActorResource>> noActors(@PathVariable String name) {
		Collection<Resource<ActorResource>> empty = Collections.emptyList();
		log.info("FALLBACK FOR ACTORS: " + empty);
		return empty;
	}
}
