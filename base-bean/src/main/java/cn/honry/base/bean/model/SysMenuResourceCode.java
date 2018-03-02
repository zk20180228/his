package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * 栏目资源编码实体类
 * 
 *
 */
public class SysMenuResourceCode extends Entity{

	/**资源名称**/
	private String mrcName;
	/**资源别名**/
	private String mrcAlias;
	/**资源类型**/
	private Integer mrcType;
	/**资源排序**/
	private Integer mrcOrder;
	/**资源说明**/
	private String mrcDescription;
	public String getMrcName() {
		return mrcName;
	}
	public void setMrcName(String mrcName) {
		this.mrcName = mrcName;
	}
	public String getMrcAlias() {
		return mrcAlias;
	}
	public void setMrcAlias(String mrcAlias) {
		this.mrcAlias = mrcAlias;
	}
	public Integer getMrcType() {
		return mrcType;
	}
	public void setMrcType(Integer mrcType) {
		this.mrcType = mrcType;
	}
	public Integer getMrcOrder() {
		return mrcOrder;
	}
	public void setMrcOrder(Integer mrcOrder) {
		this.mrcOrder = mrcOrder;
	}
	public String getMrcDescription() {
		return mrcDescription;
	}
	public void setMrcDescription(String mrcDescription) {
		this.mrcDescription = mrcDescription;
	}
	
}
