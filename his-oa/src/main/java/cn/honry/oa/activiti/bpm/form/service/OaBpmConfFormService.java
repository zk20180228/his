package cn.honry.oa.activiti.bpm.form.service;

import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.service.BaseService;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.bpm.form.vo.FormParameterVo;

public interface OaBpmConfFormService extends BaseService<OaBpmConfForm> {

	/**
	 * 根据节点id获取表单配置
	 * @param node 节点id
	 * @return
	 */
	OaBpmConfForm getFormByNodeId(String node);
	
	/**
	 * 根据流程定义id获取启动表单
	 * @param processDefinitionId 流程定义id
	 * @return
	 */
	ConfFormVo getForm(String processDefinitionId);
	
	/**
	 * 根据code和租户id获取表单模板
	 * @param code 模板code
	 * @param tenantId 租户id
	 * @return
	 */
	ConfFormVo getForm(String code, String tenantId);
	
	/**
	 * 根据id获取keyValue记录及对应的属性
	 * @param id
	 * @return
	 */
	OaKVRecord getRecord(String id);
	
	/**
	 * 保存草稿
	 * @param userId 操作人/创建人
	 * @param deptCode 创建科室
	 * @param name 流程名称
	 * @param tenantId 租户id
	 * @param formParameter 表单参数
	 * @return
	 */
	String saveDraft(String userId,String deptCode,String name, String tenantId,FormParameterVo formParameter);
	/**
	 * 保存草稿(编辑)
	 * @param userId 操作人/创建人
	 * @param deptCode 创建科室
	 * @param name 流程名称
	 * @param tenantId 租户id
	 * @param formParameter 表单参数
	 * @return
	 */
	String saveDraft1(String userId,String deptCode,String name, String tenantId,FormParameterVo formParameter);
	
	/**
	 * 根据流程定义ID、表单code和节点code查询该节点绑定的表单属性集合
	 * @param code 表单code
	 * @param node 节点code
	 * @param processDefinitionId 流程定义ID
	 * @return
	 */
	String getFormProperties(String code,String node,String processDefinitionId);
	
	/**
	 * 获取record序列
	 * @return
	 */
	String getRecordSeq();
}
