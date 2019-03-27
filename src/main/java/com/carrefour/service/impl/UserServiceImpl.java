package com.carrefour.service.impl;

import java.util.List;
import java.util.Map;
import com.carrefour.dao.UserDao;
import com.carrefour.dao.impl.UserDaoImpl;
import com.carrefour.entity.FieldDto;
import com.carrefour.entity.MobileDto;
import com.carrefour.entity.Salary;
import com.carrefour.entity.User;
import com.carrefour.service.UserService;
import com.carrefour.utils.SpringContextUtils;

public class UserServiceImpl implements UserService {
	
	@Override
	public List<User> queryAll() {
		UserDaoImpl userDao = (UserDaoImpl)SpringContextUtils.getBean("userDAO");
		return userDao.queryAll();
	}

	@Override
	public Map<String, MobileDto> getMobileByUserId(int userid) {
		UserDaoImpl userDao = (UserDaoImpl)SpringContextUtils.getBean("userDAO");
		Map<String, MobileDto> res = userDao.getMobileByUserId(userid);
		return res;
	}

	@Override
	public List<Salary> queryMonth(int userid) {
		UserDaoImpl userDao = (UserDaoImpl)SpringContextUtils.getBean("userDAO");
		return userDao.queryMonth(userid);
	}

	@Override
	public Map<String, FieldDto> getfileCode(int userid) {
		UserDaoImpl userDao = (UserDaoImpl)SpringContextUtils.getBean("userDAO");
		Map<String, FieldDto> resultMap = userDao.getfileCode(userid);
		return resultMap;
	}

	@Override
	public String getSubcompanyname(String bucode, String alcode) {
		UserDaoImpl userDao = (UserDaoImpl)SpringContextUtils.getBean("userDAO");
		return userDao.getSubcompanyname(bucode, alcode);
	}
	
	private UserDao userDAO;

	public UserDao getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDao userDAO) {
		this.userDAO = userDAO;
	}
}