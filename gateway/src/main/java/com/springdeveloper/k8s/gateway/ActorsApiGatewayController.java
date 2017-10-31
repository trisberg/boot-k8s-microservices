package com.springdeveloper.k8s.gateway;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ActorsApiGatewayController {

	private Log log = LogFactory.getLog(getClass());

	private ActorService actorService;

	private ImageService imageService;

	@Autowired
	ActorsApiGatewayController(ActorService actorService, ImageService imageService) {
		this.actorService = actorService;
		this.imageService = imageService;
	}

	@GetMapping("/actors/{name}")
	@SuppressWarnings("unchecked")
	Collection<Resource<ActorResource>> actorByName(@PathVariable String name) {
		log.info("Looking up actor: " + name);
		Collection<Resource<ActorResource>> actors = actorService.getActorByName(name);

		Collection<Resource<ImageResource>> images = imageService.getImagesByName(name);
		for (Resource<ActorResource> a : actors) {
			a.getContent().setImages(images);
		}

		return actors;
	}

}
