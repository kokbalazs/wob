package hu.wob.restapi.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import hu.wob.restapi.constants.IQueries;
import hu.wob.restapi.controllers.DatabaseController;
import hu.wob.restapi.controllers.FtpController;
import hu.wob.restapi.controllers.ReportController;
import hu.wob.restapi.helpers.MockarooParser;
import hu.wob.restapi.helpers.Validator;
import hu.wob.restapi.models.Database;
import hu.wob.restapi.models.Ftp;
import hu.wob.restapi.models.Listing;
import hu.wob.restapi.models.ListingStatus;
import hu.wob.restapi.models.Location;
import hu.wob.restapi.models.Marketplace;
import hu.wob.restapi.models.Report;
import hu.wob.restapi.models.ValidationError;

/**
 * Program kezelése
 * @author Kókay Balázs
 *
 */
public class Application {
	
	Set<Marketplace> marketplaces;
	Set<Location> locations;
	Set<ListingStatus> listingstatuses;
	Set<Listing> listings;
	
	/**
	 * Program indítása
	 */
	public void run() {
		
		//Konfiguráció betöltése
		if (!Config.load("config.properties")) {
			System.err.println("Nem sikerült a beállítások betöltése.");
			System.exit(1);
		}
		
		//REST API hívása, és adatok kiolvasása
		MockarooParser mockarooParser = new MockarooParser();
		marketplaces = mockarooParser.getMarketPlaces();
		listingstatuses = mockarooParser.getListingStatuses();
		locations = mockarooParser.getLocations();
		listings = mockarooParser.getListings();
		
		//Adatok validálása
		validateData();
		
		//Csatlakozás az adatbázishoz
		Database db = new Database();
		db.setDbUrl(Config.getProperty("JDBC_URL"));
		db.setDbUser(Config.getProperty("JDBC_USER"));
		db.setDbPassword(Config.getProperty("JDBC_PW"));
		DatabaseController dc = new DatabaseController(db);
		if (dc.connect()) {
			
			//Letárolás az adatbázisba
			saveDataToDatabase(dc);
			System.out.println("Adatbázisba szúrás elkészült.");
			
			//Riport adatok kiolvasása
			Set<Report> reportDatas = new TreeSet<Report>();
			ReportController rc = new ReportController();
			rc.getReportData(dc.executeQuery(IQueries.mainReportQuery), reportDatas, Boolean.FALSE);
			rc.getReportData(dc.executeQuery(IQueries.monthlyReportQuery), reportDatas, Boolean.TRUE);
			dc.disconnect();
			String jsonReport = rc.getJsonString(reportDatas);
			System.out.println("Json String: " + jsonReport);
			
			String jsonFile = rc.createJsonFile(jsonReport);
			System.out.println("Fájl helye: " + jsonFile);
			
			//Riport fájl feltöltése FTP-re
			if (!jsonFile.equals("")) {
				//FTP
				Ftp ftp = new Ftp();
				ftp.setFtpIp(Config.getProperty("FTP_IP"));
				ftp.setFtpPort(Integer.parseInt(Config.getProperty("FTP_PORT")));
				ftp.setFtpUser(Config.getProperty("FTP_USER"));
				ftp.setFtpPassword(Config.getProperty("FTP_PW"));
				
				FtpController fc = new FtpController(ftp);
				System.out.println("Csatlakozás az FTP-hez.");
				if (fc.connect()) {
					if (fc.uploadFile(new File(jsonFile))) {
						System.out.println("A fájl sikeresen feltöltve FTP-re.");
					} else {
						System.out.println("A fájl feltöltése sikertelen.");
					}
					fc.disconnect();
				} else {
					System.out.println("Sikertelen csatlakozás.");
				}
				
			}
			
			
		} else {
			System.out.println("Nem sikerült csatlakozni az adatbázishoz.");
		}
		
	}
	
