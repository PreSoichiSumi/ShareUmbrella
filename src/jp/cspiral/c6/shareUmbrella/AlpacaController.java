package jp.cspiral.c6.shareUmbrella;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import jp.cspiral.c6.shareUmbrella.model.Account;
import jp.cspiral.c6.shareUmbrella.model.AccountModel;
import jp.cspiral.c6.shareUmbrella.model.Building;
import jp.cspiral.c6.shareUmbrella.model.BuildingList;
import jp.cspiral.c6.shareUmbrella.model.BuildingModel;
import jp.cspiral.c6.shareUmbrella.model.UmbrellaModel;
import jp.cspiral.c6.shareUmbrella.util.DBUtils;
import com.mongodb.DBObject;
import exception.NotEnoughPointException;
import exception.NotFoundUmbrellaException;
import exception.NotOwnerException;
import exception.NotRentalException;
import exception.NotReturnException;

public class AlpacaController {

	public AlpacaController() {

	}

	/**
	 * 傘を借りる．<br>
	 *
	 * @author n-yuuta
	 * @param umbId 傘ID
	 * @return message 結果
	 * @throws NotFoundUmbrellaException
	 * @throws NotEnoughPointException
	 * @throws NotReturnException
	 */
	public void rentUmbrella(int umbId,String userId) throws NotEnoughPointException, NotFoundUmbrellaException, NotReturnException {

		//傘をレンタルする
		UmbrellaModel umbrellaModel = new UmbrellaModel();
		umbrellaModel.rental(umbId,userId);
	}

	/**
	 * 傘を返す．<br>
	 *
	 * @author n-yuuta
	 * @param umbId 傘ID
	 * @return message 結果
	 * @throws NotFoundUmbrellaException
	 * @throws NotRentalException
	 * @throws NotOwnerException
	 */
	public void returnUmbrella(int umbId,String userId,double longtitude,double latitude) throws NotFoundUmbrellaException, NotOwnerException, NotRentalException {
		//傘を返却する
		UmbrellaModel umbrellaModel = new UmbrellaModel();
		umbrellaModel.returnUmbrella(umbId,userId,longtitude,latitude);
	}

	/**
	 * 施設をさがす．<br>
	 *
	 * @author n-yuuta
	 * @param
	 * @return building 結果
	 */
	public Building searchBuilding(double longitude, double latitude) {

		//施設をさがす
		BuildingModel buildingModel = new BuildingModel();
		Building building = buildingModel.search(longitude, latitude);

		return building;
	}

//	/**
//	 * 施設をさがす．<br>
//	 *
//	 * @author kkz
//	 * @param
//	 * @return building 結果
//	 */
//	public ArrayList<Building> searchBuildings(double longitude, double latitude) {
//
//		//施設をさがす
//		BuildingModel buildingModel = new BuildingModel();
//		return buildingModel.searchMany(longitude, latitude);
//	}

	/**
	 * 施設をさがす．<br>
	 *
	 * @author kkz
	 * @param
	 * @return building 結果
	 */
	public BuildingList searchBuildings(double longitude, double latitude) {

		//施設をさがす
		BuildingModel buildingModel = new BuildingModel();
		return buildingModel.searchBuildings(longitude, latitude);
	}

	/**
	 * ログインする<br>
	 * @author Y-Yuta
	 * @param userId ユーザID
	 * @param pass パスワード
	 * @return 成功なら<code>true</code>
	 * @exception
	 */
	public Account login(String userId, String pass) {
		// TODO:validate
		userId = DBUtils.sanitize(userId);
		pass = DBUtils.sanitize(pass);

		try {
			AccountModel accountModel = new AccountModel();
			Account account = accountModel.authenticate(userId, pass);
			//TODO:セッション登録
			return account;
		}catch(Exception e) {
			// 意味ない
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ユーザ情報を取得してJSON形式で返す
	 * @author s-sumi
	 * @param String userId
	 * @return JSONObject
	 */
	public JSONObject getUserInfo(String userId){
		//DBUtils.initDB();
		AccountModel aModel=new AccountModel();
		DBObject userData=aModel.getUserData(userId);
		UmbrellaModel uModel=new UmbrellaModel();
		JSONArray arr= uModel.getUmbId(userId);
		JSONObject res=new JSONObject();
		try{
			res.put("userId", userId);
			res.put("point", userData.get("point"));
			res.put("umbIdList", arr);
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}
		return res;
	}
	/**
	 * ポイントをチャージする
	 * @author s-sumi
	 * @param o
	 * @return 実行結果を示す文字列
	 */
	public String charge(String userId,int money){
		//実際はここにクレジットカードの精算処理が入る
		AccountModel aModel= new AccountModel();
		DBObject userData=aModel.getUserData(userId);
		userData.put("point",
				(int)userData.get("point") + money);

		aModel.updateUserData(userData);

		return "succeed"+String.valueOf((int)userData.get("point"))+"money="+money;
	}

	/**
	 * 与えられたuserIdのユーザをaccountDBから削除する
	 * @author s-sumi
	 * @param userId
	 * @return 成功したかどうかのメッセージ
	 */
	public String leave(String userId){
		AccountModel aModel=new AccountModel();
		return aModel.deleteUserData(userId);
	}

	/**
	 * 新規アカウント登録
	 * @param userId
	 * @param pass
	 * @return 成功したかどうかのメッセージ
	 */
	public String register(String userId , String pass){
		//入力の書式が正しいかチェック
		String userId2 = DBUtils.sanitize(userId);
		String pass2 = DBUtils.sanitize(pass);
		if(userId != userId2 || pass != pass2){
			return "TYPENG";
		}
		if(userId.length() < 4 || userId.length() > 12 || pass.length() < 4 || pass.length() > 12){
			return "LENGTHNG";
		}
		//アカウントをDBに登録する
		AccountModel account = new AccountModel();
		String message = account.registerAccount(userId2, pass2, 100);

		return message;
	}

}
