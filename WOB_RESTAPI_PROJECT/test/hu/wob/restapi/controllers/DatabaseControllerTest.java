package hu.wob.restapi.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.constants.IConstants;
import hu.wob.restapi.constants.IQueries;
import hu.wob.restapi.models.Database;

class DatabaseControllerTest {
	
	private Database db;
	private DatabaseController dc;

	@BeforeEach
	void setUp() throws Exception {
		Config.load(IConstants.configFile);
		db = new Database();
		db.setDbUrl(Config.getProperty("JDBC_URL"));
		db.setDbUser(Config.getProperty("JDBC_USER"));
		db.setDbPassword(Config.getProperty("JDBC_PW"));
	}
	

	@Test
	void testDatabaseController() {
		dc = new DatabaseController(db);
		
		assertEquals(db, dc.getDb());
	}

	@Test
	void testConnect() {
		dc = new DatabaseController(db);
		
		assertEquals(Boolean.TRUE, dc.connect());
	}

	@Test
	void testPrepareQuery() {
		
		dc = new DatabaseController(db);
		dc.connect();
		
		String testQuery = IQueries.testPrepareQuery;
		dc.prepareQuery(testQuery);
		
		assertEquals(testQuery, dc.getPs().toString());
	}

	@Test
	void testDisconnect() {
		dc = new DatabaseController(db);
		dc.connect();
		dc.disconnect();
		try {
			assertEquals(Boolean.TRUE, dc.getConnection().isClosed());
		} catch (SQLException e) {
			fail("SQL hiba csatlakozás zárás ellenõrzésekor");
		}
	}

	@Test
	void testExecuteQuery() {
		dc = new DatabaseController(db);
		dc.connect();
		
		ResultSet rs = dc.executeQuery(IQueries.testExecuteQuery);
		
		try {
			assertEquals(Boolean.TRUE, rs.next());
		} catch (SQLException e) {
			fail("SQL hiba executeQuery ellenõrzésekor");
		}
		
	}

}
