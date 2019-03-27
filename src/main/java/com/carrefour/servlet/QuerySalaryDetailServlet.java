package com.carrefour.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
import weaver.careefour.tool.CareefourUtil;
import weaver.general.Util;
import weaver.careefour.tool.AES_256;

@SuppressWarnings("serial")
public class QuerySalaryDetailServlet extends HttpServlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("salary Detail query start...");
		String gh = Util.null2String(request.getParameter("gh"));
	    String yf = Util.null2String(request.getParameter("yf"));
	    String language1 = Util.null2String(request.getParameter("yy"));
		String selValue = Util.null2String(request.getParameter("selValue"));
		
		logger.info("salary Detail query start..gh." +gh);
		logger.info("salary Detail query start..yf." +yf);
		logger.info("salary Detail query start...language1." +language1);
		logger.info("salary Detail query start..selValue."+selValue);
		
	    String str="";
	    String k = "askj178dhs9hdkc86shz9d7snb9ugs52";
		Service service = new Service();
		//String url = "https://esalary-tt1.cn.carrefour.com/monthlyPayrollws.asmx";//测试地址
		String url ="http://10.151.200.18:8080/MonthlyPayrollWS.asmx";
		//String url = "https://esalary.cn.carrefour.com/MonthlyPayrollWS.asmx";
		//String url = "http://10.151.195.26:8080/MonthlyPayrollWS.asmx";
		String op = "GetMonthlyPayroll"; //要调用的方法名
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
		   String actionUri = "http://tempuri.org/GetMonthlyPayroll"; //Action路径  
		   call.setSOAPActionURI(actionUri); // action uri      
		   call.addParameter(new QName("http://tempuri.org/","employeeid"), org.apache.axis.encoding.XMLType.XSD_STRING,   
		   javax.xml.rpc.ParameterMode.IN);//接口的参数   
		   call.addParameter(new QName("http://tempuri.org/","yearmonth"), org.apache.axis.encoding.XMLType.XSD_STRING,   
		   javax.xml.rpc.ParameterMode.IN);//接口的参数   
		   call.addParameter(new QName("http://tempuri.org/","language"), org.apache.axis.encoding.XMLType.XSD_STRING,   
		   javax.xml.rpc.ParameterMode.IN);//接口的参数   
		   call.addParameter(new QName("http://tempuri.org/","timestamp"), org.apache.axis.encoding.XMLType.XSD_STRING,   
		   javax.xml.rpc.ParameterMode.IN);//接口的参数  
		   call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//设置返回类型    
		   call.setOperationName(new QName("http://tempuri.org/",op));//WSDL里面描述的接口名称
		   logger.info("GetMonthlyPayroll Start...");
		   str = (String)call.invoke(new Object[]{gh,selValue,language1,timestep});
		   logger.info("GetMonthlyPayroll Running...");
		   //str = (String)call.invoke(new Object[]{"CEWHV21139","2017-11","CN",timestep});
		} catch (Exception e) {
			logger.error("When calling salary query interface, exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		}
		byte[] xmlb=null;
		String xml="";
		try {
			xmlb = AES_256.decrypt(str, k);  
			if(xmlb!=null){
				xml=new String(xmlb,"utf-8");
			}
			xml=xml.replace("&", " and ");
			xml=xml.replace("<![CDATA[", "");
			xml=xml.replace("]]>", "");
			logger.info("pwstrxml==="+xml);
		} catch (Exception e) {
			logger.error("When calling salary query AES_256 Decrypt interface, exception is thrown");
			logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		}

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
	                String msgYearmonth = itemEle.elementTextTrim("msgYearmonth");
	                String msgTime = itemEle.elementTextTrim("msgTime"); 
	                String msgLanguage = itemEle.elementTextTrim("msgLanguage");
	                String msgSuccess = itemEle.elementTextTrim("msgSuccess"); 
	                String msgError = itemEle.elementTextTrim("msgError");	
	                if(msgSuccess.equals("N")){
	                	mxmap.put("error", msgError);
	                	break;
	                }else{
	                	mxmap.put("error", "true");
	                }
	            }
	          }
	          if(mxmap.get("error").equals("true")){
	          	 Iterator iter1 = rootElt.elementIterator("body"); // 获取根节点下的子节点head
	    	     while (iter1.hasNext()) {
	              	Element recordEle = (Element) iter1.next();
	             	Iterator iters = recordEle.elementIterator("response"); // 获取子节点head下的子节点script
	             	while (iters.hasNext()) {
	              		Element recordEle2 = (Element) iters.next();
	              	 	Iterator iters2 = recordEle2.elementIterator("column");
		              	 while (iters2.hasNext()) {
		              		 Element itemEle = (Element) iters2.next();
		              		 List<Element> listElement=itemEle.elements();//所有一级子节点的list  
		              		 for(int i=0;i<listElement.size();i++){
		              		 	gzmapname.put(listElement.get(i).getName(), listElement.get(i).getTextTrim());
		              		 }
		              	 }              	 
	              	 	 Iterator iters3 = recordEle2.elementIterator("paydata");
		              	 while (iters3.hasNext()) {
		              		 Element itemEle = (Element) iters3.next();
		              		 List<Element> listElement=itemEle.elements();//所有一级子节点的list  
		              		 for(int i=0;i<listElement.size();i++){
		              		 	String name=listElement.get(i).getName();
		              		 	String value=listElement.get(i).getTextTrim();
		              		 	if(value.equals("0")&&(name.equals("A1")||name.equals("A2")||name.equals("A3")||name.equals("A4")||name.equals("A5")||name.equals("A6")||name.equals("A39")||name.equals("A40")||name.equals("A42RMB")||name.equals("A52RMB")||name.equals("A53RMB"))){
		              		 		value="";
		              		 		gzmapvalue.put(name,value);
		              		 	}else if(value.equals("0")){
			              		 	if(name.equals("A52RMB")||name.equals("A53RMB")){
			              		 		name=name.substring(0, 3);
			              		 	}
		              		 		gzmapname.remove(name);
		              		 		//gzmapvalue.put(name,value);
		              		 	}else{
		              		 		gzmapvalue.put(name,value);
		              		 	}
		              		 }
		              	 }		
	             	 }
	         	}	
		        String html="<td valign='top'>";
		        html=html+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
		        html=html+"<tr>";
		        html=html+"<td width='35%' class='y'>";	
		        html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";			
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A1")+"</span></p>";	
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A2")+"</span></p>";
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A3")+"</span></p>";	
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";		
			   	html=html+"</td>";
				html=html+"<td width='15%' class='x'>";
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";				
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A1")+"</span></p>";	
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A2")+"</span></p>";
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A3")+"</span></p>";
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";			
		   		html=html+"</td>";	
		   		html=html+"<td width='35%'class='y'>";    	
		   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";			
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A4")+"</span></p>";	
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A5")+"</span></p>";
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A6")+"</span></p>";	
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";		
		   		html=html+"</td>";	
				html=html+"<td width='15%' class='x'>"; 
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";	   			
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A4")+"</span></p>";	
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A5")+"</span></p>";
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A6")+"</span></p>";
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";			
		   		html=html+"</td>";	
			   	gzmapname.remove("A1");
			   	gzmapvalue.remove("A1");
				gzmapname.remove("A2");
			   	gzmapvalue.remove("A2");
			    gzmapname.remove("A3");
			   	gzmapvalue.remove("A3");
			   	gzmapname.remove("A4");
			   	gzmapvalue.remove("A4");
			   	gzmapname.remove("A5");
			   	gzmapvalue.remove("A5");
			   	gzmapname.remove("A6");
			   	gzmapvalue.remove("A6");
			   	html=html+"</tr>";
			    html=html+"</table>";			
		    	html=html+"<br/>";
				String html1="";
				String html2="";
				String html3="";
		        int num=0;
				for (Iterator<String> i = gzmapname.keySet().iterator(); i.hasNext(); ) {
					String key = i.next();
					if(key.substring(0, 1).equals("B")){
					if(num==0){
					    html=html+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
				        html=html+"<tr>";
				        html=html+"<td width='35%' class='y'>";	
				        html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				        html1="<td width='35%' class='y'>";
				        html1=html1+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				        html2="<td width='15%' class='x'>";
				        html2=html2+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				        html3="<td width='15%' class='x'>";
				        html3=html3+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
					}
					if(num==0){
						html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html2=html2+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					if(num==1){
					    html1=html1+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html3=html3+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					if(num%2==0&&num!=0){
						html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html2=html2+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					if(num%2==1&&num!=1){
					    html1=html1+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html3=html3+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					num++;
					}
					}	
					if(num%2==1&&num!=0){
						html1=html1+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>&nbsp;&nbsp;&nbsp;&nbsp;</span></p>";
						html3=html3+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>&nbsp;&nbsp;&nbsp;&nbsp;</span></p>";
					}
					if(num!=0){
						html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
						html=html+"</td>";
						html1=html1+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
						html1=html1+"</td>";
						html2=html2+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
						html2=html2+"</td>";
						html3=html3+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
						html3=html3+"</td>";
						html=html+html2;
						html=html+html1;
						html=html+html3;
						html=html+"</tr>";
						html=html+"</table>";
						html=html+"<br/>";	
					}		
					String html4="";
					String html5="";
					String html6="";
			        num=0;
	        		for (Iterator<String> i = gzmapname.keySet().iterator(); i.hasNext(); ) {
					String key = i.next();
					if(key.substring(0, 1).equals("C")){
					if(num==0){
						html=html+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
				        html=html+"<tr>";
				        html=html+"<td width='35%' class='y'>";	
				        html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				        html4="<td width='35%' class='y'>";
				        html4=html4+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				        html5="<td width='15%' class='x'>";
				        html5=html5+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				        html6="<td width='15%' class='x'>";
				        html6=html6+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
					}
					if(num==0){
						html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html5=html5+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					if(num==1){
					    html4=html4+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html6=html6+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					if(num%2==0&&num!=0){
						html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html5=html5+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					if(num%2==1&&num!=1){
					    html4=html4+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get(key)+"</span></p>";
						html6=html6+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get(key)+"</span></p>";
				     	i.remove();
			   			gzmapvalue.remove(key);
					}
					num++;
				}
			}
			if(num%2==1&&num!=0){
				html4=html4+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>&nbsp;&nbsp;&nbsp;&nbsp;</span></p>";
				html6=html6+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>&nbsp;&nbsp;&nbsp;&nbsp;</span></p>";
			}
			if(num!=0){
				html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				html=html+"</td>";
				html4=html4+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				html4=html4+"</td>";
				html5=html5+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				html5=html5+"</td>";
				html6=html6+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
				html6=html6+"</td>";
				html=html+html5;
				html=html+html4;
				html=html+html6;
				html=html+"</tr>";
				html=html+"</table>";
				html=html+"<br/>";
			}
			if(gzmapname.containsKey("A34")){
				    html=html+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
			        html=html+"<tr>";
			        html=html+"<td width='35%' class='h4'>";	
			        html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";				
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A34")+"</span></p>";	
				   	html=html+"</td>";
					html=html+"<td width='15%' class='h3'>";    
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";					
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A34")+"</span></p>";			
			   		html=html+"</td>";	
			   		html=html+"<td width='35%'class='h2'>";    	
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";				
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A36")+"</span></p>";	
			   		html=html+"</td>";	
					html=html+"<td width='15%' class='h5'>";    
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";					
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A36")+"</span></p>";		
			   		html=html+"</td>";
			 		html=html+"</tr>";	
			   		html=html+"<tr>";	
					html=html+"<td width='25%' colspan='1' class='h'>";    
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:5px;'>&nbsp;&nbsp;</span></p>";				
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A52")+"</span></p>";	
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";
			   		html=html+"</td>";
			   		html=html+"<td width='75%' colspan='3' class='h1'>";    
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:5px;'>&nbsp;&nbsp;</span></p>";				
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>RMB:&nbsp;&nbsp;"+gzmapvalue.get("A52RMB");
			   		if(gzmapname.containsKey("A52USD")){
			   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;USD:&nbsp;&nbsp;"+gzmapvalue.get("A52USD");
			   		}
			   		if(gzmapname.containsKey("A52EUR")){
			   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;EUR:&nbsp;&nbsp;"+gzmapvalue.get("A52EUR");
			   		}
			   		if(gzmapname.containsKey("A52TWD")){
			   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;TWD:&nbsp;&nbsp;"+gzmapvalue.get("A52TWD");
			   		}
			   		html=html+"</span></p>";
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";	
			   		html=html+"</td>";		
				   	html=html+"</tr>";
				    html=html+"</table>";			
			    	html=html+"<br/>";
			}
			if(gzmapname.containsKey("A35")){
				    html=html+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
			        html=html+"<tr>";
			        html=html+"<td width='35%' class='h4'>";
			        html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";					
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A35")+"</span></p>";	
				   	html=html+"</td>";
					html=html+"<td width='15%' class='h3'>"; 
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";		   			
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A35")+"</span></p>";			
			   		html=html+"</td>";	
			   		html=html+"<td width='35%'class='h2'>";    	
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";				
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A37")+"</span></p>";	
			   		html=html+"</td>";	
					html=html+"<td width='15%' class='h5'>";    	
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";				
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A37")+"</span></p>";		
			   		html=html+"</td>";
			 		html=html+"</tr>";	
			   		html=html+"<tr>";	
					html=html+"<td width='25%' colspan='1' class='h'>";    
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:5px;'>&nbsp;&nbsp;</span></p>";				
					html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A53")+"</span></p>";	
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";	
			   		html=html+"</td>";
			   		html=html+"<td width='75%' colspan='3' class='h1'>";    
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:5px;'>&nbsp;&nbsp;</span></p>";				
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>RMB:&nbsp;&nbsp;"+gzmapvalue.get("A53RMB");			   	
			   		if(gzmapname.containsKey("A53USD")){
			   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;USD:&nbsp;&nbsp;"+gzmapvalue.get("A53USD");
			   		}
			   		if(gzmapname.containsKey("A53EUR")){
			   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;EUR:&nbsp;&nbsp;"+gzmapvalue.get("A53EUR");
			   		}
			   		if(gzmapname.containsKey("A53TWD")){
			   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;TWD:&nbsp;&nbsp;"+gzmapvalue.get("A53TWD");
			   		}
			   		
			   		html=html+"</span></p>";
			   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";	
			   		html=html+"</td>";		
				   	html=html+"</tr>";
				    html=html+"</table>";			
			    	html=html+"<br/>";
			}
			html=html+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
	        html=html+"<tr>";
	        html=html+"<td width='35%' class='h4'>";
	        html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";			
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A39")+"</span></p>";	
		   	html=html+"</td>";
			html=html+"<td width='15%' class='h3'>"; 
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";	   			
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A39")+"</span></p>";			
	   		html=html+"</td>";	
	   		html=html+"<td width='35%'class='h2'>";    	
	   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";			
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A40")+"</span></p>";	
	   		html=html+"</td>";	
			html=html+"<td width='15%' class='h5'>";   
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;&nbsp;</span></p>";	 			
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapvalue.get("A40")+"</span></p>";		
	   		html=html+"</td>";
	 		html=html+"</tr>";	
	   		html=html+"<tr>";	
			html=html+"<td width='25%' colspan='1' class='h'>";    
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:5px;'>&nbsp;&nbsp;</span></p>";				
			html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>"+gzmapname.get("A42")+"</span></p>";	
	   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";	
	   		html=html+"</td>";
	   		html=html+"<td width='75%' colspan='3' class='h1'>";    
	   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:5px;'>&nbsp;&nbsp;</span></p>";				
	   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:12px;'>RMB:&nbsp;&nbsp;"+gzmapvalue.get("A42RMB");	
	   		if(gzmapname.containsKey("A42USD")){
	   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;USD:&nbsp;&nbsp;"+gzmapvalue.get("A42USD");
	   		}
	   		if(gzmapname.containsKey("A42EUR")){
	   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;EUR:&nbsp;&nbsp;"+gzmapvalue.get("A42EUR");
	   		}
	   		if(gzmapname.containsKey("A42TWD")){
	   			html=html+"&nbsp;&nbsp;&nbsp;&nbsp;TWD:&nbsp;&nbsp;"+gzmapvalue.get("A42TWD");
	   		}
	   		html=html+"</span></p>";	
	   		html=html+"<p style='text-align:left;'><span style='line-height:1;font-size:2px;'>&nbsp;</span></p>";	
	   		html=html+"</td>";		
		   	html=html+"</tr>";
		    html=html+"</table>";			
	    	html=html+"<br/>";
	    	html=html+"</td>";
	    	mxmap.put("error", "true");
	    	mxmap.put("html", html);
	    	logger.info("html==="+html);
		}
		jsonArray.put(mxmap);
	} catch (Exception e) {
		logger.error("When calling salary query xml analysis interface, exception is thrown");
		logger.error("exception is ...."+ExceptionUtils.getStackTrace(e));
		Map mxmap = new HashMap();
		mxmap.put("error", "error");
		jsonArray.put(mxmap);
	} 
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonArray.toString());
		logger.info("salary Detail query end...");
	}
}