package hu.wob.restapi.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.models.Report;

/**
 * JSON riport kezelõ osztály
 * @author Kókay Balázs
 *
 */
public class ReportController {

	/**
	 * A ResultSet-bõl kiolvassa az adatokat, és azokat rendszerezi a mainOrMonthly paraméter alapján.
	 * @param resultSet - Bejövõ lekérdezés
	 * @param reportDatas - riport adatokat tartalmazó halmaz
	 * @param mainOrMonthly - fõ lekérdezés, vagy hónapos lekérdezés.
	 */
	public void getReportData(ResultSet resultSet, Set<Report> reportDatas, Boolean mainOrMonthly) {
		
		try {
			while (resultSet.next()) {
				Report rd = new Report();
				rd.setMonthly(mainOrMonthly);
				rd.setMonthString(mainOrMonthly ? resultSet.getString("month") : "");
				rd.setTotalCount(resultSet.getDouble("total_listing_count"));
				rd.setEbayCount(resultSet.getDouble("ebay_count"));
				rd.setEbaySum(resultSet.getDouble("ebay_sum"));
				rd.setEbayAvg(resultSet.getDouble("ebay_avg"));
				rd.setAmazonCount(resultSet.getDouble("amazon_count"));
				rd.setAmazonSum(resultSet.getDouble("amazon_sum"));
				rd.setAmazonAvg(resultSet.getDouble("amazon_avg"));
				rd.setBestEmailAddress(resultSet.getString("best_lister_email"));
				
				reportDatas.add(rd);
			}
			resultSet.close();
		} catch (SQLException e) {
			System.err.println("SQL hiba a ReportData írásakor: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * A bejövõ halmazt átalakítja JSON String-é.
	 * @param reportDatas - riport adatok
	 * @return - elkészült JSON String
	 */
	public String getJsonString(Set<Report> reportDatas) {
		
		JSONObject report = new JSONObject();
		JSONArray monthlyReportArray = new JSONArray();
		JSONObject monthlyReport;
		
		for (Report rd : reportDatas) {
			if (!rd.getMonthly()) {
				report.put("Total listing count", rd.getTotalCount().intValue());
				report.put("Total eBay listing count", rd.getEbayCount().intValue());
				report.put("Total eBay listing price", rd.getEbaySum());
				report.put("Average eBay listing price", rd.getEbayAvg());
				report.put("Total Amazon listing count", rd.getAmazonCount().intValue());
				report.put("Total Amazon listing price", rd.getAmazonSum());
				report.put("Average Amazon listing price", rd.getAmazonAvg());
				report.put("Best lister email address", rd.getBestEmailAddress());
			} else {
				
				monthlyReport = new JSONObject();
				monthlyReport.put("Month: ", rd.getMonthString());
				monthlyReport.put("Total listing count per month", rd.getTotalCount().intValue());
				monthlyReport.put("Total eBay listing count per month", rd.getEbayCount().intValue());
				monthlyReport.put("Total eBay listing price per month", rd.getEbaySum());
				monthlyReport.put("Average eBay listing price per month", rd.getEbayAvg());
				monthlyReport.put("Total Amazon listing count per month", rd.getAmazonCount().intValue());
				monthlyReport.put("Total Amazon listing price per month", rd.getAmazonSum());
				monthlyReport.put("Average Amazon listing price per month", rd.getAmazonAvg());
				monthlyReport.put("Best lister email address of the month", rd.getBestEmailAddress());
				monthlyReportArray.add(monthlyReport);
				
			}
		}
		
		report.put("Monthly reports:",monthlyReportArray);
		
		return report.toJSONString();
		
	}

	/**
	 * A bejövõ JSON String-et kiírja egy fájlba.
	 * @param jsonReport - JSON String
	 * @return - Kiírt fájl helye.
	 */
	public String createJsonFile(String jsonReport) {
		
		File jsonFile = new File(Config.getProperty("JSON_REPORT_LOCATION") + File.separator + Config.getProperty("JSON_REPORT_NAME"));
		
		try {
			if (jsonFile.getParentFile().exists()) {
				jsonFile.getParentFile().mkdirs();
			}
			PrintWriter pw = new PrintWriter(jsonFile);
			pw.write(jsonReport);
			pw.flush();
			pw.close();
			
			return jsonFile.getAbsolutePath();
			
		} catch (FileNotFoundException e) {
			System.err.println("IO Hiba printwriter írásakor: " + e.getMessage());
			e.printStackTrace();
			return "";
		}
		
		
	}
	
	

}
