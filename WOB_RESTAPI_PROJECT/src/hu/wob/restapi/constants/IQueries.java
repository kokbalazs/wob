package hu.wob.restapi.constants;

/**
 * Lekérdezések
 * @author Kókay Balázs
 *
 */
public interface IQueries {
	
	//Lekérdezések
	public static final String mainReportQuery = "select * from pr_get_report_main_data()";
	public static final String monthlyReportQuery = "select * from pr_get_report_monthly_data() where month is not null";

	//Insertek
	public static final String marketplaceInsert = "INSERT INTO marketplace (id, marketplace_name) VALUES (?, ?);";
	public static final String listingStatusInsert = "INSERT INTO listing_status (id, status_name) VALUES (?, ?);";
	public static final String locationInsert = "INSERT INTO location (id, manager_name, phone, address_primary, address_secondary, country, town, postal_code) " + 
			"	VALUES (?::uuid, ?, ?, ?, ?, ?, ?, ?);";
	public static final String listingInsert = "INSERT INTO listing(id, title, description, inventory_item_location_id, listing_price, currency, quantity, listing_status, marketplace, upload_time, owner_email_address) " + 
			"	VALUES (?::uuid, ?, ?, ?::uuid, ?, ?, ?, ?, ?, ?::date, ?);";
	
	//Tesztek
	public static final String testPrepareQuery = "SELECT 1 FROM valamitabla";
	public static final String testExecuteQuery = "SELECT 1 FROM listing";
}
