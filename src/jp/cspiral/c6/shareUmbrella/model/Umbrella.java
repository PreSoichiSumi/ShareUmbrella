package jp.cspiral.c6.shareUmbrella.model;

public class Umbrella {

	/**
	 * 傘のID<br>
	 */
	private int umbId;

	/**
	 * 傘の管理状態<br>
	 */
	private String status;

	/**
	 * 傘の所有者<br>
	 */
	private String owner;

	/**
	 * 傘の種類<br>
	 */
	private String type;

	/**
	 * 施設ID
	 */
	private String buildId;

	/**
	 * 日時
	 */
	private String time;

	public int getUmbId() {
		return umbId;
	}

	public void setUmbId(int umbId) {
		this.umbId = umbId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
