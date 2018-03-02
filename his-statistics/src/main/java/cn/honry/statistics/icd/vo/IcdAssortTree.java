package cn.honry.statistics.icd.vo;

import java.io.Serializable;
/**
 * 
 * <p>icd分类tree树 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月11日 下午1:56:18 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月11日 下午1:56:18 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class IcdAssortTree implements Serializable{
	
	private static final long serialVersionUID = 1199719074301111430L;
	
	private String id;  //icd分类的id
	private String text; //icd分类名称 
	private String state;//状态："closed"||"open"
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
    
    
	
	
	

}
