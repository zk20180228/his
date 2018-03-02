package cn.honry.inner.baseinfo.employee.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

/**
 * 员工业务接口
 * @author azh
 * @createDate： 2016年4月12日 上午11:22:48 
 * @modifier azh
 * @modifyDate：2016年4月12日 上午11:22:48
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
public interface EmployeeInInterService extends BaseService<SysEmployee> {

	/**
	 * 员工业务接口：根据userid查询其部门
	 * @author azh
	 * @createDate： 2016年4月12日 上午11:22:48 
	 * @modifier azh
	 * @modifyDate：2016年4月12日 上午11:22:48
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	SysDepartment findEmployeesByUserId(String id);

	/**
	 * 员工业务接口：根据userid查询员工
	 * @author azh
	 * @createDate： 2016年4月12日 上午11:22:48 
	 * @modifier azh
	 * @modifyDate：2016年4月12日 上午11:22:48
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	SysEmployee findEmpByUserId(String userId);
	
	/** 员工业务接口：根据jobNo查询员工信息
	 * @Description: 根据jobNo查询员工信息
	 * @param jobNo jobNo
	 * @Author: dutianliang
	 * @CreateDate: 2016年5月3日
	 * @Version: V 1.0
	 */
	SysEmployee findEmpByuserCode(String userCode);
	
	
	/**
	 * 查询财务部下的具有核算权利的员工
	 * @author  kjh
	 * @param entity
	 * @date 2015-06-10
	 * @version 1.0
	 */
	List<SysEmployee> findFianceEmployee(String page,String rows,SysEmployee entity);
	/**
	 * 查询财务部下的具有核算权利的员工(分页页数)
	 * @author  kjh
	 * @param entity
	 * @date 2015-06-10
	 * @version 1.0
	 */
	int getFianceEmployeeTotal(SysEmployee entity);
	
	/**  
	 *  
	 * @Description：  住院诊断获取医生
	 * @Author：zahngjin
	 * @CreateDate：2015-12-29   
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysEmployee> queryDiangDoctor(String no);
	
	/**  
	 *  
	 * @Description：  查询树
	 *@Author：wujiao
	 * @CreateDate：2015-6-25 上午3:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 上午3:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	List<SysEmployee> getEmployeeListByDeptId(String deptId);
	/**
	 * 
	 * @Description  查询树的id
	 * @author  lyy
	 * @createDate： 2016年7月7日 下午4:11:01 
	 * @modifier lyy
	 * @modifyDate：2016年7月7日 下午4:11:01
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String searchtree(String searcht);
	/**
	 * 
	 * @Description  根据员工Id
	 * @author  ldl
	 * @createDate： 2016年7月13日 下午4:11:01 
	 * @modifier ldl
	 * @modifyDate：2016年7月13日 下午4:11:01
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	SysEmployee findEmpById(String id);
	/**
	 * 
	 * @Description 获得员工JobNo和name的map
	 * @author  aizhonghua
	 * @createDate： 2016年7月13日 下午4:11:01 
	 * @modifier aizhonghua
	 * @modifyDate：2016年7月13日 下午4:11:01
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> queryEmpCodeAndNameMap();
	
	/**  
	 *  
	 * @Description：根据code获取医生
	 * @Author：zhangjin
	 * @CreateDate：2016-7-21
	 * @Modifier：
	 * @ModifyDate： 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	SysEmployee querNurseDoctor(String code);
	
	/**
	 * 根据类型获取员工数据
	 * @Author：luyanshou
	 * @CreateDate：2016-12-2
	 * @Modifier：
	 * @ModifyDate： 
	 * @param：
	 * @ModifyRmk：  
	 * @return
	 */
	List<SysEmployee> getEmpByType(String type);
	
	/**
	 * 根据员工号获取员工数据
	 * @Author：luyanshou
	 * @CreateDate：2016-12-2
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @return
	 */
	SysEmployee getEmpByJobNo(String jobNo);
	
	/**
	 * 获取所有的员工
	 * @return
	 */
	List<SysEmployee> getEmpInfo();
	/**
	 * 
	 * @param startPage
	 * @param endPage
	 * @return list
	 * @see 分页查询医生
	 */
	List<SysEmployee> getAffuseDoctor(String startPage, String endPage, String p);
	/**
	 * 
	 * @return list
	 * @see 分页查询医生
	 */
	int getAffuseDoctorTotal(String p);
	
	/**
	 * 根据员工号获取员工扩展信息
	 * @param jobNo
	 * @return
	 */
	EmployeeExtend getEmpExtend(String jobNo);
}
