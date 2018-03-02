 package cn.honry.outpatient.scheduleModel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.newInfo.dao.RegistrationDAO;
import cn.honry.outpatient.newInfo.vo.HospitalVo;
import cn.honry.outpatient.scheduleModel.dao.ScheduleModelDAO;
import cn.honry.outpatient.scheduleModel.service.ScheduleModelService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Service("scheduleModelService")
@Transactional
@SuppressWarnings({ "all" })
public class ScheduleModelServiceImpl implements ScheduleModelService{
	
	@Autowired
	@Qualifier(value = "scheduleModelDAO")
	private ScheduleModelDAO scheduleModelDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "ationDAO")
	private RegistrationDAO ationDAO;

	@Override
	public void removeUnused(String id) {
	}

	@Override
	public RegisterSchedulemodel get(String id) {
		return scheduleModelDAO.get(id);
	}

	@Override
	public void saveOrUpdate(RegisterSchedulemodel model) {
	}

	@Override
	public List<RegisterSchedulemodel> getPage(String page, String rows,RegisterSchedulemodel registerSchedulemodel) {
		return scheduleModelDAO.getPage(registerSchedulemodel, page, rows);
	}

	@Override
	public int getTotal(RegisterSchedulemodel registerSchedulemodel) {
		return scheduleModelDAO.getTotal(registerSchedulemodel);
	}

	@Override
	public void del(String id) {
		scheduleModelDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	}

	@Override
	public boolean saveOrUpdateModel(RegisterSchedulemodel model) {
		SysDepartment d = deptInInterDAO.get(model.getModelWorkdept());
		model.setModelWorkdept(d.getDeptCode());
		SysDepartment dd = deptInInterDAO.get(model.getDepartment());
		model.setDepartment(dd.getDeptCode());
		if(StringUtils.isBlank(model.getId())){
			boolean isNotSave = scheduleModelDAO.findModelByWeekAndDoctor(null,model.getDepartment(),model.getModelWeek(),model.getModelDoctor(),model.getModelMidday());
			if(isNotSave){
				return false;
			}
			model.setId(null);
			if(StringUtils.isNotEmpty(model.getModelRemark())){
				String remark = "";
				remark = model.getModelRemark().replaceAll("\\r\\n", "</br>");
				remark = remark.replaceAll("\\n", "</br>");
				model.setModelRemark(remark);
			}
			model.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			model.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			model.setCreateTime(new Date());
			if(StringUtils.isNotBlank(model.getModelWorkdept())){
				HospitalVo hospitalVo = ationDAO.queryHospitalInfo(model.getModelWorkdept());
				//设置所属医院
				model.setHospitalId(hospitalVo.getHospital_id());
				//设置所属院区
				model.setAreaCode(hospitalVo.getDept_area_code());
			}
			scheduleModelDAO.save(model);
			OperationUtils.getInstance().conserve(null,"挂号排班模板","INSERT INTO","T_REGISTER_SCHEDULEMODEL",OperationUtils.LOGACTIONINSERT);
			return true;
		}else{
			boolean isNotSave = scheduleModelDAO.findModelByWeekAndDoctor(model.getId(),model.getDepartment(),model.getModelWeek(),model.getModelDoctor(),model.getModelMidday());
			List<RegisterSchedulemodel> list = scheduleModelDAO.findOldScheduleModel(model.getId());
			model.setCreateTime(list.get(0).getCreateTime());
			if(isNotSave){
				return false;
			}
			if(model.getModelClass()==2){
				model.setModelLimit(null);
				model.setModelPrelimit(null);
				model.setModelPhonelimit(null);
				model.setModelNetlimit(null);
				model.setModelSpeciallimit(null);
				model.setModelAppflag(0);
				model.setModelReggrade(null);
			}
			if(StringUtils.isNotEmpty(model.getModelRemark())){
				String remark = "";
				remark = model.getModelRemark().replaceAll("\\r\\n", "</br>");
				remark = remark.replaceAll("\\n", "</br>");
				model.setModelRemark(remark);
			}
			model.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			model.setUpdateTime(new Date());
			if(StringUtils.isNotBlank(model.getModelWorkdept())){
				HospitalVo hospitalVo = ationDAO.queryHospitalInfo(model.getModelWorkdept());
				//设置所属医院
				model.setHospitalId(hospitalVo.getHospital_id());
				//设置所属院区
				model.setAreaCode(hospitalVo.getDept_area_code());
			}
			scheduleModelDAO.save(model);
			OperationUtils.getInstance().conserve(model.getId(),"挂号排班模板","UPDATE","T_REGISTER_SCHEDULEMODEL",OperationUtils.LOGACTIONUPDATE);
			return true;
		}
	}

	/**  
	 *  
	 * @Description：  查询某科室下某星期的全部排班模板
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterSchedulemodel> getSchedulemodel(String deptId,int week,String search) {
		SysDepartment dept = deptInInterDAO.get(deptId);
		List<RegisterSchedulemodel> modelList = scheduleModelDAO.getScheduleByDeptAndWeek(dept.getDeptCode(),week,search);
		if(modelList!=null&&modelList.size()>0){
			return modelList;
		}
		return new ArrayList<RegisterSchedulemodel>();
	}

	/**  
	 *  
	 * @Description：  查询挂号部门树
	 * @Author：aizhonghua
	 * @CreateDate：2015年12月23日 上午9:17:50  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015年12月23日 上午9:17:50  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> treeDeptSchedule() {
		List<SysDepartment> sysDepartmentList = new ArrayList<SysDepartment>();
		sysDepartmentList = deptInInterDAO.treeDeptSchedule();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		String sType="";
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("科室信息");
		topTreeJson.setIconCls("icon-branch");
		Map<String,String> tAttMap = new HashMap<String, String>();
		topTreeJson.setAttributes(tAttMap);
		treeJsonList.add(topTreeJson);
		if(sysDepartmentList!=null && sysDepartmentList.size()>0){
			String curType="";
			for(SysDepartment sysDepartment : sysDepartmentList){
				TreeJson treeJson = new TreeJson();
				if(sysDepartment.getDeptType()!=null && !sysDepartment.getDeptType().equals(curType)){
					TreeJson typeTreeJson = new TreeJson();
					curType=sysDepartment.getDeptType();
					typeTreeJson.setId("type_"+curType);
					if("C".equals(curType)){
						typeTreeJson.setText("门诊");
						typeTreeJson.setIconCls("icon-vcard");
						typeTreeJson.setState("closed");
					}else if("I".equals(curType)){
						typeTreeJson.setText("住院");
						typeTreeJson.setIconCls("icon-application_side_list");
						typeTreeJson.setState("closed");
					}else if("F".equals(curType)){
						typeTreeJson.setText("财务");
						typeTreeJson.setIconCls("icon-coins");
						typeTreeJson.setState("closed");
					}else if("L".equals(curType)){
						typeTreeJson.setText("后勤");
						typeTreeJson.setIconCls("icon-report");
						typeTreeJson.setState("closed");
					}else if("PI".equals(curType)){
						typeTreeJson.setText("药库");
						typeTreeJson.setIconCls("icon-bullet_home");
						typeTreeJson.setState("closed");
					}else if("T".equals(curType)){
						typeTreeJson.setText("医技");
						typeTreeJson.setIconCls("icon-application_side_list");
						typeTreeJson.setState("closed");						
					}else if("D".equals(curType)){
						typeTreeJson.setText("行政");
						typeTreeJson.setIconCls("icon-client");
						typeTreeJson.setState("closed");
					}else if("P".equals(curType)){
						typeTreeJson.setText("药房");
						typeTreeJson.setIconCls("icon-brick");
						typeTreeJson.setState("closed");
					}else if("N".equals(curType)){
						typeTreeJson.setText("护士站");
						typeTreeJson.setIconCls("icon-bullet_home");
						typeTreeJson.setState("closed");
					}else if("S".equals(curType)){
						typeTreeJson.setText("科研");
						typeTreeJson.setIconCls("icon-bullet_home");
						typeTreeJson.setState("closed");
					}else if("OP".equals(curType)){
						typeTreeJson.setText("手术");
						typeTreeJson.setIconCls("icon-bullet_home");
						typeTreeJson.setState("closed");
					}else if("U".equals(curType)){
						typeTreeJson.setText("科室");
						typeTreeJson.setIconCls("icon-bullet_home");
						typeTreeJson.setState("closed");
					}else if("O".equals(curType)){
						typeTreeJson.setText("其他");
						typeTreeJson.setIconCls("icon-bullet_home");
						typeTreeJson.setState("closed");
					}
					if("".equals(sType)){
						sType=curType;
					}else{
						sType+=","+curType;
					}
					Map<String,String> attributes = new HashMap<String, String>();
					attributes.put("pid","1");
					attributes.put("hasson","1");
					attributes.put("deptCode","code_"+curType);
					attributes.put("code", sysDepartment.getDeptCode());
					typeTreeJson.setAttributes(attributes);
					treeJsonList.add(typeTreeJson);							
				}
				
				treeJson.setId(sysDepartment.getId());
				treeJson.setText(sysDepartment.getDeptName());

				if(sysDepartment.getDeptHasson()==0){
					treeJson.setIconCls("icon-user_brown");
				}
				
				Map<String,String> attributes = new HashMap<String, String>();
				if(sysDepartment.getDeptLevel()==1){
					attributes.put("pid","type_"+curType);
				}else{
					attributes.put("pid",sysDepartment.getDeptParent());
				}
				attributes.put("hasson","2");
				attributes.put("deptCode","2");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}

		}

		if((","+sType+",").indexOf(",C,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_C");
			treeJson.setText("门诊");
			treeJson.setIconCls("icon-vcard");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_C");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",I,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_I");
			treeJson.setText("住院");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_I");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",F,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_F");
			treeJson.setText("财务");
			treeJson.setIconCls("icon-coins");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_F");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",L,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_L");
			treeJson.setText("后勤");
			treeJson.setIconCls("icon-report");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_L");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",PI,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_PI");
			treeJson.setText("药库");
			treeJson.setIconCls("icon-bullet_home");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_PI");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",T,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_T");
			treeJson.setText("医技");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_T");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",D,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_D");
			treeJson.setText("行政");
			treeJson.setIconCls("icon-client");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_D");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",P,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_P");
			treeJson.setText("药房");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_P");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",N,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_N");
			treeJson.setText("护士站");
			treeJson.setIconCls("icon-application_cascade");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_N");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",S,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_S");
			treeJson.setText("科研");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_S");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",OP,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_OP");
			treeJson.setText("手术");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_OP");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",U,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_U");
			treeJson.setText("科室");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_U");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",O,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_O");
			treeJson.setText("其他");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_O");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		return TreeJson.formatTree(treeJsonList);
	}

	/**  
	 *  
	 * @Description：  查询科室下的员工
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> getEmployeeByDeptId(RegisterSchedulemodel model) {
		return scheduleModelDAO.getEmployeeByDeptId(model);
	}

	/**
	 * @Description 挂号排版模板导入
	 * @Author huangbiao
	 * @CreateDate 2016年3月26日
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk
	 * @version 1.0
	 */
	public Map<String,Object> schedulModelAddTemp(String ids,String deptId,int nowWeek,int week) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		int retSuc = 0;
		int retFai = 0;
		SysDepartment dept = deptInInterDAO.get(deptId);
		List<RegisterSchedulemodel> modelList = scheduleModelDAO.getScheduleByidsAndDeptIdAndRq(ids,dept.getDeptCode(),week);
		if(modelList!=null && modelList.size()>0){
			for(RegisterSchedulemodel model : modelList){
				boolean isNotSave = scheduleModelDAO.findModelByWeekAndDoctor(model.getId(), model.getDepartment(), nowWeek, model.getModelDoctor(), model.getModelMidday());
				if(!isNotSave){
					model.setId(null);
					model.setModelWeek(nowWeek);
					scheduleModelDAO.clear();
					scheduleModelDAO.save(model);
					retSuc+=1;
				}else{
					retFai+=1;
				}
			}
			
			if(modelList.size()==retSuc){//如果导入条数等于总数说明全部导入
				retMap.put("resMsg", "allSucc");
				retMap.put("resSuc", retSuc);
			}else if(modelList.size()==retFai){//如果未导入条数等于总数说明没有导入
				retMap.put("resMsg", "notImp");
			}else{//既有导入又有未导入
				retMap.put("resMsg", "partSucc");
				retMap.put("resSuc", retSuc);
				retMap.put("retFai", retFai);
			}
		}else{//没有可导入数据
			retMap.put("resMsg", "notExist");
		}
		return retMap;
	}
	
}
