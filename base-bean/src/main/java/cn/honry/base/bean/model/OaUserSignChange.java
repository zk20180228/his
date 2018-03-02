package cn.honry.base.bean.model;

import java.sql.Blob;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：用户电子签章表更表
 * @Author：donghe
 * @CreateDate：2017-7-19 11:29:36
 * @Modifier：donghe
 * @ModifyDate：2017-7-19 11:29:36
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class OaUserSignChange extends Entity{

	private static final long serialVersionUID = 1L;
	/**电子签章主键**/
	private String signId;
	/**旧密码**/
	private String oldSignPassword;
	/**新密码**/
	private String newSignPassword;
	/**旧名称**/
	private String oldSignName;
	/**新名称**/
	private String newSignName;
	/**旧签章或签名图片**/
	private Blob oldSignInfo;
	/**新签章或签名图片**/
	private Blob newSignInfo;
	/**旧签章分类**/
	private String oldSignCategory;
	/**新签章分类**/
	private String newSignCategory;
	/**旧签章类别**/
	private Integer oldSignType;
	/**新签章类别**/
	private Integer newSignType;
	/**旧用户账户或角色code或职务code**/
	private String oldUserAcc;
	/**新用户账户或角色code或职务code**/
	private String newUserAcc;
	/**旧用户名称或角色名称或职务名称**/
	private String oldUserAccName;
	/**旧用户名称或角色名称或职务名称**/
	private String newUserAccName;
	/**版本**/
	private Integer version	;
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public String getOldSignPassword() {
		return oldSignPassword;
	}
	public void setOldSignPassword(String oldSignPassword) {
		this.oldSignPassword = oldSignPassword;
	}
	public String getNewSignPassword() {
		return newSignPassword;
	}
	public void setNewSignPassword(String newSignPassword) {
		this.newSignPassword = newSignPassword;
	}
	public String getOldSignName() {
		return oldSignName;
	}
	public void setOldSignName(String oldSignName) {
		this.oldSignName = oldSignName;
	}
	public String getNewSignName() {
		return newSignName;
	}
	public void setNewSignName(String newSignName) {
		this.newSignName = newSignName;
	}
	public Blob getOldSignInfo() {
		return oldSignInfo;
	}
	public void setOldSignInfo(Blob oldSignInfo) {
		this.oldSignInfo = oldSignInfo;
	}
	public Blob getNewSignInfo() {
		return newSignInfo;
	}
	public void setNewSignInfo(Blob newSignInfo) {
		this.newSignInfo = newSignInfo;
	}
	public String getOldSignCategory() {
		return oldSignCategory;
	}
	public void setOldSignCategory(String oldSignCategory) {
		this.oldSignCategory = oldSignCategory;
	}
	public String getNewSignCategory() {
		return newSignCategory;
	}
	public void setNewSignCategory(String newSignCategory) {
		this.newSignCategory = newSignCategory;
	}
	public Integer getOldSignType() {
		return oldSignType;
	}
	public void setOldSignType(Integer oldSignType) {
		this.oldSignType = oldSignType;
	}
	public Integer getNewSignType() {
		return newSignType;
	}
	public void setNewSignType(Integer newSignType) {
		this.newSignType = newSignType;
	}
	public String getOldUserAcc() {
		return oldUserAcc;
	}
	public void setOldUserAcc(String oldUserAcc) {
		this.oldUserAcc = oldUserAcc;
	}
	public String getNewUserAcc() {
		return newUserAcc;
	}
	public void setNewUserAcc(String newUserAcc) {
		this.newUserAcc = newUserAcc;
	}
	public String getOldUserAccName() {
		return oldUserAccName;
	}
	public void setOldUserAccName(String oldUserAccName) {
		this.oldUserAccName = oldUserAccName;
	}
	public String getNewUserAccName() {
		return newUserAccName;
	}
	public void setNewUserAccName(String newUserAccName) {
		this.newUserAccName = newUserAccName;
	}
}