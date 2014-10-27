package com.github.mwmahlberg.speedy.handler;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.view.Viewable;

@Singleton
@Provider
public class DefaultWebApplicationExceptionHandler implements
		ExceptionMapper<WebApplicationException> {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Response toResponse(WebApplicationException exception) {
		
		logger.warn("WebApplicationException cought!", exception);
		
		return Response.status(500)
				.entity(new Viewable("_status/Default", exception)).build();
	}
}
