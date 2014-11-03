package com.github.mwmahlberg.speedy.components;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import eu.infomas.annotation.AnnotationDetector;

/**
 * Binds classes discovered by {@link ComponentReporter} into the {@link GuiceContainer}.
 * 
 * @author markus
 *
 */
public class ComponentModule extends AbstractModule {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void configure() {
		Set<Class<?>> components = new HashSet<Class<?>>();

		ComponentReporter componentReporter = new ComponentReporter();
		AnnotationDetector cf = new AnnotationDetector(componentReporter);

		try {
			cf.detect();
		} catch (IOException e) {
			logger.error("Could not process components!", e);
			System.exit(5);
		}

		components.addAll(componentReporter.getComponents());

		logger.info("Found {} components", components.size());

		for (Class<?> component : components) {
			bind(component);
		}
	}

}
