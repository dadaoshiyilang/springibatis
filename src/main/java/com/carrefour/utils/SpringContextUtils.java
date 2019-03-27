package com.carrefour.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext = null;
	 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringContextUtils.applicationContext == null) {
			SpringContextUtils.applicationContext = applicationContext;
		}
	}
 
	/**
	 * @apiNote 获取applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
 
	/**
	 * @apiNote 通过name获取 Bean
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
}