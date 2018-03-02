package cn.honry.inpatient.delivery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.delivery.dao.CancelSendDAO;
import cn.honry.inpatient.delivery.service.CancelSendService;
import cn.honry.utils.ShiroSessionUtils;
@Service("cancelSendService")
@Transactional
@SuppressWarnings({"all"})
/**
 * 取消发送业务层
 * @author  lyy
 * @createDate： 2015年12月30日 下午5:37:34 
 * @modifier lyy
 * @modifyDate：2015年12月30日 下午5:37:34  
 * @modifyRmk：  
 * @version 1.0
 */
public class CancelSendServiceImpl implements CancelSendService {
	@Autowired
	@Qualifier(value="cancelSendDAO")
	private CancelSendDAO cancelSendDao;
	@Autowired
	@Qualifier(value="drugInfoInInerDAO")
	private DrugInfoInInerDAO drugInfoInInerDAO;
	/** 参数数据库操作类 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public DrugApplyout get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(DrugApplyout entity) {
		
	}
	/**
	 * 查询所有的药房科室
	 * @author  lyy
	 * @createDate： 2015年12月31日 上午10:43:07 
	 * @modifier lyy
	 * @modifyDate：2015年12月31日 上午10:43:07  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<SysDepartment> queryDept() throws Exception {
		return cancelSendDao.queryDept();
	}
	/**
	 * 取消发送
	 * @author  lyy
	 * @createDate： 2016年1月4日 下午2:00:09 
	 * @modifier lyy
	 * @modifyDate：2016年5月4日 下午2:00:09  
	 * @modifyRmk：  ids 出库申请表主键id数组
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public String editUpdate(String ids) throws Exception {
		String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String [] id=ids.split(",");
		String retVal = cancelSendDao.enitUpdate(id,userId);
		OperationUtils.getInstance().conserve(ids,"药品集中发送取消","UPDATE","t_drug_applyout_now",OperationUtils.LOGACTIONDELETE);
		return retVal;
	}
	/**
	 * 分页查询列表list
	 * @author  lyy
	 * @createDate： 2015年12月31日 下午3:14:00 
	 * @modifier lyy
	 * @modifyDate：2015年12月31日 下午3:14:00  
	 * @modifyRmk：  entity 出库申请表  page 分页总页数  rows 总条数
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<DrugApplyoutNow> getPageDrugApply(DrugApplyoutNow entity, String page, String rows) throws Exception {
		String deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String infoTime=parameterInnerDAO.getParameterByCode("ypqxfs");
		int days=1;
		days = Integer.valueOf(infoTime);
		List<DrugApplyoutNow> list = cancelSendDao.getPageDrugApply(entity,page,rows,days,deptId);
		return list;
	}
	/**
	 * 分页查询总条数
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:41:46 
	 * @modifier lyy
	 * @modifyDate：2016年5月5日 下午5:41:46
	 * @param：    entity 出库申请表
	 * @modifyRmk：  添加了参数
	 * @version 1.0
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@Override
	public int getTotalDrugApply(DrugApplyoutNow entity) throws Exception {
		String deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String parameterValue = parameterInnerDAO.getParameterByCode("ypqxfs");//根据参数去查参数值(ypqxfs 药品取消发送)
		if("".equals(parameterValue)){
			parameterValue = "7";
		}
		return cancelSendDao.getTotalDrugApply(entity,Integer.valueOf(parameterValue),deptId);
	}
}
