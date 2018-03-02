package cn.honry.base.bean.model;

import java.io.Serializable;

/**  
 * 流程权限表实体
 * @Author：aizhonghua
 * @CreateDate：2018-2-1 下午20:32:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2018-2-1 下午20:32:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class OaJuris implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**主键**/
	private String id;
	/**流程编号**/
	private String flowCode;
	/**流程名称**/
	private String flowName;
	/**权限分类编码(编码)**/
	private String jurisCode;
	/**权限分类名称(编码)**/
	private String jurisName;
	/**范围编码**/
	private String rangeCode;
	/**范围名称**/
	private String rangeName;
	/**备注**/
	private String remarks;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getJurisCode() {
		return jurisCode;
	}
	public void setJurisCode(String jurisCode) {
		this.jurisCode = jurisCode;
	}
	public String getJurisName() {
		return jurisName;
	}
	public void setJurisName(String jurisName) {
		this.jurisName = jurisName;
	}
	public String getRangeCode() {
		return rangeCode;
	}
	public void setRangeCode(String rangeCode) {
		this.rangeCode = rangeCode;
	}
	public String getRangeName() {
		return rangeName;
	}
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
