package com.obss.jss.project13_1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obss.jss.project13_1.model.LoginRequest;
import com.obss.jss.project13_1.model.LoginResponse;
import com.obss.jss.project13_1.service.UserService;
import com.obss.jss.project13_1.util.JwtTokenUtil;
import com.obss.jss.project13_1.util.ResponseMessage;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" })
@RequestMapping("/authenticate")
public class AuthenticateController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
		if (loginRequest == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Username or password are cannot be empty"),
					HttpStatus.BAD_REQUEST);

		authenticate(loginRequest.getUsername(), loginRequest.getPassword());

		UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return new ResponseEntity<LoginResponse>(new LoginResponse(token), HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
