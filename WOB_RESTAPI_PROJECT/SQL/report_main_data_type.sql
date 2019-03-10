-- Type: report_main_data_type

-- DROP TYPE public.report_main_data_type;

CREATE TYPE public.report_main_data_type AS
(
	total_listing_count numeric,
	ebay_count numeric,
	ebay_sum numeric,
	ebay_avg numeric,
	amazon_count numeric,
	amazon_sum numeric,
	amazon_avg numeric,
	best_lister_email character varying(150)
);

ALTER TYPE public.report_main_data_type
    OWNER TO postgres;
