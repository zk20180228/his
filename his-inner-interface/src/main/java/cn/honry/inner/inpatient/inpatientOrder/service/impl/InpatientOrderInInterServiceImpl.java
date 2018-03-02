package cn.honry.inner.inpatient.inpatientOrder.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DeptDateModc;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugSplit;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.inpatient.execUndrug.dao.UnDrugOrderExecInInterDao;
import cn.honry.inner.inpatient.execdrug.dao.DrugExecOrderInInterDao;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;
import cn.honry.inner.inpatient.inpatientOrder.dao.InpatientOrderInInterDao;
import cn.honry.inner.inpatient.inpatientOrder.service.InpatientOrderInInterService;
import cn.honry.inner.inpatient.inpatientOrder.vo.InpatientOrderInInterVO;
import cn.honry.inner.inpatient.inpatientOrder.vo.MsgInInterVO;
import cn.honry.inner.inpatient.kind.service.InpatientKindInInterService;
import cn.honry.inner.nursestation.nurseDateModc.dao.NurseDateModcInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;

/**
 * @author Administrator
 *
 */
/**
 * @author zl
 * @createDate： 2016年4月22日 下午2:31:14
 * @modifier liujl yueyf
 * @modifyDate：2016年4月22日 下午2:31:14
 * @param：
 * @modifyRmk：
 * @version 1.0
 */
