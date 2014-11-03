package com.github.mwmahlberg.speedy.handler;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.authz.AuthorizationException;

@Singleton
@Provider
public class AuthorizationExceptionMapper implements ExceptionMapper<AuthorizationException>{

	public Response toResponse(AuthorizationException exception) {
		
		return Response.status(Status.UNAUTHORIZED).entity("Not Authorized").build();
	}

}
