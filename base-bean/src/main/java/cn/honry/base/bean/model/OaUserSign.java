package cn.honry.base.bean.model;

import java.sql.Blob;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：用户电子签章维护
 * @Author：donghe
 * @CreateDate：2017-7-19 11:29:36
 * @Modifier：donghe
 * @ModifyDate：2017-7-19 11:29:36
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class OaUserSign extends Entity{

	private static final long serialVersionUID = 1L;
	/**用户账户或角色code或职务code**/
	private String userAcc;
	/**用户名称或角色名称或职务名称**/
	private String userAccName;
	/**签章类别1电子签名2电子签章**/
	private Integer signType;
	/**签章或签名名称**/
	private String signName;
	/**签章或签名密码**/
	private String signPassword;
	/**拼音码**/
	private String signPinYin;
	/**五笔码**/
	private String signWb;
	/**自定义码**/
	private String signInputcode;
	/**签章描述**/
	private String signDesc;
	/**签章或签名图片**/
	private Blob signInfo;
	/**签章分类(1用户,2角色,3职务,4科室)**/
	private String signCategory;
	/**版本**/
	private Integer version	;
	
	//赋值字段
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getSignCategory() {
		return signCategory;
	}
	public void setSignCategory(String signCategory) {
		this.signCategory = signCategory;
	}
	public String getUserAcc() {
		return userAcc;
	}
	public void setUserAcc(String userAcc) {
		this.userAcc = userAcc;
	}
	public String getUserAccName() {
		return userAccName;
	}
	public void setUserAccName(String userAccName) {
		this.userAccName = userAccName;
	}
	public Integer getSignType() {
		return signType;
	}
	public void setSignType(Integer signType) {
		this.signType = signType;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getSignPassword() {
		return signPassword;
	}
	public void setSignPassword(String signPassword) {
		this.signPassword = signPassword;
	}
	public String getSignPinYin() {
		return signPinYin;
	}
	public void setSignPinYin(String signPinYin) {
		this.signPinYin = signPinYin;
	}
	public String getSignWb() {
		return signWb;
	}
	public void setSignWb(String signWb) {
		this.signWb = signWb;
	}
	public String getSignInputcode() {
		return signInputcode;
	}
	public void setSignInputcode(String signInputcode) {
		this.signInputcode = signInputcode;
	}
	public String getSignDesc() {
		return signDesc;
	}
	public void setSignDesc(String signDesc) {
		this.signDesc = signDesc;
	}
	public Blob getSignInfo() {
		return signInfo;
	}
	public void setSignInfo(Blob signInfo) {
		this.signInfo = signInfo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}