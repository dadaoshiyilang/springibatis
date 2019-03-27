package com.carrefour.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weaver.careefour.tool.AES_256;
import weaver.careefour.tool.CareefourUtil;

@SuppressWarnings("serial")
public class QueryAccumulationServlet extends HttpServlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("query accumulation servlet start ...");
		String gh = request.getParameter("gh");
		String s_year = request.getParameter("cxyear");
		String s_datetime =request.getParameter("cxdatetime");
	    String str = "";
	    String k = "askj178dhs9hdkc86shz9d7snb9ugs52";
		Service service = new Service();
		//String url = "https://esalary-tt1.cn.carrefour.com/InsuranceFundWS.asmx";//测试地址
		String url ="http://10.151.200.18:8080/InsuranceFundWS.asmx";
		//String url = "https://esalary.cn.carrefour.com/MonthlyPayrollWS.asmx";
		String op = "GetInsuranceFund"; //要调用的方法名
		try {
		   Calendar cale = Calendar.getInstance();
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   String date = CareefourUtil.getDate();
		   String time = CareefourUtil.getTime();
		   cale.setTime(sdf.parse(date+" "+time));
		   long currentTime=cale.getTimeInMillis();
		   String timestep=currentTime+"";
		   timestep=timestep.substring(0,timestep.length()-3);
		   Call call = (Call) service.createCall();
		   call.setTargetEndpointAddress(url);  
		   call.setUseSOAPAction(true); 
		   String actionUri = "http://tempuri.org/GetInsuranceFund"; //Action路径  
		   call.setSOAPActionURI(actionUri); // action uri      
		   call.addParameter(new QName("http://tempuri.org/","employeeid"), org.apache.axis.encoding.XMLType.XSD_STRING,   
		   javax.xml.rpc.ParameterMode.IN);//接口的参数   
		   call.addParameter(new QName("http://tempuri.org/","year"), org.apache.axis.encoding.XMLType.XSD_STRING,   
		   javax.xml.rpc.ParameterMode.IN);//接口的参数   
		   call.addParameter(new QName("http://tempuri.org/","timestamp"), org.apache.axis.encoding.XMLType.XSD_STRING,   
		   javax.xml.rpc.ParameterMode.IN);//接口的参数  
		   call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//设置返回类型    
		   call.setOperationName(new QName("http://tempuri.org/",op));//WSDL里面描述的接口名称   
		   str = (String)call.invoke(new Object[]{gh,s_year,timestep});
		} catch (Exception e) {
			logger.error("When calling query accumulation interface, exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		}	
		byte[] xmlb = null;
		String xml="";
		try {
			xmlb = AES_256.decrypt(str, k);
			if(xmlb!=null){
				xml=new String(xmlb,"utf-8");
			}
			xml=xml.replace("&", " and ");
			xml=xml.replace("<![CDATA[", "");
			xml=xml.replace("]]>", "");
		} catch (Exception e1) {
			logger.error("When calling query accumulation AES_256 decrypt interface, exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e1));
		}  

		logger.info("报文返回信息pwstrxml==="+xml);
	   	JSONArray jsonArray = new JSONArray();
		Document doc = null;		
		try {
			Map mxmap = new HashMap();
			Map gzmapname = new LinkedHashMap();
			Map gzmapvalue = new LinkedHashMap();
			// 将字符串转为XML
			doc = DocumentHelper.parseText(xml); 
			// 获取根节点
			Element rootElt = doc.getRootElement(); 
			Iterator iter = rootElt.elementIterator("header"); // 获取根节点下的子节点head
	        // 遍历head节点
	        while (iter.hasNext()) {
	  	  		Element recordEle = (Element) iter.next();
				String ver = recordEle.elementTextTrim("ver"); // 拿到head节点下的子节点title值
	            Iterator iters = recordEle.elementIterator("msg"); // 获取子节点head下的子节点script
	            // 遍历Header节点下的Response节点
	            while (iters.hasNext()) {
	                Element itemEle = (Element) iters.next();
	                String msgEmployeeid = itemEle.elementTextTrim("msgEmployeeid"); 
	                String msgYear = itemEle.elementTextTrim("msgYear");
	                String msgTime = itemEle.elementTextTrim("msgTime"); 
	                String msgSuccess = itemEle.elementTextTrim("msgSuccess"); 
	                String msgError = itemEle.elementTextTrim("msgError");	
	                if(msgSuccess.equals("N")){
	                	mxmap.put("error", msgError);
	                	break;
	                }else{
	                	mxmap.put("error", "true");
						mxmap.put("msgYear", msgYear);
	                }
	            }
	          }
	          if(mxmap.get("error").equals("true")){
	          	 Iterator iter1 = rootElt.elementIterator("body"); // 获取根节点下的子节点head
	    	     while (iter1.hasNext()) {
	              	Element recordEle = (Element) iter1.next();
	             	Iterator iters = recordEle.elementIterator("response"); // 获取子节点head下的子节点script
	             	while (iters.hasNext()) {
						Element itemEle = (Element) iters.next();
	              		String name = itemEle.elementTextTrim("name"); 
						String averagesalary = itemEle.elementTextTrim("averagesalary");
						String inspara = itemEle.elementTextTrim("inspara"); 
						String fundpara = itemEle.elementTextTrim("fundpara"); 
						String dept = itemEle.elementTextTrim("deptname");	
						String idcode = itemEle.elementTextTrim("idcode");
						mxmap.put("error", "true");
						mxmap.put("name", name);
						mxmap.put("averagesalary", averagesalary);
						mxmap.put("inspara", inspara);
						mxmap.put("fundpara", fundpara);
						mxmap.put("dept", dept);
						mxmap.put("idcode", idcode);
						StringBuffer html = new StringBuffer(); 
						html.append("<html>")
							.append("<head>")
			                .append("<meta http-equiv=\"Content-Type\" content=\"text/html\" charset=\"UTF-8\"></meta>") 
			                .append("<style type=\"text/css\">")
			                .append("body {")
			                .append("font-family:SimSun; }")
			                .append(".thin { background:#000000; }")
			                .append(".thin td { background:#FFFFFF; text-align:center; }")
			                .append(" @page{size:A4 portrait;}")
			                .append("</style>")
			                .append("</head>")
			                .append("<body>")
							.append("<div align=\"center\" id=\"div4\" style=\"position:relative;margin-top:50px;display: block\" >")
							.append("<div id=\"GjjTitle\" style=\"position:relative;margin-bottom:20px;\"><font size=\"5\">")
							.append("Y"+(Integer.parseInt(s_year)+1)+" Housing Found Base Confirm</font></div>")
							.append("<table id=\"tbGjj\" style=\"width:100%;\" border=\"0\" cellspacing=\"1\" cellpadding=\"0\" class=\"thin\">")
							.append("<tr><td>EmployeeName</td><td>DeptName</td><td>ID Code</td><td id=\"GjjTitleAVIncomeEg\">")
							.append("Y"+(Integer.parseInt(s_year))+"_Average Monthly Salary")
							.append("</td><td id=\"GjjTitlebaseEg\">")
							.append("Y"+(Integer.parseInt(s_year)+1)+"_Housing Fund Base")
							.append("</td><td>Confirm by Employee</td></tr>")
							.append("<tr><td>姓名</td><td>部门</td><td>身份证号码</td><td id=\"GjjTitleAVIncomeCh\">")
							.append((Integer.parseInt(s_year))+"年月平均收入")
							.append("</td><td id=\"GjjTitlebaseCh\">")
							.append((Integer.parseInt(s_year)+1)+"_年社公积金数")
							.append("</td><td>员工确认</td></tr>")
							.append("<tr><td class=\"name\">"
									+name+"</td><td class=\"dept\">"
									+dept+"</td><td class=\"idcode\">"
									+idcode+"</td><td class=\"incomeAvg\">"
									+averagesalary+ "</td><td id=\"Gjj\">"
									+inspara+"</td>"
									+"<td class=\"emp\"></td></tr>")
						    .append("</table>").append("</div>");
						html.append("</body></html>");
						mxmap.put("hidmsg", html.toString());
						logger.info("when call query accumalation html made is..."+html.toString());
	             	 }
	         	}			
		}
		jsonArray.put(mxmap);
	} catch (Exception e) {
		logger.error("When calling query accumulation xml analysis interface, exception is thrown");
		logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		Map mxmap = new HashMap();
		mxmap.put("error", e.toString());
		jsonArray.put(mxmap);
    }
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonArray.toString());
		logger.info("query accumulation servlet end ...");
	}
}