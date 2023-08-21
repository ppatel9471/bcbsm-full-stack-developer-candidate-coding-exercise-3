package com.example.feedback.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.feedback.dto.FeedbackDTO;
import com.example.feedback.dto.ResponseDTO;
import com.example.feedback.dto.UserDTO;
import com.example.feedback.models.Feedback;
import com.example.feedback.models.User;
import com.example.feedback.repository.FeedbackRepository;
import com.example.feedback.repository.UserRepository;
import com.example.feedback.security.TokenResponse;
import com.example.feedback.security.JwtTokenFilter;
import com.example.feedback.security.JwtTokenUtil;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired 
	private FeedbackRepository feedbackRepo;
	
	@Autowired 
	AuthenticationManager authManager;
    
	@Autowired 
    JwtTokenUtil jwtUtil;
    
	@Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtTokenFilter jwtTokenFilter;
	
	@PostMapping("/signup")
	public ResponseEntity signup(@RequestBody UserDTO userDTO) {
	
		User user = new User(userDTO.getUsername(), encoder.encode(userDTO.getPassword()), userDTO.getName(), userDTO.isAdmin());
		if(userRepo.findByUsername(userDTO.getUsername()).orElse(null) != null) {
			return ResponseEntity.status(400).body(generateResponseObject(false,"User Already exist", null));
		}
		try{
			userRepo.save(user);
			return ResponseEntity.ok().body(generateResponseObject(true,"User signup successfully", user));
		}catch(Exception e) {
			return ResponseEntity.status(400).body(generateResponseObject(false,"User signup failed", null));
		}	
	}
	
	@PostMapping("/api/user/login")
	public ResponseEntity login(@RequestBody UserDTO userDTO) {
		try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		userDTO.getUsername(), userDTO.getPassword())
            );
             
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(user);
            TokenResponse response = new TokenResponse(accessToken);
             
            return ResponseEntity.ok().body(generateResponseObject(true, "Login successfully", response));
             
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(generateResponseObject(false,"Credential is not valid", null));
        }
	}
	
	@GetMapping("/api/user/logout")
	public ResponseEntity logout(HttpServletRequest request) {
		String token= null;
		final String authorizationHeaderValue = request.getHeader("Authorization");
		if(authorizationHeaderValue == null)
			return ResponseEntity.status(400).body(generateResponseObject(false, "Authorization should not be null", null));
	    if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
	      token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
	    }
		User user = (User)jwtTokenFilter.getUserDetails(token);
		String accessToken = jwtUtil.refereshAccessToken(user);
		TokenResponse response = new TokenResponse(accessToken);
		return ResponseEntity.ok().body(generateResponseObject(true, "Logout successfully", response));
	}
	
	
	@GetMapping("/api/user/getFeedback")
	public ResponseEntity getFeedback(HttpServletRequest request) {
		String token= null;
		final String authorizationHeaderValue = request.getHeader("Authorization");
		if(authorizationHeaderValue == null)
			return ResponseEntity.status(400).body(generateResponseObject(false, "Authorization should not be null", null));
	    if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
	      token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
	    }
		User user = (User)jwtTokenFilter.getUserDetails(token);
		String userid=user.getId();
		Boolean isAdmin=user.isAdmin();
		
		List<Feedback> feedbacks;
		if(isAdmin)
		{
			feedbacks = feedbackRepo.findAll();
		}
		else
		{
			feedbacks= feedbackRepo.findFeedbackByUserId(userid);
		}
		return ResponseEntity.ok().body(generateResponseObject(true, "Successfully got feedback of user", feedbacks));
	}
	
	
	@PostMapping("/api/user/submitFeedback")
	public ResponseEntity addFeedback(HttpServletRequest request,@RequestBody FeedbackDTO feedbackDTO) {
		String token = null;
		final String authorizationHeaderValue = request.getHeader("Authorization");
		if(authorizationHeaderValue == null)
			return ResponseEntity.status(400).body(generateResponseObject(false, "Authorization should not be null", null));
	    if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
	      token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
	    }
		User user = (User)jwtTokenFilter.getUserDetails(token);
		String userid=user.getId();
		
		Feedback feedback = new Feedback(feedbackDTO.getRating_value(), feedbackDTO.getComment(), userid, new Date());
		feedbackRepo.save(feedback);
		return ResponseEntity.ok().body(generateResponseObject(true, "Successfully submitted feedback of user", feedback));
	}
	
	public ResponseDTO generateResponseObject(Boolean status, String message, Object data) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(status);
		responseDTO.setMessage(message);
		responseDTO.setData(data);
		return responseDTO;
	}
}
