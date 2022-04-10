package com.sashacorp.springmongojwtapi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.Status;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.security.UserDetailsImpl;
import com.sashacorp.springmongojwtapi.util.StatusUtil;
import com.sashacorp.springmongojwtapi.util.TimeUtil;
import com.sashacorp.springmongojwtapi.util.http.HttpUtil;
import com.sashacorp.springmongojwtapi.util.http.hateoas.Url;

/**
 * Admin API endpoints for user management
 * 
 * @author matteo
 *
 */
@RestController
@CrossOrigin
public class UserController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;

	/**
	 * Get all users
	 * 
	 * @return
	 */
	@RequestMapping(value = Url.USERS, method = RequestMethod.GET)
	public ResponseEntity<?> getUsers() {
		Map<String, List<Message>> messagesByUser = messageRepository.findOngoing(TimeUtil.now()).parallelStream()
				.collect(Collectors.groupingBy(Message::fetchRequesterUsernameIfPresent, Collectors.toList()));
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());

		List<User> users = userRepository.findAll().parallelStream().map(user -> {
			StatusUtil.setUserStatus(messagesByUser, user);
			user.eraseCredentials();
			return user;
		}).collect(Collectors.toList());

		return HttpUtil.getResponse(users, HttpStatus.OK, requester);
	}

	/**
	 * Get a user by username (corporate email)
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = Url.USER_BY_USERNAME, method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable String username) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());

		if (userRepository.existsByUsername(username)) {
			User user = userRepository.findByUsername(username);
			StatusUtil.setUserStatus(messageRepository, user);
			user.eraseCredentials();
			return HttpUtil.getResponse(user, HttpStatus.OK, requester);
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Modify a user by username. Fields 'username', 'password', 'authorities' and
	 * 'status' cannot be updated through this endpoint.
	 * 
	 * @param username
	 * @param userFromRequest - provide
	 * @return
	 */
	@RequestMapping(value = Url.USER_BY_USERNAME, method = RequestMethod.PUT)
	public ResponseEntity<?> updateUserData(@PathVariable String username, @RequestBody User userFromRequest) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());

		if (userRepository.existsByUsername(username)) {
			User userToUpdate = userRepository.findByUsername(username);
			userToUpdate.mergeWith(userFromRequest);
			User updatedUser = userRepository.save(userToUpdate);
			StatusUtil.setUserStatus(messageRepository, updatedUser);
			updatedUser.eraseCredentials();
			return HttpUtil.getResponse(updatedUser, HttpStatus.OK, requester);
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Set the user's authorities. See {@link AuthorityOld}.
	 * 
	 * @param username
	 * @param authoritiesFromRequest - provide an array such as
	 *                               [{"authority":"ADMIN"},{"authority":"USER"}] in
	 *                               JSON
	 * @return
	 */
	@RequestMapping(value = Url.USER_AUTHORITIES_BY_USERNAME, method = RequestMethod.PUT)
	public ResponseEntity<?> updateUserAuthorities(@PathVariable String username,
			@RequestBody Set<String> authoritiesFromRequest) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());

		if (userRepository.existsByUsername(username)) {
			User userToUpdate = userRepository.findByUsername(username);
			Set<Authority> authorities = new HashSet<>();

			authoritiesFromRequest.forEach(authority -> {
				Authority internalAuthority = Authority.getAuthority(authority);
				authorities.add(internalAuthority);
			});

			userToUpdate.setAuthorities(authorities);
			User updatedUser = userRepository.save(userToUpdate);
			StatusUtil.setUserStatus(messageRepository, updatedUser);
			updatedUser.eraseCredentials();
			return HttpUtil.getResponse(updatedUser, HttpStatus.OK, requester);
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Update user status. If status.hardcoded is set to <b>false</b> user status is
	 * reset to null.
	 * 
	 * @param username
	 * @param status   - provide status as {"availability": "YOUR_VALUE",
	 *                 "hardcoded": true|false} in JSON
	 * @return
	 */
	@RequestMapping(value = Url.USER_STATUS_BY_USERNAME, method = RequestMethod.PUT)
	public ResponseEntity<?> updateUserStatus(@PathVariable String username, @RequestBody Status status) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());

		if (userRepository.existsByUsername(username)) {
			User userToUpdate = userRepository.findByUsername(username);

			if (status.isHardcoded()) {
				userToUpdate.setStatus(status);
			} else {
				userToUpdate.setStatus(null);
			}

			User updatedUser = userRepository.save(userToUpdate);
			StatusUtil.setUserStatus(messageRepository, updatedUser);
			updatedUser.eraseCredentials();
			return HttpUtil.getResponse(updatedUser, HttpStatus.OK, requester);
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
	}

}
