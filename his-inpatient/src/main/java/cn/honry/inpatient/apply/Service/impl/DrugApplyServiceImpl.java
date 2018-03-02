package cn.honry.inpatient.apply.Service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugPreoutstore;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientStoMsg;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.MatBaseinfo;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.base.bean.model.MatUndrugCompare;
import cn.honry.base.bean.model.MaterialsCancelmetlist;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.drug.applyout.dao.ApplyoutInInterDAO;
import cn.honry.inner.drug.drugInfo.service.DrugInfoInInerService;
import cn.honry.inner.drug.outstore.service.OutStoreInInterService;
import cn.honry.inner.inpatient.feeInfo.dao.InpatientFeeInfoInInterDAO;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.inpatient.apply.Service.ConfirmService;
import cn.honry.inpatient.apply.Service.DrugApplyService;
import cn.honry.inpatient.apply.dao.DrugApplyDAO;
import cn.honry.inpatient.apply.dao.InpatientStoMsgDAO;
import cn.honry.inpatient.apply.dao.MatOutputDao;
import cn.honry.inpatient.apply.dao.ConfirmDAO;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.inpatient.docAdvManage.dao.DocAdvManageDAO;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * 申请退费业务层
 * @author  lyy
 * @createDate： 2016年1月6日 下午2:21:48 
 * @modifier lyy
 * @modifyDate：2016年1月6日 下午2:21:48  
 * @modifyRmk：  
 * @version 1.0
 */
@Service("drugApplyService")
@Transactional
@SuppressWarnings({ "all" })
public class DrugApplyServiceImpl implements DrugApplyService  {
	
	/***
	 * 注入本类退费申请DAO
	 */
	@Autowired
	@Qualifier("drugApplyDao")
	private DrugApplyDAO drugApplyDao;  
	
