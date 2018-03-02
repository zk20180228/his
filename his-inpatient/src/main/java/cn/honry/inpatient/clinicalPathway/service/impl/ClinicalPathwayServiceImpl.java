package cn.honry.inpatient.clinicalPathway.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.CpExecute;
import cn.honry.base.bean.model.CpExecuteDetail;
import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.base.bean.model.CpVcontrol;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InpAccess;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysRole;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inpatient.clinicalPathway.dao.ClinicalPathwayDao;
import cn.honry.inpatient.clinicalPathway.service.ClinicalPathwayService;
import cn.honry.inpatient.clinicalPathwayModel.service.ClinicalPathwayModelService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Service("clinicalPathwayService")
@Transactional
@SuppressWarnings({"all"})
public class ClinicalPathwayServiceImpl implements ClinicalPathwayService {

	@Autowired
	@Qualifier("clinicalPathwayDao")
	private ClinicalPathwayDao clinicalPathwayDao;

	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService  innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	@Autowired
	@Qualifier(value = "clinicalPathwayModelService")
	public ClinicalPathwayModelService clinicalPathwayModelService;
	public void setClinicalPathwayModelService(
			ClinicalPathwayModelService clinicalPathwayModelService) {
		this.clinicalPathwayModelService = clinicalPathwayModelService;
	}

