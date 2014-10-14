package com.github.mwmahlberg.speedy.template;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.github.mwmahlberg.speedy.ModelAndView;
import com.github.mwmahlberg.speedy.TemplateEngine;
import com.google.inject.Inject;

public class ThymeleafTemplateEngine implements TemplateEngine {

	@Inject
	ServletContext servletContext;

	private org.thymeleaf.TemplateEngine thymeleaf;

	public ThymeleafTemplateEngine() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("META-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(3600000L);
		thymeleaf = new org.thymeleaf.TemplateEngine();
		thymeleaf.setTemplateResolver(templateResolver);
		thymeleaf.addDialect(new LayoutDialect());

	}

	public String render(ModelAndView modelAndView) {
		WebContext ctx = new WebContext((HttpServletRequest) modelAndView
				.getModel().get("request"), (HttpServletResponse) modelAndView
				.getModel().get("response"), servletContext);
		ctx.setVariables(modelAndView.getModel());
		return thymeleaf.process(modelAndView.getView(), ctx);
	}

}
