package cn.honry.patinent.account.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.PatientAccountdetail;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.account.dao.AccountDetailDAO;
import cn.honry.patinent.account.dao.AccountrepayDetailDAO;
import cn.honry.patinent.account.service.AccountrepayDetailService;
import cn.honry.utils.ShiroSessionUtils;

@Service("repayDetailService")
@Transactional
@SuppressWarnings({ "all" })
public class AccountrepayDetailServiceImpl implements AccountrepayDetailService{
	/** 账户数据库操作类 **/
	@Autowired
	@Qualifier(value = "repayDetailDAO")
	private AccountrepayDetailDAO repayDetailDAO;
	@Autowired
	@Qualifier(value = "detailDAO")
	private AccountDetailDAO detailDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public PatientAccountrepaydetail get(String id) {
		return repayDetailDAO.get(id);
	}

	@Override
	public boolean saveOrUpdate(PatientAccountrepaydetail entity,PatientAccountdetail detail,String type) {
		
		if(StringUtils.isBlank(entity.getId())){//保存		
				entity.setId(null);
				entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId());
				entity.setCreateTime(new Date());
				OperationUtils.getInstance().conserve(null, "患者账户管理", "INSERT_INTO", "T_PATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONINSERT);
			}else{//修改
				entity.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				entity.setUpdateTime(new Date());
				OperationUtils.getInstance().conserve(entity.getId(), "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONUPDATE);
			}
			
			if(StringUtils.isBlank(detail.getId())){//保存		
				detail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				detail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId());
				detail.setCreateTime(new Date());
				OperationUtils.getInstance().conserve(null, "患者账户管理", "INSERT_INTO", "T_PATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONINSERT);
				detail.setId(null);
				if(entity.getDetailOptype()==3){
				Map<String,String> invoiceMap =invoicePublicMap(type);
					 detail.setDetailInvoiceno(invoiceMap.get("strAll")) ;
					 detail.setDetailInvoicetype(type);
					 detailDAO.save(detail);  
					 repayDetailDAO.save(entity);
						return true;
				 }else if(entity.getId()==null&&entity.getDetailOptype()==1){
					 repayDetailDAO.save(entity);
						return true;
				 }else if(entity.getId()==null&&entity.getDetailOptype()==2){
					 repayDetailDAO.save(entity);
						return true;
				 }
				 else{
					 
					 return false;
				 }
			}else{//修改
				detail.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				detail.setUpdateTime(new Date());
				OperationUtils.getInstance().conserve(detail.getId(), "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONUPDATE);
				detailDAO.save(detail);
				return true;
			}
			
	}

	@Override
	public List<PatientAccountrepaydetail> getPage(String page, String rows,
			PatientAccountrepaydetail entity,String accountId,int ishis,int detailAccounttype) {
		return repayDetailDAO.getPage(entity, page, rows,accountId,ishis,detailAccounttype);
	}

	@Override
	public int getTotal(PatientAccountrepaydetail entity,String accountId,int ishis,int detailAccounttype) {
		return repayDetailDAO.getTotal(entity,accountId, ishis,detailAccounttype);
	}

	@Override
	public void del(String id) {
		repayDetailDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public double totalAccount(String accountId) {
		return repayDetailDAO.totalAccount(accountId);
	}

	@Override
	public List<PatientAccountrepaydetail> listRepayDetail(String accountId) {
		
		return repayDetailDAO.listRepayDetail(accountId);
	}

	@Override
	public void updateIshis(String accountId) {
		repayDetailDAO.updateIshis(accountId);
		OperationUtils.getInstance().conserve("pid = " + accountId, "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void delByParentId(String id) {
		repayDetailDAO.delByParentId(id);
	}

	@Override
	public void saveOrUpdate(PatientAccountrepaydetail entity) {
	}
	/**  
	 *  
	 * @Description：发票领取公共法方map: 
	 * @Author：wujiao
	 * @CreateDate：2016-01-19下午06:56:13  
	 * @Modifier：wujiao
	 * @ModifyDate：2016-01-19下午06:56:13    
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> invoicePublicMap(String type) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysEmployee emp=detailDAO.findEmpByUserid(user.getId());
		Map<String,String> invoiceMap = new HashMap<String, String>();
		if(emp!=null){
			FinanceInvoice finnce =detailDAO.findbyall(emp.getId(),type);//根据领取人，发票类型，使用状态查询。
			if(finnce!=null){
			String startNo=finnce.getInvoiceUsedno();
			   Integer len =startNo.length();
				String startNoStren=startNo.toString().substring(0, 1);
				String startNoStr=startNo.toString().substring(1,startNo.toString().length());
				Integer useNo=Integer.valueOf(startNoStr)+1;
					if(useNo==-1){
						startNoStr=startNo.subSequence(0, startNo.length()-4)+"9999";
					}
				String subStr=useNo.toString();
			    int	strlen=useNo.toString().length();
					for(int i=0;i<len-strlen-1;i++){
						subStr ="0"+subStr;
					}
				String strAll=startNoStren+subStr;
			if(finnce!=null&&finnce.getInvoiceUsestate()==1){//状态为使用
					invoiceMap.put("strAll", strAll);//发票号
					invoiceMap.put("resMSg","0");//不需要领取发票
				    finnce.setInvoiceUsedno(strAll);
				    detailDAO.save(finnce);
					return invoiceMap;
			 }else if(finnce!=null&&finnce.getInvoiceUsestate()==0){//状态为未用
					 finnce.setInvoiceUsestate(1);
					 finnce.setInvoiceUsedno(strAll);
					 invoiceMap.put("strAll", strAll);//发票号
					 invoiceMap.put("resMSg","0");//不需要领取发票
					 detailDAO.save(finnce);
					return invoiceMap;
			 }
			 else{
				 invoiceMap.put("resMSg","1"); //需要领取发票
				 return invoiceMap;
			 }
		  }else{
			FinanceUsergroup group= detailDAO.findFinanceByempId(emp.getId());
			if(group!=null){//当前登录用户在组里面
				FinanceInvoice finncei =detailDAO.findbyall(group.getNo(),type);//根据领取组，发票类型，使用状态查询。
					String startNo=finncei.getInvoiceUsedno();
					   Integer len =startNo.length();
						String startNoStren=startNo.toString().substring(0, 1);
						String startNoStr=startNo.toString().substring(1,startNo.toString().length());
						Integer useNo=Integer.valueOf(startNoStr)+1;
							if(useNo==-1){
								startNoStr=startNo.subSequence(0, startNo.length()-4)+"9999";
							}
						String subStr=useNo.toString();
					    int	strlen=useNo.toString().length();
							for(int i=0;i<len-strlen-1;i++){
								subStr ="0"+subStr;
							}
						String strAll=startNoStren+subStr;
					if(finncei!=null&&finncei.getInvoiceUsestate()==1){//状态为使用
							invoiceMap.put("strAll", strAll);//发票号
							invoiceMap.put("resMSg","0");//不需要领取发票
						    finncei.setInvoiceUsedno(strAll);
						    detailDAO.save(finncei);
							return invoiceMap;
					 }else if(finncei!=null&&finncei.getInvoiceUsestate()==0){//状态为未用
							 finncei.setInvoiceUsestate(1);
							 finncei.setInvoiceUsedno(strAll);
							 invoiceMap.put("strAll", strAll);//发票号
							 invoiceMap.put("resMSg","0");//不需要领取发票
							 detailDAO.save(finncei);
							return invoiceMap;
					 }else{
						 invoiceMap.put("resMSg","1"); //需要领取发票
						 return invoiceMap;
					 }
			}else{//当前登录用户不在组里
				invoiceMap.put("resMSg","2"); //改用户没有发票 
				return invoiceMap;	
			}
		}
		}else{
			invoiceMap.put("resMSg","2"); //改用户没有发票 
			return invoiceMap;	
		}
	}
	
}
