package cn.honry.base.bean.model;

import java.util.Date;


/**
 * 
 * 
 * @author wujiao
 *挂号级别
 */
public class RegisterGrade {
	/**唯一编号(主键)**/
	private String id;
	/**编号(外键)**/
	private Hospital hospitalId;
	/** 级别代码**/
	private String encode;
	private String code;
	/** 级别名称 **/
	private String name;
	/** 拼音码 **/
	private String codePinyin;
	/** 五笔码 **/
	private String codeWb;
	/** 自定义码 **/
	private String codeInputcode;
	/**是否使用专家号**/
	private Integer expertno=0;
	/**是否使用专科号**/
	private Integer specialistno=0;
	/**是否使用特诊号**/
	private Integer specialdiagnosisno=0;
	/** 说明 **/
	private String description;
	/**排序**/
	private Long order;
	/**默认标志**/
	private Integer isdefault;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	/**修改人员**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	/**删除人员**/
	private String deleteUser;
	/**删除时间**/
	private Date deleteTime;	
	/**停用标志**/
	private Integer stop_flg=0;
	/**删除标志**/
	private Integer del_flg=0;
	private String str;
	private boolean disabled;
	//条件查询
	private String sysEmployeeId;//员工Id
	public String getSysEmployeeId() {
		return sysEmployeeId;
	}
	public void setSysEmployeeId(String sysEmployeeId) {
		this.sysEmployeeId = sysEmployeeId;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public Hospital getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCodePinyin() {
		return codePinyin;
	}
	public void setCodePinyin(String codePinyin) {
		this.codePinyin = codePinyin;
	}
	public String getCodeWb() {
		return codeWb;
	}
	public void setCodeWb(String codeWb) {
		this.codeWb = codeWb;
	}
	public String getCodeInputcode() {
		return codeInputcode;
	}
	public void setCodeInputcode(String codeInputcode) {
		this.codeInputcode = codeInputcode;
	}
	public Integer getExpertno() {
		return expertno;
	}
	public void setExpertno(Integer expertno) {
		this.expertno = expertno;
	}
	public Integer getSpecialistno() {
		return specialistno;
	}
	public void setSpecialistno(Integer specialistno) {
		this.specialistno = specialistno;
	}
	public Integer getSpecialdiagnosisno() {
		return specialdiagnosisno;
	}
	public void setSpecialdiagnosisno(Integer specialdiagnosisno) {
		this.specialdiagnosisno = specialdiagnosisno;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getOrder() {
		return order;
	}
	public void setOrder(Long order) {
		this.order = order;
	}
	public Integer getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
	}
	public Integer getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public RegisterGrade(String id, Hospital hospitalId, String code,
			String name, String codePinyin, String codeWb,
			String codeInputcode, Integer expertno, Integer specialistno,
			Integer specialdiagnosisno, String description, Long order,
			Integer isdefault, String createUser, String createDept,
			Date createTime, String updateUser, Date updateTime,
			String deleteUser, Date deleteTime, Integer stop_flg,
			Integer del_flg) {
		super();
		this.id = id;
		this.hospitalId = hospitalId;
		this.code = code;
		this.name = name;
		this.codePinyin = codePinyin;
		this.codeWb = codeWb;
		this.codeInputcode = codeInputcode;
		this.expertno = expertno;
		this.specialistno = specialistno;
		this.specialdiagnosisno = specialdiagnosisno;
		this.description = description;
		this.order = order;
		this.isdefault = isdefault;
		this.createUser = createUser;
		this.createDept = createDept;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.deleteUser = deleteUser;
		this.deleteTime = deleteTime;
		this.stop_flg = stop_flg;
		this.del_flg = del_flg;
	}
	public RegisterGrade() {
		super();
	}
	

}
