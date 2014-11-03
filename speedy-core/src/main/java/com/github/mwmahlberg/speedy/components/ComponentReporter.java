package com.github.mwmahlberg.speedy.components;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;

import com.github.mwmahlberg.speedy.annotations.Service;

import eu.infomas.annotation.AnnotationDetector.TypeReporter;


/**
 * Scans the classpath for classes annotated with {@link Service} and {@link Path}
 * @author markus
 *
 */
public class ComponentReporter implements TypeReporter {

	Set<Class<?>> components = new HashSet<Class<?>>();
	
	@SuppressWarnings("unchecked")
	public Class<? extends Annotation>[] annotations() {
		return new Class[]{Service.class,Path.class};
	}

	public void reportTypeAnnotation(Class<? extends Annotation> annotation,
			String className) {
		
		try {
			Class<?> c = Class.forName(className);
			components.add(c);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Set<Class<?>> getComponents() {
		return components;
	}

	public void setComponents(Set<Class<?>> services) {
		this.components = services;
	}
	

}
