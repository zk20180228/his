package cn.honry.oa.activiti.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.honry.base.bean.model.OaDelegateHistory;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.oa.activiti.delegateHistory.service.OaDelegateHistoryService;
import cn.honry.oa.activiti.delegateInfo.service.OaDelegateInfoService;

/**
 * 代理监听器
 * @author luyanshou
 *
 */
public class DelegateHumanTaskListener implements HumanTaskListener {

	@Autowired
	@Qualifier(value = "oaDelegateInfoService")
	private OaDelegateInfoService oaDelegateInfoService;
	
	public void setOaDelegateInfoService(OaDelegateInfoService oaDelegateInfoService) {
		this.oaDelegateInfoService = oaDelegateInfoService;
	}

	@Autowired
	@Qualifier(value = "oaDelegateHistoryService")
	private OaDelegateHistoryService oaDelegateHistoryService;
	
	public void setOaDelegateHistoryService(
			OaDelegateHistoryService oaDelegateHistoryService) {
		this.oaDelegateHistoryService = oaDelegateHistoryService;
	}
	
	@Override
	public void onCreate(OaTaskInfo taskInfo) throws Exception {
		String assignee = taskInfo.getAssignee();//负责人
		String processDefinitionId = taskInfo.getProcessDefinitionId();//流程定义id
		String code = taskInfo.getCode();
		String tenantId = taskInfo.getTenantId();
		String attorney = assignee;
		Map<String, String> map = oaDelegateInfoService.getAttorney(assignee, processDefinitionId,code,tenantId);
		Date date = new Date();
		List<OaDelegateHistory> list = new ArrayList<OaDelegateHistory>();
		for(Map.Entry<String, String> entry:map.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			attorney=attorney.replace(key, value);
			OaDelegateHistory history = new OaDelegateHistory();//保存记录
			history.setAssignee(key);
			history.setAttorney(value);
			history.setProcessDefinitionId(processDefinitionId);
			history.setTaskDefinitionKey(code);
			history.setTenantId(tenantId);
			history.setTaskInfoId(taskInfo.getId());
			history.setDelegateTime(date);
			history.setCreateTime(date);
			list.add(history);
		}
		if(list.size()>0){
			oaDelegateHistoryService.saveOrUpdateList(list);
		}
		if(!assignee.equals(attorney)){
			taskInfo.setAssignee(attorney);
			taskInfo.setOwner(assignee);
		}
		
	}

	@Override
	public void onComplete(OaTaskInfo taskInfo) throws Exception {

	}

}
