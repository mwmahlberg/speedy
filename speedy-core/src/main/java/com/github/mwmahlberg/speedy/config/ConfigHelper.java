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

import com.github.mwmahlberg.speedy.SpeedyApplication;

/**
 * Helper class for configuration file processing.
 * @author markus
 *
 */
public class ConfigHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(ConfigHelper.class);

	/**
	 * Loads property files from various locations.
	 * @param props property file locations
	 * @param configFile as given by the user with the -f command line
	 *        option of {@link SpeedyApplication}
	 * @return Flattened properties representing the key/value pairs of <u>all</u> config files
	 * @throws ConfigurationException
	 */
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
			logger.info("Processing configuration file {}",string);
			PropertiesConfiguration file = new PropertiesConfiguration(string);
			cc.addConfiguration(file);
		}

		return ConfigurationConverter.getProperties(cc);

	}
}
