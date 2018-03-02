package cn.honry.inpatient.delivery.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
/**
 * 取消发送数据层
 * @author  lyy
 * @createDate： 2015年12月30日 下午5:37:34 
 * @modifier lyy
 * @modifyDate：2015年12月30日 下午5:37:34  
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface CancelSendDAO extends EntityDao<DrugApplyout> {
	/**
	 * 取消发送分页查询
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午5:37:34 
	 * @modifier lyy
	 * @modifyDate：2015年12月30日 下午5:37:34  
	 * @modifyRmk：    drugApplyout 出库申请表   page 分页查询总页数    rows  总条数
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DrugApplyout> getPage(DrugApplyout drugApplyout,String page, String rows) throws Exception ;
	/**
	 * 取消发送总条数
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午5:37:34 
	 * @modifier lyy
	 * @modifyDate：2015年12月30日 下午5:37:34  
	 * @modifyRmk：    drugApplyout 出库申请表 
	 * @version 1.0
	 */
	int getTotal(DrugApplyout drugApplyout);
	/**
	 * 查询所有的药房科室
	 * @author  lyy
	 * @createDate： 2015年12月31日 上午10:41:28 
	 * @modifier lyy
	 * @modifyDate：2015年12月31日 上午10:41:28  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysDepartment> queryDept() throws Exception; 
	/**
	 * 取消发送
	 * @author  lyy
	 * @createDate： 2016年1月4日 下午2:00:09 
	 * @modifier lyy
	 * @modifyDate：2016年5月5日 下午2:00:09  
	 * @modifyRmk：  ids 出库申请表的主键id集合 userId 登录人 
	 * @version 1.0
	 * @throws Exception 
	 */
	String enitUpdate(String[] id,String userId) throws Exception;
	/**
	 * 分页查询出库申请表list集合
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:44:35 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:44:35
	 * @param：      entity 出库申请表   page 分页查询总页数    rows  总条数  parameter 参数值
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DrugApplyoutNow> getPageDrugApply(DrugApplyoutNow entity, String page, String rows,Integer parameter,String deptId) throws Exception;
	/**
	 * 分页查询出库申请表总条数
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:44:02 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:44:02
	 * @param：    entity 出库申请表     parameter 参数值
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTotalDrugApply(DrugApplyoutNow entity,Integer parameter,String deptId) throws Exception;
}
