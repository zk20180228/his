package cn.honry.oa.activiti.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.activiti.task.service.TaskInternalService;


public class AssigneeAliasHumanTaskListener implements HumanTaskListener {

	@Autowired
	@Qualifier(value = "taskInternalService")
	private TaskInternalService taskInternalService;
	
	public void setTaskInternalService(TaskInternalService taskInternalService) {
		this.taskInternalService = taskInternalService;
	}
	
	@Autowired
	@Qualifier(value = "taskDefaultService")
	private TaskDefaultService taskDefaultService;
	
	public void setTaskDefaultService(TaskDefaultService taskDefaultService) {
		this.taskDefaultService = taskDefaultService;
	}
	
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	@Override
	public void onCreate(OaTaskInfo taskInfo) throws Exception {

		String assignee = taskInfo.getAssignee();//获取负责人(这里的值可能是一个动态表达式)
		String processStarter = taskInfo.getProcessStarter();//流程发起人
		String createUser=taskInfo.getCreateUser();
		String cu=(StringUtils.isBlank(createUser)?processStarter:createUser);
		if(StringUtils.isBlank(assignee)){
			String attr1 = taskInfo.getAttr1();
			if(StringUtils.isNotBlank(attr1)){
				String assigness = getAssigness(attr1, cu,taskInfo);
				if(StringUtils.isBlank(assigness)){//负责人列表为空时,将负责人设置为系统管理员
					String admin = parameterInnerService.getparameter("adminOA");
					if(StringUtils.isBlank(admin)){
						admin="000000";
					}
					taskInfo.setAssignee(admin);
				}else{
					taskInfo.setAssignee(assigness);
				}
			}else{
				taskInfo.setAssignee(processStarter);
			}
			return ;
		}
		
		if("starter".equals(assignee)){
			taskInfo.setAssignee(taskInfo.getCreateUser());
			return;
		}
		
		if(assignee.startsWith("${")){// 以'${'开头说明存的是一个表达式,需要解析
			String expression =(String)taskInternalService.executeExpression(taskInfo.getTaskId(), assignee);//解析表达式,获取任务负责人
			taskInfo.setAssignee(expression);//设置负责人
			return ;
		}
		//解析自定义表达式
		String string = getAssigness(assignee, processStarter,taskInfo);
		if(StringUtils.isNotBlank(string)){
			taskInfo.setAssignee(string);
		}
		
		
	}

	@Override
	public void onComplete(OaTaskInfo taskInfo) throws Exception {

	}

	public String getAssigness(String assignee,String processStarter,OaTaskInfo taskInfo){
		//解析自定义的表达式
		String hql="from EmployeeExtend where stop_flg=0 and del_flg=0 and employeeJobNo=? ";
		EmployeeExtend emp = taskDefaultService.findUnique(hql, new EmployeeExtend(),processStarter);
		String expression = expression(assignee, emp,taskInfo);
		if(StringUtils.isBlank(expression)){
			return null;
		}
		List<String> list = taskDefaultService.getList(expression,"");
		String ass="";
		if(list!=null && list.size()>0){
			StringBuffer jobNo=new StringBuffer();
			for (String no : list) {
				if(StringUtils.isNotBlank(no)){
					jobNo.append(no).append(",");
				}
			}
			StringBuffer sbf = jobNo.deleteCharAt(jobNo.length()-1);
			ass=sbf.toString();
		}
		return ass;
	}
	
