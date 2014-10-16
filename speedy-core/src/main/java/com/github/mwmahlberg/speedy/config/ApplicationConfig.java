package com.github.mwmahlberg.speedy.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public class ApplicationConfig extends GuiceServletContextListener {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private ArrayList<Module> modules;

	public ApplicationConfig(String basePackage, Module... modules) {

		this.modules = new ArrayList<Module>();
		if (modules.length > 0) {
			this.modules.addAll(Arrays.asList(modules));
		}
		this.modules.add(new SpeedyConfig(basePackage));
	}

	@Override
	protected Injector getInjector() {
		Module[] mods = new Module[modules.size()];
		mods = modules.toArray(mods);
		return Guice.createInjector(mods);
	}

}
