package hu.wob.restapi.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.constants.IConstants;
import hu.wob.restapi.models.Ftp;

class FtpControllerTest {
	
	private File jsonFile;
	private Ftp ftp;

	@BeforeEach
	void setUp() throws Exception {
		Config.load(IConstants.configFile);
		jsonFile = new File(Config.getProperty("JSON_REPORT_LOCATION") + File.separator + Config.getProperty("JSON_REPORT_NAME"));
		if (!jsonFile.exists()) {
			jsonFile.createNewFile();
		}
		ftp = new Ftp();
		ftp.setFtpIp(Config.getProperty("FTP_IP"));
		ftp.setFtpPort(Integer.parseInt(Config.getProperty("FTP_PORT")));
		ftp.setFtpUser(Config.getProperty("FTP_USER"));
		ftp.setFtpPassword(Config.getProperty("FTP_PW"));
	}

	@Test
	void testFtpController() {
		FtpController fc = new FtpController(ftp);
		
		assertEquals(ftp, fc.getFtp());
	}

	@Test
	void testConnect() {
		FtpController fc = new FtpController(ftp);
		assertEquals(Boolean.TRUE, fc.connect());
	}

	@Test
	void testUploadFile() {
		FtpController fc = new FtpController(ftp);
		fc.connect();
		
		assertEquals(Boolean.TRUE, fc.uploadFile(jsonFile));
		
		fc.disconnect();
	}

	@Test
	void testDisconnect() {
		FtpController fc = new FtpController(ftp);
		fc.connect();
		fc.disconnect();
		
		assertEquals(Boolean.FALSE, fc.getFtpClient().isConnected());
	}

}
