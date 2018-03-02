package cn.honry.statistics.operation.operationDept.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OperationApply;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVo;
import cn.honry.statistics.operation.operationDept.vo.OpDeptDetailVo;
import cn.honry.statistics.operation.operationDept.vo.OpDeptTotalVo;
import cn.honry.statistics.operation.operationDept.vo.OpDoctorDetailVo;
/***
 * 手术科室汇总DAO层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月27日 
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface OperationDeptDao extends EntityDao <OperationCostVo>{
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
	 * @Description:根据条件查询科室汇总中间 方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   execDept 执行科室   page 当前页数   rows 分页条数
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OpDeptTotalVo> getOpDeptTotalVo2(String beginTime,String endTime,String opcDept,String execDept,String page,String rows);
	
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
	 * @Description:按医生科室分组查询手术申请信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationApply> getOpApplyByDept();
	/**
	 * @Description:按医生科室，医生分组查询手术申请信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationApply> getOpApplyByDocDept();
	/**
	 * @Description:查询该科室下的手术申请记录
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationApply> findOpApplyByDocDept(String execDept);
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
	 * @Description:查询该医生的手术申请记录
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationApply> findOpApplyByDoc(String opDoctor);
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
	 * 手术医生明细统计报表打印
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间 
	 * @param:execDept 执行科室  
	 * @param:page 当前页数  
	 * @param:rows 分页条数 
	 * @param:opcDept 医生科室
	 * @param:opDoctor手术医生
	 * @param:identityCard身份证号
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
