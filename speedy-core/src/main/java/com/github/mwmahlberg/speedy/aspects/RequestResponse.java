/*
 * Copyright 2014 Markus W Mahlberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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