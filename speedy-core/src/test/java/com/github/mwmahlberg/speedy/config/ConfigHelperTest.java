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


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigHelperTest {

	Properties propertiesFromConfigFile;

	static HashSet<String> files = new HashSet<String>();

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

	@Before
	public void setUp() throws Exception {
		propertiesFromConfigFile = new Properties();
		propertiesFromConfigFile.load(getClass().getResourceAsStream(
				"/test.properties"));

	}

	@Test
	public final void testGetSystemPropertiesOnly() throws IOException {
		
		Properties returned = ConfigHelper.getProperties(null, null);
		assertNotNull("Properties returned must not be null", returned);
		assertEquals(returned, System.getProperties());
		
	}
	
	@Test
	public final void testWithoutDefaults() throws IOException{
		Properties returned = ConfigHelper.getProperties(null, propertiesFromConfigFile);
		assertNull(returned.get("unreadable"));
	}

	@Test
	public final void testDefaultValuesOverridden() throws IOException {

		Properties returned = ConfigHelper.getProperties(files,
				propertiesFromConfigFile);

		assertNotEquals(returned.get("testproperty"), "speedyDefaultValue");
		assertNotEquals(returned.get("moduleproperty"), "speedyDefaultValue");
	}

	@Test
	public final void testDefaultValues() throws IOException {
		Properties returned = ConfigHelper.getProperties(files, null);
		assertEquals(returned.get("testproperty"), "speedyDefaultValue");
		assertEquals(returned.get("moduleproperty"), "speedyDefaultValue");
	}
	
	@Test(expected=IOException.class)
	public final void testIllegalPath() throws IOException {
		HashSet<String> invalidIncludedFiles = files;
		invalidIncludedFiles.add("does/not/exist");
		Properties returned = ConfigHelper.getProperties(invalidIncludedFiles, propertiesFromConfigFile);
		assertNotEquals(returned.get("testproperty"), "speedyDefaultValue");
		assertNotEquals(returned.get("moduleproperty"), "speedyDefaultValue");
		files.remove("does/not/exist");
	}
}
