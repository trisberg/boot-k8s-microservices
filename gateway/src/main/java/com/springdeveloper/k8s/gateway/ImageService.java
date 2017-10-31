package com.springdeveloper.k8s.gateway;

import java.util.Collection;
import java.util.Collections;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class ImageService {

	private Log log = LogFactory.getLog(getClass());

	@Value("${images.service.url:http://localhost:8082/images}")
	String imageServiceUrl;

	private final RestTemplate restTemplate;

	@Autowired
	ImageService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(commandKey = "ImagesByName", fallbackMethod = "noImages", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
	})
	Collection<Resource<ImageResource>> getImagesByName(@PathVariable String name) {

		String imagesSearchPath = imageServiceUrl + "/search/findByName?name={name}";
		log.info("URL is: " + imagesSearchPath);

		ParameterizedTypeReference<Resources<Resource<ImageResource>>> imageTypeRef =
				new TypeReferences.ResourcesType<Resource<ImageResource>>() {};
		ResponseEntity<Resources<Resource<ImageResource>>> imageResources =
					this.restTemplate.exchange(imagesSearchPath, HttpMethod.GET, null, imageTypeRef,
							Collections.singletonMap("name", name));
 		Collection<Resource<ImageResource>> images = imageResources.getBody().getContent();
		log.info("Found images: " + images);

		return images;
	}

	Collection<Resource<ImageResource>> noImages(@PathVariable String name) {
		Collection<Resource<ImageResource>> empty = Collections.emptyList();
		log.info("FALLBACK FOR IMAGES: " + empty);
		return empty;
	}

}
