package com.github.mwmahlberg.speedy.provider;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class JacksonJaxbJsonProviderProviderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testGet() {
		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProviderProvider().get();
		assertNotNull(provider);
		/* TODO add isReadable and isWritable checks for Json annotated
		 * JAXB annotated and mixed annotated classes
		 * This does not break unit testing, since we have very specific requirements
		 * to the provider.
		 */

		assertTrue(provider.isWriteable(JAXBAnnotated.class,String.class,JAXBAnnotated.class.getAnnotations() , MediaType.APPLICATION_JSON_TYPE));
	}

}
