package cn.honry.finance.collectDaily.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientScDreport;
import cn.honry.base.bean.model.InpatientScDreportdetail;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.finance.collectDaily.dao.CollectDailyDao;
import cn.honry.finance.collectDaily.service.CollectDailyService;
import cn.honry.finance.collectDaily.vo.ColDaiVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;
/**   
*  
* @className：CollectDailyServiceimpl
* @description：结算员日结service实现类
* @author：tcj
* @createDate：2016-04-12  
* @modifyRmk：  
* @version 1.0
 */
@Service("collectDailyService")
@Transactional
@SuppressWarnings({"all"})
public class CollectDailyServiceImpl implements CollectDailyService {
	/**
	 * 注入collectDailyDao
	 */
	@Autowired
	@Qualifier(value = "collectDailyDao")
	private CollectDailyDao collectDailyDao;
	public void setCollectDailyDao(CollectDailyDao collectDailyDao) {
		this.collectDailyDao = collectDailyDao;
	}

	@Override
	public ColDaiVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(ColDaiVo arg0) {
		
	}

	@Override
	public List<InpatientScDreport> queryCollectMaxTime() throws Exception {
		return collectDailyDao.queryCollectMaxTime();
	}
	
	@Override
	public List<ColDaiVo> querydetalDaily(String startTime,
			String endTime) throws Exception {
		
		return collectDailyDao.querydetalDaily(startTime,endTime);
	}

	@Override
	public ColDaiVo queryTableDaily(String startTime, String endTime) throws Exception {
		return collectDailyDao.queryTableDaily(startTime,endTime);
	}

