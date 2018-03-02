package cn.honry.inner.drug.applyout.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.drug.applyout.dao.ApplyoutInInterDAO;
import cn.honry.inner.drug.applyout.service.ApplyoutInInterService;
import cn.honry.inner.drug.applyout.vo.VinpatientApplyout;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.manufacturer.dao.ManufacturerInnerDao;
import cn.honry.inner.drug.stockInfo.dao.BusinessStockInfoInInterDAO;
import cn.honry.inner.outpatient.registration.dao.RegistrationInInterDAO;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
@Service("applyoutInInterService")
@Transactional
public class ApplyoutInInterServiceImpl implements ApplyoutInInterService{
	/**
	 * 请求出库DAO
	 */
	@Autowired
	private ApplyoutInInterDAO applyoutInInterDAO;
	
	/**
	 * 药品信息DAO
	 */
	@Autowired
	private DrugInfoInInerDAO drugInfoInInerDAO;
	
	/**
	 * 键值对DAO
	 */
	@Autowired
	private KeyvalueInInterDAO keyvalueInInterDAO;
	
	/** 
	* @Fields codeInInterDAO : 编码接口DAO 
	*/ 
	@Autowired
	private CodeInInterDAO codeInInterDAO;
	
	/** 
	 * @Fields deptInInterDAO : 科室接口DAO 
	 */ 
	@Autowired
	private DeptInInterDAO deptInInterDAO;
	/** 
	 * @Fields deptInInterDAO : 科室接口DAO 
	 */ 
	@Autowired
	private RegistrationInInterDAO registrationInInterDAO;
	
	/**
	 * 库存维护表DAO
	 */
	@Autowired
	private BusinessStockInfoInInterDAO businessStockInfoInInterDAO;
	
	/** 
	* @Fields manufacturerInnerDao : 生产厂家DAO
	*/ 
	@Autowired
	private ManufacturerInnerDao manufacturerInnerDao;

