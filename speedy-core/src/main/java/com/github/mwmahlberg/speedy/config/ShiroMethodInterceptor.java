package com.github.mwmahlberg.speedy.config;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author pablo.biagioli
 *
 */
public class ShiroMethodInterceptor implements MethodInterceptor{
	
	static final Logger logger = LoggerFactory.getLogger(ShiroMethodInterceptor.class.getName());

    private org.apache.shiro.aop.MethodInterceptor methodInterceptor;

    public ShiroMethodInterceptor(org.apache.shiro.aop.MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }


	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		return methodInterceptor.invoke(new ShiroMethodInvocation(methodInvocation));
	}

	private static class ShiroMethodInvocation implements org.apache.shiro.aop.MethodInvocation {

		private final MethodInvocation methodInvocation;

		public ShiroMethodInvocation(MethodInvocation methodInvocation) {
			this.methodInvocation = methodInvocation;
		}

        public Object proceed() throws Throwable {
            return methodInvocation.proceed();
        }

        public Method getMethod() {
            return methodInvocation.getMethod();
        }

        public Object[] getArguments() {
            return methodInvocation.getArguments();
        }

        public Object getThis() {
            return methodInvocation.getThis();
        }
    }
}