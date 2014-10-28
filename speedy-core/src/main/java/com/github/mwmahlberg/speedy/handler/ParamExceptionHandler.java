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
