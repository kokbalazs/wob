create table marketplace
(
	id serial PRIMARY KEY,
	marketplace_name varchar(250)
);

create table listing_status
(
	id serial PRIMARY KEY,
	status_name varchar(250)
);

create table location
(
	id uuid PRIMARY KEY,
	manager_name varchar(150),
	phone varchar(50),
	address_primary varchar(250),
	address_secondary varchar(250),
	country varchar(56),
	town varchar(150),
	postal_code varchar(100)
);

create table listing
(
	id uuid PRIMARY KEY,
	title varchar(150),
	description text,
	inventory_item_location_id uuid,
	listing_price numeric(18,5),
      currency varchar(10),
      quantity numeric(18,0),
      listing_status int,
	marketplace int,
      upload_time date,
      owner_email_address varchar(150)
);

truncate listing_Status;
truncate location;
truncate listing;
truncate marketplace;

select * from pr_get_report_main_data()

select * from pr_get_report_monthly_data()