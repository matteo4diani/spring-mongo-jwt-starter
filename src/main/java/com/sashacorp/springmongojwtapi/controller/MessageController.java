package com.sashacorp.springmongojwtapi.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.sashacorp.springmongojwtapi.models.http.sse.UserNotificationSse;
import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.security.UserDetailsImpl;
import com.sashacorp.springmongojwtapi.service.sse.AdminNotificationService;
import com.sashacorp.springmongojwtapi.service.sse.UserNotificationService;
import com.sashacorp.springmongojwtapi.util.TimeUtil;
import com.sashacorp.springmongojwtapi.util.http.HttpUtil;
import com.sashacorp.springmongojwtapi.util.http.Par;
import com.sashacorp.springmongojwtapi.util.http.Url;

/**
 * HR/Admin API endpoints for request management
 * 
 * @author matteo
 *
 */
@RestController
@CrossOrigin
public class MessageController {
	@Autowired
	UserNotificationService userNotificationService;
	@Autowired
	AdminNotificationService adminNotificationService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;

	/**
	 * If both parameters are undefined selects all messages. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param pending  - Optional.
	 * @param approved - Optional. if present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<List<Message>> getMessages(
			@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		List<Message> messages = null;
		if (approved != null) {
			messages = messageRepository.findByApproval(approved);
		} else if (pending != null) {
			messages = messageRepository.findByPending(pending);
		} else {
			messages = messageRepository.findAll();
		}
		return HttpUtil.getResponse(messages, HttpStatus.OK, requester);
	}

	/**
	 * Select a message by ID
	 * 
	 * @param messageId
	 * @return
	 */
	@RequestMapping(value = Url.MESSAGE_BY_MESSAGE_ID, method = RequestMethod.GET)
	public ResponseEntity<?> getMessageById(@PathVariable String messageId) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		if (messageRepository.existsById(messageId)) {
			return HttpUtil.getResponse(messageRepository.findById(messageId).orElse(null), HttpStatus.OK, requester);
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * If both parameters are undefined selects all current messages. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param pending  - Optional.
	 * @param approved - Optional. if present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.CURRENT_MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<List<Message>> getCurrentMessages(
			@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		List<Message> messages = null;
		if (approved != null) {
			messages = messageRepository.findCurrentByApproval(TimeUtil.now(), approved);
		} else if (pending != null) {
			messages = messageRepository.findCurrentByPending(TimeUtil.now(), pending);
		} else {
			messages = messageRepository.findCurrent(TimeUtil.now());
		}
		return HttpUtil.getResponse(messages, HttpStatus.OK, requester);

	}

	/**
	 * Selectes all ongoing messages. Refer to {@link MessageRepository} for
	 * definitions
	 * 
	 * @return
	 */
	@RequestMapping(value = Url.ONGOING_MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<List<Message>> getOngoingMessages() {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		return HttpUtil.getResponse(messageRepository.findOngoing(TimeUtil.now()), HttpStatus.OK, requester);
	}

	/**
	 * If both parameters are undefined selects all outdated messages. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param pending  - Optional.
	 * @param approved - Optional. if present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.OUTDATED_MESSAGES, method = RequestMethod.GET)
	public ResponseEntity<List<Message>> getOutdatedMessages(
			@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		List<Message> messages = null;
		if (approved != null) {
			messages = messageRepository.findOutdatedByApproval(TimeUtil.now(), approved);
		} else if (pending != null) {
			messages = messageRepository.findOutdatedByPending(TimeUtil.now(), pending);
		} else {
			messages = messageRepository.findOutdated(TimeUtil.now());
		}

		return HttpUtil.getResponse(messages, HttpStatus.OK, requester);
	}

	/**
	 * 
	 * If both parameters are undefined selects all messages matching the
	 * <b>username</b> path variable. Refer to {@link MessageRepository} for
	 * definitions
	 * 
	 * @param username
	 * @param pending  - Optional.
	 * @param approved - Optional. If present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.MESSAGES_BY_USERNAME, method = RequestMethod.GET)
	public ResponseEntity<?> getUserMessages(@PathVariable String username,
			@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
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
	 * 
	 * If both parameters are undefined selects all current messages matching the
	 * <b>username</b> path variable. Refer to {@link MessageRepository} for
	 * definitions
	 * 
	 * @param username
	 * @param pending  - Optional.
	 * @param approved - Optional. If present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.CURRENT_MESSAGES_BY_USERNAME, method = RequestMethod.GET)
	public ResponseEntity<?> getCurrentUserMessages(@PathVariable String username,
			@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
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
	 * 
	 * If both parameters are undefined selects all ongoing messages matching the
	 * <b>username</b> path variable. Refer to {@link MessageRepository} for
	 * definitions
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = Url.ONGOING_MESSAGES_BY_USERNAME, method = RequestMethod.GET)
	public ResponseEntity<?> getOngoingUserMessages(@PathVariable String username) {

		return HttpUtil.getResponse(messageRepository.findOngoingByUsername(username, TimeUtil.now()),
				HttpStatus.OK);

	}

	/**
	 * 
	 * If both parameters are undefined selects all outdated messages matching the
	 * <b>username</b> path variable. Refer to {@link MessageRepository} for
	 * definitions
	 * 
	 * @param username
	 * @param pending  - Optional.
	 * @param approved - Optional. If present, <b>pending</b> is set to <b>false</b>
	 * @return
	 */
	@RequestMapping(value = Url.OUTDATED_MESSAGES_BY_USERNAME, method = RequestMethod.GET)
	public ResponseEntity<?> getOutdatedUserMessages(@PathVariable String username,
			@RequestParam(name = Par.PENDING, required = false) Boolean pending,
			@RequestParam(name = Par.APPROVED, required = false) Boolean approved) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
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
	 * 
	 * Selects all messages with start or end between the </b>from</b> and <b>to</b>
	 * request parameters. Refer to {@link MessageRepository} for definitions and
	 * limit cases.
	 * 
	 * @param from - Required.
	 * @param to   - Required.
	 * @return
	 */
	@RequestMapping(value = Url.MESSAGES_BETWEEN, method = RequestMethod.GET)
	public ResponseEntity<?> getMessagesBetween(
			@RequestParam(name = Par.FROM, required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam(name = Par.TO, required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		return HttpUtil.getResponse(messageRepository.findApprovedBetween(from, to), HttpStatus.OK, requester);

	}

	/**
	 * 
	 * Selects all messages with start or end between the </b>from</b> and <b>to</b>
	 * request parameters matching the <b>username</b> path variable. Refer to
	 * {@link MessageRepository} for definitions and limit cases.
	 * 
	 * @param from - Required.
	 * @param to   - Required.
	 * @return
	 */
	@RequestMapping(value = Url.MESSAGES_BETWEEN_BY_USERNAME, method = RequestMethod.GET)
	public ResponseEntity<?> getUserMessagesBetween(@PathVariable String username,
			@RequestParam(name = Par.FROM, required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam(name = Par.TO, required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		return HttpUtil.getResponse(messageRepository.findApprovedBetweenByUsername(username, from, to),
				HttpStatus.OK, requester);

	}

	/**
	 * 
	 * Selects all messages with start or end between the </b>from</b> and <b>to</b>
	 * request parameters matching the <b>username</b> path variable. Refer to
	 * {@link MessageRepository} for definitions and limit cases.
	 * 
	 * @param from - Required.
	 * @param to   - Required.
	 * @return
	 */
	@RequestMapping(value = Url.MESSAGES_BETWEEN_BY_TEAM, method = RequestMethod.GET)
	public ResponseEntity<?> getTeamMessagesBetween(@PathVariable String team,
			@RequestParam(name = Par.FROM, required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam(name = Par.TO, required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User requester = userRepository.findByUsername(userDetails.getUsername());
		return HttpUtil.getResponse(messageRepository.findApprovedBetweenByTeam(team, from, to), HttpStatus.OK, requester);

	}

	/**
	 * Respond to a user's message. Users are notified via Server Sent Events, see
	 * {@link UserNotificationService}
	 * 
	 * @param messageId
	 * @param responseMsg - provide (bool) 'approved' and (string) 'responseNote' in
	 *                    JSON
	 * @return
	 */
	@RequestMapping(value = Url.MESSAGE_BY_MESSAGE_ID, method = RequestMethod.PUT)
	public ResponseEntity<?> respondToMessage(@PathVariable String messageId, @RequestBody Message responseMsg) {

		UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails == null) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		if (!userRepository.existsByUsername(userDetails.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.AUTH_USERNAME_NOT_FOUND.text(), HttpStatus.FORBIDDEN);
		}

		User responder = userRepository.findByUsername(userDetails.getUsername());
		responder.eraseCredentials();
		responder.eraseState();
		responder.eraseInfo();

		if (!messageRepository.existsById(messageId)) {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}

		Message requestMsg = messageRepository.findById(messageId).orElse(null);
		requestMsg.setPending(false);
		requestMsg.setApproved(responseMsg.getApproved());
		requestMsg.setResponded(TimeUtil.now());
		requestMsg.setResponder(responder);
		requestMsg.setResponseNote(responseMsg.getResponseNote());

		/*
		 * 'seen' is set to false as message status changed
		 */
		requestMsg.setSeen(false);
		Message updatedMsg = messageRepository.save(requestMsg);

		UserNotificationSse userNotificationEvent = new UserNotificationSse(Notification.PUT_ADMIN.text(),
				requestMsg.getRequester().getId(), requestMsg.getId(), updatedMsg);

		userNotificationService.getEventPublisher().publishEvent(userNotificationEvent);

		AdminNotificationSse adminNotificationEvent = new AdminNotificationSse(Notification.PUT_ADMIN.text(),
				requestMsg.getRequester().getId(), requestMsg.getRequester().getUsername(), requestMsg.getId(),
				updatedMsg);

		adminNotificationEvent.setAdminId(responder.getId());

		adminNotificationService.getEventPublisher().publishEvent(adminNotificationEvent);
		return HttpUtil.getResponse(updatedMsg, HttpStatus.OK, responder);

	}

	@RequestMapping(value = Url.MESSAGE_BY_MESSAGE_ID, method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMessage(@PathVariable String messageId) {
		messageRepository.deleteById(messageId);
		return HttpUtil.getHttpStatusResponse(HttpStatus.OK);
	}

}
