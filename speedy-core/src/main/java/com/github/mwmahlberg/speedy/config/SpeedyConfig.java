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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.guice.web.GuiceShiroFilter;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mwmahlberg.speedy.components.ComponentModule;
import com.github.mwmahlberg.speedy.handler.HandlerModule;
import com.github.mwmahlberg.speedy.provider.ProviderModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class SpeedyConfig extends JerseyServletModule {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	String basePackage;

	String configFile;

	public SpeedyConfig(String basePackage, String configFile) {

		if (basePackage == null) {
			throw new IllegalArgumentException("basePackage must not be null");
		} else if (basePackage.length() < 1) {
			throw new IllegalArgumentException(
					"basePackage name must contain at least one caracter");
		}

		this.basePackage = basePackage;
		this.configFile = configFile;

	}

	@Override
	protected void configureServlets() {

		bind(String.class).annotatedWith(Names.named("basePackage"))
				.toInstance(basePackage);


		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		


		logger.info("Installing modules");
		install(new ProviderModule());

		install(new SpeedyShiroWebModule(getServletContext()));
		install(SpeedyShiroWebModule.guiceFilterModule());
		install(new ShiroAopModule());
		install(new ShiroMethodInterceptorModule());
		
		install(new ComponentModule());
		install(new HandlerModule());

		logger.info("Finished installing modules");

		filter("/*").through(GuiceShiroFilter.class);

		serve("/*").with(GuiceContainer.class, params);


	}

	@Provides
	@Singleton
	public Set<Realm> realm() {

		return new HashSet<Realm>(Arrays.asList(new IniRealm(
				"classpath:shiro.ini")));
	}
	

}
