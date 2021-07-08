package com.simin.approval.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.simin.approval.model.dto.User;

@Mapper
public interface UserMapper {
	String selectName(int seq);
	List<User> selectAll();
	int countById(String id);
	List<User> selectById(String id);
	List<User> selectByIdAndPassword(User user);
	int insert(User user);
}
