package hu.wob.restapi.helpers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.models.Listing;
import hu.wob.restapi.models.ListingStatus;
import hu.wob.restapi.models.Location;
import hu.wob.restapi.models.Marketplace;

/**
 * Json Parseolását megkönnyítõ osztály.
 * @author Kókay Balázs
 * 
 */
public class MockarooParser {
	
	private JSONParser parser;
	private JSONArray mainArray;
	private String httpUrl;
	
	public MockarooParser() {
		httpUrl = Config.getProperty("BASE_URL");
	}

	/**
	 * A bejövõ HTTP címre létrehoz egy csatlakozást és kiolvassa az adatot belõle.
	 * @param httpUrl - HTTP URL cím
	 * @return - Kiolvasott adat
	 */
	private String getJsonFromApi(String httpUrl) {
		
		URL url;
		HttpURLConnection con = null;
		Scanner scan = null;
		try {
			url = new URL(httpUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			
			int responseCode = con.getResponseCode();
			
			if (responseCode != 200) {
				System.out.println("Responsecode: " + responseCode);
				return "";
			}
						
			scan = new Scanner(url.openStream());
			
			StringBuilder sb = new StringBuilder();
			while (scan.hasNext()) {
				sb.append(scan.nextLine());
			}
			
			return sb.toString();
			
		} catch (MalformedURLException e) {
			System.err.println("URL hiba: " + e.getMessage());
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			System.err.println("IO hiba: " + e.getMessage());
			e.printStackTrace();
			return "";
		} finally {
			if (scan != null) {
				scan.close();
			}
			
			if (con != null) {
				con.disconnect();
			}
		}
		
	}
	
	/**
	 * Piacok kiolvasása.
	 * @return JSON-bõl kiolvasott piacok halmaza
	 */
	public Set<Marketplace> getMarketPlaces(){
		
		System.out.println("Marketplace-ek lekérdezése.");
		
		Set<Marketplace> ret = new HashSet<>(); 
		
		parser = new JSONParser();
		try {
			mainArray = (JSONArray) parser.parse(getJsonFromApi(httpUrl.replaceAll("@class", "marketplace")));
			
			for (int i=0; i< mainArray.size(); i++) {
				
				JSONObject obj = (JSONObject) mainArray.get(i);
				
				Marketplace marketplace = new Marketplace();
				marketplace.setId((Long) obj.get("id"));
				marketplace.setMarketplaceName((String) obj.get("marketplace_name"));
				ret.add(marketplace);
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			System.err.println("Hiba a Marketplace JSON Parseoláskor: " + e.getMessage());
		}
		
		return ret;
		
	}
	
	/**
	 * Listázási állapotok kiolvasása.
	 * @return JSON-bõl kiolvasott listázás állapotok halmaza
	 */
	public Set<ListingStatus> getListingStatuses(){
		
		System.out.println("ListingStatus-ok lekérdezése.");
		
		Set<ListingStatus> ret = new HashSet<>();
		
		parser = new JSONParser();
		try {
			mainArray = (JSONArray) parser.parse(getJsonFromApi(httpUrl.replaceAll("@class", "listingStatus")));
			
			for (int i=0; i< mainArray.size(); i++) {
				
				JSONObject obj = (JSONObject) mainArray.get(i);
				
				ListingStatus listingStatus = new ListingStatus();
				listingStatus.setId((Long) obj.get("id"));
				listingStatus.setStatus_name((String) obj.get("status_name"));
				ret.add(listingStatus);
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			System.err.println("Hiba a ListingStatus JSON Parseoláskor: " + e.getMessage());
		}
		
		return ret;
		
	}
	
	/**
	 * Helyek kiolvasása
	 * @return JSON-bõl kiolvasott helyek halmaza
	 */
	public Set<Location> getLocations(){
		
		System.out.println("Location-ok lekérdezése.");
		
		Set<Location> ret = new HashSet<>();
		
		parser = new JSONParser();
		try {
			mainArray = (JSONArray) parser.parse(getJsonFromApi(httpUrl.replaceAll("@class", "location")));
			
			for (int i=0; i< mainArray.size(); i++) {
				
				JSONObject obj = (JSONObject) mainArray.get(i);

				Location location = new Location();
				location.setId(UUID.fromString((String) obj.get("id")));
				location.setManagerName((String) obj.get("manager_name"));
				location.setPhone((String) obj.get("phone"));
				location.setAddressPrimary((String) obj.get("address_primary")); 
				location.setAddressSecondary((String) obj.get("address_secondary"));
				location.setCountry((String) obj.get("country"));
				location.setTown((String) obj.get("town"));
				location.setPostalCode((String) obj.get("postal_code"));
				
				ret.add(location);
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			System.err.println("Hiba a Location JSON Parseoláskor: " + e.getMessage());
		}
		
		return ret;
		
	}
	
	/**
	 * Listázások kiolvasása
	 * @return JSON-bõl kiolvasott listázások halmaza
	 */
	public Set<Listing> getListings(){
		
		System.out.println("Listing-ek lekérdezése.");
		
		Set<Listing> ret = new HashSet<Listing>();
		
		parser = new JSONParser();
		try {
			mainArray = (JSONArray) parser.parse(getJsonFromApi(httpUrl.replaceAll("@class", "listing")));
			
			for (int i=0; i< mainArray.size(); i++) {
				
				JSONObject obj = (JSONObject) mainArray.get(i);

				Listing listing = new Listing();
				listing.setId(UUID.fromString((String) obj.get("id")));
				listing.setTitle((String) obj.get("title"));
				listing.setDescription((String) obj.get("description"));
				listing.setListingPrice(getJsonDouble(obj.get("listing_price")));
				listing.setCurrency((String) obj.get("currency"));
				listing.setQuantity((Long) obj.get("quantity"));
				listing.setOwnerEmailAddress((String) obj.get("owner_email_address"));
				listing.setUploadTime(getJsonDate(obj.get("upload_time")));
				listing.setListingStatus((Long) obj.get("listing_status"));
				listing.setMarketplace((Long) obj.get("marketplace"));
				listing.setIntentoryItemLocation(UUID.fromString((String) obj.get("location_id")));
				
				ret.add(listing);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			System.err.println("Hiba a Listing JSON Parseoláskor: " + e.getMessage());
		}
		
		return ret;
		
	}
	
	/**
	 * Json Double kiolvasása
	 * @param jsonObj - Json object, amibõl ki kell olvasni a Double értéket.
	 * @return - Double érték a bejövõ objektumnak.
	 */
	private Double getJsonDouble(Object jsonObj) {
		
		if (jsonObj instanceof Long) {
			return ((Long) jsonObj).doubleValue();
		}
		
		return (Double) jsonObj;
	}
	
	/**
	 * Json Date kiolvasása
	 * @param jsonObj - Json object, amibõl ki kell olvasni a Date értéket.
	 * @return - Date érték a bejövõ objektumnak.
	 */
	private Date getJsonDate(Object jsonObj) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			if (jsonObj != null) {
				return sdf.parse((String) jsonObj);
			}
				
		} catch (java.text.ParseException e) {
			System.out.println("Üres upload_time");
		}
		return null;
	}
	
}
