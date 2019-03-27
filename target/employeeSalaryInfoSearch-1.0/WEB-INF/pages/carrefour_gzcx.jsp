<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.hrm.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="rs1" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="sutil" class="weaver.general.Util" scope="page" />
<jsp:useBean id="cutil" class="weaver.careefour.tool.CareefourUtil" scope="page" />
<%
	String cxyf=cutil.getDate().substring(0, 7);
	cxyf=cutil.getdateold(cxyf,-1);
	User user1 = HrmUserVarify.getUser (request , response);
	String sjh="";
	int userid=user1.getUID();
	String sqlsj="select mobile from hrmresource where id='"+userid+"'";
	rs.executeQuery(sqlsj);
	if(rs.next()){
		sjh=rs.getString("mobile");
	}
	
	int language1 = user1.getLanguage();
	String sql="select field2,field3 from cus_fielddata where id='"+userid+"'";
	String bucode="";
	String alcode="";
	rs.executeQuery(sql);
	if(rs.next()){
		bucode=rs.getString("field2");
		alcode=rs.getString("field3");
	}
	String subsql="select replace(replace(subcompanyname,'~`~`',''),'`~`~','') as subcompanyname from HrmSubcompanyDefined t ,HrmSubCompany t1 where t.subcomid=t1.id and t.bucode='"+bucode+"' and t.alcode='"+alcode+"'";
	String subname="";
	rs1.executeQuery(subsql);
	if(rs1.next()){
		subname=rs1.getString("subcompanyname");
	}
	String [] subnames=subname.split("`~`");
	String subname7="";
	String subname8="";
	for(int i=0;i<subnames.length;i++){
		 if(subnames[i].length()>1&&subnames[i].charAt(0)=='7'){
			subname7=subnames[i].substring(1, subnames[i].length()).trim();
		 }else if(subnames[i].length()>1&&subnames[i].charAt(0)=='8'){
		  	subname8=subnames[i].substring(1, subnames[i].length()).trim();
		 }else{
		 	if(subname7.equals("")){
		 		subname7=subnames[i];
		 	}
		 	if(subname8.equals("")){
		 		subname8=subnames[i];
		 	}
		 }
	}
	if(subname8.contains("(H.O)")){
		subname8="CARREFOUR CHINA MANAGEMENT & CONSULTANT LTD. CO.";
		subname7="家乐福（中国）管理咨询服务有限公司 ";
	}else{
		subname8="CARREFOUR CHINA  "+subname8;
	}
	
	
	
	int depid=user1.getUserDepartment();
	rs.executeProc("HrmDepartment_SelectByID",""+depid);
	String departmentname="";
	if(rs.next()){
		departmentname = sutil.toScreen(rs.getString("departmentname"),language1);
	}
	String gh = user1.getLoginid();
	gh=gh.toUpperCase();
	//工号以CHNHO/DCSHV开头的以自然月为区间；其他以上月26日本月25日为区间
	String month_day="";
	String dateScopeHO =cutil.getdate(cxyf,0);
	String dateScope = cutil.getdatenew(cxyf,-1)+"26-"+cutil.getdatenew(cxyf,0)+"25";
	if(gh!=null&&gh.length()>5&&(gh.substring(0,5).equals("CHNHO")||gh.substring(0,5).equals("DCSHV"))){
		month_day=cutil.getdate(cxyf,0);
	}else{
		month_day=cutil.getdatenew(cxyf,-1)+"26-"+cutil.getdatenew(cxyf,0)+"25";
	}
	String name= user1.getUsername();
	String label_1 = "";
	String label_2 = "";
	String label_3 = "";
	String label_4 = "";
	String label_5 = "";
	String label_6 = "";
	String label_7 = "";
	String label_8="";
	String label_9="";
	if(language1==7){
		label_1 = "获取验证码";
		label_2 = "确认";
		label_3 = "重新发送";
		label_4 = "工资";
		label_5 = "验证码错误";
		label_6 = "查询";
		label_7 ="手机号为空";
		label_8 ="发送成功";
		label_9 ="获取验证码失败";
	}else{
		label_1 = "Get Verification Code";
		label_2 = "Confirm";
		label_3 = "Resend";
		label_4 = "Salary";
		label_5 = "Verification Code Error";
		label_6 = "Query";
		label_7 = "Cell phone number is empty";
		label_8 ="send success";
		label_9="Get Verification Code Error";
	}
	long currentTime=System.currentTimeMillis();
 %>
