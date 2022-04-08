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

import com.sashacorp.springmongojwtapi.models.http.Errors;
import com.sashacorp.springmongojwtapi.models.http.Notifications;
import com.sashacorp.springmongojwtapi.models.http.resources.HateoasUtil;
import com.sashacorp.springmongojwtapi.models.http.resources.Par;
import com.sashacorp.springmongojwtapi.models.http.resources.Url;
import com.sashacorp.springmongojwtapi.models.http.sse.AdminNotificationSse;
import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.security.UserDetailsImpl;
import com.sashacorp.springmongojwtapi.service.sse.AdminNotificationService;
import com.sashacorp.springmongojwtapi.util.HttpUtil;
import com.sashacorp.springmongojwtapi.util.StatusUtil;
import com.sashacorp.springmongojwtapi.util.TimeUtil;

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
			User user = userRepository.findByUsername(userDetails.getUsername());
			HateoasUtil.setUserResources(user);
			user.eraseCredentials();
			StatusUtil.setUserStatus(messageRepository, user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);

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
			return HttpUtil.getMessageResponse(Errors.USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();

		if (approved != null) {
			return new ResponseEntity<List<Message>>(messageRepository.findByReqUsernameAndApproval(username, approved),
					HttpStatus.OK);
		} else if (pending != null) {
			return new ResponseEntity<List<Message>>(messageRepository.findByReqUsernameAndPending(username, pending),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Message>>(messageRepository.findByReqUsername(username), HttpStatus.OK);
		}
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
			return HttpUtil.getMessageResponse(Errors.USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();

		if (approved != null) {
			return new ResponseEntity<List<Message>>(
					messageRepository.findCurrentByReqUsernameAndApproval(username, TimeUtil.now(), approved),
					HttpStatus.OK);
		} else if (pending != null) {
			return new ResponseEntity<List<Message>>(
					messageRepository.findCurrentByReqUsernameAndPending(username, TimeUtil.now(), pending),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Message>>(
					messageRepository.findCurrentByReqUsername(username, TimeUtil.now()), HttpStatus.OK);
		}

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
			return HttpUtil.getMessageResponse(Errors.USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();

		return new ResponseEntity<List<Message>>(messageRepository.findOngoingByReqUsername(username, TimeUtil.now()),
				HttpStatus.OK);
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
			return HttpUtil.getMessageResponse(Errors.USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		String username = userDetails.getUsername();

		if (approved != null) {
			return new ResponseEntity<List<Message>>(
					messageRepository.findApprovalByReqUsernameAndApproval(username, TimeUtil.now(), approved),
					HttpStatus.OK);
		} else if (pending != null) {
			return new ResponseEntity<List<Message>>(
					messageRepository.findOutdatedByReqUsernameAndPending(username, TimeUtil.now(), pending),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Message>>(
					messageRepository.findOutdatedReqUsername(username, TimeUtil.now()), HttpStatus.OK);
		}
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
			return HttpUtil.getMessageResponse(Errors.USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		Message message = messageRepository.findById(messageId).orElse(null);

		if (userDetails.getUsername().equals(message.getRequirer().getUsername())) {
			/**
			 * 'seen' is set to true to suppress notification on the user side (non-admin)
			 * of the front-end
			 */
			message.setSeen(true);
			return new ResponseEntity<Message>(messageRepository.save(message), HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * Post a new request for a leave. Admins are notified via Server Sent Events:
	 * see {@link AdminNotificationService}.
	 * 
	 * @param message - provide 'start', 'end', 'requestNote' and 'leave' in JSON
	 * @return
	 */
	@RequestMapping(value = Url.MY_MESSAGES, method = RequestMethod.POST)
	public ResponseEntity<?> postMyMessage(@RequestBody Message message) {

		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null || !userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getMessageResponse(Errors.USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		User user = userRepository.findByUsername(userDetails.getUsername());
		user.eraseCredentials();
		user.eraseState();
		List<Message> requestsFromStartToEnd = messageRepository.findBetweenByReqUsername(user.getUsername(),
				message.getStart(), message.getEnd());

		/*
		 * Activities cannot overlap, thus when posting a new request we check
		 * beforehand if the user has any ongoing activities in the selected timeslot.
		 * If so, we return an HTTP STATUS 418 ("I'M A TEAPOT"), because they're trying
		 * to brew coffee with a teapot.
		 */
		if (requestsFromStartToEnd != null && requestsFromStartToEnd.size() > 0) {
			return new ResponseEntity<List<Message>>(requestsFromStartToEnd, HttpStatus.I_AM_A_TEAPOT);
		}

		message.setCreated(TimeUtil.now());
		message.setPending(true);
		message.setRequirer(user);
		message.setSeen(true);
		Message newMessage = messageRepository.save(message);
		AdminNotificationSse adminNotificationEvent = new AdminNotificationSse(Notifications.POST_USER.text(),
				user.getId(), user.getUsername(), newMessage.getId(), newMessage);
		adminNotificationService.getEventPublisher().publishEvent(adminNotificationEvent);
		return new ResponseEntity<Message>(newMessage, HttpStatus.CREATED);
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
			return HttpUtil.getMessageResponse(Errors.USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		Message message = messageRepository.findById(messageId).orElse(null);

		if (userDetails.getUsername().equals(message.getRequirer().getUsername())) {
			messageRepository.deleteById(messageId);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}
}
