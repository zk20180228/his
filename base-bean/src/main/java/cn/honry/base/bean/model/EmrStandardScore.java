package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * @Description  电子病历质控：评分标准表
 * @author  marongbin
 * @createDate： 2017年4月22日 上午10:25:29 
 * @modifier 
 * @modifyDate：
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public class EmrStandardScore {
	/**评分编码**/
	private String scoreId;
	/**大项编号**/
	private String itemId;
	/**评分编号**/
	private String scoreCode;
	/**评分描述**/
	private String scoreDesc;
	/**扣分**/
	private Double scoreValue;
	/**评分标准**/
	private String scoreBak;
	/**自动评分标志 1是0否**/
	private Integer scoreAutoFlag;
	/**关键字段**/
	private String scoreField;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	/**修改人员**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	/**删除人员**/
	private String deleteUser;
	/**删除时间**/
	private Date deleteTime;	
	/**停用标志**/
	private Integer stop_flg=0;
	/**删除标志**/
	private Integer del_flg=0;
	private Integer single = 0;
	public String getScoreId() {
		return scoreId;
	}
	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getScoreCode() {
		return scoreCode;
	}
	public void setScoreCode(String scoreCode) {
		this.scoreCode = scoreCode;
	}
	public String getScoreDesc() {
		return scoreDesc;
	}
	public void setScoreDesc(String scoreDesc) {
		this.scoreDesc = scoreDesc;
	}
	public Double getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(Double scoreValue) {
		this.scoreValue = scoreValue;
	}
	public String getScoreBak() {
		return scoreBak;
	}
	public void setScoreBak(String scoreBak) {
		this.scoreBak = scoreBak;
	}
	public Integer getScoreAutoFlag() {
		return scoreAutoFlag;
	}
	public void setScoreAutoFlag(Integer scoreAutoFlag) {
		this.scoreAutoFlag = scoreAutoFlag;
	}
	public String getScoreField() {
		return scoreField;
	}
	public void setScoreField(String scoreField) {
		this.scoreField = scoreField;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
	}
	public Integer getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}
	public Integer getSingle() {
		return single;
	}
	public void setSingle(Integer single) {
		this.single = single;
	}
}
