package cn.honry.base.bean.model;

import java.util.List;
import java.util.Set;

import cn.honry.base.bean.business.Entity;

/**
 * 系统栏目表
 * SysMenu entity. @author aizhonghua
 */

public class SysMenu extends Entity {

	/**功能**/
	private SysMenufunction menufunction;
	/**名称**/
	private String name;
	/**别名**/
	private String alias;
	/**类型1:系统栏目2：一般栏目**/
	private Integer type;
	/**父级**/
	private String parent;
	/**子菜单标志**/
	private Integer haveson = 0;
	/**链接**/
	private String url;
	/**参数**/
	private String parameter;
	/**图标**/
	private String icon;
	/**说明**/
	private String description;
	/**排序**/
	private Integer order;
	/**路径**/
	private String path;
	/**层级**/
	private Integer level;
	/**所有父级**/
	private String uppath;
	/**打开方式**/
	private String openmode;
	/**审核标志**/
	private Integer needcheck;
	/**栏目排序**/
	private Integer levelOrder;
	
	/**栏目功能按钮**/
	private List<SysMenuButton> buttonList;
	/**角色-栏目**/
	private Set<SysRoleMenuRelation> roleMenuList;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String inputcode;
	/** 
	* @Fields belong : 栏目归属 1平台，2移动，3移动与平台 
	*/ 
	private Integer belong;
	/**  
	 * 
	 * @Fields applyIsNeed : 是否需要申请0不需要 1需要
	 *
	 */
	private Integer applyIsNeed = 0;
	/**  
	 * 
	 * @Fields interfacePath : 接口路径
	 *
	 */
	private String interfacePath;
	
	/**  
	 * 
	 * @Fields interfacePath : 删除图标
	 *
	 */
	private String menuIconPathDel;
	/**  
	 * 
	 * @Fields interfacePath : 开放状态
	 *
	 */
	private Integer openState;
	
	
	public String getMenuIconPathDel() {
		return menuIconPathDel;
	}
	public void setMenuIconPathDel(String menuIconPathDel) {
		this.menuIconPathDel = menuIconPathDel;
	}
	public Integer getOpenState() {
		return openState;
	}
	public void setOpenState(Integer openState) {
		this.openState = openState;
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
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
	public SysMenufunction getMenufunction() {
		return menufunction;
	}
	public void setMenufunction(SysMenufunction menufunction) {
		this.menufunction = menufunction;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Integer getHaveson() {
		return haveson;
	}
	public void setHaveson(Integer haveson) {
		this.haveson = haveson;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getUppath() {
		return uppath;
	}
	public void setUppath(String uppath) {
		this.uppath = uppath;
	}
	public String getOpenmode() {
		return openmode;
	}
	public void setOpenmode(String openmode) {
		this.openmode = openmode;
	}
	public Integer getNeedcheck() {
		return needcheck;
	}
	public void setNeedcheck(Integer needcheck) {
		this.needcheck = needcheck;
	}
	public List<SysMenuButton> getButtonList() {
		return buttonList;
	}
	public void setButtonList(List<SysMenuButton> buttonList) {
		this.buttonList = buttonList;
	}
	public Set<SysRoleMenuRelation> getRoleMenuList() {
		return roleMenuList;
	}
	public void setRoleMenuList(Set<SysRoleMenuRelation> roleMenuList) {
		this.roleMenuList = roleMenuList;
	}
	public Integer getLevelOrder() {
		return levelOrder;
	}
	public void setLevelOrder(Integer levelOrder) {
		this.levelOrder = levelOrder;
	}
	public Integer getBelong() {
		return belong;
	}
	public void setBelong(Integer belong) {
		this.belong = belong;
	}
	public Integer getApplyIsNeed() {
		return applyIsNeed;
	}
	public void setApplyIsNeed(Integer applyIsNeed) {
		this.applyIsNeed = applyIsNeed;
	}
	public String getInterfacePath() {
		return interfacePath;
	}
	public void setInterfacePath(String interfacePath) {
		this.interfacePath = interfacePath;
	}
	
}