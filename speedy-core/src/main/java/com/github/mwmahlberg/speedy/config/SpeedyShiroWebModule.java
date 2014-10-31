package com.github.mwmahlberg.speedy.config;

import javax.servlet.ServletContext;

import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;

import com.google.inject.name.Names;

class SpeedyShiroWebModule extends ShiroWebModule {

	public SpeedyShiroWebModule(ServletContext servletContext) {
		super(servletContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configureShiroWeb() {

		IniRealm realm = new IniRealm("classpath:shiro.ini");

		bindRealm().toInstance(realm);
		bindConstant().annotatedWith(Names.named("shiro.loginUrl"))
				.to("/login");

		addFilterChain("/", ANON);
		addFilterChain("/logout", LOGOUT);
		addFilterChain("/**", AUTHC);

//		ShiroWebModule.bindGuiceFilter(binder());
	}

}
