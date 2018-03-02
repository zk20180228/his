package cn.honry.finance.daybalance.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.finance.daybalance.dao.BalancedetailDAO;
import cn.honry.finance.daybalance.dao.DaybalanceDAO;
import cn.honry.finance.daybalance.service.DaybalanceService;
import cn.honry.finance.daybalance.vo.AllPayTypeVo;
import cn.honry.finance.daybalance.vo.PayTypeVo;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.outpatient.register.dao.RegisterInfoInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("daybalanceService")
@Transactional
@SuppressWarnings({ "all" })
public class DaybalanceServiceImpl implements DaybalanceService{

	public static final String STRINGZIFEI="自费";//自费
	
	@Autowired
	@Qualifier(value = "daybalanceDAO")
	private DaybalanceDAO daybalanceDAO;
	@Autowired
	private BalancedetailDAO balancedetailDAO;
	@Autowired
	private RegisterInfoInInterDAO registerInfoDAO;

	@Autowired
	private CodeInInterDAO codeInInterDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterDaybalance get(String id) {
		return daybalanceDAO.get(id);
	}

	@Override
	public void saveOrUpdate(RegisterDaybalance entity) {
		
	}
	
	/**  
	 *  
	 * @Description：   获得开始时间 (上一次的结算时间 ,如果没有返回当天的0点)
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Date getStartTime() {
		return daybalanceDAO.getStartTime();
	}

	/**  
	 *  
	 * @Description：  获得某一时间段的日结详细信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterBalancedetail> getBalance(RegisterDaybalance daybalance,String registrarId) {
		//1.获得支付方式
		List<PayTypeVo> payTypeList = getPayType();
		//2.获得日结明细实体List
		List<RegisterBalancedetail> balanList = new ArrayList<RegisterBalancedetail>();
		AllPayTypeVo vo = daybalanceDAO.getInfoByTimeNow(daybalance.getStartTime(),daybalance.getEndTime(), registrarId);
		//现金
		RegisterBalancedetail ca = new RegisterBalancedetail();
		ca.setPayType("CA");
		ca.setRegFee(vo.getCashRegAmount());
		ca.setRegNum(vo.getCashRegNum());
		ca.setQuitFee(vo.getCashUnregAmount());
		ca.setQuitNum(vo.getCashUnregNum());
		ca.setSumNum(vo.getCashRegNum()-vo.getCashUnregNum());
		ca.setSumFee(vo.getCashRegAmount()-vo.getCashUnregAmount());
		balanList.add(ca);
		//转押金
		RegisterBalancedetail aj = new RegisterBalancedetail();
		aj.setPayType("AJ");
		aj.setRegFee(vo.getTrunsferRegAmount());
		aj.setRegNum(vo.getTrunsferRegNum());
		aj.setQuitFee(vo.getTrunsferUnregAmount());
		aj.setQuitNum(vo.getTrunsferUnregNum());
		aj.setSumFee(vo.getTrunsferRegAmount()-vo.getTrunsferUnregAmount());
		aj.setSumNum(vo.getTrunsferRegNum()-vo.getTrunsferUnregNum());
		balanList.add(aj);
		//信用卡
		RegisterBalancedetail cd = new RegisterBalancedetail();
		cd.setPayType("CD");
		cd.setRegFee(vo.getCreditRegAmount());
		cd.setRegNum(vo.getCreditRegNum());
		cd.setQuitFee(vo.getCreditUnregAmount());
		cd.setQuitNum(vo.getCreditUnregNum());
		cd.setSumFee(vo.getCreditRegAmount()-vo.getCreditUnregAmount());
		cd.setSumNum(vo.getCreditRegNum()-vo.getCreditUnregNum());
		balanList.add(cd);
		//支票
		RegisterBalancedetail ch = new RegisterBalancedetail();
		ch.setPayType("CH");
		ch.setRegFee(vo.getCheckRegAmount());
		ch.setRegNum(vo.getCheckRegNum());
		ch.setQuitFee(vo.getCheckUnregAmount());
		ch.setQuitNum(vo.getCheckUnregNum());
		ch.setSumFee(vo.getCheckRegAmount()-vo.getCheckUnregAmount());
		ch.setSumNum(vo.getCheckRegNum()-vo.getCheckUnregNum());
		balanList.add(ch);
		//银联卡
		RegisterBalancedetail db = new RegisterBalancedetail();
		db.setPayType("DB");
		db.setRegFee(vo.getUnionPayRegAmount());
		db.setRegNum(vo.getUnionPayRegNum());
		db.setQuitFee(vo.getUnionPayUnregAmount());
		db.setQuitNum(vo.getUnionPayUnregNum());
		db.setSumFee(vo.getUnionPayRegAmount()-vo.getUnionPayUnregAmount());
		db.setSumNum(vo.getUnionPayRegNum()-vo.getUnionPayUnregNum());
		balanList.add(db);
		//垫付款
		RegisterBalancedetail hp = new RegisterBalancedetail();
		hp.setPayType("HP");
		hp.setRegFee(vo.getAdvanceRegAmount());
		hp.setRegNum(vo.getAdvanceRegNum());
		hp.setQuitFee(vo.getAdvanceUnregAmount());
		hp.setQuitNum(vo.getAdvanceUnregNum());
		hp.setSumFee(vo.getAdvanceRegAmount()-vo.getAdvanceUnregAmount());
		hp.setSumNum(vo.getAdvanceRegNum()-vo.getAdvanceUnregNum());
		balanList.add(hp);
		//统筹
		RegisterBalancedetail pb = new RegisterBalancedetail();
		pb.setPayType("PB");
		pb.setRegFee(vo.getBalanceRegAmount());
		pb.setRegNum(vo.getBalanceRegNum());
		pb.setQuitFee(vo.getBalanceUnregAmount());
		pb.setQuitNum(vo.getBalanceUnregNum());
		pb.setSumFee(vo.getBalanceRegAmount()-vo.getBalanceUnregAmount());
		pb.setSumNum(vo.getBalanceRegNum()-vo.getBalanceUnregNum());
		balanList.add(pb);
		//汇票
		RegisterBalancedetail po = new RegisterBalancedetail();
		po.setPayType("PO");
		po.setRegFee(vo.getDraftRegAmount());
		po.setRegNum(vo.getDraftRegNum());
		po.setQuitFee(vo.getDraftUnregAmount());
		po.setQuitNum(vo.getDraftUnregNum());
		po.setSumFee(vo.getDraftRegAmount()-vo.getDraftUnregAmount());
		po.setSumNum(vo.getDraftRegNum()-vo.getDraftUnregNum());
		balanList.add(po);
		//保险账户
		RegisterBalancedetail ps = new RegisterBalancedetail();
		ps.setPayType("PS");
		ps.setRegFee(vo.getInsuranceRegAmount());
		ps.setRegNum(vo.getInsuranceRegNum());
		ps.setQuitFee(vo.getInsuranceUnregAmount());
		ps.setQuitNum(vo.getInsuranceUnregNum());
		ps.setSumFee(vo.getInsuranceRegAmount()-vo.getInsuranceUnregAmount());
		ps.setSumNum(vo.getInsuranceRegNum()-vo.getInsuranceUnregNum());
		balanList.add(ps);
		//体检患者优惠
		RegisterBalancedetail tj = new RegisterBalancedetail();
		tj.setPayType("TJ");
		tj.setRegFee(vo.getTjhzyhRegAmount());
		tj.setRegNum(vo.getTjhzyhRegNum());
		tj.setQuitFee(vo.getTjhzyhUnregAmount());
		tj.setQuitNum(vo.getTjhzyhUnregNum());
		tj.setSumFee(vo.getTjhzyhRegAmount()-vo.getTjhzyhUnregAmount());
		tj.setSumNum(vo.getTjhzyhRegNum()-vo.getTjhzyhUnregNum());
		balanList.add(tj);
		//院内账户
		RegisterBalancedetail ys = new RegisterBalancedetail();
		ys.setPayType("YS");
		ys.setRegFee(vo.getInacountRegAmount());
		ys.setRegNum(vo.getInacountRegNum());
		ys.setQuitFee(vo.getInacountUnregAmount());
		ys.setQuitNum(vo.getInacountUnregNum());
		ys.setSumFee(vo.getInacountRegAmount()-vo.getInacountUnregAmount());
		ys.setSumNum(vo.getInacountRegNum()-vo.getInacountUnregNum());
		balanList.add(ys);
		
		
		
		
		
		//3.根据支付方式和时间计算日结明细
//		if(payTypeList!=null&&payTypeList.size()>0){
//			//4.获得结算类型为自费的id
//			String Settlement = daybalanceDAO.getSettlementByName(STRINGZIFEI);
//			for(PayTypeVo payType : payTypeList){
//				//5.获得符合条件的所有挂号信息
//				List<Registration> infoList = registerInfoDAO.getInfoByTime(daybalance.getStartTime(),daybalance.getEndTime(),payType.getId(),registrarId);
//				//6.判断该支付类型下是否存在挂号信息  有责计算  无责初始化默认值
//				if(infoList!=null&&infoList.size()>0){//有挂号信息 进行计算
//					//7.获得日结明细实体
//					RegisterBalancedetail balan = new RegisterBalancedetail();
//					Double regFee = new Double(0.00);//总收入
//					Double quitFee = new Double(0.00);//总退费
//					Double sumFee = new Double(0.00);//实际收入
//					Integer quitNum = new Integer(0);//退号数量
//					Integer sumNum = new Integer(0);//实际挂号数量
//					Double ownFee = new Double(0);//个人承担的，自费的额度
//					for(Registration info : infoList){
//						//8.计算总收入  （收费标志为1）
//						if(info.getYnregchrg()==1){ 
//							regFee = regFee + info.getRegFee();
//						}
//						//9.计算总退费（收费标志为1,退费状态为0） 计算退号数量
//						if(info.getYnregchrg()==1&&info.getInState()==3){
//							quitFee = quitFee + info.getRegFee();//退费金额累计相加
//	 						quitNum = quitNum + 1;//退费数量累计相加
//						}
//						//10.计算个人承担的，自费的额度
//						if(Settlement!=null){
//							if(Settlement.equals(info.getPaykindCode())){
//								ownFee = ownFee + info.getRegFee();
//							}
//						}
//					}
//					//11.计算实际挂号数量  (挂号数量-退号数量)
//					sumNum = infoList.size() - quitNum;
//					//12.计算实际收入  (总收入-总退费)
//					sumFee = regFee - quitFee;
//					balan.setPayType(payType.getId());//支付类型
//					balan.setRegNum(infoList.size());//挂号数量
//					balan.setQuitNum(quitNum);//退号数量
//					balan.setSumNum(sumNum);//挂号数量-退号数量
//					balan.setRegFee(regFee);//挂号费用总额
//					balan.setQuitFee(quitFee);//挂号退费总额
//					balan.setSumFee(sumFee);//挂号费用总额-挂号退费总额
//					balan.setOwnFee(ownFee);//个人承担的，自费的额度
//					balanList.add(balan);
//				}else{//没有挂号信息 初始化默认值
//					RegisterBalancedetail balan = new RegisterBalancedetail();
//					balan.setPayType(payType.getId());//支付类型
//					balan.setRegNum(0);//挂号数量
//					balan.setQuitNum(0);//退号数量
//					balan.setSumNum(0);//挂号数量-退号数量
//					balan.setRegFee(0.00);//挂号费用总额
//					balan.setQuitFee(0.00);//挂号退费总额
//					balan.setSumFee(0.00);//挂号费用总额-挂号退费总额
//					balan.setOwnFee(0.00);//个人承担的，自费的额度
//					balanList.add(balan);
//				}
//			}
//		}
		return balanList;
	}
	
	/**  
	 *  
	 * @Description：  获得支付方式
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List<PayTypeVo> getPayTypeList(){
		List<PayTypeVo> payTypeList = new ArrayList<PayTypeVo>();
		//[{id:'1',value:'现金'},{id:'2',value:'银联卡'},{id:'3',value:'支票'},{id:'4',value:'院内账户'}]
		PayTypeVo vo1 = new PayTypeVo();
		vo1.setId("1");
		vo1.setName("现金");
		PayTypeVo vo2 = new PayTypeVo();
		vo2.setId("2");
		vo2.setName("银联卡");
		PayTypeVo vo3 = new PayTypeVo();
		vo3.setId("3");
		vo3.setName("支票");
		PayTypeVo vo4 = new PayTypeVo();
		vo4.setId("4");
		vo4.setName("院内账户");
		payTypeList.add(vo1);
		payTypeList.add(vo2);
		payTypeList.add(vo3);
		payTypeList.add(vo4);
		return payTypeList;
	}

	/**  
	 *  
	 * @Description：  获得支付方式
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：zhuxiaolu
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List<PayTypeVo> getPayType(){
		List<BusinessDictionary> list = codeInInterDAO.getDictionary("payway");
		List<PayTypeVo> payTypeList = new ArrayList<PayTypeVo>();
		PayTypeVo[] vo = new PayTypeVo[list.size()];
		for(int i=0;i<list.size();i++){
			vo[i]= new PayTypeVo();
			vo[i].setId(list.get(i).getEncode());
			vo[i].setName(list.get(i).getName());
			payTypeList.add(vo[i]);
		}
		return payTypeList;
	}

	/**  
	 *  
	 * @Description：  获得某一时间段的日结信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterDaybalance getDaybalance(RegisterDaybalance daybalance,String registrarId) {
		//1.获得日结明细实体
		RegisterDaybalance registerDaybalance = new RegisterDaybalance();
		//2.计算日结明细
		List<RegistrationNow> infoList = registerInfoDAO.getInfoByTime(daybalance.getStartTime(),daybalance.getEndTime(),null,registrarId);
		Double income = new Double(0.00);//总收入
		Double refund = new Double(0.00);//总退费
		Double actualIncome = new Double(0.00);//实际收入
		Integer inNum = new Integer(0);//总挂号人数
		Integer outNum = new Integer(0);//退号人数
		Integer actualNum = new Integer(0);//实际看诊人数
		if(infoList!=null&&infoList.size()>0){
			for(RegistrationNow info : infoList){
				//3.计算总收入  （收费标志为1）
				if(info.getYnregchrg()==1){ 
					income = income + info.getRegFee();
				}
				//4.计算总退费（收费标志为1,退费状态为0） 
				if(info.getYnregchrg()==1&&info.getInState()==3){
					refund = refund + info.getRegFee();
					outNum = outNum + 1;
				}
			} 
			
			//5.计算实际收入  (总收入-总退费)
			actualIncome = income - refund;
			//6.计算总挂号人数
			inNum = infoList.size();
			//7.计算实际看诊人数
			actualNum = inNum - outNum;
		}
		registerDaybalance.setIncome(income);
		registerDaybalance.setRefund(refund);
		registerDaybalance.setActualIncome(actualIncome);
		registerDaybalance.setInNum(inNum);
		registerDaybalance.setOutNum(outNum);
		registerDaybalance.setActualNum(actualNum);
		registerDaybalance.setStartTime(daybalance.getStartTime());
		registerDaybalance.setEndTime(daybalance.getEndTime());
		return registerDaybalance;
	}

	/**  
	 *  
	 * @Description：  保存挂号日结档信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> saveDaybalance(RegisterDaybalance daybalance) {
		Map<String,Object> map=new HashMap<String,Object>();
		Date startTime = daybalanceDAO.getStartTime();//获得开始时间(上一次的结算时间 ,如果没有返回当天的0点)
		Date endTime = new Date();//获得结束时间为当前时间
		//如果当前要结账的开始时间小于应该结账的开始　　则结账数据有可能重复　不予许结账
		if(DateUtils.compareDate(DateUtils.parseDateY_M_D_H_M_S(daybalance.getStartTimeStr()),startTime)==-1){
			map.put("resMsg", "error");
			map.put("resCode", "结账数据重复,请重新选择！");
			return map;
		}
		//如果当前要结账的开始时间大于应该结账的开始　　则结账数据有可能漏结　不予许结账
		if(DateUtils.compareDate(DateUtils.parseDateY_M_D_H_M_S(daybalance.getStartTimeStr()),startTime)==1){
			map.put("resMsg", "error");
			map.put("resCode", "开始结账时间前仍有未结账信息,请重新选择！");
			return map;
		}
		//如果当前结账结束时间大于当前时间　则结账数据可能漏结　不予许结账
		if(DateUtils.compareDate(DateUtils.parseDateY_M_D_H_M_S(daybalance.getEndTimeStr()),endTime)==1){
			map.put("resMsg", "error");
			map.put("resCode", "不能提前结账,请重新选择！");
			return map;
		}
		String balanceNo = daybalanceDAO.getNextBalanceNo();
		daybalance.setBalanceNo(balanceNo);//日结序号
		daybalance.setStartTime(DateUtils.parseDateY_M_D_H_M_S(daybalance.getStartTimeStr()));
		daybalance.setEndTime(DateUtils.parseDateY_M_D_H_M_S(daybalance.getEndTimeStr()));
		daybalance.setCheckFlag(1);//财务审核 1未审核/0已审核
		daybalance.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		daybalance.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		daybalance.setCreateTime(new Date());//创建时间
		daybalanceDAO.save(daybalance);
		//1.获得支付方式
		List<PayTypeVo> payTypeList = getPayTypeList();
		//2.获得日结明细实体List
		List<RegisterBalancedetail> balanList = new ArrayList<RegisterBalancedetail>();
		//3.根据支付方式和时间计算日结明细
		if(payTypeList!=null&&payTypeList.size()>0){
//			//4.获得结算类型为自费的id
//			String Settlement = daybalanceDAO.getSettlementByName(STRINGZIFEI);
//			for(PayTypeVo payType : payTypeList){
//				//5.获得符合条件的所有挂号信息
//				List<RegistrationNow> infoList = registerInfoDAO.getInfoByTime(daybalance.getStartTime(),daybalance.getEndTime(),payType.getId(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
//				//6.判断该支付类型下是否存在挂号信息  有责计算  无责初始化默认值
//				if(infoList!=null&&infoList.size()>0){//有挂号信息 进行计算
//					//7.获得日结明细实体
//					RegisterBalancedetail balan = new RegisterBalancedetail();
//					Double regFee = new Double(0.00);//总收入
//					Double quitFee = new Double(0.00);//总退费
//					Double sumFee = new Double(0.00);//实际收入
//					Integer quitNum = new Integer(0);//退号数量
//					Integer sumNum = new Integer(0);//实际挂号数量
//					Double ownFee = new Double(0);//个人承担的，自费的额度
//					for(RegistrationNow info : infoList){
//						//8.计算总收入  （收费标志为1）
//						if(info.getYnregchrg()==1){ 
//							regFee = regFee + info.getRegFee();
//						}
//						//9.计算总退费（收费标志为1,退费状态为0） 计算退号数量
//						if(info.getYnregchrg()==1&&info.getInState()==3){
//							quitFee = quitFee + info.getRegFee();//退费金额累计相加
//	 						quitNum = quitNum + 1;//退费数量累计相加
//						}
//						//10.计算个人承担的，自费的额度
//						if(Settlement!=null){
//							if(Settlement.equals(info.getPaykindCode())){
//								ownFee = ownFee + info.getRegFee();
//							}
//						}
//					}
//					
//					//11.计算实际挂号数量  (挂号数量-退号数量)
//					sumNum = infoList.size() - quitNum;
//					//12.计算实际收入  (总收入-总退费)
//					sumFee = regFee - quitFee;
//					balan.setPayType(payType.getId());//支付类型
//					balan.setRegNum(infoList.size());//挂号数量
//					balan.setQuitNum(quitNum);//退号数量
//					balan.setSumNum(sumNum);//挂号数量-退号数量
//					balan.setRegFee(regFee);//挂号费用总额
//					balan.setQuitFee(quitFee);//挂号退费总额
//					balan.setSumFee(sumFee);//挂号费用总额-挂号退费总额
//					balan.setOwnFee(ownFee);//个人承担的，自费的额度
//					balan.setBalance(daybalance);
//					balan.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
//					balan.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建部门
//					balan.setCreateTime(new Date());//创建时间
//					balan.setDel_flg(0);//删除标志
//					balan.setStop_flg(0);//停用标志
//					balanList.add(balan);
//				}else{//没有挂号信息 初始化默认值
//					RegisterBalancedetail balan = new RegisterBalancedetail();
//					balan.setPayType(payType.getId());//支付类型
//					balan.setRegNum(0);//挂号数量
//					balan.setQuitNum(0);//退号数量
//					balan.setSumNum(0);//挂号数量-退号数量
//					balan.setRegFee(0.00);//挂号费用总额
//					balan.setQuitFee(0.00);//挂号退费总额
//					balan.setSumFee(0.00);//挂号费用总额-挂号退费总额
//					balan.setOwnFee(0.00);//个人承担的，自费的额度
//					balan.setBalance(daybalance);
//					balan.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
//					balan.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建部门
//					balan.setCreateTime(new Date());//创建时间
//					balan.setDel_flg(0);//删除标志
//					balan.setStop_flg(0);//停用标志
//					balanList.add(balan);
//				}
//			}
			balanList = this.getBalance(daybalance, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			for (RegisterBalancedetail rb : balanList) {
				rb.setBalance(daybalance);
				rb.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				rb.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				rb.setCreateTime(new Date());
				rb.setOwnFee(0.00);
			}
		}
		int regDaybalance = daybalanceDAO.updateRegDaybalance(balanceNo,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(), startTime, endTime);
		balancedetailDAO.saveOrUpdateList(balanList);
		OperationUtils.getInstance().conserve(null,"挂号员日结","INSERT INTO","T_REGISTER_DAYBALANCE",OperationUtils.LOGACTIONCHECKOUT);
		map.put("resMsg", "success");
		map.put("resCode", "结账成功!");
		map.put("rid", daybalance.getId());
		return map;
	}

	@Override
	public RegisterDaybalance getDaybalanceNow(List<RegisterBalancedetail> balancedetailList) {
		Double income = new Double(0.00);//总收入
		Double refund = new Double(0.00);//总退费
		Double actualIncome = new Double(0.00);//实际收入
		Integer inNum = new Integer(0);//总挂号人数
		Integer outNum = new Integer(0);//退号人数
		Integer actualNum = new Integer(0);//实际看诊人数
		if(balancedetailList!=null||balancedetailList.size()>0){
			for (RegisterBalancedetail red : balancedetailList) {
				income += red.getRegFee();
				refund += red.getQuitFee();
				inNum += red.getRegNum();
				outNum += red.getQuitNum();
			}
		}
		RegisterDaybalance rd = new RegisterDaybalance();
		rd.setActualIncome(income+refund);
		rd.setActualNum(inNum-outNum);
		rd.setIncome(income);
		rd.setInNum(inNum);
		rd.setOutNum(outNum);
		rd.setRefund(refund);
		return rd;
	}

}
