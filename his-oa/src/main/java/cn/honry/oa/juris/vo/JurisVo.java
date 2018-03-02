package cn.honry.oa.juris.vo;

import java.util.ArrayList;

public class JurisVo {

	/**流程id**/
	private String id;
	/**流程名称**/
	private String name;
	/**是否所有**/
	private boolean isAll;
	/**人员类别**/
	private ArrayList<TypeVo> type;
	/**职务**/
	private ArrayList<DutiesVo> duties;
	/**级别**/
	private ArrayList<LevelVo> level;
	/**院区**/
	private ArrayList<AreaVo> area;
	/**个人**/
	private ArrayList<PersVo> pers;
	/**自定义**/
	private CustVo cust;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAll() {
		return isAll;
	}
	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
	public ArrayList<TypeVo> getType() {
		return type;
	}
	public void setType(ArrayList<TypeVo> type) {
		this.type = type;
	}
	public ArrayList<DutiesVo> getDuties() {
		return duties;
	}
	public void setDuties(ArrayList<DutiesVo> duties) {
		this.duties = duties;
	}
	public ArrayList<LevelVo> getLevel() {
		return level;
	}
	public void setLevel(ArrayList<LevelVo> level) {
		this.level = level;
	}
	public ArrayList<AreaVo> getArea() {
		return area;
	}
	public void setArea(ArrayList<AreaVo> area) {
		this.area = area;
	}
	public ArrayList<PersVo> getPers() {
		return pers;
	}
	public void setPers(ArrayList<PersVo> pers) {
		this.pers = pers;
	}
	public CustVo getCust() {
		return cust;
	}
	public void setCust(CustVo cust) {
		this.cust = cust;
	}
	
}
