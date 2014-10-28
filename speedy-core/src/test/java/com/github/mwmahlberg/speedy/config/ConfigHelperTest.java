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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.BeforeClass;
import org.junit.Test;

public final class ConfigHelperTest {

	Properties propertiesFromConfigFile;

	static HashSet<String> files = new HashSet<String>();

	/* Just to shut Cobertura up*/
	@SuppressWarnings("unused")
	private static ConfigHelper helper;

	@BeforeClass
	public static void checkResources() {
		/* Make sure our test environment is sane */
		assertNotNull(ConfigHelperTest.class.getClassLoader().getResource(
				"META-INF/_speedy/default.properties"));
		assertNotNull(ConfigHelperTest.class.getClassLoader().getResource(
				"META-INF/_speedy/moduleDefault.properties"));
		
		files.add("META-INF/_speedy/default.properties");
		files.add("META-INF/_speedy/moduleDefault.properties");
	}
	
	@Test
	public void testWithoutDefaults() throws ConfigurationException{
		helper = new ConfigHelper();
		Properties returned = ConfigHelper.getProperties(null, "test.properties");
		assertNull(returned.get("unreadable"));
	}

	@Test
	public final void testDefaultValuesOverridden() throws ConfigurationException {

		Properties returned = ConfigHelper.getProperties(files,
				"test.properties");

		assertNotEquals( "speedyDefaultValue",returned.get("testproperty"));
		assertNotEquals("speedyDefaultValue",returned.get("moduleproperty") );
	}

	@Test
	public final void testDefaultValues() throws ConfigurationException {
		Properties returned = ConfigHelper.getProperties(files, null);
		assertEquals("speedyDefaultValue",returned.get("testproperty"));
		assertEquals( "speedyDefaultValue",returned.get("moduleproperty"));
	}
	
	@Test(expected=ConfigurationException.class)
	public final void testIllegalPath() throws ConfigurationException {
		HashSet<String> invalidIncludedFiles = files;
		invalidIncludedFiles.add("does/not/exist");
		Properties returned = ConfigHelper.getProperties(invalidIncludedFiles, "test.properties");
		assertNotEquals(returned.get("testproperty"), "speedyDefaultValue");
		assertNotEquals(returned.get("moduleproperty"), "speedyDefaultValue");
		files.remove("does/not/exist");
	}
}
