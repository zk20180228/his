package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 频次实体类
 * @author liujinliang
 * Date:2015/5/20 17:39
 */
public class Hospital {
	/**医院主键**/
	private Integer id;
	/**6位省市县编号+6位流水号**/
	private String code;
	/**名称**/
	private String name;
	/**简称**/
	private String brev;
	/**logo**/
	private String logo;
	/**照片**/
	private String photo;
	/**所在省市县**/
	private String district;
	/**创建时间 zpty20150526**/
	private Date createDate;
	/**说明**/
	private String description;
	/**交通路线**/
	private String trafficRoutes;
	/**详细地址**/
	private String address;
	/**等级 1:三甲 2：三乙 3：三丙；...**/
	private String level;
	/**营利性 1:营利性2非营利性**/
	private String rentability;
	/**性质 1公立2私营**/
	private Integer property;
	/**医生总数**/
	private Integer doctorNum=0;
	/**护士总数**/
	private Integer nurseNum=0;
	/**备注**/
	private String remark;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getBrev() {
		return brev;
	}
	public void setBrev(String brev) {
		this.brev = brev;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTrafficRoutes() {
		return trafficRoutes;
	}
	public void setTrafficRoutes(String trafficRoutes) {
		this.trafficRoutes = trafficRoutes;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getRentability() {
		return rentability;
	}
	public void setRentability(String rentability) {
		this.rentability = rentability;
	}
	public Integer getProperty() {
		return property;
	}
	public void setProperty(Integer property) {
		this.property = property;
	}
	public Integer getDoctorNum() {
		return doctorNum;
	}
	public void setDoctorNum(Integer doctorNum) {
		this.doctorNum = doctorNum;
	}
	public Integer getNurseNum() {
		return nurseNum;
	}
	public void setNurseNum(Integer nurseNum) {
		this.nurseNum = nurseNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	
	
	
	
}
