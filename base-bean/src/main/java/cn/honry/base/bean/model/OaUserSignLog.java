package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：用户电子签章日志表
 * @Author：donghe
 * @CreateDate：2017-7-19 11:29:36
 * @Modifier：donghe
 * @ModifyDate：2017-7-19 11:29:36
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class OaUserSignLog extends Entity{

	private static final long serialVersionUID = 1L;
	/**电子签章主键**/
	private String signId;
	/**电子签章名称**/
	private String signName;
	/**使用人**/
	private String useOper;
	/**使用时间**/
	private Date useTime;
	/**使用模块**/
	private String useModule;
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getUseOper() {
		return useOper;
	}
	public void setUseOper(String useOper) {
		this.useOper = useOper;
	}
	public Date getUseTime() {
		return useTime;
	}
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
	public String getUseModule() {
		return useModule;
	}
	public void setUseModule(String useModule) {
		this.useModule = useModule;
	}
	
}