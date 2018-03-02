package cn.honry.statistics.finance.nursebill.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.nursebill.vo.NurseBillHzVo;
import cn.honry.statistics.finance.nursebill.vo.NurseBillMxVo;

@SuppressWarnings({"all"})
public interface NurseBillDAO extends EntityDao<NurseBillHzVo>{
	
	/**
	 * @Description:根据条件得到查询医院领药单汇总的hql
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	String getHzHql(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) throws Exception ;
	
	/**
	 * @Description:根据条件得到查询医院领药单汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态  page 页数 rows 记录数
 	 * @return List<NurseBillHzVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<NurseBillHzVo> getNurseBillHz(List<String> tnL, String deptCode,String billClassCode,String stime, String etime,String applyState,String page,String rows,String drugName) throws Exception ;

	/**
	 * @Description:根据条件得到查询医院领药单汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态
	 * @return int
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	int getHzTotal(List<String> tnL,String deptCode,String billClassCode,String stime, String etime,String applyState,String drugName) throws Exception ;
	
	/**
	 * @Description:根据条件得到查询医院领药单明细的hql
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	String getMxHql(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) throws Exception ;
	
	/**
	 * @Description:根据条件得到查询医院领药单明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态  page 页数 rows 记录数
 	 * @return List<NurseBillHzVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<NurseBillMxVo> getNurseBillMx(List<String> tnL,String deptCode, String billClassCode, String applyState, String page, String rows,String drugName,String beginTime, String endTime) throws Exception ;
	

	/**
	 * @Description:根据条件得到查询医院领药单汇总的记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态
	 * @return int
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	int getMxTotal(List<String> tnL,String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) throws Exception ;
	
	/**
	 * @Description:根据条件查询所有医院领药单汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态  
 	 * @return List<NurseBillHzVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<NurseBillHzVo> getAllNurseBillHz(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) throws Exception ;
	
	/**
	 * @Description:根据条件查询所有医院领药单明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态  
 	 * @return List<NurseBillMxVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<NurseBillMxVo> getAllNurseBillMx(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) throws Exception ;

	/**
	 * @Description:获取最大最小时间
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年6月22日
 	 * @return List<NurseBillMxVo>
	 * @throws Exception 
	 */
	StatVo findMaxMin() throws Exception;

	
	/**
	 * @Description:拼接查询医院领药单明细的hql新
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	String getNewMxHql(List<String> tnL,String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName,String type) throws Exception ;
	
	/**
	 * @Description:获取包装单位
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年6月22日
	 * @version: 1.0
	 */
	List queryPackUnitMap() throws Exception ;
}
