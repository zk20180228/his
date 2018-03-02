package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 会签配置实体
 * @author luyanshou
 *
 */
public class OaBpmConfCountersign extends Entity {

	private static final long serialVersionUID = 1L;

	/** 会签次序. */
    private Integer sequential;

    /** 参与者. */
    private String participant;

    /** 类型. */
    private Integer type;

    /** 等级. */
    private Integer rate;
    
    /**所属节点id(外键)*/
    private String nodeId;

    /**策略 (数据库无关字段)*/
    private String strategy;
    
	public Integer getSequential() {
		return sequential;
	}

	public void setSequential(Integer sequential) {
		this.sequential = sequential;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
    
}
