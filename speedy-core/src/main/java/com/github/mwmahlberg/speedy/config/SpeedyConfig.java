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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.guice.web.GuiceShiroFilter;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mwmahlberg.speedy.components.ComponentModule;
import com.github.mwmahlberg.speedy.handler.HandlerModule;
import com.github.mwmahlberg.speedy.provider.ProviderModule;
import com.github.mwmahlberg.speedy.security.ShiroMethodInterceptorModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.research.ws.wadl.Method;

/**
 * 
 * @author markus
 *
 */
public class SpeedyConfig extends JerseyServletModule {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	String basePackage;

	Class<? extends ShiroWebModule> clazz;

	Boolean secured;

	String configFile;

	public SpeedyConfig(String basePackage,
			Class<? extends ShiroWebModule> clazz, Boolean secured,
			String configFile) {

		if (basePackage == null) {
			throw new IllegalArgumentException("basePackage must not be null");
		} else if (basePackage.length() < 1) {
			throw new IllegalArgumentException(
					"basePackage name must contain at least one caracter");
		}

		this.basePackage = basePackage;
		this.configFile = configFile;
		this.clazz = clazz;
		this.secured = secured;
	}

	@Override
	protected void configureServlets() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

		filter("/*").through(GuiceShiroFilter.class);
		serve("/*").with(GuiceContainer.class, params);

		bind(String.class).annotatedWith(Names.named("basePackage"))
				.toInstance(basePackage);

		logger.info("Installing modules");
		install(new ProviderModule());

		ShiroWebModule shiroModule = getShiroConfigInstance(clazz);
		ServletModule filterModule = getFilterModule(clazz);

		if (secured && shiroModule != null && filterModule != null) {
			// install(new ShiroMethodInterceptorModule());
			install(shiroModule);
			install(filterModule);
			install(new ShiroAopModule());
		} else {
			logger.info("Continuing without security layer.");
		}

		install(new ComponentModule());
		install(new HandlerModule());

		logger.info("Finished installing modules");

	}

	/**
	 * Provides realms for {@link GuiceShiroFilter}
	 * 
	 * @return
	 */
	@Provides
	@Singleton
	public Set<Realm> realm() {

		return new HashSet<Realm>(Arrays.asList(new IniRealm(
				"classpath:shiro.ini")));
	}

	public ServletModule getFilterModule(Class<? extends ShiroWebModule> clazz) {
		ServletModule shiroFilterModule = null;

		try {
			java.lang.reflect.Method method = clazz
					.getMethod("guiceFilterModule");
			method.setAccessible(true);
			Object retval = method.invoke(null);
			method.setAccessible(false);
			if (retval instanceof ServletModule) {
				shiroFilterModule = (ServletModule) retval;
			}
		} catch (NoSuchMethodException e) {
			logger.error("Method guiceFilterModule() not found in {}",
					clazz.getName());
		} catch (SecurityException e) {
			logger.error("Security exception thrown while accessing guiceFilterModule");
			logger.error("Exception was:", e);
		} catch (IllegalAccessException e) {
			logger.error("Could not access guiceFilterModule()");
			logger.error("Exception was: ", e);
		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument exception was thrown:", e);
		} catch (InvocationTargetException e) {
			logger.error("guiceFilterModule() threw an exception;", e);
		}

		return shiroFilterModule;
	}

	public ShiroWebModule getShiroConfigInstance(
			Class<? extends ShiroWebModule> clazz) {

		ShiroWebModule instance = null;

		try {
			Constructor<? extends ShiroWebModule> ctor = clazz
					.getConstructor(ServletContext.class);

			ctor.setAccessible(true);
			instance = ctor.newInstance(getServletContext());
			ctor.setAccessible(false);

			return instance;

		} catch (NoSuchMethodException e) {
			logger.warn("No matching constructor found in {}", clazz);
		} catch (SecurityException e) {
			logger.error("Security exception: ", e);
		} catch (InstantiationException e) {
			logger.error("Could not instantiate {}", clazz);
		} catch (IllegalAccessException e) {

			logger.error("Illegal access", e);

		} catch (IllegalArgumentException e) {
			logger.error(
					"Servlet context passed to constructor of {} is not valid",
					clazz);
		} catch (InvocationTargetException e) {
			logger.error("Constructor of {} threw an exception", clazz);
			logger.error("InvocationTargetException:", e);
		}

		return instance;

	}

}
