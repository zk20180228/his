package cn.honry.outpatient.itemlist.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientItemlist;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.stack.dao.StackInInterDAO;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.patient.account.dao.AccountInInterDAO;
import cn.honry.outpatient.feedetail.dao.FeedetailDAO;
import cn.honry.outpatient.grade.dao.GradeDAO;
import cn.honry.outpatient.info.dao.RegisterInfoDAO;
import cn.honry.outpatient.itemlist.dao.ItemlistDAO;
import cn.honry.outpatient.itemlist.service.ItemlistService;
import cn.honry.outpatient.itemlist.vo.UndrugVo;
import cn.honry.outpatient.recipedetail.dao.RecipedetailDAO;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("itemlistService")
@Transactional
@SuppressWarnings({ "all" })
public class ItemlistServiceImpl implements ItemlistService{

	@Resource
	private UndrugInInterDAO undrugInInterDAO;//非药品DAO
	@Resource
	private CodeInInterDAO innerCodeDao;//最小费用DAO
	@Resource
	private StackInInterDAO stackInInterDAO;//组套DAO
	@Resource
	private StackinfoInInterDAO stackinfoInInterDAO;//组套详情DAO
	@Resource
	private FeedetailDAO feedetailDAO;//医嘱明细DAO
	@Resource
	private RecipedetailDAO recipedetailDAO;//医嘱DAO
	@Resource
	private AccountInInterDAO accountInInterDAO;//用户账户DAO
	@Resource
	private GradeDAO gradeDAO;//挂号级别DAO
	@Resource
	private RegisterInfoDAO registerInfoDAO;//挂号DAO
	@Autowired
	@Qualifier(value = "itemlistDAO")
	private ItemlistDAO itemlistDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public OutpatientItemlist get(String id) {
		return itemlistDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OutpatientItemlist entity) {
		
	}
	
