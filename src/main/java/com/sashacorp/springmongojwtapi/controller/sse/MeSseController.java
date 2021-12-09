package com.sashacorp.springmongojwtapi.controller.sse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sashacorp.springmongojwtapi.models.http.sse.UserNotificationSse;

/**
 * API endpoint to send real-time notifications to users. Admin-generated
 * notifications are sent only to the interested user.
 * 
 * @author matteo
 *
 */
@Controller
@CrossOrigin
public class MeSseController {

	public ConcurrentHashMap<String, SseEmitter> getEmitters() {
		return emitters;
	}

	private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	/**
	 * Subscribe to a new event stream detailing new admin activity on your requests
	 * 
	 * @param userId
	 * @param response
	 * @return
	 */
	@GetMapping("/me/sse/{userId}")
	public SseEmitter handle(@PathVariable String userId, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store");

		SseEmitter emitter = new SseEmitter(600_000L);

		this.emitters.put(userId, emitter);

		emitter.onCompletion(() -> this.emitters.remove(userId));
		emitter.onTimeout(() -> this.emitters.remove(userId));

		return emitter;
	}

	/**
	 * 
	 * Event listener for user notifications. <br/>
	 * Broadcasts to the user matching the 'userId' in {@link UserNotificationSse} a server sent event with 'data':
	 * {...userNotification}. <br/>
	 * Checks for disconnected emitters and removes them from the emitter pool.
	 * 
	 * @param userNotification
	 */
	@EventListener
	public void onUserNotification(UserNotificationSse userNotification) {
		List<String> deadEmitters = new ArrayList<>();

		if (emitters.containsKey(userNotification.getUserId())) {

			try {
				emitters.get(userNotification.getUserId()).send(userNotification);
			} catch (Exception e) {
				deadEmitters.add(userNotification.getUserId());
			}

			deadEmitters.forEach(userId -> {
				this.emitters.remove(userId);
			});
		}
	}
}
