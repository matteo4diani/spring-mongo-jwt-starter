package com.sashacorp.springmongojwtapi.models.persistence.msg;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class is to be used by the user agent to understand and internationalize the 'leave' property of {@link Message} 
 * @author matteo
 *
 */
@Document(collection="leaves")
public class Leave {
	
	@Id
	private String id;
	
	private String name;
	private String ita;
	private String eng;

	public Leave() {
		super();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Leave(String leave, String ita) {
		super();
		this.name = leave;
		this.ita = ita;
	}
	
	public Leave(String leave, String ita, String eng) {
		super();
		this.name = leave;
		this.ita = ita;
		this.eng = eng;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String leave) {
		this.name = leave;
	}
	public String getIta() {
		return ita;
	}
	public void setIta(String ita) {
		this.ita = ita;
	}
	public String getEng() {
		return eng;
	}
	public void setEng(String eng) {
		this.eng = eng;
	}
	
	

}
