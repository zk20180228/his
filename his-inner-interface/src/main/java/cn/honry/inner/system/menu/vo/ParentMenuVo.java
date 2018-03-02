package cn.honry.inner.system.menu.vo;

public class ParentMenuVo {
	
	/**  
	 * 
	 * @Fields menu_name : 栏目名称
	 *
	 */
	private String name;
	/**  
	 * 
	 * @Fields menu_id : 栏目id
	 *
	 */
	private String id;
	private String alias;
	private String parent;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
}