	/**
	 * Halmazok alapján feltölti az adatbázist.
	 * @param dc - Adatbázis kezelõ
	 */
	private void saveDataToDatabase(DatabaseController dc) {
		String insertQuery = "";
		
		//Marketplace-k letárolása
		insertQuery = IQueries.marketplaceInsert;
		dc.prepareQuery(insertQuery);
		for (Marketplace marketplace : marketplaces) {
			dc.addToStatement(1, marketplace.getId());
			dc.addToStatement(2, marketplace.getMarketplaceName());
			dc.executeStatement();
		}
		
		//Listing_statusok letárolása
		insertQuery = IQueries.listingStatusInsert;
		dc.prepareQuery(insertQuery);
		for (ListingStatus listingStatus : listingstatuses) {
			dc.addToStatement(1, listingStatus.getId());
			dc.addToStatement(2, listingStatus.getStatus_name());
			dc.executeStatement();
		}
		
		//Locationok letárolása
		insertQuery = IQueries.locationInsert;
		dc.prepareQuery(insertQuery);
		for (Location location : locations) {
			dc.addToStatement(1, location.getId());
			dc.addToStatement(2, location.getManagerName());
			dc.addToStatement(3, location.getPhone());
			dc.addToStatement(4, location.getAddressPrimary());
			dc.addToStatement(5, location.getAddressSecondary());
			dc.addToStatement(6, location.getCountry());
			dc.addToStatement(7, location.getTown());
			dc.addToStatement(8, location.getPostalCode());
			dc.executeStatement();
		}
		
		insertQuery = IQueries.listingInsert;
		dc.prepareQuery(insertQuery);
		for (Listing listing : listings) {
			dc.addToStatement(1, listing.getId());
			dc.addToStatement(2, listing.getTitle());
			dc.addToStatement(3, listing.getDescription());
			dc.addToStatement(4, listing.getIntentoryItemLocation());
			dc.addToStatement(5, listing.getListingPrice());
			dc.addToStatement(6, listing.getCurrency());
			dc.addToStatement(7, listing.getQuantity());
			dc.addToStatement(8, listing.getListingStatus());
			dc.addToStatement(9, listing.getMarketplace());
			dc.addToStatement(10, listing.getUploadTime());
			dc.addToStatement(11, listing.getOwnerEmailAddress());
			dc.executeStatement();
		}
		
		dc.commit();
		
	}

	/**
	 * Adatok validálása
	 */
	private void validateData() {
		Set<ValidationError> validationErrors = new HashSet<>();
		System.out.println("Legyûjtés kész, validálás jön.");
		for (Listing listing : listings) {
			listing.setMarketplaceName(listing.getMarketplace(), marketplaces);
			
			Validator validator = new Validator(listing,validationErrors,marketplaces,listingstatuses,locations);
			validator.validate();
			
		}
		
		if (validationErrors.size() > 0) {
			System.out.println("Nem valid sorok kiírása CSV fájlba");
			writeErrorsToCsv(validationErrors);
			System.out.println("Nem valid sorok kiírása CSV fájlba elkészült");
		}
	}

	/**
	 * Hibák kiírása fájlba
	 * @param validationErrors - Hibák halmaza
	 */
	private void writeErrorsToCsv(Set<ValidationError> validationErrors) {
		try {
			File importLogCsv = new File(Config.getProperty("CSV_LOCATION") + File.separator + Config.getProperty("CSV_NAME"));
			
			if (!importLogCsv.getParentFile().exists()) {
				importLogCsv.getParentFile().mkdirs();
			}
			PrintWriter pw = new PrintWriter(importLogCsv);
			
			for (ValidationError validationError : validationErrors) {
				pw.append(validationError.getId().toString());
				pw.append(";");
				pw.append(validationError.getMarketplaceName());
				pw.append(";");
				pw.append(validationError.getInvalidField());
				pw.append('\n');
			}
			
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("Hiba, nincs ilyen fájl: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO hiba: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
