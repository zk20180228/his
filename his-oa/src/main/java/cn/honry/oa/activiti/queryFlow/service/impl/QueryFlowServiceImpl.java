package cn.honry.oa.activiti.queryFlow.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaReminders;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.mq.MessageSend;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;
import cn.honry.oa.activiti.queryFlow.dao.QueryFlowDao;
import cn.honry.oa.activiti.queryFlow.service.QueryFlowService;
import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.service.impl.HumanTaskServiceImpl;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("queryFlowService")
@Transactional
@SuppressWarnings({ "all" })
public class QueryFlowServiceImpl implements QueryFlowService{

	@Autowired
	@Qualifier(value = "queryFlowDao")
	private QueryFlowDao queryFlowDao;
	
	@Autowired
	@Qualifier(value = "humanTaskService" )
	private HumanTaskService humanTaskService;
	public void setHumanTaskService(HumanTaskService humanTaskService) {
		this.humanTaskService = humanTaskService;
	}
	public static final String path = Thread.currentThread().getContextClassLoader().getResource("jdbc.properties").getPath();
	@Autowired
	@Qualifier(value = "messageSend")
	private MessageSend messageSend;
	
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}

	@Override
	public OaTaskInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(OaTaskInfo arg0) {
	}

	@Override
	public OaTaskInfo querybyInstanceId(String instanceId) {
		return queryFlowDao.querybyInstanceId(instanceId);
	}

	@Override
	public List<OaTaskInfo> queryOaTaskInfo() {
		return queryFlowDao.queryOaTaskInfo();
	}

	@Override
	public List<OaKVRecord> queryOaKVRecord(String id,int page,int rows,String category,String startTime,String endTime) {
		return queryFlowDao.queryOaKVRecord(id, page, rows, category, startTime, endTime);
	}
	@Override
	public int queryOaKVRecordtotal(String id,String category,String startTime,String endTime) {
		return queryFlowDao.queryOaKVRecordtotal(id, category, startTime, endTime);
	}
	@Override
	public List<OaTaskInfo> queryOaTaskInfoVAct(String param,int page,int rows,String category,String startTime,String endTime) {
		return queryFlowDao.queryOaTaskInfoVAct(param, page, rows, category, startTime, endTime);
	}

	@Override
	public int queryOaTaskInfoVActTotal(String param, String category,
			String startTime, String endTime) {
		return queryFlowDao.queryOaTaskInfoVActTotal(param, category, startTime, endTime);
	}

	@Override
	public Integer queryListSize(String userAccount,String startTime,String endTime,String title) {
		return queryFlowDao.queryListSize(userAccount,startTime,endTime,title);
	}

	@Override
	public List<OaTaskInfo> tuijianqueryOaTaskInfoVAct(String param,int page,int rows,String category,String startTime,String endTime) {
		return queryFlowDao.tuijianqueryOaTaskInfoVAct(param, page, rows, category, startTime, endTime);
	}
	
	@Override
	public int tuijianqueryOaTaskInfoVActTotal(String param, String category,
			String startTime, String endTime) {
		return queryFlowDao.tuijianqueryOaTaskInfoVActToal(param, category, startTime, endTime);
	}

	@Override
	public Map<String, Object> savecuiban(String processInstanceId,String name,String attr2,String code,String assignee,String remindcontent,String taskInfoId) {
		Properties pps = new Properties();
		try {
			pps.load(new FileInputStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String reminderTime = pps.getProperty("reminder_time");
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<String> processDefinitionIds = queryFlowDao.queryProcessDefinitionIdByProcessInstanceId(processInstanceId);
		if(null!=processDefinitionIds&&processDefinitionIds.size()>0){
			ExtendVo extendVo = humanTaskService.getConfRule(processDefinitionIds.get(0),code);
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			if(StringUtils.isNotBlank(extendVo.getMessage())){
				if("true".equals(extendVo.getUrge())){
					if("true".equals(extendVo.getMessage())){
						//保存催办数据
						List<OaReminders> list = queryFlowDao.queryListpan(taskInfoId);
						if(list!=null&&list.size()>0){
							for (OaReminders oaReminders : list) {
								Date cTime = DateUtils.addMin(oaReminders.getRemindTime(), Integer.valueOf(reminderTime));
								Date now = new Date();
								if(DateUtils.compareDate(now, cTime)==-1){
									map1.put("resCode", "success");
									map1.put("resMsg", "还未到下次催办时间!");
									return map1;
								}
								oaReminders.setReminderNum(oaReminders.getReminderNum()+1);
								oaReminders.setReminder(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
								oaReminders.setRemindTime(new Date());
								oaReminders.setRemindcontent(remindcontent);
								queryFlowDao.save(oaReminders);
							}
						}else{
							OaReminders oaReminders = new OaReminders();
							oaReminders.setId(null);
							oaReminders.setProcedureId(processInstanceId);
							oaReminders.setProcedureName(attr2);
							oaReminders.setReminderNum(1);
							oaReminders.setReminder(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
							oaReminders.setReminderName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
							oaReminders.setRemindTime(new Date());
							if(assignee!=null&&!"".equals(assignee)){
								if(assignee.indexOf(",")==-1){
									SysEmployee employee = employeeInInterService.getEmpByJobNo(assignee);
									oaReminders.setReminderdName(employee.getName());
								}else{
									String reminderName = null;
									String [] assign = assignee.split(",");
									for (String ass : assign) {
										SysEmployee employee = employeeInInterService.getEmpByJobNo(ass);
										if(reminderName==null){
											reminderName=employee.getName();
										}else{
											reminderName+=","+employee.getName();
										}
									}
									oaReminders.setReminderdName(reminderName);
								}
							}
							
							oaReminders.setReminderd(assignee);
							oaReminders.setRemindNode(code);
							oaReminders.setRemindenodeName(name);
							oaReminders.setRemindreStatus(0);
							oaReminders.setType(0);
							oaReminders.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
							oaReminders.setCreateTime(new Date());
							oaReminders.setRemindcontent(remindcontent);
							oaReminders.setRemidetime(new Date());
							oaReminders.setTaskInfoId(taskInfoId);
							queryFlowDao.save(oaReminders);
						}
						
						//推送催办信息
						List<OaReminders> o = queryFlowDao.queryListpan(taskInfoId);
						String id = o.get(0).getId();
						if(!account.equals(assignee)){
							String[] ids = assignee.split(",");
							if(null!=ids&&ids.length>0){
								Map<String,Object> map = new LinkedHashMap<String, Object>();
								for (String jid : ids) {
									map.put("id", id);
									map.put("title",StringUtils.isNotBlank(attr2)?attr2.split("-")[2]:"");
									map.put("username",StringUtils.isNotBlank(attr2)?attr2.split("-")[0]+"--"+attr2.split("-")[1]:"" );
									map.put("createTime", new Date());
									map.put("jid", jid);
									map.put("msg_type", "msg_type_remind_message");
									try {
										messageSend.sendMessage(JSONUtils.toJson(map));
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
						map1.put("resCode", "success");
						map1.put("resMsg", "催办成功!");
					}else{
						
						//保存催办数据
						List<OaReminders> list = queryFlowDao.queryListpan(taskInfoId);
						if(list!=null&&list.size()>0){
							for (OaReminders oaReminders : list) {
								Date cTime = DateUtils.addMin(oaReminders.getRemindTime(), Integer.valueOf(reminderTime));
								Date now = new Date();
								if(DateUtils.compareDate(now, cTime)==-1){
									map1.put("resCode", "success");
									map1.put("resMsg", "还未到下次催办时间!");
									return map1;
								}
								oaReminders.setReminderNum(oaReminders.getReminderNum()+1);
								oaReminders.setReminder(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
								oaReminders.setRemindTime(new Date());
								oaReminders.setRemindcontent(remindcontent);
								queryFlowDao.save(oaReminders);
							}
						}else{
							OaReminders oaReminders = new OaReminders();
							oaReminders.setId(null);
							oaReminders.setProcedureId(processInstanceId);
							oaReminders.setProcedureName(attr2);
							oaReminders.setReminderNum(1);
							oaReminders.setReminder(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
							oaReminders.setReminderName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
							oaReminders.setRemindTime(new Date());
							if(assignee!=null&&!"".equals(assignee)){
								if(assignee.indexOf(",")==-1){
									SysEmployee employee = employeeInInterService.getEmpByJobNo(assignee);
									oaReminders.setReminderdName(employee.getName());
								}else{
									String reminderName = null;
									String [] assign = assignee.split(",");
									for (String ass : assign) {
										SysEmployee employee = employeeInInterService.getEmpByJobNo(ass);
										if(reminderName==null){
											reminderName=employee.getName();
										}else{
											reminderName+=","+employee.getName();
										}
									}
									oaReminders.setReminderdName(reminderName);
								}
							}
							
							oaReminders.setReminderd(assignee);
							oaReminders.setRemindNode(code);
							oaReminders.setRemindenodeName(name);
							oaReminders.setRemindreStatus(0);
							oaReminders.setType(0);
							oaReminders.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
							oaReminders.setCreateTime(new Date());
							oaReminders.setRemindcontent(remindcontent);
							oaReminders.setRemidetime(new Date());
							oaReminders.setTaskInfoId(taskInfoId);
							queryFlowDao.save(oaReminders);
						}
						map1.put("resCode", "success");
						map1.put("resMsg", "催办成功!");
					}
				}else{
					map1.put("resCode", "error");
					map1.put("resMsg", "该环节不能催办!");
				}
			}
		}
		return map1;
	}

	@Override
	public List<OaTaskInfo> getListForTask(String account, String tenantId) {
		return queryFlowDao.getListForTask(account, tenantId);
	}

	@Override
	public List<OaReminders> queryListcui(String userAccount) {
		return queryFlowDao.queryListcui(userAccount);
	}

	@Override
	public List<OaTaskInfo> querylishijili(String param) {
		return queryFlowDao.querylishijili(param);
	}

	@Override
	public List<OaTaskInfo> querylistyijie(String param,int page,int rows,String category,String startTime,String endTime) {
		List<OaTaskInfo> infos = queryFlowDao.querylistyijie(param, page, rows, category, startTime, endTime);
		for (OaTaskInfo oaTaskInfo : infos) {
			int num = queryFlowDao.queryRemindernum(oaTaskInfo.getProcessInstanceId());
			oaTaskInfo.setReminderNum(num);
		}
		return infos;
	}

	@Override
	public int querylistyijieTotal(String param, String category,
			String startTime, String endTime) {
		return queryFlowDao.querylistyijieTotal(param, category, startTime, endTime);
	}

	@Override
	public List<OaBpmCategory> quert() {
		return queryFlowDao.quert();
	}

	@Override
	public Map<String, Object> updatecuiban(String processInstanceId, String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OaReminders> list = queryFlowDao.queryListId(processInstanceId);
		if(list!=null&&list.size()>0){
			for (OaReminders oaReminders : list) {
				if(oaReminders.getRemindreStatus()==0){
					oaReminders.setRemindreStatus(1);
					queryFlowDao.save(oaReminders);
					map.put("resCode", "success");
					map.put("resMsg", "已读成功!");
				}else if(oaReminders.getRemindreStatus()==1){
					map.put("resCode", "error");
					map.put("resMsg", "已经是已读状态!");
				}
			}
		}else{
			map.put("resCode", "error");
			map.put("resMsg", "没有该条记录!");
		}
		return map;
	}
	
	@Override
	public Map<String, Object> updateHuifu(String processInstanceId, String code,String neiron) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OaReminders> list = queryFlowDao.queryListId(processInstanceId);
		if(list!=null&&list.size()>0){
			for (OaReminders oaReminders : list) {
				oaReminders.setRemindreContent(neiron);
				oaReminders.setRemidereTime(new Date());
				oaReminders.setRemindreStatus(1);
				queryFlowDao.save(oaReminders);
				map.put("resCode", "success");
				map.put("resMsg", "回复成功!");
			}
		}else{
			map.put("resCode", "error");
			map.put("resMsg", "没有该条记录!");
		}
		return map;
	}
	
	@Override
	public void deleteMyCuiBan(String rowsid) {
		try{
			if(StringUtils.isNoneBlank(rowsid)){
				String[] ids = rowsid.split(",");
				for (String id : ids) {
					queryFlowDao.deleteMyCuiBan(id);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	@Override
	public void deleteMyCuiBanByProcess(String processid) {
		try{
			if(StringUtils.isNoneBlank(processid)){
				String[] ids = processid.split(",");
				for (String id : ids) {
					queryFlowDao.deleteMyCuiBanByProcess(id);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<OaReminders> getMyCuiBanByProecssId(String processid) {
		return queryFlowDao.getMyCuiBanByProecssID(processid);
	}

	@Override
	public List<OaTaskInfo> getListForTask(String account, String tenantId, String startTime, String endTime, String title, int page, int rows) {
		return queryFlowDao.getListForTask(account, tenantId, startTime, endTime, title, page, rows);
	}
	
	@Override
	public int getNumberForTask(String account, String tenantId, String startTime, String endTime, String title) {
		return queryFlowDao.getNumberForTask(account, tenantId, startTime, endTime, title);
	}


@Override
	public List<OaTaskInfo> querylishijili(String startTime, String endTime, String title, int page, int rows) {
		return queryFlowDao.querylishijili(startTime, endTime, title, page, rows);
	}
	@Override
	public int querylishijiliNum(String startTime, String endTime, String title) {
		return queryFlowDao.querylishijiliNum(startTime, endTime, title);
	}

@Override
	public List<OaReminders> queryList(String userAccount, String startTime,
			String endTime, String param, int page, int rows) {
		return queryFlowDao.queryList(userAccount,startTime,endTime,param,page,rows);
	}

@Override
public List<OaReminders> queryList1(String userAccount, String startTime,
		String endTime, String param, int page, int rows) {
	return queryFlowDao.queryList1(userAccount,startTime,endTime,param,page,rows);
}


@Override
	public int queryNum(String startTime, String endTime, String title) {
		return queryFlowDao.queryNum(startTime, endTime, title);
	}

	@Override
	public void deleteMyGao(String rowsid) {
		try{
			if(StringUtils.isNoneBlank(rowsid)){
				String[] ids = rowsid.split(",");
				for (String id : ids) {
					queryFlowDao.deleteMyGao(id);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<TreeJson> getXinJianTree() {
		List<TreeJson> parTree = new ArrayList<TreeJson>();
		TreeJson parTreeJson = new TreeJson();
		parTreeJson.setId("1");
		parTreeJson.setState("open");
		parTreeJson.setText("流程导航");
		parTree.add(parTreeJson);
		Map<String,String> treejson1Map = new HashMap<String, String>();
		treejson1Map.put("pid", "1");
		TreeJson treeJson1 = new TreeJson();
		treeJson1.setId("2");
		treeJson1.setState("close");
		treeJson1.setText("分类");
		treeJson1.setAttributes(treejson1Map);
		TreeJson treeJson2 = new TreeJson();
		treeJson2.setId("root");
		treeJson2.setState("close");
		treeJson2.setText("科室");
		treeJson2.setAttributes(treejson1Map);
		parTree.add(treeJson1);
		parTree.add(treeJson2);
//		//C-门诊, I-住院, F-财务，L-后勤，PI-药库，T-医技(终端)，0-其它，D-机关(部门)，P-药房，N-护士站 ，S-科研,O-其他,OP-手术,U-自定义
//		String[] strArray = {"C-门诊","I-住院","F-财务","L-后勤","PI-药库","T-医技(终端)","0-其它","D-机关(部门)","P-药房","N-护士站","S-科研","O-其他","OP-手术","U-自定义"};
//		for (String str : strArray) {
//			String[] split = str.split("-");
//			TreeJson childtreeJson = new TreeJson();
//			childtreeJson.setId(split[0]);
//			childtreeJson.setText(split[1]);
//			childtreeJson.setState("close");
//			Map<String,String> treejsonMap = new HashMap<String, String>();
//			treejsonMap.put("pid", "2");
//			childtreeJson.setAttributes(treejsonMap);
//			parTree.add(childtreeJson);
//		}
		List<OaBpmCategory> category = queryFlowDao.getAllOaBpmCategory();
		for (OaBpmCategory cate : category) {
			TreeJson childtreeJson = new TreeJson();
			childtreeJson.setId(cate.getId());
			childtreeJson.setText(cate.getName());
			Map<String,String> treejsonMap = new HashMap<String, String>();
			treejsonMap.put("pid", "2");
			childtreeJson.setAttributes(treejsonMap);
			parTree.add(childtreeJson);
		}
		List<OaActivitiDept> dept = queryFlowDao.getAllOaActivitiDept();
		Map<String,String> deptIdAndCode = new HashMap<String, String>();
		for (OaActivitiDept oaActivitiDept : dept) {
			deptIdAndCode.put(oaActivitiDept.getId(), oaActivitiDept.getDeptCode());
		}
		for (OaActivitiDept oadept : dept) {
			TreeJson childtreeJson = new TreeJson();
			childtreeJson.setId(oadept.getDeptCode());
			childtreeJson.setText(oadept.getDeptName());
			Map<String,String> treejsonMap = new HashMap<String, String>();
			treejsonMap.put("pid", deptIdAndCode.containsKey(oadept.getParentCode())?deptIdAndCode.get(oadept.getParentCode()):oadept.getParentCode());
			childtreeJson.setAttributes(treejsonMap);
			parTree.add(childtreeJson);
		}
		return TreeJson.formatTree(parTree);
	}

	@Override
	public OaTaskInfo queryOaTaskInfo(String processInstanceId, String code) {
		return queryFlowDao.queryOaTaskInfo(processInstanceId, code);
	}
	
}
