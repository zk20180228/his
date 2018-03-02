package cn.honry.statistics.finance.operationCost.service;

import java.util.List;
import java.util.Map;

import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVo;
import cn.honry.utils.FileUtil;

/**手术费用汇总SERVICE
 * @author  tangfeishuai
 * @date 创建时间：2016年5月27日 下午5:53:22
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@SuppressWarnings({"all"})
public interface OperationCostService {


	/**  
	 * 
	 * 手术费用汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	List<OperationCostVo> queryOperationCost(String beganTime, String endTime,
			String inpatientNo, String execDept,String page,String rows, String identityCard);
	
	/**
	 * @Description:根据条件查询手术费用汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public int getTotal(String beganTime, String endTime,String patientNo,String execDept); 
	/**
	 * @Description:科室id与科室name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String , String> depMap();
	
	/**
	 * @Description:根据条件查询所有手术费用汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationCostVo> allOperationCostVo(String beganTime, String endTime,String patientNo,String execDept); 
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil export(List<OperationCostVo> list, FileUtil fUtil);

	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:36:23 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:36:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<MenuListVO> getDeptList();
	/**  
	 * 
	 * 手术费用汇总(分页总条数)
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	int queryOperationCostTotal(String startTime, String endTime,
			String inpatientNo, String execDept,String identityCard);

	/**
	 * @Description:根据条件查询手术费用汇总(导出及打印)
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； inpatientNo 住院流水号； execDept 执行科室 identityCard 身份证号
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @param  
	 */
	List<OperationCostVo> queryOperationCostOther(String startTime,
			String endTime, String inpatientNo, String execDept,
			String identityCard);
	
	
}
