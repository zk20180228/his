package cn.honry.base.bean.model;

import java.sql.Clob;

import cn.honry.base.bean.business.Entity;

/**  
 * <p>知识库信息 </p>
 * @Author: mrb
 * @CreateDate: 2017年11月14日 下午5:05:11 
 * @Modifier: mrb
 * @ModifyDate: 2017年11月14日 下午5:05:11 
 * @ModifyRmk:  
 * @version: V1.0 
 *RepositoryInfo
 */
public class RepositoryInfo extends Entity{
	/**
	 * 标题
	 */
	private String name;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 关键字
	 */
	private String keyWord;
	/**
	 * 类别/分类编码
	 */
	private String categCode;
	/**
	 * 类别/分类名称
	 */
	private String categName;
	/**
	 * 来源
	 */
	private String origin;
	/**
	 * 附件
	 */
	private String attach;
	/**
	 * 附件名称
	 */
	private String attachName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态是否公开(0否1是)
	 */
	private Integer isOvert = 0;
	/**
	 * 是否为收藏(0否1是)
	 */
	private Integer isCollect = 0;
	/**
	 * 0-发布；1-草稿
	 */
	private Integer pubFlg = 0;
	/**
	 * 浏览次数
	 */
	private Integer views = 0;
	/**
	 * 冗余字段，信息内容
	 */
	private String contentHtml;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getCategCode() {
		return categCode;
	}
	public void setCategCode(String categCode) {
		this.categCode = categCode;
	}
	public String getCategName() {
		return categName;
	}
	public void setCategName(String categName) {
		this.categName = categName;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsOvert() {
		return isOvert;
	}
	public void setIsOvert(Integer isOvert) {
		this.isOvert = isOvert;
	}
	public Integer getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(Integer isCollect) {
		this.isCollect = isCollect;
	}
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public Integer getPubFlg() {
		return pubFlg;
	}
	public void setPubFlg(Integer pubFlg) {
		this.pubFlg = pubFlg;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	
}
