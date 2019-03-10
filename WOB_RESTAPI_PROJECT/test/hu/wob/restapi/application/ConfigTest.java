package hu.wob.restapi.application;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.wob.restapi.constants.IConstants;

class ConfigTest {

	@Test
	void testLoad() {
		assertEquals(Boolean.TRUE, Config.load(IConstants.configFile));
	}

	@Test
	void testGetProperty() {
		Config.load(IConstants.configFile);
		
		assertEquals("https://my.api.mockaroo.com/@class?key=63304c70", Config.getProperty("BASE_URL"));
		assertEquals("jsonReport.json", Config.getProperty("JSON_REPORT_NAME"));
	}

}
