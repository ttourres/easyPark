package com.android.myfirstapp;

import com.google.android.maps.GeoPoint;

public class Parking
{
	private GeoPoint point;
	private Integer id;
	private String name;
	private String adress;
	private Integer capacity;
	private Integer availabity;
	
	public Parking(String name, double lati, double longi, String adress, Integer capacity, Integer availabity){
		this.name = name;
		point = new GeoPoint((int)(lati * 1E6), (int)(longi * 1E6));
		this.adress = adress;
		this.capacity = capacity;
		this.availabity = availabity;
	}
	
	public Parking() {
		// TODO Auto-generated constructor stub
	}

	// accesseurs
	public GeoPoint getPoint() {
		return point;
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getAdress() {
		return adress;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public Integer getAvailabity() {
		return availabity;
	}
	
	// modifieurs
	public void setPoint(GeoPoint point) {
		this.point = point;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public void setAvailabity(Integer availabity) {
		this.availabity = availabity;
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("Id ='" + this.id + "', ");
		buf.append("Name ='" + this.name + "', ");
		buf.append("GPS ='(" + this.point.getLatitudeE6() + "," + this.point.getLongitudeE6() + ")', ");
		buf.append("Capacity ='" + this.capacity + "', ");
		buf.append("Adress ='" + this.adress + "', ");
		buf.append("Availabity ='" + this.availabity + "', ");
		return buf.toString();
	} // toString()
} // class Parking