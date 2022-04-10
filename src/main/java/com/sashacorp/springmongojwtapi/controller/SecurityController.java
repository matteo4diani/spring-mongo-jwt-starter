package com.sashacorp.springmongojwtapi.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.http.Error;
import com.sashacorp.springmongojwtapi.models.http.auth.AuthenticationRequest;
import com.sashacorp.springmongojwtapi.models.http.auth.SignupRequest;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.security.Authority;
import com.sashacorp.springmongojwtapi.security.AuthorityUtil;
import com.sashacorp.springmongojwtapi.security.JwtUtil;
import com.sashacorp.springmongojwtapi.security.UserDetailsImpl;
import com.sashacorp.springmongojwtapi.security.UserDetailsServiceImpl;
import com.sashacorp.springmongojwtapi.util.http.HttpUtil;

/**
 * API endpoints for authentication and ('ADMIN' only) registration
 * 
 * @author matteo
 *
 */
@RestController
@CrossOrigin
public class SecurityController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private UserRepository userRepository;

	/**
	 * Public mapping for authentication. Returns a JSON containing a JSON Web
	 * Token, the user's username and their authorities
	 * 
	 * @param authenticationRequest - provide 'username' and 'password' in JSON
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception(Error.AUTH_WRONG_CREDENTIALS.text(), e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final User user = userRepository.findByUsername(userDetails.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return HttpUtil.getAuthenticationResponse(jwt, user.getUsername(), user.getAuthorities(), HttpStatus.OK);
	}

	/**
	 * ADMIN-only mapping for user registration. See {@link SignupRequest} for
	 * request structure.
	 * 
	 * @param signUpRequest
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return HttpUtil.getPlainTextResponse(Error.REG_USERNAME_EXISTS.text(), HttpStatus.BAD_REQUEST);
		}

		User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getUsername());
		User requester = null;
		Authority requesterAuthority;
		
		UserDetails requesterDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		requester = userRepository.findByUsername(requesterDetails.getUsername());

		requester.eraseCredentials();
		requesterAuthority = requester.getMaxAuthority();
		
		Set<Authority> authorities = AuthorityUtil.buildAuthorities(requesterAuthority, signUpRequest.getAuthorities());

		user.setAuthorities(authorities);
		User savedUser = userRepository.save(user);
		return HttpUtil.getResponse(savedUser, HttpStatus.OK, requester);
	}
	
	

}
