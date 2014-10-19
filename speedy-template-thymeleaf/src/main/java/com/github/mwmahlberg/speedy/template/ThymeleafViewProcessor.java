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

package com.github.mwmahlberg.speedy.template;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;

@Provider
public class ThymeleafViewProcessor implements ViewProcessor<String>{

	@Context
	ServletContext servletContext;
	
	@Context
	HttpServletRequest request;
	
	@Context
	HttpServletResponse response;
	
	@Inject
	TemplateEngine thymeleaf;
	
	@Override
	public String resolve(String name) {
		return name;
	}

	@Override
	public void writeTo(String t, Viewable viewable, OutputStream out)
			throws IOException {
		
		// Send the headers
		out.flush();
		
		WebContext ctx = new WebContext(request, response, servletContext,request.getLocale());
		
		// Thymeleaf wants a ready made writer for writing to out
		OutputStreamWriter outStreamWriter = new OutputStreamWriter(out);
		ctx.setVariables((HashMap<String, Object>)viewable.getModel());
		thymeleaf.process(viewable.getTemplateName(), ctx, outStreamWriter);
		
		// Thymeleaf does not flush the writer ot the underlying output stream
		outStreamWriter.flush();
		outStreamWriter.close();
//		out.flush();
//		out.close();
	}

}
