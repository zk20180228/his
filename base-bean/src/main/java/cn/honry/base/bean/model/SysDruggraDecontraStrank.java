package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 药品等级医生职级对照
 * @author liuyuanyuan
 *@data 2015/06/01 09:55
 */
public class SysDruggraDecontraStrank implements Serializable {
	//编号
	private String id;
	//医药职级
	private String tpost;
	//职级名称
	private String postname;
	//药品等级
	private String druggraade;
	//等级名称
	private String  graadename;
	//描述
	private String description;
	//排序
	private Integer order;
	//适用医院
	private String  hospital;
	//不适用医院
	private String nonhospital;
	//建立人员
	private String createUser;
	//建立部门
	private String createDept;
	//建立时间
	private Date createTime;
	//修改人员
	private String updateUser;
	//修改时间
	private Date updateTime;
	//删除人员
	private String deleteUser;
	//删除时间
	private Date deleteTime;	
	//停用标志
	private Integer stop_flg=0;
	//删除标志
	private Integer del_flg=0;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTpost() {
		return tpost;
	}
	public void setTpost(String tpost) {
		this.tpost = tpost;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public String getDruggraade() {
		return druggraade;
	}
	public void setDruggraade(String druggraade) {
		this.druggraade = druggraade;
	}
	public String getGraadename() {
		return graadename;
	}
	public void setGraadename(String graadename) {
		this.graadename = graadename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getNonhospital() {
		return nonhospital;
	}
	public void setNonhospital(String nonhospital) {
		this.nonhospital = nonhospital;
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
	
	

}
