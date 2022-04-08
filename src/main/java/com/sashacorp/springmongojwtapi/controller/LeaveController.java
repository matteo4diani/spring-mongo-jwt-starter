package com.sashacorp.springmongojwtapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Leave;
import com.sashacorp.springmongojwtapi.repository.LeaveRepository;
import com.sashacorp.springmongojwtapi.util.HttpUtil;

/**
 * API endpoints to manage event (leaves) types
 * 
 * @author matteo
 *
 */
@RestController
@CrossOrigin
public class LeaveController {

	@Autowired
	LeaveRepository leaveRepository;

	/**
	 * Get all leave types. See {@link Leave}.
	 * 
	 * @return
	 */
	@RequestMapping({ "/leaves" })
	public ResponseEntity<List<Leave>> getAllLeaves() {
		return HttpUtil.getResponse(leaveRepository.findAll(), HttpStatus.OK);
	}

}
