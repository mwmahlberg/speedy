package com.github.mwmahlberg.speedy.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ProviderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(
				Scopes.SINGLETON);

		bind(JacksonJaxbJsonProvider.class).toProvider(
				JacksonJaxbJsonProviderProvider.class).in(Scopes.SINGLETON);
		
	}

}
