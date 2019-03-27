package com.carrefour.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.carrefour.dao.UserDao;
import com.carrefour.entity.FieldDto;
import com.carrefour.entity.MobileDto;
import com.carrefour.entity.Salary;
import com.carrefour.entity.User;

public class UserDaoImpl extends SqlMapClientDaoSupport implements UserDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryAll() {
		List<User> roles = getSqlMapClientTemplate().queryForList("queryAll", null);  
        return roles;  
	}

	@Override
	public Map<String, MobileDto> getMobileByUserId(int userId) {
		Map<String, MobileDto> resultObj = getSqlMapClientTemplate().queryForMap("getMobile", userId,"id");
		return resultObj;
	}

	@Override
	public List<Salary> queryMonth(int userid) {
		List<Salary> salarys = getSqlMapClientTemplate().queryForList("queryMonth", userid);  
        return salarys;
	}

	@Override
	public Map<String, FieldDto> getfileCode(int userid) {
		Map<String, FieldDto> resultObj = getSqlMapClientTemplate().queryForMap("getfileCode", userid,"id");
		return resultObj;
	}

	@Override
	public String getSubcompanyname(String bucode, String alcode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bucode", bucode);
		params.put("alcode", alcode);
		Object resultObj = getSqlMapClientTemplate().queryForObject("getSubcompanyname", params);
		return resultObj !=null?resultObj.toString():"";
	}
}