	/***
	 * 注入药品 druginfo的service
	 * 调用接口
	 */
	@Autowired
	@Qualifier(value = "drugInfoInInerService")
	private DrugInfoInInerService drugInfoInInterService;
	public void setDrugInfoInInterService(DrugInfoInInerService drugInfoInInterService) {
		this.drugInfoInInterService = drugInfoInInterService;
	}
	/***
	 * 注入系统参数的service
	 * 调用接口
	 */
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}
	/***
	 * 直接退费service
	 */
	@Autowired
	@Qualifier("confirmService")
	private ConfirmService confirmService;
	public void setConfirmService(ConfirmService confirmService) {
		this.confirmService = confirmService;
	}
	
	/*K 库存预扣**/
	@Autowired
	@Qualifier(value = "outstoreInInterService")
	private OutStoreInInterService outstoreService;
	
	/***
	 * 出库表
	 */
	@Autowired
	@Qualifier("applyoutInInterDAO")
	private ApplyoutInInterDAO applyoutDAO;  
	
	/***
	 * 物资接口
	 */
	@Autowired
	@Qualifier("matOutPutInInterService")
	private MatOutPutInInterService matOutPutService;  
	
	
	/***
	 * 参数表
	 */
	@Autowired
	@Qualifier(value = "keyvalueInInterDAO")
	private KeyvalueInInterDAO keyvalueDAO;
	

	@Autowired
	@Qualifier("confirmDao")
	private ConfirmDAO confirmDao;
	
	/***
	 * 摆药单通知
	 */
	@Autowired
	@Qualifier("inpatientStoMsgDAO")
	private InpatientStoMsgDAO inpatientStoMsgDAO;   
	
	
	//获得参数值
	@Autowired
	@Qualifier(value = "docAdvManageDAO")
	private DocAdvManageDAO docAdvManageDAO;   
	
	/***
	 * 物资出库
	 */
	@Autowired
	@Qualifier(value="matOutputDao")
	private MatOutputDao matOutputDao;    
	
	
	@Autowired
	@Qualifier(value="inpatientFeeInfoInInterDAO")
	private InpatientFeeInfoInInterDAO feeInfoDao;
	
	
	@Autowired
	@Qualifier(value="deptInInterDAO")
	private DeptInInterDAO departmentDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}
	
	@Override
	public InpatientCancelitemNow get(String id) {
		return drugApplyDao.get(id);
	}
	
	@Override
	public void saveOrUpdate(InpatientCancelitemNow entity) {
		
	}
	
	@Override
	public List<TreeJson> treePatient(String deptId) throws Exception {
		List<InpatientInfoNow> listInfo=new ArrayList<InpatientInfoNow>();
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
			//加入科室诊室间关系树的根节点
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			pTreeJson.setText("患者信息");
			treeJsonList.add(pTreeJson);
			listInfo=drugApplyDao.treeQuery(deptId);
		if(listInfo != null && listInfo.size() > 0){
			for (InpatientInfoNow info : listInfo) {
				TreeJson treeJson = new TreeJson();
				treeJson.setId(info.getId());
				treeJson.setText(info.getPatientName());
				Map<String,String> attributes=new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("medicalrecordId",info.getMedicalrecordId());
				attributes.put("inpatientNo",info.getInpatientNo());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}		
		return TreeJson.formatTree(treeJsonList);
	}
	
	@Override
	public InpatientInfoNow patientBasicData(String inpatientNo) {
		return drugApplyDao.patientBasicData(inpatientNo);
	}

	@Override
	public String findInpatientNo(String medicalrecordId) {
		if(StringUtils.isEmpty(medicalrecordId)){
			return "";
		}else {
			return drugApplyDao.findInpatientNo(medicalrecordId);
		}
	}
	
	@Override
	public List<ApplyVo> getPage(ApplyVo applySerch) {
		List<ApplyVo> list = drugApplyDao.getPage(applySerch);
		for(ApplyVo vo : list){
			if("1".equals(vo.getExtFlag3())){//包装单位
				String packagingunit = drugInfoInInterService.companyCode(vo.getDrugCode());
				vo.setShowUnit(packagingunit);
			}else if("2".equals(vo.getExtFlag3())){
				vo.setShowUnit(vo.getUnit());
			}
		}
		return list;
	}
	
	@Override
	public List<ApplyVo> getPageBack(ApplyVo entity) {
		return drugApplyDao.getPageBack(entity);
	}
	
	@Override
	public List<ApplyVo> getPageNotDrug(ApplyVo entity) {
		List<ApplyVo> list = drugApplyDao.getPageNotDrug(entity);
		for(ApplyVo vo : list){
			//非药品是否为物资，只有物资才存在库存
			if("2".equals(vo.getItemFlag())){
				String stockNo = drugApplyDao.findStockNo(vo.getUpdateSequenceno());
				vo.setStockNo(stockNo);
			}
		}
		return list;
	}
	
	@Override
	public List<ApplyVo> getPageDrugBack(ApplyVo entity) {
		return drugApplyDao.getPageDrugBack(entity);
	}
	
	
	/***
	 * 从药品和非药品申请列表中获取退费申请单号
	 * @Title: obtainApplyNo 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param list0 ： 药品申请
	 * @param list1 ： 非药品申请
	 * @return String ： 申请单据号
	 * @version 1.0
	 */
	public String obtainApplyNo(List<ApplyVo> list0,List<ApplyVo> list1){
		/***
		 * 申请单序号是否存在
		 * 存在： 则直接使用
		 * 不存在：读取序列
		 */
		String billCode = "";
		for(ApplyVo vo : list0){
			if(vo.getApplyNo() != null){
				InpatientCancelitemNow model = drugApplyDao.getById(vo.getApplyNo());
				billCode = model.getBillCode();
				return billCode;
			}
		}
		
		if("".equals(billCode)){
			for(ApplyVo vo : list1){
				if(vo.getApplyNo() != null){
					InpatientCancelitemNow model = drugApplyDao.getById(vo.getApplyNo());
					billCode = model.getBillCode();
					return billCode;
				}
			}
		}
		
		if("".equals(billCode)){
			billCode = drugApplyDao.getSeqByNameorNumNew("SEQ_INPATIENT_CAN_BILLCODE", 14);
		}
		return billCode;
	}
	
	/**
	 * 对于已摆药的信息，需要生成退药申请单
	 * @Title: obtainBackDrug 
	 * @author  WFJ
	 * @createDate ：2016年4月28日
	 * @param vo
	 * @return DrugApplyout
	 * @version 1.0
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public DrugApplyoutNow obtainBackDrug(ApplyVo vo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//当前登录信息
		String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();  
		Date date = DateUtils.getCurrentTime();
		
		DrugApplyoutNow model0 = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
		DrugApplyoutNow model1 = new DrugApplyoutNow();
		//model0 ：出库申请表中已摆药的记录，model1： 将要发起的住院退药申请
			if(model0!=null){
				PropertyUtils.copyProperties(model1, model0);

			}
		model1.setId(null);
		//申请流水号是读取序列
		int applyNumber = Integer.valueOf(drugApplyDao.getSeqByNameorNum("SEQ_DRUG_APPLYOUT", 12).toString());
		model1.setApplyNumber(applyNumber);
		//申请部门为患者所在科室
		if(model0!=null){
			model1.setDeptCode(model0.getPatientDept());
			SysDepartment dept=departmentDAO.getByCode(model0.getPatientDept());
			model1.setDeptName(dept.getDeptName());
		}
		
		model1.setOpType(5);
		//申请单号是读取keyValue表
		String yearLast = new SimpleDateFormat("yyMM").format(new Date());
		if(model0!=null){
			int value = keyvalueDAO.getVal(model0.getPatientDept(),"内部入库申请单号",yearLast);
			String applyBillcode = "0" + yearLast + value;//组成内部入库申请单号
			model1.setApplyBillcode(applyBillcode);
		}
		//申请人状态
		model1.setApplyOpercode(userid);
		model1.setApplyDate(date);
		//申请状态
		model1.setApplyState(0);
		//申请的出库量
		model1.setApplyNum(Double.valueOf(vo.getQuantity()));
		//付数
		model1.setDays(1);
		//是否预扣库存1是0否
		model1.setPreoutFlag(0);
		//收费状态：0未收费，1已收费
		model1.setChargeFlag(0);
		//医嘱发送类型（1集中发送，2临时发送，3全部）
		model1.setSendType(2);
		//摆药单分类设置为R退药单分类
		model1.setBillclassCode("R");
		//有效标记（1有效，0无效，2不摆药）
		model1.setValidState(1);
		/*操作状态*/
		model1.setCreateUser(userid);
		model1.setCreateDept(deptid);
		model1.setCreateTime(date);
		model1.setUpdateUser(userid);
		model1.setUpdateTime(date);
		return model1;
	}
	
	/***
	 * 根据药品明细表，生成退费申请记录
	 * @Title: obtainVoForMedicine 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param medicine0	: 根据药品id获取的药品明细实体
	 * @param vo	： 前台传输的退药数量
	 * @return InpatientCancelitem
	 * @version 1.0
	 * @throws Exception 
	 */
	public InpatientCancelitemNow obtainVoForMedicine(InpatientMedicineListNow medicine0,ApplyVo vo) throws Exception{
		InpatientCancelitemNow model = new InpatientCancelitemNow();
		//当前登录信息
		SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		Date date = DateUtils.getCurrentTime();
		//申请归属标识 1门诊/2住院
		model.setApplyFlag(2);
		//住院流水号
		model.setInpatientNo(medicine0.getInpatientNo());
		model.setName(medicine0.getName());
		//是否婴儿用药
		model.setBabyFlag(medicine0.getBabyFlag());
		//所在科室
		model.setDeptCode(medicine0.getInhosDeptCode());
		model.setDeptName(medicine0.getInhosDeptname());
		//所在病区
		model.setCellCode(medicine0.getNurseCellCode());
		model.setNurseCellName(medicine0.getNurseCellName());
		//药品非药品标识：1药品，2非药品
		model.setDrugFlag(1);
		//项目code
		model.setItemCode(medicine0.getDrugCode());
		//项目名称
		model.setItemName(medicine0.getDrug_name());
		//规格
		model.setSpecs(medicine0.getSpecs());
		//零售价
		model.setSalePrice(medicine0.getUnitPrice());
		//申请的退费数量
		model.setQuantity(vo.getQuantity()*1.0);
		//计价单位
		model.setPriceUnit(medicine0.getCurrentUnit());
		//执行科室
		model.setExecDpcd(medicine0.getExecuteDeptCode());
		model.setExecDpcdName(medicine0.getExecuteDeptname());
		model.setOperCode(user.getAccount());
		model.setOperDpcdName(user.getName());
		model.setOperDate(date);
		model.setOperDpcd(dept.getDeptCode());
		model.setOperDpcdName(dept.getDeptName());
		//处方号
		model.setRecipeNo(medicine0.getRecipeNo());
		//处方流水号
		model.setSequenceNo(medicine0.getSequenceNo());
		//退药确认标识
		model.setConfirmFlag(0);
		//退费确认标识
		model.setChargeFlag(0);
		if(vo.getExtFlag3()==null){
		}else{
			model.setExtFlag(Integer.valueOf(vo.getExtFlag3()));
		}
		model.setCreateUser(user.getAccount());
		model.setCreateDept(dept.getDeptCode());
		model.setCreateTime(date);
		model.setUpdateUser(user.getAccount());
		model.setUpdateTime(date);
		
		model.setHospitalId(HisParameters.CURRENTHOSPITALID);//当前医院id
		//获取当前科室信息
		String deptCode=dept.getDeptCode();//
		model.setAreaCode(inprePayDAO.getDeptArea(deptCode));//保存当前科室所在院区
		
		return model;
	}
	
	/***
	 * 应对部分退药，重新生成出库申请记录
	 * @Title: againObtainApplyout 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param vo
	 * @return DrugApplyout
	 * @version 1.0
	 */
	public DrugApplyoutNow againObtainApplyout(DrugApplyoutNow model0 ,ApplyVo vo){
		//当前登录信息	
		String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Date date = DateUtils.getCurrentTime();
		
		InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
		DrugApplyoutNow model1 = new DrugApplyoutNow();
		try {
			PropertyUtils.copyProperties(model1, model0);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		//根据sequence 获取申请流水号
		int applyNumber = Integer.parseInt(drugApplyDao.getDrugApplyoutSequece("SEQ_DRUG_APPLYOUT").toString());
		
		model1.setId(null);
		model1.setApplyNumber(applyNumber);
		//1 门诊发药 ， 2 内部入库,3 门诊退药，4 住院摆药 ,5住院退药
		model1.setOpType(4);
		//申请单号
		model1.setApplyOpercode(userid);
		model1.setApplyDate(date);
		model1.setApplyState(0);
		//由于事务和缓存原因，getNobackNum的值已被更改，不需要再进行计算
		model1.setApplyNum(medicine.getNobackNum());
		model1.setValidState(1);
		model1.setCreateUser(userid);
		model1.setCreateDept(deptid);
		model1.setCreateTime(date);
		model1.setUpdateUser(userid);
		model1.setUpdateTime(date);
		return model1;
	}
	
	/***
	 * 住院更新药品预扣库存
	 * @Title: BackingStore 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param applyout
	 * @param num
	 * @return void
	 * @version 1.0
	 */
	public void backingStore(DrugApplyoutNow applyout,Double num){
		/**是否预扣库存1是0否*/
		if("1".equals(applyout.getPreoutFlag())){
			//当前登录信息
			String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			Date date = DateUtils.getCurrentTime();
			
			DrugPreoutstore preoutstore = new DrugPreoutstore();
			//申请流水号？？？
			preoutstore.setApplyNumber(applyout.getApplyNumber().intValue());
			//发药部门编码
			preoutstore.setDrugDeptCode(applyout.getDeptCode());
			//出库申请分类 ????
			preoutstore.setClass3MeaningCode("0000000000000");
			//药品编码
			preoutstore.setDrugCode(applyout.getDrugCode());
			//药品商品名
			preoutstore.setTradeName(applyout.getDrugName());
			//规格
			preoutstore.setSpecs(applyout.getSpecs());
			//申请出库量(每付的总数量)
			preoutstore.setApplyNum(num);
			//付数（草药）
			preoutstore.setDays(1);
			//申请人编码
			preoutstore.setApplyOpercode(userid);
			//申请日期
			preoutstore.setApplyDate(date);
			//患者编号
			preoutstore.setPatientId(applyout.getPatientId());
			preoutstore.setCreateUser(userid);
			preoutstore.setCreateDept(deptid);
			preoutstore.setCreateTime(date);
			preoutstore.setUpdateUser(userid);
			preoutstore.setUpdateTime(date);
			drugApplyDao.update(preoutstore);
		}
	}
	
	/***
	 * 药品摆药通知处理
	 * 若和其他药品公用一个通知则不处理
	 * 否则作废摆药通知
	 * @Title: drugStoMsg 
	 * @author  WFJ
	 * @createDate ：2016年4月28日
	 * @param applyout : 住院药品摆药单
	 * @return void
	 * @version 1.0
	 */

	/***
	 * 根据非药品明细，产生病区退费申请物资信息
	 * @Title: obtainMatmetlist 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param itemList
	 * @return void
	 * @version 1.0
	 */
	public MaterialsCancelmetlist obtainMatmetlist(InpatientItemListNow itemList,ApplyVo vo){
		//当前登录信息
		String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//当前时间
		Date date = DateUtils.getCurrentTime();
		MaterialsCancelmetlist model = new MaterialsCancelmetlist();
		//出库流水号
		String applyNo = drugApplyDao.getSeqByNameorNum("SEQ_MAT_CANCEL_NO", 14);
		model.setApplyNo(applyNo);
		//出库单流水号
		model.setOutNo(itemList.getUpdateSequenceno());
		//库存流水号
		MatOutput matOutput  = drugApplyDao.findMatOPbyNO(itemList.getUpdateSequenceno());
		model.setStockNo(matOutput.getStockCode());
		model.setRecipeNo(itemList.getRecipeNo());
		model.setSequenceNo(itemList.getSequenceNo());
		model.setItemCode(itemList.getItemCode());
		model.setItemName(itemList.getItemName());
		//规格
		model.setSpecs(matOutput.getSpecs());
		//计量单位
		model.setStatUnit(itemList.getCurrentUnit());
		//零售价
		model.setSalePrice(itemList.getUnitPrice());
		model.setOutNum(vo.getQuantity());
		//确认标识（0申请，1退费，2作废）
		model.setCancelflag("1");
		model.setCreateUser(userid);
		model.setCreateDept(deptid);
		model.setCreateTime(date);
		model.setUpdateUser(userid);
		model.setUpdateTime(date);
		
		return model;
	}
	
	/***
	 * 根据住院非药品明细，产生病区退费申请
	 * @Title: obtainVoForItemlist 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param itemList
	 * @param vo
	 * @return InpatientCancelitem
	 * @version 1.0
	 * @throws Exception 
	 */
	public InpatientCancelitemNow obtainVoForItemlist(InpatientItemListNow itemList,ApplyVo vo) throws Exception{
		InpatientCancelitemNow model = new InpatientCancelitemNow();
		//当前登录信息
		SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		Date date = DateUtils.getCurrentTime();
		//申请归属标识 1门诊/2住院
		model.setApplyFlag(2);
		//住院流水号
		model.setInpatientNo(itemList.getInpatientNo());
		model.setName(itemList.getName());
		//是否婴儿用药
		model.setBabyFlag(itemList.getBabyFlag());
		//所在科室
		model.setDeptCode(itemList.getInhosDeptcode());
		model.setDeptName(itemList.getInhosDeptname());
		//所在病区
		model.setCellCode(itemList.getNurseCellCode());
		model.setNurseCellName(itemList.getNurseCellName());
		//药品非药品标识：1药品，2非药品
		model.setDrugFlag(2);
		//项目code
		model.setItemCode(itemList.getItemCode());
		//项目名称
		model.setItemName(itemList.getItemName());
		//零售价
		model.setSalePrice(itemList.getUnitPrice());
		//申请的退费数量
		model.setQuantity(vo.getQuantity()*1.0);
		//计价单位
		model.setPriceUnit(itemList.getCurrentUnit());
		//执行科室
		model.setExecDpcd(itemList.getExecuteDeptcode());
		model.setExecDpcdName(itemList.getExecuteDeptname());
		model.setOperCode(user.getAccount());
		model.setOperName(user.getName());
		model.setOperDate(date);
		model.setOperDpcd(dept.getDeptCode());
		model.setOperDpcdName(dept.getDeptName());
		//处方号
		model.setRecipeNo(itemList.getRecipeNo());
		//处方流水号
		model.setSequenceNo(itemList.getSequenceNo());
		//未退费标识
		model.setChargeFlag(0);
		model.setCreateUser(user.getAccount());
		model.setCreateDept(dept.getDeptCode());
		model.setCreateTime(date);
		model.setUpdateUser(user.getAccount());
		model.setUpdateTime(date);
		
		model.setHospitalId(HisParameters.CURRENTHOSPITALID);//当前医院id
		//获取当前科室信息
		String deptCode=dept.getDeptCode();//
		model.setAreaCode(inprePayDAO.getDeptArea(deptCode));//保存当前科室所在院区
		return model;
	}
	
	/***
	 * 对于要取消的申请 进行状态判断
	 * 对于已确认状态的申请不可取消
	 * @Title: applyState 
	 * @author  WFJ
	 * @createDate ：2016年4月28日
	 * @param list
	 * @return Map<String,Object>
	 * @version 1.0
	 */
	public Map<String,Object> applyState(List<InpatientCancelitemNow> list){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("resCode","1");
		map.put("resMes","success");
		
		for(InpatientCancelitemNow model : list){
			//该申请是否被处理
			if(model.getChargeFlag() != 0){
				map.put("resCode","-1");
				map.put("resMes","项目名称（"+model.getItemName()+"），已被处理，此操作不可进行！");
				break;
			}else{
				//药品进行是否退药判断
				if(1 == model.getDrugFlag()){
					//已完成退药的项目，不可取消
					if(1 == model.getConfirmFlag()){
						map.put("resCode","-2");
						map.put("resMes","已摆药项目（"+model.getItemName()+"），已完成实时退药，此操作不可进行！");
						break;
					}
				}
			}
		}
		return map;
	}
	
	/***
	 * 针对取消退费申请操作，重新生成出库申请
	 * @Title: obtainApplyout 
	 * @author  WFJ
	 * @createDate ：2016年5月7日
	 * @param oldapply	： 现在的出库申请记录
	 * @param model	： 要取消的退费申请
	 * @return DrugApplyout
	 * @version 1.0
	 */
	public DrugApplyoutNow obtainApplyout(DrugApplyoutNow oldapply,InpatientCancelitemNow model){
		//当前登录信息
		String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Date date = DateUtils.getCurrentTime();
		
		DrugApplyoutNow model1 = new DrugApplyoutNow();
		try {
			PropertyUtils.copyProperties(model1, oldapply);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		model1.setId(null);
		//申请流水号是读取序列
		int applyNumber = Integer.valueOf(drugApplyDao.getSeqByNameorNumNew("SEQ_DRUG_APPLYOUT", 12).toString());
		model1.setApplyNumber(applyNumber);
		//申请的出库量
		model1.setApplyNum(model1.getApplyNum() + model.getQuantity());
		//操作状态
		model1.setCreateUser(userid);
		model1.setCreateDept(deptid);
		model1.setCreateTime(date);
		model1.setUpdateUser(userid);
		model1.setUpdateTime(date);
		return model1;
	}
	
	
	@Override
	public List<ApplyVo> queryInpatientInfo(String medicalrecordId) throws Exception {
		return drugApplyDao.queryInpatientInfo(medicalrecordId);
	}
	
	
	public Map<String, Object> directSavePD(String medicalrecordId,List<ApplyVo> drugList, List<ApplyVo> notDrugList) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("resCode","0");
		map.put("resMsg","保存成功！");
		
		for(ApplyVo vo : drugList){
			if(StringUtils.isNotBlank(vo.getApplyNo())){
				InpatientCancelitemNow model = this.get(vo.getApplyNo());
				if(model.getChargeFlag() != 0){
					map.put("resCode","-1");
					map.put("resMes","项目名称（"+model.getItemName()+"），已被终端处理，此操作不可进行！");
					return map;
				}
			}
		}
		if("0".equals(map.get("resCode"))){
			for(ApplyVo vo : notDrugList){
				if(StringUtils.isNotBlank(vo.getApplyNo())){
					InpatientCancelitemNow model = this.get(vo.getApplyNo());
					if(model.getChargeFlag() != 0){
						map.put("resCode","-1");
						map.put("resMes","项目名称（"+model.getItemName()+"），已被终端处理，此操作不可进行！");
						return map;
					}
				}
			}
		}
		return map;
	}
	
/*-----------------------------------------------------	退费申请的操作 	--------------------------------------------------------------------------------------*/
	@Override
	public Map<String, Object> saveAdd(String medicalrecordId, String drugJson, String notDrugJson) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("resCode","0");
		map.put("resMsg","保存成功！");
		//当前登录信息
		String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Date date = DateUtils.getCurrentTime();
		
		List<ApplyVo> drugList = new Gson().fromJson(drugJson, new TypeToken<List<ApplyVo>>(){}.getType());
		List<ApplyVo> notDrugList = new Gson().fromJson(notDrugJson, new TypeToken<List<ApplyVo>>(){}.getType());

		String billCode = obtainApplyNo(drugList,notDrugList);
		
		//产生的退费申请集合
		List<InpatientCancelitemNow> list = new ArrayList<InpatientCancelitemNow>();
		//产生的出库申请记录集合
		List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
		//需要进行摆药通知处理的集合
		List<DrugApplyoutNow> listMsg = new ArrayList<DrugApplyoutNow>();
		
		for(ApplyVo vo : drugList){
			/***
			 * 药品是否已摆药
			 * 已摆药：产生退药申请,核准后产生退费申请
			 * 未摆药：产生退费申请
			 */
			if(2 == vo.getSenddrugFlag()){
				/***
				 * 在产生退药申请的时，修改药品明细的可退数量并还回预扣库存
				 * 若核准退药单，则产生退费申请单
				 * 若取消退药单，需要还回药品明细的可退数量并增加预扣库存
				 */
				DrugApplyoutNow applyout = obtainBackDrug(vo);
				listApplyout.add(applyout);
				//操作住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				//该操作是产生新的退药申请，故可退数量为此算法
				medicine.setNobackNum(medicine.getNobackNum() - vo.getQuantity());
				medicine.setUpdateUser(userid);
				medicine.setUpdateTime(date);
				drugApplyDao.updateMedicine(medicine);
				
				map.put("resCode","1");
				map.put("resMsg","申请成功，但申请的项目中包含已摆药信息，实时退药后，可显示所有信息。");
			}else{
				/***
				 * 申请是否存在
				 * 存在，则更新
				 * 不存在，则添加
				 */
				//住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				if(vo.getApplyNo() != null){
					InpatientCancelitemNow model = drugApplyDao.getById(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					//该操作是修改退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0-model.getQuantity()));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					model.setDrugFlag(1);
					model.setChargeFlag(0);
					list.add(model);
				}else{
					InpatientCancelitemNow model = obtainVoForMedicine(medicine, vo);
					model.setBillCode(billCode);
					model.setChargeFlag(0);
					model.setDrugFlag(1);
					model.setHospitalId(HisParameters.CURRENTHOSPITALID);
					model.setAreaCode(inprePayDAO.getDeptArea(deptid));
					list.add(model);
					
					//该操作是生成退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
				}
				/***
				 * 判断是否全退
				 * 部分退，作废出库申请后，将剩余的部分重新生成出库申请
				 * 全退，直接作废出库申请后，是否预扣库存，返还预扣数量
				 */
				DrugApplyoutNow applyout = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
				if(0 == medicine.getNobackNum()){
					//药品是否产生摆药通知
					String drugedBill=applyout.getDrugedBill();
					
					if(StringUtils.isNotEmpty(applyout.getDrugedBill())){
						listMsg.add(applyout);
					}
					//全部退药，作废出库申请
					applyout.setValidState(0);
					drugApplyDao.updateDrugApplyout(applyout);
					//返还药品库存预扣
					outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(),applyout.getApplyNum(), applyout.getShowFlag());
					
				}else{
					//在执行全退后，又进行数量的修改时，无有效的出库申请记录，此时需要进行该方法查询
					if(applyout == null){
						applyout = drugApplyDao.obtainApplyoutdesc(vo.getRecipeNo(), vo.getSequenceNo());
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
					}else{
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
						applyout.setValidState(0);
						drugApplyDao.updateDrugApplyout(applyout);
					}
				}
			}
		}
		//TODO   这是摆药通知处理（待测）
		Map<String, Integer> mapMsg = new HashMap<String, Integer>();
		for(DrugApplyoutNow applyout : listMsg){
			//摆药单分类代码_医嘱发送类型_取药科室id
			if(map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode()) == null){
		    	mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(), 1);
			}else{
				mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(),(int)map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode())+1);
			}
		}
		for(String key : mapMsg.keySet()){
			String valStr[] = key.split("_");
			InpatientStoMsgNow stomsg = drugApplyDao.getMsg(valStr[0], valStr[1], "0", valStr[2]);
			if(stomsg != null){
				List<DrugApplyoutNow> list2 = drugApplyDao.getApplyout(stomsg.getBillclassCode(), stomsg.getSendType(), stomsg.getMedDeptCode());
				if(list2.size() <= mapMsg.get(key)){
					stomsg.setDel_flg(1);
					stomsg.setDeleteUser(userid);
					stomsg.setDeleteTime(date);
					drugApplyDao.update(stomsg);
				}
			}
		}
		
		drugApplyDao.saveOrUpdate(listApplyout);
		
		for(ApplyVo vo : notDrugList){
			/***
			 * 判断非药品是否为物资
			 * 是：物资退费申请，病区退费申请，物资出入库申请，物资出入库记录，物资库存表，库管表，非药品明细数量
			 * 否：病区退费申请，非药品明细可退数量，
			 */  
			MatUndrugCompare info =  drugApplyDao.queryUndrugCompare(vo.getObjCode());
			if(info != null){
				/***
				 * 病区退费申请是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = get(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					
					//根据处方号和处方内序号获取非药品明细表
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					
					Double cancelNum = model.getQuantity();
					Double num = itemList.getNobackNum() + cancelNum - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量是否为0
					 * 接口中  useNum 判断是否全退
					 * 
					 * 若为0，说明上一次进行的是全退操作，此处需要产生正常出库流程
					 *     正交易类型
					 *     出库量为申请量 即患者实际使用量（applyNum = useNum）
					 * 若不为0，说明上次进行的是部分退操作，需要产生退库流程
					 *     反交易类型
					 *     根据库存流水号生成反交易冲账记录
					 *     出库量为患者实际使用量（useNum）
					 *     
					 */
					if(itemList.getNobackNum() == 0){
						outputvo.setTransType(1);
						outputvo.setApplyNum(num);
					}else{
						outputvo.setTransType(2);
						outputvo.setOutListCode(itemList.getUpdateSequenceno());
					}
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() + cancelNum - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请表
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					Double num = itemList.getNobackNum() - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量不为0 
					 *  反交易类型
					 *  出库量为退库量
					 *  更新库存的流水号
					 */
					outputvo.setTransType(2);
					outputvo.setApplyNum(itemList.getNobackNum());
					outputvo.setOutListCode(itemList.getUpdateSequenceno());
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请
					model = obtainVoForItemlist(itemList,vo);
					model.setBillCode(billCode);
					model.setChargeFlag(0);
					model.setDrugFlag(2);
					list.add(model);
				}
			}else{
				/***
				 * 是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = drugApplyDao.getById(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					
					//根据处方号和处方内序号获取非药品明细表7
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - (vo.getQuantity()*1.0 - model.getQuantity()));
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					model.setChargeFlag(0);
					model.setDrugFlag(2);
					model.setHospitalId(HisParameters.CURRENTHOSPITALID);
					model.setAreaCode(inprePayDAO.getDeptArea(deptid));
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					//病区退费申请
					model = obtainVoForItemlist(itemList,vo);
					model.setBillCode(billCode);
					model.setChargeFlag(0);
					model.setDrugFlag(2);
					model.setHospitalId(HisParameters.CURRENTHOSPITALID);
					model.setAreaCode(inprePayDAO.getDeptArea(deptid));
					list.add(model);
					
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - model.getQuantity());
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
				}
			}
		}
		drugApplyDao.saveOrUpdateCancelitemList(list);
		return map;
	}
	
	
	/***
	 * 要取消申请的id集合
	 * @Title: delDrugOrNotDrugApply 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param ids
	 * @return void
	 * @version 1.0
	 */
	@Override
	public Map<String,Object> delDrugOrNotDrugApply(String[] ids) {
		//当前登录信息
		String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Date date = DateUtils.getCurrentTime();
		
		//产生的退费申请
		List<InpatientCancelitemNow> listCancel = new ArrayList<InpatientCancelitemNow>();
		//产生的出库申请记录
		List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
		
		List<InpatientCancelitemNow> list = drugApplyDao.findCancelitemByIds(ids);
		
		Map<String,Object> map = applyState(list);
		
		if("1".equals(map.get("resCode"))){
			for(InpatientCancelitemNow model : list){
				//取消退费的项目是否为药品
				if(1 == model.getDrugFlag()){
					
					InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(model.getRecipeNo(), model.getSequenceNo());
					/***
					 * 判断可退数量是否为0
					 * 若为0 之前进行的是全退操作，
					 *     全退时直接作废的出库申请，无有效申请
					 *     取消时应找无效申请的第一条，恢复申请
					 * 若不为0，之前进行的是部分退操作，
					 *     操作时是作废之前的操作，产生新申请，
					 *     取消时，应作废有效申请，重新产生新申请
					 */
					DrugApplyoutNow oldapply = new DrugApplyoutNow();
					DrugApplyoutNow newApply = new DrugApplyoutNow();
					if(medicine.getNobackNum() == 0){
						oldapply = drugApplyDao.obtainApplyoutdesc(model.getRecipeNo(), model.getSequenceNo());
						oldapply.setValidState(1);
						listApplyout.add(oldapply);
					}else{
						oldapply = drugApplyDao.obtainApplyout(model.getRecipeNo(), model.getSequenceNo());
						newApply = obtainApplyout(oldapply,model);
						listApplyout.add(newApply);
						oldapply.setValidState(0);
						listApplyout.add(oldapply);
					}
					
					//更新药品明细可退数量
					medicine.setNobackNum(medicine.getNobackNum() + model.getQuantity());
					drugApplyDao.update(medicine);
					
					//返还药品库存预扣
					outstoreService.withholdStock(oldapply.getDrugDeptCode(), oldapply.getDrugCode(), model.getQuantity(), oldapply.getShowFlag());
					
					//作废病区退费申请
					model.setDel_flg(1);
					model.setChargeFlag(2);
					listCancel.add(model);
				}else {
					/***
					 * 判断非药品是否为物资
					 * 是：（物资退费申请，物资出库记录，物资库存，物资库管表），病区退费申请，非药品明细可退数量
					 * 否：作废病区退费申请，修改非药品明细的可退数量
					 */
					MatUndrugCompare info =  drugApplyDao.queryUndrugCompare(model.getItemCode());
					InpatientItemListNow  item = drugApplyDao.getItemListByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(info != null){
						//调用接口，物资入出库申请，出库记录，库存明细，库管明细
						OutputInInterVO outputvo = new OutputInInterVO();
						outputvo.setStorageCode(item.getExecuteDeptcode());
						outputvo.setInpatientNo(item.getInpatientNo());
						outputvo.setUndrugItemCode(item.getItemCode());
						outputvo.setRecipeNo(item.getRecipeNo());
						outputvo.setSequenceNo(item.getSequenceNo());
						
						Double cancelNum = model.getQuantity();
						Double num = item.getNobackNum() + cancelNum;
						outputvo.setUseNum(num);
						/***
						 * 判断物资的可退数量是否为0
						 * 接口中  useNum 判断是否全退
						 * 
						 * 若为0，说明上一次进行的是全退操作，此处需要产生正常出库流程
						 *     正交易类型
						 *     出库量为申请量 即患者实际使用量（applyNum = useNum）
						 * 若不为0，说明上次进行的是部分退操作，需要产生退库流程
						 *     反交易类型
						 *     根据库存流水号生成反交易冲账记录
						 *     出库量为患者实际使用量（useNum）
						 */
						if(item.getNobackNum() == 0){
							outputvo.setTransType(1);
							outputvo.setApplyNum(num);
						}else{
							outputvo.setTransType(2);
							outputvo.setOutListCode(item.getUpdateSequenceno());
						}
						outputvo.setFlag("C");
						Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
						
						String updateSequenceno = (String) map2.get("billNo");
						if(StringUtils.isNotEmpty(updateSequenceno)){
							//更新非药品明细表
							item.setUpdateSequenceno(updateSequenceno);
						}
						
						item.setNobackNum(item.getNobackNum() + cancelNum);
						item.setUpdateUser(userid);
						item.setUpdateTime(date);
						drugApplyDao.update(item);
						
						//作废病区退费申请
						model.setDel_flg(1);
						model.setChargeFlag(2);
						listCancel.add(model);
					}else{
						//作废病区退费申请
						model.setDel_flg(1);
						model.setChargeFlag(2);
						listCancel.add(model);
						
						//修改非药品明细的可退数量
						item.setNobackNum(item.getNobackNum() + model.getQuantity());
						drugApplyDao.update(item);
					}
				}
			}
			drugApplyDao.saveOrUpdate(listApplyout);
			drugApplyDao.saveOrUpdateList(listCancel);
		}
		return map;
	}

	/***
	 * 直接退费的保存过程
	 * @Title: directSave 
	 * @author  WFJ
	 * @createDate ：2016年5月16日
	 * @param medicalrecordId 患者病历号
	 * @param drugList 患者的退费药品集合
	 * @param notDrugList 患者退费的非药品集合
	 * @return Map<String,Object> 提示信息map
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> directSave(String medicalrecordId, List<ApplyVo> drugList, List<ApplyVo> notDrugList) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resCode","0");
		map.put("resMsg","保存成功！");
		
		//当前登录信息
		String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Date date = DateUtils.getCurrentTime();
		
		String billCode = obtainApplyNo(drugList,notDrugList);
		
		//产生的退费申请集合
		List<InpatientCancelitemNow> list = new ArrayList<InpatientCancelitemNow>();
		//产生的出库申请记录集合
		List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
		//需要进行摆药通知处理的集合
		List<DrugApplyoutNow> listMsg = new ArrayList<DrugApplyoutNow>();
		
		for(ApplyVo vo : drugList){
			/***
			 * 药品是否已摆药
			 * 已摆药：产生退药申请,核准后产生退费申请
			 * 未摆药：产生退费申请
			 */
			if(2 == vo.getSenddrugFlag()){
				/***
				 * 在产生退药申请的时，修改药品明细的可退数量并还回预扣库存
				 * 若核准退药单，则产生退费申请单
				 * 若取消退药单，需要还回药品明细的可退数量并增加预扣库存
				 */
				DrugApplyoutNow applyout = obtainBackDrug(vo);
				listApplyout.add(applyout);
				//操作住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				//该操作是产生新的退药申请，故可退数量为此算法
				medicine.setNobackNum(medicine.getNobackNum() - vo.getQuantity());
				medicine.setUpdateUser(userid);
				medicine.setUpdateTime(date);
				drugApplyDao.updateMedicine(medicine);
				
				map.put("resCode",1);
				map.put("resMsg","申请成功，但申请的项目中包含已摆药信息，实时退药后，可显示所有信息。");
			}else{
				/***
				 * 申请是否存在
				 * 存在，则更新
				 * 不存在，则添加
				 */
				//住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				if(vo.getApplyNo() != null){
					InpatientCancelitemNow model = get(vo.getApplyNo());
					//该操作是修改退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0-model.getQuantity()));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientCancelitemNow model = obtainVoForMedicine(medicine, vo);
					model.setBillCode(billCode);
					list.add(model);
					
					//该操作是生成退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
				}
				/***
				 * 判断是否全退
				 * 部分退，作废出库申请后，将剩余的部分重新生成出库申请
				 * 全退，直接作废出库申请后，是否预扣库存，返还预扣数量
				 */
				DrugApplyoutNow applyout = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
				if(0 == medicine.getNobackNum()){
					if(applyout != null){
						//药品是否产生摆药通知
						if(StringUtils.isNotEmpty(applyout.getDrugedBill())){
							listMsg.add(applyout);
						}
						
						//全部退药，作废出库申请
						applyout.setValidState(0);
						drugApplyDao.updateDrugApplyout(applyout);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(),applyout.getApplyNum(), applyout.getShowFlag());
					}
				}else{
					//在执行全退后，又进行数量的修改时，无有效的出库申请记录，此时需要进行该方法查询
					if(applyout == null){
						applyout = drugApplyDao.obtainApplyoutdesc(vo.getRecipeNo(), vo.getSequenceNo());
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
					}else{
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
						applyout.setValidState(0);
						drugApplyDao.updateDrugApplyout(applyout);
					}
				}
			}
		}
		//TODO   这是摆药通知处理（待测）
		Map<String, Integer> mapMsg = new HashMap<String, Integer>();
		for(DrugApplyoutNow applyout : listMsg){
			//摆药单分类代码_医嘱发送类型_取药科室id
			if(map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode()) == null){
				mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(), 1);
			}else{
				mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(),(int)map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode())+1);
			}
		}
		for(String key : mapMsg.keySet()){
			String valStr[] = key.split("_");
			InpatientStoMsgNow stomsg = drugApplyDao.getMsg(valStr[0], valStr[1], "0", valStr[2]);
			if(stomsg != null){
				List<DrugApplyoutNow> list2 = drugApplyDao.getApplyout(stomsg.getBillclassCode(), stomsg.getSendType(), stomsg.getMedDeptCode());
				if(list2.size() <= mapMsg.get(key)){
					stomsg.setDel_flg(1);
					stomsg.setDeleteUser(userid);
					stomsg.setDeleteTime(date);
					drugApplyDao.update(stomsg);;
				}
			}
		}
		drugApplyDao.saveOrUpdate(listApplyout);
		
		for(ApplyVo vo : notDrugList){
			/***
			 * 判断非药品是否为物资
			 * 是：物资退费申请，病区退费申请，物资出入库申请，物资出入库记录，物资库存表，库管表，非药品明细数量
			 * 否：病区退费申请，非药品明细可退数量，
			 */  
			MatUndrugCompare info =  drugApplyDao.queryUndrugCompare(vo.getObjCode());
			if(info != null){
				/***
				 * 病区退费申请是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = get(vo.getApplyNo());
					//根据处方号和处方内序号获取非药品明细表
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					
					Double cancelNum = model.getQuantity();
					Double num = itemList.getNobackNum() + cancelNum - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量是否为0
					 * 接口中  useNum 判断是否全退
					 * 
					 * 若为0，说明上一次进行的是全退操作，此处需要产生正常出库流程
					 *     正交易类型
					 *     出库量为申请量 即患者实际使用量（applyNum = useNum）
					 * 若不为0，说明上次进行的是部分退操作，需要产生退库流程
					 *     反交易类型
					 *     根据库存流水号生成反交易冲账记录
					 *     出库量为患者实际使用量（useNum）
					 *     
					 */
					if(itemList.getNobackNum() == 0){
						outputvo.setTransType(1);
						outputvo.setApplyNum(num);
					}else{
						outputvo.setTransType(2);
						outputvo.setOutListCode(itemList.getUpdateSequenceno());
					}
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() + cancelNum - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请表
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					Double num = itemList.getNobackNum() - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量不为0 
					 *  反交易类型
					 *  出库量为退库量
					 *  更新库存的流水号
					 */
					outputvo.setTransType(2);
					outputvo.setApplyNum(itemList.getNobackNum());
					outputvo.setOutListCode(itemList.getUpdateSequenceno());
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请
					model = obtainVoForItemlist(itemList,vo);
					model.setBillCode(billCode);
					list.add(model);
				}
			}else{
				/***
				 * 是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = get(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					
					//根据处方号和处方内序号获取非药品明细表7
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - (vo.getQuantity()*1.0 - model.getQuantity()));
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					//病区退费申请
					model = obtainVoForItemlist(itemList,vo);
					model.setBillCode(billCode);
					list.add(model);
					
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - model.getQuantity());
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
				}
			}
		}
		confirmService.applyConfirm(null,list);
		return map;
	}
	
	
	
/*-----------------------------------------------------	渲染 	--------------------------------------------------------------------------------------*/
	/**
	 * 合同单位下拉框
	 * @author  lyy
	 * @createDate： 2016年1月11日 下午8:05:31 
	 * @modifier lyy
	 * @modifyDate：2016年1月11日 下午8:05:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessContractunit> likeContractunit() {
		return drugApplyDao.likeContractunit();
	}
	
	
	
	@Override
	public InpatientBedinfoNow getBedinfo(String bedinfoId) {
		return drugApplyDao.getBedinfo(bedinfoId);
	}

	@Override
	public BusinessHospitalbed getBed(String bedId) {
		return drugApplyDao.getBed(bedId);
	}
	
/*-----------------------------------------------------		以下是未知领域  		--------------------------------------------------------------------------------------*/
	
	
	@Override
	public int getTotal(ApplyVo applySerch) {
		String inpatientNo = this.findInpatientNo(applySerch.getMedicalrecordId());
		applySerch.setInpatientNo(inpatientNo);
		
		return drugApplyDao.getTotal(applySerch);
	}
	
	@Override
	public int getTotalBack(ApplyVo vo) {
		return drugApplyDao.getTotalBack(vo);
	}

	@Override
	public int getTotalNotDrug(ApplyVo vo) {
		return drugApplyDao.getTotalNotDrug(vo);
	}


	@Override
	public List<SysDepartment> likeDept() {
		return drugApplyDao.likeDept();
	}
	/**
	 * 退费申请保存
	 * @author  lyy
	 * @createDate： 2016年1月14日 下午7:57:17 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 下午7:57:17  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void saveAdd(String cancelitemJson,String amounts,String deptCodes,String drugIds) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String arr[]=amounts.split(",");
		int amount=0;
		String deptArr[]=deptCodes.split(",");
		String deptCode="";
		String drugArr[]=drugIds.split(",");
		String drugId="";
		List<ApplyVo> modelList = new Gson().fromJson(cancelitemJson, new TypeToken<List<ApplyVo>>(){}.getType());   //把json转换成list
		if(modelList!=null&&modelList.size()>0){
			for(ApplyVo mod : modelList){
				InpatientCancelitem litem=new InpatientCancelitem();
				litem.setId(null);   //主键
				litem.setInpatientNo(mod.getInpatientNo());  //住院流水号
				litem.setName(mod.getName());  //患者名称
				litem.setDeptCode(mod.getInhosDept());   //患者所在科室
				litem.setItemName(mod.getDrugName());  //项目名称
				litem.setItemCode(mod.getDrugCode());  //项目编号
				litem.setSpecs(mod.getSpecs()); //规格
				litem.setSalePrice(mod.getPrice()); //价格
				litem.setQuantity(mod.getQty()); //申请退药数量
				litem.setPriceUnit(mod.getPackagingUnit());  //单位
				litem.setExecDpcd(mod.getExecuteDept());  //执行科室
				litem.setOperDate(new Date());  //操作时间
				litem.setRecipeNo(mod.getRecipeNo());  //处方号
				litem.setSequenceNo(mod.getSequenceNo());  //处方流水号
				litem.setDrugFlag(mod.getDrugFlag());
				litem.setCreateUser(userId);  //创建人
				litem.setCreateTime(new Date());  //创建时间
				litem.setCreateDept(deptId);   //创建部门
				litem.setDel_flg(0);
				litem.setStop_flg(0);
				if(mod.getApplyNo()!=null){
					litem.setQty(-(mod.getNum())); //退药数量
					litem.setChargeFlag(mod.getChargeFlag());   //退费标识	
				}else{
					litem.setQty(mod.getQty()); //退药数量
					litem.setChargeFlag(0);   //退费标识
					
				}
				drugApplyDao.save(litem);
				OperationUtils.getInstance().conserve(null, "退费申请", "INSERT INTO", "T_INPATIENT_CANCELITEM", OperationUtils.LOGACTIONINSERT);
			}
		}
		List<ApplyVo> newmodelList=new ArrayList<ApplyVo>();   //药品集合
		List<ApplyVo> newmodelNotList=new ArrayList<ApplyVo>();  //非品集合
		for(ApplyVo modl :modelList){   //从传过来的值
			if(modl.getDrugFlag()==1){      //判断是不是药品
				newmodelList.add(modl);    //是药品把他添加到newmodelList集合中
			}else{
				newmodelNotList.add(modl);   //是非药品把他添加到newmodeNotlList集合中
			}
		}
		//非药品
		for(ApplyVo mod11 : newmodelNotList){    //判断是不是物资
			String itemCode=mod11.getDrugCode();   //获得非药品编号
			MatUndrugCompare unDrugCompare= drugApplyDao.queryUndrugCompare(itemCode);  //根据非药品编号
			String compareId=unDrugCompare.getMatItemCode();   //获得物资编码
			List<MatBaseinfo> baseInfoList =drugApplyDao.queryUnDrugCode(compareId);  //根据非药品物资对照表的物资编码
			MatStockdetail stockdetail= drugApplyDao.queryStockNo(compareId);  //根据物资编码拿到库存号
			String stockno= stockdetail.getStockNo();
			if(baseInfoList.size()>0){
				MaterialsCancelmetlist canList=new MaterialsCancelmetlist();
				canList.setId(null);
				canList.setApplyNo(mod11.getApplyNo());  //申请流水号
				String Number = applyoutDAO.getSequece("T_MATERIALS_CANCELMETLIST").toString();//根据sequence 获取applyNumber
				canList.setOutNo(Number);    //出库单流水号
				canList.setStockNo(stockno);    //库存号
				canList.setRecipeNo(mod11.getRecipeNo());   //处方号
				canList.setSequenceNo(mod11.getSequenceNo());  //处方流水号
				canList.setItemCode(mod11.getDrugCode());  //物品编码
				canList.setItemName(mod11.getDrugName());  //物品名称
				canList.setSpecs(mod11.getSpecs());  //规格
				canList.setStatUnit(mod11.getPackagingUnit());  //单位
				canList.setSalePrice(mod11.getPrice());  //价格
				canList.setOutNum(mod11.getQuantity());
				canList.setCancelflag("0");
			}
			OperationUtils.getInstance().conserve(mod11.getId(), "退费申请物资", "INSERT INTO", "T_INPATIENT_CANCELITEM", OperationUtils.LOGACTIONUPDATE);
		}
		//药品
		for(ApplyVo mod2 : newmodelList){
			if(StringUtils.isNotBlank(mod2.getId())){
				drugApplyDao.updateApplyOutState(mod2.getId());
			}
			OperationUtils.getInstance().conserve(mod2.getId(), "作废摆药申请", "INSERT INTO", "T_INPATIENT_CANCELITEM", OperationUtils.LOGACTIONUPDATE);
		}
		int i=0;  //定义一个变量
		for(ApplyVo mod3 : newmodelList){    //判断是部门退药 或者 全部退药
			amount=Integer.parseInt(arr[i]); 
			i++;
			if(StringUtils.isNotBlank(mod3.getId())){
				if(amount>0){ 
					DrugApplyoutNow drugOut=new DrugApplyoutNow();
					drugOut.setId(null);
					int applyNumber = Integer.parseInt(applyoutDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
					drugOut.setApplyNumber(applyNumber);   //申请流水号
					drugOut.setDeptCode(deptId);       //申请部门   //暂时保存的是当前登录科室
					drugOut.setMinUnit(StringUtils.isBlank(mod3.getPackagingUnit())?"盒":mod3.getPackagingUnit());   //当前单位
					drugOut.setDrugName(mod3.getDrugCode()); //项目名称
					drugOut.setDrugCode(mod3.getDrugCode()); //项目编号
					drugOut.setPackUnit(mod3.getUnit());   //包装单位
					drugOut.setRecipeNo(mod3.getRecipeNo());   //处方号
					drugOut.setRetailPrice(mod3.getPrice());  //单价
					drugOut.setApplyState(0);    //申请状态
					drugOut.setCreateUser(userId);   //创建人
					drugOut.setCreateTime(new Date());  //创建时间
					drugOut.setCreateDept(deptId);  //创建部门
					drugOut.setDel_flg(0);    //删除人
					drugOut.setStop_flg(0);   //停用标志
					applyoutDAO.save(drugOut);
					OperationUtils.getInstance().conserve(null, "摆药申请", "INSERT INTO", "T_DRUG_APPLYOUT", OperationUtils.LOGACTIONINSERT);
					SysDepartment sys=null;
					if(StringUtils.isBlank(mod3.getDeptcode())){
						 sys=departmentDAO.getDeptCode(mod3.getDeptcode());
					}
					InpatientStoMsg sto=new InpatientStoMsg();
					sto.setId(null);
					sto.setDeptCode(mod3.getRecipeDept());
					sto.setDeptName("");
					sto.setBillclassCode("0");
					sto.setBillclassName("非医嘱摆药");
					sto.setSendType("0");
					sto.setSendDtime(new Date());
					sto.setSendFlag("0");
					sto.setMedDeptCode(StringUtils.isBlank(mod3.getDeptcode())?"取药":mod3.getDeptcode());  //取药科室
					sto.setMedDeptCode(StringUtils.isBlank(mod3.getDeptcode())?"取药":sys.getDeptName());  //取药科室
					sto.setCreateUser(userId);
					sto.setCreateTime(new Date());
					sto.setCreateDept(deptId);
					sto.setDel_flg(0);
					sto.setStop_flg(0);
					inpatientStoMsgDAO.save(sto);
					OperationUtils.getInstance().conserve(null, "摆药申请通知单", "INSERT INTO", "T_INPATIENT_STO_MSG", OperationUtils.LOGACTIONINSERT);
				}
			}
		}
		
	}
	/**
	 * 药品明细表的修改
	 * @author  lyy
	 * @createDate： 2016年1月14日 下午7:57:17 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 下午7:57:17  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	
	@Override
	public String editUpdate(String amounts, String ids,String cancelitemJson) {
		List<InpatientMedicineListNow> applyList = drugApplyDao.getChildByIds(ids);  //根据药品明细表id查询
		String arr[]=amounts.split(",");
		String arrs[]=ids.split(",");
		int amount=0;
		String id="";
		String retVal=null;
		for (int i = 0; i < arr.length; i++) {
			amount=Integer.parseInt(arr[i]);
			id=arrs[i];
			 retVal = drugApplyDao.updateMediceList(amount,id);      //更新住院药品明细表
		}
		OperationUtils.getInstance().conserve(id,"药品明细表","UPDATE","T_INPATIENT_MEDICINELIST",OperationUtils.LOGACTIONUPDATE);
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();      //获得登录人
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();   //获得登录部门
		List<ApplyVo> modelList = new Gson().fromJson(cancelitemJson, new TypeToken<List<InpatientCancelitem>>(){}.getType());   //把json转换成list
		if(modelList!=null&&modelList.size()>0){              //生成药品退库申请
			for(ApplyVo modl : modelList){
				DrugApplyoutNow out=new DrugApplyoutNow();
					out.setId(null);
					int applyNumber = Integer.parseInt(applyoutDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
					out.setApplyNumber(applyNumber);
					out.setDeptCode(deptId);       //申请部门   //暂时保存的是当前登录科室
					out.setMinUnit(modl.getUnit());   //单位
					out.setDrugName(modl.getDrugName()); //项目名称
					out.setDrugCode(modl.getDrugCode()); //项目编号
					out.setPackUnit(modl.getPackagingUnit());   //包装单位
					out.setRecipeNo(modl.getRecipeNo());   //处方号
					out.setRetailPrice(modl.getPrice());  //单价
					out.setBillclassCode("0000000000");   //摆药单分类代码
					out.setCreateUser(userId);
					out.setCreateTime(new Date());
					out.setCreateDept(deptId);
					out.setDel_flg(0);
					out.setStop_flg(0);
					applyoutDAO.save(out);
					OperationUtils.getInstance().conserve(null, "退药申请", "INSERT INTO", "T_DRUG_APPLYOUT", OperationUtils.LOGACTIONINSERT);
			}
			for (ApplyVo modl : modelList) {
				SysDepartment sys=null;
				if(StringUtils.isBlank(modl.getDeptcode())){
					 sys=departmentDAO.getDeptCode(modl.getDeptcode());
				}
				InpatientStoMsg stoMsg=new InpatientStoMsg();
				stoMsg.setId(null);
				stoMsg.setDeptCode(modl.getRecipeDept());
				stoMsg.setDeptName("");
				stoMsg.setBillclassCode("0000000000");
				stoMsg.setBillclassName("退药单");
				stoMsg.setSendType("0");
				stoMsg.setSendDtime(new Date());
				stoMsg.setSendFlag("0");
				stoMsg.setMedDeptCode(StringUtils.isBlank(modl.getDeptcode())?"取药":modl.getDeptcode());    //取药科室
				stoMsg.setMedDeptCode(StringUtils.isBlank(modl.getDeptcode())?"取药":sys.getDeptName());    //取药科室
				stoMsg.setSendDtime(new Date());
				stoMsg.setCreateUser(userId);
				stoMsg.setCreateTime(new Date());
				stoMsg.setCreateDept(deptId);
				stoMsg.setDel_flg(0);
				stoMsg.setStop_flg(0);
				inpatientStoMsgDAO.save(stoMsg);
				OperationUtils.getInstance().conserve(null, "摆药通知", "INSERT INTO", "T_DRUG_APPLYOUT", OperationUtils.LOGACTIONINSERT);
			}
		}
		return retVal;
	}
	/**
	 * 根据id去查询药品明细表
	 * @author  lyy
	 * @createDate： 2016年1月14日 下午7:56:45 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 下午7:56:45  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientMedicineListNow> getChildByIds(String ids) {
		return drugApplyDao.getChildByIds(ids);
	}
	/**
	 * 根据药品编码修改摆药单类型
	 * @author  lyy
	 * @createDate： 2016年1月15日 下午2:33:10 
	 * @modifier lyy
	 * @modifyDate：2016年1月15日 下午2:33:10  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public String UpdateApplyOutState(String ids) {
		return drugApplyDao.updateApplyOutState(ids);		
	}
	
	@Override
	public String directUpdate(String amounts, String ids, String cancelitemJson,String moneys) throws Exception {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();      //获得登录人
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();   //获得登录部门
		String arr[]=amounts.split(",");  //退药个数
		String arrs[]=ids.split(",");  //退药的id
		String mArr[]=moneys.split(",");  //退药的钱
		int amount=0;
		String id="";
		Double money=0.00;
		String retVal=null;
		List<ApplyVo> modelList = new Gson().fromJson(cancelitemJson, new TypeToken<List<ApplyVo>>(){}.getType());   //把json转换成list
		List<ApplyVo> newDrugList=new ArrayList<ApplyVo>();   //药品集合
		List<ApplyVo> newNotDrugList=new ArrayList<ApplyVo>();  //非药品集合
		for (ApplyVo mod : modelList) {
			if(mod.getDrugFlag()==1){
				newDrugList.add(mod);
			}else{
				newNotDrugList.add(mod);
			}
		}
		int i=0;  //定义一个变量
		//药品
		for (ApplyVo mod1 : newDrugList) {
			if(mod1.getBabyFlag()!=null){
				if(mod1.getBabyFlag()==2){
					if(modelList!=null&&modelList.size()>0){            //生成药品退库申请
						id=arrs[i];
						i++;
						List<InpatientMedicineListNow> applyList = drugApplyDao.getChildByIds(id);  //根据药品明细表id查询
						if(applyList!=null&&applyList.size()>0){
								amount=Integer.parseInt(arr[i]);
								retVal = drugApplyDao.updateMediceList(amount,id);      //更新住院药品明细表
						}
						amount=Integer.parseInt(arr[i]); 
						if(StringUtils.isNotBlank(mod1.getId())){
							if(amount>0){   //部门退药
								DrugApplyoutNow out=new DrugApplyoutNow();
								out.setId(null);
								int applyNumber = Integer.parseInt(applyoutDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
								out.setApplyNumber(applyNumber);
								out.setDeptCode(deptId);       //申请部门   //暂时保存的是当前登录科室
								out.setMinUnit(mod1.getUnit());   //单位
								out.setDrugName(mod1.getDrugName()); //项目名称
								out.setDrugCode(mod1.getDrugCode()); //项目编号
								out.setPackUnit(mod1.getUnit());   //包装单位
								out.setRecipeNo(mod1.getRecipeNo());   //处方号
								out.setRetailPrice(mod1.getPrice());  //单价
								out.setBillclassCode("0000000000");   //摆药单分类代码
								out.setCreateUser(userId);
								out.setCreateTime(new Date());
								out.setCreateDept(deptId);
								out.setDel_flg(0);
								out.setStop_flg(0);
								applyoutDAO.save(out);
								OperationUtils.getInstance().conserve(null, "退药申请", "INSERT INTO", "T_DRUG_APPLYOUT", OperationUtils.LOGACTIONINSERT);
								
								drugApplyDao.updateApplyOutState(mod1.getId());
								OperationUtils.getInstance().conserve(mod1.getId(), "作废摆药申请", "INSERT INTO", "T_INPATIENT_CANCELITEM", OperationUtils.LOGACTIONUPDATE);
							
								SysDepartment sys=null;
								if(StringUtils.isBlank(mod1.getDeptcode())){
									 sys=departmentDAO.getDeptCode(mod1.getDeptcode());
								}
								InpatientStoMsg sto=new InpatientStoMsg();
								sto.setId(null);
								sto.setDeptCode(mod1.getRecipeDept());
								sto.setDeptName("");
								sto.setBillclassCode("0");
								sto.setBillclassName("非医嘱摆药");
								sto.setSendType("0");
								sto.setSendDtime(new Date());
								sto.setSendFlag("0");
								sto.setMedDeptCode(StringUtils.isBlank(mod1.getDeptcode())?"取药":mod1.getDeptcode());  //取药科室
								sto.setMedDeptCode(StringUtils.isBlank(mod1.getDeptcode())?"取药":sys.getDeptName());  //取药科室
								sto.setCreateUser(userId);
								sto.setCreateTime(new Date());
								sto.setCreateDept(deptId);
								sto.setDel_flg(0);
								sto.setStop_flg(0);
								inpatientStoMsgDAO.save(sto);
								OperationUtils.getInstance().conserve(null, "摆药通知单", "INSERT INTO", "T_INPATIENT_STO_MSG", OperationUtils.LOGACTIONINSERT);
							}
						}
					}
				}
			}
			
		}
		//非药品
		for (ApplyVo mod2 : newNotDrugList) {
			i++;
			String itemCode=mod2.getDrugCode();   //获得非药品编号
			MatUndrugCompare unDrugCompare= drugApplyDao.queryUndrugCompare(itemCode);  //根据非药品编号去查非药品物资对照表
			String compareId=unDrugCompare.getMatItemCode();   //获得物资编码
			MatBaseinfo baseInfoList =drugApplyDao.querybaseInfo(compareId);  //根据非药品物资对照表的物资编码去查物资字典表
			String baseId=baseInfoList.getId();   //id
			int batchFlag=baseInfoList.getBatchFlag();   //批次号
			if(baseInfoList!=null){     //是物资
				MatBaseinfo matInfo=drugApplyDao.queryMatBase(baseId,batchFlag);
				if(matInfo!=null){
					DrugApplyoutNow drugOut=new DrugApplyoutNow();
					drugOut.setId(null);
					int applyNumber = Integer.parseInt(applyoutDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
					drugOut.setApplyNumber(applyNumber);   //申请流水号
					drugOut.setDeptCode(deptId);       //申请部门   //暂时保存的是当前登录科室
					drugOut.setMinUnit(StringUtils.isBlank(mod2.getMinUnit())?"盒":mod2.getMinUnit());   //当前单位
					drugOut.setDrugName(mod2.getDrugName()); //项目名称
					drugOut.setDrugCode(mod2.getDrugCode()); //项目编号
					drugOut.setPackUnit(mod2.getPackagingUnit());   //包装单位
					drugOut.setRecipeNo(mod2.getRecipeNo());   //处方号
					drugOut.setSequenceNo(mod2.getSequenceNo());
					drugOut.setRetailPrice(mod2.getPrice());  //单价
					drugOut.setApplyNum(-(Double.valueOf(mod2.getNobackNum())));    //出库数量
					drugOut.setApplyState(0);    //申请状态
					drugOut.setCreateUser(userId);   //创建人
					drugOut.setCreateTime(new Date());  //创建时间
					drugOut.setCreateDept(deptId);  //创建部门
					drugOut.setDel_flg(0);    //删除人
					drugOut.setStop_flg(0);   //停用标志
					applyoutDAO.save(drugOut);
					OperationUtils.getInstance().conserve(null, "出库数量", "INSERT INTO", "T_DRUG_APPLYOUT", OperationUtils.LOGACTIONINSERT);
					
					MatOutput matoutPat=new MatOutput();
					String outNo = applyoutDAO.getSequece("SEQ_MAT_OUT_NO").toString();//根据sequence 获取applyNumber
					matoutPat.setOutNo(outNo);  //出库流水号
					String outListCode=applyoutDAO.getSequece("SEQ_OUT_BILL_CODE").toString();
					matoutPat.setOutListCode(outListCode);   //出库单号
					matoutPat.setTransType(2);   //交易类型  反交易
					matoutPat.setOutSerialNo(i);    //出库单内序号
					matoutPat.setStorageCode(""); //仓库编码
					matoutPat.setOutState(0);  //状态(0-申请出库,1-审批出库,2-核准出库,3-备货出库,4-特殊出库核准)
					matoutPat.setItemCode(mod2.getDrugCode());  //物品编码
					matoutPat.setItemName(mod2.getDrugName());  //物品名称
					matoutPat.setSpecs(mod2.getSpecs());  //规格
					matoutPat.setPackUnit(mod2.getUnit());  //最小单位
					matoutPat.setOutNum(-(Double.valueOf(mod2.getNobackNum())));   //数量
					matoutPat.setOutCost(mod2.getMoneySum());  //出库金额
					matoutPat.setSalePrice(mod2.getPrice());  //零售价格
					matoutPat.setPrivStoreNum(mod2.getNum());  //出库前库存量
					matOutputDao.save(matoutPat); 
					OperationUtils.getInstance().conserve(null, "物资出库", "INSERT INTO", "T_MAT_OUTPUT", OperationUtils.LOGACTIONINSERT);
					
					amount=Integer.parseInt(arr[i]); 
					drugApplyDao.updateItemList(amount,id);    //更新非药品明细表中的数量
					OperationUtils.getInstance().conserve(null, "非药品明细表", "INSERT INTO", "T_INPATIENT_ITEMLIST", OperationUtils.LOGACTIONUPDATE);
					
				}
			}
		}
		
		//药品和非药品公共部分
		for (ApplyVo applyVo : modelList) {
			id=arrs[i];
			money=Double.parseDouble(mArr[i]);
			amount=Integer.parseInt(arr[i]);
			i++;
		    InpatientCancelitemNow canlitem= drugApplyDao.queryCancelItem(id);  //根据id去查询退费申请表
		    if(canlitem!=null){
		    	canlitem.setId(null);   //主键
				canlitem.setInpatientNo(applyVo.getInpatientNo());  //住院流水号
				canlitem.setName(applyVo.getName());  //患者名称
				canlitem.setDeptCode(applyVo.getDeptcode());   //患者所在科室
				canlitem.setItemName(applyVo.getDrugName());  //项目名称
				canlitem.setItemCode(applyVo.getDrugCode());  //项目编号
				canlitem.setSpecs(applyVo.getSpecs()); //规格
				canlitem.setSalePrice(applyVo.getPrice()); //价格
				canlitem.setQuantity(-(applyVo.getQty())); //申请退药数量
				canlitem.setPriceUnit(applyVo.getUnit());  //单位
				canlitem.setExecDpcd(applyVo.getExecuteDept());  //执行科室
				canlitem.setOperDate(new Date());  //操作时间
				canlitem.setRecipeNo(applyVo.getRecipeNo());  //处方号
				canlitem.setSequenceNo(applyVo.getSequenceNo());  //处方流水号
				canlitem.setDrugFlag(applyVo.getDrugFlag());
				canlitem.setCreateUser(userId);  //创建人
				canlitem.setCreateTime(new Date());  //创建时间
				canlitem.setCreateDept(deptId);   //创建部门
				canlitem.setDel_flg(0);
				canlitem.setStop_flg(0);
				drugApplyDao.save(canlitem);
		    }
//		    confirmDao.confirmBack(id, userId, deptId);   //修改退费申请表中的退费状态为退费
		    String ipatientNo= canlitem.getInpatientNo(); //住院流水号
		    InpatientFeeInfoNow feeInfo = drugApplyDao.queryFeeInfo(ipatientNo);   //根据住院流水号查询费用汇总表
		    if(StringUtils.isNotBlank(feeInfo.getId())){
		    	feeInfoDao.del(feeInfo.getId(),ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getId());
		    	InpatientFeeInfo fee =new InpatientFeeInfo();
		    	fee.setId(null);
		    	fee.setRecipeNo(applyVo.getRecipeNo());   //处方号
		    	fee.setTransType(2);    //交易类型
		    	fee.setInpatientNo(applyVo.getInpatientNo());  //住院流水号
		    	fee.setName(applyVo.getName());   //患者名称
		    	fee.setTotCost(-money);   //费用金额 
		    	fee.setRecipeDeptcode(applyVo.getRecipeDept());   //开立科室
		    	fee.setBalanceDate(new Date());  //结算时间  
		    	fee.setBalanceState(1);   //结算标志 0:未结算；1:已结算 2:已结转   
		    	fee.setDel_flg(0);
		    	fee.setStop_flg(0);
		    	fee.setAreaCode(inprePayDAO.getDeptArea(deptId));
		    	fee.setHospitalId(HisParameters.CURRENTHOSPITALID);
		    	feeInfoDao.save(fee);    //生成反交易记录
		    } 
			
			//涉及到的医保预结算（没有医保接口）暂时先搁置不做
			drugApplyDao.updateInpatientInfo(money,ipatientNo);   
			drugApplyDao.updateApplyOutState(applyVo.getId());
			OperationUtils.getInstance().conserve(applyVo.getId(), "作废摆药申请", "INSERT INTO", "T_INPATIENT_CANCELITEM", OperationUtils.LOGACTIONUPDATE);
			if(amount>0){
				SysDepartment sys=null;
				if(StringUtils.isBlank(applyVo.getDeptcode())){
					 sys=departmentDAO.getDeptCode(applyVo.getDeptcode());
				}
				InpatientStoMsg sto=new InpatientStoMsg();
				sto.setId(null);
				sto.setDeptCode(applyVo.getRecipeDept());
				sto.setDeptName("");
				sto.setBillclassCode("0");
				sto.setBillclassName("非医嘱摆药");
				sto.setSendType("0");
				sto.setSendDtime(new Date());
				sto.setSendFlag("0");
				sto.setMedDeptCode(StringUtils.isBlank(applyVo.getDeptcode())?"取药":applyVo.getDeptcode());  //取药科室
				sto.setMedDeptCode(StringUtils.isBlank(applyVo.getDeptcode())?"取药":sys.getDeptName());  //取药科室
				sto.setCreateUser(userId);
				sto.setCreateTime(new Date());
				sto.setCreateDept(deptId);
				sto.setDel_flg(0);
				sto.setStop_flg(0);
				inpatientStoMsgDAO.save(sto);
				OperationUtils.getInstance().conserve(null, "摆药申请通知单", "INSERT INTO", "T_INPATIENT_STO_MSG", OperationUtils.LOGACTIONINSERT);
			}
		}
		return retVal;
	}
	
	@Override
	public int getTotalDrugBack(ApplyVo vo) {
		return drugApplyDao.getTotalDrugBack(vo);
	}
	
	@Override
	public String delDrugOrNotDrugApply(String id,String flags,String recipeNos,String sequenceNos,String amounts,String balanceStates,String drugIds) {
		String deptId=ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode();
		String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		int flag=0;   //药品状态
		String recipeNo="";     //处方号
		String sequenceNo="";  //处方流水号
		int amount=0;        //可退数量
		int balanceState=0;   //结算状态
		String drugId="";      //药品编号
		String arr[]=flags.split(",");  //药品状态分割
		String reno[]= recipeNos.split(",");  //处方号分割
		String seq[]=sequenceNos.split(",");   //处方流水号分割
		String amt[]= amounts.split(",");  //可退数量分割
		String state[]=balanceStates.split(",");   //结算状态分割
		String dId[]=drugIds.split(",");
		String retVal=null; 
		DrugApplyoutNow apply=null;
		for (int i = 0; i < arr.length; i++) {
			flag=Integer.parseInt(arr[i]);
			recipeNo=reno[i];
			sequenceNo=seq[i];
			amount=Integer.parseInt(amt[i]);
			balanceState=Integer.parseInt(state[i]);
			drugId=dId[i];
			if(flag==1){    //如果是药品
				List<InpatientMedicineListNow> applyList = drugApplyDao.getChild(recipeNo,sequenceNo);  //根据药品明细表处方号,处方流水号查询
				apply=drugApplyDao.QueryApplyOut(recipeNo,sequenceNo);
				int validState=0;   //表示是否退药
				if(apply.getValidState()==null){
					validState=2;
				}else{
					validState=apply.getValidState();
				}
				if(apply!=null){
					if(validState==1){//未退药
						apply= drugApplyDao.QueryApplyOut(recipeNo,sequenceNo);      //根据处方号和处方流水号查询出库信息
						 drugApplyDao.amendStorage(amount,drugId);
						 drugApplyDao.amendStockInfo(amount,drugId);
					}else{  //已退药  
						if(applyList.size()>0){
							if(applyList.get(0).getNobackNum()>0){  //部分退
								drugApplyDao.updateApplyOutState(id);
								drugApplyDao.amendStorage(amount,drugId);
								drugApplyDao.amendStockInfo(amount,drugId);
								DrugApplyoutNow drug=drugApplyDao.queryDrdugApply(recipeNo,sequenceNo);   //根据id去查询list集合
								DrugApplyoutNow drugOut=new DrugApplyoutNow();
								drugOut.setId(null);
								int applyNumber = Integer.parseInt(applyoutDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
								drugOut.setApplyNumber(applyNumber);   //申请流水号
								drugOut.setDeptCode(deptId);       //申请部门   //暂时保存的是当前登录科室
								drugOut.setMinUnit(StringUtils.isBlank(drug.getMinUnit())?"盒":drug.getMinUnit());   //当前单位
								drugOut.setDrugName(drug.getDrugName()); //项目名称
								drugOut.setDrugCode(drug.getDrugCode()); //项目编号
								drugOut.setPackUnit(drug.getPackUnit());   //包装单位
								drugOut.setRecipeNo(drug.getRecipeNo());   //处方号
								drugOut.setSequenceNo(drug.getSequenceNo());
								drugOut.setRetailPrice(drug.getRetailPrice());  //单价
								drugOut.setApplyState(0);    //申请状态
								drugOut.setCreateUser(userId);   //创建人
								drugOut.setCreateTime(new Date());  //创建时间
								drugOut.setCreateDept(deptId);  //创建部门
								drugOut.setDel_flg(0);    //删除人
								drugOut.setStop_flg(0);   //停用标志
								applyoutDAO.save(drugOut);
								OperationUtils.getInstance().conserve(null, "摆药申请", "INSERT INTO", "T_DRUG_APPLYOUT", OperationUtils.LOGACTIONINSERT);
							}else{   //全部退
								drugApplyDao.updateApplyOut(recipeNo, sequenceNo);  //根据处方号和处方流水号修改出库表的有效状态
								apply=drugApplyDao.QueryApplyOut(recipeNo, sequenceNo);   //根据处方号和处方流水号去查出库表，并获得
								String bill=apply.getBillclassCode();
								drugApplyDao.updateStoMsg(bill);//根据处方号和处方流水号修改摆药通知的
								drugApplyDao.amendStorage(amount,drugId);
							    drugApplyDao.amendStockInfo(amount,drugId);
							}
							retVal= drugApplyDao.updateInpatientMedList(recipeNo,sequenceNo,amount,balanceState);      //更新住院药品明细表
							drugApplyDao.updateApplyState(deptId,recipeNo,sequenceNo);      //更新出库申请表
							String str = parameterInnerService.getparameter("zysfsyykkc");   //查询参数设置住院收费使用预扣库存的方式的情况
							if("1".equals(str)){
								apply= drugApplyDao.QueryApplyOut(recipeNo,sequenceNo);      //根据处方号和处方流水号查询出库信息
							}
						}
					}
				}
			}else{   //非药品
				List<InpatientItemListNow> itemList=drugApplyDao.getChildNotDrug(recipeNo,sequenceNo);  //根据处方号和处方流水号查询非药品明细表
				if(itemList.size()>0){ //判断非药品明细是否存在 
					drugApplyDao.updateInpatientItemList(recipeNo,sequenceNo,amount,balanceState);        //更新住院非药品明细表
					MatOutput  outPut=drugApplyDao.queryOutPut(recipeNo, sequenceNo);   //根据处方号和处方流水号去查出库表，并获得
					String applyNo=outPut.getApplyNo();  //申请流水号
					String outNo=outPut.getOutNo();    //出库单流水号
					String itemCode=outPut.getItemCode();  //物资编码
					MatStockdetail stockdetail= drugApplyDao.queryStockNo(itemCode);  //根据物资编码拿到库存号
					String stockNo= stockdetail.getStockNo();   //库存序号==库存流水号
					drugApplyDao.cancelmetList(amount,applyNo,outNo,stockNo);     //根据申请流水号、出库单流水号以及库存序  修改物资退费申请表可退数量
				}
			}
			drugApplyDao.updateApplyOutState(id);
		}
		return retVal;
	}

	public List<InpatientInfoNow> getInfoList(InpatientInfoNow info) {
		return drugApplyDao.getInfoList(info);
	}
	public BusinessHospitalbed getBedName(String bedInfoId) {
		return drugApplyDao.getBedName(bedInfoId);
	}

	@Override
	public List queryInpatientByMedicalRecordId(String medicalrecordId,String page,String rows) {
         return  drugApplyDao.queryInpatientByMedicalRecordId(medicalrecordId,page,rows);
	}

	@Override
	public List<InpatientCancelitemNow> queryInpatientReturns(
			String inpatientNo,String page,String rows) throws Exception {
		 List list = drugApplyDao.queryInpatientReturns(inpatientNo,page,rows);
		 Iterator iterator = list.iterator();
		 List<InpatientCancelitemNow> list2=new ArrayList<InpatientCancelitemNow>();
		 while(iterator.hasNext()){
			 Object[] obj = (Object[]) iterator.next(); 
			 InpatientCancelitemNow inpatientCancelitemNow=new InpatientCancelitemNow();
			 if(obj[0]!=null){
				 inpatientCancelitemNow.setName((String)obj[0]);
			 }
			 if(obj[1]!=null){
				 inpatientCancelitemNow.setDeptName((String)obj[1]);
			 }
			 if(obj[2]!=null){
				 inpatientCancelitemNow.setItemName((String)obj[2]);
			 }
			 if(obj[3]!=null){
				 inpatientCancelitemNow.setSpecs((String)obj[3]);
			 }
			 if(obj[4]!=null){
				 inpatientCancelitemNow.setSalePrice(Double.valueOf(obj[4].toString()));
			 }
			 if(obj[5]!=null){
				 inpatientCancelitemNow.setPriceUnit((String)obj[5]);
			 }
			 if(obj[6]!=null){
				 inpatientCancelitemNow.setQuantity(Double.valueOf(obj[6].toString()));
			 }
			 if(obj[7]!=null){
				 inpatientCancelitemNow.setOperDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj[7].toString()));
			 }
			 if(obj[8]!=null){
				 inpatientCancelitemNow.setOperName((String)obj[8]);
			 }
			 if(obj[9]!=null){
				 inpatientCancelitemNow.setRecipeNo((String)obj[9]);
			 }
			 if(obj[10]!=null){
				 inpatientCancelitemNow.setSequenceNo(Integer.parseInt(obj[10].toString()));
			 }
			 list2.add(inpatientCancelitemNow);
		 }
		 return list2;
	}

	@Override
	public int queryTotal(String medicalrecordId) {
		return drugApplyDao.queryTotal(medicalrecordId);
	}

	@Override
	public int queryTotalBy(String inpatientNo) {
		return drugApplyDao.queryTotalBy(inpatientNo);
	}

	@Override
	public Map<String, Object> directSave(String medicalrecordId,
			List<ApplyVo> drugList, List<ApplyVo> notDrugList, String deptCode,
			String empJobNo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resCode","0");
		map.put("resMsg","保存成功！");
		
		//当前登录信息
		String deptid=deptCode;
		String userid = empJobNo;
		Date date = DateUtils.getCurrentTime();
		
		String billCode = obtainApplyNo(drugList,notDrugList);
		
		//产生的退费申请集合
		List<InpatientCancelitemNow> list = new ArrayList<InpatientCancelitemNow>();
		//产生的出库申请记录集合
		List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
		//需要进行摆药通知处理的集合
		List<DrugApplyoutNow> listMsg = new ArrayList<DrugApplyoutNow>();
		
		for(ApplyVo vo : drugList){
			/***
			 * 药品是否已摆药
			 * 已摆药：产生退药申请,核准后产生退费申请
			 * 未摆药：产生退费申请
			 */
			if(2 == vo.getSenddrugFlag()){
				/***
				 * 在产生退药申请的时，修改药品明细的可退数量并还回预扣库存
				 * 若核准退药单，则产生退费申请单
				 * 若取消退药单，需要还回药品明细的可退数量并增加预扣库存
				 */
				DrugApplyoutNow applyout = obtainBackMobileDrug(vo,empJobNo,deptCode);
				listApplyout.add(applyout);
				//操作住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				//该操作是产生新的退药申请，故可退数量为此算法
				medicine.setNobackNum(medicine.getNobackNum() - vo.getQuantity());
				medicine.setUpdateUser(userid);
				medicine.setUpdateTime(date);
				drugApplyDao.updateMedicine(medicine);
				
				map.put("resCode",1);
				map.put("resMsg","申请成功，但申请的项目中包含已摆药信息，实时退药后，可显示所有信息。");
				return map;
			}else{
				/***
				 * 申请是否存在
				 * 存在，则更新
				 * 不存在，则添加
				 */
				//住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				if(vo.getApplyNo() != null){
					InpatientCancelitemNow model = get(vo.getApplyNo());
					//该操作是修改退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0-model.getQuantity()));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientCancelitemNow model = obtainVoForMedicine(medicine, vo);
					model.setBillCode(billCode);
					list.add(model);
					
					//该操作是生成退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
				}
				/***
				 * 判断是否全退
				 * 部分退，作废出库申请后，将剩余的部分重新生成出库申请
				 * 全退，直接作废出库申请后，是否预扣库存，返还预扣数量
				 */
				DrugApplyoutNow applyout = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
				if(0 == medicine.getNobackNum()){
					if(applyout != null){
						//药品是否产生摆药通知
						if(StringUtils.isNotEmpty(applyout.getDrugedBill())){
							listMsg.add(applyout);
						}
						
						//全部退药，作废出库申请
						applyout.setValidState(0);
						drugApplyDao.updateDrugApplyout(applyout);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(),applyout.getApplyNum(), applyout.getShowFlag());
					}
				}else{
					//在执行全退后，又进行数量的修改时，无有效的出库申请记录，此时需要进行该方法查询
					if(applyout == null){
						applyout = drugApplyDao.obtainApplyoutdesc(vo.getRecipeNo(), vo.getSequenceNo());
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
					}else{
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
						applyout.setValidState(0);
						drugApplyDao.updateDrugApplyout(applyout);
					}
				}
			}
		}
		//TODO   这是摆药通知处理（待测）
		Map<String, Integer> mapMsg = new HashMap<String, Integer>();
		for(DrugApplyoutNow applyout : listMsg){
			//摆药单分类代码_医嘱发送类型_取药科室id
			if(map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode()) == null){
				mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(), 1);
			}else{
				mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(),(int)map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode())+1);
			}
		}
		for(String key : mapMsg.keySet()){
			String valStr[] = key.split("_");
			InpatientStoMsgNow stomsg = drugApplyDao.getMsg(valStr[0], valStr[1], "0", valStr[2]);
			if(stomsg != null){
				List<DrugApplyoutNow> list2 = drugApplyDao.getApplyout(stomsg.getBillclassCode(), stomsg.getSendType(), stomsg.getMedDeptCode());
				if(list2.size() <= mapMsg.get(key)){
					stomsg.setDel_flg(1);
					stomsg.setDeleteUser(userid);
					stomsg.setDeleteTime(date);
					drugApplyDao.update(stomsg);;
				}
			}
		}
		drugApplyDao.saveOrUpdate(listApplyout);
		
		for(ApplyVo vo : notDrugList){
			/***
			 * 判断非药品是否为物资
			 * 是：物资退费申请，病区退费申请，物资出入库申请，物资出入库记录，物资库存表，库管表，非药品明细数量
			 * 否：病区退费申请，非药品明细可退数量，
			 */  
			MatUndrugCompare info =  drugApplyDao.queryUndrugCompare(vo.getObjCode());
			if(info != null){
				/***
				 * 病区退费申请是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = get(vo.getApplyNo());
					//根据处方号和处方内序号获取非药品明细表
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					
					Double cancelNum = model.getQuantity();
					Double num = itemList.getNobackNum() + cancelNum - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量是否为0
					 * 接口中  useNum 判断是否全退
					 * 
					 * 若为0，说明上一次进行的是全退操作，此处需要产生正常出库流程
					 *     正交易类型
					 *     出库量为申请量 即患者实际使用量（applyNum = useNum）
					 * 若不为0，说明上次进行的是部分退操作，需要产生退库流程
					 *     反交易类型
					 *     根据库存流水号生成反交易冲账记录
					 *     出库量为患者实际使用量（useNum）
					 *     
					 */
					if(itemList.getNobackNum() == 0){
						outputvo.setTransType(1);
						outputvo.setApplyNum(num);
					}else{
						outputvo.setTransType(2);
						outputvo.setOutListCode(itemList.getUpdateSequenceno());
					}
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() + cancelNum - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请表
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					Double num = itemList.getNobackNum() - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量不为0 
					 *  反交易类型
					 *  出库量为退库量
					 *  更新库存的流水号
					 */
					outputvo.setTransType(2);
					outputvo.setApplyNum(itemList.getNobackNum());
					outputvo.setOutListCode(itemList.getUpdateSequenceno());
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请
					model = obtainVoMobileForItemlist(itemList,vo,empJobNo,deptCode);
					model.setBillCode(billCode);
					list.add(model);
				}
			}else{
				/***
				 * 是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = get(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					
					//根据处方号和处方内序号获取非药品明细表7
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - (vo.getQuantity()*1.0 - model.getQuantity()));
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					//病区退费申请
					model = obtainVoMobileForItemlist(itemList,vo,empJobNo,deptCode);
					model.setBillCode(billCode);
					list.add(model);
					
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - model.getQuantity());
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
				}
			}
		}
		confirmService.applyConfirm(null,list,deptCode,empJobNo);
		return map;

	}

	@Override
	public Map<String, Object> delDrugOrNotDrugApply(String[] ids,
			String deptCode, String empJobNo) {
		//当前登录信息
				String deptid=deptCode;
				String userid = empJobNo;
				Date date = DateUtils.getCurrentTime();
				
				//产生的退费申请
				List<InpatientCancelitemNow> listCancel = new ArrayList<InpatientCancelitemNow>();
				//产生的出库申请记录
				List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
				
				List<InpatientCancelitemNow> list = drugApplyDao.findCancelitemByIds(ids);
				
				Map<String,Object> map = applyState(list);
				
				if("1".equals(map.get("resCode"))){
					for(InpatientCancelitemNow model : list){
						//取消退费的项目是否为药品
						if(1 == model.getDrugFlag()){
							
							InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(model.getRecipeNo(), model.getSequenceNo());
							/***
							 * 判断可退数量是否为0
							 * 若为0 之前进行的是全退操作，
							 *     全退时直接作废的出库申请，无有效申请
							 *     取消时应找无效申请的第一条，恢复申请
							 * 若不为0，之前进行的是部分退操作，
							 *     操作时是作废之前的操作，产生新申请，
							 *     取消时，应作废有效申请，重新产生新申请
							 */
							DrugApplyoutNow oldapply = new DrugApplyoutNow();
							DrugApplyoutNow newApply = new DrugApplyoutNow();
							if(medicine.getNobackNum() == 0){
								oldapply = drugApplyDao.obtainApplyoutdesc(model.getRecipeNo(), model.getSequenceNo());
								oldapply.setValidState(1);
								listApplyout.add(oldapply);
							}else{
								oldapply = drugApplyDao.obtainApplyout(model.getRecipeNo(), model.getSequenceNo());
								newApply = obtainApplyout(oldapply,model);
								listApplyout.add(newApply);
								oldapply.setValidState(0);
								listApplyout.add(oldapply);
							}
							
							//更新药品明细可退数量
							medicine.setNobackNum(medicine.getNobackNum() + model.getQuantity());
							drugApplyDao.update(medicine);
							
							//返还药品库存预扣
							outstoreService.withholdStock(oldapply.getDrugDeptCode(), oldapply.getDrugCode(), model.getQuantity(), oldapply.getShowFlag());
							
							//作废病区退费申请
							model.setDel_flg(1);
							model.setChargeFlag(2);
							listCancel.add(model);
						}else {
							/***
							 * 判断非药品是否为物资
							 * 是：（物资退费申请，物资出库记录，物资库存，物资库管表），病区退费申请，非药品明细可退数量
							 * 否：作废病区退费申请，修改非药品明细的可退数量
							 */
							MatUndrugCompare info =  drugApplyDao.queryUndrugCompare(model.getItemCode());
							InpatientItemListNow  item = drugApplyDao.getItemListByRecipe(model.getRecipeNo(), model.getSequenceNo());
							if(info != null){
								//调用接口，物资入出库申请，出库记录，库存明细，库管明细
								OutputInInterVO outputvo = new OutputInInterVO();
								outputvo.setStorageCode(item.getExecuteDeptcode());
								outputvo.setInpatientNo(item.getInpatientNo());
								outputvo.setUndrugItemCode(item.getItemCode());
								outputvo.setRecipeNo(item.getRecipeNo());
								outputvo.setSequenceNo(item.getSequenceNo());
								
								Double cancelNum = model.getQuantity();
								Double num = item.getNobackNum() + cancelNum;
								outputvo.setUseNum(num);
								/***
								 * 判断物资的可退数量是否为0
								 * 接口中  useNum 判断是否全退
								 * 
								 * 若为0，说明上一次进行的是全退操作，此处需要产生正常出库流程
								 *     正交易类型
								 *     出库量为申请量 即患者实际使用量（applyNum = useNum）
								 * 若不为0，说明上次进行的是部分退操作，需要产生退库流程
								 *     反交易类型
								 *     根据库存流水号生成反交易冲账记录
								 *     出库量为患者实际使用量（useNum）
								 */
								if(item.getNobackNum() == 0){
									outputvo.setTransType(1);
									outputvo.setApplyNum(num);
								}else{
									outputvo.setTransType(2);
									outputvo.setOutListCode(item.getUpdateSequenceno());
								}
								outputvo.setFlag("C");
								Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
								
								String updateSequenceno = (String) map2.get("billNo");
								if(StringUtils.isNotEmpty(updateSequenceno)){
									//更新非药品明细表
									item.setUpdateSequenceno(updateSequenceno);
								}
								
								item.setNobackNum(item.getNobackNum() + cancelNum);
								item.setUpdateUser(userid);
								item.setUpdateTime(date);
								drugApplyDao.update(item);
								
								//作废病区退费申请
								model.setDel_flg(1);
								model.setChargeFlag(2);
								listCancel.add(model);
							}else{
								//作废病区退费申请
								model.setDel_flg(1);
								model.setChargeFlag(2);
								listCancel.add(model);
								
								//修改非药品明细的可退数量
								item.setNobackNum(item.getNobackNum() + model.getQuantity());
								drugApplyDao.update(item);
							}
						}
					}
					drugApplyDao.saveOrUpdate(listApplyout);
					drugApplyDao.saveOrUpdateList(listCancel);
				}
				return map;
	}

	@Override
	public Map<String, Object> saveAdd(String medicalrecordId, String drugJson,
			String notDrugJson, String deptCode, String empJobNo) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("resCode","0");
		map.put("resMsg","保存成功！");
		//当前登录信息
		String deptid=deptCode;
		String userid = empJobNo;
		Date date = DateUtils.getCurrentTime();
		
		List<ApplyVo> drugList = new Gson().fromJson(drugJson, new TypeToken<List<ApplyVo>>(){}.getType());
		List<ApplyVo> notDrugList = new Gson().fromJson(notDrugJson, new TypeToken<List<ApplyVo>>(){}.getType());

		String billCode = obtainApplyNo(drugList,notDrugList);
		
		//产生的退费申请集合
		List<InpatientCancelitemNow> list = new ArrayList<InpatientCancelitemNow>();
		//产生的出库申请记录集合
		List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
		//需要进行摆药通知处理的集合
		List<DrugApplyoutNow> listMsg = new ArrayList<DrugApplyoutNow>();
		
		for(ApplyVo vo : drugList){
			/***
			 * 药品是否已摆药
			 * 已摆药：产生退药申请,核准后产生退费申请
			 * 未摆药：产生退费申请
			 */
			if(2 == vo.getSenddrugFlag()){
				/***
				 * 在产生退药申请的时，修改药品明细的可退数量并还回预扣库存
				 * 若核准退药单，则产生退费申请单
				 * 若取消退药单，需要还回药品明细的可退数量并增加预扣库存
				 */
				DrugApplyoutNow applyout = obtainBackMobileDrug(vo,empJobNo,deptCode);
				listApplyout.add(applyout);
				//操作住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				//该操作是产生新的退药申请，故可退数量为此算法
				medicine.setNobackNum(medicine.getNobackNum() - vo.getQuantity());
				medicine.setUpdateUser(userid);
				medicine.setUpdateTime(date);
				drugApplyDao.updateMedicine(medicine);
				
				map.put("resCode","1");
				map.put("resMsg","申请成功，但申请的项目中包含已摆药信息，实时退药后，可显示所有信息。");
			}else{
				/***
				 * 申请是否存在
				 * 存在，则更新
				 * 不存在，则添加
				 */
				//住院药品明细表
				InpatientMedicineListNow medicine = drugApplyDao.getChildByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
				if(vo.getApplyNo() != null){
					InpatientCancelitemNow model = drugApplyDao.getById(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					//该操作是修改退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0-model.getQuantity()));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					model.setDrugFlag(1);
					model.setChargeFlag(0);
					list.add(model);
				}else{
					InpatientCancelitemNow model = obtainVoMobileForMedicine(medicine, vo,empJobNo,deptCode);
					model.setBillCode(billCode);
					model.setChargeFlag(0);
					model.setDrugFlag(1);
					model.setHospitalId(HisParameters.CURRENTHOSPITALID);
					model.setAreaCode(inprePayDAO.getDeptArea(deptid));
					list.add(model);
					
					//该操作是生成退费申请，故可退数量为此算法
					medicine.setNobackNum(medicine.getNobackNum() - (vo.getQuantity()*1.0));
					medicine.setUpdateUser(userid);
					medicine.setUpdateTime(date);
					drugApplyDao.updateMedicine(medicine);
				}
				/***
				 * 判断是否全退
				 * 部分退，作废出库申请后，将剩余的部分重新生成出库申请
				 * 全退，直接作废出库申请后，是否预扣库存，返还预扣数量
				 */
				DrugApplyoutNow applyout = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
				if(0 == medicine.getNobackNum()){
					//药品是否产生摆药通知
					String drugedBill=applyout.getDrugedBill();
					
					if(StringUtils.isNotEmpty(applyout.getDrugedBill())){
						listMsg.add(applyout);
					}
					//全部退药，作废出库申请
					applyout.setValidState(0);
					drugApplyDao.updateDrugApplyout(applyout);
					//返还药品库存预扣
					outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(),applyout.getApplyNum(), applyout.getShowFlag());
					
				}else{
					//在执行全退后，又进行数量的修改时，无有效的出库申请记录，此时需要进行该方法查询
					if(applyout == null){
						applyout = drugApplyDao.obtainApplyoutdesc(vo.getRecipeNo(), vo.getSequenceNo());
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
					}else{
						//对于部分退药的，先生成出库新的申请，在作废掉之前的出库申请
						DrugApplyoutNow againmodel = againObtainApplyout(applyout,vo);
						listApplyout.add(againmodel);
						//返还药品库存预扣
						outstoreService.withholdStock(applyout.getDrugDeptCode(), applyout.getDrugCode(), vo.getQuantity()*1.0-applyout.getApplyNum(), applyout.getShowFlag());
						applyout.setValidState(0);
						drugApplyDao.updateDrugApplyout(applyout);
					}
				}
			}
		}
		//TODO   这是摆药通知处理（待测）
		Map<String, Integer> mapMsg = new HashMap<String, Integer>();
		for(DrugApplyoutNow applyout : listMsg){
			//摆药单分类代码_医嘱发送类型_取药科室id
			if(map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode()) == null){
		    	mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(), 1);
			}else{
				mapMsg.put(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode(),(int)map.get(applyout.getBillclassCode()+"_"+applyout.getSendType()+"_"+applyout.getDrugDeptCode())+1);
			}
		}
		for(String key : mapMsg.keySet()){
			String valStr[] = key.split("_");
			InpatientStoMsgNow stomsg = drugApplyDao.getMsg(valStr[0], valStr[1], "0", valStr[2]);
			if(stomsg != null){
				List<DrugApplyoutNow> list2 = drugApplyDao.getApplyout(stomsg.getBillclassCode(), stomsg.getSendType(), stomsg.getMedDeptCode());
				if(list2.size() <= mapMsg.get(key)){
					stomsg.setDel_flg(1);
					stomsg.setDeleteUser(userid);
					stomsg.setDeleteTime(date);
					drugApplyDao.update(stomsg);
				}
			}
		}
		
		drugApplyDao.saveOrUpdate(listApplyout);
		
		for(ApplyVo vo : notDrugList){
			/***
			 * 判断非药品是否为物资
			 * 是：物资退费申请，病区退费申请，物资出入库申请，物资出入库记录，物资库存表，库管表，非药品明细数量
			 * 否：病区退费申请，非药品明细可退数量，
			 */  
			MatUndrugCompare info =  drugApplyDao.queryUndrugCompare(vo.getObjCode());
			if(info != null){
				/***
				 * 病区退费申请是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = get(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					
					//根据处方号和处方内序号获取非药品明细表
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					
					Double cancelNum = model.getQuantity();
					Double num = itemList.getNobackNum() + cancelNum - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量是否为0
					 * 接口中  useNum 判断是否全退
					 * 
					 * 若为0，说明上一次进行的是全退操作，此处需要产生正常出库流程
					 *     正交易类型
					 *     出库量为申请量 即患者实际使用量（applyNum = useNum）
					 * 若不为0，说明上次进行的是部分退操作，需要产生退库流程
					 *     反交易类型
					 *     根据库存流水号生成反交易冲账记录
					 *     出库量为患者实际使用量（useNum）
					 *     
					 */
					if(itemList.getNobackNum() == 0){
						outputvo.setTransType(1);
						outputvo.setApplyNum(num);
					}else{
						outputvo.setTransType(2);
						outputvo.setOutListCode(itemList.getUpdateSequenceno());
					}
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() + cancelNum - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请表
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					
					//调用接口，物资入出库申请，出库记录，库存明细，库管明细
					OutputInInterVO outputvo = new OutputInInterVO();
					outputvo.setStorageCode(itemList.getExecuteDeptcode());
					outputvo.setInpatientNo(itemList.getInpatientNo());
					outputvo.setUndrugItemCode(itemList.getItemCode());
					outputvo.setRecipeNo(itemList.getRecipeNo());
					outputvo.setSequenceNo(itemList.getSequenceNo());
					Double num = itemList.getNobackNum() - vo.getQuantity();
					outputvo.setUseNum(num);
					/***
					 * 判断物资的可退数量不为0 
					 *  反交易类型
					 *  出库量为退库量
					 *  更新库存的流水号
					 */
					outputvo.setTransType(2);
					outputvo.setApplyNum(itemList.getNobackNum());
					outputvo.setOutListCode(itemList.getUpdateSequenceno());
					outputvo.setFlag("C");
					Map<String,Object> map2 = matOutPutService.addRecord(outputvo);
					String updateSequenceno = (String) map2.get("billNo");
					if(StringUtils.isNotEmpty(updateSequenceno)){
						//更新非药品明细表
						itemList.setUpdateSequenceno(updateSequenceno);
					}
					itemList.setNobackNum(itemList.getNobackNum() - vo.getQuantity()*1.0);
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//病区退费申请
					model = obtainVoForItemlist(itemList,vo);
					model.setBillCode(billCode);
					model.setChargeFlag(0);
					model.setDrugFlag(2);
					list.add(model);
				}
			}else{
				/***
				 * 是否已存在
				 * 是：修改数量
				 * 否：添加
				 */
				InpatientCancelitemNow model = new InpatientCancelitemNow();
				if(vo.getApplyNo() != null){
					model = drugApplyDao.getById(vo.getApplyNo());
					if(model.getQuantity() == (vo.getQuantity()*1.0)){
						continue;
					}
					
					//根据处方号和处方内序号获取非药品明细表7
					InpatientItemListNow itemList = drugApplyDao.getItemListByRecipe(vo.getRecipeNo(), vo.getSequenceNo());
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - (vo.getQuantity()*1.0 - model.getQuantity()));
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
					
					//申请的退费数量
					model.setQuantity(vo.getQuantity()*1.0);
					model.setUpdateUser(userid);
					model.setUpdateTime(date);
					model.setChargeFlag(0);
					model.setDrugFlag(2);
					model.setHospitalId(HisParameters.CURRENTHOSPITALID);
					model.setAreaCode(inprePayDAO.getDeptArea(deptid));
					list.add(model);
				}else{
					InpatientItemListNow itemList = drugApplyDao.getItemListById(vo.getId());
					//病区退费申请
					model = obtainVoMobileForItemlist(itemList,vo,empJobNo,deptCode);
					model.setBillCode(billCode);
					model.setChargeFlag(0);
					model.setDrugFlag(2);
					model.setHospitalId(HisParameters.CURRENTHOSPITALID);
					model.setAreaCode(inprePayDAO.getDeptArea(deptid));
					list.add(model);
					
					//更新非药品明细表
					itemList.setNobackNum(itemList.getNobackNum() - model.getQuantity());
					itemList.setUpdateUser(userid);
					itemList.setUpdateTime(date);
					drugApplyDao.updateInpatientItem(itemList);
				}
			}
		}
		drugApplyDao.saveOrUpdateCancelitemList(list,empJobNo);
		return map;
	}
	
	
	/**
	 * 对于已摆药的信息，需要生成退药申请单
	 * @Title: obtainBackDrug 
	 * @author  WFJ
	 * @createDate ：2016年4月28日
	 * @param vo
	 * @return DrugApplyout
	 * @version 1.0
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public DrugApplyoutNow obtainBackMobileDrug(ApplyVo vo,String empJobNo,String deptCode) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//当前登录信息
		String deptid=deptCode;
		String userid = empJobNo;
		Date date = DateUtils.getCurrentTime();
		
		DrugApplyoutNow model0 = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
		DrugApplyoutNow model1 = new DrugApplyoutNow();
		//model0 ：出库申请表中已摆药的记录，model1： 将要发起的住院退药申请
			if(model0!=null){
				PropertyUtils.copyProperties(model1, model0);

			}
		model1.setId(null);
		//申请流水号是读取序列
		int applyNumber = Integer.valueOf(drugApplyDao.getSeqByNameorNum("SEQ_DRUG_APPLYOUT", 12).toString());
		model1.setApplyNumber(applyNumber);
		//申请部门为患者所在科室
		if(model0!=null){
			model1.setDeptCode(model0.getPatientDept());
			SysDepartment dept=departmentDAO.getByCode(model0.getPatientDept());
			model1.setDeptName(dept.getDeptName());
		}
		
		model1.setOpType(5);
		//申请单号是读取keyValue表
		String yearLast = new SimpleDateFormat("yyMM").format(new Date());
		if(model0!=null){
			int value = keyvalueDAO.getVal(model0.getPatientDept(),"内部入库申请单号",yearLast);
			String applyBillcode = "0" + yearLast + value;//组成内部入库申请单号
			model1.setApplyBillcode(applyBillcode);
		}
		//申请人状态
		model1.setApplyOpercode(userid);
		model1.setApplyDate(date);
		//申请状态
		model1.setApplyState(0);
		//申请的出库量
		model1.setApplyNum(Double.valueOf(vo.getQuantity()));
		//付数
		model1.setDays(1);
		//是否预扣库存1是0否
		model1.setPreoutFlag(0);
		//收费状态：0未收费，1已收费
		model1.setChargeFlag(0);
		//医嘱发送类型（1集中发送，2临时发送，3全部）
		model1.setSendType(2);
		//摆药单分类设置为R退药单分类
		model1.setBillclassCode("R");
		//有效标记（1有效，0无效，2不摆药）
		model1.setValidState(1);
		/*操作状态*/
		model1.setCreateUser(userid);
		model1.setCreateDept(deptid);
		model1.setCreateTime(date);
		model1.setUpdateUser(userid);
		model1.setUpdateTime(date);
		return model1;
	}
	
	/***
	 * 根据药品明细表，生成退费申请记录
	 * @Title: obtainVoForMedicine 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param medicine0	: 根据药品id获取的药品明细实体
	 * @param vo	： 前台传输的退药数量
	 * @return InpatientCancelitem
	 * @version 1.0
	 * @throws Exception 
	 */
	public InpatientCancelitemNow obtainVoMobileForMedicine(InpatientMedicineListNow medicine0,ApplyVo vo,String empJobNo,String deptCode) throws Exception{
		InpatientCancelitemNow model = new InpatientCancelitemNow();
		//当前登录信息
		Date date = DateUtils.getCurrentTime();
		//申请归属标识 1门诊/2住院
		model.setApplyFlag(2);
		//住院流水号
		model.setInpatientNo(medicine0.getInpatientNo());
		model.setName(medicine0.getName());
		//是否婴儿用药
		model.setBabyFlag(medicine0.getBabyFlag());
		//所在科室
		model.setDeptCode(medicine0.getInhosDeptCode());
		model.setDeptName(medicine0.getInhosDeptname());
		//所在病区
		model.setCellCode(medicine0.getNurseCellCode());
		model.setNurseCellName(medicine0.getNurseCellName());
		//药品非药品标识：1药品，2非药品
		model.setDrugFlag(1);
		//项目code
		model.setItemCode(medicine0.getDrugCode());
		//项目名称
		model.setItemName(medicine0.getDrug_name());
		//规格
		model.setSpecs(medicine0.getSpecs());
		//零售价
		model.setSalePrice(medicine0.getUnitPrice());
		//申请的退费数量
		model.setQuantity(vo.getQuantity()*1.0);
		//计价单位
		model.setPriceUnit(medicine0.getCurrentUnit());
		//执行科室
		model.setExecDpcd(medicine0.getExecuteDeptCode());
		model.setExecDpcdName(medicine0.getExecuteDeptname());
		model.setOperCode(empJobNo);
		model.setOperDpcdName(empJobNo);
		model.setOperDate(date);
		model.setOperDpcd(deptCode);
		model.setOperDpcdName(deptCode);
		//处方号
		model.setRecipeNo(medicine0.getRecipeNo());
		//处方流水号
		model.setSequenceNo(medicine0.getSequenceNo());
		//退药确认标识
		model.setConfirmFlag(0);
		//退费确认标识
		model.setChargeFlag(0);
		model.setExtFlag(Integer.valueOf(vo.getExtFlag3()));
		model.setCreateUser(empJobNo);
		model.setCreateDept(deptCode);
		model.setCreateTime(date);
		model.setUpdateUser(empJobNo);
		model.setUpdateTime(date);
		
		model.setHospitalId(HisParameters.CURRENTHOSPITALID);//当前医院id
		//获取当前科室信息
		model.setAreaCode(inprePayDAO.getDeptArea(deptCode));//保存当前科室所在院区
		
		return model;
	}
	
	
	/***
	 * 根据住院非药品明细，产生病区退费申请
	 * @Title: obtainVoForItemlist 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param itemList
	 * @param vo
	 * @return InpatientCancelitem
	 * @version 1.0
	 * @throws Exception 
	 */
	public InpatientCancelitemNow obtainVoMobileForItemlist(InpatientItemListNow itemList,ApplyVo vo,String empJobNo,String deptCode) throws Exception{
		InpatientCancelitemNow model = new InpatientCancelitemNow();
		//当前登录信息
		Date date = DateUtils.getCurrentTime();
		//申请归属标识 1门诊/2住院
		model.setApplyFlag(2);
		//住院流水号
		model.setInpatientNo(itemList.getInpatientNo());
		model.setName(itemList.getName());
		//是否婴儿用药
		model.setBabyFlag(itemList.getBabyFlag());
		//所在科室
		model.setDeptCode(itemList.getInhosDeptcode());
		model.setDeptName(itemList.getInhosDeptname());
		//所在病区
		model.setCellCode(itemList.getNurseCellCode());
		model.setNurseCellName(itemList.getNurseCellName());
		//药品非药品标识：1药品，2非药品
		model.setDrugFlag(2);
		//项目code
		model.setItemCode(itemList.getItemCode());
		//项目名称
		model.setItemName(itemList.getItemName());
		//零售价
		model.setSalePrice(itemList.getUnitPrice());
		//申请的退费数量
		model.setQuantity(vo.getQuantity()*1.0);
		//计价单位
		model.setPriceUnit(itemList.getCurrentUnit());
		//执行科室
		model.setExecDpcd(itemList.getExecuteDeptcode());
		model.setExecDpcdName(itemList.getExecuteDeptname());
		model.setOperCode(empJobNo);
		model.setOperName(empJobNo);
		model.setOperDate(date);
		model.setOperDpcd(deptCode);
		model.setOperDpcdName(deptCode);
		//处方号
		model.setRecipeNo(itemList.getRecipeNo());
		//处方流水号
		model.setSequenceNo(itemList.getSequenceNo());
		//未退费标识
		model.setChargeFlag(0);
		model.setCreateUser(empJobNo);
		model.setCreateDept(deptCode);
		model.setCreateTime(date);
		model.setUpdateUser(empJobNo);
		model.setUpdateTime(date);
		
		model.setHospitalId(HisParameters.CURRENTHOSPITALID);//当前医院id
		//获取当前科室信息
		model.setAreaCode(inprePayDAO.getDeptArea(deptCode));//保存当前科室所在院区
		return model;
	}
}