<title><%=label_4 %></title>
<script type="text/javascript" language="javascript" src="/js/jquery/jquery_wev8.js"></script>
<script type="text/javascript" language="javascript" src="/js/jquery/jquery.cookie.js"></script>
<style type="text/css">
.x {
border-top: 1px solid black;
border-left: 0px solid black;
border-right: 1px solid black;
border-bottom: 1px solid black; 
}
.y {
border-top: 1px solid black;
border-left: 1px solid black;
border-right: 0px solid black;
border-bottom: 1px solid black; 
}
.h {
border-top: 0px solid black;
border-left: 1px solid black;
border-right: 0px solid black;
border-bottom: 1px solid black; 
}
.h1 {
border-top: 0px solid black;
border-left: 0px solid black;
border-right: 1px solid black;
border-bottom: 1px solid black; 
}
.h2 {
border-top: 1px solid black;
border-left: 0px solid black;
border-right: 0px solid black;
border-bottom: 0px solid black; 
}
.h3 {
border-top: 1px solid black;
border-left: 0px solid black;
border-right: 0px solid black;
border-bottom: 0px solid black; 
}
.h4 {
border-top: 1px solid black;
border-left: 1px solid black;
border-right: 0px solid black;
border-bottom: 0px solid black; 
}
.h5 {
border-top: 1px solid black;
border-left: 0px solid black;
border-right: 1px solid black;
border-bottom: 0px solid black; 
}
</style>
</head>
<body>
<div align="center" id="div2" >
<table align="center" valign="middle"  style="width: 10%;height: 6%"  >
	<tr style="width: 100%;height: 55%">
		<td colspan="2" align="center">
			<input style="width: 100%;height:100%;" id="yzm" type="text" value="" />
		</td>
	</tr>
	<tr style="width: 100%;">
		<td style="width: 50%" align="left">
			<input style="height:120%;" id="second" type="button" value="<%=label_1 %>"/>
		</td>
		<td style="width: 50%" align="right">
			<input  style="height:120%;" id="send" type="button" value="<%=label_2 %>"/>
		</td>
	</tr>
</table>
</div>
  
<div align="center" id="div1" style="display: none" >
<!--
<div align="center" id="div1"  >
-->
<table   style="width: 80%;height: 70%"  >
	<tr style="height: 8%">
		<td colspan="2" style="height: 9%;" >
			<table  style="width: 100%; " >
				<tr style="width: 100%;">
				<td align="left" width="25%">
				<font size="2px" style="font-weight: bold"><%if(language1==7) {%>部门:<%}else{ %>Department:<% }%><%=departmentname %></font>
				</td>
				<td align="left" width="25%">
				<font size="2px" style="font-weight: bold"><%if(language1==7) {%>员工编号:<%}else{ %>Employee ID:<% }%> <%=gh %></font>
				</td>
				<td align="left" width="35%">
				<font size="2px" style="font-weight: bold"><%if(language1==7) {%>姓名:<%}else{ %>Name:<% }%> <%=name %></font>
				</td>
				<td align="left" width="25%">
					<font size="4px" style="font-weight: bold">
					<select id="select_list" name="issued_sub_key_c"></select>
					</font>
				</td>
				
				<td align="left" width="10%">
					<input  style="height:120%;" id="query" type="button" value="<%=label_6 %>" onclick="querygz();"/>
				</td>
				<td >
				</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="tr2" style="height: 18%">
	</tr>
	</br>
	<tr id="tr1" align="top" >
	</tr>
	</br>
	<tr style="height: 8%;display: none" id="NewHD">
		<td colspan="2" style="height: 9%;" >
			<table  style="width: 100%; " >
				<tr style="width: 100%;">
				<td align="left" width="25%" id="newDep">
				<font size="2px" style="font-weight: bold"><%if(language1==7) {%>部门:<%}else{ %>Department:<% }%></font>
				</td>
				<td align="left" width="25%" id="newEmp">
				<font size="2px" style="font-weight: bold"><%if(language1==7) {%>员工编号:<%}else{ %>Employee ID:<% }%> </font>
				</td>
				<td align="left" width="35%">
				<font size="2px" style="font-weight: bold"><%if(language1==7) {%>姓名:<%}else{ %>Name:<% }%> <%=name %></font>
				</td>
				<td align="left" width="10%">
					
				</td>
				<td >
				</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="tr4" style="height: 18%">
	</tr>
	<tr id="tr3" align="top">
	</tr>
