package cn.honry.inner.baseinfo.employee.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import freemarker.template.utility.StringUtil;

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
@Repository("employeeInInterDAO")
@SuppressWarnings({ "all" })
public class EmployeeInInterDAOImpl extends HibernateEntityDao<SysEmployee> implements EmployeeInInterDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
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
	public List<SysDepartment> findEmployeesByUserId(String userId) {
		String hql = "select deptId from SysEmployee r where r.userId='"+userId+"'";
		List<SysDepartment> list = this.find(hql, null);
		return list==null?new ArrayList<SysDepartment>():list;
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
	public List<SysEmployee> findEmpByUserId(String userId) {
		String hql = "from SysEmployee r where r.del_flg=0 and r.stop_flg=0 and r.userId='"+userId+"'";
		List<SysEmployee> list = super.find(hql, null);
		return list==null?new ArrayList<SysEmployee>():list;
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
		String sql = "select t.employee_jobno as jobNo,t.employee_name as name,t.employee_sex as sex,t.employee_birthday as birthday,t.WAGES_ACCOUNT as wagesAccount,"
				+ "t1.code_name as education,t.employee_mobile as mobile from t_employee t LEFT JOIN T_BUSINESS_DICTIONARY t1 ON t.employee_education = t1.CODE_ENCODE AND T1.CODE_TYPE = 'education' where t.employee_jobno = '"+userCode+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("jobNo").addScalar("name")
				.addScalar("sex").addScalar("birthday").addScalar("wagesAccount").addScalar("education").addScalar("mobile");
		List<SysEmployee> list = queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		return (list==null||list.size()==0)?null:list.get(0);
	}
	
	/**
	 * 查询财务部下的具有核算权利的员工
	 * @author  kjh
	 * @date 2015-06-10
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> findFianceEmployee(String page,String rows,SysEmployee entity) {
		String hql = jointFiance(entity);
		return super.getPage(hql, page, rows);
	}
	public String jointFiance(SysEmployee entity){
			String  hql = "FROM SysEmployee where type in ('9') ";
		return hql;
	}
	/**
	 * 查询财务部下的具有核算权利的员工(分页页数)
	 * @author  kjh
	 * @date 2015-06-10
	 * @version 1.0
	 */
	@Override
	public int getFianceEmployeeTotal(SysEmployee entity) {
		String hql = jointFiance(entity);
		return super.getTotal(hql);
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
		String hql="from SysEmployee r where  r.stop_flg = 0 AND r.del_flg = 0  ";
		if(StringUtils.isNotBlank(no)){
			hql+=" AND r.deptId='"+no+"'";
		}
		List<SysEmployee> list = super.find(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}
	
	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> getPagebyhql(String hql, String page, String rows) {
		return super.getPage(hql, page, rows);
	}
	
	@Override
	public SysEmployee queryByUserId(String userid) {
		String hql="FROM SysEmployee e WHERE e.del_flg = '0' AND e.userId.id ='"+userid+"'";
		List<SysEmployee> employeeList=super.findByObjectProperty(hql, null);
		if(employeeList!=null && employeeList.size()>0){
			return employeeList.get(0);
		}
		return new SysEmployee();
	}

	@Override
	public String searchtree(String searcht) {
		String hql=" FROM SysDepartment d WHERE d.del_flg = '0' and (upper(d.deptPinyin) like '%"+searcht.toUpperCase()+"%' or upper(d.deptWb) like '%"+searcht.toUpperCase()+"%' or upper(d.deptInputcode) like '%"+searcht.toUpperCase()+"%' or upper(d.deptName) like '%"+searcht.toUpperCase()+"%')";
		hql+=" order by abs(length(d.deptName) - length('"+searcht+"')),abs(length(d.deptWb) - length('"+searcht+"')),"
				+ "abs(length(d.deptPinyin) - length('"+searcht+"')),abs(length(d.deptInputcode) - length('"+searcht+"'))";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return "";
		}
		return departmentList.get(0).getId();
	}

	@Override
	public SysEmployee findEmpById(String id) {
		String hql="from SysEmployee r where r.id='"+id+"' AND r.stop_flg = 0 AND r.del_flg = 0  ";
		List<SysEmployee> list = super.find(hql, null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return new SysEmployee();
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
		String hql="from SysEmployee a where a.stop_flg=0 and a.del_flg=0 and a.code='"+code+"'";
		List<SysEmployee> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList.get(0);
		}
		return new SysEmployee();
	}
	
	
	/**
	 * 员工业务接口：根据accunt查询员工
	 * @author zhuxiaolu
	 * @createDate： 2016年4月12日 上午11:22:48 
	 * @modifier zhuxiaolu
	 * @modifyDate：2016年4月12日 上午11:22:48
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> findEmpByUserAccunt(String accunt) {
		String hql = "from SysEmployee r where r.del_flg=0 and r.stop_flg=0 and r.jobNo='"+accunt+"'";
		List<SysEmployee> list = super.find(hql, null);
		return list==null?new ArrayList<SysEmployee>():list;
	}

	@Override
	public SysEmployee getEmpByJobNo(String jobNo) {
		String hql="select a.id as id, a.code as code,a.jobNo as jobNo,a.name as name,a.pinyin as pinyin,a.wb as wb,"
				+ "a.inputCode as inputCode from SysEmployee a where a.stop_flg=0 and a.del_flg=0 and a.jobNo= ?";
		List<SysEmployee> iList = this.createQuery(hql, jobNo)
				.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList.get(0);
		}
		return new SysEmployee();
	}
	
	/**
	 * 根据类型获取员工数据
	 * @param type
	 * @return
	 */
	public List<SysEmployee> getEmpByType(String type){
		String hql=" select a.id as id, a.code as code,a.jobNo as jobNo,a.name as name,a.pinyin as pinyin,"
				+ "a.wb as wb,a.inputCode as inputCode,a.type as type from SysEmployee a where a.stop_flg=0 and a.del_flg=0 and a.type=:type";
		List<SysEmployee> list = this.createQuery(hql).setParameter("type", type)
				.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		return list;
	}
	
	/**
	 * 获取员工数据
	 * @return
	 */
	public List<SysEmployee> getSysEmployeeInfo(){
		String hql="select t.id as id, t.code as code,t.jobNo as jobNo,t.name as name,t.pinyin as pinyin,t.wb as wb,"
				+ "t.inputCode as inputCode,t.type as type from SysEmployee t where t.stop_flg = 0 and t.del_flg = 0 ";
		List<SysEmployee> list = this.createQuery(hql)
				.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		return list;
	}
	
	@Override
	public List<SysEmployee> getAffuseDoctor(String startPage, String endPage, String p) {
		String hql="from SysEmployee where del_flg=0 and stop_flg=0 " ;
		if(StringUtils.isNotBlank(p)){
			p=p.toUpperCase();
			hql += "and (upper(userId) like '%" + p + "%' or name like '%" + p + "%' or pinyin like '%"+p+"%' or wb like '%"+p+"%' or upper(inputCode) like '%"+p+"%' or upper(jobNo) like '%"+p+"%')";
		}
		List<SysEmployee> sysEmployeeList = super.getPage(hql, endPage, startPage);
		if(sysEmployeeList==null||sysEmployeeList.size()<=0){
			return new ArrayList<SysEmployee>();
		}
		return sysEmployeeList;
	}

	@Override
	public int getAffuseDoctorTotal(String p) {
		String hql="from SysEmployee where del_flg=0 and stop_flg=0 " ;
		if(StringUtils.isNotBlank(p)){
			p=p.toUpperCase();
			hql += "and (upper(userId) like '%" + p + "%' or name like '%" + p + "%' or pinyin like '%"+p+"%' or wb like '%"+p+"%' or upper(inputCode) like '%"+p+"%' or upper(jobNo) like '%"+p+"%')";
		}
		return super.getTotal(hql);
	}
	
	/**
	 * 根据员工号获取员工扩展信息
	 * @param jobNo
	 * @return
	 */
	public EmployeeExtend getEmp(String jobNo){
		String hql="from EmployeeExtend e where e.stop_flg=0 and e.del_flg=0 and e.employeeJobNo=?";
		List<EmployeeExtend> list = this.createQuery(hql, jobNo).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
