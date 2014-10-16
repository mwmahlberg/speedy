package com.github.mwmahlberg.speedy.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.github.mwmahlberg.speedy.ModelAndView;
import com.github.mwmahlberg.speedy.TemplateEngine;
import com.google.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Provider
@Produces(MediaType.TEXT_HTML)
public class ModelAndViewWriter implements MessageBodyWriter<ModelAndView> {

	
	@Inject
	TemplateEngine templateEngine;
	
	public long getSize(ModelAndView arg0, Class<?> arg1, Type arg2,
			Annotation[] arg3, MediaType arg4) {
		return -1;
	}

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		return type == ModelAndView.class;
	}

	public void writeTo(ModelAndView modelAndView, Class<?> arg1, Type arg2,
			Annotation[] arg3, MediaType arg4,
			MultivaluedMap<String, Object> arg5, OutputStream os)
			throws IOException, WebApplicationException {
			Writer osWriter = new OutputStreamWriter(os);
		    osWriter.write('\uFEFF');
			osWriter.write(templateEngine.render(modelAndView));
			osWriter.flush();
			osWriter.close();
	}

}
