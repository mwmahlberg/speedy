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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.mwmahlberg.speedy.Service;
import com.github.mwmahlberg.speedy.handler.DefaultWebApplicationExceptionHandler;
import com.github.mwmahlberg.speedy.handler.NotFoundExceptionHandler;
import com.github.mwmahlberg.speedy.handler.ParamExceptionHandler;
import com.github.mwmahlberg.speedy.provider.JacksonJaxbJsonProviderProvider;
import com.github.mwmahlberg.speedy.provider.ObjectMapperProvider;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class SpeedyConfig extends JerseyServletModule {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	String basePackage;

	String configFile;

	final static Pattern CONFIGS = Pattern.compile("speedy-.*\\.properties");

	public SpeedyConfig(String basePackage, String configFile) {

		if (basePackage == null) {
			throw new IllegalArgumentException("basePackage must not be null");
		}
		else if(basePackage.length() < 1) {
			throw new IllegalArgumentException("basePackage name must contain at least one caracter");
		}
		
		this.configFile = configFile;
		
	}

	@Override
	protected void configureServlets() {

		Reflections reflection = Reflections.collect();

		/* Bind configuration parameters from various config locations
		 * to @Named
		 */
		try {
		Names.bindProperties(binder(), ConfigHelper.getProperties(
				reflection.getResources(CONFIGS), configFile));
		} catch(ConfigurationException e) {
			logger.error("Configuration failed", e);
			System.exit(5);
		}
		bind(String.class).annotatedWith(Names.named("basePackage"))
				.toInstance(basePackage);

		bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(
				Scopes.SINGLETON);
		
		bind(JacksonJaxbJsonProvider.class).toProvider(
				JacksonJaxbJsonProviderProvider.class).in(Scopes.SINGLETON);

		bind(GuiceContainer.class);

		Set<Class<?>> components = new HashSet<Class<?>>();

		Set<Class<?>> controllers = reflection
				.getTypesAnnotatedWith(Path.class);
		logger.info("Found {} controllers", controllers.size());
		components.addAll(controllers);

		Set<Class<?>> services = reflection
				.getTypesAnnotatedWith(Service.class);
		logger.info("Found {} services", services.size());
		components.addAll(services);

		for (Class<?> component : components) {
			bind(component);
		}

		bind(NotFoundExceptionHandler.class);
		bind(DefaultWebApplicationExceptionHandler.class);
		bind(ParamExceptionHandler.class);

		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		serve("/*").with(GuiceContainer.class, params);
	}

}
