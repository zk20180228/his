package cn.honry.inpatient.permission.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPermission;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.permission.dao.PermissionDao;
@Repository("permissionDao")
@SuppressWarnings({ "all" })
public class PermissionDaoImpl extends HibernateEntityDao<InpatientPermission> implements PermissionDao {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * @Description：通过住院流水号查询医嘱授权历史记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientPermission> queryListById(String no) {
		String hql=" from InpatientPermission s where s.del_flg = 0  and s.inpatientNo = ?";
		List<InpatientPermission> inpatientPermission =super.find(hql, no);
		if(inpatientPermission!=null && inpatientPermission.size()>0){
			return inpatientPermission;
		}
		return new ArrayList<InpatientPermission>();
	}
	/**  
	 * @Description：通过Id删除医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 上午11:50  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public void delById(String[] idsArr,String userId) {
		for(int i=0;i<idsArr.length;i++){
			int c = getSession().createQuery("UPDATE "+entityClass.getSimpleName()+" d SET del_flg = 1 ,updateUser = ? ,updateTime = ?  WHERE d.id ='"+idsArr[i]+"' ")
					.setParameter(0, userId)
					.setTimestamp(1, new Date())
					.executeUpdate();
		}
	}
	/**  
	 * @Description：通过病历号 查询医嘱授权记录信息
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 15 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientPermission queryPermissionById(String inpatientNo) {
		String hql="from InpatientPermission i where i.del_flg = 0 and i.id = ?";
		List<InpatientPermission> inpatientPermission=super.find(hql, inpatientNo);
		if(inpatientPermission!=null && inpatientPermission.size()>0){
			return inpatientPermission.get(0);
		}
		return new InpatientPermission();
	}
	@Override
	public List<InpatientInfoNow> queryByMedicall(String medicalNo) {
		String hql="FROM InpatientInfoNow i WHERE (i.medicalrecordId = :medicalNo  or i.inpatientNo = :medicalNo)  ";
		List<InpatientInfoNow> iList=this.getSession().createQuery(hql).setParameter("medicalNo",medicalNo).list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public String queryDeptType(String id) {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0 and id='"+id+"'";
		List<SysDepartment> sysdeptList=super.find(hql, null);
		if(sysdeptList!=null&&sysdeptList.size()>0){
			if("I".equals(sysdeptList.get(0).getDeptType())){
				return "2";
			}else if("R".equals(sysdeptList.get(0).getDeptType())){
				return "1";
			}else{
				return "3";
			}
		}else{
			return "3";
		}
	}
	@Override
	public List<SysDepartment> queryDeptByType(String state, String q) {
		StringBuffer hql=new StringBuffer();
		hql.append(" from SysDepartment where del_flg=0 and stop_flg=0  ");
		if("1".equals(state)){
			hql.append(" and deptType ='C' ");
		}else if("2".equals(state)){
			hql.append(" and deptType ='I' ");
		}else{
			hql.append(" and deptType in ('C','I') ");
		}
		
		if(StringUtils.isNotBlank(q)){
			hql.append(" and (deptName like :q or deptCode like :q or deptPinyin like :q "
					+ "or deptWb like :q or deptInputcode like :q ) ");
		}
		Query query=this.getSession().createQuery(hql.toString());
		if(StringUtils.isNotBlank(q)){
			query.setParameter("q", "%"+q+"%");
		}
		List<SysDepartment> sysdeptl=query.list();
		if(sysdeptl!=null&&sysdeptl.size()>0){
			return sysdeptl;
		}
		return new ArrayList<SysDepartment>();
	}
	@Override
	public Patient queryPatientInfo(String mid) {
		String hql="from Patient where del_flg=0 and stop_flg=0 and medicalrecordId like '%"+mid+"'";
		List<Patient> pl=super.find(hql, null);
		if(pl!=null&&pl.size()>0){
			return pl.get(0);
		}
		return new Patient();
	}
	@Override
	public List<SysEmployee> queryLoginUserDept(String departmentId) {
		String sql="select t.EMPLOYEE_JOBNO as jobNo,t.EMPLOYEE_NAME as name "
				+ "from T_EMPLOYEE t where t.del_flg=0 and t.stop_flg=0";
		 SQLQuery queryObject = this.getSession().createSQLQuery(sql);
		 queryObject.addScalar("jobNo").addScalar("name");
		 List<SysEmployee> sysempl=queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(sysempl!=null&&sysempl.size()>0){
			return sysempl;
		}
		return new ArrayList<SysEmployee>();
	}
	@Override
	public int getQueryListByIdCount(String inno) {
		String hql="from InpatientPermission s where s.del_flg = 0  and s.inpatientNo = '"+inno+"'";
		return super.getTotal(hql.toString());
	}
	@Override
	public List<SysEmployee> employeeCombobox(String departmentId, String q) {
		StringBuffer sql=new StringBuffer();
		sql.append("select e.EMPLOYEE_JOBNO as jobNo,e.EMPLOYEE_NAME as name from T_EMPLOYEE e "
				+ "where e.del_flg=0 and e.stop_flg=0 ");
		if(StringUtils.isNotBlank(departmentId)){
			sql.append(" and e.DEPT_ID=:deptCode");
		
		}
		if(StringUtils.isNotBlank(q)){
			sql.append("  and (EMPLOYEE_NAME like :q or EMPLOYEE_JOBNO like :q "
					+ "or EMPLOYEE_OLDNAME like :q or EMPLOYEE_PINYIN like :q or EMPLOYEE_WB like :q "
					+ "or EMPLOYEE_INPUTCODE like :q ) ");
		}
		
		sql.append("  and e.EMPLOYEE_TYPE in (1)");
		SQLQuery queryObject=this.getSession().createSQLQuery(sql.toString()).addScalar("jobNo").addScalar("name");
		
		if(StringUtils.isNotBlank(departmentId)){
			queryObject.setParameter("deptCode", departmentId);
		}
		if(StringUtils.isNotBlank(q)){
			queryObject.setParameter("q", "%"+q+"%");
		}
		List<SysEmployee> emplist=queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(emplist!=null&&emplist.size()>0){
			return emplist;
		}
		return new ArrayList<SysEmployee>();
	}
	
}
