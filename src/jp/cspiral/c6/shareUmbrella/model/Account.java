package jp.cspiral.c6.shareUmbrella.model;



public class Account{

	/**
	 * ユーザID
	 */
	private String userId;

	/**
	 * パスワード
	 */
	private String pass;

	/**
	 * 所持ポイント
	 */
	private int point;

	/**
	 * セッションID
	 */
	private String sessionId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


}