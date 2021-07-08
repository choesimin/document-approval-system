package com.simin.approval.model.service;

import java.util.List;

import com.simin.approval.exception.NoDataFoundException;
import com.simin.approval.exception.UnsuitableDataException;
import com.simin.approval.model.common.PasswordSecureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simin.approval.model.dto.User;
import com.simin.approval.model.mapper.UserMapper;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordSecureManager passwordSecureManager;

	public String getName(int seq) {
		return userMapper.selectName(seq);
	}
	
	public List<User> list() {
		return userMapper.selectAll();
	}

	public void regist(User user) {
		int user_count_duplicated = userMapper.countById(user.getId());

		checkRegistValidation(user, user_count_duplicated);

		String password_secure = passwordSecureManager.getSecureData(user.getPassword());
		user.setPassword(password_secure);

		userMapper.insert(user);
	}

	public int login(User user) {
		String password_secure = passwordSecureManager.getSecureData(user.getPassword());
		user.setPassword(password_secure);

		List<User> user_list = userMapper.selectByIdAndPassword(user);
		List<User> user_list_id = userMapper.selectById(user.getId());

		if (user_list.size() < 1) {
			if (user_list_id.size() < 1) {
				throw new NoDataFoundException("ID is not found");
			}
			throw new NoDataFoundException("password is wrong");
		}

		return user_list.get(0).getSeq();
	}

	private void checkRegistValidation(User user, int user_count_duplicated) {
		if (user.getName().length() < 3 || user.getName().length() > 20)
			throw new UnsuitableDataException("name is out of length");
		if (user.getId().length() < 6 || user.getId().length() > 20) {
			throw new UnsuitableDataException("ID is out of length");
		}
		if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
			throw new UnsuitableDataException("password is out of length");
		}
		if (!user.getPassword().equals(user.getRepassword())) {
			throw new UnsuitableDataException("repassword is not equal with password");
		}
		if (user_count_duplicated > 0) {
			throw new UnsuitableDataException("ID is duplicated");
		}
	}
}
