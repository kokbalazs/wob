package hu.wob.restapi.models;

/**
 * Listázási állapotok
 * @author Kókay Balázs
 *
 */
public class ListingStatus {

	private Long id;
	private String status_name;

	public ListingStatus() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

}
