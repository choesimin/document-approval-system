package com.simin.approval;

import com.simin.approval.model.common.JwtManager;
import com.simin.approval.model.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtManagerTest {

	private JwtManager jwtManager;

	@BeforeEach
	void setUp() {
		jwtManager = new JwtManager();
	}

	@Test
	void contextLoads() {
	    User user = new User();
	    user.setSeq(1);

	    String token = jwtManager.generateJwtToken(user);

	    int seqFromToken = jwtManager.getSeqFromToken(token);

	    System.out.println(seqFromToken);
	}

}
