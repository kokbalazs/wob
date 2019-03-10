package hu.wob.restapi.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.constants.IConstants;
import hu.wob.restapi.constants.IQueries;
import hu.wob.restapi.models.Database;
import hu.wob.restapi.models.Report;

class ReportControllerTest {
	
	private DatabaseController dc;

	@BeforeEach
	void setUp() throws Exception {
		Config.load(IConstants.configFile);
		Database db = new Database();
		db.setDbUrl(Config.getProperty("JDBC_URL"));
		db.setDbUser(Config.getProperty("JDBC_USER"));
		db.setDbPassword(Config.getProperty("JDBC_PW"));
		
		dc = new DatabaseController(db);
		dc.connect();
	}

	@Test
	void testGetReportData() {
		Set<Report> reportDatas = new TreeSet<Report>();
		ReportController rc = new ReportController();
		rc.getReportData(dc.executeQuery(IQueries.mainReportQuery), reportDatas, Boolean.FALSE);
		
		if (reportDatas.isEmpty()) {
			fail("Hiba a json nem havi riport kiolvasásakor.");
		} else {
			reportDatas.clear();
		}
		
		rc.getReportData(dc.executeQuery(IQueries.monthlyReportQuery), reportDatas, Boolean.TRUE);
		if (reportDatas.isEmpty()) {
			fail("Hiba a json havi riport kiolvasásakor.");
		} 
		dc.disconnect();
	}

	@Test
	void testGetJsonString() {
		Set<Report> reportDatas = new TreeSet<Report>();
		ReportController rc = new ReportController();
		rc.getReportData(dc.executeQuery(IQueries.mainReportQuery), reportDatas, Boolean.FALSE);
		String jsonString = rc.getJsonString(reportDatas);
		
		JSONParser parser = new JSONParser();
		try {
			parser.parse(jsonString);
		} catch (ParseException e) {
			fail("Hiba a JSON String kinyerésekor");
		} finally {
			dc.disconnect();
		}
	}

	@Test
	void testCreateJsonFile() {
		ReportController rc = new ReportController();
		File file = new File(rc.createJsonFile("kak.bat"));
		
		assertEquals(Boolean.TRUE, file.exists());
		
		dc.disconnect();
	}

}
