package cn.honry.base.bean.model;

import java.sql.Blob;

import cn.honry.base.bean.business.Entity;
/**
 * 医用图片维护实体类
* @ClassName: EmcPicture
* @Description: 
* @author yeguanqun
* @date 2016年5月18日 下午1:52:29
*
 */
@SuppressWarnings("serial")
public class EmcPicture extends Entity implements java.io.Serializable {
	/**图片编码  **/
	private String picCode;
	/**图片名称  **/
	private String picName;
	/**身体部位  **/
	private String picBody;
	/**图片路径  **/
	private String picPath;	
	/**图片内容  **/
	private Blob picFile;
	/**图片说明  **/
	private String picBak;
	/**拼音码  **/
	private String pingyin;
	/**五笔码  **/
	private String wb;
	/**自定义码  **/
	private String inputcode;
	
	public String getPicCode() {
		return picCode;
	}
	public void setPicCode(String picCode) {
		this.picCode = picCode;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getPicBody() {
		return picBody;
	}
	public void setPicBody(String picBody) {
		this.picBody = picBody;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Blob getPicFile() {
		return picFile;
	}
	public void setPicFile(Blob picFile) {
		this.picFile = picFile;
	}
	public String getPicBak() {
		return picBak;
	}
	public void setPicBak(String picBak) {
		this.picBak = picBak;
	}
	public String getPingyin() {
		return pingyin;
	}
	public void setPingyin(String pingyin) {
		this.pingyin = pingyin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
		
}
