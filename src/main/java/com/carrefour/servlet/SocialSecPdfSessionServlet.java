package com.carrefour.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

@SuppressWarnings("serial")
public class SocialSecPdfSessionServlet extends HttpServlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("social security pdf session start...");
		String hidMsg = request.getParameter("hid_msg");
		User user1 = HrmUserVarify.getUser(request, response);
		HttpSession session = request.getSession();
        session.setAttribute("hidMsg" + user1 + "socialSec", hidMsg);
        response.getWriter().write("success");
        logger.info("social security pdf session end...");
	}
}