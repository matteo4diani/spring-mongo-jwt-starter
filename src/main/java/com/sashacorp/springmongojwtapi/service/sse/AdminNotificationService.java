package com.sashacorp.springmongojwtapi.service.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Service providing access to Spring's {@link ApplicationEventPublisher} for admin notification use.
 * @author matteo
 *
 */
@Service
public class AdminNotificationService implements ApplicationEventPublisherAware {
	
	public ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public AdminNotificationService(ApplicationEventPublisher eventPublisher) {
		super();
		this.eventPublisher = eventPublisher;
	}

	public ApplicationEventPublisher getEventPublisher() {
		return eventPublisher;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
		
	}
}
