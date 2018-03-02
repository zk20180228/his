package cn.honry.statistics.finance.nursebill.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.service.BaseService;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.nursebill.vo.NurseBillHzVo;
import cn.honry.statistics.finance.nursebill.vo.NurseBillMxVo;
import cn.honry.utils.FileUtil;
import cn.honry.utils.TreeJson;
	/***
	 * 护士站领药service层
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@SuppressWarnings({"all"})
	public interface NurseBillService extends BaseService<NurseBillHzVo>{

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
	List<NurseBillHzVo> getNurseBillHz(String deptCode,String billClassCode,String applyState,String drugName,String page,String rows,String etime, String stime) throws Exception ;
	

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
	int getHzTotal(String deptCode,String billClassCode,String drugName,String applyState,String etime, String stime) throws Exception ;
	
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
	List<NurseBillMxVo> getNurseBillMx(String deptCode, String billClassCode, String applyState, String page, String rows,String drugName,String beginTime, String endTime,StatVo statVo ) throws Exception ;
	

	/**
	 * @Description:根据条件得到查询医院领药单明细记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态
	 * @return int
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	int getMxTotal(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName,StatVo statVo) throws Exception ;
	
	/***
	 * 登录病区下的各科室摆药单信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 * @parameter 
	 * @since 
	 * @return List<TreeJson>
	 */
	List<TreeJson> treeNurseBillSearch() throws Exception ;
	
	/**
	 * @Description:导出汇总列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil exportNurseBillHzVo(List<NurseBillHzVo> list, FileUtil fUtil) throws Exception ;
	/**
	 * @Description:导出明细列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	 **/
	FileUtil exportNurseBillMxVo(List<NurseBillMxVo> list, FileUtil fUtil) throws Exception ;
	
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
	List<NurseBillHzVo> getAllNurseBillHz(String deptCode,String billClassCode,String applyState,String drugName,String etime,String stime) throws Exception ;
	
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
	List<NurseBillMxVo> getAllNurseBillMx(String deptCode,String billClassCode,String applyState,String drugName,String etime,String stime) throws Exception ;

	/**
	 * @Description:获取最大最小时间
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年6月22日
	 * @version: 1.0
	 */
	StatVo findMaxMin() throws Exception ;

	/**
	 * @Description:获取包装单位
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年6月22日
	 * @version: 1.0
	 */
	Map<String, String> queryPackUnitMap() throws Exception ;



}
