package jp.cspiral.c6.shareUmbrella.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="session")
public class Session {

	/**
	 * ユーザID
	 */
	private String userId;

	/**
	 * パスワード
	 */
	private String pass;

	/**
	 * ポイント
	 */
	private int point;

	@XmlElement(name="userId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@XmlElement(name="pass")
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@XmlElement(name="point")
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}