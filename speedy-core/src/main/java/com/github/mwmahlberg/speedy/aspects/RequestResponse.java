package com.github.mwmahlberg.speedy.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.github.mwmahlberg.speedy.ModelAndView;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class RequestResponse implements MethodInterceptor {

	@Inject
	Injector injector;
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		Object mav = invocation.proceed();
		
		if(mav instanceof ModelAndView) {
			ModelAndView returnValue =((ModelAndView) mav);
			returnValue.getModel().put("request", injector.getInstance(HttpServletRequest.class));
			returnValue.getModel().put("response", injector.getInstance(HttpServletResponse.class));
			return returnValue;
		}
		return mav;
	}

}
