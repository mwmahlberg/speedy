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

import org.apache.shiro.guice.web.ShiroWebModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mwmahlberg.speedy.SpeedyApplication;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * The main configuration of Speedy is done here.
 * <p>
 * The {@link Injector} is created with the modules passed
 * to {@link SpeedyApplication}.
 * 
 * @author markus
 *
 */
public class ApplicationConfig extends GuiceServletContextListener {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private LinkedList<Module> modules;

	ServletContext context;


	public ApplicationConfig(String basePackage, String configFile, Class<? extends ShiroWebModule> clazz,Boolean secured,
			Module... modules) {

		this.modules = new LinkedList<Module>();

		if (modules.length > 0) {
			this.modules.addAll(Arrays.asList(modules));
		}

		this.modules.add(new SpeedyConfig(basePackage, clazz, secured, configFile));


	}

	/**
	 * Creates an {@link ApplicationConfig} instance.
	 * 
	 * @param basePackage package containg the application
	 * @param clazz Configuration class for Shiro.
	 * @param secured (de)activates {@link http://shiro.apache.org}. Set to false to disable security. 
	 * @param modules {@link Module}s to install into the {@link Injector}
	 */
	public ApplicationConfig(String basePackage, Class<? extends ShiroWebModule> clazz, Boolean secured,
			 Module... modules) {
		this(basePackage, null,clazz, secured, modules);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		this.context = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}

	@Override
	protected Injector getInjector() {

		Long start = System.currentTimeMillis();

		logger.info("Bootstrapping application");
		
		Injector injector = Guice.createInjector(this.modules);

		logger.info("Bootstrapped application in {} ms",
				System.currentTimeMillis() - start);

		return injector;
	}

}