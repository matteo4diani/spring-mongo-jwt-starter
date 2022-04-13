package com.sashacorp.springmongojwtapi.appconfig;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * Entity class representing application-wide configuration and state
 * @author matteo
 *
 */
@Document(collection = "appconfig")
public class AppConfiguration {
	@Id
	private String id;
	
	private String companyName;
	private boolean isInitialized;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppConfiguration(String companyName, boolean isInitialized) {
		super();
		this.companyName = companyName;
		this.isInitialized = isInitialized;
	}

	public String getCompanyName() {
		return companyName;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	@Override
	public String toString() {
		return "AppConfiguration [id=" + id + ", companyName=" + companyName + ", isInitialized=" + isInitialized + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(companyName, id, isInitialized);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppConfiguration other = (AppConfiguration) obj;
		return Objects.equals(companyName, other.companyName) && Objects.equals(id, other.id)
				&& isInitialized == other.isInitialized;
	}
	
	
	
	
	
	
	
}
