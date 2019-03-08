package hu.wob.restapi.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.models.Listing;
import hu.wob.restapi.models.ListingStatus;
import hu.wob.restapi.models.Location;
import hu.wob.restapi.models.Marketplace;
import hu.wob.restapi.models.ValidationError;

/**
 * A bejövõ Listing objektumot validálja és gyûjti a hibás elemeket egy objektumba
 * @author Kókay Balázs
 *
 */
public class Validator {
	
	private Listing listing;
	private Set<ValidationError> validationErrors;
	private Set<Marketplace> marketplaces; 
	private Set<ListingStatus> listingStatuses; 
	private Set<Location> locations;
	
	public Validator(Listing listing, Set<ValidationError> validationErrors, Set<Marketplace> marketplaces, Set<ListingStatus> listingStatuses, Set<Location> locations) {
		 this.listing = listing;
		 this.validationErrors = validationErrors;
		 this.marketplaces = marketplaces;
		 this.listingStatuses = listingStatuses;
		 this.locations = locations;
	}
	
	/**
	 * Validálja a konstruktorban megkapott Listing-et.
	 */
	public void validate() {
		
		//id
		if (listing.getId() == null) {
			addError(null, listing.getMarketplaceName(), "id");
		} 
		
		//Title
		if (listing.getTitle() == null) {
			addError(listing.getId(), listing.getMarketplaceName(), "title");
		}
		
		//Description
		if (listing.getDescription() == null) {
			addError(listing.getId(), listing.getMarketplaceName(), "description");
		}
		
		//Location
		if (listing.getIntentoryItemLocation() == null) {
			addError(listing.getId(), listing.getMarketplaceName(), "location_id");
		} else {
			boolean found = false;
			for (Location location : locations) {
				if (location.getId().equals(listing.getIntentoryItemLocation())) {
					found = true;
					break;
				}
			}
			if (!found)
				addError(listing.getId(), listing.getMarketplaceName(), "location_id");
		}
		
		//Listing_price
		if ((listing.getListingPrice() == null ? 0.0 : listing.getListingPrice())>0.0) {
			
			String checkDouble = Double.toString(Math.abs(listing.getListingPrice()));
			int integerPlace = checkDouble.indexOf(".");
			int decimalPlace = checkDouble.length() - integerPlace - 1;
			
			if (decimalPlace!=2) {
				addError(listing.getId(), listing.getMarketplaceName(), "listing_price");
			}
			
		} else {
			addError(listing.getId(), listing.getMarketplaceName(), "listing_price");
		}
		
		//Currency
		if ((listing.getCurrency() == null ? "" : listing.getCurrency()).length() != 3) {
			addError(listing.getId(), listing.getMarketplaceName(), "currency");
		}
		
		//Quantity
		if ((listing.getQuantity() == null ? 0 : listing.getQuantity())<=0) {	
			addError(listing.getId(), listing.getMarketplaceName(), "quantity");
		}
		
		//Listing_status
		if (listing.getListingStatus() == null) {
			addError(listing.getId(), listing.getMarketplaceName(), "listing_status");
		} else {
			boolean found = false;
			for (ListingStatus listingStatus : listingStatuses) {
				if (listingStatus.getId().equals(listing.getListingStatus())) {
					found = true;
					break;
				}
			}
			if (!found)
				addError(listing.getId(), listing.getMarketplaceName(), "listing_status");
		}
		
		//Marketplace
		if (listing.getMarketplace() == null) {
			addError(listing.getId(), listing.getMarketplaceName(), "marketplace");
		} else {
			boolean found = false;
			for (Marketplace marketplace : marketplaces) {
				if (marketplace.getId().equals(listing.getMarketplace())) {
					found = true;
					break;
				}
			}
			if (!found)
				addError(listing.getId(), listing.getMarketplaceName(), "owner_email_address");
		}
		
		//Owner email address
		if (listing.getOwnerEmailAddress() == null) {
			addError(listing.getId(), listing.getMarketplaceName(), "marketplace");
		} else {
			Pattern pattern = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
			if (!pattern.matcher(listing.getOwnerEmailAddress()).matches()) {
				addError(listing.getId(), listing.getMarketplaceName(), "owner_email_address");
			}
				
		}
	}
	
	/**
	 * Egy új ValidationError objektumot ad a hibahalmazhoz
	 * @param id
	 * @param marketplaceName
	 * @param fieldName
	 */
	private void addError(UUID id, String marketplaceName, String fieldName) {
		
		ValidationError validationError = new ValidationError();
		validationError.setId(id);
		validationError.setMarketplaceName(marketplaceName);
		validationError.setInvalidField(fieldName);
		
		validationErrors.add(validationError);
		
	}
	
	

}
