package hu.wob.restapi.helpers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.constants.IConstants;
import hu.wob.restapi.models.Listing;
import hu.wob.restapi.models.ListingStatus;
import hu.wob.restapi.models.Location;
import hu.wob.restapi.models.Marketplace;
import hu.wob.restapi.models.ValidationError;

class ValidatorTest {
	
	private Set<Marketplace> marketplaces;
	private Set<Location> locations;
	private Set<ListingStatus> listingStatuses;
	private MockarooParser mockarooParser;
	private Validator validator;

	@BeforeEach
	void setUp() throws Exception {
		Config.load(IConstants.configFile);
		mockarooParser = new MockarooParser();
		marketplaces = mockarooParser.getMarketPlaces();
		listingStatuses = mockarooParser.getListingStatuses();
		locations = mockarooParser.getLocations();
	}

	@Test
	public void testValidator() {
		
		Listing listing = new Listing();
		Set<ValidationError> validationErrors = new HashSet<ValidationError>();
		
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		
		assertEquals(listing, validator.getListing());
		assertEquals(validationErrors, validator.getValidationErrors());
		assertEquals(marketplaces, validator.getMarketplaces());
		assertEquals(listingStatuses, validator.getListingStatuses());
		assertEquals(locations, validator.getLocations());
	}

	@Test
	public void testValidate() {
		
		UUID id = UUID.fromString("6468a219-40f7-4bf6-bbdc-0bb54d0a2411");
		String title = "K�nyvc�m";
		String description = "K�nyvle�r�s";
		UUID itemLoc = UUID.fromString("1d551b07-fd16-4760-88a3-4aa4fda13a2b");
		Double listingPrice = 136.57;
		String currency = "HUF";
		Long quantity = (long) 12;
		Long listingStatus = (long) 3;
		Long marketplace = (long) 1;
		String email = "valami@valahol.hu";
		
		Set<ValidationError> validationErrors = new HashSet<ValidationError>();
		Listing listing = new Listing();
		listing.setId(id);
		listing.setTitle(title);
		listing.setDescription(description);
		listing.setIntentoryItemLocation(itemLoc);
		listing.setListingPrice(listingPrice);
		listing.setCurrency(currency);
		listing.setQuantity(quantity);
		listing.setListingStatus(listingStatus);
		listing.setMarketplace(marketplace);
		listing.setOwnerEmailAddress(email);
		
		
		//ID
		listing.setId(null);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("ID null valid�l�s rossz.");
		} else {
			listing.setId(id);
			validationErrors.clear();
		}
		
		//Title
		listing.setTitle(null);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Title null valid�l�s rossz.");
		} else {
			listing.setTitle(title);
			validationErrors.clear();
		}
		
		//Description
		listing.setDescription(null);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Description null valid�l�s rossz.");
		} else {
			listing.setDescription(description);
			validationErrors.clear();
		}
		
		//Location
		listing.setIntentoryItemLocation(null);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Location null valid�l�s rossz.");
		} else {
			listing.setIntentoryItemLocation(itemLoc);
			validationErrors.clear();
		}
		
		//Location tartalmaz�sa (v�letlen UUID gener�l�sa)
		listing.setIntentoryItemLocation(UUID.randomUUID());
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Location random UUID valid�l�s rossz.");
		} else {
			listing.setIntentoryItemLocation(itemLoc);
			validationErrors.clear();
		}
		
		//Listing price kisebb mint 0
		listing.setListingPrice(-1.0);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Listing price kisebb mint 0 valid�l�s rossz.");
		} else {
			listing.setListingPrice(listingPrice);
			validationErrors.clear();
		}
		
		//Listing price decim�lis helyek sz�ma nem 2
		listing.setListingPrice(2.0003);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Listing price decim�lis helyek sz�ma nem 2 valid�l�sa rossz.");
		} else {
			listing.setListingPrice(listingPrice);
			validationErrors.clear();
		}
		
		//Currency
		listing.setCurrency("HU");
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Currency valid�l�sa rossz.");
		} else {
			listing.setCurrency(currency);
			validationErrors.clear();
		}
		
		//Quantity
		listing.setQuantity((long) -1);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Quantity valid�l�sa rossz.");
		} else {
			listing.setQuantity(quantity);
			validationErrors.clear();
		}
		
		//Listing_Status null
		listing.setListingStatus(null);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Listing_Status null valid�l�sa rossz.");
		} else {
			listing.setListingStatus(listingStatus);
			validationErrors.clear();
		}
		
		//Listing_Status nem l�tezik
		listing.setListingStatus((long) 131);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Listing_Status l�tez�s�nek valid�l�sa rossz.");
		} else {
			listing.setListingStatus(listingStatus);
			validationErrors.clear();
		}
		
		//Marketplace null
		listing.setMarketplace(null);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Marketplace null valid�l�sa rossz.");
		} else {
			listing.setMarketplace(marketplace);
			validationErrors.clear();
		}
		
		//Marketplace nem l�tezik
		listing.setMarketplace((long) 245);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Marketplace l�tez�s�nek valid�l�sa rossz.");
		} else {
			listing.setMarketplace(marketplace);
			validationErrors.clear();
		}
		
		//Email null
		listing.setOwnerEmailAddress(null);
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Email null valid�l�sa rossz.");
		} else {
			listing.setOwnerEmailAddress(email);
			validationErrors.clear();
		}
		
		//Email form�tum ellen�rz�s
		listing.setOwnerEmailAddress("valamikukacvalaholponthu");
		validator = new Validator(listing, validationErrors, marketplaces, listingStatuses, locations);
		validator.validate();
		
		if (validationErrors.isEmpty()) {
			fail("Email form�tum ellen�rz�s valid�l�sa rossz.");
		} else {
			listing.setOwnerEmailAddress(email);
			validationErrors.clear();
		}
	}

}
