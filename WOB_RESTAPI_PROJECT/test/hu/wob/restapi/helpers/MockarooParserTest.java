package hu.wob.restapi.helpers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.constants.IConstants;
import hu.wob.restapi.models.Listing;
import hu.wob.restapi.models.ListingStatus;
import hu.wob.restapi.models.Location;
import hu.wob.restapi.models.Marketplace;

class MockarooParserTest {
	
	private MockarooParser mockarooParser;

	@BeforeEach
	void setUp() throws Exception {
		Config.load(IConstants.configFile);
		mockarooParser = new MockarooParser();
	}

	@Test
	void testGetMarketPlaces() {
		Set<Marketplace> marketplaces = mockarooParser.getMarketPlaces();

		if (marketplaces.isEmpty()) {
			fail("Üres a marketplace Set");
		}
	}

	@Test
	void testGetListingStatuses() {
		Set<ListingStatus> listingStatuses = mockarooParser.getListingStatuses();
		
		if (listingStatuses.isEmpty()) {
			fail("Üres a listingStatus Set");
		}
	}

	@Test
	void testGetLocations() {
		Set<Location> locations = mockarooParser.getLocations();
		
		if (locations.isEmpty()) {
			fail("Üres a location Set");
		}
	}

	@Test
	void testGetListings() {
		Set<Listing> listings = mockarooParser.getListings();
		
		if (listings.isEmpty()) {
			fail("Üres a listing Set");
		}
	}
	
	

}
