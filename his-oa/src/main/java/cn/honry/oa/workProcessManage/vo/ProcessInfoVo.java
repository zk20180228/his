package cn.honry.oa.workProcessManage.vo;

import java.io.Serializable;

/**
 * 
 * <p>流程详情Vo</p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月18日 上午11:48:04 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月18日 上午11:48:04 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class ProcessInfoVo implements Serializable{
	
	private static final long serialVersionUID = 5201431957462945321L;
	private String id;//id
	private String title;//标题
	private String createTime;//创建时间
	private String processExplain;//流程说明
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getProcessExplain() {
		return processExplain;
	}
	public void setProcessExplain(String processExplain) {
		this.processExplain = processExplain;
	}
	
	

}
