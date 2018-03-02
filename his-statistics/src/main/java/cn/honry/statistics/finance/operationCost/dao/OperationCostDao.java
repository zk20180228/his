package cn.honry.statistics.finance.operationCost.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVo;

/***
 * 手术费用汇总DAO层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月8日 上午9:47:41 
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface OperationCostDao extends EntityDao <OperationCostVo>{
	
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
	public List<OperationCostVo> OperationCostVo(String beganTime, String endTime,String patientNo,String execDept,String page,String rows,List<String> tnL, String identityCard); 
	/**
	 * @Description:根据条件查询手术费用汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public int getTotal(String beganTime, String endTime,String patientNo,String execDept); 
	/**
	 * @Description:查询所有科室
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:
	 * @return List<SysDepartment>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<SysDepartment> depMentList(); 
	
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
	 * @Description:获取最大最小时间
	 * @Author: zhangjin
	 * @CreateDate: 2016年12月30日 
	 * @param:b
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public StatVo findMaxMin();
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
	public List<MenuListVO> getDeptList();
	
	
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
	public int queryOperationCostTotal(String beganTime, String endTime,
			String inpatientNo, String execDept, List<String> tnL,
			String identityCard);
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
	public List<OperationCostVo> queryOperationCostOther(String beganTime,
			String endTime, String inpatientNo, String execDept,
			List<String> tnL, String identityCard); 
}
