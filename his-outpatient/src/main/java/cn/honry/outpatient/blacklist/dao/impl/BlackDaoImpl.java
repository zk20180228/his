package cn.honry.outpatient.blacklist.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.EmployeeBlacklist;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.blacklist.dao.BlackDao;
import cn.honry.outpatient.blacklist.vo.EmployeeBlackListVo;
@Repository("blackDAO")
@SuppressWarnings({"all"})
public class BlackDaoImpl extends HibernateEntityDao<EmployeeBlacklist> implements BlackDao{
	
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<EmployeeBlackListVo> getPage(EmployeeBlacklist entity,SysEmployee sysEmployee,String page, String rows) {
		int p = StringUtils.isNotBlank(page)?Integer.parseInt(page):0; 
		int r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):0; 
		int start = (p-1)*r;
		int end = p*r;
		String name=sysEmployee.getName();
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.BLACKLIST_ID as blackListid, t.BLACKLIST_REASON as reason,e.EMPLOYEE_ID as employeeid,e.EMPLOYEE_NAME as employeeName,e.EMPLOYEE_OLDNAME as employeeOldName ");
		sb.append(" from t_Employee_Blacklist t join T_EMPLOYEE e on t.EMPLOYEE_ID = e.EMPLOYEE_ID ");
		sb.append(" where e.del_flg = 0  and e.stop_flg=0 and t.del_flg = 0  and t.stop_flg=0 ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" and (e.EMPLOYEE_JOBNO like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_NAME like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_PINYIN like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_WB like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_INPUTCODE like '%").append(name).append("%') ");
		}
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		query.addScalar("reason").addScalar("employeeid").addScalar("employeeName").addScalar("employeeOldName").addScalar("blackListid");
		List<EmployeeBlackListVo> list = query.setResultTransformer(Transformers.aliasToBean(EmployeeBlackListVo.class)).setMaxResults(end).setFirstResult(start).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<EmployeeBlackListVo>();
	}

	@Override
	public int getTotal(EmployeeBlacklist entity,SysEmployee sysEmployee) {
		String name=sysEmployee.getName();
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.BLACKLIST_ID as blackListid, t.BLACKLIST_REASON as reason,e.EMPLOYEE_ID as employeeid,e.EMPLOYEE_NAME as employeeName,e.EMPLOYEE_OLDNAME as employeeOldName ");
		sb.append(" from t_Employee_Blacklist t join T_EMPLOYEE e on t.EMPLOYEE_ID = e.EMPLOYEE_ID ");
		sb.append(" where e.del_flg = 0  and e.stop_flg=0 and t.del_flg = 0  and t.stop_flg=0 ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" and (e.EMPLOYEE_JOBNO like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_NAME like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_PINYIN like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_WB like '%").append(name).append("%' ");
			sb.append(" or e.EMPLOYEE_INPUTCODE like '%").append(name).append("%') ");
		}
		return super.getSqlTotal(sb.toString());
	}

	public String joint(EmployeeBlacklist entity,SysEmployee employeeSearch){
 		String hql="FROM EmployeeBlacklist e WHERE  e.del_flg = 0  and e.stop_flg=0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(employeeSearch.getName())){
				String name=employeeSearch.getName();
				hql += " AND (upper(e.dmployeeId.name) LIKE '%"+name+"%'"
				  + " OR upper(e.dmployeeId.oldName) LIKE '%"+name+"%'"
				  + " OR upper(e.dmployeeId.pinyin) LIKE '%"+name.toUpperCase()+"%'" 
				  + " OR upper(e.dmployeeId.wb) LIKE '%"+name.toUpperCase()+"%'"
				  + " OR upper(e.dmployeeId.inputCode) LIKE '%"+name+"%')";
			}	
			if(entity.getDmployeeId()!=null){
				hql = hql+" AND e.dmployeeId LIKE '%"+entity.getDmployeeId()+"%'";
			}
			
		}
		return hql;
	}
	/**
	 * @Description：  获取下拉员工名称
	 * @Author：lyy
	 * @CreateDate：2015-11-20 下午05:50:42  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-20 下午05:50:42  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> queryEmpName(String name) {
		String hql="from SysEmployee e where e.del_flg = 0 and e.type='3' ";   //9--财会人员
		if(StringUtils.isNotBlank(name)){
			hql=hql+"AND (e.name LIKE '%"+name.toUpperCase()+"%' or e.jobNo LIKE '%"+name.toUpperCase()+"%' or e.pinyin LIKE '%"+name.toUpperCase()+"%' or e.wb LIKE '%"+name.toUpperCase()+"%' or e.inputCode LIKE '%"+name.toUpperCase()+"%' )";
		}
		List<SysEmployee> empList=super.find(hql, null);
		if(empList!=null&&empList.size()>0){
			return empList;
		}
		
		return new ArrayList<SysEmployee>();
	}

}
