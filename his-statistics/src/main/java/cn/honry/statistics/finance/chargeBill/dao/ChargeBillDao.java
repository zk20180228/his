package cn.honry.statistics.finance.chargeBill.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.chargeBill.vo.ChargeBillVo;

/**   
*  
* @className：ChargeBill
* @description：住院患者费用清单dao
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
public interface ChargeBillDao extends EntityDao<ChargeBillVo>{
	/**
	 * @Description:通过病历号住院主表的患者信息
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> queryInpatientInfo(String medId) throws Exception;
	/**
	 * @Description:通过住院流水号查询患者的费用信息（住院药品明细表、住院费药品明细表）
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @param tnlMed 
	 * @param tnlItem 
	 * @throws Exception 
	**/
	List<ChargeBillVo> queryDatagridinfo(List<String> tnlItem, List<String> tnlMed, String inpatientNo,String startTime,String endTime,String sendFlag) throws Exception;
	/**
	 * @Description:查询住院科室用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	List<SysDepartment> queryZYDept();
	/**
	 * @Description:查询患者弹窗
	 * @Author：  tcj
	 * @param medicalrecordId 6位病历号
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	 * @throws Exception 
	**/
	List<Patient> queryPatientbill(String medicalrecordId) throws Exception;
	/**
	 * @Description:查询员工用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	List<User> queryEmpbill();

	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 住院非药品明细表</p>
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	StatVo findMaxMinItem() throws Exception;
	
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间  住院药品明细表</p>
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	StatVo findMaxMinMed() throws Exception;
}
