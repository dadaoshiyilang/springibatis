package com.carrefour.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

@SuppressWarnings("serial")
public class TextPdfServlet extends HttpServlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		doGet(request, response);
    }
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("accumulation fund make pdf start...");
		String basePath = request.getSession().getServletContext().getRealPath("/");
		User user1 = HrmUserVarify.getUser(request, response);
		HttpSession session = request.getSession();
		String hidMsg = (String) session.getAttribute("hidMsg"+user1+"accumulation");
		session.removeAttribute("hidMsg" + user1 + "accumulation");
		logger.info("accumulation fund make pdf start params..."+hidMsg);
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		try {
			String path = basePath + "WEB-INF/classes/simsun.ttc";
			path = path.replace("/", "\\");
			fontResolver.addFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.setDocumentFromString(hidMsg);
			renderer.layout();
			OutputStream os = response.getOutputStream();
			response.setCharacterEncoding("UTF-8");
	        response.setHeader("Content-type", "application/pdf;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;");
			renderer.createPDF(os);
			os.close();
			logger.info("accumulation fund make pdf end...");
		} catch (DocumentException e) {
			logger.error("When calling accumulation fund make pdf exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		}
	}
}