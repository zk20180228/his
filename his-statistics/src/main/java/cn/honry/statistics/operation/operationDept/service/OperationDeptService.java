package cn.honry.statistics.operation.operationDept.service;

import java.util.List;
import java.util.Map;

import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationDept.vo.OpDeptDetailVo;
import cn.honry.statistics.operation.operationDept.vo.OpDeptTotalVo;
import cn.honry.statistics.operation.operationDept.vo.OpDoctorDetailVo;
import cn.honry.utils.FileUtil;

/**手术科室汇总SERVICE
 * @author  tangfeishuai
 * @date 创建时间：2016年6月3日 下午5:53:22
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@SuppressWarnings({"all"})
public interface OperationDeptService {
	/**
	 * 根据条件手术医生明细
	 * @Description:根据条件手术医生明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OpDoctorDetailVo> getOpDoctorDetailVo(String beginTime,String endTime,String opcDept,String execDept,String opDoctor,String page,String rows, String identityCard);
	
	
	/**
	 * @Description:根据条件查询科室汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   execDept 执行科室   page 当前页数   rows 分页条数
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OpDeptTotalVo> getOpDeptTotalVo(String beginTime,String endTime,String opcDept,String execDept,String page,String rows);
	
	/**
	 *根据条件查询手术科室明细
	 * @Description:根据条件查询手术科室明细
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OpDeptDetailVo> getOpDeptDetailVo(String beginTime,String endTime,String opcDept,String execDept,String opDoctor,String page,String rows);
	

	/**
	 * 根据条件手术医生明细总记录数
	 * @Description:根据条件手术医生明细总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public int getOpDoctorTotal(String beginTime,String endTime,String opcDept,String execDept,String opDoctor, String identityCard);
	
	/**
	 * @Description:导出手术医生明细列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil exportOpDoctorDetailVo(List<OpDoctorDetailVo> list, FileUtil fUtil);

	/**
	 * 根据条件查询手术科室明细(总条数)
	 * @Description:根据条件查询手术科室明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public int getOpDeptDetailTotal(String beginTime, String endTime,String opcDept,
			String execDept, String opDoctor);
	
	/**
	 * 手术科室汇总>手术医生明细报表打印
	 * @Description:导出手术医生明细
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @param:deptMap 部门map
	 * @param:empMap 员工表Map
	 * @param:identityCard 身份证号
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OpDoctorDetailVo> getOpDoctorDetailToReport(String beginTime, String endTime, String opcDept,
			String execDept, String opDoctor, Map<String, String> deptMap, Map<String, String> empMap, String identityCard);


	/**
	 * @Description 查询科室信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public List<MenuListVO> querysysDeptment();

	/**
	 * @Description 查询医生信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param deptCode科室code
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public List<MenuListVO> queryDoctor(String deptCode);
}
