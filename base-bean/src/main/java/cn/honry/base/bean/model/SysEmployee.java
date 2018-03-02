package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 医院员工表
 * @author zpty
 * Date:2015/05/20 15:30
 */

public class SysEmployee extends Entity {	
	
	/**医院编号**/
	private Hospital hospitalId;
	/**用户编号**/
	private User userId;
	/**部门编号**/
	private SysDepartment deptId;
	/**系统编号**/
	private String code;
	/**工作号**/
	private String jobNo;
	/**姓名**/
	private String name;
	/**曾用名**/
	private String oldName;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String inputCode;
	/**能否改票据**/
	private Integer canEditBill;
	/**能否直接收费**/
	private Integer canCharge;
	/**员工类型**/
	private String type;
	/**1正式2退休3聘用制4编外5离休6出国7返聘8死亡9外出学习a临时工------20151225zpty新加字段**/
	private String workState;
	/**1干部2工人------20151225zpty新加字段**/
	private String ifcadre;
	/**性别**/
	private String sex;
	/**生日**/
	private Date birthday;
	/**民族**/
	private String family;
	/**学历**/
	private String education;
	/**职务**/
	private String post;
	/**职称**/
	private String title;
	/**办公室**/
	private String office;
	/**传真**/
	private String fax;
	/**办公电话**/
	private String officePhone;
	/**移动电话**/
	private String mobile;
	/**电子邮件**/
	private String email;
	/**是否是专家**/
	private Integer isExpert;
	/**身份证**/
	private String idEntityCard;
	/**照片**/
	private String photo;
	/**电子签名**/
	private String esign;
	/**备注**/
	private String remark;
	/**排序**/
	private Integer order;
	/**  
	 * 
	 * @Fields mofficePhoneVisible : 0：可见 1：不可见 20170421
	 *
	 */
	private Integer mofficePhoneVisible = 0;
	/**  
	 * 
	 * @Fields mmobileVisible : 0：可见 1：不可见 20170421
	 *
	 */
	private Integer mmobileVisible = 0;
	/**  
	 * 
	 * @Fields memailVisible : 0：可见 1：不可见 20170421
	 *
	 */
	private Integer memailVisible = 0;
	/**  
	 * 
	 * @Fields emailPwd : 邮箱密码（用于移动端邮件的发送）
	 *
	 */
	private String emailPwd;
	
	/**  
	 * 
	 * @Fields nurseCode : 所属护士站
	 *
	 */
	private String nurseCode;
	
	/**  分页  **/
	private String page;
	private String rows;
	//hospital表关联出来的需要的内容
	private String hName;//名称
	//user用户表关联出来的需要内容
	private String account;//账号
	private String userCode;//user id
	//dept表关联出来的需要内容
	private String deptName;//部门名称
	private String deptCode;//部门id
	//条件查询
	private String departmentId;//部门Id
	//模糊查询
	private String str;//部门Id
	//判断是住院部下的科室，还是已选择科室( 1代表住院部下员工  ;0代表选择科室下员工)
	private String deptRange;
	/**生日显示字段**/
	private String birthdayView;
	/**  
	 * 
	 * @Fields virtualDeptCode : 虚拟科室code
	 *
	 */
	private String virtualDeptCode;
	/** *工资号*/ 
	private String wagesAccount;
	/** * 工资查询密码 */ 
	private String wagesPassword;
	/**  
	 * 
	 * @Fields minPhone  电话小号 
	 *
	 */
	private String minPhone;
	
	
	public String getWagesAccount() {
		return wagesAccount;
	}
	public void setWagesAccount(String wagesAccount) {
		this.wagesAccount = wagesAccount;
	}
	public String getWagesPassword() {
		return wagesPassword;
	}
	public void setWagesPassword(String wagesPassword) {
		this.wagesPassword = wagesPassword;
	}
	public Hospital getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}

	public SysDepartment getDeptId() {
		return deptId;
	}
	public void setDeptId(SysDepartment deptId) {
		this.deptId = deptId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public Integer getCanEditBill() {
		return canEditBill;
	}
	public void setCanEditBill(Integer canEditBill) {
		this.canEditBill = canEditBill;
	}
	public Integer getCanCharge() {
		return canCharge;
	}
	public void setCanCharge(Integer canCharge) {
		this.canCharge = canCharge;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getIsExpert() {
		return isExpert;
	}
	public void setIsExpert(Integer isExpert) {
		this.isExpert = isExpert;
	}
	public String getIdEntityCard() {
		return idEntityCard;
	}
	public void setIdEntityCard(String idEntityCard) {
		this.idEntityCard = idEntityCard;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getEsign() {
		return esign;
	}
	public void setEsign(String esign) {
		this.esign = esign;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public Integer getMofficePhoneVisible() {
		return mofficePhoneVisible;
	}
	public void setMofficePhoneVisible(Integer mofficePhoneVisible) {
		this.mofficePhoneVisible = mofficePhoneVisible;
	}
	public Integer getMmobileVisible() {
		return mmobileVisible;
	}
	public void setMmobileVisible(Integer mmobileVisible) {
		this.mmobileVisible = mmobileVisible;
	}
	public Integer getMemailVisible() {
		return memailVisible;
	}
	public void setMemailVisible(Integer memailVisible) {
		this.memailVisible = memailVisible;
	}
	public String getEmailPwd() {
		return emailPwd;
	}
	public void setEmailPwd(String emailPwd) {
		this.emailPwd = emailPwd;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String gethName() {
		return hName;
	}
	public void sethName(String hName) {
		this.hName = hName;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setNickName(String account) {
		this.account = account;
	}
	public String getNickName() {
		return account;
	}
	public void setDeptRange(String deptRange) {
		this.deptRange = deptRange;
	}
	public String getDeptRange() {
		return deptRange;
	}
	public String getWorkState() {
		return workState;
	}
	public void setWorkState(String workState) {
		this.workState = workState;
	}
	public String getIfcadre() {
		return ifcadre;
	}
	public void setIfcadre(String ifcadre) {
		this.ifcadre = ifcadre;
	}
	public String getBirthdayView() {
		return birthdayView;
	}
	public void setBirthdayView(String birthdayView) {
		this.birthdayView = birthdayView;
	}
	public String getNurseCode() {
		return nurseCode;
	}
	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}
	public String getVirtualDeptCode() {
		return virtualDeptCode;
	}
	public void setVirtualDeptCode(String virtualDeptCode) {
		this.virtualDeptCode = virtualDeptCode;
	}
	public String getMinPhone() {
		return minPhone;
	}
	public void setMinPhone(String minPhone) {
		this.minPhone = minPhone;
	}
	
}