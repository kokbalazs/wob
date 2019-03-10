-- FUNCTION: public.pr_get_report_main_data()

-- DROP FUNCTION public.pr_get_report_main_data();

CREATE OR REPLACE FUNCTION public.pr_get_report_main_data(
	)
    RETURNS report_main_data_type
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$
DECLARE
	rowcur 		record;
	listingSum 	      numeric;
	countTmp	      numeric;
	sumTmp		numeric;
	avgTmp		numeric;
	ret			report_main_data_type;
BEGIN

	for rowcur in 
		SELECT DISTINCT marketplace_name
		  FROM marketplace
	LOOP
		
		SELECT 
			COUNT(DISTINCT l.id) as listing_count
			,SUM(quantity * listing_price) as listing_price_sum
			,AVG(listing_price) as listing_price_avg
			,(SELECT owner_email_address 
				FROM listing subl 
				JOIN marketplace subm on subl.marketplace=subm.id 
			   WHERE 
					listing_status in (1,2) 
					and owner_email_address ~ '^[^@\s]+@[^@\s]+(\.[^@\s]+)+$' 
					and subm.marketplace_name=m.marketplace_name 
			   GROUP BY owner_email_address, marketplace 
			   ORDER BY COUNT(DISTINCT subl.id) desc 
			   LIMIT 1) as best_email_address
	    INTO	
			countTmp,
			sumTmp,
			avgTmp,
			ret.best_lister_email
		FROM listing l 
		LEFT JOIN marketplace m on l.marketplace = m.id 
		WHERE marketplace_name=rowcur.marketplace_name
		GROUP BY m.marketplace_name;
		
		IF (lower(rowcur.marketplace_name) = 'ebay')
		THEN
			ret.ebay_count=countTmp;
			ret.ebay_sum = sumTmp;
			ret.ebay_avg = avgTmp;
		ELSE
			ret.amazon_count=countTmp;
			ret.amazon_sum = sumTmp;
			ret.amazon_avg = avgTmp;
		END IF;
		
	END LOOP;
	
	ret.total_listing_count = coalesce(ret.ebay_count,0)+coalesce(ret.amazon_count,0);
	
	return ret;

END
$BODY$;

ALTER FUNCTION public.pr_get_report_main_data()
    OWNER TO postgres;
