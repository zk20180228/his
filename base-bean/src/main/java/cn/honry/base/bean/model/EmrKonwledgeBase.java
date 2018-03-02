/**
 * 
 */
package cn.honry.base.bean.model;

import java.sql.Clob;

import cn.honry.base.bean.business.Entity;

/**
 * @author abc
 *电子病历知识库
 */
public class EmrKonwledgeBase extends Entity{
	/**
	 * 知识库编码  14位   前两位分类库   后十二位每层补零三位序号
	 */
	private String konwCode;
	/**
	 * 知识分类库：01治愈好转标准库02医疗护理技术操作常规03药品库04法律规范库05鉴别诊断知识库06诊疗计划知识库
	 */
	private String konwLib;
	/**
	 * 上级分类   存上级知识编码
	 */
	private String konwType;
	/**
	 * 知识名称或分类名称
	 */
	private String konwName;
	/**
	 * 拼音码
	 */
	private String konwPinYin;
	/**
	 * 五笔码
	 */
	private String konwWb;
	/**
	 * 自定义码
	 */
	private String konwInputCode;
	/**
	 * 节点级别,根节点是1级
	 */
	private Integer konwLevel;
	/**
	 * 知识标志    0 表示是分类1表示是终极节点，也就是知识
	 */
	private Integer konwFlag;
	/**
	 * 排序号  每个节点下的重新排
	 */
	private Integer konwOrder;
	/**
	 * 只是路径  从根节点八位补零排序号到自己的八位补零排序号
	 */
	private String konwPath;
	/**
	 * 知识内容  大数据存Html
	 */
	private Clob konwContent;
	
	//与数据库无关字段
	/**
	 * 字符串内容
	 */
	private String strContent;
	/**  分页  **/
	private String page;
	private String rows;
	
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
	public String getStrContent() {
		return strContent;
	}
	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	public String getKonwCode() {
		return konwCode;
	}
	public void setKonwCode(String konwCode) {
		this.konwCode = konwCode;
	}
	public String getKonwLib() {
		return konwLib;
	}
	public void setKonwLib(String konwLib) {
		this.konwLib = konwLib;
	}
	public String getKonwType() {
		return konwType;
	}
	public void setKonwType(String konwType) {
		this.konwType = konwType;
	}
	public String getKonwName() {
		return konwName;
	}
	public void setKonwName(String konwName) {
		this.konwName = konwName;
	}
	public String getKonwPinYin() {
		return konwPinYin;
	}
	public void setKonwPinYin(String konwPinYin) {
		this.konwPinYin = konwPinYin;
	}
	public String getKonwWb() {
		return konwWb;
	}
	public void setKonwWb(String konwWb) {
		this.konwWb = konwWb;
	}
	public String getKonwInputCode() {
		return konwInputCode;
	}
	public void setKonwInputCode(String konwInputCode) {
		this.konwInputCode = konwInputCode;
	}
	public Integer getKonwLevel() {
		return konwLevel;
	}
	public void setKonwLevel(Integer konwLevel) {
		this.konwLevel = konwLevel;
	}
	public Integer getKonwFlag() {
		return konwFlag;
	}
	public void setKonwFlag(Integer konwFlag) {
		this.konwFlag = konwFlag;
	}
	public Integer getKonwOrder() {
		return konwOrder;
	}
	public void setKonwOrder(Integer konwOrder) {
		this.konwOrder = konwOrder;
	}
	public String getKonwPath() {
		return konwPath;
	}
	public void setKonwPath(String konwPath) {
		this.konwPath = konwPath;
	}
	public Clob getKonwContent() {
		return konwContent;
	}
	public void setKonwContent(Clob konwContent) {
		this.konwContent = konwContent;
	}
	
}
