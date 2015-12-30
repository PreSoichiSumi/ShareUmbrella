package jp.cspiral.c6.shareUmbrella.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.cspiral.c6.shareUmbrella.util.DBUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class BuildingModel {

	private final String DB_BUILDING_COLLECTION = "building";

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
	public BuildingModel() {

		db = jp.cspiral.c6.shareUmbrella.util.DBUtils.getInstance().getDb();
		coll = db.getCollection(DB_BUILDING_COLLECTION);
	}

	/**
	 * 施設が保有する傘の本数を1本減らす．<br>
	 *
	 * @author n-yuuta
	 * @param buildId 施設ID
	 */
	public void decreaseUmbrella(int buildId) {

		DBObject buildingQuery = new BasicDBObject("buildId",buildId);
		DBObject buildingResult = coll.findOne(buildingQuery);

		if (buildingResult == null) {
			return;
		}

		int umbCount = Integer.valueOf(buildingResult.get("count").toString());
		umbCount--;
		DBObject updateBuilding = new BasicDBObject();
		updateBuilding.put("count",umbCount);
		coll.update(buildingResult,new BasicDBObject("$set",updateBuilding),false,false);

		return ;
	}

	/**
	 * 施設が保有する傘の本数を1本増やす．<br>
	 *
	 * @author n-yuuta
	 * @param buildId 施設ID
	 */
	public void increaseUmbrella(int buildId) {

		DBObject buildingQuery = new BasicDBObject("buildId",buildId);
		DBObject buildingResult = coll.findOne(buildingQuery);

		if (buildingResult == null) {
			return;
		}

		int umbCount = Integer.valueOf(buildingResult.get("count").toString());
		umbCount++;
		DBObject updateBuilding = new BasicDBObject();
		updateBuilding.put("count",umbCount);
		coll.update(buildingResult,new BasicDBObject("$set",updateBuilding),false,false);

		return ;
	}


	public Building search(double longitude, double latitude) {

		return getNearestBuilding(longitude,latitude);
	}

	public ArrayList<Building> searchMany(double longitude, double latitude){
		return getNearBuildings(longitude, latitude);
	}

	/**
	 * 近くの施設をまとめて返す（おそらく正式に使うBuildingList版）
	 *
	 * @author 2015020 賀数
	 * @param lon 緯度
	 * @param lat 経度
	 * @return 施設群
	 */
	public BuildingList searchBuildings(double longitude, double latitude){
		BuildingList buildingList = new BuildingList();

		List<Building> buildings = new ArrayList<Building>();

		DBCursor cursor = coll.find(new BasicDBObject());

		buildingList.setBuildNum(cursor.count());

		for(DBObject test: cursor){
			Building bld = new Building();
			bld.setBuildId((int)test.get("buildId"));
			bld.setCount((int)test.get("count"));
			bld.setLatitude((double)test.get("latitude"));
			bld.setLongitude((double)test.get("longitude"));
			bld.setName((String)test.get("name"));

			buildings.add(bld);
		}

		buildingList.setBuildings(buildings);

		return buildingList;
	}


	/**
	 * 近くの施設をまとめて返す（ArrayList版。使用しなくなる予定）
	 *
	 * @author 2015020 賀数
	 * @param lon 緯度
	 * @param lat 経度
	 * @return 施設群
	 */
	public ArrayList<Building> getNearBuildings(double lon, double lat){

		ArrayList<Building> buildingList = DBUtils.convertDBToBuildingList();

		//離れた施設は除外
		double removeDist = 0.5;
		int size = buildingList.size();
		for(int i=0; i<size; i++){
			Building target = buildingList.get(i);

			if(Math.abs(lat - target.getLatitude()) > removeDist
					|| Math.abs(lon - target.getLongitude()) > removeDist){

				buildingList.remove(i);
				i--; size--;
			}
		}

		//距離によりソートしたほうがいい？


		return buildingList;

	}

	/**
	 * 最寄りの施設をさがす．<br>
	 *
	 * @author n-yuuta<br>
	 * @param lan 経度<br>
	 * @param lon 緯度<br>
	 * @return building
	 */
	public Building getNearestBuilding(double longitude, double latitude) {

		int minBuildId = 0;
		double minLan = 0.0;
		double minLon = 0.0;
		int minCount = 0;
		String minName = "";

		double minDistance = 0.0;
		double distance = 0.0;

		ArrayList<Building> buildingList = DBUtils.convertDBToBuildingList();

		for (int i=0; i<buildingList.size(); i++) {

			distance = this.calcDistance(longitude, latitude, buildingList.get(i).getLongitude(), buildingList.get(i).getLatitude(), 5);

			if (i==0) {
				minDistance = distance;
				minBuildId = buildingList.get(i).getBuildId();
				minLan = buildingList.get(i).getLongitude();
				minLon = buildingList.get(i).getLatitude();
				minCount = buildingList.get(i).getCount();
				minName = buildingList.get(i).getName();

			} else {
				if (distance < minDistance) {
					minDistance = distance;
					minBuildId = buildingList.get(i).getBuildId();
					minLan = buildingList.get(i).getLongitude();
					minLon = buildingList.get(i).getLatitude();
					minCount = buildingList.get(i).getCount();
					minName = buildingList.get(i).getName();
				}
			}
		}

		Building building = new Building();
		building.setBuildId(minBuildId);
		building.setLongitude(minLan);
		building.setLatitude(minLon);
		building.setCount(minCount);
		building.setName(minName);

		return building;
	}

	/**
	 * 経緯度で表された2点間の距離を計算する．<br>
	 *
	 * @author n-yuuta<br>
	 * @param lan1 地点Aの軽度<br>
	 * @param lon1 地点Aの緯度<br>
	 * @param lan2 地点Bの軽度<br>
	 * @param lon2 地点Bの緯度<br>
	 * @param precision 小数点以下の桁数<br>
	 * @return distance 2点間の距離<br>
	 */
	private double calcDistance(double lan1, double lon1, double lan2, double lon2, int precision) {

	    int R = 6371;
	    double lat = Math.toRadians(lan2 - lan1);
	    double lng = Math.toRadians(lon2 - lon1);
	    double A = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lan1)) * Math.cos(Math.toRadians(lan2)) * Math.sin(lng / 2) * Math.sin(lng / 2);
	    double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1 - A));
	    double decimalNo = Math.pow(10, precision);
	    double distance = R * C;
	    distance = Math.round(decimalNo * distance / 1) / decimalNo;

	    return distance;
	}
}