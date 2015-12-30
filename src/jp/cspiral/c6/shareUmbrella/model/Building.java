package jp.cspiral.c6.shareUmbrella.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="building")
public class Building {

	/**
	 * 施設ID
	 */
	private int buildId;

	/**
	 * 施設の名前
	 */
	private String name;

	/**
	 * 経度
	 */
	private Double longitude;

	/**
	 * 緯度
	 */
	private Double latitude;

	/**
	 * 傘の本数
	 */
	private int count;

	@XmlElement(name="buildId")
	public int getBuildId() {
		return buildId;
	}

	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	@XmlElement(name="longitude")
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@XmlElement(name="latitude")
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@XmlElement(name="count")
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}