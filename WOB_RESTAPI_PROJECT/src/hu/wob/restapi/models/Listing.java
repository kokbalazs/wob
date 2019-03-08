package hu.wob.restapi.models;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Listázási adatok.
 * @author Kókay Balázs
 *
 */
public class Listing {
	
	private UUID id;
	private String title;
	private String description;
	private Double listingPrice;
	private String currency;
	private Long quantity;
	private Date uploadTime;
	private String ownerEmailAddress;	
	private UUID intentoryItemLocation;
	private Long listingStatus;
	private Long marketplace;
	private String marketplaceName;

	public Listing() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getListingPrice() {
		return listingPrice;
	}

	public void setListingPrice(Double listingPrice) {
		this.listingPrice = listingPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getOwnerEmailAddress() {
		return ownerEmailAddress;
	}

	public void setOwnerEmailAddress(String ownerEmailAddress) {
		this.ownerEmailAddress = ownerEmailAddress;
	}

	public Long getListingStatus() {
		return listingStatus;
	}

	public void setListingStatus(Long listingStatus) {
		this.listingStatus = listingStatus;
	}

	public Long getMarketplace() {
		return marketplace;
	}

	public void setMarketplace(Long marketplace) {
		this.marketplace = marketplace;
	}

	public String getMarketplaceName() {
		return marketplaceName;
	}

	public void setMarketplaceName(String marketplaceName) {
		this.marketplaceName = marketplaceName;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getIntentoryItemLocation() {
		return intentoryItemLocation;
	}

	public void setIntentoryItemLocation(UUID intentoryItemLocation) {
		this.intentoryItemLocation = intentoryItemLocation;
	}

	public void setMarketplaceName(Long marketplaceId, Set<Marketplace> marketplaces) {
		
		for (Marketplace marketplace : marketplaces) {
			if (marketplace.getId().equals(marketplaceId)) {
				setMarketplaceName(marketplace.getMarketplaceName());
			}
		}
		
	}
}
