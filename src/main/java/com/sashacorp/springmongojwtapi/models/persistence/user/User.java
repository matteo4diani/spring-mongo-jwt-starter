package com.sashacorp.springmongojwtapi.models.persistence.user;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class representing users. <br>
 * <span style='color: red;'><b>Warning: </b></span> Instances of this class
 * hold user credentials and sensitive data. When passing instances around
 * always call <b>eraseCredentials()</b> unless strictly needed. <br>
 * <span style='color: red;'><b>Warning: </b></span> When adding, removing or
 * modifying class properties please update the <i>mergeWith</i> method. It
 * provides a useful utility to do conservative updates of user data.
 * 
 * @author matteo
 *
 */
@Document(collection = "users")
public class User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NotBlank
	private String username;
	@NotBlank
	private String password;

	private Integer internalId;

	private String firstName;
	private String lastName;

	private String email;
	private String phone;

	private Boolean smartWorking;
	private String position;

	private String team;

	private String site;
	private Status status;


    
	private Set<Authority> authorities = new HashSet<>();

	public User() {
		super();
	}

	public User(@NotBlank String username, @NotBlank String password, String email) {
		super();

		this.username = username;
		this.password = password;
		this.email = email;

	}

	public User(String id, @NotBlank String username, @NotBlank String password, int internalId, String firstName,
			String lastName, String email, String phone, Boolean smartWorking, String position, String team,
			String site, Status status, Set<Authority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.internalId = internalId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.smartWorking = smartWorking;
		this.position = position;
		this.team = team;
		this.site = site;
		this.status = status;
		this.authorities = authorities;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getInternalId() {
		return internalId == null ? -1 : internalId;
	}

	public void setInternalId(Integer internalId) {
		this.internalId = internalId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean isSmartWorking() {
		return smartWorking == null ? false : smartWorking;
	}

	public void setSmartWorking(Boolean smartWorking) {
		this.smartWorking = smartWorking;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public void eraseState() {

		this.status = null;

	}

	public void eraseCredentials() {
		this.password = null;
	}

	public void eraseInfo() {

		this.email = null;
		this.phone = null;
		this.smartWorking = null;
		this.position = null;
		this.site = null;
		this.status = null;
		this.team = null;
		this.authorities = null;

	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", internalId=" + internalId
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phone=" + phone
				+ ", smartWorking=" + smartWorking + ", position=" + position + ", team=" + team + ", site=" + site
				+ ", status=" + status + ", authorities=" + authorities + "]";
	}

	/**
	 * Merges the current instance with <b>userFromRequest</b>, updating only fields
	 * that are non-null in <b>userFromRequest</b>. 'status' and security-related
	 * fields are not updated ('username','password','authorities').
	 * 
	 * @param userFromRequest
	 */
	public void mergeWith(User userFromRequest) {

		this.internalId = userFromRequest.getInternalId() != null ? userFromRequest.getInternalId() : this.internalId;
		this.firstName = userFromRequest.getFirstName() != null ? userFromRequest.getFirstName() : this.firstName;
		this.lastName = userFromRequest.getLastName() != null ? userFromRequest.getLastName() : this.lastName;
		this.email = userFromRequest.getEmail() != null ? userFromRequest.getEmail() : this.email;
		this.phone = userFromRequest.getPhone() != null ? userFromRequest.getPhone() : this.phone;
		this.smartWorking = userFromRequest.isSmartWorking() != null ? userFromRequest.isSmartWorking()
				: this.smartWorking;
		this.position = userFromRequest.getPosition() != null ? userFromRequest.getPosition() : this.position;
		this.team = userFromRequest.getTeam() != null ? userFromRequest.getTeam() : this.team;
		this.site = userFromRequest.getSite() != null ? userFromRequest.getSite() : this.site;
	}
	
	public Authority getMaxAuthority() {
		if (authorities == null) return null;
		return authorities
			      .stream()
			      .min(Comparator.comparing(Authority::getAuthorityLevel))
			      .orElseThrow(NoSuchElementException::new);
	}

}
