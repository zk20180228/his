package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;
/**
 *默认取药药房维护
 * @author wfj
 *@data 2016/1/4 
 */
public class PutGetDrug implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//编号
	private String id;
	//摆药科室编码
	private String putdrug;
	//取药科室编码
	private String getdrug;
	//药品类别
	private String drugtype;
	//开放时间
	private Date begintime;	
	//关闭时间
	private Date endtime;	
	//备注
	private String mark;
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
	public String getPutdrug() {
		return putdrug;
	}
	public void setPutdrug(String putdrug) {
		this.putdrug = putdrug;
	}
	public String getGetdrug() {
		return getdrug;
	}
	public void setGetdrug(String getdrug) {
		this.getdrug = getdrug;
	}
	public String getDrugtype() {
		return drugtype;
	}
	public void setDrugtype(String drugtype) {
		this.drugtype = drugtype;
	}
	public Date getBegintime() {
		return begintime;
	}
	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
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
