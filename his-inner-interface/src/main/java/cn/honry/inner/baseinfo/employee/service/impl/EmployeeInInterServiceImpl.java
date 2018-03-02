package cn.honry.inner.baseinfo.employee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.utils.RedisUtil;

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
@Service("employeeInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class EmployeeInInterServiceImpl implements EmployeeInInterService {

	@Resource
	private RedisUtil redis;
	
	@Autowired
	@Qualifier(value="employeeInInterDAO")
	private EmployeeInInterDAO employeeInInterDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysEmployee get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(SysEmployee entity) {
		
	}

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
	@Override
	public SysDepartment findEmployeesByUserId(String userId) {
		List<SysDepartment> employeeList = employeeInInterDAO.findEmployeesByUserId(userId);
		return employeeList.size()>0?employeeList.get(0):null;
	}

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
	@Override
	public SysEmployee findEmpByUserId(String userId) {
		List<SysEmployee> employeeList = employeeInInterDAO.findEmpByUserId(userId);
		return employeeList.size()>0?employeeList.get(0):null;
	}
	
	/**根据userCode查询员工信息
	 * @Description: 根据userCode查询员工信息
	 * @param userCode
	 * @Author: dutianliang
	 * @CreateDate: 2016年5月3日
	 * @Version: V 1.0
	 */
	@Override
	public SysEmployee findEmpByuserCode(String userCode) {
		return employeeInInterDAO.findEmpByuserCode(userCode);
	}

	/**
	 * 查询财务部下的具有核算权利的员工
	 * @author  kjh
	 * @param null
	 * @date 2015-06-10
	 * @version 1.0
	 */
	@Override
	public List findFianceEmployee(String page,String rows,SysEmployee entity) {
		return employeeInInterDAO.findFianceEmployee(page,rows,entity);
	}

	/**
	 * 查询财务部下的具有核算权利的员工(分页页数)
	 * @author  kjh
	 * @param null
	 * @date 2015-06-10
	 * @version 1.0
	 */
	@Override
	public int getFianceEmployeeTotal(SysEmployee entity) {
		return employeeInInterDAO.getFianceEmployeeTotal(entity);
	}
	
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
	@Override
	public List<SysEmployee> queryDiangDoctor(String no) {
		return employeeInInterDAO.queryDiangDoctor(no);
	}
	
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
	@Override
	public List<SysEmployee> getEmployeeListByDeptId(String deptId) {
		String hql="from SysEmployee e where e.del_flg=0 and e.stop_flg=0 AND e.deptId = '"+deptId+"'";
		List<SysEmployee> employeeList=employeeInInterDAO.findByObjectProperty(hql, null);
		if(employeeList!=null&&employeeList.size()>0){
			return employeeList;
		}
		return null;
	}
	/**
	 * 查询树id
	 * @author  zpty
	 * @param 参数id
	 * @date 2015-06-03 16:00
	 * @version 1.0
	 */
	@Override
	public String searchtree(String searcht) {
		return employeeInInterDAO.searchtree(searcht);
	}

	@Override
	public SysEmployee findEmpById(String id) {
		return employeeInInterDAO.findEmpById(id);
	}

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
	@Override
	public Map<String, String> queryEmpCodeAndNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<SysEmployee> list=null;
		try {
			list = (List<SysEmployee>) redis.get("emp_queryAll");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list==null){
			list = employeeInInterDAO.getAll();
			try {
				redis.set("emp_queryAll",  list);
				redis.persist("emp_queryAll");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(list!=null&&list.size()>0){
			for(SysEmployee emp : list){
				map.put(emp.getJobNo(),emp.getName());
			}
		}
		return map;
	}
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
	@Override
	public SysEmployee querNurseDoctor(String code) {
		return employeeInInterDAO.querNurseDoctor(code);
	}
	
	/**
	 * 根据类型获取员工数据
	 * @param type
	 * @return
	 */
	public List<SysEmployee> getEmpByType(String type){
		List<SysEmployee> list=null;
		try {
			if(StringUtils.isBlank(type)){
				list=(List<SysEmployee>) redis.get("emp_queryAll");
			}else{
				list = (List<SysEmployee>) redis.hget("emp", type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list==null){
			if(StringUtils.isBlank(type)){
				list=employeeInInterDAO.getSysEmployeeInfo();
			}else{
				list = employeeInInterDAO.getEmpByType(type);
			}
			try {
				redis.hset("emp", type, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	@Override
	public SysEmployee getEmpByJobNo(String jobNo) {
		SysEmployee emp=null;
		try {
			emp = (SysEmployee) redis.hget("emp", jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(emp==null){
			emp = employeeInInterDAO.getEmpByJobNo(jobNo);
			try {
				redis.hset("emp", jobNo, emp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return emp;
	}
	
	/**
	 * 获取所有的员工
	 * @return
	 */
	public List<SysEmployee> getEmpInfo(){
		List<SysEmployee> list=null;
		try {
			list = (List<SysEmployee>) redis.get("emp_queryAll");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list==null){
			list= employeeInInterDAO.getSysEmployeeInfo();
		}
		return list;
	}

	@Override
	public List<SysEmployee> getAffuseDoctor(String startPage, String endPage,
			String p) {
		return employeeInInterDAO.getAffuseDoctor(startPage, endPage, p);
	}

	@Override
	public int getAffuseDoctorTotal(String p) {
		return employeeInInterDAO.getAffuseDoctorTotal(p);
	}
	
	/**
	 * 根据员工号获取员工扩展信息
	 * @param jobNo
	 * @return
	 */
	public EmployeeExtend getEmpExtend(String jobNo){
		if(StringUtils.isBlank(jobNo)){
			return null;
		}
		return employeeInInterDAO.getEmp(jobNo);
	}
}
