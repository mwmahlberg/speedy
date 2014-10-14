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
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.mwmahlberg.speedy.config.ApplicationConfig;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;

public class SpeedyApplication {

	private Server jetty;

	private final String basePackage;

	public SpeedyApplication(String basePackage) {
		this.basePackage = basePackage;
	}

	public void configure(String[] args, Module... modules) {

		Options options = new Options();
		options.addOption("h", "host", Boolean.TRUE,
				"IP address the server should bind to.\nDefaults to \"0.0.0.0\" for all IPs");
		options.addOption("p", "port", true,
				"port the server should bind to.\nDefaults to 8080");
		options.addOption(null, "help", false, "prints this message");

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

			jetty.setConnectors(new Connector[] { connector });

			ServletContextHandler context = new ServletContextHandler();

			/*
			 * Filter everything through Guice, so that @Inject's can be done
			 */
			context.addFilter(GuiceFilter.class, "/*",
					EnumSet.of(DispatcherType.REQUEST, DispatcherType.INCLUDE));

			/*
			 * Configure the application Jersey is added via binding
			 */
			context.addEventListener(new ApplicationConfig(basePackage, modules));

			jetty.setHandler(context);

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void run() throws Exception {
		jetty.start();
		jetty.join();
	}

}
