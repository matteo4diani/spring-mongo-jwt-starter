package com.sashacorp.springmongojwtapi.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.http.Error;
import com.sashacorp.springmongojwtapi.models.http.Notification;
import com.sashacorp.springmongojwtapi.models.http.sse.AdminNotificationSse;
import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.security.UserDetailsImpl;
import com.sashacorp.springmongojwtapi.service.sse.AdminNotificationService;
import com.sashacorp.springmongojwtapi.util.StatusUtil;
import com.sashacorp.springmongojwtapi.util.TimeUtil;
import com.sashacorp.springmongojwtapi.util.http.HttpUtil;
import com.sashacorp.springmongojwtapi.util.http.Par;
import com.sashacorp.springmongojwtapi.util.http.Url;

/**
 * User API endpoints for request management
 * 
 * @author matteo
 *
 */
@RestController
@CrossOrigin
public class MeController {
	@Autowired
	AdminNotificationService adminNotificationService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;

	/**
	 * Get the current user's details
	 * 
	 * @return
	 */
	@RequestMapping(value = Url.ME, method = RequestMethod.GET)
	public ResponseEntity<?> getMe() {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userRepository.existsByUsername(userDetails.getUsername())) {
			User requester = userRepository.findByUsername(userDetails.getUsername());
			requester.eraseCredentials();
			StatusUtil.setUserStatus(messageRepository, requester);
			return HttpUtil.getResponse(requester, HttpStatus.OK, requester);
		}

		return HttpUtil.getHttpStatusResponse(HttpStatus.UNAUTHORIZED);

	}

	/**
	 * Get all messages for the current user
	 * 
	 * @param pending
	 * @param approved
	 * @return
	 */
	@RequestMapping(value = Url.MY_MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<?> getMyMessages(@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();
		User requester = userRepository.findByUsername(username);
		List<Message> messages = null;

		if (approved != null) {
			messages = messageRepository.findByUsernameAndApproval(username, approved);
		} else if (pending != null) {
			messages = messageRepository.findByUsernameAndPending(username, pending);
		} else {
			messages = messageRepository.findByUsername(username);
		}

		return HttpUtil.getResponse(messages, HttpStatus.OK, requester);
	}

	/**
	 * If both parameters are undefined selects all current messages for the current
	 * user. Refer to {@link MessageRepository} for definitions
	 * 
	 * @param pending  - Optional.
	 * @param approved - Optional. if present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.MY_CURRENT_MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<?> getMyCurrentMessages(@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();
		User requester = userRepository.findByUsername(username);
		List<Message> messages = null;

		if (approved != null) {
			messages = messageRepository.findCurrentByUsernameAndApproval(username, TimeUtil.now(), approved);
		} else if (pending != null) {
			messages = messageRepository.findCurrentByUsernameAndPending(username, TimeUtil.now(), pending);
		} else {
			messages = messageRepository.findCurrentByUsername(username, TimeUtil.now());
		}

		return HttpUtil.getResponse(messages, HttpStatus.OK, requester);
	}

	/**
	 * Selects all ongoing messages for the current user. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param pending  - Optional.
	 * @param approved - Optional. if present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.MY_ONGOING_MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<?> getMyOngoingMessages() {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();
		User requester = userRepository.findByUsername(username);

		return HttpUtil.getResponse(messageRepository.findOngoingByUsername(username, TimeUtil.now()), HttpStatus.OK,
				requester);
	}

	/**
	 * If both parameters are undefined selects all outdated messages for the
	 * current user. Refer to {@link MessageRepository} for definitions
	 * 
	 * @param pending  - Optional.
	 * @param approved - Optional. if present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.MY_OUTDATED_MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<?> getMyOutdatedMessages(@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();
		User requester = userRepository.findByUsername(username);

		List<Message> messages = null;
		if (approved != null) {
			messages = messageRepository.findOutdatedByUsernameAndApproval(username, TimeUtil.now(), approved);
		} else if (pending != null) {
			messages = messageRepository.findOutdatedByUsernameAndPending(username, TimeUtil.now(), pending);
		} else {
			messages = messageRepository.findOutdatedByUsername(username, TimeUtil.now());
		}

		return HttpUtil.getResponse(messages, HttpStatus.OK, requester);
	}

	/**
	 * Get a message for the current user by ID
	 * 
	 * @param messageId
	 * @return
	 */
	@RequestMapping(value = Url.MY_MESSAGE_BY_MESSAGE_ID, method = RequestMethod.GET)
	public ResponseEntity<?> getMyMessageById(@PathVariable String messageId) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		Message message = messageRepository.findById(messageId).orElse(null);
		String username = userDetails.getUsername();
		User requester = userRepository.findByUsername(username);

		if (userDetails.getUsername().equals(message.getRequester().getUsername())) {
			/**
			 * 'seen' is set to true to suppress notification on the user side (non-admin)
			 * of the front-end
			 */
			message.setSeen(true);
			return HttpUtil.getResponse(messageRepository.save(message), HttpStatus.OK, requester);
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * Post a new request for a leave/event. Admins are notified via Server Sent Events:
	 * see {@link AdminNotificationService}.
	 * 
	 * @param message - provide 'start', 'end', 'requestNote' and 'eventType' in JSON
	 * @return
	 */
	@RequestMapping(value = Url.MY_MESSAGES, method = RequestMethod.POST)
	public ResponseEntity<?> postMyMessage(@RequestBody Message message) {

		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		User requester = userRepository.findByUsername(userDetails.getUsername());
		requester.eraseCredentials();
		requester.eraseState();
		List<Message> requestsFromStartToEnd = messageRepository.findBetweenByUsername(requester.getUsername(),
				message.getStart(), message.getEnd());

		/*
		 * Activities cannot overlap, thus when posting a new request we check
		 * beforehand if the user has any ongoing activities in the selected timeslot.
		 * If so, we return an HTTP STATUS 418 ("I'M A TEAPOT"), because they're trying
		 * to brew coffee with a teapot.
		 */
		if (requestsFromStartToEnd != null && requestsFromStartToEnd.size() > 0) {
			HttpUtil.getResponse(requestsFromStartToEnd, HttpStatus.I_AM_A_TEAPOT, requester);
		}

		message.setCreated(TimeUtil.now());
		message.setPending(true);
		message.setRequester(requester);
		message.setSeen(true);
		Message newMessage = messageRepository.save(message);
		AdminNotificationSse adminNotificationEvent = new AdminNotificationSse(Notification.POST_USER.text(),
				requester.getId(), requester.getUsername(), newMessage.getId(), newMessage);
		adminNotificationService.getEventPublisher().publishEvent(adminNotificationEvent);
		return HttpUtil.getResponse(newMessage, HttpStatus.CREATED, requester);
	}

	/**
	 * Delete a message for the current user by ID
	 * 
	 * @param messageId
	 * @return
	 */
	@RequestMapping(value = Url.MY_MESSAGE_BY_MESSAGE_ID, method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMyMessage(@PathVariable String messageId) {

		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		Message message = messageRepository.findById(messageId).orElse(null);

		if (userDetails.getUsername().equals(message.getRequester().getUsername())) {
			messageRepository.deleteById(messageId);
			return HttpUtil.getHttpStatusResponse(HttpStatus.OK);
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.UNAUTHORIZED);
		}
	}
}
