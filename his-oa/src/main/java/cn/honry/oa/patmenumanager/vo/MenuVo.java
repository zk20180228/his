package cn.honry.oa.patmenumanager.vo;

import java.io.Serializable;
import java.util.List;

public class MenuVo implements Serializable {

	private static final long serialVersionUID = -255805226418299304L;
	/**栏目id**/
	private String id;
	/**栏目名称**/
	private String name;
	/**栏目code**/
	private String code;
	/**栏目父级code**/
	private String parentcode;
	/**栏目路径**/
	private String path;
	/**栏目路径**/
	private String state;
	/**栏目父级路径**/
	private String parentpath;
	/**排序**/
	private Integer morder;
	/**是否删除0:未删除,1:删除**/
	private Integer del_flag;
	/**是否停用0:使用中,1:已停用**/
	private Integer stop_flag;
	/**直接发布0:可以1:不可以**/
	private Integer publishdirt;
	/**是否评论0:关闭评论1:开启评论**/
	private Integer mcomment;
	/**栏目说明**/
	private String explain;
	/**所属部门**/
	private String dept;
	/**发布**/
	private String mpublish;
	/**审核**/
	private String mcheck;
	/**订阅**/
	private String mview;
	/**父id**/
	private String parent;
	/**type类型**/
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMpublish() {
		return mpublish;
	}
	public void setMpublish(String mpublish) {
		this.mpublish = mpublish;
	}
	public String getMcheck() {
		return mcheck;
	}
	public void setMcheck(String mcheck) {
		this.mcheck = mcheck;
	}
	public String getMview() {
		return mview;
	}
	public void setMview(String mview) {
		this.mview = mview;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public Integer getMcomment() {
		return mcomment;
	}
	public void setMcomment(Integer mcomment) {
		this.mcomment = mcomment;
	}
	public Integer getPublishdirt() {
		return publishdirt;
	}
	public void setPublishdirt(Integer publishdirt) {
		this.publishdirt = publishdirt;
	}
	public List<MenuVo> children;
	
	public List<MenuVo> getChildren() {
		return children;
	}
	public void setChildren(List<MenuVo> children) {
		this.children = children;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getParentpath() {
		return parentpath;
	}
	public void setParentpath(String parentpath) {
		this.parentpath = parentpath;
	}
	public Integer getDel_flag() {
		return del_flag;
	}
	public void setDel_flag(Integer del_flag) {
		this.del_flag = del_flag;
	}
	public Integer getStop_flag() {
		return stop_flag;
	}
	public void setStop_flag(Integer stop_flag) {
		this.stop_flag = stop_flag;
	}
	public Integer getMorder() {
		return morder;
	}
	public void setMorder(Integer morder) {
		this.morder = morder;
	}

}
