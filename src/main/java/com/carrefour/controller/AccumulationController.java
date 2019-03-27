package com.carrefour.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import com.carrefour.entity.FieldDto;
import com.carrefour.entity.MobileDto;
import com.carrefour.service.impl.UserServiceImpl;
import com.carrefour.utils.SpringContextUtils;
import weaver.careefour.tool.CareefourUtil;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

public class AccumulationController extends AbstractController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("Accumulation page query start ...");
		String cxyear = CareefourUtil.getDate().substring(0, 4);
		String cxdatetime = CareefourUtil.getDate();
		cxdatetime = cxdatetime + " " + CareefourUtil.getTime();
		User user1 = HrmUserVarify.getUser(request, response);
		int userid = user1.getUID();
		UserServiceImpl userService = (UserServiceImpl)SpringContextUtils.getBean("userService");
		Map<String, MobileDto> res = userService.getMobileByUserId(userid);
		MobileDto mobileDto = res.get(userid);
		int language1 = user1.getLanguage();
		Map<String, FieldDto> resultMap = userService.getfileCode(userid);
		String subname7 = "";
		String subname8 = "";
		if (resultMap != null && (!resultMap.isEmpty())) {
			FieldDto field = resultMap.get(userid);
			String bucode = field.getField2();
			String alcode = field.getField3();
			String subname = userService.getSubcompanyname(bucode, alcode);
			String [] subnames=subname.split("`~`");
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
		}
		String gh = user1.getLoginid();
		gh = gh.toUpperCase();
		String label_1;
		String label_2;
		String label_3;
		String label_4;
		String label_5;
		String label_6;
		String label_7;
		String label_8;
		String label_9;
		String btn_text;
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
			btn_text ="导出";
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
			btn_text ="Data Export";
		}
		ModelAndView model = new ModelAndView("carrefour_querygjj");
		model.addObject("label_1", label_1);
		model.addObject("label_2", label_2);
		model.addObject("label_3", label_3);
		model.addObject("label_4", label_4);
		model.addObject("label_5", label_5);
		model.addObject("label_6", label_6);
		model.addObject("label_7", label_7);
		model.addObject("label_8", label_8);
		model.addObject("label_9", label_9);
		model.addObject("sjh", mobileDto.getMobile());
		model.addObject("language1", language1);
		model.addObject("btn_text", btn_text);
		model.addObject("gh", gh);
		model.addObject("cxyear", cxyear);
		model.addObject("cxdatetime", cxdatetime);
		logger.info("Accumulation page query end ...");
		return model;
	}
}