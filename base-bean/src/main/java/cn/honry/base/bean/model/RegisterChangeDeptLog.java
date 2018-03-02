package cn.honry.base.bean.model;
/**  
 *  
 * @Description：  换科实体类
 * @Author：liudelin
 * @CreateDate：2015-6-25 下午05:00：00 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
import cn.honry.base.bean.business.Entity;

/**
 * @author win7
 *
 */
public class RegisterChangeDeptLog extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**挂号编号  **/
	private String rigisterId;
	/**原始科室  **/
	private String oldDept;
	/**原始医生  **/
	private String oldDoc;
	/**新科室  **/
	private String newDept;
	/**新医生  **/
	private String newDoc;
	/**备注  **/
	private String remark;
	/**换科原因  **/
	private String reason;
	
	/**数据库无关字段 午别**/
	private Integer newMidday;
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	private String oldDeptName;
	
	private String oldDocName;
	
	private String gradeX;
	
	private String gradeName;
	
	private String infoId;
	
	private String newDeptName;
	
	private String newDocName;
	
	public String getNewDeptName() {
		return newDeptName;
	}
	public void setNewDeptName(String newDeptName) {
		this.newDeptName = newDeptName;
	}
	public String getNewDocName() {
		return newDocName;
	}
	public void setNewDocName(String newDocName) {
		this.newDocName = newDocName;
	}
	public String getRigisterId() {
		return rigisterId;
	}
	public void setRigisterId(String rigisterId) {
		this.rigisterId = rigisterId;
	}
	public String getOldDept() {
		return oldDept;
	}
	public void setOldDept(String oldDept) {
		this.oldDept = oldDept;
	}
	public String getOldDoc() {
		return oldDoc;
	}
	public void setOldDoc(String oldDoc) {
		this.oldDoc = oldDoc;
	}
	public String getNewDept() {
		return newDept;
	}
	public void setNewDept(String newDept) {
		this.newDept = newDept;
	}
	public String getNewDoc() {
		return newDoc;
	}
	public void setNewDoc(String newDoc) {
		this.newDoc = newDoc;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOldDeptName() {
		return oldDeptName;
	}
	public void setOldDeptName(String oldDeptName) {
		this.oldDeptName = oldDeptName;
	}
	public String getOldDocName() {
		return oldDocName;
	}
	public void setOldDocName(String oldDocName) {
		this.oldDocName = oldDocName;
	}
	public String getGradeX() {
		return gradeX;
	}
	public void setGradeX(String gradeX) {
		this.gradeX = gradeX;
	}
	
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public Integer getNewMidday() {
		return newMidday;
	}
	public void setNewMidday(Integer newMidday) {
		this.newMidday = newMidday;
	}
	
	
}
