package cn.honry.inner.baseinfo.employee.dao;

import java.util.List;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

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
@SuppressWarnings({"all"})
public interface EmployeeInInterDAO extends EntityDao<SysEmployee>{

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
	List<SysDepartment> findEmployeesByUserId(String userId);

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
	List<SysEmployee> findEmpByUserId(String userId);
	
	/**根据userCode查询员工信息
	 * @Description: 根据userCode查询员工信息
	 * @param userCode
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
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysEmployee> getPagebyhql(String hql, String page, String rows);
	
	/**  
	 *  
	 * @Description：  根据用户id获得员工
	 * @Author：lt
	 * @CreateDate：2015-12-03 
	 * @version 1.0
	 *
	 */
	SysEmployee queryByUserId(String userid);
	/**
	 * 查询树id
	 * @author  zpty
	 * @param 参数id
	 * @date 2015-06-03 16:00
	 * @version 1.0
	 */
	String searchtree(String searcht);
	/**
	 * 根据ID查询员工信息
	 * @author  LDL
	 * @param 参数id
	 * @date 2016-7-13 16:00
	 * @version 1.0
	 */
	SysEmployee findEmpById(String id);

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
	 * 员工业务接口：根据accunt查询账号
	 * @author zhuxiaolu
	 * @createDate： 2016年4月12日 上午11:22:48 
	 * @modifier zhuxiaolu
	 * @modifyDate：2016年4月12日 上午11:22:48
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> findEmpByUserAccunt(String accunt);
	
	/**
	 * 员工业务接口：根据工号查询账号
	 * @author zhuxiaolu
	 * @createDate： 2016年4月12日 上午11:22:48 
	 * @modifier zhuxiaolu
	 * @modifyDate：2016年4月12日 上午11:22:48
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	SysEmployee getEmpByJobNo(String jobNo);
	
	/**
	 * 根据类型获取员工数据
	 * @param type
	 * @return
	 */
	List<SysEmployee> getEmpByType(String type);
	
	/**
	 * 获取员工数据
	 * @return
	 */
	public List<SysEmployee> getSysEmployeeInfo();
	
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
	EmployeeExtend getEmp(String jobNo);
}
