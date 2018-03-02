package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 组套实体类
 * @author kjh
 */
public class BusinessStack  extends Entity{

	/*医院编号*/
	private Hospital hospitalId;
	/*组套类型*/
	private Integer type;
	/*科室*/
	private String deptId;
	/*名称*/
	private String name;
	/*拼音码*/
	private String pinYin;
	/*五笔码*/
	private String wb;
	/*自定义码*/
	private String inputCode;
	/*父级*/
	private String parent;
	/*排序*/
	private Integer order;
	/*层级*/
	private String level;
	/*层级路径*/
	private String path;
	/*组套医师*/
	private String doc;
	/*组套来源  1全院、2科室、3医生  */
	private String source;
	/*是否共享*/
	private  Integer shareFlag;
	/*备注*/
	private String remark;
	/*组套对象: 1是财务用，2是医嘱用*/
	private Integer stackObject;
	/**是否有效**/
	private Integer isValid;
	/**助记码**/
	private String memoryCode;
	/**是否需要确认**/
	private Integer isConfirm ;
	/**是否需要预约**/
	private Integer isOrder;
	/**医嘱组套下 是门诊2   还是住院1**/
	private Integer stackInpmertype;
	public Integer getStackInpmertype() {
		return stackInpmertype;
	}

	public void setStackInpmertype(Integer stackInpmertype) {
		this.stackInpmertype = stackInpmertype;
	}

	/*与数据库无关字段---组套医师名称*/
	private String docShow;
	
	public Hospital getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getWb() {
		return wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}


	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getDoc() {
		return doc;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setShareFlag(Integer shareFlag) {
		this.shareFlag = shareFlag;
	}

	public Integer getShareFlag() {
		return shareFlag;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setMemoryCode(String memoryCode) {
		this.memoryCode = memoryCode;
	}

	public String getMemoryCode() {
		return memoryCode;
	}

	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}

	public Integer getIsConfirm() {
		return isConfirm;
	}

	public void setIsOrder(Integer isOrder) {
		this.isOrder = isOrder;
	}

	public Integer getIsOrder() {
		return isOrder;
	}

	public void setStackObject(Integer stackObject) {
		this.stackObject = stackObject;
	}

	public Integer getStackObject() {
		return stackObject;
	}

	public void setDocShow(String docShow) {
		this.docShow = docShow;
	}

	public String getDocShow() {
		return docShow;
	}
	
}