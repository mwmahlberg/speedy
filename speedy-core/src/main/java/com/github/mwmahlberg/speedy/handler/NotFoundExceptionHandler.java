/*
 * Copyright 2014 Markus W Mahlberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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