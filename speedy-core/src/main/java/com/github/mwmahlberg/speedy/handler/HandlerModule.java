package com.github.mwmahlberg.speedy.handler;

import com.google.inject.AbstractModule;

public class HandlerModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(NotFoundExceptionHandler.class);
		bind(ParamExceptionHandler.class);
		bind(UnauthorizedExceptionHandler.class);
		bind(DefaultWebApplicationExceptionHandler.class);
		bind(UnauthenticatedExceptionHandler.class);		
	}

}
