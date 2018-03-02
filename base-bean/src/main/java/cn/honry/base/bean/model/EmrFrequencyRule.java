package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @Description 电子病历质控：频率规则表
 * @author  marongbin
 * @createDate： 2017年4月25日 下午2:04:54 
 * @modifier 
 * @modifyDate：
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public class EmrFrequencyRule extends Entity {
	/**规则编码**/
	private String code;
	/**规则名称**/
	private String name;
	/**规则描述**/
	private String desc;
	/**规则分组**/
	private String group;
	/**责任级别**/
	private String responsibilityEvel;
	/**频率要求**/
	private Integer value;
	/**周(1)，天(2)，时(3)，分(4)，秒(5)**/
	private String unit;
	/**提示信息**/
	private String tip;
	/**警告信息**/
	private String warn;
	/**操作方式**/
	private Integer op;
	/**循环次数**/
	private Integer loop;
	/**循环间隔**/
	private Integer loopGap;
	/**处理方式**/
	private Integer dispose;
	/**相关规则**/
	private String relationRule;
	/**病历分类**/
	private String emrType;
	/**扣分原则**/
	private Double deductpionts;
	
	
	
	
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getResponsibilityEvel() {
		return responsibilityEvel;
	}
	public void setResponsibilityEvel(String responsibilityEvel) {
		this.responsibilityEvel = responsibilityEvel;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getWarn() {
		return warn;
	}
	public void setWarn(String warn) {
		this.warn = warn;
	}
	public Integer getOp() {
		return op;
	}
	public void setOp(Integer op) {
		this.op = op;
	}
	public Integer getLoop() {
		return loop;
	}
	public void setLoop(Integer loop) {
		this.loop = loop;
	}
	public Integer getLoopGap() {
		return loopGap;
	}
	public void setLoopGap(Integer loopGap) {
		this.loopGap = loopGap;
	}
	public Integer getDispose() {
		return dispose;
	}
	public void setDispose(Integer dispose) {
		this.dispose = dispose;
	}
	public String getRelationRule() {
		return relationRule;
	}
	public void setRelationRule(String relationRule) {
		this.relationRule = relationRule;
	}
	public String getEmrType() {
		return emrType;
	}
	public void setEmrType(String emrType) {
		this.emrType = emrType;
	}
	public Double getDeductpionts() {
		return deductpionts;
	}
	public void setDeductpionts(Double deductpionts) {
		this.deductpionts = deductpionts;
	}
}
