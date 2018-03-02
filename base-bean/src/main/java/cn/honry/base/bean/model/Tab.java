package cn.honry.base.bean.model;



/**  
 *  
 * @className：Tab 
 * @Description：  用于动态加载tab页签的实体类  id tab 的id  name tab显示的名称
 * @Author：aizhonghua
 * @CreateDate：2015-6-5 下午06:08:29  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-5 下午06:08:29  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class Tab {
	private String id;
	private String name;
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
	
}
