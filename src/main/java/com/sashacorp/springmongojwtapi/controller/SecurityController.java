package com.sashacorp.springmongojwtapi.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.http.MessageResponse;
import com.sashacorp.springmongojwtapi.models.http.auth.AuthenticationRequest;
import com.sashacorp.springmongojwtapi.models.http.auth.AuthenticationResponse;
import com.sashacorp.springmongojwtapi.models.http.auth.SignupRequest;
import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.security.UserDetailsServiceImpl;
import com.sashacorp.springmongojwtapi.util.JwtUtil;

/**
 * API endpoints for authentication and ('ADMIN' only) registration
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
	 * Public mapping for authentication. Returns a JSON containing a JSON Web Token, the user's username
	 * and their authorities
	 * @param authenticationRequest - provide 'username' and 'password' in JSON
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		
		final User user = userRepository.findByUsername(userDetails.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getUsername(), user.getAuthorities()));
	}
	
	/**
	 * ADMIN-only mapping for user registration. See {@link SignupRequest} for request structure.
	 * @param signUpRequest
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 encoder.encode(signUpRequest.getPassword()),
							 signUpRequest.getUsername()); // We set email = username until further notice

		Set<String> strAuthority = signUpRequest.getAuthorities();
		Set<Authority> authorities = new HashSet<>();

		if (strAuthority == null) {
			Authority userAuthority = new Authority("USER");
				authorities.add(userAuthority);
		} else {
			strAuthority.forEach(authority -> {
				switch (authority) {
					case "ADMIN":
						Authority adminAuthority = new Authority("ADMIN");
						authorities.add(adminAuthority);
	
						break;
						
					case "MANAGER":
						Authority managerAuthority = new Authority("MANAGER");
						authorities.add(managerAuthority);
						
						break;
						
					case "HR":
						Authority hrAuthority = new Authority("HR");
						authorities.add(hrAuthority);
						
						break;
					
					default:
						Authority userAuthority = new Authority("USER");
						authorities.add(userAuthority);
					}
			});
		}

		user.setAuthorities(authorities);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	
}
