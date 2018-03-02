package cn.honry.base.bean.model;

import java.sql.Blob;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class Information extends Entity {
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
	/** 排序 **/
	private Integer infoOrder;
	/** 是否置顶 **/
	private Integer infoIstop;
	/** 是否发布 1_是；0_否；2_草稿**/
	private Integer infoPubflag;
	/** 发布时间 **/
	private Date infoPubtime;
	/** 发布人**/
	private String infoPubuser;
	/** 攥写人**/
	private String infoWirteuser;
	/** 审核人**/
	private String infoChecker;
	/** 审核标记（默认审核）0_未审核；1_审核2_未通过**/
	private Integer infoCheckFlag = 0;
	/*
	 * 标题图片地址
	 */
	private String titleImg;
	/*
	 * 文章过期时间
	 */
	private Date outTime;
	/**附件地址（冗余字段）**/
	private String attachURL;
	/**附件名称（冗余字段）**/
	private String attachName;
	/**附件权限（按一定规则拼成）（冗余字段）**/
	private String attach;
	/**权限id**/
	private String authid;
	/**发布人姓名**/
	private String pubuserName;
	/**撰写人姓名**/
	private String writerName;
	/**审核人姓名**/
	private String checkerName;
	/**作者姓名**/
	private String editorName;
	private Integer isRead;
	/**
	 * 浏览次数
	 */
	private Integer views = 0;
	private String time;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
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
	public String getTitleImg() {
		return titleImg;
	}
	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}
	public Date getOutTime() {
		return outTime;
	}
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	public String getAttachURL() {
		return attachURL;
	}
	public void setAttachURL(String attachURL) {
		this.attachURL = attachURL;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getAuthid() {
		return authid;
	}
	public void setAuthid(String authid) {
		this.authid = authid;
	}
	public String getPubuserName() {
		return pubuserName;
	}
	public void setPubuserName(String pubuserName) {
		this.pubuserName = pubuserName;
	}
	public String getWriterName() {
		return writerName;
	}
	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getEditorName() {
		return editorName;
	}
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	
}
