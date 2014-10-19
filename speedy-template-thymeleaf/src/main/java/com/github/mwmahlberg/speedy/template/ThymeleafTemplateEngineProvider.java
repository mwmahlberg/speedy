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

import java.util.HashSet;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.inject.Provider;

public class ThymeleafTemplateEngineProvider implements
		Provider<TemplateEngine> {
	
	TemplateEngine thymeleaf;
	
	public ThymeleafTemplateEngineProvider()  {
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

	@Override
	public TemplateEngine get() {
		return thymeleaf;
	}

}
