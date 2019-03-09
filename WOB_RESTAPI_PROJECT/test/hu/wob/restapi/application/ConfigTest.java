package hu.wob.restapi.application;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testLoad() {
		
		boolean actual = Config.load("config.properties");
		
		assertEquals(Boolean.TRUE, actual);
	}

	@Test
	void testGetProperty() {
		Config.load("config.properties");
		
		assertEquals("https://my.api.mockaroo.com/@class?key=63304c70", Config.getProperty("BASE_URL"));
		assertEquals("jsonReport.json", Config.getProperty("JSON_REPORT_NAME"));
	}

}
