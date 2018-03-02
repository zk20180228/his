package cn.honry.oa.meeting.emGroup.vo;

import java.io.Serializable;
/**
 * 
 * <p>组树vo </p>
 * @Author: zhangkui
 * @CreateDate: 2017年9月4日 下午8:03:20 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年9月4日 下午8:03:20 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class GroupVo implements Serializable {


	private static final long serialVersionUID = -3217637768050930547L;
	private String id;//节点id,组代码
	private String text;//节点名,组名
	private boolean checked;
	/************************************下面的主要用作新建会议组********************************************************/
//	private String code_type;//编码类型
//	private String code_pinyin;//拼音码
//	private String code_wb;//五笔码
	
//	private String code_inputcode;//自定义码
	private String code_mark;//备注
	private String createUser;//创建人
	//创建时间
	
	
	
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
	public String getCode_mark() {
		return code_mark;
	}
	public void setCode_mark(String code_mark) {
		this.code_mark = code_mark;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
	
}
