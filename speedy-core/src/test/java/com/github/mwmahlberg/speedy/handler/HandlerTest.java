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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.ParamException;
import com.sun.jersey.api.view.Viewable;

/**
 * @author markus
 *
 */
@RunWith(Parameterized.class)
public class HandlerTest {

	Object responseObject;

	String templateName;

	int status;

	public HandlerTest(Response response, String template, int status) {
		this.responseObject = response;
		this.templateName = template;
		this.status = status;
	}

	@Parameters(name = "CHECKING {1}")
	public static Iterable<Object[]> data() {

		return Arrays
				.asList(new Object[][] {
						{
								new NotFoundExceptionHandler()
										.toResponse(new NotFoundException(
												"testmessage")),
								"_status/NotFound", 404 },
						{
								new DefaultWebApplicationExceptionHandler()
										.toResponse(new WebApplicationException()),
								"_status/Default", 500 },
						{
								new ParamExceptionHandler()
										.toResponse(new ParamException.PathParamException(
												new Exception(), "foo", "42")),
								"_status/BadRequest", 400 }

				});

	}

	@Test
	public void testHandlerValidResponse() {
		assertTrue("Handler must return a Response object",
				responseObject instanceof Response);
		Response response = (Response) responseObject;

		assertTrue(response.getStatus() == status);

		assertTrue(
				"Handler must send a Viewable as entity of the Response object",
				response.getEntity() instanceof Viewable);
		Viewable viewable = (Viewable) response.getEntity();

		assertNotNull("Viewable must contain a model", viewable.getModel());
		assertNotNull("Viewable must have a template Name",
				viewable.getTemplateName());
	}

	@Test
	public void testHandlerReturnedValidTemplateName() {
		assertTrue("Handler must return a Response object",
				responseObject instanceof Response);
		Response response = (Response) responseObject;

		assertTrue(
				"Handler must send a Viewable as entity of the Response object",
				response.getEntity() instanceof Viewable);
		Viewable viewable = (Viewable) response.getEntity();

		assertNotNull("Template name must be returned",
				viewable.getTemplateName());

		assertTrue("The returned template name violates naming conventions",
				viewable.getTemplateName().equals(templateName));
	}

	@Test
	public void testHandlerReturnedValidModel() {
		assertTrue("Handler must return a Response object",
				responseObject instanceof Response);
		Response response = (Response) responseObject;

		assertTrue(
				"Handler must send a Viewable as entity of the Response object",
				response.getEntity() instanceof Viewable);
		Viewable viewable = (Viewable) response.getEntity();

		assertNotNull("Handler must return a model", viewable.getModel());
		assertTrue("Handler must pass the Exception to the TemplateEngine",
				viewable.getModel() instanceof Exception);
	}
}
