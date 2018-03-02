package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Title: 
 * Description:
 * @author cxw
 * @time 2017年11月22日 上午10:49:25
 */
public class DocManage implements Serializable{

	/* 上传文档id */
	private String id;
	/* 上传科室 */
	private String uploadDept;
	/* 文档描述 */
	private String docDes;
	/* 文档下载地址 */
	private String docDownAddr;
	/* 上传人员 */
	private String createUser;
	/* 上传时间 */
	private Date createDate;
	/* 文档名称 */
	private String docName;
	/* 上传类型 */
	private String deptType;
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUploadDept() {
		return uploadDept;
	}
	public void setUploadDept(String uploadDept) {
		this.uploadDept = uploadDept;
	}
	public String getDocDes() {
		return docDes;
	}
	public void setDocDes(String docDes) {
		this.docDes = docDes;
	}
	public String getDocDownAddr() {
		return docDownAddr;
	}
	public void setDocDownAddr(String docDownAddr) {
		this.docDownAddr = docDownAddr;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDeptType() {
		return deptType;
	}
	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
	
	
}
