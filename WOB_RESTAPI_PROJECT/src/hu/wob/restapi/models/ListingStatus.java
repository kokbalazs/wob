package hu.wob.restapi.models;

/**
 * List�z�si �llapotok
 * @author K�kay Bal�zs
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
