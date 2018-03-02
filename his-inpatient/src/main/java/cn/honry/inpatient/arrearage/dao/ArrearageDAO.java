package cn.honry.inpatient.arrearage.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.dao.EntityDao;

public interface ArrearageDAO extends EntityDao<InpatientInPrepay>{
	/**
	 * @Description:根据时间和住院号查询预交金表
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param InpatientInPrepay
	 * @param @return   
	 * @return List<InpatientInPrepay>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInPrepay> QueryInpatientInPrepay(String inpatientNo,String outDate,String inDate) throws Exception;
	/**
	 * @Description:根据时间和住院号查询预交金总额
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param InpatientInPrepay
	 * @param @return   
	 * @return List<InpatientInPrepay>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInPrepay> QueryprepayCost(String inpatientNo,String outDate,String inDate) throws Exception;
	/**
	 * @Description:根据预交金表id查询预交金
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param InpatientInPrepay
	 * @param @return   
	 * @return List<InpatientInPrepay>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInPrepay> QueryInpatientInPrepayID(String id) throws Exception;
	/**
	 * @Description:根据时间和住院号查询费用汇总表
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param InpatientFeeInfo
	 * @param @return   
	 * @return List<InpatientFeeInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientFeeInfo> QueryInpatientFeeInfo(String inpatientNo,String outDate,String inDate) throws Exception;
	/**
	 * @Description:根据时间和住院号查询费用汇总表
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param InpatientFeeInfo
	 * @param @return   
	 * @return List<InpatientFeeInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientFeeInfo> QuerytotCost(String inpatientNo,String outDate,String inDate) throws Exception;
	/**
	 * @Description:根据id查询费用汇总表
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param InpatientFeeInfo
	 * @param @return   
	 * @return List<InpatientFeeInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientFeeInfo> QueryID(String id) throws Exception;
	/**
	 * @Description:保存住院收费
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param InpatientBalancePay
	 * @param @return   
	 * @return List<InpatientFeeInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	void SaveInpatientBalancePay(InpatientBalancePay inpatientBalancePay) throws Exception;
}
