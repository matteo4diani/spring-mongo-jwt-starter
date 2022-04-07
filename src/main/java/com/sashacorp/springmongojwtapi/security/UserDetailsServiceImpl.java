package com.sashacorp.springmongojwtapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.UserRepository;

/**
 * Custom implementation of Spring's {@link UserDetailsService} interface.
 * Exposes the {@link UserRepository} service converting a {@link User} to a
 * Spring-compliant {@link UserDetailsImpl}
 * 
 * @author matteo
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	public UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		return UserDetailsImpl.build(user);

	}
}