</table>
</div>
</body>
<script>
document.oncontextmenu=new Function("event.returnValue=false;");
document.onselectstart=new Function("event.returnValue=false;");
var yzm="";
//发送验证码时添加cookie
function addCookie(name,value,expiresHours){ 
	//判断是否设置过期时间,0代表关闭浏览器时失效
	if(expiresHours>0){ 
		var date=new Date(); 
		date.setTime(date.getTime()+expiresHours*1000); 
		jQuery.cookie(name, escape(value), {expires: date});
	}else{
		jQuery.cookie(name, escape(value));
	}
} 
//修改cookie的值
function editCookie(name,value,expiresHours){ 
	if(expiresHours>0){ 
	var date=new Date(); 
	date.setTime(date.getTime()+expiresHours*1000); //单位是毫秒
	jQuery.cookie(name, escape(value), {expires: date});
	} else{
	jQuery.cookie(name, escape(value));
	}
} 
//根据名字获取cookie的值
function getCookieValue(name){ 
	return jQuery.cookie(name);
}
jQuery(function(){
	jQuery("#second").click(function (){
		sendCode(jQuery("#second"));
	});
	
	jQuery("#yzm").keydown(function(e) {  
        if (e.keyCode == 13){  
          doSendFunc();
        }
   	});
	
	jQuery("#send").click(function (){
		doSendFunc();
	});
	
	v = getCookieValue("secondsremained");//获取cookie值
	if(v>0){
		settime(jQuery("#second"));//开始倒计时
	}
})

//查询按钮的操作
function doSendFunc(){
	yzm_1=jQuery("#yzm").val();
	yzm_2=getCookieValue("yzm");
	var gh_1=getCookieValue("gh");
	var gh="<%=gh%>";
	//if(yzm_1==yzm_2&&yzm_1!=""&&gh==gh_1){
		//addCookie("yzm","",180);
		toggle("div1",0);
		toggle("div2",1);
	//}else{
		//top.Dialog.alert("<%=label_5%>");
	//}
	doLinkFunc();
}

function doLinkFunc() {
	 $.ajax({
     type: 'POST',
     dataType: 'text',
	 async: false,
     url: '/salary/query',
     data: null,
        success: function (data) {
		var jsonObj = eval("("+ data +")");
		var optionstring = "";
		for(var item in jsonObj){
			optionstring += "<option value=\""+ jsonObj[item].id +"\" >"+ jsonObj[item].month +"</option>";
		}
		jQuery("#select_list").html(optionstring);
         },
         error: function (err) {
             alert("err:" + err);
         }
     });
}