	@Override
	public void saveDatagridDaily(String date,String startTime,String endTime,Integer sqc) throws Exception {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		Date date1 = sdf.parse(startTime);  
		Date date2 = sdf.parse(endTime);
		List<ColDaiVo> cdv=JSONUtils.fromJson(date,  new TypeToken<List<ColDaiVo>>(){}, "yyyy-MM-dd hh:mm:ss");
		if(cdv!=null&&cdv.size()>0){
			for(int i=0;i<cdv.size();i++){
				InpatientScDreportdetail isd=new InpatientScDreportdetail();
				isd.setStaticNo(sqc);//统计序号
				isd.setBeginDate(date1);//开始时间
				isd.setEndDate(date2);//结束时间
				isd.setOperCode(user.getAccount());//操作人
				isd.setStatCode(cdv.get(i).getCode());//统计大类
				isd.setTotCost(cdv.get(i).getCost());//费用金额
				isd.setOwnCost(cdv.get(i).getOwnCost());//自费金额
				isd.setPayCost(cdv.get(i).getPayCost());//自付金额
				isd.setPubCost(cdv.get(i).getPubCost());//公费金额
				isd.setCreateTime(DateUtils.getCurrentTime());//创建时间
				isd.setCreateUser(user.getAccount());//创建人
				isd.setCreateDept(dept.getDeptCode());//创建科室
				collectDailyDao.save(isd);
			}
		}
	}
	@Override
	public Integer saveFromDaily(ColDaiVo cdv,String date,String startTime,String endTime) throws Exception {
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		
			//通过序列生成的统计序号
			Integer sqc=Integer.valueOf(collectDailyDao.getSequece("SEQ_INPATIENT_SC_DREPORT"));
			//保存日结明细（子表）
			if(StringUtils.isNotBlank(date)){
				saveDatagridDaily(date,startTime,endTime,sqc);
			}
			//更新预交金、住院结算头表日结信息
			List<InpatientInPrepayNow>  inprel=collectDailyDao.queryInpreList(startTime,endTime,user.getAccount());
			for(InpatientInPrepayNow inpre: inprel){
				inpre.setDaybalanceOpcd(user.getAccount());//日结人
				inpre.setDaybalanceDate(DateUtils.getCurrentTime());//日结时间
				inpre.setDaybalanceNo(sqc.toString());//日结标识号  日结统计序号
				inpre.setUpdateUser(user.getAccount());
				inpre.setUpdateTime(new Date());
				collectDailyDao.save(inpre);
			}
			List<InpatientBalanceHeadNow>  inbhl=collectDailyDao.queryinbanheadList(startTime,endTime,user.getAccount());
			for(InpatientBalanceHeadNow inbh:inbhl){
				inbh.setDaybalanceDate(DateUtils.getCurrentTime());//日结时间
				inbh.setDaybalanceOpcd(user.getAccount());//日结人
				inbh.setDaybalanceOpcdName(user.getName());//日结人姓名
				inbh.setDaybalanceFlag(1);//日结标志  1已日结 0 未日结
				inbh.setDaybalanceNo(sqc.toString());//日结标识号  日结统计序号
				inbh.setUpdateUser(user.getAccount());
				inbh.setUpdateTime(new Date());
				collectDailyDao.save(inbh);
			}
			//保存日结（主表）
			Date date1 = sdf.parse(startTime);  
			Date date2 = sdf.parse(endTime);
			InpatientScDreport insdt=new InpatientScDreport();
			insdt.setStatNo(sqc);//统计序号
			insdt.setBeginDate(date1);//开始时间
			insdt.setEndDate(date2);//结束时间
			insdt.setOperCode(user.getAccount());//操作员代码
			insdt.setOperDate(DateUtils.getCurrentTime());//操作时间
			insdt.setBalancePrepaycost(cdv.getBalancePrepaycost());//医疗预收款（借方）
			insdt.setPrepayCost(cdv.getPrepayCost());//医疗预收款（贷方）
			insdt.setBalanceCost(cdv.getBalanceCost());//医疗应收款（贷方）
			insdt.setDebitCheck(cdv.getDebitCheck());//银行借方存款（借方支票）
			insdt.setLenderCheck(cdv.getLenderCheck());//银行存款贷方（贷方支票）
			insdt.setTurninCash(cdv.getTurninCash());//上缴现金
			insdt.setDebitBank(cdv.getDebitBank());//借方银行卡
			insdt.setLenderBank(cdv.getLenderBank());//贷方银行卡
			insdt.setDebitHos(cdv.getDebitHos());//院内账户借方
			insdt.setLenderHos(cdv.getLenderHos());//院内账户贷方
			insdt.setDebitOther(cdv.getDebitOther());//借方其他
			insdt.setLenderOther(cdv.getLenderOther());//贷方其他
			insdt.setDerateCost(cdv.getDerateCost());//减免金额（借方）
			insdt.setBusaryPubCost(cdv.getBusaryPubcost());//公费记账金额（借方）
			insdt.setPrepayinvNum(cdv.getPrepayinvNum());////预交金发票张数
			insdt.setWasteprepayinvNum(cdv.getWasteprepayinvNum());//预交金作废张数
			insdt.setPrepayinvZone(cdv.getPrepayinvZone());//预交金票据区间
			insdt.setWasteprepayInvno(cdv.getWasteprepayInvno());//作废预交金发票号码
			insdt.setBalanceinvNum(cdv.getBalanceinvNum());//结算发票张数
			insdt.setWastebalanceinvNum(cdv.getWastebalanceinvNum());//结算发票作废张数
			insdt.setBalanceinvZone(cdv.getBalanceinvZone());//结算发票区间
			insdt.setWastebalanceInvno(cdv.getWastebalanceInvno());//结算发票号码
			insdt.setCreateDept(dept.getDeptCode());//创建科室
			insdt.setCreateTime(DateUtils.getCurrentTime());//创建时间
			insdt.setCreateUser(user.getAccount());//创建人
			collectDailyDao.save(insdt);
		return 1;
	}

	@Override
	public List<InpatientBalanceHeadNow> querymedicdatagridDaily(String state,String startTime,String endTime) throws Exception {
		return collectDailyDao.querymedicdatagridDaily(state,startTime,endTime);
	}

	@Override
	public List<InpatientInPrepayNow> queryYjjDatagridDaily(String startTime,String endTime) throws Exception {
		return collectDailyDao.queryYjjDatagridDaily(startTime,endTime);
	}

	@Override
	public List<SysEmployee> queryEmplistdaily() throws Exception {
		return collectDailyDao.queryEmplistdaily();
	}

	@Override
	public List<User> queryUselistdaily() throws Exception {
		return collectDailyDao.queryUselistdaily();
	}

	@Override
	public List<MinfeeStatCode> queryfreecodedaily() throws Exception {
		return collectDailyDao.queryfreecodedaily();
	}

}