	/**  
	 *  
	 * @Description：  查询最小费用下的非药品tree
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public String queryUndrugByMinimum(String id) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			List<BusinessDictionary> drugminimumcostList =null;
			drugminimumcostList = innerCodeDao.getDictionary("drugMinimumcost");
			//根节点
			TreeJson gTreeJson = new TreeJson();
			gTreeJson.setId("0");
			gTreeJson.setText("非药品信息");
			Map<String,String> gAttMap = new HashMap<String, String>();
			gTreeJson.setAttributes(gAttMap);
			treeJsonList.add(gTreeJson);
			TreeJson fTreeJson = null;
			Map<String,String> fAttMap = null;
			if(drugminimumcostList!=null&&drugminimumcostList.size()>0){
				for(BusinessDictionary drugminimumcost : drugminimumcostList){
					//二级节点(费用)
					fTreeJson = new TreeJson();
					fTreeJson.setId(drugminimumcost.getId());
					fTreeJson.setText(drugminimumcost.getName());
					fTreeJson.setState("closed");
					fAttMap = new HashMap<String, String>();
					fAttMap.put("pid", "0");
					fAttMap.put("isUnDrug","0");
					fTreeJson.setAttributes(fAttMap);
					treeJsonList.add(fTreeJson);
				}
			}
			treeJson =  TreeJson.formatTree(treeJsonList);
		}else{
			List<DrugUndrug> undrugList =null;
			undrugList = undrugInInterDAO.queryUndrugByDrugminimumcost(id);
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			if(undrugList!=null&&undrugList.size()>0){
				for(DrugUndrug drugUndrug : undrugList){
					cTreeJson = new TreeJson();
					cTreeJson.setId(drugUndrug.getId());
					cTreeJson.setText(drugUndrug.getName());
					cTreeJson.setState("open");
					cAttMap = new HashMap<String, String>();
					cAttMap.put("pid", id);
					cAttMap.put("isUnDrug","1");
					cAttMap.put("feeCode",(StringUtils.isBlank(drugUndrug.getUndrugMinimumcost())?"":drugUndrug.getUndrugMinimumcost()));//最小费用代码
					cAttMap.put("itemName",(StringUtils.isBlank(drugUndrug.getName())?"":drugUndrug.getName()));//项目名称
					cAttMap.put("defaultprice", (drugUndrug.getDefaultprice()==null?"0.00":drugUndrug.getDefaultprice().toString()));//默认价
					cAttMap.put("childrenprice",(drugUndrug.getChildrenprice()==null?"0.00":drugUndrug.getChildrenprice().toString()));//儿童价
					cAttMap.put("specialprice",(drugUndrug.getSpecialprice()==null?"0.00":drugUndrug.getSpecialprice().toString()));//特诊价
					cAttMap.put("undrugUnit",(StringUtils.isBlank(drugUndrug.getUnit())?"":drugUndrug.getUnit()));//单位
					cAttMap.put("spec",(StringUtils.isBlank(drugUndrug.getSpec())?"":drugUndrug.getSpec()));//规格
					cAttMap.put("undrugIsownexpense",(drugUndrug.getUndrugIsownexpense()==null?"":drugUndrug.getUndrugIsownexpense().toString()));//是否自费
					cTreeJson.setAttributes(cAttMap);
					treeJsonList.add(cTreeJson);
				}
			}
			treeJson.addAll(treeJsonList);
		}
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.create();
		return gson.toJson(treeJson);
	}
	
	/**  
	 * @Description： 根据病历号 患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public Patient queryRegisterInfoByCaseNo(String midicalrecordId) {
		Patient patient = itemlistDAO.queryRegisterInfoByCaseNo(midicalrecordId);
		return patient;
	}

	@Override
	public String queryUndrugStackTree(String id) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			//根节点
			TreeJson gTreeJson = new TreeJson();
			gTreeJson.setId("0");
			gTreeJson.setText("非药品组套信息");
			treeJsonList.add(gTreeJson);
			
			TreeJson grTreeJson = new TreeJson();
			grTreeJson.setId("3");
			grTreeJson.setText("个人组套");
			grTreeJson.setState("closed");
			Map<String,String> grAttMap = new HashMap<String, String>();
			grAttMap.put("pid", "0");
			grTreeJson.setAttributes(grAttMap);
			treeJsonList.add(grTreeJson);
			
			TreeJson ksTreeJson = new TreeJson();
			ksTreeJson.setId("2");
			ksTreeJson.setText("科室组套");
			ksTreeJson.setState("closed");
			Map<String,String> ksAttMap = new HashMap<String, String>();
			ksAttMap.put("pid", "0");
			ksTreeJson.setAttributes(ksAttMap);
			treeJsonList.add(ksTreeJson);
			
			TreeJson qyTreeJson = new TreeJson();
			qyTreeJson.setId("1");
			qyTreeJson.setText("全院组套");
			qyTreeJson.setState("closed");
			Map<String,String> qyAttMap = new HashMap<String, String>();
			qyAttMap.put("pid", "0");
			qyTreeJson.setAttributes(qyAttMap);
			treeJsonList.add(qyTreeJson);
			
			treeJson =  TreeJson.formatTree(treeJsonList);
		}else{
			List<BusinessStack> businessStackList = null;
			businessStackList = stackInInterDAO.getStackByType(2,id);
			if(businessStackList!=null && businessStackList.size()>0){
				TreeJson cTreeJson = null;
				Map<String,String> cAttMap = null;
				for(BusinessStack stack : businessStackList){
					cTreeJson = new TreeJson();
					cTreeJson.setId(stack.getId());
					cTreeJson.setText(stack.getName());
					cAttMap = new HashMap<String, String>();
					cAttMap.put("pid", id);
					cAttMap.put("isUnDrug","1");
					cTreeJson.setAttributes(cAttMap);
					treeJsonList.add(cTreeJson);
				}
			}
			treeJson.addAll(treeJsonList);
		}
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.create();
		return gson.toJson(treeJson);
	}

	@Override
	public List<BusinessStackinfo> queryUndrugStackInfo(String id) {
		List<BusinessStackinfo> listInfo = new ArrayList<BusinessStackinfo>();
		List<BusinessStackinfo> list = stackinfoInInterDAO.findStackInfoByFk(id);
		List<DrugUndrug> undrugList = undrugInInterDAO.likeUndrugMap();
		Map<String,String> map = new HashMap<String, String>();
		if(undrugList!=null&&undrugList.size()>0){
			for(DrugUndrug undrug : undrugList){
				map.put(undrug.getId(), undrug.getName());
			}
		}
		if(list!=null&&list.size()>0){
			for(BusinessStackinfo info : list){
				if(StringUtils.isNotBlank(map.get(info.getStackInfoItemId()))){
					info.setStackInfoItemName(map.get(info.getStackInfoItemId()));
				}
			}
			listInfo.addAll(list);
		}
		return listInfo;
	}

	@Override
	public void saveItemlist(List<UndrugVo> infoList, PatientAccount account,OutpatientItemlist itemlist,Double sumMoney){
			String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			OutpatientRecipedetailNow recipedetail = new OutpatientRecipedetailNow();//门诊处方
			List<OutpatientFeedetailNow> feedetailList = new ArrayList<OutpatientFeedetailNow>();//门诊处方明细表
			List<OutpatientItemlist> itemlistList = new ArrayList<OutpatientItemlist>();//门诊非药品明细表
			RegisterInfo info = registerInfoDAO.queryRegisterInfoByCaseNo(itemlist.getCaseNo());//获得挂号信息
			//门诊处方处理
			recipedetail.setId(null);
			recipedetail.setSeeNo(info.getOrder()+"");//看诊序号
			recipedetail.setClinicCode(info.getNo());//门诊号
			recipedetail.setPatientNo(itemlist.getCaseNo());//病历号
			recipedetail.setRegDate(info.getDate());//挂号日期
			recipedetail.setRegDept(info.getDept());//挂号科室
			//recipedetail.setItemCode("");//项目代码
			//recipedetail.setItemName("");//项目名称
			//recipedetail.setSpecs("");//规格
			recipedetail.setDrugFlag(2);//1药品，2非药品
			//recipedetail.setClassCode("");//系统类别
			//recipedetail.setFeeCode();//最小费用代码
			//recipedetail.setUnitPrice();//单价
			//recipedetail.setQty()//开立数量
			//recipedetail.setDays();//付数
			//recipedetail.setPackQty();//包装数量
			//recipedetail.setItemUnit();//计价单位
			//recipedetail.setOwnCost();//自费金额
			//recipedetail.setPayCost();//自负金额
			//recipedetail.setPubCost();//报销金额
			//recipedetail.setBaseDose();//基本剂量
			//recipedetail.setSelfMade();//自制药
			//recipedetail.setDrugQuanlity();//药品性质
			//recipedetail.setOnceDose();//每次用量
			//recipedetail.setOnceUnit();//每次用量单位
			//recipedetail.setDoseModelCode();//剂型代码
			//recipedetail.setFrequencyCode();//频次
			//recipedetail.setUsageCode();//用法
			//recipedetail.setExecDpcd();//执行科室代码
			//recipedetail.setMainDrug();//主药标志
			//recipedetail.setCombNo();//组合号
			//recipedetail.setHypotest();//皮试
			//recipedetail.setInjectNumber();//院内注射次数
			//recipedetail.setRemark();//备注
			//recipedetail.setDoctCode();//开立医生
			//recipedetail.setDoctDpcd();//医生科室
			//recipedetail.setOperDate();//开立时间
			//recipedetail.setStatus();//处方状态
			//recipedetail.setCancelUserid();//作废人
			//recipedetail.setCancelDate();//作废时间
			//recipedetail.setEmcFlag();//加急标记
			//recipedetail.setLabType();//样本类型
			//recipedetail.setCheckBody();//检体
			//recipedetail.setApplyNo();//申请单号
			//recipedetail.setSubtblFlag();//附材
			//recipedetail.setNeedConfirm();//是否需要确认
			//recipedetail.setConfirmCode();//确认人
			//recipedetail.setConfirmDept();//确认科室
			//recipedetail.setConfirmDate();//确认时间
			//recipedetail.setChargeFlag();//0未收费/1收费
			//recipedetail.setChargeCode();//收费员
			//recipedetail.setChargeDate();//收费时间
			//recipedetail.setRecipeNo();//处方号
			//recipedetail.setPhamarcyCode();//发药药房
			//recipedetail.setMinunitFlag();//开立单位是否是最小单位 1 是 0 不是
			//recipedetail.setDataorder();//排序
			//recipedetail.setPrintFlag();//处方打印标志
			recipedetail.setCreateUser(userId);
			recipedetail.setCreateDept(deptId);
			recipedetail.setCreateTime(new Date());
			recipedetailDAO.save(recipedetail);//保存门诊处方
			
			//门诊处方明细表处理
			//门诊非药品明细表处理
			OutpatientFeedetailNow feedetail = null;
			OutpatientItemlist item = null;
			for(UndrugVo vo : infoList){
				//查询非药品信息
				DrugUndrug drugUndrug=null;
				drugUndrug = undrugInInterDAO.get(vo.getItemCode());
				//门诊处方明细表处理
				feedetail = new OutpatientFeedetailNow();
				feedetail.setId(null);
				feedetail.setRecipeNo(recipedetail.getId());//处方号
				//feedetail.setSequenceNo();//处方内项目流水号[4]
				feedetail.setTransType(1);//交易类型,1正交易，2反交易[5]
				feedetail.setCardNo(info.getIdcardId());//就诊卡号
				feedetail.setRegDate(info.getDate());//挂号日期
				feedetail.setRegDpcd("");//开单科室[9]
				//feedetail.setDoctCode("");//开方医师[10]
				//feedetail.setDoctDept();//开方医师所在科室[11]
				feedetail.setItemCode(vo.getItemCode());//项目代码[12]
				feedetail.setItemName(vo.getItemName());//项目名称[13]
				feedetail.setDrugFlag("0");//1药品/0非药[14]
				feedetail.setSpecs(drugUndrug.getSpec());//规格[15]
				//feedetail.setSelfMade();//自制药标志
				//feedetail.setDrugQuality();//药品性质
				//feedetail.setDoseModelCode();//剂型[18]
				feedetail.setFeeCode(drugUndrug.getUndrugMinimumcost());//最小费用代码[19]
				//feedetail.setClassCode();//系统类别[20]
				feedetail.setUnitPrice(vo.getUnitPrice());//单价[21]
				feedetail.setQty(vo.getQty());//数量[22]
				//feedetail.setDays();//草药的付数，其他药品为1[23]
				//feedetail.setFrequencyCode();//频次代码[24]
				//feedetail.setUsageCode();//用法代码[25]
				//feedetail.setUseName();//用法名称[26]
				//feedetail.setInjectNumber();//院内注射次数[27]
				//feedetail.setEmcFlag();//加急标记:1普通/2加急[28]
				//feedetail.setLabType();//样本类型[29]
				//feedetail.setCheckBody();//检体[30]
				//feedetail.setDoseOnce();//每次用量[31]
				//feedetail.setDoseUnit();//每次用量单位[32]
				//feedetail.setBaseDose();//基本剂量[33]
				//feedetail.setPackQty();//包装数量[34]
				feedetail.setPriceUnit(vo.getPriceUnit());//计价单位[35]
				//feedetail.setPubCost();//可报效金额[36]
				//feedetail.setPayCost();//自付金额[37]
				//feedetail.setOwnCost();//现金金额[38]
				feedetail.setExecDpcd(drugUndrug.getUndrugDept());//执行科室代码[39]
				//feedetail.setExecDpnm();//执行科室名称[40]
				//feedetail.setCenterCode();//医保中心项目代码[41]
				//feedetail.setItemGrade();//项目等级，1甲类，2乙类，3丙类[42]
				//feedetail.setMainDrug();//主药标志[43]
				//feedetail.setCombNo();//组合号[44]
				feedetail.setOperCode(userId);//划价人[45]
				feedetail.setOperDate(new Date());//划价时间[46]
				feedetail.setPayFlag(0);//0划价 1收费 3预收费团体体检 4 药品预审核
				feedetail.setCancelFlag(1);//0退费，1正常，2重打，3注销[48]
				//feedetail.setFeeCpcd();//收费员代码[49]
				feedetail.setFeeDate(new Date());//收费日期[50]
				//feedetail.setInvoiceNo("");//票据号[51]
				//feedetail.setInvoCode("");//发票科目代码[52]
				//feedetail.setInvoSequence("");//发票内流水号[53]
				feedetail.setConfirmFlag(1);//0未确认/1确认[54]
				//feedetail.setConfirmCode("");//确认人[55]
				//feedetail.setConfirmDept("");//确认科室[56]
				//feedetail.setConfirmDate(new Date());//确认时间[57]
				//feedetail.setEcoCost();//优惠金额[58]
				//feedetail.setInvoiceSeq();//发票序号，一次结算产生多张发票的combNo
				//feedetail.setNewItemrate();//新项目比例
				//feedetail.setOldItemrate();//原项目比例
				feedetail.setExtFlag(0);//扩展标志 特殊项目标志 1 0 非
				feedetail.setExtFlag1(0);//0 正常/1个人体检/2 集体体检
				feedetail.setExtFlag2(0);//日结标志：0：未日结/1：已日结
				//feedetail.setExtFlag3();//1 包装 单位 0, 最小单位
				//feedetail.setPackageCode();//复合项目代码
				//feedetail.setPackageName();//复合项目名称
				//feedetail.setNobackNum();//可退数量
				//feedetail.setConfirmNum();//确认数量
				//feedetail.setConfirmInject();//已确认院注次数
				//feedetail.setMoOrder();//医嘱项目流水号或者体检项目流水号
				//feedetail.setSampleId();//条码号
				//feedetail.setRecipeSeq();//收费序列
				//feedetail.setOverCost();//超标金额
				//feedetail.setExcessCost();//药品超标金额
				//feedetail.setDrugOwncost();//自费药金额
				feedetail.setCostSource(0);//费用来源 0 操作员 1 医嘱 2 终端 3 体检
				//feedetail.setSubjobFlag();//附材标志
				feedetail.setAccountFlag(1);//0没有扣账户 1 已经扣账户
				//feedetail.setUpdateSequenceno();//更新库存的流水号(物资)
				feedetail.setAccountNo(account.getId());//账户患者消费账号
				feedetail.setUseFlag(1);//0 未出结果 1 已出结果
				feedetail.setCreateUser(userId);
				feedetail.setCreateDept(deptId);
				feedetail.setCreateTime(new Date());
				feedetailList.add(feedetail);
				
				//门诊非药品明细表处理
				item = new OutpatientItemlist();
				item.setId(null);
				item.setRecipeNo(recipedetail.getRecipeNo());//处方号
				//item.setSequenceNo();//处方内项目流水号
				item.setTransType(1);//交易类型,1正交易，2反交易
				//item.setOutpatientNo();//住院流水号
				item.setName(info.getPatientId().getPatientName());//姓名
				item.setPaykindCode(1);//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
				item.setPactCode(info.getContractunit());//合同单位
				//item.setUpdateSequenceno();//更新库存的流水号(物资)
				//item.setNurseCellCode();//护士站代码
				//item.setRecipeDeptcode("");//开立科室代码 
				item.setExecuteDeptcode(drugUndrug.getUndrugDept());//执行科室代码
				//item.setStockDeptcode("");//扣库科室代码
				//item.setRecipeDoccode();//开立医师代码
				item.setItemCode(vo.getItemCode());//项目代码
				item.setFeeCode(drugUndrug.getUndrugMinimumcost());//最小费用代码
				//item.setCenterCode();//中心代码
				item.setItemName(vo.getItemName());//项目名称
				item.setUnitPrice(vo.getUnitPrice());//单价
				item.setQty(vo.getQty().intValue());//数量
				item.setCurrentUnit(vo.getPriceUnit());//当前单位
				item.setPackageCode(vo.getStackId());//组套代码
				item.setPackageName(vo.getStackName());//组套名称
				item.setTotCost(vo.getUnitPrice()*vo.getQty());//费用金额
				//item.setOwnCost();//自费金额
				//item.setPayCost();//自付金额
				//item.setPubCost();//公费金额
				//item.setEcoCost();//优惠金额
				//item.setSendmatSequence();//出库单序列号
				//item.setSendFlag();//发放状态（0 划价 2发放（执行） 1 批费）
				//item.setBabyFlag();// 是否婴儿用 0 不是 1 是 
				//item.setJzqjFlag();//急诊抢救标志  
				//item.setExtFlag();//扩展标志(公费患者是否使用了自费的项目0否,1是)
				//item.setInvoiceNo();//结算发票号
				//item.setBalanceNo();//结算序号
				//item.setBalanceState();//结算状态
				//item.setNobackNum();//可退数量
				//item.setExtCode();//扩展代码(中山一：保存退费原记录的处方号)
				//item.setExtOpercode();//扩展操作员
				//item.setExtDate();//扩展日期
				//item.setApprno();//审批号(中山一：退费时保存退费申请单号)
				item.setChargeOpercode(userId);//划价人 
				item.setChargeDate(new Date());//划价日期
				//item.setConfirmNum();//已确认数
				//item.setMachineNo();//设备号
				//item.setExecOpercode();//执行人代码
				//item.setExecDate();//执行日期 
				//item.setSendOpercode();//发放人
				item.setFeeOpercode(userId);//计费人 
				item.setFeeDate(new Date());//计费日期
				//item.setSendDate();//发放日期
				//item.setCheckOpercode();//审核人
				//item.setCheckNo();//审核序号
				//item.setMoOrder();//医嘱流水号 
				//item.setMoExecSqn();//医嘱执行单流水号
				//item.setFeeRate();//收费比率
				//item.setFeeoperDeptcode( );//收费员科室
				//item.setUploadFlag();//上传标志
				//item.setExtFlag1();//扩展标志1
				item.setExtFlag2(0);//扩展标志2(收费方式0门诊直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) 5 终端确认收费 6终端取消
				//item.setExtFlag3();//聊城市医保新增(记录凭单号)
				item.setItemFlag(0);//0非药品 2物资
				//item.setMedicalteamCode();//医疗组
				item.setOperationno(drugUndrug.getUndrugOperationcode());//手术编码
				//item.setTransactionSequenceNumber();//医保交易流水号
				//item.setSiTransactionDatetime();//医保交易时间
				//item.setHisRecipeNo();//HIS处方号
				//item.setSiRecipeNo();//医保处方号
				//item.setHisCancelRecipeNo();//HIS原处方号
				//item.setSiCancelRecipeNo();//医保原处方号
				item.setCreateUser(userId);
				item.setCreateDept(deptId);
				item.setCreateTime(new Date());
				itemlistList.add(item);
			}
			feedetailDAO.saveOrUpdateList(feedetailList);//保存门诊处方明细
			itemlistDAO.saveOrUpdateList(itemlistList);//保存门诊非药品明细
			//扣费
			Double clinicBalance = account.getClinicBalance();
			account.setClinicBalance(clinicBalance-sumMoney);
			accountInInterDAO.save(account);
	}
	/**  
	 * @Description： 开立科室下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> quertComboboxDept() {
		List<SysDepartment> lst = itemlistDAO.quertComboboxDept();
		return lst;
	}
	/**  
	 * @Description： 开立医生下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> quertComboboxEmp(String dept) {
		List<SysEmployee> lst = itemlistDAO.quertComboboxEmp(dept);
		return lst;
	}
	/**  
	 * @Description： 合同单位下拉
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessContractunit> quertComboboxCont() {
		List<BusinessContractunit> lst = itemlistDAO.quertComboboxCont();
		return lst;
	}

	@Override
	public RegisterInfo queryRegisterByCaseNo(String id) {
		return registerInfoDAO.queryRegisterInfoByCaseNo(id);
	}
	
}
