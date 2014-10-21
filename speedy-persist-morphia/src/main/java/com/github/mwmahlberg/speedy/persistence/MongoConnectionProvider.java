package com.github.mwmahlberg.speedy.persistence;

import java.net.UnknownHostException;

import javax.inject.Named;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.sun.jersey.api.container.ContainerException;

public class MongoConnectionProvider implements Provider<MongoClient> {
	
	@Inject
	@Named("mongodb.connection")
	String uriString;


	@Override
	public MongoClient get() {
		try {
			MongoClientOptions.Builder builder = MongoClientOptions.builder().legacyDefaults().socketKeepAlive(true);
			return new MongoClient(new MongoClientURI(uriString,builder));
		} catch (UnknownHostException e) {
			throw new ContainerException("Problem creating connection to '" + this.uriString +"'",e);
		}
	}
}
