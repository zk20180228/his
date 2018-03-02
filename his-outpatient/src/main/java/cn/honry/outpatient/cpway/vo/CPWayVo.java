package cn.honry.outpatient.cpway.vo;

import java.io.Serializable;

/**
 * 
 * <p>临床路径出入申请的vo,对应T_PATH_APPLY表</p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月25日 上午11:39:33 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月25日 上午11:39:33 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class CPWayVo implements Serializable{

			private static final long serialVersionUID = -5037389114460457682L;
			
			//给默认值是因为操作jdbc时我需要的
			private String 	id=null;                 //出入境申请id
			private String 	cp_id=null;              //临床路径ID
			private String 	version_no=null;         //临床路径版本号
			private String 	apply_no=null;           //申请流水号
			private String 	inpatient_no;       //住院流水号
			private String 	medicalrecord_id=null;   //病历号
			private String 	apply_type=null;         //申请类别
			private String 	apply_status=null;       //申请状态0代表申请状态，1代表申请通过，2代表申请未通过
			private String 	apply_code=null;         //申请科室
			private String 	apply_doct_code=null;    //申请医生
			private String 	apply_date=null;          //申请时间
			private String 	apply_memo=null;         //申请说明
			private String 	approval_user=null;      //审批人
			private String 	approval_date=null;      //审批日期
			private String 	execute_date=null;       //执行日期
			private String 	updateUser=null;         //修改人
			private String 	updateTime=null;         //修改时间
			private Integer stop_flg=0;           //停止标志0：正常 1：停用
			private Integer del_flg=0;            //删除标志0：正常 1：停用
			private String 	hospital_id=null;        //所属医院
			private String 	area_code=null;          //所属院区
			private String 	createUser=null;         //创建人
			private String 	createTime=null;         //创建时间
			
			//非数据库字段，用于查询
			private String patientName;//患者姓名
			private String cpName;//临床路径的名称
			
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getCp_id() {
				return cp_id;
			}
			public void setCp_id(String cp_id) {
				this.cp_id = cp_id;
			}
			public String getVersion_no() {
				return version_no;
			}
			public void setVersion_no(String version_no) {
				this.version_no = version_no;
			}
			public String getApply_no() {
				return apply_no;
			}
			public void setApply_no(String apply_no) {
				this.apply_no = apply_no;
			}
			public String getInpatient_no() {
				return inpatient_no;
			}
			public void setInpatient_no(String inpatient_no) {
				this.inpatient_no = inpatient_no;
			}
			public String getMedicalrecord_id() {
				return medicalrecord_id;
			}
			public void setMedicalrecord_id(String medicalrecord_id) {
				this.medicalrecord_id = medicalrecord_id;
			}
			public String getApply_type() {
				return apply_type;
			}
			public void setApply_type(String apply_type) {
				this.apply_type = apply_type;
			}
			public String getApply_status() {
				return apply_status;
			}
			public void setApply_status(String apply_status) {
				this.apply_status = apply_status;
			}
			public String getApply_code() {
				return apply_code;
			}
			public void setApply_code(String apply_code) {
				this.apply_code = apply_code;
			}
			public String getApply_doct_code() {
				return apply_doct_code;
			}
			public void setApply_doct_code(String apply_doct_code) {
				this.apply_doct_code = apply_doct_code;
			}
			
			public String getApply_date() {
				return apply_date;
			}
			public void setApply_date(String apply_date) {
				this.apply_date = apply_date;
			}
			public String getApply_memo() {
				return apply_memo;
			}
			public void setApply_memo(String apply_memo) {
				this.apply_memo = apply_memo;
			}
			public String getApproval_user() {
				return approval_user;
			}
			public void setApproval_user(String approval_user) {
				this.approval_user = approval_user;
			}
			public String getApproval_date() {
				return approval_date;
			}
			public void setApproval_date(String approval_date) {
				this.approval_date = approval_date;
			}
			public String getExecute_date() {
				return execute_date;
			}
			public void setExecute_date(String execute_date) {
				this.execute_date = execute_date;
			}
			public String getUpdateUser() {
				return updateUser;
			}
			public void setUpdateUser(String updateUser) {
				this.updateUser = updateUser;
			}
			public String getUpdateTime() {
				return updateTime;
			}
			public void setUpdateTime(String updateTime) {
				this.updateTime = updateTime;
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
			public String getHospital_id() {
				return hospital_id;
			}
			public void setHospital_id(String hospital_id) {
				this.hospital_id = hospital_id;
			}
			public String getArea_code() {
				return area_code;
			}
			public void setArea_code(String area_code) {
				this.area_code = area_code;
			}
			public String getCreateUser() {
				return createUser;
			}
			public void setCreateUser(String createUser) {
				this.createUser = createUser;
			}
			public String getCreateTime() {
				return createTime;
			}
			public void setCreateTime(String createTime) {
				this.createTime = createTime;
			}
			public String getPatientName() {
				return patientName;
			}
			public void setPatientName(String patientName) {
				this.patientName = patientName;
			}
			public String getCpName() {
				return cpName;
			}
			public void setCpName(String cpName) {
				this.cpName = cpName;
			}
	
			
			
	
	
}
