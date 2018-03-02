package cn.honry.base.bean.model;
import java.util.Date;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  供应商信息管理
 * @Author：zpty
 * @CreateDate：2017-11-14 上午09:35:05  
 *
 */
public class AssetsSupplier extends Entity{

	
	private static final long serialVersionUID = 1L;

	/** 公司编码   **/
	private String code;
	/** 公司名称   **/
	private String name;
	/** 公司法人   **/
	private String legal;
	/** 公司电话   **/
	private String phone;
	/** 公司地址   **/
	private String address;
	/** 公司传真   **/
	private String telautogram;
	/** 公司邮箱   **/
	private String mail;
	/** 开户银行   **/
	private String bankName;
	/**开户账号*/
	private String bankAcco;
	/** 联系人 **/
	private String linkMan;
	/** 联系电话 **/
	private String linkPhone;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegal() {
		return legal;
	}
	public void setLegal(String legal) {
		this.legal = legal;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelautogram() {
		return telautogram;
	}
	public void setTelautogram(String telautogram) {
		this.telautogram = telautogram;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAcco() {
		return bankAcco;
	}
	public void setBankAcco(String bankAcco) {
		this.bankAcco = bankAcco;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
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