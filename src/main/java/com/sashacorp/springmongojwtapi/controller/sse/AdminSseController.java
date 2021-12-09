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

import com.sashacorp.springmongojwtapi.models.http.sse.AdminNotificationSse;

/**
 * API endpoint to send real-time notifications to admins.
 * User-generated notifications are broadcasted to all admins.
 * @author matteo
 *
 */
@Controller
@CrossOrigin
public class AdminSseController {
	public ConcurrentHashMap<String, SseEmitter> getEmitters() {
		return emitters;
	}

	private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	
	/**
	 * Subscribe to an event stream detailing new user activity
	 * @param adminId
	 * @param response
	 * @return
	 */
	@GetMapping("/admin/sse/{adminId}")
	public SseEmitter handle(@PathVariable String adminId, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store");

		SseEmitter emitter = new SseEmitter(600_000L);

		this.emitters.put(adminId, emitter);

		emitter.onCompletion(() -> this.emitters.remove(adminId));
		emitter.onTimeout(() -> this.emitters.remove(adminId));

		return emitter;
	}
	
	/**
	 * Event listener for admin notifications.
	 * <br/>Broadcasts to all subscribed admins a server sent event with 'data': {...adminNotification}.
	 * <br/>Checks for disconnected emitters and removes them from the emitter pool.
	 * @param adminNotification
	 */
	@EventListener
	public void onAdminNotification(AdminNotificationSse adminNotification) {
		List<String> deadEmitters = new ArrayList<>();

		

		emitters.keySet().forEach(adminId -> {
			try {
					adminNotification.setAdminId(adminId);
					emitters.get(adminId).send(adminNotification);
			} catch (Exception e) {
				deadEmitters.add(adminNotification.getAdminId());
			}
		});

		deadEmitters.forEach(userId -> {
			this.emitters.remove(userId);
		});
		
	}
}
