package cn.honry.statistics.operation.operationDept.service.impl;
import java.io.IOException;
/***
 * 手术科室汇总service实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationDept.dao.OperationDeptDao;
import cn.honry.statistics.operation.operationDept.service.OperationDeptService;
import cn.honry.statistics.operation.operationDept.vo.OpDeptDetailVo;
import cn.honry.statistics.operation.operationDept.vo.OpDeptTotalVo;
import cn.honry.statistics.operation.operationDept.vo.OpDoctorDetailVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;

@Service("operationDeptService")
@Transactional
@SuppressWarnings({"all"})
public class OperationDeptServiceImpl implements OperationDeptService{
	@Autowired
	@Qualifier(value="operationDeptDao")
	private OperationDeptDao operationDeptDao;
	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public EmployeeInInterService getEmployeeInInterService() {
		return employeeInInterService;
	}
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	public DeptInInterService getDeptInInterService() {
		return deptInInterService;
	}
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
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
	@Override
	public List<OpDoctorDetailVo> getOpDoctorDetailVo(String beginTime, String endTime,String opcDept,String execDept,String opDoctor, String page,
			String rows, String identityCard) {
		return operationDeptDao.getOpDoctorDetailVo(beginTime, endTime, opcDept,execDept,opDoctor, page, rows, identityCard);
	}
	/**
	 * 根据条件查询手术科室汇总
	 * @Description:根据条件查询手术科室汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OpDeptTotalVo> getOpDeptTotalVo(String beginTime, String endTime,String opcDept, String execDept, String page,
			String rows) {
		return operationDeptDao.getOpDeptTotalVo(beginTime, endTime, opcDept, execDept, page, rows);
	}
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
	@Override
	public List<OpDeptDetailVo> getOpDeptDetailVo(String beginTime, String endTime, String opcDept,String execDept, String opDoctor,String page,
			String rows) {
		return operationDeptDao.getOpDeptDetailVo(beginTime, endTime, opcDept, execDept,opDoctor, page, rows);
	}
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
	@Override
	public int getOpDoctorTotal(String beginTime, String endTime, String opcDept,String execDept,String opDoctor, String identityCard) {
		return operationDeptDao.getOpDoctorTotal(beginTime, endTime, opcDept,execDept,opDoctor, identityCard);
	}
	
	/**  
	 * 
	 *导出手术医生明细列表
	 * @Author: aizhonghua
	 * @CreateDate: 2017年7月5日 下午3:53:44 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年7月5日 下午3:53:44 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param: list存放数据集合
	 * @param: fUtil 导出工具
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public FileUtil exportOpDoctorDetailVo(List<OpDoctorDetailVo> list, FileUtil fUtil) {
		Map<String, String> empMap =employeeInInterService.queryEmpCodeAndNameMap();
		Map<String, String> deptMap=deptInInterService.querydeptCodeAndNameMap();
		for (OpDoctorDetailVo model : list) {
			String record="";
			//手术医生科室
			record = CommonStringUtils.trimToEmpty(deptMap.get(model.getOpDoctorDept())) + ",";
			//手术医生
			record +=CommonStringUtils.trimToEmpty(empMap.get(model.getOpDoctor())) + ",";
			if(model.getPreDate()!=null){
				record += DateUtils.formatDateY_M_D_H_M_S(model.getPreDate()) + ",";
			}else{
				record += "" + ",";
			}
			//手术住院流水号
			record += CommonStringUtils.trimToEmpty(model.getInpatientNo()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getItemName()) + ",";
			record += model.getTotCost() + ",";
			record += CommonStringUtils.trimToEmpty(deptMap.get(model.getExecDept())) + ",";
			record += model.getFeeOperCode()==null?"":empMap.get(model.getFeeOperCode()) + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}
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
	@Override
	public int getOpDeptDetailTotal(String beginTime, String endTime,String opcDept,
			String execDept, String opDoctor) {
		return operationDeptDao.getOpDeptDetailTotal(beginTime,endTime, opcDept,execDept, opDoctor);
	}
	
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
	@Override
	public List<OpDoctorDetailVo> getOpDoctorDetailToReport(String beginTime, String endTime, String opcDept,
			String execDept, String opDoctor, Map<String, String> deptMap, Map<String, String> empMap, String identityCard) {
		return operationDeptDao.getOpDoctorDetailToReport(beginTime, endTime, opcDept, execDept, opDoctor,deptMap,empMap,identityCard);
	}
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
	@Override
	public List<MenuListVO> querysysDeptment() {
		return operationDeptDao.querysysDeptment();
	}
	
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
	@Override
	public List<MenuListVO> queryDoctor(String deptCode) {
		return operationDeptDao.queryDoctor(deptCode);
	}
	

}
