-- FUNCTION: public.pr_get_report_monthly_data()

-- DROP FUNCTION public.pr_get_report_monthly_data();

CREATE OR REPLACE FUNCTION public.pr_get_report_monthly_data(
	)
    RETURNS SETOF report_monthly_data_type 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$
DECLARE
	yearCur		record;
	monthCur	integer;
	listingSum 	numeric;
	countTmp	numeric;
	sumTmp		numeric;
	avgTmp		numeric;
	ret			report_monthly_data_type;
BEGIN

	FOR yearCur in SELECT DISTINCT date_part('year',l.upload_time) as currentYear
					 FROM listing l
	LOOP
	
		FOR monthCur in 1..12 LOOP
		
			SELECT
				sum(ebay_count) as ebay_count
				,sum(ebay_sum) as ebay_sum
				,sum(ebay_avg) as ebay_avg
				,sum(amazon_count) as amazon_count
				,sum(amazon_sum) as amazon_sum
				,sum(amazon_avg) as amazon_avg
				,(SELECT owner_email_address 
					FROM listing subl 
					JOIN marketplace subm on subl.marketplace=subm.id 
				   WHERE 
						listing_status in (1,2) 
						and owner_email_address ~ '^[^@\s]+@[^@\s]+(\.[^@\s]+)+$' 
						and date_part('year',subl.upload_time) = yearCur.currentYear
						and date_part('month',subl.upload_time) = monthCur
				   GROUP BY owner_email_address, marketplace 
				   ORDER BY COUNT(DISTINCT subl.id) desc 
				   LIMIT 1) as best_email_address
			INTO
				ret.ebay_count
				,ret.ebay_sum
				,ret.ebay_avg
				,ret.amazon_count
				,ret.amazon_sum
				,ret.amazon_avg
				,ret.best_lister_email
			FROM
			(
			SELECT 
				COUNT(DISTINCT l.id) as ebay_count
				,SUM(quantity * listing_price) as ebay_sum
				,AVG(listing_price) as ebay_avg
				,0 as amazon_count
				,0 as amazon_sum
				,0 as amazon_avg
			FROM listing l 
			LEFT JOIN marketplace m on l.marketplace = m.id 
			WHERE 
				date_part('year',l.upload_time) = yearCur.currentYear
				and date_part('month',l.upload_time) = monthCur
				and lower(m.marketplace_name)='ebay'
				GROUP BY m.marketplace_name
			UNION ALL
			SELECT 
				0
				,0
				,0
				,COUNT(DISTINCT l.id) as listing_count
				,SUM(quantity * listing_price) as listing_price_sum
				,AVG(listing_price) as listing_price_avg
			FROM listing l 
			LEFT JOIN marketplace m on l.marketplace = m.id 
			WHERE 
				date_part('year',l.upload_time) = yearCur.currentYear
				and date_part('month',l.upload_time) = monthCur
				and lower(m.marketplace_name)='amazon'
				GROUP BY m.marketplace_name
			) sub
			;

			ret.month = cast(yearCur.currentYear as varchar) || '-' || lpad(cast(monthCur as varchar),2,'0');
			ret.total_listing_count = ret.ebay_count+ret.amazon_count;

			return next ret;
		
		END LOOP; --monthCur
	
	END LOOP; --yearCur
	
	RETURN;

END
$BODY$;

ALTER FUNCTION public.pr_get_report_monthly_data()
    OWNER TO postgres;
