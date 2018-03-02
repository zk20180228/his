package cn.honry.base.bean.model;
import java.util.Date;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  采购计划审核
 * @Author：zpty
 * @CreateDate：2017-11-20 上午09:35:05  
 *
 */
public class MatPlanCheck extends Entity{

	
	private static final long serialVersionUID = 1L;

	/** 采购流水号   **/
	private String procurementNo;
	/** 审核人   **/
	private String checkUser;
	/** 审核人姓名   **/
	private String checkUserName;
	/** 审核标志 (0:通过,1未通过)  **/
	private Integer checkFlag;
	/** 审核时间   **/
	private Date checkDate;
	/** 审核意见   **/
	private String checkOpinion;
	
	/**所属医院   **/
	private Integer hospitalId;
	/** 所属院区   **/
	private String areaCode;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getProcurementNo() {
		return procurementNo;
	}
	public void setProcurementNo(String procurementNo) {
		this.procurementNo = procurementNo;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public Integer getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public String getCheckOpinion() {
		return checkOpinion;
	}
	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}