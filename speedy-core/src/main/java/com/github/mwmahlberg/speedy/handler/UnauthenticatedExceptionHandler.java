package com.github.mwmahlberg.speedy.handler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;

public  class UnauthenticatedExceptionHandler implements ExceptionMapper<UnauthenticatedException>{

	public Response toResponse(UnauthenticatedException exception) {		
		return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Authorization required").build();
	}

}
