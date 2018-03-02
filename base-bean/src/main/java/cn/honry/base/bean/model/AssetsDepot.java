package cn.honry.base.bean.model;
import java.util.Date;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  仓库维护
 * @Author：zpty
 * @CreateDate：2017-11-14 上午09:35:05  
 *
 */
public class AssetsDepot extends Entity{

	
	private static final long serialVersionUID = 1L;

	/** 仓库编码   **/
	private String depotCode;
	/** 仓库名称   **/
	private String depotName;
	/** 仓库地点   **/
	private String address;
	/** 仓库管理员工号   **/
	private String manageAcc;
	/** 仓库管理员姓名   **/
	private String manageName;
	/** 联系电话   **/
	private String phone;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getManageAcc() {
		return manageAcc;
	}
	public void setManageAcc(String manageAcc) {
		this.manageAcc = manageAcc;
	}
	public String getManageName() {
		return manageName;
	}
	public void setManageName(String manageName) {
		this.manageName = manageName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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