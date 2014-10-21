package com.github.mwmahlberg.speedy.persistence;

import javax.inject.Inject;

import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import org.mongodb.morphia.logging.slf4j.SLF4JLoggerImplFactory;

import com.google.inject.Provider;
/*
 * Basically, this class is for future use
 * The morphia instance may be configurable
 */
public class MorphiaProvider implements Provider<Morphia> {

	static {
		MorphiaLoggerFactory.registerLogger(SLF4JLoggerImplFactory.class);
	}
	
	String packageName;
	
	
	@Inject
	public MorphiaProvider(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public Morphia get() {
		Morphia morphia = new Morphia();
		morphia.mapPackage(packageName);
		return morphia;
	}

}
