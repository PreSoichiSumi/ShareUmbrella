package jp.cspiral.c6.shareUmbrella.model;

import jp.cspiral.c6.shareUmbrella.util.DBUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class AccountModel {
	private final String DB_ACCOUNT_COLLECTION = "account";

	/**
	 * DBオブジェクト
	 */
	private DB db;

	/**
	 * DBCollectionオブジェクト
	 */
	private DBCollection coll;

	/**
	 * Logger、DB、DBCollectionフィールドに各オブジェクトを設定する．
	 */
	public AccountModel() {
		db = jp.cspiral.c6.shareUmbrella.util.DBUtils.getInstance().getDb();
		coll = db.getCollection(DB_ACCOUNT_COLLECTION);
	}

	/**
	 * アカウントの新規登録処理を行う
	 *
	 * @param userId
	 * @param pass
	 * @param point - 初期ポイント
	 * @return
	 */
	public String registerAccount(String userId, String pass, int point) {
		/*
		 * Account account = new Account();
		 *
		 * account.setUserId(userId); account.setPass(pass);
		 * account.setPoint(100);
		 */

		try {
			if (isUniqueName(userId) == true) {
				DBObject obj = new BasicDBObject();
				obj.put("userId", userId);
				obj.put("pass", pass);
				obj.put("point", point);
				obj.put("sessionId", "");

				coll.insert(obj);
				return "OK";
			}else{
				return "WID";
			}
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	/**
	 * ユーザ名の重複チェック db.account.findOne({ "userId" : userId
	 * })の結果を利用して，存在するかしないかをチェックする
	 *
	 * @param userId
	 *            ユーザ名
	 * @return 与えられた userId が既に登録されていれば false，存在していなければ true．
	 */
	private boolean isUniqueName(String userId) throws Exception {

		DBObject query = new BasicDBObject();
		query.put("userId", userId);

		try {
			if (coll.findOne(query) != null) {
				return false;
			}
		} catch (MongoException e) {
			throw new Exception(e);
		}
		return true;
	}

	/**
	 * アカウント認証処理.
	 *
	 * @param userId
	 *            ユーザ名
	 * @param pass
	 *            パスワード
	 * @return アカウント情報
	 * @throws TEMFatalException
	 *             MongoExceptionが発生した場合．
	 * @throws AuthenticationFailureException
	 *             認証が失敗した場合
	 */
	public Account authenticate(String userId, String pass)
			throws Exception {
		// logger.info("AccountModel.authenticate");

		// DB問い合わせ用のDBオブジェクトを作る
		DBObject query = new BasicDBObject();
		query.put("userId", userId);
		query.put("pass", pass);

		try {
			// DBに問い合わせ
			DBObject o = coll.findOne(query);

			// 指定ユーザが存在しない
			if (o == null) {
				String msg = "User {userId:" + userId + ", pass:" + pass
						+ "} not exist.";
				// logger.warning(msg);
				throw new Exception(msg);
			}
			/*
			Account account = new Account();
//			DBUtils.attachProperties(account, o);
			// ごり押しポイント
			account.setUserId(o.get("userId").toString());
			account.setPoint(Integer.parseInt(o.get("point").toString()));
			account.setSessionId("hogehoge");
			account.setPass(o.get("userId").toString());
			*/

			return DBUtils.convertDBObjectToAccountObject(o);
		} catch (MongoException e) {
			// logger.severe(e.getMessage());
			throw new Exception(e);
		}
	}
	/**
	 * DBに登録されているユーザー情報を取得する
	 * @author s-sumi
	 * @param String userId
	 * @return JSONObject
	 */
	public DBObject getUserData(String userId){
		DBObject queryForUserData=new BasicDBObject();
		queryForUserData.put("userId", userId);
		return coll.findOne(queryForUserData);
	}
	/**
	 * account情報を更新する
	 * @author s-sumi
	 * @param DBObject
	 * @return none
	 */
	public void updateUserData(DBObject userData){
		coll.save(userData);//戻り値で成功したかどうか判定するとよい．本当は．すみ
	}
	/**
	 * userIdに対応するuserをaccount tableから削除する
	 * @author s-sumi
	 * @param DBObject
	 * @return none
	 */
	public String deleteUserData(String userId){
		DBObject queryForAccount=new BasicDBObject();
		queryForAccount.put("userId", userId);

		DBObject account =	coll.findOne(queryForAccount);
		coll.remove(account);

		return "succeed";
	}

	public void decreasePoint(int point,String userId) {

		// DB問い合わせ用のDBオブジェクトを作る
		DBObject accountQuery = new BasicDBObject();
		accountQuery.put("userId",userId);
		DBObject accountResult = coll.findOne(accountQuery);

		int userPoint = this.getUserPoint(userId);

		DBObject updateQuery = new BasicDBObject();
		updateQuery.put("point",(userPoint-point));
		coll.update(accountResult,new BasicDBObject("$set",updateQuery),false,false);
	}

	public void increasePoint(int point,String userId) {

		// DB問い合わせ用のDBオブジェクトを作る
		DBObject accountQuery = new BasicDBObject();
		accountQuery.put("userId",userId);
		DBObject accountResult = coll.findOne(accountQuery);

		int userPoint = this.getUserPoint(userId);

		DBObject updateQuery = new BasicDBObject();
		updateQuery.put("point",(userPoint+point));
		coll.update(accountResult,new BasicDBObject("$set",updateQuery),false,false);
	}

	public int getUserPoint(String userId) {

		// DB問い合わせ用のDBオブジェクトを作る
		DBObject accountQuery = new BasicDBObject();
		accountQuery.put("userId",userId);
		DBObject accountResult = coll.findOne(accountQuery);

		return Integer.valueOf(accountResult.get("point").toString());

	}
}