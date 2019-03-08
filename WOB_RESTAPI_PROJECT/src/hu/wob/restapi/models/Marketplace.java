package hu.wob.restapi.models;

/**
 * Áruház adatok.
 * @author Kókay Balázs
 *
 */
public class Marketplace {
	
	private Long id;
	private String marketplaceName;
	
	public Marketplace() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMarketplaceName() {
		return marketplaceName;
	}
	public void setMarketplaceName(String marketplace_name) {
		this.marketplaceName = marketplace_name;
	}

}
