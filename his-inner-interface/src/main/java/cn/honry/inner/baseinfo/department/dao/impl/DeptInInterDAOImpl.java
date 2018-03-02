package cn.honry.inner.baseinfo.department.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FictitiousContact;
import cn.honry.base.bean.model.FictitiousDept;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.vo.FicDeptVO;
import cn.honry.utils.HisParameters;

/**  
 *  
 * 内部接口：科室 
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("deptInInterDAO")
@SuppressWarnings({ "all" })
public class DeptInInterDAOImpl extends HibernateEntityDao<SysDepartment> implements DeptInInterDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 *
	 * <li>根据科室获得关联的病区（护士站）
	 * <li>参数科室本身不能为病区
	 * <li>如果该科室关联多个病区默认取第一个做为登录病区
	 * <li>如果该科室没有关联病区则返回null
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月21日 上午9:57:55 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param id 科室id 该科室不能为病区
	 * @return：病区
	 *
	 */
	@Override
	public SysDepartment getNursingStationByLoginDeptId(String id) {
		StringBuffer sb = new StringBuffer("FROM SysDepartment d WHERE d.id IN ");
		sb.append("(SELECT c.deptId FROM DepartmentContact c WHERE c.id IN ");
		sb.append("(SELECT dc.pardeptId FROM DepartmentContact dc WHERE dc.deptId = ? AND dc.referenceType = '03' AND dc.stop_flg = 0 AND dc.del_flg = 0) ");
		sb.append("AND c.referenceType = '03' AND c.stop_flg = 0 AND c.del_flg = 0)");
		List<SysDepartment> deptList = this.find(sb.toString(), id);
		if(deptList!=null&&deptList.size()>0){
			return deptList.get(0);
		}
		return null;
	}
	
	/**  
	 * 获得登录用户关联的科室
	 * @Description:
	 * @Author：aizhonghua
     * @CreateDate：2016-4-20 下午09:44:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-4-20 下午09:44:35  
	 * @ModifyRmk：  新增默认关联员工的所属科室
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> findDeptsByUserId(String userId) {
		String dhql = "FROM SysDepartment d WHERE d.id = (SELECT e.deptId FROM SysEmployee e WHERE e.userId = '"+userId+"') and d.stop_flg!=1 and d.del_flg!=1";
		List<SysDepartment> dlist= getSession().createQuery(dhql).list();
		String hql="select deptId from UserLoginDept t where t.userId='"+userId+"'";
		List<SysDepartment> list= getSession().createQuery(hql).list();
		if(list==null){
			list=new ArrayList<SysDepartment>();
		}
		list.removeAll(dlist);
		list.addAll(dlist);
		return list;
	}
	
	/**  
	 * 获得部门树
	 * @Description:
	 * @Author：aizhonghua
     * @CreateDate：2016-4-20 下午09:44:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-4-20 下午09:44:35  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> findTree(boolean b, String deptType) {
		String hql="";
		if(StringUtils.isNotBlank(deptType)){
			deptType=deptType.replaceAll(",", "','");
			hql="FROM SysDepartment d WHERE d.del_flg = '0' and d.deptType in ('"+ deptType +"') ";
		}else{
			hql="FROM SysDepartment d WHERE d.del_flg = '0' ";
		}
		hql = hql+" ORDER BY d.deptType,d.deptPath";
		List<SysDepartment> departmentList=super.findByObjectProperty(hql, null);
		if(departmentList!=null && departmentList.size()>0){
			return departmentList;
		}
		return new ArrayList<SysDepartment>();
	}
	
	/**  
	 *  
	 * @Description：角色用户--科室
	 * @Author：wujiao
	 * @CreateDate：2015-8-11 下午01:32:08 
	 * @Modifier：wujiao
	 * @ModifyDate：2015-8-11 下午01:32:08  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override 
	public List<SysDepartment> findDeptTree(String parentId ) {
		String hql="from SysDepartment d where 1=1 and d.del_flg= 0 and d.stop_flg= 0";
		if(StringUtils.isNotBlank(parentId)){
			hql+=" and d.deptType ='"+parentId+"'";
		}
		List<SysDepartment> deptList=super.findByObjectProperty(hql, null);
		
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}	
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<SysDepartment> getDeptList(String deptName) {
		String hql="from SysDepartment as d where d.stop_flg=0 and d.del_flg=0 ";
		if(StringUtils.isNotBlank(deptName)){
			hql+= " and (d.deptName LIKE '%"+deptName+"%' or d.deptCode like '%"+deptName+"%' "
					+ " or d.deptPinyin like '%"+deptName+"%' or d.deptWb like '%"+deptName+"%' "
					+ " or d.deptInputcode like '%"+deptName+"%')";
		}
				
		List<SysDepartment> deptList=super.findByObjectProperty(hql, null);
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}	
		return new ArrayList<SysDepartment>();
	}
	

	/**  
	 *  
	 * @Description：  获得门诊科室
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> treeDeptSchedule() {
		String hql="FROM SysDepartment d WHERE d.stop_flg=0 AND d.del_flg=0 ORDER BY d.deptType,d.deptPath";
		List<SysDepartment> deptList=super.findByObjectProperty(hql, null);
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}	
		return null;
	}

	/**  
	 *  
	 * @Description：  查询科室
	 * @Author：zhangjin
	 * @CreateDate：2016-7-18 
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getDept() {
		String hql=" from SysDepartment where stop_flg=0 and del_flg=0";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<SysDepartment> queryAllDept() {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> dl=super.find(hql, null);
		if(dl!=null&&dl.size()>0){
			return dl;
		}
		return null;
	}

	/**
	 * 根据科室编码查询 科室信息
	 * @param code 科室编码
	 * @return
	 */
	public SysDepartment getByCode(String code){
		SysDepartment sysDepartment = findUniqueBy("deptCode", code);
		return sysDepartment;
	}
	@Override
	public SysDepartment getDeptCode(String deptCode) {
		String hql="from SysDepartment t where t.del_flg=0 and t.stop_flg=0 and t.deptCode= ? ";
		List<SysDepartment> list=this.find(hql, deptCode);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据科室编码、名称、五笔、拼音等及时查询科室信息
	 * @param q
	 * @return
	 */
	public List<SysDepartment> getDeptByQ(String q){
		
		String sql="select t.dept_code as deptCode,t.dept_name as deptName from t_department t where t.del_flg=0 and t.stop_flg=0 ";
		if(StringUtils.isNotBlank(q)){
			sql+=" and (t.dept_code like '%"+q+"%' or t.dept_name like '%"+q+"%' or t.dept_wb like '%"+
		    q.toUpperCase()+"%' or t.dept_pinyin like '%"+q.toUpperCase()+"%' or t.dept_inputcode like '%"+
			q.toUpperCase()+"%')";
		}
		SQLQuery query = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("deptName");
		List<SysDepartment> list = query.setResultTransformer(Transformers.aliasToBean(entityClass)).list();
		return list;
	}

	/**
	 * 判断该账户是否有关联科室
	 * @param q
	 * @return
	 */
	@Override
	public boolean isHaveDept(final String account) {
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(DEPT_ID) FROM( ");
		sb.append("SELECT COUNT(e.DEPT_ID) DEPT_ID FROM ")
		.append(HisParameters.HISPARSCHEMAHISUSER).append("T_EMPLOYEE e WHERE e.USER_ID = (SELECT u.USER_ID FROM ")
		.append(HisParameters.HISPARSCHEMAHISUSER).append("T_SYS_USER u WHERE u.USER_ACCOUNT = :account) ");
		sb.append("UNION ALL ");
		sb.append("SELECT COUNT(t.DEPT_ID) DEPT_ID FROM ")
		.append(HisParameters.HISPARSCHEMAHISUSER).append("T_SYS_USER_LOGINDEPT t where t.USER_ID = (SELECT u.USER_ID FROM ")
		.append(HisParameters.HISPARSCHEMAHISUSER).append("T_SYS_USER u WHERE u.USER_ACCOUNT = :account) ");
		sb.append(") ");
		Object count = this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				
				return queryObject.setParameter("account", account).uniqueResult();
			}
		});
		if(count!=null&&Integer.parseInt(count.toString())>0){
			return true;
		}
		return false;
	}

	@Override
	public List<SysDepartment> getPageByQ(String page, String rows, String q) {
		String hql = joint(q);
		return super.getPage(hql, page, rows);
	}


	@Override
	public int getTotalByQ(String q) {
		String hql = joint(q);
		return super.getTotal(hql);
	}
	/** 拼接科室查询条件 通过编码、拼音码、五笔码、自定义码、科室名、英文名、部门简称模糊查询
	* @Title: joint 
	* @Description: 拼接科室查询条件 通过编码、拼音码、五笔码、自定义码、科室名、英文名、部门简称模糊查询 
	* @param q 查询条件
	* @author dtl 
	* @date 2017年2月8日
	*/
	public String joint(String q){
		StringBuffer hqlbBuffer = new StringBuffer("FROM SysDepartment d WHERE d.del_flg = 0 ");
		if(StringUtils.isNotBlank(q)){
			hqlbBuffer.append(" AND (d.deptCode LIKE '%" + q.toUpperCase() + "%'");
			hqlbBuffer.append(" OR d.deptPinyin LIKE '%" + q.toUpperCase() + "%'");
			hqlbBuffer.append(" OR d.deptWb LIKE '%" + q.toUpperCase() + "%'");
			hqlbBuffer.append(" OR d.deptInputcode LIKE '%" + q.toUpperCase() + "%'");
			hqlbBuffer.append(" OR d.deptName LIKE '%" + q + "%'");
			hqlbBuffer.append(" OR d.deptEname LIKE '%" + q + "%'"); 
			hqlbBuffer.append(" OR d.deptBrev LIKE '%" + q + "%')");
			}
		return hqlbBuffer.toString();
	}

	@Override
	public List<FictitiousDept> findFicDept(boolean flag, String deptTpye,Integer deptDistrict,String hospitalId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from FictitiousDept where del_flg = 0 and stop_flg=0 ");
		if(StringUtils.isNotBlank(deptTpye)){
			deptTpye=deptTpye.replaceAll(",", "','");
			sb.append(" and deptType in ('"+deptTpye+"')");
		}
		if(StringUtils.isNotBlank(hospitalId)){
			sb.append(" and hospitalId = '"+hospitalId+"' ");
		}
		if(deptDistrict!=null){
			sb.append(" and deptDistrict = "+deptDistrict+" ");
		}
		List<FictitiousDept> list = this.find(sb.toString(), null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<FictitiousDept>();
	}

	@Override
	public List<FictitiousContact> findFicConDept(boolean flag, String deptTpye,String fictCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from FictitiousContact where del_flg = 0 and stop_flg=0 ");
		if(StringUtils.isNotBlank(deptTpye)){
			deptTpye=deptTpye.replaceAll(",", "','");
			sb.append(" and type = '"+deptTpye+"'");
		}
		if(StringUtils.isNotBlank(fictCode)){
			sb.append(" and fictCode = '"+fictCode+"' ");
		}
		List<FictitiousContact> list = this.find(sb.toString(), null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<FictitiousContact>();
	}
	
}
