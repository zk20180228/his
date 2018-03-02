package cn.honry.base.bean.model;

import java.sql.Blob;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/***
 * 首页：文章信息表
 * @ClassName: SysInfo 
 * @Description: 
 * @author wfj
 * @date 2016年5月20日 下午1:56:20 
 */
public class SysInfo extends Entity{

	/** 栏目编号 **/
	private String infoMenuid;
	/** 类型 1_通知  2_公告 3_新闻 **/
	private Integer infoType;
	/** 标题**/
	private String infoTitle;
	/** 关键词 **/
	private String infoKeyword;
	/** 简介 **/
	private String infoBrev;
	/** 作者 **/
	private String infoEditor;
	/** 来源 **/
	private String infoSource;
	/** 附件 **/
	private String infoAttach;
	/** 内容 **/
	private Blob infoContent;
	/** 排序 **/
	private Integer infoOrder;
	/** 是否置顶 **/
	private Integer infoIstop;
	/** 是否发布 **/
	private Integer infoPubflag;
	/** 发布时间 **/
	private Date infoPubtime;
	/** 发布人**/
	private String infoPubuser;
	/** 攥写人**/
	private String infoWirteuser;
	/** 审核人**/
	private String infoChecker;
	/** 审核标记（默认审核）0_未审核；1_审核**/
	private Integer infoCheckFlag;
	
	
	public String getInfoMenuid() {
		return infoMenuid;
	}
	public void setInfoMenuid(String infoMenuid) {
		this.infoMenuid = infoMenuid;
	}
	public Integer getInfoType() {
		return infoType;
	}
	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}
	public String getInfoTitle() {
		return infoTitle;
	}
	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}
	public String getInfoKeyword() {
		return infoKeyword;
	}
	public void setInfoKeyword(String infoKeyword) {
		this.infoKeyword = infoKeyword;
	}
	public String getInfoBrev() {
		return infoBrev;
	}
	public void setInfoBrev(String infoBrev) {
		this.infoBrev = infoBrev;
	}
	public String getInfoEditor() {
		return infoEditor;
	}
	public void setInfoEditor(String infoEditor) {
		this.infoEditor = infoEditor;
	}
	public String getInfoSource() {
		return infoSource;
	}
	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}
	public String getInfoAttach() {
		return infoAttach;
	}
	public void setInfoAttach(String infoAttach) {
		this.infoAttach = infoAttach;
	}
	public Blob getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(Blob infoContent) {
		this.infoContent = infoContent;
	}
	public Integer getInfoOrder() {
		return infoOrder;
	}
	public void setInfoOrder(Integer infoOrder) {
		this.infoOrder = infoOrder;
	}
	public Integer getInfoIstop() {
		return infoIstop;
	}
	public void setInfoIstop(Integer infoIstop) {
		this.infoIstop = infoIstop;
	}
	public Integer getInfoPubflag() {
		return infoPubflag;
	}
	public void setInfoPubflag(Integer infoPubflag) {
		this.infoPubflag = infoPubflag;
	}
	public Date getInfoPubtime() {
		return infoPubtime;
	}
	public void setInfoPubtime(Date infoPubtime) {
		this.infoPubtime = infoPubtime;
	}
	public String getInfoPubuser() {
		return infoPubuser;
	}
	public void setInfoPubuser(String infoPubuser) {
		this.infoPubuser = infoPubuser;
	}
	public String getInfoWirteuser() {
		return infoWirteuser;
	}
	public void setInfoWirteuser(String infoWirteuser) {
		this.infoWirteuser = infoWirteuser;
	}
	public String getInfoChecker() {
		return infoChecker;
	}
	public void setInfoChecker(String infoChecker) {
		this.infoChecker = infoChecker;
	}
	public Integer getInfoCheckFlag() {
		return infoCheckFlag;
	}
	public void setInfoCheckFlag(Integer infoCheckFlag) {
		this.infoCheckFlag = infoCheckFlag;
	}
	
}
