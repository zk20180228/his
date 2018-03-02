package cn.honry.oa.activiti.bpm.form.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaTaskDefBase;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.oa.activiti.bpm.cmd.FindOaTaskFormCmd;
import cn.honry.oa.activiti.bpm.form.dao.OaBpmConfFormDao;
import cn.honry.oa.activiti.bpm.form.service.OaBpmConfFormService;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.bpm.form.vo.FormParameterVo;
import cn.honry.oa.activiti.bpm.process.vo.OaTaskForm;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.utils.DateUtils;

@Service("oaBpmConfFormService")
@Transactional
@SuppressWarnings({ "all" })
public class OaBpmConfFormServiceImpl implements OaBpmConfFormService {

	
	@Resource
	private ProcessEngine processEngine;//工作流引擎
	
	@Autowired
	@Qualifier(value = "oaBpmConfFormDao")
	private OaBpmConfFormDao oaBpmConfFormDao;
	
	public void setOaBpmConfFormDao(OaBpmConfFormDao oaBpmConfFormDao) {
		this.oaBpmConfFormDao = oaBpmConfFormDao;
	}

	@Autowired
	@Qualifier(value = "taskDefaultService")
	private TaskDefaultService taskDefaultService;

	public void setTaskDefaultService(TaskDefaultService taskDefaultService) {
		this.taskDefaultService = taskDefaultService;
	}
	
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	@Override
	public OaBpmConfForm get(String arg0) {
		return oaBpmConfFormDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaBpmConfForm arg0) {

		oaBpmConfFormDao.save(arg0);
	}

	/**
	 * 根据节点id获取表单配置
	 * @param node 节点id
	 * @return
	 */
	public OaBpmConfForm getFormByNodeId(String node){
		return oaBpmConfFormDao.getFormByNodeId(node);
	}
	
