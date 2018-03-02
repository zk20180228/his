package cn.honry.outpatient.cpway.vo;

import java.io.Serializable;

/**
 * 
 * <p>通用的tree树 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月25日 上午11:27:58 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月25日 上午11:27:58 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class ComboxVo implements Serializable{

	private static final long serialVersionUID = 1339482949560441300L;
	
	private String id;
	private String text;
	private String state;
	
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