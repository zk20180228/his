package cn.honry.oa.repository.repositoryStat.vo;



/**  
 * 
 * 知识库统计
 * @Author: wangshujuan
 * @CreateDate: 2017年11月18日 下午4:09:43 
 * @Modifier: wangshujuan
 * @ModifyDate: 2017年11月18日 下午4:09:43 
 * @ModifyRmk:  
 * @version: V1.0
 * @param deptCode 
 *
 */
public class RepositoryStatVo {
	private String deptCode;//部门Code
	private String deptName;//部门名称
	private Integer total;//知识发表量-科室
	private Integer totalzz;//知识发表量--作者
	private String createUser;//创建人
	private String name;//标题名称
	private String categName;//知识分类
	private String views;//浏览总量
	private String createTime;//浏览总量
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategName() {
		return categName;
	}
	public void setCategName(String categName) {
		this.categName = categName;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getTotalzz() {
		return totalzz;
	}
	public void setTotalzz(Integer totalzz) {
		this.totalzz = totalzz;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
	
	
}
