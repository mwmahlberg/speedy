package com.github.mwmahlberg.speedy.template;

import java.io.Writer;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.github.mwmahlberg.speedy.ModelAndView;
import com.github.mwmahlberg.speedy.TemplateEngine;
import com.google.inject.Inject;

public class ThymeleafTemplateEngine implements TemplateEngine {

	@Inject
	ServletContext servletContext;

	private final org.thymeleaf.TemplateEngine thymeleaf;

	public ThymeleafTemplateEngine() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("META-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setOrder(1);
		templateResolver.setCacheable(true);
		templateResolver.setCacheTTLMs(null);
		templateResolver.setName("ApplicationTemplates");

		ClassLoaderTemplateResolver defaultTemplateResolver = new ClassLoaderTemplateResolver();
		defaultTemplateResolver.setTemplateMode("HTML5");
		defaultTemplateResolver.setPrefix("META-INF/speedy-templates/");
		defaultTemplateResolver.setSuffix(".html");
		defaultTemplateResolver.setOrder(2);
		defaultTemplateResolver.setCacheable(true);
		defaultTemplateResolver.setCacheTTLMs(null);
		defaultTemplateResolver.setName("FrameworkTemplates");

		thymeleaf = new org.thymeleaf.TemplateEngine();
		thymeleaf.addTemplateResolver(templateResolver);
		thymeleaf.addTemplateResolver(defaultTemplateResolver);
		thymeleaf.addDialect(new LayoutDialect());
		
		HashSet<ITemplateModeHandler> handlers = new HashSet<ITemplateModeHandler>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add(StandardTemplateModeHandlers.HTML5);
		}};

		thymeleaf.setTemplateModeHandlers(handlers);
	}

	public String render(ModelAndView modelAndView) {
		HttpServletRequest request = (HttpServletRequest) modelAndView
				.getModel().get("request");

		WebContext ctx = new WebContext(request,
				(HttpServletResponse) modelAndView.getModel().get("response"),
				servletContext, request.getLocale());
		
		ctx.setVariables(modelAndView.getModel());
		
		return thymeleaf.process(modelAndView.getView(), ctx);
	}

	@Override
	public void render(ModelAndView modelAndView, Writer writer) {
		
		HttpServletRequest request = (HttpServletRequest) modelAndView
				.getModel().get("request");

		WebContext ctx = new WebContext(request,
				(HttpServletResponse) modelAndView.getModel().get("response"),
				servletContext, request.getLocale());

		ctx.setVariables(modelAndView.getModel());
		thymeleaf.process(modelAndView.getView(), ctx, writer);
	}

}
