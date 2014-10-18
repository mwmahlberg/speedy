package com.github.mwmahlberg.speedy;

import java.io.Writer;

public interface TemplateEngine {
	
	public String render(ModelAndView modelAndView);

	public void render(ModelAndView modelAndView, Writer writer);
}
