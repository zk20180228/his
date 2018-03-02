package cn.honry.oa.meeting.emGroup.vo;

import java.io.Serializable;

/**
 * 
 * <p>院长办公室会议出席人员维护 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年9月4日 下午8:02:26 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年9月4日 下午8:02:26 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class EmGroupVo implements Serializable{


	private static final long serialVersionUID = 8099560304969810796L;
	
	private String id;//主键
	private String employee_name="";//员工姓名
	private String employee_jobon="";//员工号
	private String dept_code="";//科室代码
	private String dept_name="";//科室名称
	private String duties_type="";//职务代码
	private String duties_name="";//职务名称
	private String title_type="";//职称代码
	private String title_name="";//职称名称
	private String employee_type_name="";//人员类型
	private String employee_group="";//组代码
	private String employee_group_name="";//会议组名
	private String creatTime="";//创建时间
	private String creatUesr="";//创建人
	private String updateTime="";//更新时间
	private String updateUser="";//修改人
	private String option="";
	
	
	
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getEmployee_jobon() {
		return employee_jobon;
	}
	public void setEmployee_jobon(String employee_jobon) {
		this.employee_jobon = employee_jobon;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDuties_name() {
		return duties_name;
	}
	public void setDuties_name(String duties_name) {
		this.duties_name = duties_name;
	}
	public String getTitle_name() {
		return title_name;
	}
	public void setTitle_name(String title_name) {
		this.title_name = title_name;
	}
	public String getEmployee_type_name() {
		return employee_type_name;
	}
	public void setEmployee_type_name(String employee_type_name) {
		this.employee_type_name = employee_type_name;
	}
	public String getCreatUesr() {
		return creatUesr;
	}
	public void setCreatUesr(String creatUesr) {
		this.creatUesr = creatUesr;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
	public String getDuties_type() {
		return duties_type;
	}
	public void setDuties_type(String duties_type) {
		this.duties_type = duties_type;
	}
	public String getTitle_type() {
		return title_type;
	}
	public void setTitle_type(String title_type) {
		this.title_type = title_type;
	}
	public String getEmployee_group() {
		return employee_group;
	}
	public void setEmployee_group(String employee_group) {
		this.employee_group = employee_group;
	}
	public String getEmployee_group_name() {
		return employee_group_name;
	}
	public void setEmployee_group_name(String employee_group_name) {
		this.employee_group_name = employee_group_name;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	
	
	
	
}