	/**
	 * 获取执行hql
	 * @param assignee 自定义表达式
	 * @param emp 流程发起人信息
	 * @return
	 */
	public String expression(String assignee,EmployeeExtend emp,OaTaskInfo taskInfo){
		StringBuffer hql= new StringBuffer("select employeeJobNo from EmployeeExtend where stop_flg=0 and del_flg=0 and ");
		hql.append(" employeeJobNo is not null and ");
		int i = assignee.indexOf("]");
		int k = assignee.indexOf("userHand");
		Map<String,OaTaskInfo> map = new HashMap<>();
		List<OaTaskInfo> infoList = new ArrayList<>();
		if(k>0){//与之前节点处理人有关联,需获取之前各个节点的处理人
			String processInstanceId = taskInfo.getProcessInstanceId();
			StringBuffer hql1=new StringBuffer()
			.append(" from OaTaskInfo t where t.status='complete' and t.processInstanceId=? and t.stop_flg=0 and t.del_flg=0 ")
			.append("order by t.createTime,to_number(t.taskId) ");
			List<OaTaskInfo> list = taskDefaultService.getList(hql1.toString(), new OaTaskInfo(), processInstanceId);
			if(list==null || list.size()==0){
				return "";
			}
			for (OaTaskInfo oaTaskInfo : list) {//list中有可能存在重复的节点(因为有驳回),对于重复的节点取第一次出现的
				String code = oaTaskInfo.getCode();
				OaTaskInfo info = map.get(code);
				if(info==null){
					map.put(code, oaTaskInfo);
					infoList.add(oaTaskInfo);
				}
			}
		}
		if(i<0){//不包含中括号']'
			if(assignee.startsWith("(")){
				String substring = assignee.substring(1, assignee.length()-1);//去掉小括号
				if(k>0){
					String[] split = substring.split(":");//substring的格式为: userHand:2
					Integer index = Integer.valueOf(split[1])-1;//userHand从1开始
					if(index>=infoList.size()){//会导致数组下标越界
						return "";
					}
					OaTaskInfo info = infoList.get(index);
					hql.append("employeeJobNo = '").append(info.getLastModifier()).append("'");
					return hql.toString();
				}else{
					hql.append("employeeJobNo in ('").append(substring.replace("-", "','")).append("')");
					return hql.toString();
				}
			}
			return "";
		}
		String express = assignee.substring(1,i);//截掉中括号[]
		String[] split =null;
		boolean flag= true;
		if(express.indexOf("&&")>0){//包含
			split = express.split("&&");
		}else{
			flag=false;
			split=express.split("\\|");
		}
		if(split.length>0){
			if(!flag){
				hql.append("(");
			}
			for (String str : split) {
				if(StringUtils.isBlank(str)){
					continue;
				}
				String[] spl = str.trim().split(":");
				String sec = spl[0].trim();
				boolean f= false;
				if("mana".equals(sec)||"divi".equals(sec)||"duty".equals(sec)){
					f=true;
				}
				if(spl.length>1){
					sec=spl[1].trim();
				}
				
				if(sec.indexOf("{")>=0 && emp!=null){
					if(sec.indexOf("{generalbranchCode}")>=0){
						String code = emp.getGeneralbranchCode();
						if(StringUtils.isBlank(code)){
							return "";
						}
						sec=sec.replace("{generalbranchCode}", code);//总支编号
					}
					if(sec.indexOf("{divisionCode}")>=0){
						String divisionCode = emp.getDivisionCode();
						if(StringUtils.isBlank(divisionCode)){
							return "";
						}
						sec=sec.replace("{divisionCode}", emp.getDivisionCode());//学部编号
					}
					if(sec.indexOf("{dept}")>=0){
						String departmentCode = emp.getDepartmentCode();
						if(StringUtils.isBlank(departmentCode)){
							return "";
						}
						sec=sec.replace("{dept}",emp.getDepartmentCode());//同科室
					}
					if(sec.indexOf("{departmentModlue")>=0){//表单中填写的科室
						String code = sec.substring(1, sec.length()-1);//科室字段编码
						String businessKey = taskInfo.getBusinessKey();
						String vHql="select value from OaKVProp t where t.recordId=? and code=? and t.stop_flg=0 and t.del_flg=0 ";
						String value = taskDefaultService.findUnique(vHql, "", businessKey,code);//表单中科室字段填写的内容
						if(StringUtils.isNotBlank(value)){// value的格式为:{"8523":"鼻科门诊","8911":"CT门诊"}
							value=value.substring(1, value.indexOf("}"));//去掉大括号
							String[] splts = value.split(",");//逗号分割
							StringBuffer dept=new StringBuffer("");
							for (String val : splts) {
								if(dept.length()>0){
									dept.append(",");
								}
								dept.append(val.split(":")[0].replace("\"", "")) ;
							}
							sec=sec.replace("{"+code+"}", dept.toString());
						}
					}
				}
				if(str.indexOf("userHand")>=0){//候选人组中包含"userHand"
					Integer index = Integer.valueOf(sec)-1;
					if(index>=infoList.size()){//会导致数组下标越界
						return "";
					}
					OaTaskInfo info = infoList.get(index);
					String assignees = info.getAssignee();//负责人
					//查询与负责人同一部门并且不包含该负责人的所有人员
					StringBuffer sbf = new StringBuffer("select employeeJobNo from EmployeeExtend e where stop_flg=0 and del_flg=0 ");
					if(assignees.indexOf(",")>0){
						sbf.append("and e.employeeJobNo not in ('").append(assignees.replaceAll(",", "','")).append("')");
						sbf.append(" and departmentCode in ( select departmentCode from EmployeeExtend t where stop_flg=0 and del_flg=0 ")
						.append(" and t.employeeJobNo in ('").append(assignees.replaceAll(",", "','")).append("'))");
					}else{
						sbf.append("and e.employeeJobNo <> '").append(assignees).append("'");
						sbf.append(" and departmentCode = (select departmentCode from EmployeeExtend t where stop_flg=0 and del_flg=0 ")
						.append(" and t.employeeJobNo= '").append(assignees).append("')");
					}
					List<String> list = taskDefaultService.getList(sbf.toString(), "");
					if(list==null || list.size()==0){
						return "";
					}
					sec="";//将sec替换成查询出来的list集合(即负责人列表)
					for (String ass : list) {
						if(sec.length()>0){
							sec+="-";
						}
						sec+=ass;
					}
				}
				hql.append(spl[0]);
				if(sec.indexOf("-")>0){//包含逗号","即:有多个值
					hql.append(" in ('").append(sec.replaceAll("-", "','")).append("')");
				}else{//不包含逗号","即只有一个值
					hql.append(" = '").append(sec).append("'");
				}
				if(f){
					hql.append(")");
				}
				hql.append(" and ");
			}
			int j = hql.lastIndexOf("and");
			hql.delete(j, j+3);
			if(!flag){
				hql.append(")");
			}
		}
		String res = hql.toString();
		return res.replaceAll("dept", "departmentCode").replaceAll("post", "dutiesCode").replaceAll("userHand", "employeeJobNo")
				.replaceAll("divi", "employeeJobNo in ( select distinct account from EmployeeDividept where type=0 and deptCode ")//0:院领导
				.replaceAll("duty", "employeeJobNo in ( select distinct account from EmployeeDividept where type=1 and deptCode ")//1:部门领导
				.replaceAll("mana", "employeeJobNo in ( select distinct empCode from OaGroup where groupCode ");
	}
}
