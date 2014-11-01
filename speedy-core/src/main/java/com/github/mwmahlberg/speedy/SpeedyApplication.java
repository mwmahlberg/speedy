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

package com.github.mwmahlberg.speedy;

import java.util.EnumSet;
import java.util.logging.LogManager;

import javax.servlet.DispatcherType;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.shiro.guice.web.GuiceShiroFilter;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.mwmahlberg.speedy.config.ApplicationConfig;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;

public class SpeedyApplication {

	private Server jetty;

	private final String basePackage;

	Logger logger = LoggerFactory.getLogger(getClass());

	public SpeedyApplication(String basePackage) {
		this.basePackage = basePackage;
	}

	public void configure(String[] args, Module... modules) {

		Options options = new Options();
		options.addOption("h", "host", Boolean.TRUE,
				"IP address the server should bind to.\nDefaults to \"0.0.0.0\" for all IPs");
		options.addOption("p", "port", true,
				"port the server should bind to.\nDefaults to 8080");
		options.addOption("f", "file", true,
				"path to optional configuration file");
		options.addOption(null, "help", false, "prints this message");

		options.addOption("n", "noauth", false,
				"disable authentication and authorization");

		CommandLineParser parser = new BasicParser();
		try {
			CommandLine line = parser.parse(options, args, true);

			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("speedy", options);
				System.exit(0);
			}

			/*
			 * Prevent Jersey from logging ugly
			 */
			LogManager.getLogManager().reset();
			SLF4JBridgeHandler.install();

			/*
			 * Set up jetty and it's connectors
			 */

			jetty = new Server();
			ServerConnector connector = new ServerConnector(jetty);
			connector.setHost(line.getOptionValue("host", "0.0.0.0"));
			connector.setPort(Integer.parseInt(line.getOptionValue("port",
					"8080")));

			connector.setName(basePackage);

			jetty.setStopAtShutdown(true);
			jetty.setConnectors(new Connector[] { connector });

			ServletContextHandler context = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			Boolean secured = Boolean.TRUE;

			if (line.hasOption("noauth")) {
				secured = Boolean.FALSE;
			}
			// context.addEventListener(new EnvironmentLoaderListener());
			context.addEventListener(new ApplicationConfig(basePackage, line
					.getOptionValue("f"), secured, modules));
			
			jetty.setHandler(context);

			/*
			 * Filter everything through Guice, so that @Inject's can be done
			 */

			context.addFilter(GuiceFilter.class, "/*", EnumSet
					.<javax.servlet.DispatcherType> of(
							javax.servlet.DispatcherType.REQUEST,
							javax.servlet.DispatcherType.ERROR,
							javax.servlet.DispatcherType.INCLUDE,
							javax.servlet.DispatcherType.FORWARD
							));


		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void run() throws Exception {
		jetty.start();
		jetty.join();
	}

}