@Service("inpatientOrderInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientOrderInInterServiceImpl implements InpatientOrderInInterService {
	
	private ParameterInnerDAO parameterInnerDAO;
	
	private InpatientOrderInInterDao inpatientOrderInInterDao;
	
	private DrugInfoInInerDAO drugInfoInInerDAO;
	
	private InpatientInfoInInterService inpatientInfoInInterService;
	
	private MatOutPutInInterService matOutPutInInterService;
	
	private DeptInInterService deptInInterService;
	
	private NurseDateModcInInterDAO nurseDateModcDAO;
	
	private UnDrugOrderExecInInterDao unDrugDao;
	
	private DrugExecOrderInInterDao drugDao;
	
	private UndrugInInterDAO undrugInInterDAO;
	
	private InpatientKindInInterService kindServiceInfo;
	@Override
	public void StopHourFrequenceOrder(InpatientOrder order,Date stopTime) throws Exception {
		Map<String,MsgInInterVO> bsMsgMap=new HashMap<String,MsgInInterVO>();//业务处理提示信息
		InpatientOrderInInterVO orderVO=new InpatientOrderInInterVO();
		MsgInInterVO msgVO=null;
		/*
		 * 判断患者情况，将患者信息设置到自定义医嘱对象中
		 */
		InpatientInfo inpatientInfo =null; //审核医嘱对应的患者住院登记信息（含患者信息、床位、病区、住院科室、费用）
		if(StringUtils.isNotBlank(inpatientInfo.getId())){//患者存在
			//FIXME:复制医嘱属性到自定义VO中
			BeanUtils.copyProperties(order, orderVO);
			orderVO.setPatient(inpatientInfo);//将患者信息设置到医嘱中
			if ("O".equals(inpatientInfo.getInState())//O：出院结算；N：无费退院；C：封账结存
					|| "N".equals(inpatientInfo.getInState())
					|| "C".equals(inpatientInfo.getInState())
					|| "P".equals(inpatientInfo.getInState())) { // 患者是出院结算或无费退院，封账结存或者预约出院状态，其医嘱不可分解
				msgVO.setMsgStatus(1);
				msgVO.setErrorMsg("分解医嘱流水号为"+orderVO.getMoOrder()+"时，患者："+inpatientInfo.getPatientName()+ "非在院状态，其医嘱不可以分解");
				bsMsgMap.put(orderVO.getMoOrder(), msgVO);
			}
		}
		//按小时计费医嘱
		String hourFeePara= parameterInnerDAO.getParameterByCode("axsjfpccode");	//获取系统参数，判断全院病区是否可以收费。
		if(StringUtils.isNotBlank(hourFeePara)){
			List<InpatientOrder> listorder=inpatientOrderInInterDao.getSubOrderByOrder(order); //获取附材医嘱	
			listorder.add(order);//将主医嘱放入数组，处理方式相同
			for(InpatientOrder o:listorder){
				orderVO=new InpatientOrderInInterVO();
				BeanUtils.copyProperties(o, orderVO);
				decomposeLongOrder(orderVO,0,stopTime,bsMsgMap);//按停止时间分解医嘱
				//TODO:查找医嘱下次分解时间到医嘱停止时间内应该收费但是未收费的药品、非药品医嘱
				List<InpatientExecdrug> execDrugList=drugDao.getListNoFeeExec(order,stopTime);//药品执行记录
				List<InpatientExecundrug> execUndrugList=unDrugDao.getListNoFeeExec(order,stopTime);//非药品执行记录
				
				String isPre=parameterInnerDAO.getParameterByCode("szfjissf");//获取系统参数中的住院摆药是否需要进行预扣库存【少医院参数】

				//发送药品，非药品执行记录
				Map<String,Object> decomposeMap=this.sendOrderByExec(orderVO.getPatient(),execDrugList,execUndrugList,bsMsgMap,isPre);
			}
		}
	}
	
	/**
	 * 分解长期医嘱
	 * @throws Exception 
	 */
	public void decomposeLongOrder(InpatientOrderInInterVO orderVO, int days, Date operTime, Map<String,MsgInInterVO> msgMap) throws Exception {
		/**
		 * 2.2、获取当前护士站设置的分解时间（时分），药品及其附材医嘱按照护士站的分解时间设置本次分解时间。如果没设置默认按12:01；非药品且不是附材的情况，设置医嘱的本次分解时间为12:01。非药品对应的附材按照非药品设置
		 * 2.3、对于下次分解时间小于设置的分解天数对应的时间的医嘱进行分解
		 * 2.4、对于需要收费的药品医嘱，需要根据药品拆分属性计算收费的数量
		 * 2.5、根据医嘱频次执行的时间点设置医嘱的服药时间，医嘱的服药时间大于医嘱的停止时间，大于2.2设置分解时间时，停止医嘱（设置医嘱为作废状态），否则生成执行记录（每个频次的时间点对应一个执行记录）
		 * 2.6、药品的执行记录中的用药数量需要根据药品拆分属性设置药品的每次用量。
		 * 2.7、医嘱默认为未发送状态。drugedflg=0
		 * 2.8、对于需要发药的医嘱类型，设置医嘱发药标记为临时发送
		 * 3、分解结束后，需要更新相应医嘱的本次分解时间和下次分解时间：医嘱的本次分解时间为当前的时间12:01;医嘱的下次分解时间根据设置的分解天数和4中获取的分解时间设置
		 */
		User user=(User) SessionUtils.getCurrentUserFromShiroSession();//当前操作人
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginNursingStationShiroSession();//当前登录病区
		List<InpatientOrderInInterVO> execList = new ArrayList<InpatientOrderInInterVO>();//保存药品、非药品执行档
		
		List<InpatientExecdrug> execDrugList=new ArrayList<InpatientExecdrug>();//药品执行记录
		List<InpatientExecundrug> execUndrugList=new ArrayList<InpatientExecundrug>();//非药品执行记录
		
        int hour=12;//默认每天中午12点
		int minute=1;
        //获取登录病区医嘱分解时间
		SysDepartment userDept = (SysDepartment) SessionUtils.getCurrentUserDepartmentFromShiroSession();
		DeptDateModc deptDateModc=nurseDateModcDAO.findNurseDateModcByDeptId(userDept.getId());//获取当前登录病区的医嘱分解时间
		if(null!=deptDateModc){
			if(null!=deptDateModc.getS_time()){// 设置的医嘱分解时间
				hour = deptDateModc.getS_time().getHours();
				minute=deptDateModc.getS_time().getMinutes()+1;
			}
		}
        //获取医嘱分解结束时间
        Date decoEndTime=null; //医嘱分解结束时间
        
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(operTime);
		int date=calendar.get(Calendar.DATE);//本月的第几天，同Calendar.DAY_OF_MONTH
        if(("1".equals(orderVO.getItemType())&&orderVO.getMainDrug()==1)||"1".equals(orderVO.getMainType())){//药品及其附材医嘱分解结束时间设置相同,取当前病区设置的分解时间，如果未设置，设置为12:01
        	operTime=DateUtils.parseDate(DateUtils.formatDateTime(calendar.getTime(), "yyyy-MM-dd")+" "+hour+":"+minute, "yyyy-MM-dd HH:mm");
        }else if(("2".equals(orderVO.getItemType())&&orderVO.getMainDrug()==1)||"2".equals(orderVO.getMainType())){//非药品及其附材医嘱分解结束时间设置相同，默认为12:01
    		operTime=DateUtils.parseDate(DateUtils.formatDateTime(calendar.getTime(), "yyyy-MM-dd")+" 12:01", "yyyy-MM-dd HH:mm");
        }
        calendar.set(Calendar.DATE, date + days);
    	decoEndTime=DateUtils.parseDate(DateUtils.formatDateTime(calendar.getTime(), "yyyy-MM-dd")+" "+hour+":"+minute, "yyyy-MM-dd HH:mm");
        /**
         * 医嘱分解过程 start
         */
    	//DECMPS_STATE（医嘱类型）：1-长期，0-临时
    	//MO_STAT（医嘱状态）0开立，1审核，2执行，3作废，4重整
		if(orderVO.getDecmpsState()==1
				&&(orderVO.getMoStat()==1||orderVO.getMoStat()==2)
				&&(DateUtils.parseDateY_M_D_H_M(DateUtils.formatDateTime(orderVO.getDateNxtmodc()==null?decoEndTime:orderVO.getDateNxtmodc(),"yyyy-MM-dd HH:mm:ss")).before(decoEndTime)
				||orderVO.getDateNxtmodc()==null)){//当前的分解时间在医嘱开始时间之后，长期审核或执行状态，且下次分解时间在分解结束时间以前的
			//设置医嘱执行记录为未执行、未收费状态
			orderVO.setIfFee(0);
			orderVO.setChargeDate(null);
			orderVO.setChargeDeptcd(null);
			orderVO.setChargeUsercd(null);
			
			orderVO.setExecuteFlag(0);
			orderVO.setExecuteDate(null);
			orderVO.setExecuteUsercd(null);
				
			if("1".equals(orderVO.getItemType())){//药品
				if(StringUtils.isBlank(orderVO.getExecDpcd())){
					orderVO.setExecDpnm(orderVO.getPatient().getDeptCode());//执行科室设置为患者住院科室
				}
				DrugInfo drugInfo=null;
				if(!"userdefined".equals(orderVO.getItemCode())){
					drugInfo=drugInfoInInerDAO.get(orderVO.getItemCode());
					orderVO.setHomeMadeFlag(drugInfo.getDrugIsmanufacture());//自制标识     1国产2进口3自制4合资  
					orderVO.setFeeCode(drugInfo.getDrugMinimumcost());
				}

				if(!orderVO.getPriceUnit().equals(orderVO.getMinUnit())){
					orderVO.setTempQtyTot(orderVO.getPackQty()*orderVO.getQtyTot());
				}
				/**
				 * 1、判断扣库科室下此药品是否有效；
				 * 2、判断扣库科室下此药品库存是否充足（库存量-预扣量）； 
				 */
				String isPre=parameterInnerDAO.getParameterByCode("szfjissf");//获取系统参数中的住院摆药是否需要进行预扣库存【少医院参数】
				
				/**以下set属性为执行记录的用药量**/
				Double useQty=0.00;	
				if(orderVO.getChargeState()==1){//需要计费的医嘱
					if(StringUtils.isNotBlank(orderVO.getExecDose())){//判断是否设置了特殊频次
						//根据特殊频次设置药品执行记录的用药数量
						useQty=this.splitAttribute(orderVO,Double.valueOf(orderVO.getExecDose()),orderVO.getBaseDose());
					}else{
						useQty=this.splitAttribute(orderVO,Double.valueOf(orderVO.getDoseOnce()),orderVO.getBaseDose());
					}
					orderVO.setTempQtyTot(useQty);
				}				
				/**以上set属性为执行记录的用药量**/
				
				DecoDrugByFrequence(orderVO,operTime,decoEndTime,days,drugInfo,msgMap, isPre, 0,execDrugList);
			}else if("2".equals(orderVO.getItemType())){//非药品
				DrugUndrug undrug=null;
				if(!"userdefined".equals(orderVO.getItemCode())){
					undrug=undrugInInterDAO.get(orderVO.getItemCode()); // 查询非药品表
				}

				DecoUndrugByFrequence(orderVO,operTime,decoEndTime,days,undrug,msgMap, 0,execUndrugList);
			}
		}
		if(execDrugList.size()>0){//药品
			drugDao.saveOrUpdateList(execDrugList);
		}
		if(execUndrugList.size()>0){
			unDrugDao.saveOrUpdateList(execUndrugList);//非药品
		}
		if(execDrugList.size()>0 || execUndrugList.size()>0){
			//TODO:配置医嘱的本次分解时间和下次分解时间
			Calendar cal=Calendar.getInstance(); 
			cal.setTime(operTime);
			cal.set(Calendar.DATE, calendar.get(Calendar.DATE) + days-1);
			orderVO.setDateCurmodc(operTime);
			orderVO.setDateNxtmodc(cal.getTime());//根据分解医嘱频次以及循环次数，计算下次分解时间
			inpatientOrderInInterDao.updateExecTime(orderVO);
		}
		if(execDrugList.size()==0 && execUndrugList.size()==0){
			MsgInInterVO msgVO = new MsgInInterVO();
			msgVO.setErrorMsg("3");
			msgVO.setConfirmMsg("暂无符合分解条件的医嘱信息！");
			msgMap.put("msg", msgVO);
		}
	}
	
	/**
	 * 根据药品、非药品执行记录发送医嘱
	 * @param inpatientInfo 患者信息
	 * @param execDrugList 未收费的药品执行记录数组
	 * @param execUndrugList 未收费的非药品执行记录数组
	 * @param bsMsgMap 异常信息
	 * @param isPre 住院摆药是否预扣库存
	 * @return
	 */
	private Map<String, Object> sendOrderByExec(
			InpatientInfo inpatientInfo, List<InpatientExecdrug> execDrugList,
			List<InpatientExecundrug> execUndrugList,
			Map<String, MsgInInterVO> msgMap, String isPre) {
		Date operTime = new Date();//统一设置操作时间
		User user=(User) SessionUtils.getCurrentUserFromShiroSession();//获取登录人
		
		List<FeeInInterVo> feeList = new ArrayList<FeeInInterVo>(); // 收费医嘱
		SysDepartment dept=(SysDepartment) SessionUtils.getCurrentUserLoginNursingStationShiroSession();//获取登录病区
		
		String hszCharge= parameterInnerDAO.getParameterByCode("hszsfkysf");	//获取系统参数，判断全院病区是否可以收费。
		InpatientExecdrug drug=null;
		InpatientExecundrug unDrug=null;
		
		Map<String, DrugApplyout> applyoutMap=new HashMap<String,DrugApplyout>();//药品出库申请Map
		InpatientOrder order=null;
		List<InpatientOrder> orderList=new ArrayList<InpatientOrder>();//需要修改执行状态的主医嘱记录
		for(InpatientExecdrug execDrug:execDrugList){//药品执行档
			//execDrug.setPatient(inpatientInfo);
			DrugApplyout applyout=null;//药品出库申请
			order=inpatientOrderInInterDao.getOrderByMoOrder(execDrug.getMoOrder());
			
			DrugInfo drugInfo=null;//药品信息
			if(!"userdefined".equals(execDrug.getDrugCode()) && execDrug.getChargeState() == 1){
				drugInfo=drugInfoInInerDAO.get(execDrug.getDrugCode());
				execDrug.setHomeMadeFlag(drugInfo.getDrugIsmanufacture());//自制标识：1国产2进口3自制4合资
				execDrug.setFeeCode(drugInfo.getDrugMinimumcost());
			}
			//设置执行信息
			execDrug.setExecDate(operTime);//设置执行时间
			execDrug.setExecUsercd(user.getId());//设置执行人
			execDrug.setExecDpcd(dept.getId());//设置执行科室
			
			if(execDrug.getChargeState()==1){//判断医嘱是否需要计费  1-收费，0-划价
				if("1".equals(hszCharge)){//护士站可以收费时，生成费用信息，设置记账相关信息
					String isCharge=parameterInnerDAO.getParameterByCode("isCharge");//获取系统参数住院摆药时计费 0-否；1-是
					if("1".equals(isCharge)){
						execDrug.setChargeFlag(0);//药房摆药时计费
						execDrug.setExecFlag(0);//执行状态
					}else{
						execDrug.setChargeFlag(1);
						order.setMoStat(2);
						execDrug.setExecFlag(1);//执行状态
					}
					execDrug.setChargeDate(operTime);
					execDrug.setChargeDeptcd(dept.getId());
					execDrug.setChargeUsercd(user.getId());
				}else{
					execDrug.setChargeFlag(0);//未收费
					execDrug.setExecFlag(0);//未执行状态
				}
				setFeeListByExec(feeList,execDrug,unDrug,1);//根据费用接口，将药品执行记录，非药品执行记录生成费用对象
				if(execDrug.getNeedDrug()==1){//药房需要发药
					//设置医嘱执行档的发药标记为临时发送
					execDrug.setDrugedFlag(2);
				}else{
					//设置医嘱执行档的发药标记为已配药
					execDrug.setDrugedFlag(3);
				}

				//XXX：生成出库申请，此时返回的出库申请信息中没有处方号及处方内流水号信息	
				if(execDrug.getNeedDrug()==1){//需要发药的医嘱生成药品出库申请 1-需要药房配药 
					applyout=setDrugApplyOutByExec(execDrug,drugInfo,isPre,4,msgMap,hszCharge);//根据医嘱及药品获取药品出库申请信息	
				}
				applyoutMap.put(execDrug.getExecId(), applyout);
			}
		}

		/**
		 * 非药品处理 start
		 */
		for(InpatientExecundrug execunDrug:execUndrugList){//非药品执行档	
			execunDrug.setPatient(inpatientInfo);
			DrugUndrug undrug=null;
			order=inpatientOrderInInterDao.getOrderByMoOrder(execunDrug.getMoOrder());
			if(!"userdefined".equals(execunDrug.getUndrugCode())){
				undrug=undrugInInterDAO.get(execunDrug.getUndrugCode()); // 查询非药品表
			}else{
				//undrug信息来源于医嘱----------
				undrug.setUndrugIssubmit(0);
			}
			
			//非药品不需要终端确认，或者医嘱的执行科室为开立科室，或者医嘱的执行科室为当前登录病区时，设置为收费执行状态
			SysDepartment loginDept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			if(undrug.getUndrugIssubmit()!=1
					||execunDrug.getExecDpnm().equals(execunDrug.getListDpcd())
					||execunDrug.getExecDpcd().equals(loginDept.getId())){

				if(execunDrug.getChargeState()==0){
					//嘱托医嘱（非附材)不收费
				}else if(undrug.getDefaultprice()<0){
					//费用小于0的非药品不收费
				}else{
					//判断是否对应物资，没对应物资，直接生成费用信息；对应物资的情况下，判断物资库存是否充足，库存充足的情况下收费，否则抛出异常
					setFeeListByExec(feeList,drug,execunDrug,1);//根据费用接口，将药品执行记录，非药品执行记录生成费用对象
					execunDrug.setChargeFlag(1);
					execunDrug.setChargeDate(operTime);
					execunDrug.setChargeUsercd(user.getId());
					execunDrug.setChargeDeptcd(dept.getId());
					execunDrug.setExecFlag(1);//执行状态
					//判断是否对应物资，没对应物资，直接生成费用信息；对应物资的情况下，判断物资库存是否充足，库存充足的情况下收费，否则抛出异常
					generateMatByExc(execunDrug,msgMap);
					order.setMoStat(2);
				}
			}else{
				execunDrug.setExecFlag(0);//未执行状态
			}
			//设置执行信息
			execunDrug.setExecDate(operTime);//设置执行时间
			execunDrug.setExecUsercd(user.getId());
			execunDrug.setExecDpcd(dept.getId());
			execunDrug.setExecDpnm(dept.getDeptName());
		}
		
		/**
		 * 生成费用(药品、非药品)
		 */
		Map<String, Object> feeMap = null;
		if(feeList.size()>0){
			feeMap = inpatientInfoInInterService.saveInpatientFeeInfo(feeList);//保存费用
			//费用接口异常信息捕捉，显示到页面
		}
		
		/**
		 * 药品相关费用的处方号，处方内流水号等设置到药品执行档和药品出库申请中，同时更新相应医嘱的执行及收费信息
		 */
		DrugApplyout applyout=null;
		for(InpatientExecdrug execDrug:execDrugList){//药品执行档
			applyout=applyoutMap.get(execDrug.getExecId());
			String[] s=(String[]) feeMap.get(execDrug.getExecId().split(",",-1));
			execDrug.setRecipeNo(s[0]);
			execDrug.setSequenceNo(Integer.valueOf(s[1]));
			
			applyout.setRecipeNo(execDrug.getRecipeNo());
			applyout.setSequenceNo(applyout.getSequenceNo());
			
			drugInfoInInerDAO.save(execDrug);//保存药品执行医嘱,更新发药标识，更新执行信息，更新收费信息		
			
		}

		/**
		 * 非药品相关费用的处方号，处方内流水号等设置到非药品执行档和物资出库申请中，同时更新相应医嘱的执行及收费信息
		 */
		for(InpatientExecundrug execUndrug:execUndrugList){//药品执行档
			String[] s=(String[]) feeMap.get(execUndrug.getExecId().split(",",-1));
			execUndrug.setRecipeNo(s[0]);
			execUndrug.setSequenceNo(Integer.valueOf(s[1]));
			unDrugDao.save(execUndrug);//保存非药品执行医嘱
			
			if(execUndrug.getIsMat()==1){
				//物资出库操作
				OutputInInterVO outVo=new OutputInInterVO();
				outVo.setStorageCode(execUndrug.getDeptCode());//执行科室(仓库)编码
				outVo.setInpatientNo(execUndrug.getInpatientNo());//住院流水号(或门诊号)
				outVo.setUndrugItemCode(execUndrug.getUndrugCode());//非药品Id
				outVo.setApplyNum(execUndrug.getQtyTot().doubleValue());//申请数量
				outVo.setUseNum(execUndrug.getQtyTot().doubleValue());//申请数量
				outVo.setTransType(1);//交易类型
				outVo.setFlag("C");//住院
				outVo.setRecipeNo(execUndrug.getRecipeNo());//处方号
				outVo.setSequenceNo(execUndrug.getSequenceNo());//处方内流水号
				matOutPutInInterService.addRecord(outVo);
			}
		}
		//主医嘱收费执行状态设置
		inpatientOrderInInterDao.saveOrUpdateList(orderList);
		return null;
	}
	
	/**
	 * 药品拆分属性设置，获取药品执行数量
	 * 
	 * @author liujl
	 * @createDate： 2016年4月26日 下午17:19:47
	 * @modifier liujl
	 * @modifyDate：2016年4月26日 下午17:19:47
	 * @param： order:医嘱对象
	 * @modifyRmk：
	 * @version 1.0
	 * @param baseDose 
	 * @param doseOnce 
	 */

	private double splitAttribute(InpatientOrderInInterVO orderVO, double doseOnce, double baseDose) {
		double countQty=0;
		DrugSplit split = inpatientOrderInInterDao.getSplitByDeptIdAndDrugId(
				orderVO.getExecDpcd(), orderVO.getItemCode());// 根据医嘱执行科室和医嘱药品/非药品id查询药品拆分表

		if (split == null) { // 不可拆分 5.4=6
			countQty = doseOnce/baseDose;
		} else {
			String property=split.getPropertyCode();
			if (property != null) {
				if ("0".equals(property)) {
					countQty = doseOnce/baseDose;
				} // 0-不可拆分 5.4=6
				else if ("1".equals(property)) {
					countQty = doseOnce/baseDose;
				} // 1-可拆分,配药时不取整 5.4=5.4
				else if ("2".equals(property)) {
					countQty = Math.floor(doseOnce/baseDose);
				} // 2-可拆分配药时上取整 5.4=5
				else if ("3".equals(property)) {
					countQty = Math.ceil(doseOnce/baseDose);
				} // 3不可拆分，当日取整 5.4=6
			}else{
				countQty = doseOnce/baseDose;
			}
		
		}
		return countQty;
	}
	
	/**
	 * 将药品执行记录、非药品执行记录转化为费用信息
	 * @param feeList 费用数组
	 * @param execDrug 药品医嘱执行对象
	 * @param execUnDrug 非药品医嘱执行对象
	 * @param transType 交易类型
	 */
	private void setFeeListByExec(List<FeeInInterVo> feeList, InpatientExecdrug execDrug,
			InpatientExecundrug execUnDrug, int transType) {
		FeeInInterVo fee=new FeeInInterVo();
		if(null!=execDrug){
			fee.setPactCode(execDrug.getPatient().getPactCode());//合同单位
			fee.setPaykindCode(execDrug.getPatient().getPaykindCode());//结算类别
			fee.setBalanceNo(execDrug.getPatient().getBalanceNo());//结算序号
			fee.setItemCode(execDrug.getDrugCode());//药品非药品项目编码
			fee.setTy("1");//药品非药品标识1药品2非药品
			fee.setTransType(transType);//交易类型,1正交易，2反交易
			fee.setInpatientNo(execDrug.getInpatientNo());//住院流水号
			fee.setNurseCellCode(execDrug.getNurseCellCode());//护士站代码
			fee.setRecipeDeptCode(execDrug.getListDpcd());//开立科室代码
			fee.setExecuteDeptCode(execDrug.getExecDeptcd());//执行科室代码
			fee.setMedicineDeptcode(execDrug.getDeptCode());//取药科室代码  设置为患者的住院科室
			fee.setRecipeDocCode(execDrug.getDocCode());//开立医师代码    
			fee.setFeeCode(execDrug.getFeeCode());// 最小费用代码 
			fee.setMoExecSqn(execDrug.getExecId());
			fee.setFeeCode(execDrug.getFeeCode()); // 最小费用单位代码
			fee.setCenterCode(null);//医疗中心项目代码 ,通过患者的合同单位及结算方式读取医保接口，获取
			fee.setQty(execDrug.getQtyTot());//按最小单位计算后的申请数量
			fee.setNobackNum(execDrug.getQtyTot());//可退数量
			fee.setHomeMadeFlag(execDrug.getHomeMadeFlag());//自制标识     1国产2进口3自制4合资    
			fee.setCurrentUnit(execDrug.getPriceUnit()); // 计价单位
			fee.setDays(execDrug.getUseDays());//草药医嘱使用
			fee.setSenddrugFlag(execDrug.getChargeFlag());//根据药品医嘱的收费状态设置发药状态
			fee.setBabyFlag(execDrug.getBabyFlag());//是否婴儿用药 0 不是 1 是  
			fee.setJzqjFlag(execDrug.getEmcFlag());// 急诊抢救标志      
			if("05".equals(execDrug.getTypeCode()) || "07".equals(execDrug.getTypeCode())){//医嘱类型为出院带药或者请假带药
				fee.setBroughtFlag(1);//出院带药标记 0 否 1 是(Change as OrderType)
			}else{
				fee.setBroughtFlag(0);
			}
			fee.setExecDate(execDrug.getExecDate());// 执行日期 
			fee.setExecOpercode(execDrug.getExecUsercd());//执行人代码
	
			// 执行科室
			if (execDrug.getExecDpcd() != null) { // 如果医嘱的执行科室为空 设置为改医嘱所属病人的住院科室
				fee.setExecuteDeptCode(execDrug.getExecDpcd());
			} else {
				fee.setExecuteDeptCode(execDrug.getDeptCode());
			}
			// 药品收费开始
			fee.setOrderId(execDrug.getId());
			fee.setMoOrder(execDrug.getMoOrder());//医嘱流水号 
			fee.setMoExecSqn(execDrug.getExecId());//医嘱执行单流水号
			fee.setUploadFlag(0);// 上传标志       0-未上传；1-上传
			
			fee.setSenddrugSequence("0");
			fee.setGoon(execDrug.getChargeFlag());
			feeList.add(fee);
		}else if(null!=execUnDrug){
			fee.setPactCode(execUnDrug.getPatient().getPactCode());//合同单位
			fee.setPaykindCode(execUnDrug.getPatient().getPaykindCode());//结算类别
			fee.setBalanceNo(execUnDrug.getPatient().getBalanceNo());//结算序号
			fee.setItemCode(execUnDrug.getUndrugCode());//药品非药品项目编码
			fee.setTy("2");//药品非药品标识1药品2非药品
			fee.setTransType(transType);//交易类型,1正交易，2反交易
			fee.setInpatientNo(execUnDrug.getInpatientNo());//住院流水号
			fee.setNurseCellCode(execUnDrug.getNurseCellCode());//护士站代码
			fee.setRecipeDeptCode(execUnDrug.getListDpcd());//开立科室代码
			fee.setExecuteDeptCode(execUnDrug.getExecDeptcd());//执行科室代码
			fee.setMedicineDeptcode(execUnDrug.getDeptCode());//取药科室代码  设置为患者的住院科室
			fee.setRecipeDocCode(execUnDrug.getDocCode());//开立医师代码    
			fee.setFeeCode(execUnDrug.getFeeCode());// 最小费用代码 
			fee.setMoExecSqn(execUnDrug.getExecId());
			fee.setFeeCode(execUnDrug.getFeeCode()); // 最小费用单位代码
			fee.setCenterCode(null);//医疗中心项目代码 ,通过患者的合同单位及结算方式读取医保接口，获取
			fee.setQty(Double.valueOf(execUnDrug.getQtyTot()));//按最小单位计算后的申请数量
			fee.setNobackNum(Double.valueOf(execUnDrug.getQtyTot()));//可退数量
			fee.setHomeMadeFlag(0);//自制标识     1国产2进口3自制4合资    
			fee.setCurrentUnit(execUnDrug.getStockUnit()); //项目单位
			fee.setSenddrugFlag(0);//根据药品医嘱的收费状态设置发药状态
			fee.setBabyFlag(execUnDrug.getBabyFlag());//是否婴儿用药 0 不是 1 是  
			fee.setJzqjFlag(execUnDrug.getEmcFlag());// 急诊抢救标志      
			if("05".equals(execUnDrug.getTypeCode()) || "07".equals(execUnDrug.getTypeCode())){//医嘱类型为出院带药或者请假带药
				fee.setBroughtFlag(1);//出院带药标记 0 否 1 是(Change as OrderType)
			}else{
				fee.setBroughtFlag(0);
			}
			fee.setExecDate(execUnDrug.getExecDate());// 执行日期 
			fee.setExecOpercode(execUnDrug.getExecUsercd());//执行人代码
	
			// 执行科室
			if (execUnDrug.getExecDpcd() != null) { // 如果医嘱的执行科室为空 设置为改医嘱所属病人的住院科室
				fee.setExecuteDeptCode(execUnDrug.getExecDpcd());
			} else {
				fee.setExecuteDeptCode(execUnDrug.getDeptCode());
			}
			fee.setMoOrder(execUnDrug.getMoOrder());//医嘱流水号 
			fee.setMoExecSqn(execUnDrug.getExecId());//医嘱执行单流水号
			fee.setUploadFlag(0);// 上传标志       0-未上传；1-上传

			fee.setSenddrugSequence("0");
			fee.setGoon(execUnDrug.getChargeFlag());
			feeList.add(fee);
		}
	}
	
	/**
	 * 按频次分解生成药品执行记录
	 * @param orderVO
	 * @param useQty
	 * @param execList
	 * @param operTime 当前分解时间
	 * @param decoEndTime 本次分解停止时间
	 * @param days 分解天数
	 * @throws Exception 
	 * @throws Exception 
	 */
	private void DecoDrugByFrequence(InpatientOrderInInterVO orderVO,Date operTime,Date decoEndTime,int days,DrugInfo drugInfo,Map<String,MsgInInterVO> msgMap,String isPre,int i,List<InpatientExecdrug> execDrugList) throws Exception {
		// TODO：根据医嘱执行频次生成执行记录
		/**
		 * 以下处理医嘱的频次，根据频次生成医嘱执行记录
		 * XXX:按频次生成执行记录
		 */
		days+=1;//默认加上1天，代表今天分解时间开始到领导以及隔天分解时间点之前24小时（两天）的。
		Date useTime=null;//服药时间
		InpatientExecdrug execDrug=null;//药品执行记录
		Date nextDecoTime=orderVO.getDateNxtmodc();//医嘱下次分解时间
		BusinessFrequency frequency = inpatientOrderInInterDao.getListByName(orderVO.getFrequencyCode()); // 查询频次表
		String period = null; // 频次对应的时间
		Integer frequencyTime=0;//频次次数
		Integer frequencyNum=0;//频次数目
		String frequencyUnit=null;//频次单位
		Integer frequencyAlwaysFlag=0;//频次持续标记 0-不持续 1-持续
		//目前医嘱开立方式，特殊频次和来源于医嘱频次表中的时间点设置不兼容，5个以上的特殊频次无法设置。
		if (StringUtils.isNotBlank(orderVO.getExecTimes())) { // 如果特殊频次不为空
			//TODO:医嘱资料档中的频次数据
			period = orderVO.getExecTimes();//默认是每天几次，如08:30,09:30,10:30...
			frequencyTime=period.split(",").length;
			frequencyUnit="D";//默认单位 天
			frequencyNum=1;//默认为1 即每天
		} else { // 否则医嘱频次
			//TODO:频次表中的频次数据
			period = frequency.getPeriod();
			frequencyTime=frequency.getFrequencyTime();
			frequencyNum=frequency.getFrequencyNum();
			frequencyUnit=frequency.getFrequencyUnit();
			frequencyUnit=StringUtils.isNotBlank(frequencyUnit)?frequencyUnit.toUpperCase():frequencyUnit;
			frequencyAlwaysFlag=frequency.getAlwaysFlag();
		}
		
		if(frequencyUnit!=null){
			Calendar c=Calendar.getInstance();
			c.setTime(operTime);
			if("ONCE".equals(frequencyUnit)){//仅一次
				for(int x=0;x<days;x++){
					c.setTime(operTime);
					c.set(Calendar.DATE, operTime.getDate()+x);
					c.set(Calendar.MINUTE, operTime.getMinutes()+1);
					useTime=c.getTime();
					createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
				}
			}else if("T".equals(frequencyUnit)){//需求（必要时）
				for(int y=0;y<days;y++){
					c.setTime(operTime);
					c.set(Calendar.DATE, operTime.getDate()+y);
					c.set(Calendar.MINUTE, operTime.getMinutes()+1);
					useTime=c.getTime();
					createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
				}
			}else if("W".equals(frequencyUnit)){//周
//				long hSpan=timeBetweenDates(operTime,decoEndTime,"D");//操作时间与分解结束时间（操作时间+分解天数）相隔天数
				//08:00|1,08:00|4,18:00|1,18:00|4  -2次/周   2/周（周一、周四）
				//08:00|1,08:00|5                  -2次/周   2/周（周五、周一）
				//08:00|1							1次/2周（周一）
//				c.setTime(operTime);
				Map<String, ArrayList<String>> wMap=new HashMap<String,ArrayList<String>>();
				if(StringUtils.isNotBlank(period)){
					String[] periodArr=period.split(",");
					for(int p=0;p<periodArr.length;p++){
						String[] weeks=periodArr[p].split("\\|");
						if(wMap.containsKey(weeks[1])){
							wMap.get(weeks[1]).add(weeks[0]);
						}else{
							ArrayList<String> wTimes=new ArrayList<String>();
							wTimes.add(weeks[0]);
							wMap.put(weeks[1], wTimes);
						}
					}
				}
				for(int w=0;w<days;w=w+(frequencyNum==1?frequencyNum:(frequencyNum*7))){
					Date tempDate=new Date(operTime.getTime());//+w*86400000-->24*60*60*1000=86400000
					tempDate=DateUtils.addDay(tempDate, w);//加上指定的天数
					c.setTime(tempDate);
//					c.set(Calendar.DATE, c.get(Calendar.DATE)+w);
					int week=c.get(Calendar.DAY_OF_WEEK)-1;
					if(wMap.containsKey(String.valueOf(week))){
						for(String time:wMap.get(String.valueOf(week))){//时间点
							c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
							c.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
							c.set(Calendar.SECOND, 0);
							useTime=c.getTime();
							if(w==0){
								//分解时间点
								long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
								//用药时间点
								long currentHHmm=Integer.valueOf(time.split(":")[0])*60+Integer.valueOf(time.split(":")[1]);
								if(currentHHmm>firstHHmm){
									createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
								}
							}else if(w==days-1){
								//分解时间点
								long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
								//用药时间点
								long currentHHmm=Integer.valueOf(time.split(":")[0])*60+Integer.valueOf(time.split(":")[1]);
								if(currentHHmm<firstHHmm){
									createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
								}
							}else{
								createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
							}
//							createExcecDrugByTime(useTime, operTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
						}
					}
//					createExcecDrugByTime(useTime, operTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
				}
			}else if("D".equals(frequencyUnit)){//天  
//				c.setTime(operTime);
				//举例：4次/日=4/日（8-11-16-20）=08:00,12:00,18:00,22:00
				for(int d=0;d<days;d=d+frequencyNum){
					c.setTime(operTime);
					String[] dTimes=period.split(",");
					c.set(Calendar.DATE, c.get(Calendar.DATE)+d);
					for(int y=0;y<dTimes.length;y++){
						c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dTimes[y].split(":")[0]));
						c.set(Calendar.MINUTE, Integer.valueOf(dTimes[y].split(":")[1]));
						c.set(Calendar.SECOND, 0);
						useTime=c.getTime();
						//分解时间点
						long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
						if(d==0){
							//用药时间点
							long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
							if(currentHHmm>firstHHmm){
								createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
							}
						}else if(d==days-1){
							//用药时间点
							long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
							if(currentHHmm<firstHHmm){
								createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
							}
						}else{
							createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
						}
						
					}
				}
			}else if("H".equals(frequencyUnit)){//时
				//从医嘱分解时间（operTime）到分解结束时间（decoEndTime）时间段获取总小时数（HSpan） ，
				//1、交替：计算循环次数=HSpan/交替频次 
				//2、非交替 period
//				c.setTime(operTime);
				int count=0;//循环次数
				long hSpan=timeBetweenDates(operTime,decoEndTime,"H");
				if(frequencyAlwaysFlag==1){//交替
					if(hSpan>0){
						count=(int)Math.floor(hSpan*1.0F/frequencyNum);
						for(int k=0;k<count;k++){
							//TODO:如果开立医嘱特殊频次不为空且为交替频次（单位时），按照特殊频次执行
							if(StringUtils.isNotBlank(period)){
								//持续频次（交替频次）不考虑执行时间点
							}else{
								if(k==0){
									c.set(Calendar.HOUR_OF_DAY, orderVO.getDateBgn().getHours());
									c.set(Calendar.MINUTE, orderVO.getDateBgn().getMinutes());
									c.set(Calendar.SECOND, 0);
									useTime=c.getTime();
								}else{
									useTime=new Date(useTime.getTime()+frequencyNum*60*60*1000);//第2，3，...的用药时间
								}
								createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
							}
//							createExcecDrugByTime(useTime, operTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
						}
						//TODO:设置主医嘱的本次分解时间与下次分解时间  下次分解时间计算：1、操作时间+分解天数。2、操作时间与循环次数及交替时间计算下次分解时间
					}
				}else{//非交替（持续）情况
					for(int d=0;d<days;d++){
						String[] dTimes=period.split(",");
						c.set(Calendar.DATE, c.get(Calendar.DATE)+d);
						for(int y=0;y<dTimes.length;y++){
							c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dTimes[y].split(":")[0]));
							c.set(Calendar.MINUTE, Integer.valueOf(dTimes[y].split(":")[1]));
							c.set(Calendar.SECOND, 0);
							useTime=c.getTime();
							//分解时间点
							long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
							if(d==0){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm>firstHHmm){
									createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
								}
							}else if(d==days-1){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm<firstHHmm){
									createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
								}
							}else{
								createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
							}
						}
					}
				}
			}else if("M".equals(frequencyUnit)){//频次单位为分钟 
				int count=0;//循环次数
				long mSpan=timeBetweenDates(operTime,decoEndTime,"M");
				if(frequencyAlwaysFlag==1){//交替
					if(mSpan>0){
						count=(int)Math.floor(mSpan*1.0F/frequencyNum);
						for(int k=0;k<count;k++){
							//TODO:如果开立医嘱特殊频次不为空且为交替频次（单位时），按照特殊频次执行
							if(StringUtils.isNotBlank(period)){
								//持续频次（交替频次）不考虑执行时间点
							}else{
								if(k==0){
									c.set(Calendar.HOUR_OF_DAY, orderVO.getDateBgn().getHours());
									c.set(Calendar.MINUTE, orderVO.getDateBgn().getMinutes());
									c.set(Calendar.SECOND, 0);
									useTime=c.getTime();
								}else{
									useTime=new Date(useTime.getTime()+frequencyNum*60*1000);//第2，3，...的用药时间
								}
								createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
							}
						}
						//TODO:设置主医嘱的本次分解时间与下次分解时间  下次分解时间计算：1、操作时间+分解天数。2、操作时间与循环次数及交替时间计算下次分解时间
					}
				}else{//非交替（持续）情况
					for(int d=0;d<days;d++){
						String[] dTimes=period.split(",");
						c.set(Calendar.DATE, c.get(Calendar.DATE)+d);
						for(int y=0;y<dTimes.length;y++){
							c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dTimes[y].split(":")[0]));
							c.set(Calendar.MINUTE, Integer.valueOf(dTimes[y].split(":")[1]));
							c.set(Calendar.SECOND, 0);
							useTime=c.getTime();
							//分解时间点
							long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
							if(d==0){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm>firstHHmm){
									createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
								}
							}else if(d==days-1){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm<firstHHmm){
									createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
								}
							}else{
								createExcecDrugByTime(useTime, operTime, decoEndTime, drugInfo, orderVO, msgMap, isPre, execDrug, execDrugList);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 产生药品执行记录
	 * @param useTime 服用时间
	 * @param operTime 操作时间
	 * @param decoEndTime 医嘱分解结束时间
	 * @param undrug 药品信息
	 * @param orderVO 医嘱对象
	 * @param msgMap 异常信息
	 * @param execUnDrug 药品执行对象
	 * @param execUndrugList 药品执行数组
	 */
	private void createExcecDrugByTime(Date useTime,Date operTime,Date decoEndTime, DrugInfo drugInfo,InpatientOrderInInterVO orderVO,Map<String,MsgInInterVO> msgMap,String isPre,InpatientExecdrug execDrug,List<InpatientExecdrug> execDrugList){
		InpatientOrder order=null;//需要停止的医嘱
		
		if(useTime.before(decoEndTime)){//服药时间在医嘱分解结束之前的
			if(null!=orderVO.getDateNxtmodc()){//服药时间在下次分解时间点之后 生成执行记录
				if(useTime.before(orderVO.getDateNxtmodc())){
					return;	
				}
			}
			if(null!=orderVO.getDateEnd() && useTime.after(orderVO.getDateEnd())){//服药时间在设定的医嘱停止时间之后，则不分解医嘱，停止医嘱
				BeanUtils.copyProperties(orderVO, order);
				orderVO.setDcConfirmDate(operTime);
				orderVO.setDcConfirmFlag(1);
				User user = (User) SessionUtils.getCurrentUserFromShiroSession();
				orderVO.setDcConfirmOper(user.getId()); 
				orderVO.setStop_flg(1);//设置为已停止
				inpatientOrderInInterDao.stopInvalidOrder(order);
			}else{
				orderVO.setDateBgn(useTime);//设置药品服用时间
				if(StringUtils.isBlank(orderVO.getExecDpcd())){//如果医嘱的执行科室为空，默认设置为患者的住院科室
					orderVO.setExecDpcd(orderVO.getPatient().getDeptCode());
					orderVO.setExecDpnm(deptInInterService.get(orderVO.getPatient().getDeptCode()).getDeptName());//执行科室设置为患者住院科室
				}
				/**以上set属性为临时变量**/
				
				String execDrugId = String.format("Y" + "%12s",inpatientOrderInInterDao.getSequece("SEQ_INPATIENT_EXECDRUG")).replaceAll("\\s", "0");//生成药品执行记录流水号
				orderVO.setExecId(execDrugId);//临时变量，用于保存执行流水号

				execDrug=saveExecdrug(orderVO,drugInfo);
				if(execDrug==null){//该药品已停用，
					MsgInInterVO msgVO=new MsgInInterVO();
					msgVO.setMsgStatus(1);
					msgVO.setErrorMsg(orderVO.getPatient().getBedName()+"床患者“"+orderVO.getPatient().getPatientName()+"”医嘱名称为    "+orderVO.getItemName()+ "分解失败，请联系管理员！");
					msgMap.put(orderVO.getMoOrder(), msgVO);
					return;
				}
				if(execDrugList!=null){
					execDrugList.add(execDrug);
				}
			}
		}
	}
	
	/**
     * 计算两时间之间相差的时间（单位可分为秒、分、时、天）
     * @param startDate
     * @param endDate
     */
    private long  timeBetweenDates(Date startDate,Date endDate,String type){
//    	Map<String,Object> map=new HashMap<String,Object>();
//    	if(startDate==null||endDate==null){
//    		map.put("resCode", "1");
//    		map.put("resMsg", "时间参数为空!");
//    		return map;
//    	}
    	long times=0;
        long time = endDate.getTime() -startDate.getTime();// 得出的时间间隔是毫秒  
        type=type.toLowerCase().trim();
        if("D".equals(type)){
        	times=time/(3600000*24);
        }else if("H".equals(type)){
        	times=time/3600000;
        }else if("M".equals(type)){
        	times=time/216000000;
        }
    	return times;
    }

	/**
	 * 保存药品执行档
	 * 
	 * @author zl
	 * @createDate： 2016年4月20日 下午2:10:01
	 * @modifier liujl
	 * @modifyDate：2016年4月26日 下午08:03:01
	 * @param： 医嘱对象，execDrugId:执行单流水号，drugInfo:药品信息，countQty开立数量
	 * @modifyRmk：
	 * @version 1.0
	 */
	private InpatientExecdrug saveExecdrug(InpatientOrderInInterVO orderVO,DrugInfo drugInfo) {
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();//获取当前登录人
		InpatientExecdrug drugExec = new InpatientExecdrug();//药品执行档
		drugExec.setExecId(orderVO.getExecId());//执行医嘱流水号
		drugExec.setDeptCode(orderVO.getPatient().getDeptCode());//患者住院科室
		drugExec.setNurseCellCode(orderVO.getNurseCellCode());//患者所在病区
		drugExec.setListDpcd(orderVO.getListDpcd());//医嘱开立科室

		drugExec.setInpatientNo(orderVO.getInpatientNo());
		drugExec.setPatientNo(orderVO.getPatientNo());
		drugExec.setMoOrder(orderVO.getMoOrder());
		drugExec.setDocCode(orderVO.getDocCode());
		drugExec.setDocName(orderVO.getDocName());//长度不一致

		drugExec.setMoDate(orderVO.getMoDate());
		drugExec.setBabyFlag(orderVO.getBabyFlag());
		
		drugExec.setHappenNo(Integer.valueOf(orderVO.getInpatientNo().substring(3,4)));//婴儿序号
		drugExec.setSetItmattr(orderVO.getSetItmattr());//项目属性,1院内项目/2院外项目
		drugExec.setSetSubtbl(orderVO.getSetSubtbl());//是否包含附材

		drugExec.setTypeCode(orderVO.getTypeCode());//医嘱类别代码
		drugExec.setDecmpsState(orderVO.getDecmpsState());//医嘱是否分解
		drugExec.setChargeState(orderVO.getChargeState());//是否计费
		drugExec.setNeedDrug(orderVO.getNeedDrug());//药房是否配药

		drugExec.setPrnExelist(0); //打印执行单
		drugExec.setPrnMorlist(0);//是否打印医嘱单，来源于医嘱类型
		drugExec.setNeedConfirm(orderVO.getNeedConfirm());//是否需要确认
		drugExec.setDrugCode(orderVO.getItemCode());//药品编码
		if(!"userdefined".equals(orderVO.getItemCode())){
			drugExec.setDrugName(drugInfo.getName());//药品名称
			drugExec.setEnglishAb(drugInfo.getDrugEnname());//英文品名
	
			drugExec.setBaseDose(drugInfo.getDrugBasicdose());//药品基本剂型
			drugExec.setDoseUnit(drugInfo.getDrugDoseunit());//剂量单位
			drugExec.setMinUnit(drugInfo.getUnit());//最小单位
			drugExec.setPackQty(drugInfo.getPackagingnum());//包装数量
			
			drugExec.setDrugType(drugInfo.getDrugType());//药品分类：1-西药；2-中成药；3-草药
			drugExec.setSpecs(drugInfo.getSpec());//规格
			drugExec.setItemPrice(drugInfo.getDrugRetailprice());//零售价
		}else{
			drugExec.setDrugName(orderVO.getItemName());//药品名称
			drugExec.setEnglishAb(orderVO.getEnglishAb());//英文品名
	
			drugExec.setBaseDose(orderVO.getBaseDose());//药品基本剂型
			drugExec.setDoseUnit(orderVO.getDoseUnit());//剂量单位
			drugExec.setMinUnit(orderVO.getMinUnit());//最小单位
			drugExec.setPackQty(orderVO.getPackQty());//包装数量
			
			drugExec.setDrugType(orderVO.getDrugType());//药品分类：1-西药；2-中成药；3-草药
			drugExec.setSpecs(orderVO.getSpecs());//规格
			drugExec.setItemPrice(orderVO.getItemPrice());//零售价
		}
		drugExec.setPriceUnit(orderVO.getMinUnit());//收费单位，目前使用最小单位收费
		drugExec.setDoseModelCode(orderVO.getDoseModelCode());//剂型
		drugExec.setDrugQuality(orderVO.getDrugQuality());//药品性质

		drugExec.setStockMin(2); //1护士站常备/2扣药房 默认药房
		drugExec.setCombNo(orderVO.getCombNo());//组合号
		drugExec.setMainDrug(orderVO.getMainDrug());//主药标志
		drugExec.setUsageCode(orderVO.getUsageCode());//用法编码
		drugExec.setUseName(orderVO.getUseName());//用法名称

		drugExec.setEnglishAb(orderVO.getEnglishAb());
		drugExec.setFrequencyCode(orderVO.getFrequencyCode());
		drugExec.setDoseOnce(orderVO.getDoseOnce());
		drugExec.setUseDays(orderVO.getUseDays());//使用次数，主药用于草药
		drugExec.setQtyTot(orderVO.getTempQtyTot());//药品用量

		drugExec.setUseTime(orderVO.getDateBgn());//服药时间
		drugExec.setPharmacyCode(orderVO.getPharmacyCode());//取药药房，或者叫扣库科室
		drugExec.setExecDpcd(orderVO.getExecDpcd());//执行护士
		drugExec.setValidFlag(1);

		drugExec.setDrugedFlag(orderVO.getDrugedFlag()==null?0:Integer.valueOf(orderVO.getDrugedFlag()));//默认为不需要发送
		drugExec.setChargeFlag(orderVO.getIfFee());//记账标志
		drugExec.setChargeDate(orderVO.getChargeDate());
		drugExec.setChargeDeptcd(orderVO.getChargeDeptcd());
		drugExec.setChargeUsercd(orderVO.getChargeUsercd());
		
		drugExec.setExecFlag(orderVO.getExecuteFlag());;
		drugExec.setExecDate(orderVO.getExecuteDate());
		drugExec.setExecUsercd(orderVO.getExecuteUsercd());
		drugExec.setExecDeptcd(orderVO.getExecDpcd());
		
		drugExec.setRecipeNo(null); // 处方流水号,来源于费用的处方号
		drugExec.setSequenceNo(0);// 收费序号,来源于费用的收费序号

		drugExec.setMoNote1(orderVO.getMoNote1());
		drugExec.setMoNote2(orderVO.getMoNote2());
		drugExec.setDecoDate(orderVO.getUpdateTime());//分解时间
		drugExec.setFrequencyName(orderVO.getFrequencyName());
		
		return drugExec;
	}
	
	/**
	 * 根据执行医嘱信息判断非药品是否物资，库存是否充足，决定是否收费
	 * @param feeList
	 * @param execUndrug
	 * @param msgMape
	 */
	private String generateMatByExc(InpatientExecundrug execUndrug, Map<String,MsgInInterVO> msgMape) {
		MsgInInterVO msgVO=null;
		OutputInInterVO outVo=new OutputInInterVO();
		outVo.setStorageCode(execUndrug.getDeptCode());//执行科室(仓库)编码
		outVo.setInpatientNo(execUndrug.getInpatientNo());//住院流水号(或门诊号)
		outVo.setUndrugItemCode(execUndrug.getUndrugCode());//非药品Id
		outVo.setApplyNum(execUndrug.getQtyTot().doubleValue());//申请数量
		outVo.setUseNum(execUndrug.getQtyTot().doubleValue());//申请数量
		outVo.setTransType(1);//正交易
		matOutPutInInterService.judgeMat(outVo);
		Map<String,Object> matMap=matOutPutInInterService.judgeMat(outVo);//判断物资是否存在，库存是否充足
		int matStatus=((Integer)matMap.get("resCode")).intValue();
		if(matStatus==0||matStatus==3){
			execUndrug.setIsMat(1);
		}else{
			msgVO=new MsgInInterVO();
			msgVO.setMsgStatus(1);
			msgVO.setErrorMsg("分解患者   "+execUndrug.getPatient().getPatientName()+" 的医嘱流水号为  "+execUndrug.getMoOrder()+"时，项目名称为    "+execUndrug.getUndrugName()+ " 物资库存不足，请联系医生终止医嘱！");
			msgMape.put(execUndrug.getMoOrder(), msgVO);
			return null;
		}
		return "sucess";
	}
	
	/**
	 * 生成药品出库记录
	 * @param execDrug 药品医嘱执行记录
	 * @param drugInfo 药品实体
	 * @param isPre 是否预扣库存
	 * @param opType 操作类型：4-住院摆药；5-住院退药
	 * @param msgMap 返回页面的异常信息
	 * @param hszCharge 护士站是否收费
	 * @return
	 */
	private DrugApplyout setDrugApplyOutByExec(InpatientExecdrug execDrug,
			DrugInfo drugInfo, String isPre, int opType, Map<String, MsgInInterVO> msgMap, String hszCharge) {		
		DrugApplyout applyout = new DrugApplyout();
		DrugBilllist billlist = inpatientOrderInInterDao.getListByProperty(execDrug.getPharmacyCode(), execDrug.getTypeCode(),execDrug.getUsageCode(), drugInfo.getDrugType(),drugInfo.getDrugNature(), drugInfo.getDrugDosageform());
		/*if(StringUtils.isBlank(billlist.getId())){
			MsgInInterVO msgVO = new MsgInInterVO();
			msgVO.setMsgStatus(1);
			CodeDrugtype drugType=codeDrugtypeService.get(execDrug.getDrugType());//药品类型
			CodeDosageform dosageForm=codeDosageformService.get(execDrug.getDoseModelCode());//剂型
			CodeDrugproperties drugQuality=codeDrugpropertiesService.get(execDrug.getDrugQuality());//药品性质
			InpatientKind typeName=kindServiceInfo.get(execDrug.getTypeCode());//医嘱类型
			msgVO.setErrorMsg("摆药对应的摆药单未进行设置! 请与药学部或信息科联系。"
					+ " \n医嘱类型:" + typeName.getTypeName() 
					+ " \n药品类型:" + drugType.getName()
		            + " \n用法:" + execDrug.getUseName()
		            + " \n药品性质:" + drugQuality.getName()
		            + " \n药品剂型:" + dosageForm.getName());
			msgMap.put(execDrug.getMoOrder(), msgVO);
			return null;
		}*/
		String applyNumber = inpatientOrderInInterDao.getSequece("SEQ_DRUG_APPLYOUT").toString();// 根据sequence,获取applyNumber
		applyout.setApplyNumber(Integer.valueOf(applyNumber)); //设置药品出库申请流水号
		applyout.setDeptCode(execDrug.getDeptCode());// 出库申请科室编码，默认住院科室
		applyout.setApplyOpercode(execDrug.getDocCode());//出库申请人
		applyout.setDrugDeptCode(execDrug.getPharmacyCode()); // 开药（扣库）科室编码
		applyout.setRecipeDept(execDrug.getListDpcd());// 医嘱开立科室
		applyout.setRecipeOper(execDrug.getDocCode());// 开立医生
		applyout.setPatientId(execDrug.getInpatientNo());//设置患者的住院流水号
		applyout.setPatientDept(execDrug.getDeptCode());
		applyout.setDoseOnce(execDrug.getDoseOnce());
		applyout.setDoseUnit(execDrug.getDoseUnit());
		applyout.setUsageCode(execDrug.getUsageCode());
		applyout.setUseName(execDrug.getUseName());
		applyout.setDfqFreq(execDrug.getFrequencyCode());
		applyout.setValidState(1);//有效
		applyout.setDfqCexp(execDrug.getFrequencyName());
		applyout.setDoseModelCode(execDrug.getDoseModelCode());
		applyout.setOrderType(execDrug.getTypeCode());
		applyout.setMoOrder(execDrug.getMoOrder());
		applyout.setCombNo(execDrug.getCombNo());
		applyout.setExecSqn(execDrug.getExecId());
		applyout.setBabyFlag(execDrug.getBabyFlag());
		applyout.setOpType(opType); //操作类型分类  1 门诊发药 ， 2 内部入库,3 门诊退药，4 住院摆药 ,5住院退药
		applyout.setDrugCode(execDrug.getDrugCode());
		if(drugInfo!=null){
			applyout.setTradeName(drugInfo.getName());
		}else{
			applyout.setTradeName(drugInfo.getName());
		}
		applyout.setCompoundGroup("4" + DateUtils.formatDateTime(execDrug.getUseTime(), "yyyy-MM-dd") + execDrug.getCombNo() + "C");//批次流水号,根据医嘱执行时间及组合号赋值
		applyout.setDrugType(drugInfo.getDrugType());
		applyout.setDrugQuality(drugInfo.getDrugNature());
		applyout.setSpecs(drugInfo.getSpec());// 规格
		applyout.setPackUnit(drugInfo.getDrugPackagingunit());// 包装单位
		applyout.setPackQty(drugInfo.getPackagingnum()); // 包装数
		applyout.setBillclassCode(billlist.getId() != null?billlist.getDrugBillclass().getId():"0");
		applyout.setMinUnit(execDrug.getMinUnit());
		applyout.setShowFlag(0); // 显示的单位为最小单位
		applyout.setShowUnit(execDrug.getMinUnit());
		applyout.setRetailPrice(drugInfo.getDrugRetailprice());
		applyout.setWholesalePrice(drugInfo.getDrugWholesaleprice());
		applyout.setPurchasePrice(drugInfo.getDrugPurchaseprice());
//		String appliBill = new SimpleDateFormat("yyMM").format(new Date());
//		applyout.setApplyBillcode(execDrug.getApplyNo());
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		applyout.setApplyOpercode(user.getId());
		applyout.setApplyDate(execDrug.getUpdateTime());
		applyout.setApplyState(0);
		applyout.setApplyNum(execDrug.getQtyTot()); // 出库总量，按最小单位
		applyout.setReturnNum(execDrug.getQtyTot()); //// 可退总量，按最小单位
		applyout.setDays(execDrug.getUseDays());

		//设置药品发送类型
		if("1".equals(hszCharge)){//已收费的
			if("05".equals(execDrug.getTypeCode()) || "07".equals(execDrug.getTypeCode())){//医嘱类型为出院带药和请假带药的且已经收费的，设置医嘱的发药类型为临时发药2。
				execDrug.setDrugedFlag(2);//发药标记:0不需发送/1集中发送/2分散发送/3已配药
				applyout.setSendType(2);//医嘱发送类型2临时，1集中，0未发送
			}else{
				execDrug.setDrugedFlag(1);
				applyout.setSendType(1);
			}
		}else{//未收费的
			execDrug.setDrugedFlag(2);
			applyout.setSendType(2);//未发送
		}
		applyout.setChargeFlag(execDrug.getChargeFlag());//1-收费，0-划价
		return applyout;
	}
	
	/**
	 * 按频次分解生成非药品执行记录
	 * @param orderVO
	 * @param useQty
	 * @param execList
	 * @param operTime
	 * @param decoEndTime
	 * @param days 
	 * @throws Exception 
	 * @throws Exception 
	 */
	private void DecoUndrugByFrequence(InpatientOrderInInterVO orderVO, Date operTime,Date decoEndTime, int days, DrugUndrug undrug,Map<String, MsgInInterVO> msgMap, int i,List<InpatientExecundrug> execUndrugList) throws Exception {

		// TODO：根据医嘱执行频次生成执行记录
		/**
		 * 以下处理医嘱的频次，根据频次生成医嘱执行记录
		 * XXX:按频次生成执行记录
		 */
		days+=1;//默认加上1天，代表今天分解时间开始到领导以及隔天分解时间点之前24小时（两天）的。
		Date useTime=null;//服药时间
		InpatientExecundrug execUnDrug=null;//药品执行记录
		Date nextDecoTime=orderVO.getDateNxtmodc();//医嘱下次分解时间
		BusinessFrequency frequency = inpatientOrderInInterDao.getListByName(orderVO.getFrequencyCode()); // 查询频次表
		String period = null; // 频次对应的时间
		Integer frequencyTime=0;//频次次数
		Integer frequencyNum=0;//频次数目
		String frequencyUnit=null;//频次单位
		Integer frequencyAlwaysFlag=0;//频次持续标记 0-不持续 1-持续
		//目前医嘱开立方式，特殊频次和来源于医嘱频次表中的时间点设置不兼容，5个以上的特殊频次无法设置。
		if (StringUtils.isNotBlank(orderVO.getExecTimes())) { // 如果特殊频次不为空
			//TODO:医嘱资料档中的频次数据
			period = orderVO.getExecTimes();//默认是每天几次，如08:30,09:30,10:30...
			frequencyTime=period.split(",").length;
			frequencyUnit="D";//默认单位 天
			frequencyNum=1;//默认为1 即每天
		} else { // 否则医嘱频次
			//TODO:频次表中的频次数据
			period = frequency.getPeriod();
			frequencyTime=frequency.getFrequencyTime();
			frequencyNum=frequency.getFrequencyNum();
			frequencyUnit=frequency.getFrequencyUnit();
			frequencyUnit=StringUtils.isNotBlank(frequencyUnit)?frequencyUnit.toUpperCase():frequencyUnit;
			frequencyAlwaysFlag=frequency.getAlwaysFlag();
		}
		if(frequencyUnit!=null){
			Calendar c=Calendar.getInstance();
			c.setTime(operTime);
			if("ONCE".equals(frequencyUnit)){//仅一次
				for(int x=0;x<days;x++){
					c.setTime(operTime);
					c.set(Calendar.DATE, operTime.getDate()+x);
					c.set(Calendar.MINUTE, operTime.getMinutes()+1);
					useTime=c.getTime();
					createExcecUnDrugByTime(useTime, operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
				}
			}else if("T".equals(frequencyUnit)){//需求（必要时）
				for(int y=0;y<days;y++){
					c.setTime(operTime);
					c.set(Calendar.DATE, operTime.getDate()+y);
					c.set(Calendar.MINUTE, operTime.getMinutes()+1);
					useTime=c.getTime();
					createExcecUnDrugByTime(useTime, operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
				}
			}else if("W".equals(frequencyUnit)){//周
//				long hSpan=timeBetweenDates(operTime,decoEndTime,"D");//操作时间与分解结束时间（操作时间+分解天数）相隔天数
				//08:00|1,08:00|4,18:00|1,18:00|4  -2次/周   2/周（周一、周四）
				//08:00|1,08:00|5                  -2次/周   2/周（周五、周一）
				//08:00|1							1次/2周（周一）
//				c.setTime(operTime);
				Map<String, ArrayList<String>> wMap=new HashMap<String,ArrayList<String>>();
				if(StringUtils.isNotBlank(period)){
					String[] periodArr=period.split(",");
					for(int p=0;p<periodArr.length;p++){
						String[] weeks=periodArr[p].split("\\|");
						if(wMap.containsKey(weeks[1])){
							wMap.get(weeks[1]).add(weeks[0]);
						}else{
							ArrayList<String> wTimes=new ArrayList<String>();
							wTimes.add(weeks[0]);
							wMap.put(weeks[1], wTimes);
						}
					}
				}
				for(int w=0;w<days;w=w+(frequencyNum==1?frequencyNum:(frequencyNum*7))){
					Date tempDate=new Date(operTime.getTime());//+w*24*60*60*1000
					tempDate=DateUtils.addDay(tempDate,w);//加上w天
					c.setTime(tempDate);
//					c.set(Calendar.DATE, c.get(Calendar.DATE)+w);
					int week=c.get(Calendar.DAY_OF_WEEK)-1;
					if(wMap.containsKey(String.valueOf(week))){
						for(String time:wMap.get(String.valueOf(week))){//时间点
							c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
							c.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
							c.set(Calendar.SECOND, 0);
							useTime=c.getTime();
							//分解时间点
							long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
							if(w==0){
								//用药时间点
								long currentHHmm=Integer.valueOf(time.split(":")[0])*60+Integer.valueOf(time.split(":")[1]);
								if(currentHHmm>firstHHmm){
									createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
								}
							}else if(w==days-1){
								//用药时间点
								long currentHHmm=Integer.valueOf(time.split(":")[0])*60+Integer.valueOf(time.split(":")[1]);
								if(currentHHmm<firstHHmm){
									createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
								}
							}else{
								createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
							}
						}
					}
				}
			}else if("D".equals(frequencyUnit)){//天  
//				c.setTime(operTime);
				//举例：4次/日=4/日（8-11-16-20）=08:00,12:00,18:00,22:00
				for(int d=0;d<days;d=d+frequencyNum){
					c.setTime(operTime);
					String[] dTimes=period.split(",");
					c.set(Calendar.DATE, c.get(Calendar.DATE)+d);
					for(int y=0;y<dTimes.length;y++){
						c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dTimes[y].split(":")[0]));
						c.set(Calendar.MINUTE, Integer.valueOf(dTimes[y].split(":")[1]));
						c.set(Calendar.SECOND, 0);
						useTime=c.getTime();
						//分解时间点
						long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
						if(d==0){
							//用药时间点
							long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
							if(currentHHmm>firstHHmm){
								createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
							}
						}else if(d==days-1){
							//用药时间点
							long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
							if(currentHHmm<firstHHmm){
								createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
							}
						}else{
							createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
						}
						
					}
				}
			}else if("H".equals(frequencyUnit)){//时
				//从医嘱分解时间（operTime）到分解结束时间（decoEndTime）时间段获取总小时数（HSpan） ，
				//1、交替：计算循环次数=HSpan/交替频次 
				//2、非交替 period
//				c.setTime(operTime);
				int count=0;//循环次数
				long hSpan=timeBetweenDates(operTime,decoEndTime,"H");
				if(frequencyAlwaysFlag==1){//交替
					if(hSpan>0){
						count=(int)Math.floor(hSpan*1.0F/frequencyNum);
						for(int k=0;k<count;k++){
							//TODO:如果开立医嘱特殊频次不为空且为交替频次（单位时），按照特殊频次执行
							if(StringUtils.isNotBlank(period)){
								//持续频次（交替频次）不考虑执行时间点
							}else{
								if(k==0){
									c.set(Calendar.HOUR_OF_DAY, orderVO.getDateBgn().getHours());
									c.set(Calendar.MINUTE, orderVO.getDateBgn().getMinutes());
									c.set(Calendar.SECOND, 0);
									useTime=c.getTime();
								}else{
									useTime=new Date(useTime.getTime()+frequencyNum*60*60*1000);//第2，3，...的用药时间
								}
								createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
							}
						}
						//TODO:设置主医嘱的本次分解时间与下次分解时间  下次分解时间计算：1、操作时间+分解天数。2、操作时间与循环次数及交替时间计算下次分解时间
					}
				}else{//非交替（持续）情况
					for(int d=0;d<days;d++){
						String[] dTimes=period.split(",");
						c.set(Calendar.DATE, c.get(Calendar.DATE)+d);
						for(int y=0;y<dTimes.length;y++){
							c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dTimes[y].split(":")[0]));
							c.set(Calendar.MINUTE, Integer.valueOf(dTimes[y].split(":")[1]));
							c.set(Calendar.SECOND, 0);
							useTime=c.getTime();
							//分解时间点
							long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
							if(d==0){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm>firstHHmm){
									createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
								}
							}else if(d==days-1){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm<firstHHmm){
									createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
								}
							}else{
								createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
							}
						}
					}
				}
			}else if("M".equals(frequencyUnit)){//频次单位为分钟 
				int count=0;//循环次数
				long mSpan=timeBetweenDates(operTime,decoEndTime,"M");
				if(frequencyAlwaysFlag==1){//交替
					if(mSpan>0){
						count=(int)Math.floor(mSpan*1.0F/frequencyNum);
						for(int k=0;k<count;k++){
							//TODO:如果开立医嘱特殊频次不为空且为交替频次（单位时），按照特殊频次执行
							if(StringUtils.isNotBlank(period)){
								//持续频次（交替频次）不考虑执行时间点
							}else{
								if(k==0){
									c.set(Calendar.HOUR_OF_DAY, orderVO.getDateBgn().getHours());
									c.set(Calendar.MINUTE, orderVO.getDateBgn().getMinutes());
									c.set(Calendar.SECOND, 0);
									useTime=c.getTime();
								}else{
									useTime=new Date(useTime.getTime()+frequencyNum*60*1000);//第2，3，...的用药时间
								}
								createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
							}
						}
						//TODO:设置主医嘱的本次分解时间与下次分解时间  下次分解时间计算：1、操作时间+分解天数。2、操作时间与循环次数及交替时间计算下次分解时间
					}
				}else{//非交替（持续）情况
					for(int d=0;d<days;d++){
						String[] dTimes=period.split(",");
						c.set(Calendar.DATE, c.get(Calendar.DATE)+d);
						for(int y=0;y<dTimes.length;y++){
							c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dTimes[y].split(":")[0]));
							c.set(Calendar.MINUTE, Integer.valueOf(dTimes[y].split(":")[1]));
							c.set(Calendar.SECOND, 0);
							useTime=c.getTime();
							//分解时间点
							long firstHHmm=operTime.getHours()*60+operTime.getMinutes();
							if(d==0){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm>firstHHmm){
									createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
								}
							}else if(d==days-1){
								//用药时间点
								long currentHHmm=Integer.valueOf(dTimes[y].split(":")[0])*60+Integer.valueOf(dTimes[y].split(":")[1]);
								if(currentHHmm<firstHHmm){
									createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
								}
							}else{
								createExcecUnDrugByTime(orderVO.getDateBgn(), operTime, decoEndTime, undrug, orderVO, msgMap,  execUnDrug, execUndrugList);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 产生非药品执行记录
	 * @param useTime 服用时间
	 * @param operTime 操作时间
	 * @param decoEndTime 医嘱分解结束时间
	 * @param undrug 非药品信息
	 * @param orderVO 医嘱对象
	 * @param msgMap 异常信息
	 * @param execUnDrug 非药品执行对象
	 * @param execUndrugList 非药品执行数组
	 */
	private void createExcecUnDrugByTime(Date useTime, Date operTime,
			Date decoEndTime, DrugUndrug undrug, InpatientOrderInInterVO orderVO,
			Map<String, MsgInInterVO> msgMap, InpatientExecundrug execUnDrug,
			List<InpatientExecundrug> execUndrugList) {
		InpatientOrder order=null;//需要停止的医嘱
		if(useTime.before(decoEndTime)){//服药时间在医嘱分解结束之前的
			if(null!=orderVO.getDateNxtmodc()){//服药时间在下次分解时间点之后 生成执行记录
				if(useTime.before(orderVO.getDateNxtmodc())){
					return;	
				}
			}
			if(null!=orderVO.getDateEnd() && useTime.after(orderVO.getDateEnd())){//服药时间在设定的医嘱停止时间之后，则不分解医嘱，停止医嘱
				BeanUtils.copyProperties(orderVO, order);
				orderVO.setDcConfirmDate(operTime);
				orderVO.setDcConfirmFlag(1);
				User user=(User) SessionUtils.getCurrentUserFromShiroSession();
				orderVO.setDcConfirmOper(user.getId()); 
				orderVO.setStop_flg(1);//设置为已停止
				inpatientOrderInInterDao.stopInvalidOrder(order);
			}else{
				orderVO.setDateBgn(useTime);//设置药品服用时间
				if(null==orderVO.getExecDpcd()){//如果患者执行科室为空，设置医嘱的执行科室为患者的住院科室
					orderVO.setExecDpcd(orderVO.getPatient().getDeptCode());
					orderVO.setExecDpnm(deptInInterService.get(orderVO.getPatient().getDeptCode()).getDeptName());
				}
				String execUnDrugId=String.format("F" + "%12s",inpatientOrderInInterDao.getSequece("SEQ_INPATIENT_EXECUNDRUG")).replaceAll("\\s", "0");//生成非药品执行记录流水号
				orderVO.setExecId(execUnDrugId);//临时变量保存医嘱执行流水号
				execUnDrug=saveExecUndrug(orderVO,undrug);
				if(execUnDrug==null){//该药品已停用，
					MsgInInterVO msgVO=new MsgInInterVO();
					msgVO.setMsgStatus(1);
					msgVO.setErrorMsg(orderVO.getPatient().getBedName()+"床患者“"+orderVO.getPatient().getPatientName()+"”医嘱名称为    "+orderVO.getItemName()+ "分解失败，请联系管理员！");
					msgMap.put(orderVO.getMoOrder(), msgVO);
					return;
				}
				if(execUndrugList!=null){
					execUndrugList.add(execUnDrug);
				}
			}
		}
	}
	
	/**
	 * 保存非药嘱执行档
	 * 
	 * @author zl
	 * @createDate： 2016年4月19日 下午5:07:57
	 * @modifier liujl, yueyf
	 * @modifyDate：2016年4月27日 下午4:27:57
	 * @param： InpatientOrder info:医嘱对象, DrugUndrug undrug:非药品对象
	 * @modifyRmk：
	 * @version 1.0
	 */
	private InpatientExecundrug saveExecUndrug(InpatientOrderInInterVO orderVO,DrugUndrug drugUndrug) {
		//XXX:保存非药品医嘱执行档【二个参数：InpatientOrderVO orderVO,DrugUndrug drugUndrug】
		User user=(User) SessionUtils.getCurrentUserFromShiroSession();//当前操作人
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginNursingStationShiroSession();//当前登录病区
		InpatientExecundrug undrugExec = new InpatientExecundrug();
		undrugExec.setExecId(orderVO.getExecId());
		undrugExec.setDeptCode(orderVO.getDeptCode());
		undrugExec.setNurseCellCode(orderVO.getNurseCellCode());
		undrugExec.setListDpcd(orderVO.getListDpcd());

		undrugExec.setInpatientNo(orderVO.getInpatientNo());
		undrugExec.setPatientNo(orderVO.getPatientNo());
		undrugExec.setMoOrder(orderVO.getMoOrder());
		undrugExec.setDocCode(orderVO.getDocCode());
		undrugExec.setDocName(orderVO.getDocName());

		undrugExec.setMoDate(orderVO.getMoDate());
		undrugExec.setBabyFlag(orderVO.getBabyFlag());
		undrugExec.setHappenNo(Integer.valueOf(orderVO.getInpatientNo().substring(3,4)));//获取住院主表中的发生序号
		undrugExec.setSetItmattr(orderVO.getSetItmattr());
		undrugExec.setSetSubtbl(orderVO.getSetSubtbl());
		undrugExec.setTypeCode(orderVO.getTypeCode());

		undrugExec.setChargeState(orderVO.getChargeState());

		undrugExec.setPrnExelist(0);
		undrugExec.setPrnMorlist(0);
		undrugExec.setNeedConfirm(orderVO.getNeedConfirm());
		undrugExec.setUndrugCode(orderVO.getItemCode());
		undrugExec.setUndrugName(orderVO.getItemName());
		undrugExec.setClassCode(orderVO.getClassCode());
		if(!"userdefined".equals(orderVO.getItemCode())){
			undrugExec.setStockUnit(drugUndrug.getUnit()); // 单位
			undrugExec.setUnitPrice(drugUndrug.getDefaultprice());// 单价
			undrugExec.setUndrugCode(drugUndrug.getId());
			undrugExec.setUndrugName(drugUndrug.getName());

			if(StringUtils.isNotBlank(drugUndrug.getUndrugDept())){
				undrugExec.setExecDpcd(drugUndrug.getUndrugDept());
				undrugExec.setExecDpnm(deptInInterService.get(drugUndrug.getUndrugDept()).getDeptName());
			}else{
				undrugExec.setExecDpcd(orderVO.getExecDpcd());
				undrugExec.setExecDpnm(orderVO.getExecDpnm());
			}
		}else{
			undrugExec.setStockUnit(orderVO.getPriceUnit()); // 取最小单位
			undrugExec.setUnitPrice(orderVO.getItemPrice());// 单价
			undrugExec.setUndrugCode(orderVO.getItemCode());
			undrugExec.setUndrugName(orderVO.getItemName());
			undrugExec.setExecDpcd(orderVO.getExecDpcd());
			undrugExec.setExecDpnm(orderVO.getExecDpnm());
		}
		undrugExec.setClassName(orderVO.getClassName());
		undrugExec.setCombNo(orderVO.getCombNo());

		undrugExec.setMainDrug(orderVO.getMainDrug());
		if(StringUtils.isBlank(orderVO.getFrequencyCode())){
			undrugExec.setDfqFreq("QD1");//默认一天执行一次
		}else{
			undrugExec.setDfqFreq(orderVO.getFrequencyCode());
		}
		undrugExec.setDfqCexp(orderVO.getFrequencyName());
		undrugExec.setQtyTot(orderVO.getQtyTot().intValue()); // 项目数量
		undrugExec.setUseTime(orderVO.getDateBgn());
		undrugExec.setEmcFlag(orderVO.getEmcFlag());
		undrugExec.setValidFlag(1);//有效的
		if(null!=orderVO.getIfFee() && orderVO.getIfFee()==1){
			undrugExec.setConfirmDate(orderVO.getUpdateTime());
			undrugExec.setConfirmUsercd(user.getId());
			undrugExec.setConfirmDeptcd(dept.getId());
		}
		undrugExec.setChargeFlag(orderVO.getIfFee());
		undrugExec.setChargeDate(orderVO.getChargeDate());
		undrugExec.setChargeDeptcd(orderVO.getChargeDeptcd());
		undrugExec.setChargeUsercd(orderVO.getChargeUsercd());
		
		undrugExec.setExecFlag(orderVO.getExecuteFlag());
		undrugExec.setExecUsercd(orderVO.getExecuteUsercd());
		undrugExec.setExecDate(orderVO.getDateBgn());
		undrugExec.setExecDeptcd(dept.getId());
		
		undrugExec.setItemNote(orderVO.getItemNote());
		undrugExec.setApplyNo(null);//来源于物资的出库单流水号

		undrugExec.setMoNote1(orderVO.getMoNote1());
		undrugExec.setMoNote2(orderVO.getMoNote2());
		undrugExec.setDecoDate(orderVO.getUpdateTime());
		undrugExec.setRecipeNo(null);//来源于费用的处方号
		undrugExec.setSequenceNo(1); //来源于费用的收费序号

		undrugExec.setSubtblFlag(orderVO.getSubtblFlag());
		undrugExec.setLabCode(orderVO.getLabCode());
		
		return undrugExec;
	}
	
	@Override
	public InpatientOrder get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientOrder arg0) {
		
	}
	
}
