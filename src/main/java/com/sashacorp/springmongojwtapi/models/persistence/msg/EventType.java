package com.sashacorp.springmongojwtapi.models.persistence.msg;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class is to be used by the user agent to understand and internationalize
 * the 'eventType' property of {@link Message}
 * 
 * @author matteo
 *
 */
@Document(collection = "eventTypes")
public class EventType {

	@Id
	private String id;

	private String name;

	private String description;

	public EventType() {
		super();
	}

	public EventType(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
