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

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.shiro.guice.web.ShiroWebModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public class ApplicationConfig extends GuiceServletContextListener {

	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private ArrayList<Module> modules;
	
	ServletContext context;

	public ApplicationConfig(String basePackage, String configFile, Boolean secured,
			Module... modules) {

		this.modules = new ArrayList<Module>();
		if (modules.length > 0) {
			this.modules.addAll(Arrays.asList(modules));
		}
		
		this.modules.add(new SpeedyConfig(basePackage, configFile));
		
		if (secured) {
			this.modules.add(new SpeedyShiroWebModule(this.context));
			this.modules.add(ShiroWebModule.guiceFilterModule());
		}
		
	}

	public ApplicationConfig(String basePackage, Boolean secured,Module... modules) {
		this(basePackage, null,secured, modules);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		/* We need the servlet context for correctly
		 * initializing Shiro
		 */
		logger.info("Acquiring servlet context");
		context = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}
	
	@Override
	protected Injector getInjector() {
		
		if(context == null){
			logger.error("ServletContext is null!!!");
		}
		
		return Guice.createInjector(modules);
	}

}