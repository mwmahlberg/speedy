package com.github.mwmahlberg.speedy.config;

import javax.inject.Singleton;
import javax.servlet.ServletContext;

import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.filter.PathConfigProcessor;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provides;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;

class SpeedyShiroWebModule extends ShiroWebModule {

	Logger logger = LoggerFactory.getLogger(getClass());

	public SpeedyShiroWebModule(ServletContext servletContext) {
		super(servletContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configureShiroWeb() {

		// IniRealm realm = new IniRealm("classpath:shiro.ini");

		// bindRealm().toInstance(realm);
		bindConstant().annotatedWith(Names.named("shiro.loginUrl"))
				.to("/login");

		// bind(PathConfigProcessor.class).to(PassThruAuthenticationFilter.class);
		// bindConstant().annotatedWith(Names.named("shiro.authc.unauthorizedUrl")).to("/unauthorized");
		//
		// bindConstant().annotatedWith(Names.named("shiro.roles.unauthorizedUrl")).to("/unauthorized");
		//
		// bindConstant().annotatedWith(Names.named("shiro.perms.unauthorizedUrl")).to("/unauthorized");

		addFilterChain("/logout", LOGOUT);
		addFilterChain("/login", AUTHC);
		// addFilterChain("/", ANON);
		// addFilterChain("/unauthorized", ANON);
		addFilterChain("/**", AUTHC);


		logger.info("Configured ShiroWebModule");

	}

//	
//	@Override
//	protected void bindSessionManager(
//			AnnotatedBindingBuilder<SessionManager> bind) {
//		bind.to(ServletContainerSessionManager.class);
//	}
	
	
}
