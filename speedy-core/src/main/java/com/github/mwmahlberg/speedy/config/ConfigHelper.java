package com.github.mwmahlberg.speedy.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);
	
	public static Properties getProperties(Set<String> props, Properties fileConfig){
		Properties defaults = new Properties();

		Properties config = new Properties();

		if (props.size() > 0) {
			for (String string : props) {
				logger.info("Processing {}", string);
				try {
					InputStream is = ConfigHelper.class.getClassLoader()
							.getResourceAsStream(string);

					if (string.startsWith("META-INF/_speedy")) {
						defaults.load(is);
						logger.info("Loaded default configuration from {}",
								string);
					} else {
						config.load(is);
						logger.info("Loaded configuration from {}", string);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Properties appConfig = new Properties();
		appConfig.putAll(defaults);
		appConfig.putAll(config);
		appConfig.putAll(fileConfig);
		appConfig.putAll(System.getProperties());

		logger.debug("defaults: {}", defaults);
		logger.debug("config: {}", config);
		logger.debug("fileConfig: {}", fileConfig);
		logger.debug("System properties: {}", System.getProperties());
		logger.debug("appConfig: {}", appConfig);
		return appConfig;
	}

}
