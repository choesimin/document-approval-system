package com.simin.approval.controller;

import com.simin.approval.model.common.JwtManager;
import com.simin.approval.model.common.Status;
import com.simin.approval.model.dto.ResponseData;
import com.simin.approval.model.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.simin.approval.model.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtManager jwtManager;

	@GetMapping("/list")
	public ResponseData list() {
		List<User> list_user = userService.list();

		ResponseData responseData = new ResponseData();
		responseData.setStatus(Status.SUCCESS);
		responseData.setData1(list_user);

		return responseData;
	}
	
    @PostMapping("/regist")
	public ResponseData regist(@RequestBody User user) {
		userService.regist(user);

		ResponseData responseData = new ResponseData();
		responseData.setStatus(Status.SUCCESS);
    	responseData.setMessage("regist success");

		return responseData;
	}
	
	@PostMapping("/login")
	public ResponseData login(@RequestBody User user) {
		int user_seq = userService.login(user);

		User user_token = new User();
		user_token.setSeq(user_seq);

		String token = jwtManager.generateJwtToken(user_token);

		ResponseData responseData = new ResponseData();
		responseData.setStatus(Status.SUCCESS);
		responseData.setToken(token);
		responseData.setMessage("login success");

		return responseData;
	}

	@GetMapping("/name")
	public ResponseData name(@RequestHeader String token) {
		int seq = jwtManager.getSeqFromToken(token);
		String user_name = userService.getName(seq);

		ResponseData responseData = new ResponseData();
		responseData.setStatus(Status.SUCCESS);
		responseData.setData1(user_name);

		return responseData;
	}

}