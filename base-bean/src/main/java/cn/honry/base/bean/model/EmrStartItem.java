package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @Description 电子病历质控：大项信息表
 * @author  marongbin
 * @createDate： 2017年4月22日 上午9:59:45 
 * @modifier 
 * @modifyDate：
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public class EmrStartItem extends Entity {
	/**大项编号**/
	private String itemId;
	/**大项名称**/
	private String itemName;
	/**病历分类**/
	private String itemEmrType;
	/**大型分数**/
	private Double itemScore;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemEmrType() {
		return itemEmrType;
	}
	public void setItemEmrType(String itemEmrType) {
		this.itemEmrType = itemEmrType;
	}
	public Double getItemScore() {
		return itemScore;
	}
	public void setItemScore(Double itemScore) {
		this.itemScore = itemScore;
	}
	
}
