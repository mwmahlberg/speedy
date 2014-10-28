package com.github.mwmahlberg.speedy.config;

import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(ConfigHelper.class);

	public static Properties getProperties(Set<String> props, String configFile)
			throws ConfigurationException {

		CombinedConfiguration cc = new CombinedConfiguration();

		SystemConfiguration systemConfiguration = new SystemConfiguration();

		PropertiesConfiguration configFileConfiguration = new PropertiesConfiguration();

		if (configFile != null) {
			configFileConfiguration = new PropertiesConfiguration(configFile);
		}

		/* Environment parameters have precendence */
		cc.addConfiguration(systemConfiguration);

		/* Next is the users config file */
		cc.addConfiguration(configFileConfiguration);

		if (props == null) {
			return ConfigurationConverter.getProperties(cc);
		}

		for (String string : props) {

			PropertiesConfiguration file = new PropertiesConfiguration(string);
			cc.addConfiguration(file);
		}

		return ConfigurationConverter.getProperties(cc);

	}
}