//发送验证码
function sendCode(obj){
	var myDate = new Date();
	var date=myDate.getDate();
	//if(date>=8&&date<=25){
		// doPostBack('jQuery{base}/login/getCode.htm',backFunc1,{"phonenum":phonenum});
	var sjh="<%=sjh%>";
	var language1="<%=language1%>";
	var gh="<%=gh%>";
	if(sjh==""){
		top.Dialog.alert("<%=label_7%>");
		return;
	}
	jQuery.ajax({ 
	url: '/interface/carrefour/carrefour_getyzm.jsp', 
	type: 'post', 
	data:{sjh:sjh,language1:language1,gh:gh},
	dataType: 'json',
	async: false,
	success: function(data) {
		jQuery.each(data,function(id,item){
	   	if(item.yzm!=""){
	   		yzm=item.yzm;
	   		top.Dialog.alert("<%=label_8%>");
   			addCookie("secondsremained",180,180);//添加cookie记录,有效时间180s
			addCookie("yzm",yzm,180);//添加cookie记录,有效时间180s
			addCookie("gh",gh,180);//添加cookie记录,有效时间180s
			settime(obj);//开始倒计时
	   	}else{
	   		top.Dialog.alert("<%=label_9%>");
	   	}
		});
	}, 
	error: function(data) { 
		top.Dialog.alert("error");
	} 
	}); 
		
	//}else{
	//	top.Dialog.alert("请在每月八号至二十五号点击获取验证码！");
		
	//}
	
	
}
//开始倒计时
var countdown;
function settime(obj) { 
	countdown=getCookieValue("secondsremained");
	if (countdown == 0) { 
		obj.removeAttr("disabled"); 
		obj.val("<%=label_1 %>"); 
		return;
	} else { 
		obj.attr("disabled", true); 
		obj.val("<%=label_3 %>(" + countdown + ")"); 
		countdown--;
		editCookie("secondsremained",countdown,countdown+10);
		setTimeout(function() { settime(obj) },1000) //每1000毫秒执行一次
	} 
	
} 

function toggle(targetid,str){
    if (document.getElementById){
        target=document.getElementById(targetid);
            if (str==1){
                target.style.display="none";
            } else {
                target.style.display="block";
            }
    }
}

