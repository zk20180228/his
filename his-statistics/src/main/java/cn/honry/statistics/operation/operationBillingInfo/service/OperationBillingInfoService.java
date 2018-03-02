package cn.honry.statistics.operation.operationBillingInfo.service;
/***
 * 手术计费信息service层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
/**   
*  手术计费信息汇总service
* @className：OperationBillingInfoService
* @description：  手术计费信息Service
* @author：tangfeishuai
* @createDate：2016-5-30 上午10:52:19  
* @modifier：tangfeishuai
* @modifyDate：2015-5-30 上午10:52:19  
* @modifyRmk：  
* @version 1.0
 */
import cn.honry.base.service.BaseService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface OperationBillingInfoService extends BaseService<OperationBillingInfoVo>{
	/**
	 * 根据条件查询手术计费信息
	 * @Description:根据条件查询手术计费信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeState 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  page 当前页数   rows 分页条数  identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationBillingInfoVo> getOperationBillingInfoVo(String beginTime, String endTime,String opStatus,String execDept,
			String feeBegTime,String feeEndTime,String feeStates,String inState,String opDoctor,String opDoctordept,String page,String rows, String identityCard);
	/**
	 * @Description:查询员工得到员工id和name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月3日
	 * @param:
	 * @returnMap<String, String> 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public Map<String, String> getEmpMap();
	
	/**
	 * 根据条件查询手术计费信息得到总记录数
	 * @Description:根据条件查询手术计费信息得到总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeStates 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室   identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @param identityCard 
	 */
	public  int getTotal(String beginTime, String endTime,String opStatus,String execDept,
			String feeBegTime,String feeEndTime,String feeStates,String inState,String opDoctor,String opDoctordept, String identityCard);
	
	/**
	 * 根据条件查询所有手术计费信息
	 * @Description:根据条件查询所有手术计费信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeStates 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @param identityCard 
	 */
	public List<OperationBillingInfoVo> getAllOperationBillingInfoVo(String beginTime, String endTime,String opStatus,String execDept,
			String feeBegTime,String feeEndTime,String feeStates,String inState,String opDoctor,String opDoctordept, String identityCard);
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil export(List<OperationBillingInfoVo> list, FileUtil fUtil);
	
	/**
	 * @Description:员工
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @return List<SysEmployee>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<SysEmployee> queryemployeeCombobox(); 
	
	/**
	 * @Description:部门
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @return List<SysDepartment>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<SysDepartment> querydeptmentCombobox();
	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	public List<MenuListVO> getDeptList();
	
	/**  
	 * 
	 * 获取医生
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	public List<MenuListVO> getDoctorList();
}
