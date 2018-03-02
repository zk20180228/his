package cn.honry.base.bean.model;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MatFiles extends Entity implements java.io.Serializable {

	/**文件类别(0产品注册附件,1厂商许可证,2字典附件)**/
	private Integer fileKind;
	/**名称**/
	private String fileName;
	/**对应主表的主键**/
	private String foreignCode;
	/**排列序号**/
	private BigDecimal orderNo;
	/**文件路径**/
	private String filePath;
	/**备注**/
	private String memo;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	/**文件**/
	private Blob file;
	
	public Blob getFile() {
		return file;
	}

	public void setFile(Blob file) {
		this.file = file;
	}

	public Integer getFileKind() {
		return this.fileKind;
	}

	public void setFileKind(Integer fileKind) {
		this.fileKind = fileKind;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getForeignCode() {
		return this.foreignCode;
	}

	public void setForeignCode(String foreignCode) {
		this.foreignCode = foreignCode;
	}

	public BigDecimal getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(BigDecimal orderNo) {
		this.orderNo = orderNo;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

}