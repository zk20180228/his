package cn.honry.base.bean.model;

import java.util.Date;

/**  
 * 
 * <p>消息推送实体</p>
 * @Author: dutianliang
 * @CreateDate: 2017年7月11日 下午2:10:51 
 * @Modifier: dutianliang
 * @ModifyDate: 2017年7月11日 下午2:10:51 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class OFBatchPush {

	private Integer id;
	private String body;//消息推送内容，map tojson
	private Date createTime;
	private int status;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
