package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 任务参与者实体类
 * @author luyanshou
 *
 */
public class OaTaskParticipant extends Entity {

	
	private static final long serialVersionUID = 1L;

	 /** 分类. */
    private String category;

    /** 类型. */
    private String type;

    /** 外部引用. */
    private String ref;
    
    /**所属任务id (外键)*/
    private String taskId;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
    
}