	@Override
	public void addPathway(CpWay cpWay) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String pyWb = clinicalPathwayDao.getSpellCode(cpWay.getCpName());
		if(pyWb!=null&&pyWb.contains("$")){
			String pw[] = pyWb.split("\\$");
			cpWay.setInputCode(pw[0]);
			cpWay.setInputCodeWb(pw[1]);
		}
		if(StringUtils.isNotBlank(cpWay.getId())){//ID不为空，修改
			
		}else{//ID为空，新增
			cpWay.setId(null);
			cpWay.setCreateUser(longinUserAccountCode);
			cpWay.setCreateTime(DateUtils.getCurrentTime());
			clinicalPathwayDao.save(cpWay);
		}
	}

	@Override
	public List<TreeJson> queryTree() {
		List<CpVcontrol> cpVcontrolList = clinicalPathwayDao.queryTree();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		String sType="";
		
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("临床路径信息");
		Map<String,String> tAttMap = new HashMap<String, String>();
		topTreeJson.setAttributes(tAttMap);
		treeJsonList.add(topTreeJson);
		
		List<CpWay> cpWayList = clinicalPathwayDao.queryDisease();
		int dictionarylen = cpWayList.size();
		
		if(cpVcontrolList!=null&&cpVcontrolList.size()>0){
			String curType="";
			for(CpVcontrol md : cpVcontrolList){
				TreeJson treeJson = new TreeJson();
				if(StringUtils.isNotBlank(md.getCpId())&&!curType.equals(md.getCpId())){
					TreeJson typeTreeJson = new TreeJson();
					curType = md.getCpId();
					typeTreeJson.setId("type_"+curType);
					for(CpWay bd : cpWayList){
						if(bd.getId().equals(curType)){
							typeTreeJson.setText(bd.getCpName());
							typeTreeJson.setState("closed");
						}
					}
					if("".equals(sType)){
						sType=curType;
					}else{
						sType+=","+curType;
					}
					Map<String,String> attributes = new HashMap<String, String>();
					attributes.put("pid","1");
					attributes.put("hasson","1");
					attributes.put("cpId","code_"+curType);
					typeTreeJson.setAttributes(attributes);
					treeJsonList.add(typeTreeJson);	
				}
				treeJson.setId(md.getId());
				treeJson.setText(md.getCpName()+md.getVersionNo());//临床路径名称+临床路径版本号
				
				Map<String,String> attributes = new HashMap<String, String>();
				
				attributes.put("pid", "type_"+curType);
				
				attributes.put("id", md.getId());
				attributes.put("cpId", md.getCpId());
				attributes.put("versionNo", md.getVersionNo());
				attributes.put("standCode", md.getStandCode());
				attributes.put("standVersionNo", md.getStandVersionNo());
				attributes.put("versionMemo", md.getVersionMemo());
//				attributes.put("versionDate", DateUtils.formatDateY_M_D_H_M_S(md.getVersionDate()));
				attributes.put("applyScope", md.getApplyScope());
				attributes.put("approvalUser", md.getApprovalUser());
//				attributes.put("approvalDate", DateUtils.formatDateY_M_D_H_M_S(md.getApprovalDate()));
				attributes.put("approvalFlag", md.getApprovalFlag());
				attributes.put("standardDate", md.getStandardDate());
				attributes.put("dateUnit", md.getDateUnit());
				attributes.put("hospitalId", md.getHospitalId());
				attributes.put("areaCode", md.getAreaCode());
				
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		
		for(CpWay bd : cpWayList){
			if((","+sType+",").indexOf(","+bd.getId()+",")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_"+bd.getId());
				treeJson.setText(bd.getCpName());
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("cpId","code_"+bd.getId());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		
		return TreeJson.formatTree(treeJsonList);
	}

	@Override
	public void addVersion(CpVcontrol cpVcontrol) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isNotBlank(cpVcontrol.getId())){//有ID，修改
			
		}else{//没有ID，新增
			cpVcontrol.setId(null);
			cpVcontrol.setCreateUser(longinUserAccountCode);
			cpVcontrol.setCreateTime(DateUtils.getCurrentTime());
			clinicalPathwayDao.save(cpVcontrol);
		}
	}

	@Override
	public List<InoroutStandard> queryStand() {
		return clinicalPathwayDao.queryStand();
	}

	@Override
	public List<CpWay> queryCpWay() {
		return  clinicalPathwayDao.queryDisease();
	}

	@Override
	public List<InoroutStandard> queryVersion(String standCode) {
		return clinicalPathwayDao.queryVersion(standCode);
	}

	@Override
	public CpWay findCpWayById(String id) {
		return clinicalPathwayDao.findCpWayById(id);
	}

	@Override
	public boolean checkIsOnly(CpVcontrol cpVcontrol) {
		return clinicalPathwayDao.checkIsOnly(cpVcontrol);
	}

	@Override
	public CpVcontrol findCpVcontrolById(String id) {
		return clinicalPathwayDao.findCpVcontrolById(id);
	}

	@Override
	public void addTimeToClinical(CpwayPlan cpwayPlan) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		cpwayPlan.setCreateUser(longinUserAccountCode);
		cpwayPlan.setCreateTime(DateUtils.getCurrentTime());
		clinicalPathwayDao.save(cpwayPlan);
	}
	
	@Override
	public List<TreeJson> treeStage(String versionId) {
		//根据版本ID，查询计划表
		List<CpwayPlan> cpwayPlanList =  clinicalPathwayDao.queryTimeTree(versionId);
		//二级节点，写死的组套树
		List<TreeJson> treeStackList = new ArrayList<TreeJson>();
		List<BusinessDictionary> dictionary = innerCodeService.getDictionary("cpProperty");
		for(int i = 0 ; i<3; i++){
			Map<String,String> attributes = new HashMap<String, String>();
			TreeJson treeTreeStack = new TreeJson();
			treeTreeStack.setId(dictionary.get(i).getEncode());
			treeTreeStack.setText("组套"+(i+1)+"_"+dictionary.get(i).getName());
			attributes.put("id", dictionary.get(i).getEncode());
			treeTreeStack.setAttributes(attributes);
			treeStackList.add(treeTreeStack);
		}
		//设置一级节点
		List<TreeJson> treeTimeList = new ArrayList<TreeJson>();
		String stageIdString = "";
		for(CpwayPlan cp : cpwayPlanList){
			if(stageIdString.contains(cp.getStageId())){
				continue;
			}else{
				stageIdString += cp.getStageId() + ",";
				Map<String,String> attributes = new HashMap<String, String>();
				TreeJson treeJsonTime = new TreeJson();
				treeJsonTime.setId(cp.getId());
				treeJsonTime.setText("第"+cp.getStageId()+"天");
				attributes.put("cpId", cp.getCpId());
				attributes.put("stageId", cp.getStageId());
				treeJsonTime.setAttributes(attributes);
				treeJsonTime.setChildren(treeStackList);
				treeTimeList.add(treeJsonTime);
			}
		}
		//设置根
		List<TreeJson> returnlist = new ArrayList<TreeJson>();
		TreeJson pat = new TreeJson();
		pat.setId("root");
		pat.setText("时间段");
		pat.setChildren(treeTimeList);
		
		
		returnlist.add(pat);
		return returnlist;
	}

	@Override
	public void saveModelToPlan(String id, String modelId, String modelNature, String cpId, String stageId) {
		clinicalPathwayDao.delOldPlan(modelNature,cpId,stageId);
		//根据id，查到对应的临床路径计划
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		CpwayPlan cpwayPlanOld = clinicalPathwayDao.findCpWayPlanById(id);
		List<String> modelIdList = Arrays.asList(modelId.split(","));
		for(String modelCode : modelIdList){
			CpwayPlan cpwayPlanNew = new CpwayPlan();
			cpwayPlanNew.setId(null);
//			cpwayPlanNew.setStageId(stageId);
			cpwayPlanNew.setCpId(cpwayPlanOld.getCpId());
			cpwayPlanNew.setVersionNo(cpwayPlanOld.getVersionNo());
			cpwayPlanNew.setStageId(cpwayPlanOld.getStageId());
			cpwayPlanNew.setCreateUser(longinUserAccountCode);
			cpwayPlanNew.setCreateTime(DateUtils.getCurrentTime());
			cpwayPlanNew.setModelCode(modelCode);
			cpwayPlanNew.setPlanCode(modelNature);
			clinicalPathwayDao.save(cpwayPlanNew);
		}
	}

	@Override
	public List<TreeJson> queryPatientTree(String treeId) {
		//设置根节点（住院科室）
		List<TreeJson> returnlist = new ArrayList<TreeJson>();
		//判断是否为根节点
		if(StringUtils.isNotBlank(treeId)&&!"root".equals(treeId)){
			List<InpatientInfoNow> patientByDeptCodeList = clinicalPathwayDao.findPatientByDeptCode(treeId);
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("deptCode", treeId);
			for(InpatientInfoNow inpatient:patientByDeptCodeList){
				TreeJson treePatient = new TreeJson();
				treePatient.setId(inpatient.getInpatientNo());
				treePatient.setText(inpatient.getPatientName());
				treePatient.setAttributes(attributes);
				treePatient.setState("open");
				int babyflag = 0;
				if(inpatient.getBabyFlag()!=null){
					babyflag = inpatient.getBabyFlag();
				}
				if(babyflag==1){
					treePatient.setIconCls("icon-image_baby");
				}else{
					if(inpatient.getReportSex()!=null){
						if("1".equals(inpatient.getReportSex())){
							treePatient.setIconCls("icon-user_b");
						}
						else if("2".equals(inpatient.getReportSex())){
							treePatient.setIconCls("icon-user_female");
						}
					}					
				}
				returnlist.add(treePatient);
			}
		}else{
			//查询住院科室
			List<SysDepartment> deptList = deptInInterService.findTree(true, "I");
			//查询二级节点（患者）
			//设置一级节点（科室）
//		List<TreeJson> list = deptInInterService.QueryTreeDepartmen(false, "I",null,null);
			List<TreeJson> treeDeptList = new ArrayList<TreeJson>();
			for(SysDepartment dept : deptList){
				TreeJson treeDept = new TreeJson();
				treeDept.setId(dept.getDeptCode());
				treeDept.setText(dept.getDeptName());
				treeDept.setIconCls("icon-bullet_home");
				treeDept.setState("closed");
				treeDeptList.add(treeDept);
			}
			//设置根节点（住院科室）
			returnlist = new ArrayList<TreeJson>();
			TreeJson pat = new TreeJson();
			pat.setIconCls("icon-branch");
			pat.setId("root");
			pat.setText("住院科室");
			pat.setChildren(treeDeptList);
			returnlist.add(pat);
		}
		return returnlist;
	}
	
	@Override
	public List<TreeJson> queryInPatientTree(String treeId) {
		//设置根节点（住院科室）
		List<TreeJson> returnlist = new ArrayList<TreeJson>();
		//判断是否为根节点
		if(StringUtils.isNotBlank(treeId)&&!"root".equals(treeId)){
			//该科室下的出径患者
			List<InpatientInfoNow> inPatientByDeptCodeList = clinicalPathwayDao.findInPatientByDeptCode(treeId);
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("deptCode", treeId);
			for(InpatientInfoNow inpatient:inPatientByDeptCodeList){
				//患者下的阶段
				List<CpwayPlan> cpList = clinicalPathwayDao.findStageByInpatientNo(inpatient.getInpatientNo());
				List<TreeJson> treeCpList = new ArrayList<TreeJson>();
				for(CpwayPlan cp : cpList){
					TreeJson treeStage = new TreeJson();
					String stage = cp.getStageId();
					if(StringUtils.isNotBlank(stage)){
						if("1".equals(stage)){
							treeStage.setText("入院日");
						}else{
							treeStage.setText("入院"+stage+"天");
						}
					}
					Map<String,String> attributesStage = new HashMap<String, String>();
					attributesStage.put("cpId", cp.getId());
					attributesStage.put("stage", stage);
					treeStage.setAttributes(attributesStage);
					treeCpList.add(treeStage);
				}
				
				TreeJson treePatient = new TreeJson();
				treePatient.setId(inpatient.getInpatientNo());
				treePatient.setText(inpatient.getPatientName());
				treePatient.setAttributes(attributes);
				treePatient.setState("open");
				treePatient.setChildren(treeCpList);
				int babyflag = 0;
				if(inpatient.getBabyFlag()!=null){
					babyflag = inpatient.getBabyFlag();
				}
				if(babyflag==1){
					treePatient.setIconCls("icon-image_baby");
				}else{
					if(inpatient.getReportSex()!=null){
						if("1".equals(inpatient.getReportSex())){
							treePatient.setIconCls("icon-user_b");
						}
						else if("2".equals(inpatient.getReportSex())){
							treePatient.setIconCls("icon-user_female");
						}
					}					
				}
				returnlist.add(treePatient);
			}
		}else{
			//查询住院科室
			List<SysDepartment> deptList = deptInInterService.findTree(true, "I");
			//查询二级节点（患者）
			//设置一级节点（科室）
//				List<TreeJson> list = deptInInterService.QueryTreeDepartmen(false, "I",null,null);
			List<TreeJson> treeDeptList = new ArrayList<TreeJson>();
			for(SysDepartment dept : deptList){
				TreeJson treeDept = new TreeJson();
				treeDept.setId(dept.getDeptCode());
				treeDept.setText(dept.getDeptName());
				treeDept.setIconCls("icon-bullet_home");
				treeDept.setState("closed");
				treeDeptList.add(treeDept);
			}
			//设置根节点（住院科室）
			returnlist = new ArrayList<TreeJson>();
			TreeJson pat = new TreeJson();
			pat.setIconCls("icon-branch");
			pat.setId("root");
			pat.setText("住院科室");
			pat.setChildren(treeDeptList);
			returnlist.add(pat);
		}
		return returnlist;
	}
	
	@Override
	public List<CpVcontrol> queryCpWayVersion(String cpId) {
		return clinicalPathwayDao.queryCpWayVersion(cpId);
	}

	@Override
	public void saveCmi(CpMasterIndex cmi) {
		CpMasterIndex newCmi = clinicalPathwayDao.findCmiByInpatient(cmi.getInpatientNo());
		if(newCmi!=null){
			newCmi.setUpdateUser(cmi.getCreateUser());
			newCmi.setUpdateTime(cmi.getCreateTime());
			clinicalPathwayDao.save(newCmi);
		}else{
			cmi.setId(null);
			clinicalPathwayDao.save(cmi);
		}
	}

	@Override
	public void saveCeAndCed(CpExecute ce, CpExecuteDetail ced) {
		ce.setId(null);
		String modelCode = ce.getModelCode();
		List<String> modelCodeList = Arrays.asList(modelCode.split(","));
		for(String mc : modelCodeList){
			ced.setModelCode(mc);
			saveCed(ced);
			ce.setModelCode(mc);
			clinicalPathwayDao.save(ce);
		}
	}
	
	@Override
	public void saveCe(CpExecute ce) {
		ce.setId(null);
		String modelCode = ce.getModelCode();
		List<String> modelCodeList = Arrays.asList(modelCode.split(","));
		for(String mc : modelCodeList){
			ce.setModelCode(mc);
			clinicalPathwayDao.save(ce);
		}
	}

	@Override
	public void saveCed(CpExecuteDetail ced) {
		ced.setId(null);
		String itemCode = ced.getItemCode();
		List<String> itemCodeList = Arrays.asList(itemCode.split(","));
		for(String ic : itemCodeList){//模板明细id
			ModelVsItem mvi = clinicalPathwayModelService.findPathwayModelDetailById(ic);
			CpExecuteDetail newCed = new CpExecuteDetail();
			newCed.setInpatientNo(ced.getInpatientNo());
			newCed.setCpId(ced.getCpId());
			newCed.setVersionCode(ced.getVersionCode());
			newCed.setExecuteDeptCode(ced.getExecuteDeptCode());
			newCed.setModelCode(ced.getModelCode());
			newCed.setItemCode(ic);
			newCed.setItemName(mvi.getItemName());
			newCed.setOrderType(mvi.getFlag());
			newCed.setUnit(mvi.getUnit());
			newCed.setNum(mvi.getNum());
			newCed.setFrequencyCode(mvi.getFrequencyCode());
			newCed.setDirectionCode(mvi.getDirectionCode());
			newCed.setCreateUser(ced.getCreateUser());
			newCed.setCreateTime(ced.getCreateTime());
			clinicalPathwayDao.save(newCed);
		}
	}

	@Override
	public List<CpExecute> executeInfoByInpatientNo(String inpatientNo) {
		return clinicalPathwayDao.executeInfoByInpatientNo(inpatientNo);
	}

	@Override
	public void outPath(String inpatientNo) {
		clinicalPathwayDao.outPath(inpatientNo);
	}

	@Override
	public void executePath(String executeId) {
		clinicalPathwayDao.executePath(executeId);
	}

	@Override
	public List<CpExecuteDetail> executeDetail(String inpatientNo, String cpId,
			String modelCode) {
			return clinicalPathwayDao.executeDetail(inpatientNo,cpId,modelCode);
	}

	@Override
	public void saveOrUpdateAssess(InpAccess ia) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
		Date currentTime = DateUtils.getCurrentTime();
		if(StringUtils.isBlank(ia.getId())){//新增
			ia.setId(null);
			ia.setCreateTime(currentTime);
			ia.setCreateUser(longinUserAccountCode);
			ia.setAccessUser(longinUserAccountCode);
			ia.setAccessDate(currentTime);
			ia.setRoleFlag(role.getName());
			clinicalPathwayDao.save(ia);
		}else{
			InpAccess newIa = clinicalPathwayDao.findInpAccess(ia.getId());
			
			newIa.setStageId(ia.getStageId());
			newIa.setAccessResult(ia.getAccessResult());
			newIa.setDays(ia.getDays());
			newIa.setUpdateTime(currentTime);
			newIa.setUpdateUser(longinUserAccountCode);
			newIa.setRoleFlag(role.getName());
			clinicalPathwayDao.save(newIa);
		}
	}
	@Override
	public void approveAssess(InpAccess ia) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Date currentTime = DateUtils.getCurrentTime();
		if(StringUtils.isBlank(ia.getId())){//新增
		}else{
			InpAccess newIa = clinicalPathwayDao.findInpAccess(ia.getId());
			
			newIa.setAccessCheckInfo(ia.getAccessCheckInfo());
			newIa.setAccessCheckDate(currentTime);
			newIa.setAccessCheckUser(longinUserAccountCode);
			clinicalPathwayDao.save(newIa);
		}
	}

	@Override
	public List<InpAccess> queryAssess(String inpatientNo, String stage,
			String page, String rows) {
		return clinicalPathwayDao.queryAssess(inpatientNo, stage, page, rows);
	}

	@Override
	public int queryAssessNum(String inpatientNo, String stage) {
		return clinicalPathwayDao.queryAssessNum(inpatientNo, stage);
	}

}
