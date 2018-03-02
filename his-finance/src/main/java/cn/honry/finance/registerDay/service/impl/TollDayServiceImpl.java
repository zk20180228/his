package cn.honry.finance.registerDay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.bean.model.OutpatientDaybalanceDetial;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.finance.daybalance.dao.DaybalanceDAO;
import cn.honry.finance.registerDay.dao.TollDayDao;
import cn.honry.finance.registerDay.service.TollDayService;
import cn.honry.finance.registerDay.vo.DayBalanceVO;
import cn.honry.finance.registerDay.vo.InfoVo;
import cn.honry.finance.registerDay.vo.ViewVo;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("tollDayService")
@Transactional
@SuppressWarnings({ "all" })
public class TollDayServiceImpl implements TollDayService {
	
	//注入本类Dao
	@Autowired
	@Qualifier(value = "tollDayDao")
	private TollDayDao tollDayDao;
	@Autowired
	@Qualifier(value = "daybalanceDAO")
	private DaybalanceDAO daybalanceDAO;
	@Override
	public OutpatientDaybalance get(String arg0) {
		return tollDayDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OutpatientDaybalance arg0) {
		
	}

	@Override
	public Date getBeginDate() {
		//用户id
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		return tollDayDao.getBeginDate(userid);
	}

	@Override
	public List<ViewVo> queryInvoiceinfo(OutpatientDaybalance dayBalance) {
		String userid = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		List<InfoVo> voList = tollDayDao.queryInvoiceinfo(userid,dayBalance.getBeginDate(),dayBalance.getEndDate());
		Map<Integer,List<InfoVo>> map = new HashMap<Integer, List<InfoVo>>();
		//进行正反交易分组
		if(voList!=null){
			List<InfoVo> list = null;
			for(InfoVo vo : voList){
				if(map.get(vo.getTransType())==null){
					list = new ArrayList<InfoVo>();
					list.add(vo);
					map.put(vo.getTransType(),list);
				}else{
					list = map.get(vo.getTransType());
					list.add(vo);
					map.put(vo.getTransType(),list);
				}
			}
		}
		List<ViewVo> retList = new ArrayList<ViewVo>();
		//对发票号连续的记录进行分组
		if(map.size()>0){
			for(Map.Entry<Integer,List<InfoVo>> m : map.entrySet()){
				ViewVo vo = null;
				if(m.getKey()!=null){
					String invoice = null;
					for(InfoVo infoVo : m.getValue()){
						if(invoice==null){
							invoice = infoVo.getInvoice();
							vo = new ViewVo();
							vo.setInvoices(infoVo.getInvoice());
							vo.setNum(1);
							vo.setIncome(infoVo.getRealCost());
							vo.setAccounting(infoVo.getPubCost());
							vo.setTotal(infoVo.getTotalNum());
							vo.setExtFlag(1);
							if(infoVo.getCancel()==1){
								vo.setRemarks("有效");
							}else if(infoVo.getCancel()==0 && infoVo.getTransType()==2){
								vo.setRemarks("退票");
							}else if((infoVo.getCancel()==2 || infoVo.getCancel()==3) && infoVo.getTransType()==2){
								vo.setRemarks("作废");
							}
							retList.add(vo);
						}else{
							Integer length = infoVo.getInvoice().length()-1;
							String head = infoVo.getInvoice().substring(0,1);
							String tail = infoVo.getInvoice().substring(1,infoVo.getInvoice().length());
							Integer tailnow = Integer.parseInt(tail)-1;
							String tailMosaic = "";
							for(int i=0;i<length-tailnow.toString().length();i++){
								tailMosaic += "0";
							}
							tailMosaic += tailnow;
							String body = head + tailMosaic;
							if(invoice.equals(body)){
								invoice = infoVo.getInvoice();
								if(vo.getInvoices().contains("~")){
									String[] invoiceArr = vo.getInvoices().split("~");
									vo.setInvoices(invoiceArr[0]+"~"+infoVo.getInvoice());
								}else{
									vo.setInvoices(vo.getInvoices()+"~"+infoVo.getInvoice());
								}
								vo.setNum(vo.getNum()+1);
								vo.setIncome(vo.getIncome() + infoVo.getRealCost());
								vo.setAccounting(vo.getAccounting() + infoVo.getPubCost());
								vo.setTotal(vo.getTotal() + infoVo.getTotalNum());
								vo.setExtFlag(vo.getExtFlag()+1);
								if(infoVo.getCancel()==1){
									vo.setRemarks("有效");
								}else if(infoVo.getCancel()==0 && infoVo.getTransType()==2){
									vo.setRemarks("退票");
								}else if((infoVo.getCancel()==2 || infoVo.getCancel()==3) && infoVo.getTransType()==2){
									vo.setRemarks("作废");
								}
							}else{
								invoice = infoVo.getInvoice();
								vo = new ViewVo();
								vo.setInvoices(infoVo.getInvoice());
								vo.setNum(1);
								vo.setIncome(infoVo.getRealCost());
								vo.setAccounting(infoVo.getPubCost());
								vo.setTotal(infoVo.getTotalNum());
								vo.setExtFlag(1);
								if(infoVo.getCancel()==1){
									vo.setRemarks("有效");
								}else if(infoVo.getCancel()==0 && infoVo.getTransType()==2){
									vo.setRemarks("退票");
								}else if((infoVo.getCancel()==2 || infoVo.getCancel()==3) && infoVo.getTransType()==2){
									vo.setRemarks("作废");
								}
								retList.add(vo);
							}
						}
					}
				}
			}
		}
		ViewVo vo = new ViewVo();
		if(retList.size()>0){
			Integer num = 0;
			Double income = 0d;
			Double accounting = 0d;
			Double total = 0d;
			Integer extFlag = 0;
			for(ViewVo viewVo : retList){
				num += viewVo.getNum();
				income += viewVo.getIncome();
				accounting += viewVo.getAccounting();
				total += viewVo.getTotal();
				extFlag += viewVo.getExtFlag();
			}
			BigDecimal inc = new BigDecimal(income);
			BigDecimal acc = new BigDecimal(accounting);
			BigDecimal tot = new BigDecimal(total);
			
			vo.setInvoices("合计：");
			vo.setNum(num);
			vo.setIncome(inc.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
			vo.setAccounting(acc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			vo.setTotal(tot.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			vo.setExtFlag(extFlag);
			retList.add(vo);
		}else{
			vo.setInvoices("合计：");
			vo.setNum(0);
			vo.setIncome(0d);
			vo.setAccounting(0d);
			vo.setTotal(0d);
			vo.setExtFlag(0);
			retList.add(vo);
		}
		return retList;
	}

	/***
	 * 保存日结信息
	 * @author aizhonghua
	 * @createDate ：2016年6月15日
	 * @return Map<String,Object>
	 * @version 1.0
	 */
	@Override
	public Map<String, Object> saveDaybalance(OutpatientDaybalance dayBalance) {
		Map<String,Object> retMap=new HashMap<String,Object>();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		Date startTime = tollDayDao.getBeginDate(user.getAccount());
		Date endTime = new Date();//获得结束时间为当前时间
		//如果当前要结账的开始时间小于应该结账的开始　　则结账数据有可能重复　不予许结账
		if(DateUtils.compareDate(dayBalance.getBeginDate(),startTime)==-1){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "结账数据重复,请重新选择！");
			return retMap;
		}

		//如果当前要结账的开始时间大于应该结账的开始　　则结账数据有可能漏结　不予许结账
		if(DateUtils.compareDate(dayBalance.getBeginDate(),startTime)==1){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "开始结账时间前仍有未结账信息,请重新选择！");
			return retMap;
		}
		//如果当前结账结束时间大于当前时间　则结账数据可能漏结　不予许结账
		if(DateUtils.compareDate(dayBalance.getEndDate(),endTime)==1){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "不能提前结账,请重新选择！");
			return retMap;
		}
		List<InfoVo> voList = tollDayDao.queryInvoiceinfo(user.getAccount(),dayBalance.getBeginDate(),dayBalance.getEndDate());
		Map<Integer,List<InfoVo>> map = new HashMap<Integer, List<InfoVo>>();
		//进行正反交易分组
		if(voList!=null){
			List<InfoVo> list = null;
			for(InfoVo vo : voList){
				if(map.get(vo.getTransType())==null){
					list = new ArrayList<InfoVo>();
					list.add(vo);
					map.put(vo.getTransType(),list);
				}else{
					list = map.get(vo.getTransType());
					list.add(vo);
					map.put(vo.getTransType(),list);
				}
			}
		}
		List<ViewVo> retList = new ArrayList<ViewVo>();
		//对发票号连续的记录进行分组
		if(map.size()>0){
			for(Map.Entry<Integer,List<InfoVo>> m : map.entrySet()){
				ViewVo vo = null;
				if(m.getKey()!=null){
					String invoice = null;
					for(InfoVo infoVo : m.getValue()){
						if(invoice==null){
							invoice = infoVo.getInvoice();
							vo = new ViewVo();
							vo.setInvoices(infoVo.getInvoice());
							vo.setNum(1);
							vo.setIncome(infoVo.getRealCost());
							vo.setAccounting(infoVo.getPubCost());
							vo.setTotal(infoVo.getTotalNum());
							vo.setExtFlag(1);
							if(infoVo.getCancel()==1){
								vo.setRemarks("有效");
							}else if(infoVo.getCancel()==0 && infoVo.getTransType()==2){
								vo.setRemarks("退票");
							}else if((infoVo.getCancel()==2 || infoVo.getCancel()==3) && infoVo.getTransType()==2){
								vo.setRemarks("作废");
							}
							retList.add(vo);
						}else{
							Integer length = infoVo.getInvoice().length()-1;
							String head = infoVo.getInvoice().substring(0,1);
							String tail = infoVo.getInvoice().substring(1,infoVo.getInvoice().length());
							Integer tailnow = Integer.parseInt(tail)-1;
							String tailMosaic = "";
							for(int i=0;i<length-tailnow.toString().length();i++){
								tailMosaic += "0";
							}
							tailMosaic += tailnow;
							String body = head + tailMosaic;
							if(invoice.equals(body)){
								invoice = infoVo.getInvoice();
								if(vo.getInvoices().contains("~")){
									String[] invoiceArr = vo.getInvoices().split("~");
									vo.setInvoices(invoiceArr[0]+"~"+infoVo.getInvoice());
								}else{
									vo.setInvoices(vo.getInvoices()+"~"+infoVo.getInvoice());
								}
								vo.setNum(vo.getNum()+1);
								vo.setIncome(vo.getIncome() + infoVo.getRealCost());
								vo.setAccounting(vo.getAccounting() + infoVo.getPubCost());
								vo.setTotal(vo.getTotal() + infoVo.getTotalNum());
								vo.setExtFlag(vo.getExtFlag()+1);
								if(infoVo.getCancel()==1){
									vo.setRemarks("有效");
								}else if(infoVo.getCancel()==0 && infoVo.getTransType()==2){
									vo.setRemarks("退票");
								}else if((infoVo.getCancel()==2 || infoVo.getCancel()==3) && infoVo.getTransType()==2){
									vo.setRemarks("作废");
								}
							}else{
								invoice = infoVo.getInvoice();
								vo = new ViewVo();
								vo.setInvoices(infoVo.getInvoice());
								vo.setNum(1);
								vo.setIncome(infoVo.getRealCost());
								vo.setAccounting(infoVo.getPubCost());
								vo.setTotal(infoVo.getTotalNum());
								vo.setExtFlag(1);
								if(infoVo.getCancel()==1){
									vo.setRemarks("有效");
								}else if(infoVo.getCancel()==0 && infoVo.getTransType()==2){
									vo.setRemarks("退票");
								}else if((infoVo.getCancel()==2 || infoVo.getCancel()==3) && infoVo.getTransType()==2){
									vo.setRemarks("作废");
								}
								retList.add(vo);
							}
						}
					}
				}
			}
		}
		if(retList.size()==0){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "暂无日结信息！");
			return retMap;
		}else{
			String seqByNameorNum = tollDayDao.getSeqByNameorNum("SEQ_OUTPATIENT_DAYBALANCE", 32);
			List<OutpatientDaybalance> saveList = new ArrayList<OutpatientDaybalance>();
			OutpatientDaybalance bb = null;
			for(ViewVo vo : retList){
				bb = new OutpatientDaybalance();
				bb.setBlanceNo(seqByNameorNum);//日结序号 
				bb.setBeginDate(dayBalance.getBeginDate());//开始时间 
				bb.setEndDate(dayBalance.getEndDate());//结束时间 
				bb.setTotCost(vo.getTotal());//总收入
				bb.setOperCode(user.getAccount());//收款员代码
				bb.setOperName(user.getName());//收款员姓名
				bb.setOperDate(new Date());//操作时间
				/**更改表结构，该部分为表以前的字段**/
//				bb.setBack1(null);//备注1(作废发票张数)
//				bb.setBack2(null);//备注2(退费发票张数)
//				bb.setBack3(new Double(vo.getNum()));//备注3(发票张数)
//				bb.setCheckFlag(1);//财务审核，1未审核/2已审核
//				bb.setCheckOpcd(null);//审核人
//				bb.setCheckDate(null);//审核时间
//				bb.setBalanceItem(0);//日结项目：0-正常；1-退费；2-重打；3-注销；4-项目明细；5-发票信息；6-金额信息 
//				bb.setInvoiceNo(vo.getInvoices());//日结项目对应的发票号或发票号区间范围；a开头为日结项目编码
//				bb.setOwnCost(vo.getIncome());//实收金额
//				bb.setPubPayNumber(vo.getExtFlag());//记账单数量
//				bb.setExtentField1(null);//日结项目名称
//				bb.setPubPayCost(vo.getAccounting());//记账金额
//				bb.setCdnumber(null);//刷卡数量 
//				bb.setCd_cost(null);//刷卡金额
//				bb.setCa_cost(null);//现金金额
//				bb.setDb_cost(null);//支票金额
//				bb.setExtentField2(null);//票据号范围
//				bb.setExtentField3(null);//发票号范围
//				bb.setExtentField4(null);//作废发票号明细
//				bb.setExtentField5(null);//退费发票号明细
//				bb.setDeptCode(dept.getDeptCode());//操作员科室
//				bb.setSortId(null);//排序号，显示在表格式中的位置
				bb.setCreateUser(user.getAccount());
				bb.setCreateDept(dept.getDeptCode());
				bb.setCreateTime(new Date());
				bb.setUpdateUser(user.getAccount());
				bb.setUpdateTime(new Date());
				saveList.add(bb);
			}
			if(saveList.size()>0){
				//更新日结处方明细的日结信息
				int i = tollDayDao.updateOutFeedetail(user.getAccount(), dayBalance.getBeginDate(), dayBalance.getEndDate());
				int invoicedetail = tollDayDao.updateInvoicedetail(user.getName(),seqByNameorNum, user.getAccount(), dayBalance.getBeginDate(), dayBalance.getEndDate());
				int info = tollDayDao.updateInvoiceInfo(user.getName(),seqByNameorNum, user.getAccount(), dayBalance.getBeginDate(), dayBalance.getEndDate());
				int paymodeNow = tollDayDao.updatePaymodeNow(user.getName(),seqByNameorNum, user.getAccount(), dayBalance.getBeginDate(), dayBalance.getEndDate());
				tollDayDao.saveOrUpdateList(saveList);
				OperationUtils.getInstance().conserve(null,"收费员日结","INSERT INTO","T_OUTPATIENT_DAYBALANCE",OperationUtils.LOGACTIONCHECKOUT);
				OperationUtils.getInstance().conserve(null,"更新收费员日结信息","UPDATE","T_OUTPATIENT_FEEDETAIL_NOW","日结时更新日结信息");
				OperationUtils.getInstance().conserve(null,"更新日结信息","UPDATE","T_FINANCE_INVOICEINFO_NOW","日结时更新日结信息");
				OperationUtils.getInstance().conserve(null,"更新日结信息","UPDATE","T_FINANCE_INVOICEDETAIL_NOW","日结时更新日结信息");
				retMap.put("resMsg", "success");
				retMap.put("resCode", "结账成功！");
				return retMap;
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode", "暂无日结信息！");
				return retMap;
			}
		}
	}

	@Override
	public List<DayBalanceVO> getBalance(String code, String startDate,String endDate) {
		return tollDayDao.getBalance(code, startDate, endDate);
	}

	@Override
	public Map<String, Object> saveDaybalanceNew(String beginDate,String endDate) {
		Map<String,Object> map = new HashMap<String, Object>();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		try{
			Date bdate = DateUtils.parseDateY_M_D_H_M_S(beginDate);
			Date edate = DateUtils.parseDateY_M_D_H_M_S(endDate);
			String blanceNo = tollDayDao.getSeqByNameorNum("SEQ_OUTPATIENT_DAYBALANCE", 10);
			String userCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			List<DayBalanceVO> list = tollDayDao.getBalance(userCode, beginDate, endDate);
			OutpatientDaybalance outDayBlance = tollDayDao.getOutDayBlance(userCode, beginDate, endDate);
			OutpatientDaybalance outdb = new OutpatientDaybalance();
			BeanUtils.copyProperties(outDayBlance, outdb);
			outdb.setId(null);
			outdb.setSjTotcaCost(outDayBlance.getCaCost()+outDayBlance.getYsPrepayca()-outDayBlance.getYsBalanceCost());
			outdb.setSjTotdbCost(outDayBlance.getCdCost()+outDayBlance.getYsPrepaydb());
			outdb.setBackUndrug(outDayBlance.getBackUndrug()+outDayBlance.getBackysundrug());
			outdb.setBackFee(outDayBlance.getBackFee()+outDayBlance.getBackDrug()+outDayBlance.getBackUndrug());
			outdb.setBackDrug(outDayBlance.getBackDrug()+outDayBlance.getBackysdrug());
			outdb.setBlanceNo(blanceNo);
			outdb.setBeginDate(bdate);
			outdb.setOper(userCode);
			outdb.setEndDate(edate);
			outdb.setOperCode(userCode);
			outdb.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			outdb.setOperDate(new Date());
			outdb.setCreateDept(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());
			outdb.setCreateTime(new Date());
			outdb.setCreateUser(user.getAccount());
			outdb.setUpdateTime(new Date());
			outdb.setUpdateUser(user.getAccount());
			tollDayDao.save(outdb);
			//遍历List<DayBalanceVO>添加日结明细
			for (DayBalanceVO vo : list) {
				OutpatientDaybalanceDetial detial = new OutpatientDaybalanceDetial();
				detial.setBeginDate(bdate);
				detial.setBlanceNo(blanceNo);
				detial.setOperCode(userCode);
				detial.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
				detial.setOperDate(new Date());
				detial.setDetailType(vo.getPayKindCode());
				detial.setDetailName(vo.getPayKindName());
				detial.setTotCost(vo.getTotalCost());
				detial.setEx1(vo.getSsyPuvCost());
				detial.setEx2(vo.getSsyOwnCost());
				detial.setEx3(vo.getSsyPayCost());
				detial.setPri1Own(vo.getSybOwnCost());
				detial.setPri1PayPub(vo.getSybPubCost());
				detial.setPri2Own(vo.getSmxbOwnCost());
				detial.setPri2Pay(vo.getSmxbPayCost());
				detial.setPri2Pub(vo.getSmxbPubCost());
				detial.setPri3Own(vo.getSlxOwnCost());
				detial.setPri3PayPub(vo.getSlxPayCost());
				detial.setCityOwn(vo.getShiybOwnCost());
				detial.setCityPubPay(vo.getShiybPubCost());
				detial.setOwnPay(vo.getOwnCost());
				detial.setNcsiOwn(vo.getNhOwnCost());
				detial.setNcsiPay(vo.getNhPayCost());
				detial.setNcsiPub(vo.getNhPubCost());
				detial.setNysiOwn(vo.getNyhgOwnCost());
				detial.setNysiPay(vo.getNyhgPayCost());
				detial.setNysiPub(vo.getNyhgPubCost());
				detial.setSbjjbcOwn(vo.getSbjjbxOwnCost());
				detial.setSbjjbcPay(vo.getSbjjbxPayCost());
				detial.setSbjjbcPub(vo.getSbjjbxPubCost());
				detial.setSjxnhOwn(vo.getSjxnhptOwnCost());
				detial.setSjxnhPay(vo.getSjxnhptPayCost());
				detial.setSjxnhPub(vo.getSjxnhptPubCost());
				detial.setYdjyOwn(vo.getYdjysdOwnCost());
				detial.setYdjyPay(vo.getYdjysdPayCost());
				detial.setYdjyPub(vo.getYdjysdPubCost());
				detial.setRailwayOwn(vo.getStlylbxOwnCost());
				detial.setRailwayPay(vo.getStlylbxPayCost());
				detial.setRailwayPub(vo.getStlylbxPubCost());
				detial.setCreateDept(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());
				detial.setCreateTime(new Date());
				detial.setCreateUser(userCode);
				detial.setCreateDept(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());
				detial.setUpdateTime(new Date());
				detial.setUpdateUser(userCode);
				detial.setId(null);
				tollDayDao.save(detial);
			}
			
			
			int i = tollDayDao.updateOutFeedetail(user.getAccount(), DateUtils.parseDateY_M_D_H_M_S(beginDate), DateUtils.parseDateY_M_D_H_M_S(endDate));
			int invoicedetail = tollDayDao.updateInvoicedetail(user.getName(),blanceNo, user.getAccount(), DateUtils.parseDateY_M_D_H_M_S(beginDate), DateUtils.parseDateY_M_D_H_M_S(endDate));
			int info = tollDayDao.updateInvoiceInfo(user.getName(),blanceNo, user.getAccount(), DateUtils.parseDateY_M_D_H_M_S(beginDate), DateUtils.parseDateY_M_D_H_M_S(endDate));
			int paymodeNow = tollDayDao.updatePaymodeNow(user.getName(),blanceNo, user.getAccount(), DateUtils.parseDateY_M_D_H_M_S(beginDate), DateUtils.parseDateY_M_D_H_M_S(endDate));
			OperationUtils.getInstance().conserve(null,"收费员日结","INSERT INTO","T_OUTPATIENT_DAYBALANCE",OperationUtils.LOGACTIONCHECKOUT);
			OperationUtils.getInstance().conserve(null,"更新收费员日结信息","UPDATE","T_OUTPATIENT_FEEDETAIL_NOW","日结时更新日结信息");
			OperationUtils.getInstance().conserve(null,"更新日结信息","UPDATE","T_FINANCE_INVOICEINFO_NOW","日结时更新日结信息");
			OperationUtils.getInstance().conserve(null,"更新日结信息","UPDATE","T_FINANCE_INVOICEDETAIL_NOW","日结时更新日结信息");
			map.put("resCode", "success");
			map.put("resMsg", "日结成功！");
			return map;
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "日结失败！");
			e.printStackTrace();
			return map;
		}
	}
	
}
