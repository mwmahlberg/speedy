package com.github.mwmahlberg.speedy.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.Provider;

public class JacksonJaxbJsonProviderProvider implements
		Provider<JacksonJaxbJsonProvider> {



	public JacksonJaxbJsonProvider get() {
		 JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(Annotations.JAXB);
		 provider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
		 return provider;
	}

}
