package jp.cspiral.c6.shareUmbrella.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import jp.cspiral.c6.shareUmbrella.model.Account;
import jp.cspiral.c6.shareUmbrella.model.Building;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Mongodbのコネクション管理クラス
 * DB接続へのシングルトン管理を行う．
 */
public class DBUtils {

	private final static String ACCOUNT_COLLECTION_NAME = "account";
	private final static String UMBRELLA_COLLECTION_NAME = "umbrella";
	private final static String BUILDING_COLLECTION_NAME = "building";

	private static DBCollection ACCOUNT_COLLECTION;
	private static DBCollection UMBRELLA_COLLECTION;
	private static DBCollection BUILDING_COLLECTION;

	public static DBUtils instance = new DBUtils();

	private DB db;

	private final String host = new String("");
	//private final String host = new String("localhost");
	private final String dbName = new String("shareUmbrellaC6");

	private DBUtils() {
		try {
			Mongo m = new Mongo(host);
			db = m.getDB(dbName);
		} catch (UnknownHostException | MongoException e) {
			e.printStackTrace();
		}
	}

	public static DBUtils getInstance() {
		return DBUtils.instance;
	}

	public DB getDb() {
		return this.db;
	}

	public static void addAccount(String userId, String pass, int point) {

		ACCOUNT_COLLECTION = DBUtils.getInstance().getDb().getCollection(ACCOUNT_COLLECTION_NAME);

		DBObject data = new BasicDBObject();
		data.put("userId",userId);
		data.put("pass",pass);
		data.put("point",point);
		data.put("sessionId","initSessionId");

		ACCOUNT_COLLECTION.insert(data);
	}

	public static void addUmbrella(int umbId, String status, String owner, String type, int buildId) {

		UMBRELLA_COLLECTION = DBUtils.getInstance().getDb().getCollection(UMBRELLA_COLLECTION_NAME);

		DBObject data = new BasicDBObject();
		data.put("umbId",umbId);
		data.put("status",status);
		data.put("owner",owner);
		data.put("type",type);
		data.put("buildId",buildId);
		data.put("time",new Date());

		UMBRELLA_COLLECTION.insert(data);
	}

	public static void addBuilding(int buildId, String name, double longitude, double latitude, int count) {

		BUILDING_COLLECTION = DBUtils.getInstance().getDb().getCollection(BUILDING_COLLECTION_NAME);

		DBObject data = new BasicDBObject();
		data.put("buildId",buildId);
		data.put("name",name);
		data.put("longitude",longitude);
		data.put("latitude",latitude);
		data.put("count",count);

		BUILDING_COLLECTION.insert(data);
	}

	public static void initDB() {

		ACCOUNT_COLLECTION = DBUtils.getInstance().getDb().getCollection(ACCOUNT_COLLECTION_NAME);
		UMBRELLA_COLLECTION = DBUtils.getInstance().getDb().getCollection(UMBRELLA_COLLECTION_NAME);
		BUILDING_COLLECTION = DBUtils.getInstance().getDb().getCollection(BUILDING_COLLECTION_NAME);

		initAccountDB();
		initUmbrellaDB();
		initBuildingDB();

	}

	private static void initAccountDB() {

		ACCOUNT_COLLECTION.insert(new BasicDBObject("init","init"));
		ACCOUNT_COLLECTION.drop();

		DBObject data = new BasicDBObject();
		data.put("userId","n-yuuta");
		data.put("pass","pass0");
		data.put("point",250);
		data.put("sessionId","initSessionId");

		ACCOUNT_COLLECTION.insert(data);

		DBObject data2 = new BasicDBObject();
		data2.put("userId","s-sumi");
		data2.put("pass","pass0");
		data2.put("point",250);
		data2.put("sessionId","initSessionId");

		ACCOUNT_COLLECTION.insert(data2);
	}

	private static void initUmbrellaDB() {

		UMBRELLA_COLLECTION.insert(new BasicDBObject("a","b"));
		UMBRELLA_COLLECTION.drop();

		//ノーマル傘
		DBObject data = new BasicDBObject();
		data.put("umbId",10);
		data.put("status","blank");
		data.put("owner","blank");
		data.put("type","normal");
		data.put("buildId",1);
		data.put("time","1018");

		UMBRELLA_COLLECTION.insert(data);

		//プレミアム傘
		data = new BasicDBObject();
		data.put("umbId",20);
		data.put("status","blank");
		data.put("owner","blank");
		data.put("type","premium");
		data.put("buildId",2);
		data.put("time","1018");

		UMBRELLA_COLLECTION.insert(data);
		
		data.put("umbId",50);
		data.put("status","blank");
		data.put("owner","blank");
		data.put("type","normal");
		data.put("buildId",52);
		data.put("time","1018");

		UMBRELLA_COLLECTION.insert(data);
		data.put("umbId",60);
		data.put("status","blank");
		data.put("owner","blank");
		data.put("type","premium");
		data.put("buildId",52);
		data.put("time","1018");

		UMBRELLA_COLLECTION.insert(data);
	}

