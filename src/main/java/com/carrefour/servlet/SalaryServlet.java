package com.carrefour.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.carrefour.entity.MonthDto;
import com.carrefour.entity.Salary;
import com.carrefour.service.impl.UserServiceImpl;
import com.carrefour.utils.SpringContextUtils;
import net.sf.json.JSONArray;
import weaver.careefour.tool.CareefourUtil;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

@SuppressWarnings("serial")
public class SalaryServlet extends HttpServlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("salary month section query start...");
		String cxyf = CareefourUtil.getDate().substring(0, 7);
		try {
			cxyf = CareefourUtil.getdateold(cxyf,-1);
		} catch (Exception e) {
			logger.error("When calling month query interface, exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		}
		User user1 = HrmUserVarify.getUser(request , response);
		int userid = user1.getUID();
		UserServiceImpl userService = (UserServiceImpl)SpringContextUtils.getBean("userService");
		List<Salary> salarys = userService.queryMonth(userid);
		List<MonthDto> months = new ArrayList<MonthDto>();
		if (salarys != null && (!salarys.isEmpty())) {
			String month = salarys.get(0).getMonth();
			if(StringUtils.isNotEmpty(month)) {
				if (Integer.parseInt(month) >= 18) {
					months = getHalfYearMonth(18, true);
				} else {
					months = getHalfYearMonth(Integer.parseInt(month), true);
				}
			}
		}
		Collections.sort(months, new Comparator<MonthDto>() {
            @Override
            public int compare(MonthDto p1, MonthDto p2) {
                if(p1.getId() > p2.getId()){
                    return 0;
                } else {
                	return 1;
                }
            }
        });
		response.getWriter().write(JSONArray.fromObject(months).toString());
		logger.info("salary month section query end...");
	}
	
	public List<MonthDto> getHalfYearMonth(int num, boolean flag) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -num);
		String before_six = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH);
		List<MonthDto> result = new ArrayList<MonthDto>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();
		try {
			min.setTime(sdf.parse(before_six));
			min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
			max.setTime(sdf.parse(sdf.format(new Date())));
			int month = max.get(Calendar.MONTH);
			max.set(Calendar.MONTH, month-1);
		} catch (Exception e) {
			logger.error("When calling month calculation exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		}
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
		Calendar curr = min;
		int index = 0;
		while (curr.before(max)) {
			MonthDto month = new MonthDto();
			index = index + 1;
			month.setId(index);
			if (flag) {
				month.setMonth(sdf.format(curr.getTime()));
			} else {
				month.setMonth(curr.get(Calendar.MONTH) + 1 + "月");
			}
			curr.add(Calendar.MONTH, 1);
			result.add(month);
		}
		return result;
	}
}