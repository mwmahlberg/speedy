package com.github.mwmahlberg.speedy.handler;

import java.util.HashMap;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.github.mwmahlberg.speedy.ModelAndView;
import com.github.mwmahlberg.speedy.TemplateEngine;
import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

@Singleton
@Provider
public class NotFoundExceptionHandler implements
		ExceptionMapper<NotFoundException> {

	@Context
	HttpServletRequest request;

	@Inject(optional = true)
	TemplateEngine engine;
	
	@Override
	public Response toResponse(NotFoundException exception) {

		String message = "not found";
		if (engine == null) {
			message = new StringBuilder().append("<html><head><title>")
					.append("404: Not Found")
					.append("</title><head><body><h1>")
					.append(request.getPathInfo()).append(" not found")
					.append("</h1></body></html>").toString();
		} else {
			HashMap<String,Object> model = new HashMap<String, Object>();
			model.put("exception", exception);
			model.put("request", request);
			message = engine.render(new ModelAndView(model, "_status/404"));
			
		}
		return Response.status(404).type("text/html").entity(message).build();
	}

}
