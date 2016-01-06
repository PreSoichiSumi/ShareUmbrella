package jp.cspiral.c6.shareUmbrella.model;

import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;

import org.codehaus.jettison.json.JSONArray;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import exception.NotEnoughPointException;
import exception.NotFoundUmbrellaException;
import exception.NotOwnerException;
import exception.NotRentalException;
import exception.NotReturnException;

public class UmbrellaModel {

	private final String DB_UMBRELLA_COLLECTION = "umbrella";

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
	public UmbrellaModel() {

		db = jp.cspiral.c6.shareUmbrella.util.DBUtils.getInstance().getDb();
		coll = db.getCollection(DB_UMBRELLA_COLLECTION);
	}

	/**
	 * 傘をレンタルする．<br>
	 * ポイントが足りない場合，指定の傘が存在しない場合はエラー．<br>
	 *
	 * @author n-yuuta, s-sumi
	 * @param umbId 傘ID
	 * @return message 結果メッセージ
	 * @throws NotEnoughPointException
	 * @throws NotFoundUmbrellaException
	 * @throws NotReturnException
	 */
	public String rental(int umbId, String userId) throws NotEnoughPointException, NotFoundUmbrellaException, NotReturnException {

		//傘IDに対応する傘情報を取得
		DBObject umbrellaQuery = new BasicDBObject();
		umbrellaQuery.put("umbId",umbId);
		DBObject umbrellaResult = coll.findOne(umbrellaQuery);

		//指定した傘が存在しない場合はエラー
		if (umbrellaResult == null) {
			throw new NotFoundUmbrellaException();
		}

		//傘の状態がblankじゃなければエラー
		String status = umbrellaResult.get("status").toString();
		if (!status.equals("blank")) {
			throw new NotReturnException();
		}

		AccountModel accountModel = new AccountModel();
		//ログイン中のユーザの所持ポイント数を取得
		int point = accountModel.getUserPoint(userId);

		//ポイント数が足りなければエラー
		int umbrellaPoint = 0;
		String type = umbrellaResult.get("type").toString();
		if (type.equals("normal")) {
			umbrellaPoint = 100;
		} else if (type.equals("premium")) {
			umbrellaPoint = 250;
		}

		if (point < umbrellaPoint) {
			throw new NotEnoughPointException();
		}

		//DB更新
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/E");
		DBObject updateQuery = new BasicDBObject();
		updateQuery.put("status","reserved");
		updateQuery.put("owner",userId);
		updateQuery.put("buildId","blank");
		updateQuery.put("time",sdf.format(new Date()));
		WriteResult res=coll.update(umbrellaResult,new BasicDBObject("$set",updateQuery),false,false);

		//メソッド実行中に他のユーザに借りられた場合
		if(res.getN()!=1)
			throw new ConcurrentModificationException("Some one heve been rent this umbrella");

		//施設IDを取得，施設IDに対応する施設の傘の本数を1本減らす
		int buildId = Integer.valueOf(umbrellaResult.get("buildId").toString());

		BuildingModel buildingModel = new BuildingModel();
		buildingModel.decreaseUmbrella(buildId);

		//ポイント数を消費
		accountModel.decreasePoint(umbrellaPoint,userId);

		return "レンタルに成功しました．";
	}

	/**
	 * 傘を返却する．<br>
	 * 傘を借りていない場合はエラー．<br>
	 * 所有者とリクエスト要求者が異なる場合はエラー<br>
	 *
	 * @author n-yuuta, s-sumi
	 * @param umbId 傘ID
	 * @return message 結果メッセージ
	 * @throws NotEnoughPointException
	 * @throws NotFoundUmbrellaException
	 * @throws NotOwnerException
	 * @throws NotRentalException
	 */
	public String returnUmbrella(int umbId, String userId, double longitude, double latitude) throws NotFoundUmbrellaException, NotOwnerException, NotRentalException {

		//傘IDに対応する傘情報を取得
		DBObject umbrellaQuery = new BasicDBObject();
		umbrellaQuery.put("umbId",umbId);
		DBObject umbrellaResult = coll.findOne(umbrellaQuery);

		//指定した傘が存在しない場合はエラー
		if (umbrellaResult == null) {
			throw new NotFoundUmbrellaException();
		}

		AccountModel accountModel = new AccountModel();

		//傘の状態を取得．状態がreservedじゃなければエラー．
		String status = umbrellaResult.get("status").toString();
		if (!status.equals("reserved")) {
			throw new NotRentalException();
		}

		//傘の所有者を取得．userIdと異なればエラー．
		String owner = umbrellaResult.get("owner").toString();
		if (!owner.equals(userId)) {
			throw new NotOwnerException();
		}

		BuildingModel buildingModel = new BuildingModel();
		Building building = buildingModel.getNearestBuilding(longitude,latitude);

		//DB更新
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/E");
		DBObject updateQuery = new BasicDBObject();
		updateQuery.put("status","blank");
		updateQuery.put("owner","blank");
		updateQuery.put("buildId",building.getBuildId());
		updateQuery.put("time",sdf.format(new Date()));
		coll.update(umbrellaResult,new BasicDBObject("$set",updateQuery),false,false);

		//傘の本数を1本増やす
		buildingModel.increaseUmbrella(building.getBuildId());

		//ポイントを還元
		accountModel.increasePoint(100,userId);

		return "返却に成功しました．";
	}

	/**
	 * userIdが所持する傘のリストをjsonarrayで取得する．
	 * @author s-sumi
	 * @param userId
	 * @return
	 */
	public JSONArray getUmbId(String userId){
		DBObject queryForUmbId = new BasicDBObject();
		queryForUmbId.put("owner", userId);
		DBCursor umbIdCursor=coll.find(queryForUmbId);
		JSONArray arr=new JSONArray();
		for (DBObject o : umbIdCursor) {
			arr.put((int)o.get("umbId"));
		}
		return arr;
	}
}