package hu.wob.restapi.models;

import java.util.Objects;

/**
 * JSON riport adatok
 * @author Kókay Balázs
 *
 */
public class Report implements Comparable<Report>{
	
	private Boolean monthly;
	private String monthString;
	private Double totalCount;
	private Double ebayCount;
	private Double ebaySum;
	private Double ebayAvg;
	private Double amazonCount;
	private Double amazonSum;
	private Double amazonAvg;
	private String bestEmailAddress;
	
	
	public Report() {
		// TODO Auto-generated constructor stub
	}

	public Boolean getMonthly() {
		return monthly;
	}

	public void setMonthly(Boolean monthly) {
		this.monthly = monthly;
	}
	
	public String getMonthString() {
		return monthString;
	}

	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}

	public Double getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}

	public Double getEbayCount() {
		return ebayCount;
	}

	public void setEbayCount(Double ebayCount) {
		this.ebayCount = ebayCount;
	}

	public Double getEbaySum() {
		return ebaySum;
	}

	public void setEbaySum(Double ebaySum) {
		this.ebaySum = ebaySum;
	}

	public Double getEbayAvg() {
		return ebayAvg;
	}

	public void setEbayAvg(Double ebayAvg) {
		this.ebayAvg = ebayAvg;
	}

	public Double getAmazonCount() {
		return amazonCount;
	}

	public void setAmazonCount(Double amazonCount) {
		this.amazonCount = amazonCount;
	}

	public Double getAmazonSum() {
		return amazonSum;
	}

	public void setAmazonSum(Double amazonSum) {
		this.amazonSum = amazonSum;
	}

	public Double getAmazonAvg() {
		return amazonAvg;
	}

	public void setAmazonAvg(Double amazonAvg) {
		this.amazonAvg = amazonAvg;
	}

	public String getBestEmailAddress() {
		return bestEmailAddress;
	}

	public void setBestEmailAddress(String bestEmailAddress) {
		this.bestEmailAddress = bestEmailAddress;
	}

	@Override
	public int compareTo(Report o) {
		
		int i = (!Objects.equals(this.getMonthly(), o.getMonthly())) ? (this.getMonthly()) ? 1 : -1 : 0;
		if (i!=0) {
			return i;
		}
		
		i = (this.getMonthString() == null ? "" : this.getMonthString()).compareTo((o.getMonthString() == null ? "" : o.getMonthString()));
		return i;
	}


}
