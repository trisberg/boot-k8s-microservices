package com.springdeveloper.k8s.gateway;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.Resource;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorResource {

	Long id;

	String name;

	int age;

	Collection<Resource<ImageResource>> images;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Collection<Resource<ImageResource>> getImages() {
		return images;
	}

	public void setImages(Collection<Resource<ImageResource>> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "ActorResource{" +
				"name='" + name + '\'' +
				", age=" + age +
				", images=" + images +
				'}';
	}
}
