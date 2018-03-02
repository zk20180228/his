package cn.honry.inner.inpatient.info.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.bean.model.BusinessMedicalGroup;
import cn.honry.base.bean.model.BusinessMedicalGroupInfo;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InsuranceSiitem;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.inpatient.drugbilldetail.dao.InpatientDrugbilldetailInnerDAO;
import cn.honry.inner.inpatient.info.dao.InpatientInfoInInterDAO;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;
import cn.honry.inner.inpatient.info.vo.InfoInInterVo;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.MyBeanUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("inpatientInfoInInterService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientInfoInInterServiceImpl implements InpatientInfoInInterService{
	private  static Map<String,String> name=null;
	@Autowired
	private InpatientInfoInInterDAO inpatientInfoDAO;

	@Override
	public InpatientInfoNow queryByMedical(String medicalNo) {
		return inpatientInfoDAO.queryByMedical(medicalNo);
	}
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterDAO;//医院参数dao
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	
	@Autowired
	@Qualifier(value="inpatientDrugbilldetailInnerDAO")
	private InpatientDrugbilldetailInnerDAO inpatientDrugbilldetailInnerDAO;
	
	@Autowired
	@Qualifier(value="stackinfoInInterDAO")
	private StackinfoInInterDAO stackinfoInInterDAO;
	
	@Autowired
	@Qualifier(value="deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	
	@Autowired
	@Qualifier(value="patinentInnerService")
	private PatinentInnerService patinentInnerService;
	
	@Autowired
	@Qualifier(value="matOutPutInInterService")
	private MatOutPutInInterService matOutPutInInterService;
	
	public void setMatOutPutInInterService(
			MatOutPutInInterService matOutPutInInterService) {
		this.matOutPutInInterService = matOutPutInInterService;
	}
	
	@Autowired
	@Qualifier(value="businessStockInfoInInterService")
	private BusinessStockInfoInInterService  businessStockInfoInInterService;
	
	public void setBusinessStockInfoInInterService(
			BusinessStockInfoInInterService businessStockInfoInInterService) {
		this.businessStockInfoInInterService = businessStockInfoInInterService;
	}

	@Override
	public InpatientInfoNow get(String arg0) {
		return inpatientInfoDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientInfoNow info) {
		inpatientInfoDAO.save(info);
	}
	
	/**  
	 * 根据id查询患者信息
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public InpatientInfoNow querNurseCharge(String no) {
		return inpatientInfoDAO.querNurseCharge(no);
	}
	
	/**  
	 *  
	 * @Description： (根据病历号获取信息) 住院收费
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */

	@Override
	public List<TreeJson> treeNurseCharge(String deptId,String id,InpatientInfoNow inpatientInfo,String a,String startTime,String endTime) {
		List<InfoInInterVo> listDept=new ArrayList<InfoInInterVo>();
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
		TreeJson treeJson=null;
		if(StringUtils.isBlank(id)){//根节点
			//
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			if("1".equals(a)){
				pTreeJson.setText("在院患者");
			}else if("2".equals(a)){
				pTreeJson.setText("出院未结算患者");
			}else if("3".equals(a)||"4".equals(a)){
				pTreeJson.setText("全部患者");
			}else{
				pTreeJson.setText("本区患者");
			}
			Map<String,String> attributes1=new HashMap<String, String>();
			attributes1.put("wb", "");
			attributes1.put("pinyin", "");
			attributes1.put("inputcode", "");
			pTreeJson.setAttributes(attributes1);
			treeJsonList.add(pTreeJson);
			listDept=inpatientInfoDAO.treeNursegetDept(deptId);
			if(listDept!=null&&listDept.size()>0){
				for(InfoInInterVo sysDept : listDept){
					 treeJson = new TreeJson();
					treeJson.setId(sysDept.getDeptCode());
					treeJson.setText(sysDept.getDeptName());
					Map<String,String> attributes=new HashMap<String, String>();
					attributes.put("pid","1");
					attributes.put("wb", "");
					attributes.put("pinyin", "");
					attributes.put("inputcode", "");
					treeJson.setAttributes(attributes);
					treeJson.setState("closed");
					treeJsonList.add(treeJson);
				}
			}else{
				listDept=inpatientInfoDAO.treeNurseCharge(id,inpatientInfo,"5",startTime,endTime);//"5"代表护士站下没有科室
				if(listDept != null && listDept.size() > 0){
					for (InfoInInterVo sysDept : listDept) {
						 treeJson = new TreeJson();
						treeJson.setId(sysDept.getId());
						treeJson.setText(sysDept.getPatientName());
						Map<String,String> attributes=new HashMap<String, String>();
						attributes.put("pid","1");
						attributes.put("inpatientNo",sysDept.getInpatientNo());
						attributes.put("medicalrecordId",sysDept.getMedicalrecordId());
						attributes.put("idcardNo",sysDept.getIdcardNo());
						attributes.put("wb", "");
						attributes.put("pinyin", "");
						attributes.put("inputcode", "");
						treeJson.setAttributes(attributes);
						treeJson.setState("closed");
						treeJsonList.add(treeJson);
					}
				}		
			}
			return TreeJson.formatTree(treeJsonList);
		}else{
			Map<String, String> map = new HashMap<String, String>();
			List<BusinessContractunit> list = inpatientInfoDAO.queryContractunit();
			if(list!=null&&list.size()>0){
				for(BusinessContractunit contractunit : list){
					map.put(contractunit.getEncode(), contractunit.getName());
				}
			}
			listDept=inpatientInfoDAO.treeNurseCharge(id,inpatientInfo,a,startTime,endTime);
			if(listDept != null && listDept.size() > 0){
				for (int i=0;i<listDept.size();i++) {
					treeJson = new TreeJson();
					treeJson.setId(listDept.get(i).getId());
					
					String pactCode=map.get(listDept.get(i).getPactCode());
					if(StringUtils.isBlank(pactCode)){
						pactCode="自费";
					}
					
					treeJson.setText("【"+listDept.get(i).getBedName()+"】【"+listDept.get(i).getMedicalrecordId()+"】"+listDept.get(i).getPatientName()+"【"+pactCode+"】");
					Map<String,String> attributes=new HashMap<String, String>();
					int babyflag = 0;
					if(listDept.get(i).getBabyFlag()!=null){
						babyflag = listDept.get(i).getBabyFlag();
					}
					if(babyflag==1){
						treeJson.setIconCls("icon-image_baby");
					}else{
						if(listDept.get(i).getReportSex()!=null){
							if("1".equals(listDept.get(i).getReportSex())){
								treeJson.setIconCls("icon-user_b");
							}
							else if("2".equals(listDept.get(i).getReportSex())){
								treeJson.setIconCls("icon-user_female");
							}
						}					
					}
					attributes.put("pid",id);
					attributes.put("inpatientNo",listDept.get(i).getInpatientNo());
					attributes.put("medicalrecordId",listDept.get(i).getMedicalrecordId());
					attributes.put("idcardNo",listDept.get(i).getIdcardNo());
					attributes.put("wb", "");
					attributes.put("pinyin", "");
					attributes.put("inputcode", "");
					treeJson.setAttributes(attributes);
					treeJsonList.add(treeJson);
				}
			}else{
				treeJson = new TreeJson();
				treeJson.setId("none");
				treeJson.setText("没有患者");
				Map<String,String> attributes=new HashMap<String, String>();
				attributes.put("pid",id);
				attributes.put("wb", "");
				attributes.put("pinyin", "");
				attributes.put("inputcode", "");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}		
		}
		return treeJsonList;
	}
		
	
	@Override
	public List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) {
		List<InpatientDrugbilldetail> inpatientDrugbilldetailList = inpatientDrugbilldetailInnerDAO.queryInpatientDrugbilldetail(inpatientDrugbilldetail);		
		return inpatientDrugbilldetailList;
	}

	@Override
	public List<TreeJson> treeDrugExes(String id) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){//根节点
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<InpatientKind> inpatientKindList = inpatientInfoDAO.treeDrugExe();
			for(InpatientKind inpatientKind:inpatientKindList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(inpatientKind.getId());
				cTreeJson.setText(inpatientKind.getTypeName());
				cTreeJson.setState("closed");
				cAttMap.put("pid", "root");
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}else if("$".equals(id.substring(0, 1))){
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<BusinessDictionary> codeUseageList = innerCodeDao.getDictionary("useage");
			for(BusinessDictionary useage:codeUseageList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(useage.getId());
				cTreeJson.setText(useage.getName());
				cTreeJson.setState("open");
				cAttMap.put("pid", id);
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}else {
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<BusinessDictionary> codeDrugtypeList = innerCodeDao.getDictionary("drugType");
			for(BusinessDictionary codeDrugtype:codeDrugtypeList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId("$"+codeDrugtype.getId());
				cTreeJson.setText(codeDrugtype.getName());
				cTreeJson.setState("closed");
				cAttMap.put("pid", id);
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}
		return treeJsonList;
	}
	
	@Override
	public List<InpatientKind> treeDrugExe() {
		List<InpatientKind> inpatientKindList = inpatientInfoDAO.treeDrugExe();
		return inpatientKindList;
	}

	@Override
	public List<TreeJson> treeNoDrugExe(String id) {
	
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();		
		if(StringUtils.isBlank(id)){//根节点
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<InpatientKind> inpatientKindList = inpatientInfoDAO.treeDrugExe();
			for(InpatientKind inpatientKind:inpatientKindList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(inpatientKind.getId());
				cTreeJson.setText(inpatientKind.getTypeName());
				cTreeJson.setState("closed");
				cAttMap.put("pid", "root");
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
				
			}
		}else {
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<BusinessDictionary> codeSystemtypeList = innerCodeDao.getDictionary("systemType");
			for(BusinessDictionary codeSystemtype:codeSystemtypeList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(codeSystemtype.getId());
				cTreeJson.setText(codeSystemtype.getName());
				cTreeJson.setState("open");
				cAttMap.put("pid", id);
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}
		return treeJsonList;
	}

	@Override
	public List<InpatientExecbill> queryDocAdvExe(String ids,String billName) {
		List<InpatientExecbill> inpatientExecbillList = inpatientInfoDAO.queryDocAdvExe(ids,billName);
		
		return inpatientExecbillList;
	}
	@Override
	public List<TreeJson> treeInpatient(String id) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){//根节点
			Map<Integer, String> weekMap = new HashMap<Integer, String>();
			weekMap.put(0, "分管患者");
			weekMap.put(1, "本科室患者");
			weekMap.put(2, "会诊患者");
			weekMap.put(3, "授权患者");
			TreeJson gTreeJson = null;
			Map<String,String> gAttMap = null;
			for (int i = 0; i < 4; i++) {
				gTreeJson = new TreeJson();
				gAttMap = new HashMap<String, String>();
				gTreeJson.setId((i+1)+"");
				gTreeJson.setText(weekMap.get(i));
				gTreeJson.setState("closed");
				gTreeJson.setIconCls("icon-people");
				gAttMap.put("pid", "root");
				gTreeJson.setAttributes(gAttMap);
				treeJsonList.add(gTreeJson);
			}
		}else{
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<InpatientInfoNow> inpatientInfoList = inpatientInfoDAO.findTree(id);
			Map<String, String> map = new HashMap<String, String>();
			List<BusinessContractunit> list = inpatientInfoDAO.queryContractunit();
			if(list!=null&&list.size()>0){
				for(BusinessContractunit contractunit : list){
					map.put(contractunit.getEncode(), contractunit.getName());
				}
			}
			for(InpatientInfoNow inpatientInfo:inpatientInfoList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(inpatientInfo.getId());
				inpatientInfo.setPactCode(map.get(inpatientInfo.getPactCode()));
				if(StringUtils.isBlank(inpatientInfo.getPactCode())){
					inpatientInfo.setPactCode("自费");
				}
				cTreeJson.setText("【"+inpatientInfo.getBedName()+"】【"+inpatientInfo.getMedicalrecordId()+"】"+inpatientInfo.getPatientName()+"【"+inpatientInfo.getPactCode()+"】");
				cTreeJson.setState("open");
				cAttMap.put("pid", id);
				cAttMap.put("name", inpatientInfo.getPatientName());
				cAttMap.put("inpatientNo", inpatientInfo.getInpatientNo());
				cAttMap.put("medicalrecordId", inpatientInfo.getMedicalrecordId());
				cAttMap.put("deptCode", inpatientInfo.getDeptCode());
				cAttMap.put("nurseCellCode", inpatientInfo.getNurseCellCode());
				int babyflag = 0;
				if(inpatientInfo.getBabyFlag()!=null){
					babyflag = inpatientInfo.getBabyFlag();
				}
				if(babyflag==1){
					cTreeJson.setIconCls("icon-image_baby");
				}else{
					if(inpatientInfo.getReportSex()!=null){
						if("1".equals(inpatientInfo.getReportSex())){
							cTreeJson.setIconCls("icon-user_b");
						}
						else if("2".equals(inpatientInfo.getReportSex())){
							cTreeJson.setIconCls("icon-user_female");
						}
					}					
				}
				cAttMap.put("babyFlag",inpatientInfo.getBabyFlag() == null ? null : inpatientInfo.getBabyFlag().toString());
				cAttMap.put("reportSex", inpatientInfo.getReportSex());
				cAttMap.put("freeCost", inpatientInfo.getFreeCost() == null ? null : inpatientInfo.getFreeCost().toString());//余额
				cAttMap.put("pactCode", inpatientInfo.getPactCode());
				cAttMap.put("totCost", inpatientInfo.getTotCost() == null ? null : inpatientInfo.getTotCost().toString());//费用总额
				cAttMap.put("prepayCost", inpatientInfo.getPrepayCost() == null ? null : inpatientInfo.getPrepayCost().toString());//预交金总额
				String leaveFlag=null;
				if(inpatientInfo.getLeaveFlag() != null && inpatientInfo.getLeaveFlag()==1){
					leaveFlag="[请假]";
				}
				cAttMap.put("leaveFlag", leaveFlag);
				String dateState=null;
				if(inpatientInfo.getInDate() != null){
					Date a =DateUtils.addDay(inpatientInfo.getInDate(),1);
					Date b =DateUtils.getCurrentTime();				
					if(DateUtils.compareDate(a,b)==1){
						dateState="(新)";
					}
				}
				cAttMap.put("dateState", dateState);
				if("3".equals(id)){
					List<InpatientConsultation> inpatientConsultation = inpatientInfoDAO.findInpatientConsultation(inpatientInfo.getInpatientNo());
					String createOrderFlag = null;
					if(inpatientConsultation!=null && inpatientConsultation.size()>0){
						createOrderFlag = inpatientConsultation.get(0).getCreateOrderFlag();					
					}
					cAttMap.put("createOrderFlag", createOrderFlag);
				}				
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}		
		return treeJsonList;
	}
	
	@Override
	public List<TreeJson> findTree(String treeAll, String deptTypes) {
		return null;		
	}
	
	/**  
	 *  
	 * @Description：根据住院流水号查询患者信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-18
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	@Override
	public  List<InpatientInfoNow> queryNurseChargeInpinfo(String no,String dept) {
		//String med=patinentInnerService.getMedicalrecordId(no);
		return inpatientInfoDAO.queryNurseChargeInpinfo(no,dept);
	}
	@Override
	public List<SysEmployee> employeeComboboxProof(String departmentCode,String q) {
		return inpatientInfoDAO.employeeComboboxProof(departmentCode,q);
	}

	@Override
	public List<SysEmployee> queryEmpMapPublic() {
		return inpatientInfoDAO.queryEmpMapPublic();
	}

	@Override
	public List<SysDepartment> queryDeptMapPublic() {
		return inpatientInfoDAO.queryDeptMapPublic();
	}

	@Override
	public List<BusinessContractunit> queryContractunitListForcombobox() {
		return inpatientInfoDAO.queryContractunitListForcombobox();
	}

	@Override
	public List<BusinessDictionary> queryDictionaryListForcomboboxPublic(String type, String encode) {
		return inpatientInfoDAO.queryDictionaryListForcomboboxPublic(type, encode);
	}

	@Override
	public List<User> queryUserListPublic() {
		return inpatientInfoDAO.queryUserListPublic();
	}

	@Override
	public List<BusinessFrequency> queryFrequencyListPublic() {
		return inpatientInfoDAO.queryFrequencyListPublic();
	}
	
	/**
	 * @Description:费用接口
	 * @Author： yeguanqun
	 * @CreateDate： 2016-2-19
	 * @param feeVoList：费用结算vo集合
	 * map中的几种消息状态及信息：
	 * resCode：对应的处理状态的信息提示
	 * resMsg：表示处理的状态
	 * resCode="success" 生成费用成功
	 * resCode="arrearage" -患者余额不足
	 * resCode="error"-患者正在出院结算或已经无费退院，不能进行收费操作、婴儿患者母亲信息不存在，不能进行收费操作、婴儿患者母亲非在院状态，请先进行召回操作、患者账户关闭，不能进行收费操作
	 * @version 1.0
	**/
	@Override
	public Map<String, Object> saveInpatientFeeInfo(List<FeeInInterVo> newfeeVoList){
		name=deptInInterService.querydeptCodeAndNameMap();
		Map<String, String> map1=new HashMap<String, String>();
		//用于计算各患者的费用汇总信息,key为处方号;value为vo对象,
		//vo对象中的totCost、ownCost、pubCost等属性的值即为该处方号对应的费用汇总
		Map<String,FeeInInterVo> patientMap= new HashMap<>();
		Map<String,Object> map=new HashMap<String,Object>();//返回信息
		if(newfeeVoList==null|| newfeeVoList.size()==0){
			map.put("resCode", "error");
			map.put("resMsg", "无费用信息要保存！");
			return map;
		}
		
		//1.判断患者账户是否有效
		Map<String, Object> validMap = getValidInfo(newfeeVoList);
		Object object = validMap.get("resCode");
		if("ok".equals(object)){
			Object object2 = validMap.get("resMsg");
			newfeeVoList=(List<FeeInInterVo>) validMap.get("resMsg");
		}else{
			return validMap;
		}
		
		//2.调用findUndrug 方法实现以下功能:
		//(1)计算各个收费项目的金额(总金额、公费金额、自费金额、优惠金额等)
		//(2)对收费项目按照:'患者'进行分组;
		
		Map<String,List<FeeInInterVo>> feeMap=findUndrug(newfeeVoList);
		List<FeeInInterVo> list2 = feeMap.get("resCode");
		if(list2!=null && list2.size()>0){
			String itemCode = list2.get(0).getItemCode();
			map.put("resCode", "error");
			map.put("resMsg", "'"+itemCode+"'的库存不足！");
			return map;
		}
		List<FeeInInterVo> list3 = feeMap.get("error");
		if(list3!=null && list3.size()>0){
			String itemCode = list3.get(0).getItemCode();
			map.put("resCode", "error");
			if(StringUtils.isBlank(itemCode)){
				map.put("resMsg", "收费项目编码为空,请联系管理员!");
			}else{
				map.put("resMsg", "未找到'"+itemCode+"'的相关信息！");
			}
			return map;
		}
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//获取当前的登录人	
		SysDepartment  loginDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室				
		String recipeNo1 ="";//网页面传的处方号集合（汇总表中存在）护士站收费用到
		String recipeNo2 ="";//网页面传的处方号集合（汇总表中不存在）护士站收费用到
		String recipeNo3 ="";//网页面传的处方号集合 公用
		String SequenceNo3 = "";//网页面传的处方流水号集合 公用
		
		//Map集合feeMap的value(即List<FeeInInterVo>)中存的是某一个患者的所有收费项目信息
		for(Entry<String, List<FeeInInterVo>> entry: feeMap.entrySet()){
			List<FeeInInterVo> feeVoList=entry.getValue();//获取某一患者的所有收费项目
			//3.计算各个患者费用
			double totCost2 = 0;// 本次全部总费用(未结)
			double ownCost2 = 0;// 本次全部自费金额(未结)
			double payCost2 = 0;// 本次全部自付金额(预留--暂时等于自费金额)(未结)
			double pubCost2 = 0;// 本次全部公费金额(未结)
			double ecoCost2 = 0;// 本次全部优惠金额 (未结)	
			double changeTotcost2 = 0;//本次全部转入费用金额(未结)
			Map<String,List<FeeInInterVo>> feeCodeMap= new HashMap<>();//将患者的收费项目按最小费用代码和执行科室进行分组
			for (FeeInInterVo vo : feeVoList) {
				totCost2+=vo.getTotCost();
				ownCost2+=vo.getOwnCost();
				pubCost2+=vo.getPubCost();
				ecoCost2+=vo.getEcoCost();
				Double changeTotcost = vo.getChangeTotcost()==null?0:vo.getChangeTotcost();
				changeTotcost2+=changeTotcost;
				
				String key = vo.getFeeCode()+"-"+vo.getExecuteDeptCode();//最小费用代码
				List<FeeInInterVo> list = feeCodeMap.get(key);
				if(list!=null){
					list.add(vo);
				}else{
					list=new ArrayList<>();
					list.add(vo);
					feeCodeMap.put(key, list);
				}
				
			}
			//4.患者欠费判断
			int qw = 0;//是否已进行欠费判断的标志(0-未判断;1-已判断)
			FeeInInterVo vo = feeVoList.get(0);
			if(vo.getGoon()!=null){
				qw = vo.getGoon();
			}
			if(qw==0){//未进行欠费判断
				boolean bl = isArrearageByInpatientInfo(vo.getInpatient(),totCost2);						
				if(bl==true){//欠费
					map.put("resCode", "arrearage");
					map.put("resMsg", "患者账户余额不足");//患者账户余额不足	
					map.put("userId", account);//用户Id
					map.put("totCost", totCost2);//本次全部总费用
					map.put("orderId", vo.getOrderId());//网页面传的医嘱Id集合（汇总表中不存在）
					map.put("inpatientNo", vo.getInpatientNo());//患者住院流水号
					map.put("qty", vo.getQty());//网页面传的数量（汇总表中不存在）
					return map;									
				}
			}
			//程序走到这一步说明患者未欠费,更新患者住院主表费用信息
			InpatientInfoNow inpatientInfo=vo.getInpatient();
			double totCost = 0;
			double ownCost = 0;
			double payCost = 0;
			double pubCost = 0;
			double ecoCost = 0;
			double freeCost = 0;
			double prepayCost = 0;
			if(inpatientInfo.getTotCost()!=null){
				totCost = inpatientInfo.getTotCost();
			}
			if(inpatientInfo.getOwnCost()!=null){
				ownCost = inpatientInfo.getOwnCost();
			}
			if(inpatientInfo.getPayCost()!=null){
				payCost = inpatientInfo.getPayCost();
			}
			if(inpatientInfo.getPubCost()!=null){
				pubCost = inpatientInfo.getPubCost();
			}
			if(inpatientInfo.getEcoCost()!=null){
				ecoCost = inpatientInfo.getEcoCost();
			}
			if(inpatientInfo.getPrepayCost()!=null){
				prepayCost = inpatientInfo.getPrepayCost();
			}
			totCost = totCost+totCost2;
			ownCost = ownCost+ownCost2;
			payCost = payCost+ownCost2;
			pubCost = pubCost+pubCost2;
			ecoCost = ecoCost+ecoCost2;
			freeCost= prepayCost-ownCost;// 全部余额(未结)-未走医保		
			inpatientInfo.setTotCost(totCost);
			inpatientInfo.setOwnCost(ownCost);
			inpatientInfo.setPayCost(payCost);
			inpatientInfo.setPubCost(pubCost);
			inpatientInfo.setEcoCost(ecoCost);
			inpatientInfo.setFreeCost(freeCost);
			inpatientInfo.setChangeTotcost(changeTotcost2);
			inpatientInfo.setUpdateUser(account);
			inpatientInfo.setUpdateTime(DateUtils.getCurrentTime());
			inpatientInfoDAO.update(inpatientInfo);//修改住院主表中的汇总费用
			OperationUtils.getInstance().conserve(inpatientInfo.getId(),"费用","UPDATE","T_INPATIENT_INFO_NOW",OperationUtils.LOGACTIONINSERT);
			
			//5.按最小费用代码和执行科室进行分组,生成处方号和处方内流水号
			for(Entry<String, List<FeeInInterVo>> ent: feeCodeMap.entrySet()){
				List<FeeInInterVo> list = ent.getValue();
				String recipeNo=list.get(0).getRecipeNo();//处方号
				Integer no = 1;//处方内流水号
				for (FeeInInterVo feeInVo : list) {
					if(StringUtils.isBlank(recipeNo)){//处方号不存在
						if("1".equals(feeInVo.getTy())){//药品
							recipeNo= "Y0000"+inpatientInfoDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
						}else{//非药品
							recipeNo= "F0000"+inpatientInfoDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
						}
					}
					feeInVo.setRecipeNo(recipeNo);
					//处方内流水号
					if(feeInVo.getSequenceNo()==null ||feeInVo.getSequenceNo()<=0){
						feeInVo.setSequenceNo(no);
						no++;
					}else{
						no=feeInVo.getSequenceNo();
					}
					if(StringUtils.isNotBlank(feeInVo.getIndex())){
						SequenceNo3+=feeInVo.getIndex()+","+recipeNo+","+feeInVo.getSequenceNo()+"-";
					}
					FeeInInterVo interVo = patientMap.get(recipeNo);
					if(interVo!=null){
						interVo.setTotCost(interVo.getTotCost()+feeInVo.getTotCost());//总金额
						interVo.setOwnCost(interVo.getOwnCost()+feeInVo.getOwnCost());//自费金额
						interVo.setPubCost(interVo.getPubCost()+feeInVo.getPubCost());//公费金额
						interVo.setEcoCost(interVo.getEcoCost()+feeInVo.getEcoCost());//优惠金额
						
					}else{
						patientMap.put(recipeNo, feeInVo);
					}
					
					//6.保存药品明细和非药品明细 数据
					Date date = DateUtils.getCurrentTime();
					if("1".equals(feeInVo.getTy())){//药品
						InpatientMedicineListNow inpatientMedicineList = getMedicineList(feeInVo);//药品明细
						inpatientMedicineList.setChargeOpercode(account);//划价人
						inpatientMedicineList.setChargeDate(date);// 划价日期  
						
						if(feeInVo.getFeeFlag()==1){//收费
							inpatientMedicineList.setFeeOpercode(account);//计费人
							inpatientMedicineList.setFeeDate(date);// 计费时间
							inpatientMedicineList.setFeeoperDeptcode(loginDept.getDeptCode());// 收费员科室
						}
						inpatientMedicineList.setCreateUser(account);//创建人
						inpatientMedicineList.setCreateTime(date);//创建时间
						inpatientMedicineList.setCreateDept(loginDept.getDeptCode());//创建科室
						inpatientMedicineList.setHospitalId(HisParameters.CURRENTHOSPITALID);
						inpatientMedicineList.setAreaCode(inpatientInfoDAO.getDeptArea(loginDept.getDeptCode()));
						inpatientInfoDAO.save(inpatientMedicineList);
						OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_MEDICINELIST_NOW",OperationUtils.LOGACTIONINSERT);
					}
					if("2".equals(feeInVo.getTy())){//非药品
						
						InpatientItemListNow inpatientItemList = getItemList(feeInVo);//非药品明细
						
						inpatientItemList.setChargeOpercode(account);//划价人
						inpatientItemList.setChargeDate(date);// 划价日期 
						if(feeInVo.getFeeFlag()==1){//收费
							inpatientItemList.setFeeDate(date);// 计费时间
							inpatientItemList.setFeeOpercode(account);//计费人 
							inpatientItemList.setFeeoperDeptcode(loginDept.getDeptCode());// 收费员科室
						}
						inpatientItemList.setCreateUser(account);//创建人员
						inpatientItemList.setCreateTime(date);//创建时间
						inpatientItemList.setCreateDept(loginDept.getDeptCode());//创建科室
						inpatientItemList.setHospitalId(HisParameters.CURRENTHOSPITALID);
						inpatientItemList.setAreaCode(inpatientInfoDAO.getDeptArea(loginDept.getDeptCode()));
						inpatientInfoDAO.save(inpatientItemList);
						OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_ITEMLIST_NOW",OperationUtils.LOGACTIONINSERT);
					}
				}
			}
			
			
		}
		List<FeeInInterVo> feeVoList=new ArrayList<>();
		feeVoList.addAll(patientMap.values());
		try{
		
		
			
//		String parameterValue = parameterDAO.getParameterByCode("hszsfkysf");//护士站是否可以收费
//		if(parameterValue.equals("")){
//			parameterValue = "1";
//		}
//		String feeOpercode = account;//计费人
//		Date feeDate = null;//计费时间
//		if("1".equals(parameterValue)){
//			feeOpercode = account;
//			feeDate = DateUtils.getCurrentTime();
//		}
//		
		InpatientFeeInfoNow inpatientFeeInfo =null;//汇总表
		for (FeeInInterVo  feeInVo: feeVoList) {
			double totCost1 = 0;// 费用金额
			double ownCost1 = 0;// 自费金额
			double payCost1 = 0;// 自付金额(预留--暂时等于自费金额)
			double pubCost1 = 0;// 公费金额
			double ecoCost1 = 0;// 优惠金额
			String re = map1.get(feeInVo.getRecipeNo());
			if(re!=null){
				map1.put(feeInVo.getRecipeNo(), map1.get(feeInVo.getRecipeNo())+"-"+feeInVo.getSequenceNo().toString());
			}else{
				map1.put(feeInVo.getRecipeNo(), feeInVo.getSequenceNo().toString());
			}
			List<InpatientFeeInfoNow> inpatientFeeInfoList1 = inpatientInfoDAO.queryInpatientFeeInfo(feeInVo.getRecipeNo());//查询费用汇总表
			if(inpatientFeeInfoList1!=null&&inpatientFeeInfoList1.size()>0){//汇总表中存在该处方号
				inpatientFeeInfo = inpatientFeeInfoList1.get(0);
				if(inpatientFeeInfo.getTotCost()!=null){
					totCost1 = inpatientFeeInfo.getTotCost();// 费用金额
				}
				if(inpatientFeeInfo.getTotCost()!=null){
					ownCost1 = inpatientFeeInfo.getOwnCost();// 自费金额
				}
				if(inpatientFeeInfo.getTotCost()!=null){
					payCost1 = inpatientFeeInfo.getPayCost();// 自付金额(预留--暂时等于自费金额)
				}
				if(inpatientFeeInfo.getTotCost()!=null){
					pubCost1 = inpatientFeeInfo.getPubCost();// 公费金额
				}
				if(inpatientFeeInfo.getTotCost()!=null){
					ecoCost1 = inpatientFeeInfo.getEcoCost();// 优惠金额 
				}
				inpatientFeeInfo.setUpdateUser(account);
				inpatientFeeInfo.setUpdateTime(DateUtils.getCurrentTime());
				
				
			}else{//汇总表中不存在该处方号						
				 
				inpatientFeeInfo = getFeeInfo(feeInVo);//汇总
				inpatientFeeInfo.setChargeOpercode(account);//划价人
				inpatientFeeInfo.setChargeDate(DateUtils.getCurrentTime());// 划价日期 						
				recipeNo2 = recipeNo2+feeInVo.getRecipeNo()+",";
				if(feeInVo.getFeeFlag()==1){//收费
					inpatientFeeInfo.setFeeOpercode(account);//计费人 
					inpatientFeeInfo.setFeeDate(DateUtils.getCurrentTime());// 计费时间
					inpatientFeeInfo.setFeeoperDeptcode(loginDept.getDeptCode());//收费员科室 
				}
				inpatientFeeInfo.setCreateUser(account);
				inpatientFeeInfo.setCreateDept(loginDept.getDeptCode());
				inpatientFeeInfo.setCreateTime(DateUtils.getCurrentTime());
					
			}
			totCost1 += feeInVo.getTotCost();// 费用金额
			ownCost1 += feeInVo.getOwnCost();// 自费金额
			pubCost1 += feeInVo.getPubCost();// 公费金额
			ecoCost1 += feeInVo.getEcoCost();// 优惠金额 
			inpatientFeeInfo.setTotCost(totCost1);// 费用金额						
			inpatientFeeInfo.setOwnCost(ownCost1);// 自费金额
			inpatientFeeInfo.setPayCost(ownCost1);// 自付金额
			inpatientFeeInfo.setPubCost(pubCost1);// 公费金额
			inpatientFeeInfo.setEcoCost(ecoCost1);// 优惠金额 	
			if(StringUtils.isNotBlank(inpatientFeeInfo.getId())){//id不为空即update操作
				recipeNo1 = recipeNo1+feeInVo.getRecipeNo()+",";
				inpatientInfoDAO.update(inpatientFeeInfo);//修改汇总费用
				OperationUtils.getInstance().conserve(inpatientFeeInfo.getId(),"费用","UPDATE","T_INPATIENT_FEEINFO_NOW",OperationUtils.LOGACTIONINSERT);
			}else{//id为空 即insert操作
				//insert操作即 该处方号和处方流水号都是新生成的,在返回的处方号集合中没有此处方号
				inpatientFeeInfo.setAreaCode(inpatientInfoDAO.getDeptArea(loginDept.getDeptCode()));
				inpatientFeeInfo.setHospitalId(HisParameters.CURRENTHOSPITALID);
				inpatientInfoDAO.save(inpatientFeeInfo);//保存新增汇总费用
				OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_FEEINFO_NOW",OperationUtils.LOGACTIONINSERT);
				
			}
		}
		if(StringUtils.isNotBlank(recipeNo1)){
			recipeNo1=recipeNo1.substring(0, recipeNo1.length()-1);
		}
		if(StringUtils.isNotBlank(recipeNo2)){
			recipeNo2=recipeNo2.substring(0, recipeNo2.length()-1);
		}
		if(StringUtils.isNotBlank(SequenceNo3)){
			SequenceNo3=SequenceNo3.substring(0, SequenceNo3.length()-1);
		}
			map.put("recipeNo1", recipeNo1);
			map.put("recipeNo2", recipeNo2);
			map.put("recipeNo3", recipeNo2);
			map.put("SequenceNo3", SequenceNo3);
			map.put("recipeNo4", map1);
			map.put("resCode", "success");
			map.put("resMsg", "费用保存成功！");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "费用保存失败！");
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 反交易收费	
	* @Title: reverseTran
	* @Description: 
	* @param feeVoList  某一个患者的所有退费项目 
	* @return 
	* @date 2016年5月16日下午5:48:02
	 */
	public Map<String,Object> reverseTran(List<FeeInInterVo> feeVoList){
		Map<String,Object> map=new HashMap<>();//返回信息
		Map<String,List<FeeInInterVo>> feeMap= new HashMap();
		Map<String,Integer> map1=new HashMap<>();
		try {
			//1.按最小费用代码和执行科室进行分组
			for (FeeInInterVo vo : feeVoList) {
				String key=vo.getFeeCode()+"-"+vo.getExecuteDeptCode();
				List<FeeInInterVo> list = feeMap.get(key);
				if(list!=null){
					list.add(vo);
				}else{
					list= new ArrayList<>();
					list.add(vo);
					feeMap.put(key, list);
				}
				String recipeNo = vo.getRecipeNo();
				Integer i = map1.get(recipeNo);
				if(i==null){
					int j="1".equals(vo.getTy())?1:2;
					int no = inpatientInfoDAO.getMaxSequenceNo(recipeNo, j)+1;
					map1.put(recipeNo, no);
				}
			}
			double  totCost1 = 0;//费用金额(用于计算患者住院主表中的金额)               
			double  ownCost1 = 0;//自费金额(用于计算患者住院主表中的金额) 
			double  payCost1 = 0;//自付金额(用于计算患者住院主表中的金额)                
			double  pubCost1 = 0;//公费金额(用于计算患者住院主表中的金额)                 
			double  ecoCost1 = 0;//优惠金额(用于计算患者住院主表中的金额)    
			
			String newRecipeNo="";//新生成的处方号
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//获取当前的登录人
			SysDepartment  loginDept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			for(Entry<String, List<FeeInInterVo>> ent: feeMap.entrySet()){
				double  totCost2 = 0;//费用金额(用于计算费用汇总表中的金额)               
				double  ownCost2 = 0;//自费金额(用于计算费用汇总表中的金额) 
				double  payCost2 = 0;//自付金额(用于计算费用汇总表中的金额)                
				double  pubCost2 = 0;//公费金额(用于计算费用汇总表中的金额)                 
				double  ecoCost2 = 0;//优惠金额(用于计算费用汇总表中的金额)  
				List<FeeInInterVo> list = ent.getValue();
				int m = 1;
				for (FeeInInterVo vo : list){			
					String recipeNo = vo.getRecipeNo();//处方号
					Integer sequenceNo = vo.getSequenceNo();//处方内流水号
					double  totCost = 0;//费用金额                 
					double  ownCost = 0;//自费金额                 
					double  payCost = 0;//自付金额                
					double  pubCost = 0;//公费金额                 
					double  ecoCost = 0;//优惠金额 
					
					//2.生成药品或非药品的冲账记录(如果是部分退费,产生新的住院药品或非药品明细信息)
					if("1".equals(vo.getTy())){	//药品
						List<InpatientMedicineListNow> inpatientMedicineLists = inpatientInfoDAO.queryInpatientMedicineList(recipeNo, sequenceNo);
						if(inpatientMedicineLists!=null&&inpatientMedicineLists.size()>0){
							InpatientMedicineListNow medicineListNow = inpatientMedicineLists.get(0);
							Double qty = medicineListNow.getQty();
							Double cost = medicineListNow.getTotCost();
							Double qty2 = vo.getQty();
							Double cost1= vo.getQty()*cost/qty;
							vo.setTotCost(cost1);
							vo.setPubCost(qty2*medicineListNow.getPubCost()/qty);
							vo.setOwnCost(qty2*medicineListNow.getOwnCost()/qty);
							totCost1+=vo.getTotCost();
							ownCost1+=vo.getOwnCost();
							pubCost1+=vo.getPubCost();
							ecoCost1+=vo.getEcoCost();
							totCost2+=vo.getTotCost();
							ownCost2+=vo.getOwnCost();
							pubCost2+=vo.getPubCost();
							ecoCost2+=vo.getEcoCost();
							medicineListNow.setExtFlag1(1);//扩展标志1（是否冲底记录：1 是，0  否）
							InpatientMedicineListNow inpatientMedicine  = new InpatientMedicineListNow();
							MyBeanUtils.copyPropertiesButNull(inpatientMedicine, medicineListNow);
							inpatientMedicine.setId(null);
							inpatientMedicine.setRecipeNo(recipeNo);
							inpatientMedicine.setSequenceNo(sequenceNo);
							inpatientMedicine.setTransType(2);//反交易
							if(inpatientMedicine.getTotCost()!=null){
								totCost = inpatientMedicine.getTotCost();
							}
							if(inpatientMedicine.getOwnCost()!=null){
								ownCost = inpatientMedicine.getOwnCost();
							}
							if(inpatientMedicine.getPayCost()!=null){
								payCost = inpatientMedicine.getPayCost();
							}
							if(inpatientMedicine.getPubCost()!=null){
								pubCost = inpatientMedicine.getPubCost();
							}
							if(inpatientMedicine.getEcoCost()!=null){
								ecoCost = inpatientMedicine.getEcoCost();
							}
							inpatientMedicine.setQty(qty*(-1));
							inpatientMedicine.setTotCost(totCost*(-1));
							inpatientMedicine.setOwnCost(ownCost*(-1));
							inpatientMedicine.setPayCost(payCost*(-1));
							inpatientMedicine.setPubCost(pubCost*(-1));
							inpatientMedicine.setEcoCost(ecoCost*(-1));
							inpatientMedicine.setCheckOpercode(account);//划价人
							inpatientMedicine.setChargeDate(DateUtils.getCurrentTime());// 划价日期  
							inpatientMedicine.setFeeOpercode(account);//计费人 
							inpatientMedicine.setFeeDate(DateUtils.getCurrentTime());// 计费时间hisRecipeNo
							inpatientMedicine.setHisRecipeNo(vo.getRecipeNo());
							inpatientMedicine.setCreateUser(account);
							inpatientMedicine.setCreateDept(loginDept.getDeptCode());
							inpatientMedicine.setCreateTime(DateUtils.getCurrentTime());
							inpatientMedicine.setUpdateUser(null);
							inpatientMedicine.setUpdateTime(null);
							inpatientMedicine.setDeleteUser(null);
							inpatientMedicine.setDeleteTime(null);
							inpatientInfoDAO.save(inpatientMedicine);
							OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_MEDICINELIST_NOW",OperationUtils.LOGACTIONINSERT);	
							
							if(vo.getTotCost()<totCost){//部分退费
								FeeInInterVo feeInVo=new FeeInInterVo();
								//feeInVo.setRecipeNo(newRecipeNo);
								feeInVo.setQty(qty-vo.getQty());//剩余数量
								feeInVo.setTotCost(totCost-vo.getTotCost());//费用金额
								feeInVo.setOwnCost(ownCost-vo.getOwnCost());//自费金额
								feeInVo.setPubCost(pubCost-vo.getPubCost());//公费金额
								feeInVo.setEcoCost(ecoCost-vo.getEcoCost());//优惠金额
								InpatientMedicineListNow medicineList = new InpatientMedicineListNow();
								MyBeanUtils.copyPropertiesButNull(medicineList, medicineListNow);
								medicineList.setId(null);
								medicineList.setExtFlag1(0);
//								if(StringUtils.isBlank(newRecipeNo)){
//									newRecipeNo= "Y0000"+inpatientInfoDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
//								}
//								medicineList.setRecipeNo(newRecipeNo);//新的处方号
								m=map1.get(recipeNo);
								medicineList.setSequenceNo(m);//处方内流水号
								map.put(recipeNo+"-"+vo.getSequenceNo(), recipeNo+"-"+m);//返回新生成的处方号和处方内流水号
								m++;
								map1.put(recipeNo, m);
								medicineList.setQty(feeInVo.getQty());
								medicineList.setTotCost(feeInVo.getTotCost());
								medicineList.setOwnCost(feeInVo.getOwnCost());
								medicineList.setPayCost(feeInVo.getOwnCost());
								medicineList.setPubCost(feeInVo.getPubCost());
								medicineList.setEcoCost(feeInVo.getEcoCost());
								
								medicineList.setFeeOpercode(account);//计费人
								medicineList.setFeeDate(DateUtils.getCurrentTime());// 计费时间
								medicineList.setFeeoperDeptcode(loginDept.getDeptCode());// 收费员科室
								medicineList.setCreateUser(account);//创建人
								medicineList.setCreateDept(loginDept.getDeptCode());//创建科室
								medicineList.setCreateTime(DateUtils.getCurrentTime());//创建时间
								inpatientInfoDAO.save(medicineList);
								OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_MEDICINELIST_NOW",OperationUtils.LOGACTIONINSERT);	
							}
						}
					}
					if("2".equals(vo.getTy())){//非药品
						List<InpatientItemListNow> inpatientItemLists = inpatientInfoDAO.queryInpatientItemList(vo.getRecipeNo(), vo.getSequenceNo());
						if(inpatientItemLists!=null&&inpatientItemLists.size()>0){
							InpatientItemListNow itemListNow = inpatientItemLists.get(0);
							Double qty = itemListNow.getQty();
							Double cost = itemListNow.getTotCost();
							Double qty2 = vo.getQty();
							Double cost1= vo.getQty()*cost/qty;
							vo.setTotCost(cost1);
							vo.setPubCost(qty2*itemListNow.getPubCost()/qty);
							vo.setOwnCost(qty2*itemListNow.getOwnCost()/qty);
							totCost1+=vo.getTotCost();
							ownCost1+=vo.getOwnCost();
							pubCost1+=vo.getPubCost();
							ecoCost1+=vo.getEcoCost();
							totCost2+=vo.getTotCost();
							ownCost2+=vo.getOwnCost();
							pubCost2+=vo.getPubCost();
							ecoCost2+=vo.getEcoCost();
							itemListNow.setExtFlag1(1);//扩展标志1（是否冲底记录：1 是，0  否）
							InpatientItemListNow inpatientItem  = new InpatientItemListNow();
							MyBeanUtils.copyPropertiesButNull(inpatientItem, itemListNow);
							inpatientItem.setId(null);
							inpatientItem.setRecipeNo(recipeNo);
							inpatientItem.setSequenceNo(sequenceNo);
							inpatientItem.setTranstype(2);
							if(inpatientItem.getTotCost()!=null){
								totCost = inpatientItem.getTotCost();
							}
							if(inpatientItem.getOwnCost()!=null){
								ownCost = inpatientItem.getOwnCost();
							}
							if(inpatientItem.getPayCost()!=null){
								payCost = inpatientItem.getPayCost();
							}
							if(inpatientItem.getPubCost()!=null){
								pubCost = inpatientItem.getPubCost();
							}
							if(inpatientItem.getEcoCost()!=null){
								ecoCost = inpatientItem.getEcoCost();
							}
							inpatientItem.setQty(qty*(-1));
							inpatientItem.setTotCost(totCost*(-1));
							inpatientItem.setOwnCost(ownCost*(-1));
							inpatientItem.setPayCost(payCost*(-1));
							inpatientItem.setPubCost(pubCost*(-1));
							inpatientItem.setEcoCost(ecoCost*(-1));
							inpatientItem.setCheckOpercode(account);//划价人
							inpatientItem.setChargeDate(DateUtils.getCurrentTime());// 划价日期  
							inpatientItem.setFeeOpercode(account);//计费人 
							inpatientItem.setFeeDate(DateUtils.getCurrentTime());// 计费时间
							inpatientItem.setHisRecipeNo(vo.getRecipeNo());
							inpatientItem.setCreateUser(account);
							inpatientItem.setCreateDept(loginDept.getDeptCode());
							inpatientItem.setCreateTime(DateUtils.getCurrentTime());
							inpatientItem.setUpdateUser(null);
							inpatientItem.setUpdateTime(null);
							inpatientItem.setDeleteUser(null);
							inpatientItem.setDeleteTime(null);
							inpatientInfoDAO.save(inpatientItem);
							OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_ITEMLIST_NOW",OperationUtils.LOGACTIONINSERT);
							if(vo.getTotCost()<totCost){
								
								FeeInInterVo feeInVo=new FeeInInterVo();
								feeInVo.setRecipeNo(newRecipeNo);
								feeInVo.setQty(qty-vo.getQty());//剩余数量
								feeInVo.setTotCost(totCost-vo.getTotCost());//费用金额
								feeInVo.setOwnCost(ownCost-vo.getOwnCost());//自费金额
								feeInVo.setPubCost(pubCost-vo.getPubCost());//公费金额
								feeInVo.setEcoCost(ecoCost-vo.getEcoCost());//优惠金额
								
								InpatientItemListNow itemList = new InpatientItemListNow();
								MyBeanUtils.copyPropertiesButNull(itemList, itemListNow);
								itemList.setId(null);
								itemList.setExtFlag1(0);
//								if(StringUtils.isBlank(newRecipeNo)){
//									newRecipeNo = "F"+"0000"+inpatientInfoDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
//								}
//								itemList.setRecipeNo(newRecipeNo);//新的处方号
//								itemList.setSequenceNo(m);//新的处方内流水号
//								map.put(vo.getRecipeNo()+"-"+vo.getSequenceNo(), newRecipeNo+"-"+m);//返回新生成的处方号和处方内流水号
//								m++;
								m=map1.get(recipeNo);
								itemList.setSequenceNo(m);//处方内流水号
								map.put(recipeNo+"-"+vo.getSequenceNo(), recipeNo+"-"+m);//返回新生成的处方号和处方内流水号
								m++;
								map1.put(recipeNo, m);
								itemList.setQty(feeInVo.getQty());
								itemList.setTotCost(feeInVo.getTotCost());
								itemList.setOwnCost(feeInVo.getOwnCost());
								itemList.setPayCost(feeInVo.getOwnCost());
								itemList.setPubCost(feeInVo.getPubCost());
								itemList.setEcoCost(feeInVo.getEcoCost());
								
								itemList.setFeeOpercode(account);//计费人
								itemList.setFeeDate(DateUtils.getCurrentTime());// 计费时间
								itemList.setFeeoperDeptcode(loginDept.getDeptCode());// 收费员科室
								itemList.setCreateUser(account);//创建人
								itemList.setCreateDept(loginDept.getDeptCode());//创建科室
								itemList.setCreateTime(DateUtils.getCurrentTime());//创建时间
								inpatientInfoDAO.save(itemList);
								OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_MEDICINELIST_NOW",OperationUtils.LOGACTIONINSERT);
							}
						}
					}
				}
				//3.生成费用汇总信息的冲账记录
				FeeInInterVo feevo = list.get(0);
				double  totCost = 0;// 费用金额                 
				double  ownCost = 0;// 自费金额                 
				double  payCost = 0;// 自付金额                
				double  pubCost = 0;// 公费金额                 
				double  ecoCost = 0;// 优惠金额 
				List<InpatientFeeInfoNow> inpatientFeeInfoList = inpatientInfoDAO.queryInpatientFeeInfo(feevo.getRecipeNo());
				if(inpatientFeeInfoList!=null&&inpatientFeeInfoList.size()>0){
					InpatientFeeInfoNow inpatientFeeInfo  = new InpatientFeeInfoNow();
					InpatientFeeInfoNow infoNow = inpatientFeeInfoList.get(0);
					infoNow.setExtFlag1("1");//扩展标志1（是否冲底记录：1 是，0  否）
					MyBeanUtils.copyPropertiesButNull(inpatientFeeInfo, infoNow);
					inpatientFeeInfo.setId(null);
					inpatientFeeInfo.setTransType(2);
					if(inpatientFeeInfo.getTotCost()!=null){
						totCost = inpatientFeeInfo.getTotCost();
					}
					if(inpatientFeeInfo.getOwnCost()!=null){
						ownCost = inpatientFeeInfo.getOwnCost();
					}
					if(inpatientFeeInfo.getPayCost()!=null){
						payCost = inpatientFeeInfo.getPayCost();
					}
					if(inpatientFeeInfo.getPubCost()!=null){
						pubCost = inpatientFeeInfo.getPubCost();
					}
					if(inpatientFeeInfo.getEcoCost()!=null){
						ecoCost = inpatientFeeInfo.getEcoCost();
					}
					inpatientFeeInfo.setTotCost(totCost*(-1));
					inpatientFeeInfo.setOwnCost(ownCost*(-1));
					inpatientFeeInfo.setPayCost(payCost*(-1));
					inpatientFeeInfo.setPubCost(pubCost*(-1));
					inpatientFeeInfo.setEcoCost(ecoCost*(-1));
					inpatientFeeInfo.setChargeOpercode(account);//划价人
					inpatientFeeInfo.setChargeDate(DateUtils.getCurrentTime());// 划价日期 						
					inpatientFeeInfo.setFeeOpercode(account);//计费人 
					inpatientFeeInfo.setFeeDate(DateUtils.getCurrentTime());// 计费时间
					inpatientFeeInfo.setExtCode(feevo.getRecipeNo());
					inpatientFeeInfo.setCreateUser(account);
					inpatientFeeInfo.setCreateDept(loginDept.getDeptCode());
					inpatientFeeInfo.setCreateTime(DateUtils.getCurrentTime());
					inpatientFeeInfo.setUpdateUser(null);
					inpatientFeeInfo.setUpdateTime(null);
					inpatientFeeInfo.setDeleteUser(null);
					inpatientFeeInfo.setDeleteTime(null);
					inpatientInfoDAO.save(inpatientFeeInfo);
					OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_FEEINFO_NOW",OperationUtils.LOGACTIONINSERT);
					double sum=totCost-totCost2;
					if(sum>0){
						InpatientFeeInfoNow feeInfo = new InpatientFeeInfoNow();
						MyBeanUtils.copyPropertiesButNull(feeInfo, infoNow);
						feeInfo.setId(null);
						feeInfo.setExtFlag1("0");
						feeInfo.setTotCost(feeInfo.getTotCost()-totCost2);
						feeInfo.setOwnCost(feeInfo.getOwnCost()-ownCost2);
						feeInfo.setPayCost(feeInfo.getPayCost()-ownCost2);
						feeInfo.setPubCost(feeInfo.getPubCost()-pubCost2);
						feeInfo.setEcoCost(feeInfo.getEcoCost()-ecoCost2);
						feeInfo.setChargeOpercode(account);//划价人
						feeInfo.setChargeDate(DateUtils.getCurrentTime());// 划价日期 						
						feeInfo.setFeeOpercode(account);//计费人 
						feeInfo.setFeeDate(DateUtils.getCurrentTime());// 计费时间
						feeInfo.setExtCode(newRecipeNo);
						feeInfo.setCreateUser(account);
						feeInfo.setCreateDept(loginDept.getDeptCode());
						feeInfo.setCreateTime(DateUtils.getCurrentTime());
						feeInfo.setUpdateUser(null);
						feeInfo.setUpdateTime(null);
						feeInfo.setDeleteUser(null);
						feeInfo.setDeleteTime(null);
						inpatientInfoDAO.save(feeInfo);
						OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_FEEINFO_NOW",OperationUtils.LOGACTIONINSERT);
					}
				}
			}
			//4.修改住院主表中的患者费用信息
			List<InpatientInfoNow> inpatientInfoList = inpatientInfoDAO.queryInpatientInfo(feeVoList.get(0).getInpatientNo());	//患者信息-住院主表
			if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
				InpatientInfoNow inpatientInfo = inpatientInfoList.get(0);
				if(inpatientInfo.getTotCost()!=null){
					inpatientInfo.setTotCost(inpatientInfo.getTotCost()+totCost1*(-1));
				}else{
					inpatientInfo.setTotCost(totCost1*(-1));
				}
				if(inpatientInfo.getOwnCost()!=null){
					inpatientInfo.setOwnCost(inpatientInfo.getOwnCost()+ownCost1*(-1));
				}else{
					inpatientInfo.setOwnCost(ownCost1*(-1));
				}
				if(inpatientInfo.getPayCost()!=null){
					inpatientInfo.setPayCost(inpatientInfo.getPayCost()+payCost1*(-1));
				}else{
					inpatientInfo.setPayCost(payCost1*(-1));
				}
				if(inpatientInfo.getPubCost()!=null){
					inpatientInfo.setPubCost(inpatientInfo.getPubCost()+pubCost1*(-1));
				}else{
					inpatientInfo.setPubCost(pubCost1*(-1));
				}
				if(inpatientInfo.getEcoCost()!=null){
					inpatientInfo.setEcoCost(inpatientInfo.getEcoCost()+ecoCost1*(-1));
				}else{
					inpatientInfo.setEcoCost(ecoCost1*(-1));
				}
				inpatientInfo.setUpdateUser(account);
				inpatientInfo.setUpdateTime(DateUtils.getCurrentTime());
				inpatientInfoDAO.update(inpatientInfo);
				OperationUtils.getInstance().conserve(inpatientInfo.getId(),"费用","UPDATE","T_INPATIENT_INFO_NOW",OperationUtils.LOGACTIONUPDATE);
			}
			map.put("resCode", "success");
			map.put("resMsg", "费用保存成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "费用保存失败！");
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 判断患者是否欠费  
	 * 欠费计算公式     余额或预交金+担保金额<警戒线金额
	 * @param inpatientInfo
	 * @param totCost
	 * @return
	 */
	public  boolean isArrearageByInpatientInfo(InpatientInfoNow inpatientInfo,double totCost){
		boolean  bl=false;
		//未进行欠费判断
		List<InpatientSurety> inpatientSuretyList = inpatientInfoDAO.querysuretyCost(inpatientInfo.getInpatientNo());//查询患者账户明细表-获取担保金额
		double cost = 0;//余额或预交金
		double detailDebitamount = 0;//担保金额
		double moneyAlert = 0;//警戒线金额
		if(inpatientInfo.getPrepayCost()!=null){
			cost=inpatientInfo.getFreeCost();
		}
		if(inpatientSuretyList.size()>0){
			if(inpatientSuretyList.get(0).getSuretyCost()!=null){
				detailDebitamount=inpatientSuretyList.get(0).getSuretyCost();
			}
		}
		if(inpatientInfo.getMoneyAlert()!=null){
			moneyAlert = inpatientInfo.getMoneyAlert();
		}
//		String paykindCode = inpatientInfo.getPaykindCode();
//		if(StringUtils.isBlank(paykindCode)){
//			List<BusinessContractunit> contractunitList = inpatientInfoDAO.queryContractunit(inpatientInfo.getPactCode());//查询合同单位维护表-合同单位比例
//			if(contractunitList!=null&& contractunitList.size()>0){
//				paykindCode=contractunitList.get(0).getPaykindCode();//公费比例\报销比例
//			}	
//		}
//		if("02".equals(paykindCode)){//患者合同单位的结算类型为02（保险）							
//			double h=cost+detailDebitamount-totCost;//欠费计算
//			if(h<moneyAlert){
//				bl=true;							
//			}
//		}else{			
//			double j=cost+detailDebitamount-totCost;//欠费计算				
//			if(j<moneyAlert){
//				bl=true;
//			}
//		}
		double h=cost+detailDebitamount-totCost;//欠费计算
		if(h<moneyAlert){
			bl=true;							
		}
		return bl;
	}

	/**
	 * @Description:查询登录密码-校验登录密码
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-3-4
	 * @param user：用户信息实体类   
	 * @return void  
	 * @version 1.0
	**/
	@Override
	public List<User> confirmPassword(User user) {
		List<User> userList = inpatientInfoDAO.confirmPassword(user);
		return userList;
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String inpatientNo) {
		return inpatientInfoDAO.queryInpatientInfo(inpatientNo);
	}

	@Override
	public List<BusinessIcd10> queryICD() {
		return inpatientInfoDAO.queryICD();
	}

	
	/**
	 * @Description:
	 * 1.判断改收费项目(药品或非药品)的库存是否充足,计算该收费项目的金额(总金额、公费金额、自费金额、优惠金额等);
	 * 收费项目为组套时传递过来的已经是组套明细,无须再对组套进行查询
	 * 2.按患者(住院流水号) 分组
	 * @Author： zhuxiaolu
	 * @CreateDate： 2016-2-19
	 * @param feeVoList：费用结算vo集合
	 * @version 1.0
	**/
	private Map<String,List<FeeInInterVo>>  findUndrug(List<FeeInInterVo> feeVoList){
		Map<String,List<FeeInInterVo>> map =new HashMap<>();
		List<FeeInInterVo> newList=new ArrayList<FeeInInterVo>();
		//1.判断传过来的项目是否是组套,如果为组套,将项目替换为组套明细,并计算该收费项目的金额(totCast)
		for (FeeInInterVo feeVo : feeVoList){
			String itemCode = feeVo.getItemCode();//药品非药品项目编码
			if(StringUtils.isBlank(itemCode)){
				List<FeeInInterVo> list= new ArrayList<>();
				list.add(feeVo);
				map.put("error", list);
				return map;
			}
			String recipeDocCode = feeVo.getRecipeDocCode();//开立医师代码
			String medicalgroupId="";
			//获取医疗组信息
			BusinessMedicalGroupInfo groupInfo = inpatientInfoDAO.queryMedicalGroupInfo(recipeDocCode);
			if(groupInfo!=null){
				BusinessMedicalGroup medicalgroup = groupInfo.getBusinessMedicalgroup();
				if(medicalgroup!=null){
					medicalgroupId = medicalgroup.getId();
				}
			}
			feeVo.setMedicalteamCode(medicalgroupId);
			if("2".equals(feeVo.getTy())){//药品非药品标识:1-药品,2-非药品
				//查询非药品信息
				DrugUndrug drugUndrug=null;
				List<DrugUndrug> drugUndrugList = inpatientInfoDAO.queryNoDrugInfo(feeVo.getItemCode());//查询非药品信息
				if(drugUndrugList.size()>0){
					drugUndrug=drugUndrugList.get(0);
				}else{
					List<FeeInInterVo> list= new ArrayList<FeeInInterVo>();
					list.add(feeVo);
					map.put("error", list);
					return map;
				}
				if(feeVo.getItemFlag()!=null && feeVo.getItemFlag()==2){//物资
					//判断物资库存是否充足
					OutputInInterVO mat= new OutputInInterVO();
					mat.setApplyNum(feeVo.getQty());//申请数量
					mat.setTransType(1);//交易类型:1-正交易
					mat.setUndrugItemCode(itemCode);//非药品编码
					mat.setStorageCode(feeVo.getExecuteDeptCode());//出库科室
					Map<String, Object> judgeMap = matOutPutInInterService.judgeMat(mat);
					String s = judgeMap.get("resCode").toString();
					if("1".equals(s)){//库存不足
						List<FeeInInterVo> list= new ArrayList<>();
						feeVo.setItemCode(drugUndrug.getName());
						list.add(feeVo);
						map.put("resCode", list);
						return map;
					}
				}
				feeVo.setItemName(drugUndrug.getName());//非药品名称
				feeVo.setUndrugEquipmentno(drugUndrug.getUndrugEquipmentno());//设备号
				feeVo.setRetailprice(drugUndrug.getDefaultprice());//单价
				
				feeVo.setCurrentUnit(drugUndrug.getUnit());
				feeVo.setFeeCode(drugUndrug.getUndrugMinimumcost());//最小费用代码
				feeVo.setTotCost(feeVo.getQty()*drugUndrug.getDefaultprice());//金额=数量*默认价格
				FeeInInterVo interVo = getCosts(feeVo);//计算收费项目的金额(公费金额、自费金额、优惠金额等)
				newList.add(interVo);
			}else{//药品
				DrugInfo drugInfo = inpatientInfoDAO.getDrugInfoByCode(itemCode);//药品信息
				if(drugInfo!=null){
					double num=0;
					Double price = drugInfo.getDrugRetailprice();//价格
					String packagingunit = drugInfo.getDrugPackagingunit();//包装单位
					Integer packagingnum = drugInfo.getPackagingnum();//包装数量
//					if(packagingunit.equals(feeVo.getCurrentUnit())){//当前单位为包装单位
//						num=feeVo.getQty();
//					}else{//当前单位为最小单位
//						num=feeVo.getQty()/packagingnum;
//					}
					num=feeVo.getQty()/packagingnum;//目前所有调用接口的地方传过来的都是最小单位
					
					//判断药品状态是否可用及库存是否充足
					List<String> drugCodes= new ArrayList<>();//申请药品编码list
					drugCodes.add(itemCode);
					List<Double> applyNums= new ArrayList<>();//申请数量list
					applyNums.add(num);
					
					List<Integer> showFlags=new ArrayList<>();//申请数量list
					showFlags.add(1);
					Map<String, Object> valiuDrugMap = businessStockInfoInInterService.ynValiuDrug(feeVo.getExecuteDeptCode(), drugCodes, applyNums, showFlags, false, true, true);
					String flag = valiuDrugMap.get("valiuFlag").toString();
					if("0".equals(flag)){//库存不足
						List<FeeInInterVo> list= new ArrayList<>();
						feeVo.setItemCode(drugInfo.getName());
						list.add(feeVo);
						map.put("resCode", list);
						return map;
					}
					feeVo.setDrugPackagingunit(packagingunit);//包装单位
					feeVo.setPackagingnum(packagingnum);//包装数量
					feeVo.setRetailprice(price);//单价
					feeVo.setItemName(drugInfo.getName());//药品名称
					feeVo.setSpec(drugInfo.getSpec());//规格
					feeVo.setDrugType(drugInfo.getDrugType());//药品类型
					feeVo.setDrugNature(drugInfo.getDrugNature());//药品性质
					feeVo.setTotCost(num*price);
					feeVo.setFeeCode(drugInfo.getDrugMinimumcost());//最小费用代码
					FeeInInterVo interVo = getCosts(feeVo);
					newList.add(interVo);
				}else{
					List<FeeInInterVo> list= new ArrayList<>();
					list.add(feeVo);
					map.put("error", list);
					return map;
				}
			}
		}
		//对收费项目按患者的住院流水号进行分组
		for (FeeInInterVo vo : newList) {
			String inpatientNo = vo.getInpatientNo();//住院流水号
			List<FeeInInterVo> list = map.get(inpatientNo);
			if(list!=null){
				list.add(vo);
				//map.put(inpatientNo, list);这一步可以省略
			}else{
				list= new ArrayList<>();
				list.add(vo);
				map.put(inpatientNo, list);
			}
		}
		return map;
	}

	@Override
	public InpatientInfoNow queryByInpatientNot(String inpatientNo) {
		return inpatientInfoDAO.queryByInpatientNot(inpatientNo);
	}
	
	private Map<String,Object> getValidInfo(List<FeeInInterVo> newfeeVoList){
		Map<String,Object> map= new HashMap<>();
		//根据住院流水号获取患者信息
		List<InpatientInfoNow> patientList = inpatientInfoDAO.queryInpatientInfo(newfeeVoList.get(0).getInpatientNo());
		InpatientInfoNow infoNow = null;
		if(patientList!=null && patientList.size()>0){
			infoNow= patientList.get(0);
			Integer babyFlag = infoNow.getBabyFlag();//婴儿标志
			boolean flag=false;//判断标志,当患者为婴儿且 婴儿费用都记在母亲身上时 标志值为true
			if(babyFlag!=null && babyFlag==1){//患者是婴儿
				//获取系统参数'婴儿发生费用是否都记录在母亲身上'
				String parameterValue =parameterDAO.getParameterByCode("yefyisjlmqss");
				if("".equals(parameterValue)){
					parameterValue = "1";
				}
				//1-婴儿费用都记录在母亲身上
				if("1".equals(parameterValue)){
					String no = infoNow.getInpatientNo();
					String mInpatientNo = no.substring(0, 4)+"00"+no.substring(6);//婴儿母亲的住院流水号
					List<InpatientInfoNow> list = inpatientInfoDAO.queryInpatientInfo(mInpatientNo);
					if(list==null||list.size()==0){
						map.put("resCode", "error");
						map.put("resMsg", "婴儿患者母亲信息不存在，不能进行收费操作！");		
						return map;
					}
					infoNow = list.get(0);
					flag=true;
				}
			}
			Integer stopAcount = infoNow.getStopAcount();//账户是否关闭
			if(stopAcount!=null && stopAcount==1){
				map.put("resCode", "error");
				if(flag){
					map.put("resMsg", "婴儿患者母亲账户关闭，不能进行收费操作！");	
				}else{
					map.put("resMsg", "患者账户关闭，不能进行收费操作！");	
				}
				return map;
			}
			String inState = infoNow.getInState();//状态
			if("O".equals(inState)||"N".equals(inState)){
				map.put("resCode", "error");
				if(flag){
					map.put("resMsg", "婴儿患者母亲非在院状态，请先进行召回操作！");
				}else{
					map.put("resMsg", "患者正在出院结算或已经无费退院，不能进行收费操作！");	
				}
				return map;
			}
			
		}else{
			map.put("resCode", "error");
			map.put("resMsg", "未找到患者相关信息！");	
			return map;
		}
		for (FeeInInterVo vo : newfeeVoList) {
			vo.setInpatient(infoNow);
			vo.setPactCode(infoNow.getPactCode());//合同单位
			vo.setPaykindCode(infoNow.getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
			vo.setNurseCellCode(infoNow.getNurseCellCode());//护士站代码
			vo.setNurseCellName(infoNow.getNurseCellName());//护士站名称
		}
		map.put("resCode", "ok");
		map.put("resMsg", newfeeVoList);
		return map;
	}
	
	/**
	 * 计算收费项目的金额(公费金额、自费金额、优惠金额等)
	 * @param feeInVo
	 * @return
	 */
	public FeeInInterVo getCosts(FeeInInterVo fee){
		//查询自负比例
		List<InsuranceSiitem> insurList= inpatientInfoDAO.queryInsuranceSiitem(fee.getItemCode());
		//查询合同单位维护表-合同单位比例
		List<BusinessContractunit> contractunitList = inpatientInfoDAO.queryContractunit(fee.getPactCode());
		double pubRatio = 0;
		if(contractunitList!=null&& contractunitList.size()>0&&contractunitList.get(0).getPubRatio()!=null){
			pubRatio=contractunitList.get(0).getPubRatio();//公费比例\报销比例
		}	
		String itemGrade="";//医保目录等级 1 甲类(统筹全部支付) 2 乙类(准予部分支付) 3 自费
		if(insurList!=null&&insurList.size()>0&&insurList.get(0).getItemGrade()!=null){
			itemGrade = insurList.get(0).getItemGrade();
		}
		double rate=0;//自付比例
		if(insurList!=null&&insurList.size()>0&&insurList.get(0).getRate()!=null){
			rate = insurList.get(0).getRate();
		}
		double ecoCost = 0;//优惠金额 
		double pubCost = 0;//公费金额
		double ownCost = 0;//自费金额
		double totCost = fee.getTotCost();//总金额
		if(fee.getEcoCost()!=null){
			ecoCost = fee.getEcoCost();
		}
		if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
			pubCost = (totCost-ecoCost)*pubRatio;
			ownCost = totCost-ecoCost-pubCost;
		}
		if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
			pubCost = (totCost-ecoCost)*(1-rate)*pubRatio;
			ownCost = totCost-ecoCost-pubCost;
		}
		if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
			ownCost = totCost-ecoCost;
		}
		fee.setPubCost(pubCost);//公费金额
		fee.setOwnCost(ownCost);//自费金额
		
		return fee;
	}
	
	/**
	 * 获取药品信息
	 * @param feeInVo
	 * @return
	 */
 	private InpatientMedicineListNow getMedicineList(FeeInInterVo feeInVo){
		InpatientMedicineListNow inpatientMedicineList = new InpatientMedicineListNow();//药品明细
		
		String itemCode =feeInVo.getItemCode();//药品代码
		String itemName = feeInVo.getItemName();//药品名称
		double retailprice = feeInVo.getRetailprice();//单价
		String drugPackagingunit = feeInVo.getDrugPackagingunit();//包装单位
		int packagingnum = feeInVo.getPackagingnum();//包装数量
		String spec = feeInVo.getSpec();//规格	
		String drugType = feeInVo.getDrugType();//药品类别
		String drugNature = feeInVo.getDrugNature();//药品性质
		
		inpatientMedicineList.setId(null);
		inpatientMedicineList.setRecipeNo(feeInVo.getRecipeNo());//处方号
		inpatientMedicineList.setSequenceNo(feeInVo.getSequenceNo());//处方内项目流水号
		inpatientMedicineList.setTransType(1);//交易类型,1正交易，2反交易    
		inpatientMedicineList.setInpatientNo(feeInVo.getInpatientNo());//住院流水号 
		inpatientMedicineList.setName(feeInVo.getInpatient().getPatientName());//患者姓名
		inpatientMedicineList.setPaykindCode(feeInVo.getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干---暂时都按自费？？？？？   
		inpatientMedicineList.setPactCode(feeInVo.getPactCode());//合同单位 
		inpatientMedicineList.setInhosDeptCode(feeInVo.getInpatient().getDeptCode());//在院科室代码   
		inpatientMedicineList.setInhosDeptname(feeInVo.getInpatient().getDeptName());//在院科室代码  
		InpatientInfoNow inpatientInfoSerch = new InpatientInfoNow();
		inpatientInfoSerch.setInpatientNo(feeInVo.getInpatientNo());
		inpatientMedicineList.setNurseCellCode(feeInVo.getNurseCellCode());//护士站代码  
		inpatientMedicineList.setNurseCellName(feeInVo.getNurseCellName());//护士站名称 
		inpatientMedicineList.setRecipeDeptCode(feeInVo.getExecuteDeptCode());//开立科室代码
		inpatientMedicineList.setRecipeDeptname(deptInInterDAO.getDeptCode(feeInVo.getExecuteDeptCode()).getDeptName());//开立科室名称
		inpatientMedicineList.setExecuteDeptCode(feeInVo.getExecuteDeptCode());//执行科室代码 
		inpatientMedicineList.setExecuteDeptname(feeInVo.getExecuteDeptName());//执行科室名称   
		inpatientMedicineList.setMedicineDeptcode(feeInVo.getMedicineDeptcode());//取药科室代码???
		inpatientMedicineList.setRecipeDocCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//开立医师代码 
		inpatientMedicineList.setRecipeDocname(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//开立医师名称
		inpatientMedicineList.setDrugCode(itemCode);//药品编码 
		inpatientMedicineList.setFeeCode(feeInVo.getFeeCode());//最小费用代码  
		inpatientMedicineList.setCenterCode(feeInVo.getCenterCode());//医疗中心项目代码?????
		inpatientMedicineList.setDrug_name(itemName);//药品名称   
		inpatientMedicineList.setSpecs(spec);//规格
		inpatientMedicineList.setDrugType(drugType);//药品类别   
		inpatientMedicineList.setDrugQuality(drugNature);// 药品性质
		inpatientMedicineList.setHomeMadeFlag(feeInVo.getHomeMadeFlag());//自制标识  
		inpatientMedicineList.setUnitPrice(retailprice);// 单价   
		inpatientMedicineList.setCurrentUnit(feeInVo.getCurrentUnit());//当前单位  
		inpatientMedicineList.setPackQty(packagingnum);//包装数 
		inpatientMedicineList.setQty(feeInVo.getQty());//数量 
		inpatientMedicineList.setDays(feeInVo.getDays());//天数							
		inpatientMedicineList.setTotCost(feeInVo.getTotCost());// 费用金额	
		inpatientMedicineList.setOwnCost(feeInVo.getOwnCost());// 自费金额
		inpatientMedicineList.setPayCost(feeInVo.getOwnCost());// 自付金额
		inpatientMedicineList.setPubCost(feeInVo.getPubCost());// 公费金额
		inpatientMedicineList.setEcoCost(feeInVo.getEcoCost());// 优惠金额 
		inpatientMedicineList.setUpdateSequenceno(feeInVo.getUpdateSequenceno());//更新库存的流水号  
		inpatientMedicineList.setSenddrugSequence(feeInVo.getSenddrugSequence());//发药单序列号
		inpatientMedicineList.setSenddrugFlag(feeInVo.getSenddrugFlag());// 发药状态（0 划价 2摆药 1批费）
		inpatientMedicineList.setBabyFlag(feeInVo.getBabyFlag());//是否婴儿用药 0 不是 1 是     
		inpatientMedicineList.setJzqjFlag(feeInVo.getJzqjFlag());// 急诊抢救标志 
		inpatientMedicineList.setBroughtFlag(feeInVo.getBroughtFlag());//出院带药标记 0 否 1 是(Change as OrderType)
//		inpatientMedicineList.setExtFlag(null);//扩展标志(公费患者是否使用了自费的项目0否,1是)
//		inpatientMedicineList.setInvoiceNo(null);// 结算发票号-不涉及结算
		inpatientMedicineList.setBalanceNo(feeInVo.getBalanceNo());// 结算序号  
		inpatientMedicineList.setBalanceState(0);//结算状态
		inpatientMedicineList.setNobackNum(feeInVo.getNobackNum());//可退数量 
//		inpatientMedicineList.setExtCode(null);//扩展代码(中山一：保存退费原记录的处方号) 
//		inpatientMedicineList.setExtOpercode(null);//扩展操作员
//		inpatientMedicineList.setExtDate(null);// 扩展日期 
		inpatientMedicineList.setApprno(feeInVo.getApprno());// 审批号(中山一：退费时保存退费申请单号)
		 
		inpatientMedicineList.setExecOpercode(feeInVo.getExecOpercode());//执行人代码  
		inpatientMedicineList.setExecDate(feeInVo.getExecDate());// 执行日期
		inpatientMedicineList.setSenddrugOpercode(feeInVo.getSenddrugOpercode());// 发药人
		inpatientMedicineList.setSenddrugDate(feeInVo.getSenddrugDate());//发药日期
		inpatientMedicineList.setCheckOpercode(feeInVo.getCheckOpercode());//审核人   
		inpatientMedicineList.setCheckNo(feeInVo.getCheckNo());//审核序号  
		inpatientMedicineList.setMoOrder(feeInVo.getMoOrder());//医嘱流水号 
		inpatientMedicineList.setMoExecSqn(feeInVo.getMoExecSqn());//医嘱执行单流水号 
		inpatientMedicineList.setFeeRate(feeInVo.getFeeRate());//收费比率 
		
		inpatientMedicineList.setUploadFlag(feeInVo.getUploadFlag());// 上传标志  
		inpatientMedicineList.setExtFlag2(feeInVo.getExtFlag2());//扩展标志2(收费方式0住院处直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) 
		inpatientMedicineList.setExtFlag1(0);// 扩展标志1 
		inpatientMedicineList.setExtFlag3(feeInVo.getMinunitFlag());//聊城市医保新增(记录凭单号) 现在存的是 开医嘱时药品的单位(1包装单位  2最小单位) 	
		inpatientMedicineList.setMedicalteamCode(feeInVo.getMedicalteamCode());//住院药品明细表-医疗组
								
		inpatientMedicineList.setOperationno(feeInVo.getOperationno());//手术编码   
		inpatientMedicineList.setTransactionSequenceNumber(null);//医保交易流水号
		inpatientMedicineList.setSiTransactionDatetime(null);//医保交易时间
		inpatientMedicineList.setHisRecipeNo(null);//HIS处方号  
		inpatientMedicineList.setSiRecipeNo(null);//医保处方号   
		inpatientMedicineList.setHisCancelRecipeNo(null);//HIS原处方号
		inpatientMedicineList.setSiCancelRecipeNo(null);//医保原处方号
		
		
		return inpatientMedicineList;
	}
	
	/**
	 * 获取非药品信息
	 * @param vo
	 * @return
	 */
	private InpatientItemListNow getItemList(FeeInInterVo vo){
		InpatientItemListNow inpatientItemList = new InpatientItemListNow();
		double num = vo.getQty();	
		String itemCode =vo.getItemCode();//非药品代码
		String itemName = vo.getItemName();//非药品名称
		double defaultprice = vo.getRetailprice();//单价
		String undrugEquipmentno = vo.getUndrugEquipmentno();//设备号
		inpatientItemList.setId(null);
		inpatientItemList.setRecipeNo(vo.getRecipeNo());//处方号
		inpatientItemList.setSequenceNo(vo.getSequenceNo());//处方内项目流水号
		inpatientItemList.setTranstype(1);//交易类型,1正交易，2反交易    
		inpatientItemList.setInpatientNo(vo.getInpatientNo());//住院流水号 
		inpatientItemList.setName(vo.getInpatient().getPatientName());//患者姓名
		//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干---暂时都按自费？？？？？   
		inpatientItemList.setPaykindCode(vo.getPaykindCode());
		inpatientItemList.setPactCode(vo.getPactCode());//合同单位
		inpatientItemList.setUpdateSequenceno(vo.getUpdateSequenceno());//更新库存的流水号 
		inpatientItemList.setInhosDeptcode(vo.getInpatient().getDeptCode());//在院科室代码   
		inpatientItemList.setInhosDeptname(vo.getInpatient().getDeptName());//在院科室名称
		InpatientInfoNow inpatientInfoSerch = new InpatientInfoNow();
		inpatientInfoSerch.setInpatientNo(vo.getInpatientNo());
		inpatientItemList.setNurseCellCode(vo.getNurseCellCode());//护士站代码  
		inpatientItemList.setNurseCellName(vo.getNurseCellName());//护士站名称  
		inpatientItemList.setRecipeDeptcode(vo.getExecuteDeptCode());//开立科室代码
		inpatientItemList.setRecipeDeptname(deptInInterDAO.getDeptCode(vo.getExecuteDeptCode()).getDeptName());//开立科室名称
		inpatientItemList.setExecuteDeptcode(vo.getExecuteDeptCode());//执行科室代码   
		inpatientItemList.setExecuteDeptname(vo.getExecuteDeptName());//执行科室名称
		inpatientItemList.setStockDeptcode(null);//扣库科室代码???
		inpatientItemList.setRecipeDoccode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//开立医师代码 
		inpatientItemList.setRecipeDocname(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//开立医师名称 
		inpatientItemList.setItemCode(itemCode);//项目代码 
		inpatientItemList.setFeeCode(vo.getFeeCode());//最小费用代码  
		inpatientItemList.setFeeName(innerCodeDao.getDictionaryByCode("drugMinimumcost",vo.getFeeCode()).getName());//最小费用名称
		inpatientItemList.setCenterCode(vo.getCenterCode());//医疗中心项目代码?????
		inpatientItemList.setCenterName(null);//医疗中心项目名称?????
		inpatientItemList.setItemName(itemName);//项目名称  
		inpatientItemList.setUnitPrice(defaultprice);//单价   
		inpatientItemList.setQty(vo.getQty());//数量 
		inpatientItemList.setCurrentUnit(vo.getCurrentUnit());//当前单位  
		inpatientItemList.setPackageCode(vo.getPackageCode());//组套代码
		inpatientItemList.setPackageName(vo.getPackageName());//组套名称 	
		inpatientItemList.setTotCost(vo.getTotCost());// 费用金额						
		inpatientItemList.setOwnCost(vo.getOwnCost());// 自费金额
		inpatientItemList.setPayCost(vo.getOwnCost());// 自付金额
		inpatientItemList.setPubCost(vo.getPubCost());// 公费金额
		inpatientItemList.setEcoCost(vo.getEcoCost());// 优惠金额 
		inpatientItemList.setSendmatSequence(vo.getSenddrugSequence());//出库单序列号
		inpatientItemList.setSendFlag(vo.getSenddrugFlag());//发放状态（0 划价 2发放（执行） 1 批费）
		inpatientItemList.setBabyFlag(vo.getBabyFlag());//是否婴儿用药 0 不是 1 是     
		inpatientItemList.setJzqjFlag(vo.getJzqjFlag());// 急诊抢救标志 
		inpatientItemList.setBroughtFlag(vo.getBroughtFlag());//出院带药标记 0 否 1 是(Change as OrderType)
//		inpatientItemList.setExtFlag(null);//扩展标志(公费患者是否使用了自费的项目0否,1是)
//		inpatientItemList.setInvoiceNo(null);// 结算发票号-不涉及结算
		inpatientItemList.setBalanceNo(vo.getBalanceNo());// 结算序号  
		inpatientItemList.setBalanceState(0);//结算状态
		inpatientItemList.setNobackNum(vo.getNobackNum());//可退数量 
//		inpatientItemList.setExtCode(null);//扩展代码(中山一：保存退费原记录的处方号) 
//		inpatientItemList.setExtOpercode(null);//扩展操作员
//		inpatientItemList.setExtDate(null);// 扩展日期 
		inpatientItemList.setApprno(vo.getApprno());// 审批号(中山一：退费时保存退费申请单号)
		inpatientItemList.setConfirmNum(vo.getConfirmNum());//已确认数
		inpatientItemList.setMachineNo(undrugEquipmentno);// 设备号
		inpatientItemList.setExecOpercode(vo.getExecOpercode());//执行人代码  
		inpatientItemList.setExecDate(vo.getExecDate());// 执行日期
		inpatientItemList.setSendOpercode(vo.getSenddrugOpercode());//发放人  						
		
		inpatientItemList.setSendDate(vo.getSenddrugDate());//发放日期						
		inpatientItemList.setCheckOpercode(vo.getCheckOpercode());//审核人   
		inpatientItemList.setCheckNo(vo.getCheckNo());//审核序号  
		inpatientItemList.setMoOrder(vo.getMoOrder());//医嘱流水号 
		inpatientItemList.setMoExecSqn(vo.getMoExecSqn());//医嘱执行单流水号 
		inpatientItemList.setFeeRate(vo.getFeeRate());//收费比率 
		
		inpatientItemList.setUploadFlag(vo.getUploadFlag());// 上传标志  
		inpatientItemList.setExtFlag2(vo.getExtFlag2());//扩展标志2(收费方式0住院处直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) 
		inpatientItemList.setExtFlag1(0);// 扩展标志1 
		inpatientItemList.setExtFlag3(null);// 聊城市医保新增(记录凭单号) 	
		inpatientItemList.setItemFlag(vo.getItemFlag());// 0非药品 2物资   
		inpatientItemList.setMedicalteamCode(vo.getMedicalteamCode());//医疗组
		inpatientItemList.setOperationno(vo.getOperationno());//手术编码   
		inpatientItemList.setTransactionSequenceNumber(null);//医保交易流水号
		inpatientItemList.setSiTransactionDatetime(null);//医保交易时间
		inpatientItemList.setHisRecipeNo(null);//HIS处方号  
		inpatientItemList.setSiRecipeNo(null);//医保处方号   
		inpatientItemList.setHisCancelRecipeNo(null);//HIS原处方号
		inpatientItemList.setSiCancelRecipeNo(null);//医保原处方号
		
		return inpatientItemList;
	}
	
	/**
	 * 获取费用汇总信息
	 * @param feeInVo
	 * @return
	 */
	private InpatientFeeInfoNow getFeeInfo(FeeInInterVo feeInVo){
		
		InpatientFeeInfoNow inpatientFeeInfo = new InpatientFeeInfoNow();//费用汇总	
		
		inpatientFeeInfo.setRecipeNo(feeInVo.getRecipeNo());//处方号
		inpatientFeeInfo.setFeeCode(feeInVo.getFeeCode());//最小费用代码  
		inpatientFeeInfo.setTransType(1);//交易类型,1正交易，2反交易    
		inpatientFeeInfo.setInpatientNo(feeInVo.getInpatientNo());//住院流水号 
		inpatientFeeInfo.setName(feeInVo.getInpatient().getPatientName());//患者姓名
		inpatientFeeInfo.setPaykindCode(feeInVo.getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干---暂时都按自费？？？？？   
		inpatientFeeInfo.setPactCode(feeInVo.getPactCode());//合同单位 
		inpatientFeeInfo.setNurseCellCode(feeInVo.getNurseCellCode());//护士站代码 
		inpatientFeeInfo.setInhosDeptcode(feeInVo.getInpatient().getDeptCode());//在院科室代码   
		
		inpatientFeeInfo.setNurseCellCode(feeInVo.getNurseCellCode());//护士站代码  
		inpatientFeeInfo.setNurseCellName(feeInVo.getNurseCellName());//护士站名称  
		if(feeInVo.getRecipeDeptCode()!=null){
			inpatientFeeInfo.setRecipeDeptcode(feeInVo.getRecipeDeptCode());//开立科室代码
		}
		inpatientFeeInfo.setExecuteDeptcode(feeInVo.getExecuteDeptCode());//执行科室代码  
		String stockDept=feeInVo.getMedicineDeptcode();
		if(stockDept!=null){
			inpatientFeeInfo.setStockDeptcode(stockDept);//扣库科室代码
			inpatientFeeInfo.setStockDeptname(name.get(stockDept));//扣库科室名称
		}
		if(feeInVo.getRecipeDocCode()!=null){
			inpatientFeeInfo.setRecipeDoccode(feeInVo.getRecipeDocCode());//开立医师代码
		}
//		inpatientFeeInfo.setBalanceOpercode(null);//结算人代码  
//		inpatientFeeInfo.setBalanceDate(null);//结算时间
//		inpatientFeeInfo.setInvoiceNo(null);//结算发票号    
		inpatientFeeInfo.setBalanceNo(feeInVo.getBalanceNo());//结算序号    
		inpatientFeeInfo.setBalanceState(0);//结算标志 0:未结算；1:已结算 2:已结转   	
		if(StringUtils.isNotBlank(feeInVo.getCheckNo())){
			inpatientFeeInfo.setCheckNo(feeInVo.getCheckNo());//审核序号  
		}
		if(feeInVo.getBabyFlag() != null){
			inpatientFeeInfo.setBabyFlag(feeInVo.getBabyFlag());//婴儿标记1：是，0：否(初始值为0)    
		}							
		inpatientFeeInfo.setExtFlag1("0");// 扩展标志1 
//		inpatientFeeInfo.setExtFlag(null);//扩展标志(公费患者是否使用了自费的项目0否,1是) 
//		inpatientFeeInfo.setExtCode(null);//扩展代码(中山一：保存退费原记录的处方号)
//		inpatientFeeInfo.setExtDate(null);//扩展日期
//		inpatientFeeInfo.setExtOpercode(null);//扩展操作员  
//		inpatientFeeInfo.setExtFlag2(null);//扩展标志2
		
		return inpatientFeeInfo;
	}

	@Override
	public List<InpatientInfoNow> queryNurseChargeByMedicalNo(
			String inpatientNo, String deptId) {
		return inpatientInfoDAO.queryNurseChargeInpinfo(inpatientNo,deptId);
	}

	@Override
	public String queryMedicalrecordId(String IdcardOrRe) {
		return inpatientInfoDAO.queryMedicalrecordId(IdcardOrRe);
	}

	/**  
	 * 
	 * 根据sequence,获取applyNumber
	 * @Author:zxl
	 * @CreateDate: 2017年7月4日 下午5:45:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月4日 下午5:45:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:drugAppLyout 药品出库申请序列
	 * @throws:
	 * @return: applyNumber
	 *
	 */
	@Override
	public String getSequece(String drugAppLyout) {
		return inpatientInfoDAO.getSequece(drugAppLyout);
	}

	/**  
	 * 
	 * 保存药品出库申请信息
	 * @Author:zxl
	 * @CreateDate: 2017年7月4日 下午5:45:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月4日 下午5:45:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:applyout 药品出库申请实体
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void save(DrugApplyout applyout) {
		inpatientInfoDAO.save(applyout);
		
	}
}
