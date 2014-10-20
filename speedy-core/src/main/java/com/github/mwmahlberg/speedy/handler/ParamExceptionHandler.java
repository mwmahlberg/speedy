package com.github.mwmahlberg.speedy.handler;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.ParamException;
import com.sun.jersey.api.view.Viewable;

@Singleton
@Provider
public class ParamExceptionHandler implements ExceptionMapper<ParamException> {

	@Override
	public Response toResponse(ParamException exception) {
		// @see https://jersey.java.net/apidocs/1.18/jersey/
		return Response.status(Status.BAD_REQUEST).entity(new Viewable("_status/BadRequest", exception)).build();
	}

}