	/**
	 * 根据流程定义id获取启动表单
	 * @param processDefinitionId 流程定义id
	 * @return
	 */
	public ConfFormVo getForm(String processDefinitionId){
		//获取流程定义
		ProcessDefinition processDefinition = processEngine
                .getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
		//获取表单信息
		OaTaskForm firstTaskForm = processEngine.getManagementService().executeCommand(
				new FindOaTaskFormCmd(processDefinitionId));
		boolean exists = firstTaskForm.isExists();
		String activityId = firstTaskForm.getActivityId();
		if((exists) && (activityId != null)){
			 OaTaskDefBase taskDefBase = 
					 taskDefaultService.findUnique("from OaTaskDefBase where code=? and processDefinitionId=?"
							 ,new OaTaskDefBase(),activityId,processDefinitionId);
			 
			if(taskDefBase!=null){
				String formKey = taskDefBase.getFormKey();
				firstTaskForm.setFormKey(formKey);
				ConfFormVo vo = new ConfFormVo();
				vo.setCode(taskDefBase.getFormKey());
				vo.setActivityId(activityId);
				vo.setProcessDefinitionId(processDefinitionId);
				vo.setId(taskDefBase.getId());
				OaFormInfo info = taskDefaultService.findUnique("from OaFormInfo f where f.formCode=? and f.stop_flg=0 and f.del_flg=0"
						, new OaFormInfo(), formKey);
				if(info==null){
					return vo;
				}
				String properties = getFormProperties(formKey, activityId, processDefinitionId);
				vo.setProperties(properties);
				try {
					vo.setContent(ClobToString(info.getFormInfo()));
					return vo;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (!firstTaskForm.isExists()){
			return new ConfFormVo();
		}
		if (!firstTaskForm.isTaskForm()){//非任务表单
			return findStartEventForm(firstTaskForm);
		}
		
		return null;
	}
	
	/**
	 * 获取开始节点表单
	 * @param firstTaskForm
	 * @return
	 */
	public ConfFormVo findStartEventForm(OaTaskForm firstTaskForm){
		try {
			String processDefinitionId = firstTaskForm.getProcessDefinitionId();
			String activityId = firstTaskForm.getActivityId();
			
			ProcessDefinition processDefinition = processEngine
	                .getRepositoryService().createProcessDefinitionQuery()
	                .processDefinitionId(processDefinitionId).singleResult();
			
			ConfFormVo vo = new ConfFormVo();
			vo.setProcessDefinitionId(processDefinitionId);
			vo.setActivityId(activityId);
			vo.setCode(firstTaskForm.getFormKey());
			
			List<OaBpmConfForm> list = oaBpmConfFormDao.getFormList(processDefinitionId, activityId);
			if(list!=null && list.size()>0){
				OaBpmConfForm bpmConfForm = list.get(0);
				if(!Integer.valueOf(2).equals(bpmConfForm.getStatus())){//状态不等于2
					if (Integer.valueOf(1).equals(bpmConfForm.getType())){//类型为1
						vo.setRedirect(true);//需重定向
						vo.setUrl(bpmConfForm.getValue());//路径
					}else{
						vo.setCode(bpmConfForm.getValue());
					}
				}
			}
			
			OaFormInfo info = taskDefaultService.findUnique("from OaFormInfo f where f.formCode=? and f.stop_flg=0 and f.del_flg=0"
					, new OaFormInfo(), vo.getCode());
			if(info==null){
				return vo;
			}
			
			vo.setContent(ClobToString(info.getFormInfo()));
			return vo;
		} catch (Exception e) {
			return null;
		}
		
		
	}
	
	/**
	 * 根据流程定义ID、表单code和节点code查询该节点绑定的表单属性集合
	 * @param code 表单code
	 * @param node 节点code
	 * @param processDefinitionId 流程定义ID
	 * @return
	 */
	public String getFormProperties(String code,String node,String processDefinitionId){
		 
		return oaBpmConfFormDao.getFormProperties(code, node, processDefinitionId);
	}
	
	/**
	 * 根据code和租户id获取表单模板
	 * @param code 模板code
	 * @param tenantId 租户id
	 * @return
	 */
	public ConfFormVo getForm(String code, String tenantId){
		try {
			OaFormInfo info = taskDefaultService.findUnique("from OaFormInfo f where f.formCode=? and f.stop_flg=0 and f.del_flg=0"
					, new OaFormInfo(), code);
			if(info==null){
				return null;
			}
			ConfFormVo vo =new ConfFormVo();
			vo.setId(info.getId());
			vo.setCode(info.getFormCode());
			vo.setName(info.getFormName());
			//此处应判断是外部表单还是内部表单,如果是外部表单,存的是外部表单的URL地址(外部表单功能暂未实现)
			vo.setContent(ClobToString(info.getFormInfo()));
			return vo;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 根据id获取keyValue记录及对应的属性
	 * @param id
	 * @return
	 */
	public OaKVRecord getRecord(String id){
		OaKVRecord record = taskDefaultService.findUnique(
				"from OaKVRecord t where t.id=? and t.stop_flg=0 and t.del_flg=0 ", new OaKVRecord(), id);
		if(record!=null){
			List<OaKVProp> list = taskDefaultService.getList(
					"from OaKVProp t where t.recordId=? and t.stop_flg=0 and t.del_flg=0", new OaKVProp(), id);
			if(list!=null && list.size()>0){
				for (OaKVProp oaKVProp : list) {
					record.getProps().put(oaKVProp.getCode(), oaKVProp);
				}
			}
		}
		return record;
	}

	
	/**
	 * 保存草稿
	 * @param userId 操作人/创建人
	 * @param deptCode 创建科室
	 * @param name 流程名称
	 * @param tenantId 租户id
	 * @param formParameter 表单参数
	 * @return
	 */
	public String saveDraft(String userId,String deptCode,String name, String tenantId,FormParameterVo formParameter){
		String bpmProcessId = formParameter.getBpmProcessId();
		String businessKey = formParameter.getBusinessKey();
		if(StringUtils.isNotBlank(bpmProcessId)){
			OaKVRecord record = taskDefaultService.findUnique(
					"from OaKVRecord t where t.businessKey=? and t.stop_flg=0 and t.del_flg=0 ", new OaKVRecord(), businessKey);
			if(record==null){
				record = new OaKVRecord();//构建Recode记录
				String recordSeq =getRecordSeq();
				record.setCreateUser(userId);
				record.setCreateTime(new Date());
				record.setCreateDept(deptCode);
				if(StringUtils.isNotBlank(recordSeq)){
					record.setFlowCode(recordSeq);
				}
			}
	        record.setCategory(bpmProcessId);
	        record.setBusinessKey(businessKey);
	        record.setUserId(userId);
	        record.setTenantId(tenantId);
	        record.setStatus(1);//草稿状态
	        SysEmployee employee = employeeInInterService.getEmpByJobNo(userId);
	        if(employee.getDeptCode()==null){
	        	record.setName(employee.getName()+"-"+name);
	        }else{
	        	SysDepartment department =  deptInInterService.getDeptCode(employee.getDeptCode());
	        	record.setName(department.getDeptName()+"-"+employee.getName()+"-"+name);
	        }
	        oaBpmConfFormDao.save(record);
	        businessKey=record.getId();
	        record.setBusinessKey(businessKey);
	        oaBpmConfFormDao.save(record);
	        List<OaKVProp> list = taskDefaultService.getList(
					  "from OaKVProp where recordId=? and stop_flg=0 and del_flg=0 ", new OaKVProp(),businessKey);
			  Map<String,OaKVProp> map =new HashMap<>();
			  for (OaKVProp oaKVProp : list) {
				map.put(oaKVProp.getCode(), oaKVProp);
			}
			  
	        String except="humanTaskId,zkhonryState,account,nextAssignee,";
	        for (Entry<String, List<String>> entry : formParameter
	                .getMultiValueMap().entrySet()) {
	            String key = entry.getKey();
	            List<String> value = entry.getValue();
	            if(except.contains(key)){
	            	continue;
	            }

	            if ((value == null) || (value.isEmpty())) {
	                continue;
	            }

	            OaKVProp prop = map.get(key);
	            if(prop==null){
	            	prop =new OaKVProp();
	            }
	            prop.setCode(key);
	            prop.setType(0);
	            prop.setValue(value.get(0));
	            prop.setRecordId(record.getId());
	            oaBpmConfFormDao.save(prop);
	        }
	        
	        String id = record.getId();
        	record.setBusinessKey(id);
        	oaBpmConfFormDao.save(record);
	        return id;
		}
		return "";
	}
	
	/**
	 * 保存草稿
	 * @param userId 操作人/创建人
	 * @param deptCode 创建科室
	 * @param name 流程名称
	 * @param tenantId 租户id
	 * @param formParameter 表单参数
	 * @return
	 */
	public String saveDraft1(String userId,String deptCode,String name, String tenantId,FormParameterVo formParameter){
		String bpmProcessId = formParameter.getBpmProcessId();
		String businessKey = formParameter.getBusinessKey();
		if(StringUtils.isNotBlank(bpmProcessId)){
			OaKVRecord record = taskDefaultService.findUnique(
					"from OaKVRecord t where t.businessKey=? and t.stop_flg=0 and t.del_flg=0 ", new OaKVRecord(), businessKey);
			if(record==null){
				record = new OaKVRecord();//构建Recode记录
				String recordSeq =getRecordSeq();
				record.setCreateUser(userId);
				record.setCreateTime(new Date());
				record.setCreateDept(deptCode);
				if(StringUtils.isNotBlank(recordSeq)){
					record.setFlowCode(recordSeq);
				}
			}
	        record.setCategory(bpmProcessId);
	        record.setBusinessKey(businessKey);
	        record.setUserId(userId);
	        record.setTenantId(tenantId);
	        record.setStatus(1);//草稿状态
	        SysEmployee employee = employeeInInterService.getEmpByJobNo(userId);
	        if(employee.getDeptCode()==null){
	        	record.setName(employee.getName()+"-"+name);
	        }else{
	        	SysDepartment department =  deptInInterService.getDeptCode(employee.getDeptCode());
	        	record.setName(department.getDeptName()+"-"+employee.getName()+"-"+name);
	        }
	        oaBpmConfFormDao.save(record);
	        businessKey=record.getId();
	        if(record.getBusinessKey()==null||"".equals(record.getBusinessKey())){
	        	record.setBusinessKey(businessKey);
	        	oaBpmConfFormDao.save(record);
	        }
	        List<OaKVProp> list = taskDefaultService.getList(
					  "from OaKVProp where recordId=? and stop_flg=0 and del_flg=0 ", new OaKVProp(),businessKey);
			  Map<String,OaKVProp> map =new HashMap<>();
			  for (OaKVProp oaKVProp : list) {
				map.put(oaKVProp.getCode(), oaKVProp);
			}
			  
	        String except="humanTaskId,zkhonryState,account,nextAssignee,";
	        for (Entry<String, List<String>> entry : formParameter
	                .getMultiValueMap().entrySet()) {
	            String key = entry.getKey();
	            List<String> value = entry.getValue();
	            if(except.contains(key)){
	            	continue;
	            }

	            if ((value == null) || (value.isEmpty())) {
	                continue;
	            }

	            OaKVProp prop = map.get(key);
	            if(prop==null){
	            	prop =new OaKVProp();
	            }
	            prop.setCode(key);
	            prop.setType(0);
	            prop.setValue(value.get(0));
	            prop.setRecordId(record.getId());
	            oaBpmConfFormDao.save(prop);
	        }
	        
	        String id = record.getId();
        	record.setBusinessKey(id);
        	oaBpmConfFormDao.save(record);
	        return id;
		}
		return "";
	}
	
	/**
	 * 获取record序列
	 * @return
	 */
	public String getRecordSeq(){
		String recordSeq = oaBpmConfFormDao.getRecordSeq();
		if(StringUtils.isNotBlank(recordSeq)){
			String string = DateUtils.formatDateYMDHMS(new Date())+recordSeq;
			return string;
		}
		return "";
	}
	
	private String ClobToString(Clob clob) throws SQLException, IOException {   
        String reString = "";   
        java.io.Reader is = clob.getCharacterStream();// 得到流   
        BufferedReader br = new BufferedReader(is);   
        String s = br.readLine();   
        StringBuffer sb = new StringBuffer();   
        while (s != null) {  
            sb.append(s);   
            s = br.readLine();   
        }   
        reString = sb.toString();   
        return reString;   
    }
}
