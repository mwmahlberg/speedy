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

package com.github.mwmahlberg.speedy.config;

import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public class ApplicationConfig extends GuiceServletContextListener {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private LinkedList<Module> modules;

	ServletContext context;

	Boolean secured = Boolean.TRUE;

	public ApplicationConfig(String basePackage, String configFile, Boolean secured,
			Module... modules) {

		this.modules = new LinkedList<Module>();
		this.modules.add(new SpeedyConfig(basePackage, configFile));

		if (modules.length > 0) {
			this.modules.addAll(Arrays.asList(modules));
		}


		this.secured = secured;

	}

	public ApplicationConfig(String basePackage, Boolean secured,
			 Module... modules) {
		this(basePackage, null, secured, modules);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		this.context = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}

	@Override
	protected Injector getInjector() {

		Long start = System.currentTimeMillis();

		logger.info("Bootstrapping Shiro Security Layer");
		
		Injector injector = Guice.createInjector(this.modules);

		logger.info("Bootstrapped Shiro Security Layer in {} ms",
				System.currentTimeMillis() - start);

		return injector;
	}

}