package com.github.mwmahlberg.speedy.persistence;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.mongodb.MongoClient;

public class MorphiaModule extends AbstractModule {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	String uriString;
	

	
	public MorphiaModule(String uriString) {
		this.uriString = uriString;
	}
	
	public MorphiaModule() {
	}

	@Override
	protected void configure() {
		
		if(uriString != null) {
			bind(String.class).annotatedWith(Names.named("mongodb.connection")).toInstance(uriString);
		}
		
		bind(MongoClient.class).toProvider(MongoConnectionProvider.class).in(Scopes.SINGLETON);
		bind(Morphia.class).toProvider(MorphiaProvider.class).in(Scopes.SINGLETON);
		bind(Datastore.class).toProvider(MorphiaDatastoreProvider.class);
	}

}
