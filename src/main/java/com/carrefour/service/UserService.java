package com.carrefour.service;

import java.util.List;
import java.util.Map;
import com.carrefour.entity.FieldDto;
import com.carrefour.entity.MobileDto;
import com.carrefour.entity.Salary;
import com.carrefour.entity.User;

public interface UserService {

	List<User> queryAll();
	
	Map<String, MobileDto> getMobileByUserId(int userid);
	
	List<Salary> queryMonth(int userid);
	
	Map<String, FieldDto> getfileCode(int userid);
	
	String getSubcompanyname(String bucode, String alcode);
}