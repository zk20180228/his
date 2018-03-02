package cn.honry.base.bean.model;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  医院档案管理
 * @Author：cxw
 * @CreateDate：2017-11-20 上午09:35:05  
 *
 */
/**
 * Title: 
 * Description:
 * @author cxw
 * @time 2017年11月21日 上午9:15:23
 */
public class HospitalFileManager extends Entity{

	
	private static final long serialVersionUID = 1L;

	/** 档案编号   **/
	private String fileNumber;
	/** 档案分类   **/
	private String fileClassify;
	/** 档案级别 **/
	private String fileRank;
	/** 科室名称   **/
	private String deptName;
	/** 档案名称   **/
	private String name;
	/** 档案类型   **/
	private String fileType;
	/** 档案路径   **/
	private String fileURL;
	/** 档案状态   **/
	private String fileStatus;
	/** 档案负责人   **/
	private String fileMan;
	/** 是否可借阅 **/
	private String borrow;   //0：可以   1：否
	public String getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	public String getFileClassify() {
		return fileClassify;
	}
	public void setFileClassify(String fileClassify) {
		this.fileClassify = fileClassify;
	}
	public String getFileRank() {
		return fileRank;
	}
	public void setFileRank(String fileRank) {
		this.fileRank = fileRank;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getFileMan() {
		return fileMan;
	}
	public void setFileMan(String fileMan) {
		this.fileMan = fileMan;
	}
	public String getBorrow() {
		return borrow;
	}
	public void setBorrow(String borrow) {
		this.borrow = borrow;
	}
	
	
	
	
	
}