	private static void initBuildingDB() {

		BUILDING_COLLECTION.insert(new BasicDBObject("a","b"));
		BUILDING_COLLECTION.drop();

		//吹田キャンパス
		DBObject data = new BasicDBObject();
		data.put("buildId",1);
		data.put("name","大阪大学吹田キャンパス");
		data.put("longitude",135.522139);
		data.put("latitude",34.82337);
		data.put("count",100);

		BUILDING_COLLECTION.insert(data);

		//豊中キャンパス
		data = new BasicDBObject();
		data.put("buildId",2);
		data.put("name","大阪大学豊中キャンパス");
		data.put("longitude",135.453902);
		data.put("latitude",34.806421);
		data.put("count",200);

		BUILDING_COLLECTION.insert(data);

		//ライフ
		data = new BasicDBObject();
		data.put("buildId",3);
		data.put("name","ライフ");
		data.put("longitude",135.475236);
		data.put("latitude",34.826537);
		data.put("count",300);

		BUILDING_COLLECTION.insert(data);

		//尼崎
		data = new BasicDBObject();
		data.put("buildId",4);
		data.put("name","ぺんぎん");
		data.put("longitude",135.4162766);
		data.put("latitude",34.7406223);
		data.put("count",13);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",50);
		data.put("name","大阪大学 中之島センター 事務所");
		data.put("longitude",135.490469);
		data.put("latitude",34.692715);
		data.put("count",6);

		BUILDING_COLLECTION.insert(data);


		data = new BasicDBObject();
		data.put("buildId",51);
		data.put("name","JR東西線　新福島駅");
		data.put("longitude",135.485644);
		data.put("latitude",34.695105);
		data.put("count",20);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",52);
		data.put("name","東京駅");
		data.put("longitude",139.76595282554626);
		data.put("latitude",35.68140486651128);
		data.put("count",6);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",53);
		data.put("name","品川駅");
		data.put("longitude",139.73885983228683);
		data.put("latitude",35.62885510928976);
		data.put("count",3);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",54);
		data.put("name","両国駅");
		data.put("longitude",139.79321479797363);
		data.put("latitude",35.69581339191388);
		data.put("count",7);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",55);
		data.put("name","渋谷駅");
		data.put("longitude",139.70171928405762);
		data.put("latitude",35.6581679808236);
		data.put("count",9);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",56);
		data.put("name","新宿駅");
		data.put("longitude",139.69957888126373);
		data.put("latitude",35.69055482137934);
		data.put("count",20);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",57);
		data.put("name","池袋駅");
		data.put("longitude",139.7118043899536);
		data.put("latitude",35.72986153843338);
		data.put("count",2);

		BUILDING_COLLECTION.insert(data);

		data = new BasicDBObject();
		data.put("buildId",58);
		data.put("name","上野駅");
		data.put("longitude",139.77598428726196);
		data.put("latitude",35.71212713081159);
		data.put("count",1);

		BUILDING_COLLECTION.insert(data);

	}

	public static Account convertDBObjectToAccountObject(DBObject document) {

		Account account = new Account();

		account.setUserId(document.get("userId").toString());
		account.setPass(document.get("pass").toString());
		account.setPoint(Integer.valueOf(document.get("point").toString()));
		account.setSessionId(document.get("sessionId").toString());

		return account;
	}

	public static Building convertDBObjectToBuildingObject(DBObject document) {

		Building building = new Building();

		building.setBuildId(Integer.valueOf(document.get("buildId").toString()));
		building.setName(document.get("name").toString());
		building.setLongitude(Double.valueOf(document.get("longitude").toString()));
		building.setLatitude(Double.valueOf(document.get("latitude").toString()));
		building.setCount(Integer.valueOf(document.get("count").toString()));

		return building;
	}

	public static ArrayList<Building> convertDBToBuildingList() {

		ArrayList<Building> buildingList = new ArrayList<Building>();

		DBCursor cursor = BUILDING_COLLECTION.find();
		try {
		   while(cursor.hasNext()) {

			   buildingList.add(convertDBObjectToBuildingObject(cursor.next()));
		   }
		} finally {
		   cursor.close();
		}

		return buildingList;

	}

	public static String sanitize(String str) {
		if (str == null) {
			return "";
		}
		str = str.replaceAll("&" , "&amp;" );
		str = str.replaceAll("<" , "&lt;"  );
		str = str.replaceAll(">" , "&gt;"  );
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'" , "&#39;" );
		return str;
	}
}