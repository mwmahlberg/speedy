package com.github.mwmahlberg.speedy.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.*;

public class ConfigHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(ConfigHelper.class);

	public static Properties getProperties(Set<String> props,
			Properties fileConfig) throws IOException {
		Properties defaults = new Properties();

		Properties config = new Properties();

		if (props == null) {
			props = new HashSet<String>();
		}

		if (props.size() > 0) {
			for (String string : props) {
				logger.info("Processing {}", string);

				InputStream is = ConfigHelper.class.getClassLoader().getResourceAsStream(string);

				if (is == null) {
					throw new IOException("Property file " + string
							+ " not found or not readable");
				} else if (string.startsWith("META-INF/_speedy")) {
					defaults.load(is);
					logger.info("Loaded default configuration from {}", string);
				} else {
					config.load(is);
					logger.info("Loaded configuration from {}", string);
				}

			}
		}

		Properties appConfig = new Properties();
		appConfig.putAll(defaults);
		appConfig.putAll(config);

		if (fileConfig != null) {
			appConfig.putAll(fileConfig);
		}

		appConfig.putAll(System.getProperties());

		logger.debug("defaults: {}", defaults);
		logger.debug("config: {}", config);
		logger.debug("fileConfig: {}", fileConfig);
		logger.debug("System properties: {}", System.getProperties());
		logger.debug("appConfig: {}", appConfig);
		return appConfig;
	}

}
