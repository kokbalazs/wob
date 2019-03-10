package hu.wob.restapi.models;

/**
 * Listázási állapotok
 * @author Kókay Balázs
 *
 */
public class ListingStatus {

	private Long id;
	private String statusName;

	public ListingStatus() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
