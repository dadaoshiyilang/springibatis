package com.carrefour.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weaver.careefour.tool.CareefourUtil;
import weaver.general.Util;

@SuppressWarnings("serial")
public class MonthSectionServlet extends HttpServlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("query month section start ...");
		String cxyf = request.getParameter("cxyf");
		String gh = Util.null2String(request.getParameter("gh"));
		String month_day="";
		try {
			if(gh!=null&&gh.length()>5&&(gh.substring(0,5).equals("CHNHO")||gh.substring(0,5).equals("DCSHV"))){
				month_day=CareefourUtil.getdate(cxyf,0);
			} else {
				month_day=CareefourUtil.getdatenew(cxyf,-1)+"26-"+CareefourUtil.getdatenew(cxyf,0)+"25";
			}
		} catch (Exception e) {
			logger.error("When calling month section query interface, exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		}
		logger.info("query month section cxyf is ："+cxyf);
		logger.info("query month section month_day is："+month_day);
		response.getWriter().write(month_day);
    }
}