function querygz(){
	var gh="<%=gh%>";
	var yf="<%=cxyf%>";
	var yy="<%=language1%>";
	var dateScopeHO ="<%=dateScopeHO%>";
	var dateScope = "<%=dateScope%>";
	var obj = $("#select_list option:selected");
	var selValue = obj.text();
	alert
	if(yy=="7"){
		yy="CN";
	}else{
		yy="EN";
	}
	var newId="";
	var newDep="";
	var newScope="";
	try {
		jQuery.ajax({ 
		url: "/interface/carrefour/others/carrefour_getNewId.jsp", 
		type: 'post', 
		data:{gh:gh},
		dataType: 'text',
		async: false,
		success: function(data) {
			if(data.trim()!="")
			{
				try{
					newId = data.trim().split('|')[0];
					newDep= data.trim().split('|')[1];
					//工号以CHNHO/DCSHV开头的以自然月为区间；其他以上月26日本月25日为区间
					
					if(newId.substring(0,5)=="CHNHO"||newId.substring(0,5)=="DCSHV"){
						newScope=dateScopeHO;
					}else{
						newScope=dateScope;
					}
				}catch(err)
				{
					newId="";
					newDep="";
				}
				
				
			}else{
				newId="";
				newDep="";
			}
			
			//if(gh=="CHNHO02064")
			//{
			//	newId="EASHVM0100";
			//}else if(gh=="EASHV10014")
			//{
			//	newId="CHNHO01738";
			//}
		}, 
		error: function(data) { 
			
		} 
	}); 		
	} catch (err) {
		
	}
	
	var htmltr="";
	jQuery.ajax({ 
		url: '/querySalaryDetail', 
		type: 'post', 
		data:{gh:gh,yf:yf,yy:yy,selValue:selValue},
		dataType: 'json',
		async: false,
		success: function(data) {
			jQuery.each(data,function(id,item){
				if(item.error=="true"){
					htmltr=item.html;
				}else{
					top.Dialog.alert(item.error);
				}
			});
		}, 
		error: function(data) { 
			top.Dialog.alert("error");
		} 
	});

	var month_day="";
	jQuery.ajax({ 
		url: '/monthSection', 
		type: 'post', 
		data:{gh:gh,cxyf:selValue},
		dataType: 'text',
		async: false,
		success:function(data) {
			month_day = data;
		}, 
		error: function(data) { 
			top.Dialog.alert("error");
		} 
	}); 
	
	var html11="<td >";
	html11=html11+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
	html11=html11+"<tbody>";	
	html11=html11+"<tr>";		
	html11=html11+"<td width='100%' >";	
	html11=html11+"<p style='text-align:center;'><span style='line-height:0;font-size:2px;'>&nbsp;</span></p>";			
	html11=html11+"<p style='text-align:center;'><span style='line-height:0;font-size:14px;'>"+selValue+"SALARY SLIP</span></p>";					
	html11=html11+"<p style='text-align:center;'><span style='line-height:1;font-size:14px;'>"+"<%=subname8 %>"+"</span></p>";	
	html11=html11+"<p style='text-align:center;'><span style='line-height:0;font-size:14px;'>"+"<%=subname7 %>"+"工资单</span></p>";
	html11=html11+"<p style='text-align:right;'><span id ='month_day' style='line-height:0;font-size:12px;'>"+month_day+"</span></p>";	
	html11=html11+"</td>";		
	html11=html11+"</tr>";
	html11=html11+"</tbody>";
	html11=html11+"</table>";
	html11=html11+"</td>";
	
	var htmltr3="";
	var NewCompany="";
	var NewTitle="";
	if(newId.trim()!="")
	{
		jQuery.ajax({ 
			url: '/interface/carrefour/others/carrefour_companyName.jsp', 
			type: 'post', 
			data:{gh:newId},
			dataType: 'text',
			async: false,
			success: function(data) {
				NewCompany = data.split("||")[1];
				NewTitle = data.split("||")[0];
				if(NewCompany.trim()=="")
				{
					NewCompany=subname7;
				}
			}, 
			error: function(data) { 
				top.Dialog.alert("error");
			} 
		}); 		
		jQuery.ajax({ 
		url: '/querySalaryDetail', 
		type: 'post', 
		data:{gh:newId,yf:yf,yy:yy,selValue:selValue},
		dataType: 'json',
		async: false,
		success: function(data) {
			jQuery.each(data,function(id,item){
				if(item.error=="true"){
					htmltr3=item.html;
					var htm4="<td >";
					htm4=htm4+"<table style='width:100%;' cellpadding='1' cellspacing='0' align='center' border='1' bordercolor='#000000'>";
					htm4=htm4+"<tbody>";	
					htm4=htm4+"<tr>";		
					htm4=htm4+"<td width='100%' >";	
					htm4=htm4+"<p style='text-align:center;'><span style='line-height:0;font-size:2px;'>&nbsp;</span></p>";			
					htm4=htm4+"<p style='text-align:center;'><span style='line-height:0;font-size:14px;'>"+selValue+"SALARY SLIP</span></p>";					
					htm4=htm4+"<p style='text-align:center;'><span style='line-height:1;font-size:14px;'>"+NewTitle+"</span></p>";	
					htm4=htm4+"<p style='text-align:center;'><span style='line-height:0;font-size:14px;'>"+NewCompany+"工资单</span></p>";
					htm4=htm4+"<p style='text-align:right;'><span style='line-height:0;font-size:12px;'>"+newScope+"</span></p>";	
					htm4=htm4+"</td>";		
					htm4=htm4+"</tr>";
					htm4=htm4+"</tbody>";
					htm4=htm4+"</table>";
					htm4=htm4+"</td>";
					document.getElementById("NewHD").style.display="block";
					document.getElementById("newDep").innerHTML=document.getElementById("newDep").innerHTML+"<font size='2px' style='font-weight: bold'>"+newDep+"</font>";
					document.getElementById("newEmp").innerHTML=document.getElementById("newEmp").innerHTML+"<font size='2px' style='font-weight: bold'>"+newId+"</font>";
					document.getElementById("tr4").innerHTML=htm4;
					document.getElementById("tr3").innerHTML=htmltr3;
				}else{
					
				}
			});
		}, 
		error: function(data) { 
			top.Dialog.alert("error");
		} 
	}); 	
	}
	document.getElementById("tr2").innerHTML=html11;
	document.getElementById("tr1").innerHTML=htmltr;
}
</script>
</html>
