package cn.honry.oa.activiti.rule;

import java.util.List;

/**
 * 配置任务负责人规则的接口
 * @author luyanshou
 *
 */
public interface AssigneeRule {

	public List<String> getUser(String initiator);//获取负责人(员工jobNo)
}
