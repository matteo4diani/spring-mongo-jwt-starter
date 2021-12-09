package com.sashacorp.springmongojwtapi.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sashacorp.springmongojwtapi.models.persistence.user.Status;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;

/**
 * Custom implementation of Spring's {@link UserDetails} interface
 * @author matteo
 *
 */
public class UserDetailsImpl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String username;
	
	
	@JsonIgnore
	private String password;
	
	private String email;
	
	private int internalId;

	private String firstName;
	private String lastName;
	
	private String phone;
	
	private boolean smartWorking;
	private String position;
	
	private String site;
	private Status status;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	

	public UserDetailsImpl(String id, String username, String password, String email, int internalId, String firstName,
			String lastName, String phone, boolean smartWorking, String position, String site, Status status,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.internalId = internalId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.smartWorking = smartWorking;
		this.position = position;
		this.site = site;
		this.status = status;
		this.authorities = authorities;
	}
	
	/**
	 * Builds a Spring-compliant {@link UserDetailsImpl} from a User.
	 * Each {@link Authority} is converted to a {@link SimpleGrantedAuthority} to make it fungible for Spring Security.
	 * @param user
	 * @return
	 */
	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getPassword(), 
				user.getEmail(),
				user.getInternalId(),
				user.getFirstName(),
				user.getLastName(),
				user.getPhone(),
				user.isSmartWorking(),
				user.getPosition(),
				user.getSite(),
				user.getStatus(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getInternalId() {
		return internalId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public boolean isSmartWorking() {
		return smartWorking;
	}

	public String getPosition() {
		return position;
	}

	public String getSite() {
		return site;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public String toString() {
		return "UserDetailsImpl [id=" + id + ", username=" + username + ", email=" + email + ", internalId="
				+ internalId + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone
				+ ", smartWorking=" + smartWorking + ", position=" + position + ", site=" + site + ", status=" + status
				+ ", authorities=" + authorities + "]";
	}
	

}
