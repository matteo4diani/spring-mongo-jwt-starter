package com.sashacorp.springmongojwtapi.models.persistence.msg;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class offering a detailed representation/mold for the 'eventType' property of {@link Message}
 * 
 * @author matteo
 *
 */
@Document(collection = "eventTypes")
public class EventType {

	@Id
	private String id;

	private String type;

	private String description;

	public EventType() {
		super();
	}

	public EventType(String type, String description) {
		super();
		this.type = type;
		this.description = description;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
