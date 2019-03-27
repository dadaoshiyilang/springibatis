<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<head>	
<title>社保公积金基数核定</title>
<script type="text/javascript" language="javascript" src="/js/jquery/jquery_wev8.js"></script>
<script type="text/javascript" language="javascript" src="/js/jquery/jquery.cookie.js"></script>
<style type="text/css">
	.thin{background:#000000}
	.thin td{background:#FFFFFF;text-align:center}
</style>
</head>
<body>
<div align="center" id="div2">
<table align="center" valign="middle"  style="width: 10%;height: 6%"  >
	<tr style="width: 100%;height: 55%">
		<td colspan="2" align="center">
			<input style="width: 100%;height:100%;" id="yzm" type="text" value="" />
		</td>
	</tr>
	<tr style="width: 100%;">
		<td style="width: 50%" align="left">
			<input style="height:120%;" id="second" type="button" value="${label_1}"/>
		</td>
		<td style="width: 50%" align="right">
			<input  style="height:120%;" id="send" type="button" value="${label_2}"/>
		</td>
	</tr>
</table>
</div>
<div align="center" id="div1" style="display: none"> 
	<div align="center" id="div4" style="position:relative;margin-top:50px;display: block" >
		<div id="GjjTitle" style="position:relative;margin-bottom:20px;"><font size="5">Y20XX Housing Found Base Confirm</font></div>
		<table id="tbGjj" style="width:100%;" border="0" cellspacing="1" cellpadding="0" class="thin">
			<tr><td>EmployeeName</td><td>DeptName</td><td>ID Code</td><td id="GjjTitleAVIncomeEg">Y20XX-1_Average Monthly Salary</td><td id="GjjTitlebaseEg">Y20XX_Housing Fund Base</td><td>Confirm by Employee</td></tr>	
			<tr><td>姓名</td><td>部门</td><td>身份证号码</td><td id="GjjTitleAVIncomeCh">20XX-1年月平均收入</td><td id="GjjTitlebaseCh">20XX年公积金基数</td><td>员工确认</td></tr>
			<tr><td class="name"></td><td class="dept"></td><td class="idcode"></td><td class="incomeAvg"></td><td id="Gjj"></td><td class="emp"></td></tr>
		<table>
		<br>
		<input style="height:5%;" id="printing" type="button" value="${btn_text}" onclick ="pringting()"/>
		<input id="hid_msg" type="hidden">
	</div>
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
	querygjj();
}

//发送验证码
function sendCode(obj){
	var myDate = new Date();
	var date=myDate.getDate();
	var sjh="${sjh}";
	var language1="${language1}";
	var gh="${gh}";
	if(sjh==""){
		top.Dialog.alert("${label_7}");
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
	   		top.Dialog.alert("${label_8}");
   			addCookie("secondsremained",180,180);//添加cookie记录,有效时间180s
			addCookie("yzm",yzm,180);//添加cookie记录,有效时间180s
			addCookie("gh",gh,180);//添加cookie记录,有效时间180s
			settime(obj);//开始倒计时
	   	}else{
	   		top.Dialog.alert("${label_9}");
	   	}
		});
	}, 
	error: function(data) { 
		top.Dialog.alert("error");
	} 
	}); 
}
//开始倒计时
var countdown;
function settime(obj) { 
	countdown=getCookieValue("secondsremained");
	if (countdown == 0) { 
		obj.removeAttr("disabled"); 
		obj.val("${label_1}"); 
		return;
	} else { 
		obj.attr("disabled", true); 
		obj.val("${label_3}(" + countdown + ")"); 
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

function querygjj(){
	var gh="${gh}";
	var cxyear=(parseInt("${cxyear}")-1).toString();
	var cxdatetime="${cxdatetime}";
	jQuery.ajax({ 
		url: '/queryAccumulation', 
		type: 'post', 
		data:{gh:gh,cxyear:cxyear,cxdatetime:cxdatetime},
		dataType: 'json',
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		async: false,
		success: function(data) {
			jQuery.each(data,function(id,item){
				if(item.error=="true"){
					jQuery("#GjjTitle").html("Y"+(parseInt(cxyear)+1)+" Housing Found Base Confirm");
					jQuery("#GjjTitleAVIncomeEg").html("Y"+cxyear+"_Average Monthly Salary");
					jQuery("#GjjTitlebaseEg").html("Y"+(parseInt(cxyear)+1)+"_Housing Fund Base");
					jQuery("#GjjTitleAVIncomeCh").html(cxyear+"年月平均收入");
					jQuery("#GjjTitlebaseCh").html((parseInt(cxyear)+1)+"年社公积金数");
					jQuery(".name").html(item.name);
					jQuery(".dept").html(item.dept);
					jQuery(".idcode").html(item.idcode);
					if(parseInt(item.averagesalary)!=0)
					{
						jQuery(".incomeAvg").html(item.averagesalary);
					}
					if(parseInt(item.inspara)!=0)
					{
						jQuery("#Gjj").html(item.inspara);
					}
					jQuery("#hid_msg").val(item.hidmsg);
					toggle("div1",0);
					toggle("div2",1);
				}else{
					top.Dialog.alert("数据异常：" + item.error);
				}
			});
		}, 
		error: function(data) {			
			top.Dialog.alert("error");
		} 
	});
}

function pringting() {
	var hid_msg = $("#hid_msg").val();
	jQuery.ajax({ 
		url: '/textPdfSessionPritn', 
		type: 'post', 
		data:{hid_msg:hid_msg},
		dataType: 'text',
		async: false,
		success: function(data) {
			window.location.href='/accumulationReport';
		}, 
		error: function(data) {
			top.Dialog.alert("error");
		} 
	});
}
</script>
</html>