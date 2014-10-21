package com.github.mwmahlberg.speedy.persistence;

import javax.inject.Inject;
import javax.inject.Named;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.inject.Provider;
import com.mongodb.MongoClient;

public class MorphiaDatastoreProvider implements Provider<Datastore> {

	MongoClient mongoClient;

	Morphia morphia;
	
	String database;

	@Inject
	public MorphiaDatastoreProvider(MongoClient mongoClient, Morphia morphia,
			@Named("mongodb.database") String database) {
		this.mongoClient = mongoClient;
		this.morphia = morphia;
		this.database = database;
	}

	@Override
	public Datastore get() {
		return morphia.createDatastore(mongoClient, database);
	}

}