	@Override
	public DrugApplyoutNow get(String arg0) {
		return applyoutInInterDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(DrugApplyoutNow applyout) {
		applyoutInInterDAO.save(applyout);
	}
	
	@Override
	public Map<String, String> outDrugInterface(DrugApplyoutNow applyOut,String type, Integer start) {
		User user =(User)SessionUtils.getCurrentUserFromShiroSession();
		String loginUser = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//当前登录人
		String loginUserName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();//当前登录人
		//判断传过来的出库申请数据时候为空 为空时返回map提示
			Map<String,String> mapOut = new HashMap<String, String>(); 
			DrugInfo drugInfo = applyoutInInterDAO.queryDrugInfo(applyOut.getDrugCode());
			List<DrugStorage> storageList = new ArrayList<DrugStorage>();
			//生成出库单号一个药品用一个
			String outBillCode = applyoutInInterDAO.getSeqByNameorNum("SEQ_OUT_BILL_CODE", 20);
			//申请数量
			Double drugQty = applyOut.getApplyNum();
			//判断是否是最小单位 如果是包装单位 需*包装单位数量
			if("1".equals(applyOut.getShowFlag())){
				drugQty *= drugInfo.getPackagingnum();
			}
			//初始化批次号
			String groupCode = "";
			//初始化批号
			String batchNo = "";
			//根据批次号排序查询到的库存记录
			storageList = applyoutInInterDAO.findDrugStorageByDrugId(applyOut.getDrugDeptCode(),applyOut.getDrugCode());
			
			if(storageList.size()>0){
				//按照批次循环生成出库记录表 与实扣库存
				//单内序号
				int serialCode = 0;
				for(DrugStorage storage:storageList){
					serialCode++;
					if(drugQty>0){
						//提出申请数量减库存数量的值 为应用数量
						Double sum = drugQty-storage.getStoreSum();
						DrugOutstoreNow outstore =  new DrugOutstoreNow();
						outstore.setId(null);//id
						outstore.setDrugDeptCode(applyOut.getDrugDeptCode());//出库科室编码
						outstore.setOutBillCode(outBillCode);//出库单号
						outstore.setSerialCode(serialCode+"");//单内序号
						outstore.setGroupCode(storage.getGroupCode());//批次号
						outstore.setOpType(type);//出库分类
						outstore.setOutType("8");//科室领药
						outstore.setDrugCode(applyOut.getDrugCode());//药品ID
						outstore.setTradeName(drugInfo.getName());//药品名字
						outstore.setDrugType(drugInfo.getDrugType());//药品类别
						outstore.setDrugQuality(drugInfo.getDrugNature());//药品性质
						outstore.setSpecs(drugInfo.getSpec());//规格
						outstore.setPackUnit(drugInfo.getDrugPackagingunit());//包装单位
						outstore.setPackQty(drugInfo.getPackagingnum().doubleValue());//包装数量
						outstore.setMinUnit(drugInfo.getUnit());//最小单位
						outstore.setShowFlag(applyOut.getShowFlag());//类型
						outstore.setBatchNo(storage.getBatchNo());//批号
						outstore.setProducerCode(drugInfo.getDrugManufacturer());//生产厂家
						outstore.setCompanyCode(drugInfo.getDrugSupplycompany());//供货公司
						outstore.setRetailPrice(drugInfo.getDrugRetailprice());//零售价
						outstore.setWholesalePrice(drugInfo.getDrugWholesaleprice());//批发价
						outstore.setPurchasePrice(drugInfo.getDrugPurchaseprice());//购入价
						
						outstore.setDrugedCode(loginUser);//发药人
						outstore.setDrugedName(loginUserName);//发药人姓名
						
						//判断应用数量时候大于0
						if(sum<0){//扣完库存的情况下（就是申请数量为0的情况下）
							//应用数量就等于申请数量
							sum = drugQty;
							if("5".equals(type)){
								outstore.setOutNum(-sum);//出库数量
								outstore.setStoreNum(-(storage.getStoreSum()-sum));//出库后库存数量
								outstore.setStoreCost(-((storage.getStoreSum()-sum)*drugInfo.getDrugRetailprice()));//出库后库存总金额
							}else{
								outstore.setOutNum(sum);//出库数量
								outstore.setStoreNum(storage.getStoreSum()-sum);//出库后库存数量
								outstore.setStoreCost((storage.getStoreSum()-sum)*drugInfo.getDrugRetailprice());//出库后库存总金额
							}
							drugQty = 0.0;
							groupCode = outstore.getGroupCode();//最后一个批次号
							batchNo = outstore.getBatchNo();//最后一个批号
							if(start==2){
								if("5".equals(type)){
									outstore.setOutNum(-sum);//出库数量
									outstore.setStoreNum(-(storage.getStoreSum()-sum));//出库后库存数量
									outstore.setStoreCost(-((storage.getStoreSum()-sum)*drugInfo.getDrugRetailprice()));//出库后库存总金额
								}else{
									storage.setStoreSum(storage.getStoreSum()-sum);//库存表中库存数量
									storage.setStoreCost((storage.getStoreSum()-sum)*drugInfo.getDrugRetailprice());//库存金额
									if("1".equals(type)){
										outstore.setRecipeNo(applyOut.getRecipeNo());//处方号
										outstore.setSequenceNo(applyOut.getSequenceNo().toString());
										outstore.setOutDate(DateUtils.parseDateY_M_D_SLASH(DateUtils.formatDateY_M_SLASH(new Date())));//出库时间
									}
								}
								storage.setOperCode(type);
								applyoutInInterDAO.save(storage);
							}
						}else{
							if("5".equals(type)){
								outstore.setOutNum(-sum);//出库数量
							}else{
								outstore.setOutNum(-sum);//出库数量
							}
							outstore.setStoreNum(0.0);//出库后库存数量
							outstore.setStoreCost(0.0);//出库后库存总金额
							drugQty = drugQty - outstore.getStoreNum();
							if(start==1){
								storage.setStoreSum(0.0);//库存表中库存数量
								storage.setStoreCost(0.0);//库存金额
								storage.setOperCode(type);
								applyoutInInterDAO.save(storage);
							}
						}
						outstore.setSpecialFlag(0);//特殊标记，1是，0否
						outstore.setOutState(start);//出库状态 0申请、1审批、2核准
						outstore.setDrugStorageCode(applyOut.getDeptCode());//领取科室
						SysEmployee emp = (SysEmployee) SessionUtils.getCurrentEmployeeFromShiroSession();
						outstore.setApproveOpercode(emp.getJobNo());//核准人
						outstore.setApproveDate(DateUtils.getCurrentTime());//核准日期
						outstore.setDrugedBill(applyOut.getDrugedBill());//摆药单号
						outstore.setCreateTime(DateUtils.getCurrentTime());
						outstore.setCreateUser(user.getAccount());
						SysDepartment dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
						outstore.setCreateDept(dept.getDeptCode());
						applyoutInInterDAO.save(outstore);
					}
				}
			}
			mapOut.put("resMeg", "success");
			mapOut.put("resCode", "出库成功");
			mapOut.put("outBillCode", outBillCode);
			mapOut.put("groupCode", groupCode);
			mapOut.put("batchNo", batchNo);
			return mapOut;
	}
	
	@Override
	public Map<String, String> actualOutDrugInterface(DrugApplyoutNow applyout, String type) {
		String loginUser = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//当前登录人
		String loginDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();//当前登录科室
		String loginUserName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();//当前登录人
		String drugCode = applyout.getDrugCode();//药品code
		String drugDeptCode = applyout.getDrugDeptCode();//库存科室code
		String deptCode = applyout.getDeptCode();//申请科室code
		DrugInfo info = drugInfoInInerDAO.getByCode(drugCode);//药品信息
		String name = info.getDrugCommonname();//药品名
		Double applyoutNum = 0d;//申请数量
		Double applyNum;//申请出库数量  批次出库数量
		String groupCode = "";//初始化批次号
		String batchNo = "";//初始化批号
		Integer serialCode = 0;//单内序号
		Double stoCost;//出库后库存金额
		Double reCost;//零售金额
		Double whCost;//批发金额
		Double puCost;//购入金额
		
		Integer showFlag = info.getShowFlag();//药品信息中的单位显示标记 0：最小单位 1：包装单位
		/**
		 * 以药品信息表中的showflag标记格式化申请数量
		 * 出库申请表和药品信息的showflag相同  则直接使用申请表中的申请数量
		 * 出库申请表和药品信息的showflag不同且药品信息的showflag为1  则使用申请表中的申请数量/药品包装数量
		 * 出库申请表和药品信息的showflag不同且药品信息的showflag为0  则使用申请表中的申请数量*药品包装数量
		 */
		if ((applyout.getShowFlag() - showFlag) == 0) {//如果出库申请表和药品信息的showflag相同  则直接使用申请表中的申请数量
			applyoutNum = applyout.getApplyNum();
		}else if (showFlag == 1) {//如果出库申请表和药品信息的showflag不同且药品信息的showflag为1  则使用申请表中的申请数量/药品包装数量
			applyoutNum = applyout.getApplyNum() / info.getPackagingnum();
		}else if (showFlag == 0) {//如果出库申请表和药品信息的showflag不同且药品信息的showflag为0  则使用申请表中的申请数量*药品包装数量
			applyoutNum = applyout.getApplyNum() / info.getPackagingnum();
		}
		Map<String,String> mapOut = new HashMap<String, String>();//返回数据用的map
		
		/**
		 * 判断药品库存是否充足
		 */
		Double stockNum = businessStockInfoInInterDAO.getDrugStockInfoStoreNum(drugDeptCode, drugCode);
		if(stockNum - applyoutNum < 0){
			mapOut.put("resMeg", "error");
			mapOut.put("resCode", "【" + name + "】库存不足！");
			return mapOut;
		}
		List<DrugStorage> list = applyoutInInterDAO.findDrugStorageByDrugId(drugDeptCode, drugCode);
		if (list.size() == 0) {
			mapOut.put("resMeg", "error");
			mapOut.put("resCode", "【" + name + "】库存不足！");
			return mapOut;
		}
		/*避免影响速度暂时注掉
		 * String minUnit = codeInInterDAO.getDictionaryByCode(HisParameters.DRUGMINUNIT, info.getUnit()).getName();//药品最小单位
		String pacUnit = codeInInterDAO.getDictionaryByCode(HisParameters.DRUGPACKUNIT, info.getDrugPackagingunit()).getName();//药品最小单位
		Map<String, Object> producerMap = manufacturerInnerDao.findAllMap();//生产厂家MAP
		*/
		String produce = "" ;//生产厂家
		//出库单流水号
		String outBillCode = applyoutInInterDAO.getSequece("SEQ_OUT_BILL_CODE").toString();//出库单流水号
		/**
		 * 循环操作数据
		 *   生成出库记录，修改库存明细
		 */
		for (DrugStorage storage : list) {
			/*避免影响速度暂时注掉
			 * if (StringUtils.isNotBlank(storage.getProducerCode())) {
				produce = (String) producerMap.get(storage.getProducerCode());
			}*/
			serialCode ++;//单内序号自增
			Double storeNum = storage.getStoreSum();//本批次库存数量
			DrugOutstoreNow outstore = new DrugOutstoreNow();
			outstore.setId(null);//id
			outstore.setDrugDeptCode(drugDeptCode);//出库科室编码
			outstore.setOutBillCode(outBillCode);//出库单号
			outstore.setSerialCode(serialCode.toString());//单内序号
			outstore.setGroupCode(storage.getGroupCode());//批次号
			outstore.setOpType(type);//出库分类
			outstore.setOutType("8");//科室领药
			outstore.setDrugCode(drugCode);//药品ID
			outstore.setTradeName(info.getDrugCommonname());//药品商品名
			outstore.setDrugType(info.getDrugType());//药品类别
			outstore.setDrugQuality(info.getDrugNature());//药品性质
			outstore.setSpecs(info.getSpec());//规格
			outstore.setPackUnit(applyout.getPackUnit());//包装单位
			outstore.setPackQty(info.getPackagingnum().doubleValue());//包装数量
			outstore.setMinUnit(applyout.getMinUnit());//最小单位
			outstore.setBatchNo(storage.getBatchNo());//批号
			outstore.setValidDate(storage.getValidDate());//有效期
			outstore.setProducerCode(info.getDrugManufacturer());//生产厂家
			outstore.setCompanyCode(applyout.getDrugDeptCode());//供货公司
			outstore.setRetailPrice(info.getDrugRetailprice());//零售价
			outstore.setWholesalePrice(info.getDrugWholesaleprice());//批发价
			outstore.setPurchasePrice(info.getDrugPurchaseprice());//购入价
			/**
			 * 该批次不够（该批次扣光）
			 */
			if (applyoutNum >= storeNum) {//该批次不够（该批次扣光）
				applyNum = storeNum;//本批次出库数量
				applyoutNum = applyoutNum - storeNum;//更新总申请数量
				storeNum = 0d;//剩余库存量
			}else {
				/**
				 * 本批次够扣   库存-申请>0
				 */
				applyNum = applyoutNum;//本批次出库数量
				applyoutNum = 0d;//更新总申请数量
				storeNum = storeNum - applyNum;//剩余库存量
			}
			/**
			 * 根据药品信息showflag计算金额
			 */
			if (showFlag == 0) {//如果库存数量为最小单位
				outstore.setShowFlag(0);//显示单位标记0：最小单位 1：包装单位
				outstore.setShowUnit(outstore.getMinUnit());
				stoCost = info.getDrugRetailprice() / info.getPackagingnum() * (storeNum);//出库后库存金额
				if (info.getDrugWholesaleprice() != null) {//零售价
					reCost = info.getDrugRetailprice() / info.getPackagingnum() * applyoutNum;//零售金额
				}else {
					reCost = 0d;
				}
				if (info.getDrugWholesaleprice() != null) {//批发价
					whCost = info.getDrugWholesaleprice() / info.getPackagingnum() * applyoutNum;//批发金额
				}else {
					whCost = 0d;
				}
				if (info.getDrugPurchaseprice() != null) {//购入价
					puCost = info.getDrugPurchaseprice() / info.getPackagingnum() * applyoutNum;//购入金额
				}else {
					puCost = 0d;
				}
			}else {//如果库存数量为包装单位
				outstore.setShowFlag(1);//显示单位标记0：最小单位 1：包装单位
				outstore.setShowUnit(outstore.getPackUnit());
				stoCost = info.getDrugRetailprice() * (storeNum);//出库后库存金额
				if (info.getDrugWholesaleprice() != null) {//零售价
					reCost = info.getDrugRetailprice() * applyoutNum;//零售金额
				}else {
					reCost = 0d;
				}
				if (info.getDrugWholesaleprice() != null) {//批发价
					whCost = info.getDrugWholesaleprice() * applyoutNum;//批发金额
				}else {
					whCost = 0d;
				}
				if (info.getDrugPurchaseprice() != null) {//购入价
					puCost = info.getDrugPurchaseprice() * applyoutNum;//购入金额
				}else {
					puCost = 0d;
				}
			}
			/**
			 * 补全剩余字段
			 */
			outstore.setOutNum(applyNum);//出库数量
			outstore.setRetailCost(reCost);//零售金额
			outstore.setWholesaleCost(whCost);//批发金额
			outstore.setPurchaseCost(puCost);//购入金额
			outstore.setStoreNum(storeNum);//出库后库存数量
			outstore.setStoreCost(stoCost);//出库后库存金额
			outstore.setOutState(2);//出库状态 0申请、1审批、2核准
			//这里放的是每个批次的出库数量
			outstore.setApplyNum(applyNum);//申请出库量
			outstore.setApplyOpercode(applyout.getApplyOpercode());//申请出库人
			if (applyout.getApplyDate() != null) {
				outstore.setApplyDate(DateUtils.formatDateY_M(applyout.getApplyDate()));//申请出库日期
			}
			outstore.setApproveOpercode(loginUser);//核准人
			outstore.setApproveDate(new Date());//核准时间
			outstore.setPlaceCode(storage.getPlaceCode());//货位号
			outstore.setReturnNum(0d);//退库数量
			outstore.setDrugedBill(applyout.getDrugedBill());//摆药单号
			//存的开方科室
			outstore.setDrugStorageCode(deptCode);//领药单位
			outstore.setRecipeNo(applyout.getRecipeNo());//处方号
			outstore.setSequenceNo(applyout.getSequenceNo().toString());//处方流水号
			//这里面放的是住院号或者门诊号
			outstore.setGetPerson(applyout.getPatientId());//领药人
			outstore.setStrikeFlag(0);//冲账标志0表示没有冲账1表示被冲账
			outstore.setOutDate(new Date());//出库记录发生时间
			outstore.setDrugedCode(loginUser);//发药人
			outstore.setDrugedDate(new Date());//发药时间
			outstore.setDrugDeptName(applyout.getDrugDeptName());//出库科室名
			outstore.setApproveOpername(loginUserName);//核准人姓名
			//存的开方科室
			outstore.setDrugStorageName(applyout.getDeptCodeName());//领药单位名称
			RegistrationNow registrationNow = registrationInInterDAO.getRegistration(outstore.getGetPerson());
			outstore.setGetPersonName(registrationNow.getPatientName());//领药人姓名
			outstore.setDrugedName(loginUserName);//发药人姓名
			outstore.setProducerName(produce);//生产厂家名称
			outstore.setProducerName(applyout.getDrugDeptName());//供货单位名称
			outstore.setOpType(type);//1门诊摆药 2内部入库 3 门诊退药 4 住院摆药 
			storage.setStoreSum(storeNum);//库存数量
			storage.setStoreCost(stoCost);//库存金额
			
			/**
			 * 更新库存明细
			 */
			storage.setTargetDept(deptCode);//目标科室(保留最后一次库存数量变化时,产生变化的科室) ,药房或药库
			storage.setBillCode(outBillCode);//单据号(保留最后一次库存数量变化时,产生变化的单据号)
			storage.setSerialCode(serialCode);//单据序号(保留最后一次库存数量变化时,产生变化的单内序号)
			storage.setUpdateTime(new Date());
			SysDepartment department = deptInInterDAO.getByCode(drugDeptCode);
			storage.setUpdateUser(loginUser);
			applyoutInInterDAO.update(storage);
			
			/**
			 * 保存出库记录
			 */
			outstore.setCreateDept(loginDept);
			outstore.setCreateTime(new Date());
			outstore.setCreateUser(loginUser);
			outstore.setAreaCode(department.getAreaCode());
			outstore.setHospitalId(Integer.valueOf(HisParameters.CURRENTHOSPITALID));
			applyoutInInterDAO.save(outstore);
			
			/**
			 * 全部扣完时保存最后操作明细的批次号和批号
			 */
			if (applyoutNum == 0) {
				groupCode = storage.getGroupCode();//批次号
				batchNo = storage.getBatchNo();//批号
				break;
			}
			
		}
		mapOut.put("resMeg", "success");
		mapOut.put("resCode", "出库成功");
		mapOut.put("outBillCode", outBillCode);
		mapOut.put("groupCode", groupCode);
		mapOut.put("batchNo", batchNo);
		return mapOut;
	}
	
	/**  
	 *  
	 * @Description：  查询患者树
	 * @Author：zhenglin
	 * @CreateDate：2015-12-22 下午05:37:12  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-8-22 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public String queryPatientTree(String deptId) {
		String type=applyoutInInterDAO.queryState(deptId).getDeptType();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		List<InpatientInfoNow> infoList = applyoutInInterDAO.queryPatient(deptId,type);
		//根节点
		TreeJson gTreeJson = new TreeJson();
		gTreeJson.setId("1");
		gTreeJson.setText("本区患者");
		gTreeJson.setIconCls("icon-house");
		Map<String,String> gAttMap = new HashMap<String, String>();
		gTreeJson.setAttributes(gAttMap);
		treeJsonList.add(gTreeJson);
		TreeJson fTreeJson = null;
		Map<String,String> fAttMap = null;
		if(infoList!=null&&infoList.size()>0){
			for(InpatientInfoNow infonfo : infoList){
				//二级节点(患者)
				fTreeJson = new TreeJson();
				fTreeJson.setId(infonfo.getInpatientNo());
				fTreeJson.setText(infonfo.getPatientName());
				if("3".equals(infonfo.getReportSex())||"1".equals(infonfo.getReportSex())){
					fTreeJson.setIconCls("icon-user_b");
				}
				else{
					fTreeJson.setIconCls("icon-user_female");
				}
				fTreeJson.setState("open");
				fAttMap = new HashMap<String, String>();
				fAttMap.put("pid", "1");
				fAttMap.put("no",infonfo.getInpatientNo());
				fTreeJson.setAttributes(fAttMap);
				treeJsonList.add(fTreeJson);
			}
		}
		treeJson =  TreeJson.formatTree(treeJsonList);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		return gson.toJson(treeJson);
	}

	@Override
	public List<VinpatientApplyout> queryVinpatientApplyout(String deptId,
			String type, String page, String rows,String tradeName,String inpatientNo,String endDate,String beginDate) {
		return applyoutInInterDAO.queryVinpatientApplyout(deptId, type, page, rows,tradeName,inpatientNo,endDate,beginDate);
	}

	@Override
	public int qqueryVinpatientApplyoutTotal(String deptId, String type,String tradeName,String inpatientNo,String endDate,String beginDate) {
		return applyoutInInterDAO.qqueryVinpatientApplyoutTotal(deptId, type,tradeName,inpatientNo,endDate,beginDate);
	}

	@Override
	public SysDepartment querySysDepartment(String id) {
		return applyoutInInterDAO.querySysDepartment(id);
	}

	@Override
	public User queryUser(String id) {
		return applyoutInInterDAO.queryUser(id);
	}

	@Override
	public DrugBillclass queryDrugBillclass(String id) {
		return applyoutInInterDAO.queryDrugBillclass(id);
	}

	@Override
	public String getSequece(String name) {
		return applyoutInInterDAO.getSequece(name);
	}

	@Override
	public List<DrugApplyoutNow> queryDrugApplyoutNow(DrugApplyoutNow drugApplyoutNow, int flag) {
		return applyoutInInterDAO.queryDrugApplyoutNow(drugApplyoutNow, flag);
	}
	
}
