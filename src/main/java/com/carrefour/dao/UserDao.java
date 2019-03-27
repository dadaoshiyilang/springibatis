package com.carrefour.dao;

import java.util.List;
import java.util.Map;

import com.carrefour.entity.Salary;
import com.carrefour.entity.User;

public interface UserDao {

	List<User> queryAll();
	
	Map getMobileByUserId(int userid);
	
	List<Salary> queryMonth(int userid);
	
	Map getfileCode(int userid);
	
	String getSubcompanyname(String bucode, String alcode);
}