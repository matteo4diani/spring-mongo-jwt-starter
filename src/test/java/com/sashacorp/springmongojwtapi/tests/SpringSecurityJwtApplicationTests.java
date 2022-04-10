package com.sashacorp.springmongojwtapi.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sashacorp.springmongojwtapi.repository.LeaveRepository;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.service.sse.AdminNotificationService;
import com.sashacorp.springmongojwtapi.service.sse.UserNotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringSecurityJwtApplicationTests {
	
	@Autowired
	UserNotificationService userNotificationService;
	@Autowired
	AdminNotificationService adminNotificationService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	LeaveRepository leaveRepository;
	
	
	@Test
	public void contextLoads() {
		
	}

}
