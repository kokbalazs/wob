package hu.wob.restapi.models;

import java.util.UUID;

/**
 * Validációkor talált hibák adatai
 * @author Kókay Balázs
 *
 */
public class ValidationError {
	
	private UUID id;
	private String marketplaceName;
	private String invalidField;
	
	public ValidationError() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getMarketplaceName() {
		return marketplaceName;
	}

	public void setMarketplaceName(String marketplaceName) {
		this.marketplaceName = marketplaceName;
	}

	public String getInvalidField() {
		return invalidField;
	}

	public void setInvalidField(String invalidField) {
		this.invalidField = invalidField;
	}
	
	

}
