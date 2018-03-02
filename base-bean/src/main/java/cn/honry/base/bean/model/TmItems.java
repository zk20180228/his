package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

public class TmItems implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	/**项目编码**/
	private String code;
	/**项目名称**/
	private String name;
	/**项目分类代码**/
	private Integer type;
	/**项目分类名称**/
	private String typeName;
	/**建立时间**/
	private Date createTime;
	/**修改时间**/
	private Date updateTime;
	/**停用标志**/
	private Integer stop_flg=0;
	/**删除标志**/
	private Integer del_flg=0;
	/**栏目级别  1=1级栏目 ,2=2级栏目,3=3级栏目**/
	private Integer isParent;
	/**栏目访问路径**/
	private String path;
	/**父级id**/
	private String parentId;
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getIsParent() {
		return isParent;
	}
	public void setIsParent(Integer isParent) {
		this.isParent = isParent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
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
	
}
