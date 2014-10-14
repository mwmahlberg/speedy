package com.github.mwmahlberg.speedy.template;

import com.github.mwmahlberg.speedy.TemplateEngine;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ThymeleafModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TemplateEngine.class).to(ThymeleafTemplateEngine.class).in(Scopes.SINGLETON);;
	}

}
