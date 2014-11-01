package com.github.mwmahlberg.speedy.handler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.shiro.authz.UnauthorizedException;

public class UnauthorizedExceptionHandler implements ExceptionMapper<UnauthorizedException>{

	public Response toResponse(UnauthorizedException exception) {		
		return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Authorization required").build();
	}

}
