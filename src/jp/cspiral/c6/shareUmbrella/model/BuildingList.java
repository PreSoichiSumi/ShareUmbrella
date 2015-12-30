package jp.cspiral.c6.shareUmbrella.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="buildingList")
public class BuildingList{
	private int buildNum;
	private List<Building> buildings;

	public BuildingList(){
		this.buildNum = 0;
		buildings = new ArrayList<Building>();
	}

	@XmlElement(name="buildNum")
	public int getBuildNum(){
		return this.buildNum;
	}

	@XmlElement(name="buildings")
	public Building[] getBuildings(){
		return buildings.toArray(new Building[buildings.size()]);
	}


	public void setBuildNum(int num){
		this.buildNum = num;
	}

	public void setBuildings(List<Building> builds){
		this.buildings = builds;
	}
}