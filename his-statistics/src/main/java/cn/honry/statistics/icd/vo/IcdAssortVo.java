package cn.honry.statistics.icd.vo;

import java.io.Serializable;

/**
 * 
 * <p>icd分类 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月11日 上午11:01:13 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月11日 上午11:01:13 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class IcdAssortVo implements Serializable {

	private static final long serialVersionUID = 5920277966118923363L;
	
		private String 	id;//ID
		private String 	assort_Name; //icd分类名称
		private Integer is_Parent=0;//是否是父节点
		private String 	parent_Id;//父节点id
		private Integer stop_Flag=0;//是否停用
		private Integer del_Flag=0;//是否删除
		private String 	createUser;
		private String 	updateUser; 
		private String 	delUser;
		private String 	createTime; 
		private String 	updateTime;
		private String 	delTime;
		
		
		
		
		private String  icdCode;//诊断码
		private String  icdName;//诊断名称
		private String  assortId;//所属分类id
		private Integer totalPages=0;//总页数
		
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getAssort_Name() {
			return assort_Name;
		}
		public void setAssort_Name(String assort_Name) {
			this.assort_Name = assort_Name;
		}
		public Integer getIs_Parent() {
			return is_Parent;
		}
		public void setIs_Parent(Integer is_Parent) {
			this.is_Parent = is_Parent;
		}
		public String getParent_Id() {
			return parent_Id;
		}
		public void setParent_Id(String parent_Id) {
			this.parent_Id = parent_Id;
		}
		public Integer getStop_Flag() {
			return stop_Flag;
		}
		public void setStop_Flag(Integer stop_Flag) {
			this.stop_Flag = stop_Flag;
		}
		public Integer getDel_Flag() {
			return del_Flag;
		}
		public void setDel_Flag(Integer del_Flag) {
			this.del_Flag = del_Flag;
		}
		public String getCreateUser() {
			return createUser;
		}
		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}
		public String getUpdateUser() {
			return updateUser;
		}
		public void setUpdateUser(String updateUser) {
			this.updateUser = updateUser;
		}
		public String getDelUser() {
			return delUser;
		}
		public void setDelUser(String delUser) {
			this.delUser = delUser;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}
		public String getDelTime() {
			return delTime;
		}
		public void setDelTime(String delTime) {
			this.delTime = delTime;
		}
		public String getIcdCode() {
			return icdCode;
		}
		public void setIcdCode(String icdCode) {
			this.icdCode = icdCode;
		}
		public String getIcdName() {
			return icdName;
		}
		public void setIcdName(String icdName) {
			this.icdName = icdName;
		}
		public Integer getTotalPages() {
			return totalPages;
		}
		public void setTotalPages(Integer totalPages) {
			this.totalPages = totalPages;
		}
		public String getAssortId() {
			return assortId;
		}
		public void setAssortId(String assortId) {
			this.assortId = assortId;
		}
		
		
		
		

}
