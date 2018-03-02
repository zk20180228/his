package cn.honry.outpatient.scheduleModel.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.scheduleModel.dao.ScheduleModelDAO;

@Repository("scheduleModelDAO")
@SuppressWarnings({ "all" })
public class ScheduleModelDAOImpl extends HibernateEntityDao<RegisterSchedulemodel> implements ScheduleModelDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<RegisterSchedulemodel> getPage(RegisterSchedulemodel entity,String page, String rows) {
		String hql = joint(entity);
		if(hql==null){
			return new ArrayList<RegisterSchedulemodel>();
		}
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(RegisterSchedulemodel entity) {
		String hql = joint(entity);
		if(hql==null){
			return 0;
		}
		return super.getTotal(hql);
	}
	
	public String joint(RegisterSchedulemodel model){
		String hql="FROM RegisterSchedulemodel s WHERE s.del_flg = 0 ";
		if(model!=null){
			if(model.getModelWeek()!=null){
				hql = hql+" AND s.modelWeek = "+model.getModelWeek()+"";
			}else{
				hql = hql+" AND s.modelWeek = 1 ";
			}
			if(StringUtils.isNotBlank(model.getDepartment())){
				hql = hql+" AND s.modelWorkdept = '"+model.getDepartment()+"' ";
			}
			if(StringUtils.isNotBlank(model.getSearch())){
				hql = hql + "AND (s.modelDoctor IN (SELECT e.jobNo FROM SysEmployee e WHERE e.name LIKE '%"+model.getSearch()+"%' OR e.oldName LIKE '%"+model.getSearch()+"%' OR e.pinyin LIKE '%"+model.getSearch().toUpperCase()+"%' OR e.wb LIKE '%"+model.getSearch().toUpperCase()+"%' OR e.inputCode LIKE '%"+model.getSearch().toUpperCase()+"%') "+
							" OR s.clinic IN (SELECT c.id FROM Clinic c WHERE c.clinicName LIKE '%"+model.getSearch()+"%' OR c.clinicPiyin LIKE '%"+model.getSearch().toUpperCase()+"%' OR c.clinicWb LIKE '%"+model.getSearch().toUpperCase()+"%' OR c.clinicInputcode LIKE '%"+model.getSearch().toUpperCase()+"%'))";
			}
		}
		hql = hql+" ORDER BY s.modelDoctor,s.modelMidday";
		return hql;
	}

	/**  
	 *  
	 * @Description：  根据科室,星期,医生,午别查询该记录是否存在 如果id为空则查询全部符合条件的信息 如果id不为空查询除此id外的全部信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-17 下午04:30:05  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-17 下午04:30:05  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public boolean findModelByWeekAndDoctor(String id,String deptId, Integer modelWeek,String doctorId,Integer midday) {
		String hql = "SELECT count(id) FROM RegisterSchedulemodel r WHERE " +
		" r.modelWeek = "+modelWeek+" AND r.modelDoctor = '"+doctorId+"' AND r.modelMidday = "+midday+" " +
		"AND r.stop_flg = 0 AND r.del_flg = 0";
		if(StringUtils.isNotBlank(id)){
			hql += " AND r.id != '"+id+"'";
		}
		Long count = (Long) getSession().createQuery(hql).uniqueResult();
		if(count>0){
			return true;
		}
		return false;
	}

	/**  
	 *  
	 * @Description：  根据部门和星期查询
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午05:57:43  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午05:57:43  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterSchedulemodel> getScheduleByDeptAndWeek(String deptId,int week,String search) {
		String hql="FROM RegisterSchedulemodel s WHERE s.del_flg = 0  AND s.stop_flg = 0 AND s.modelWorkdept = '"+deptId+"' AND s.modelWeek = "+week+" ";
		if(StringUtils.isNotBlank(search)){
			hql = hql + "AND (s.modelDoctor IN (SELECT e.jobNo FROM SysEmployee e WHERE e.name LIKE '%"+search+"%' OR e.oldName LIKE '%"+search+"%' OR e.pinyin LIKE '%"+search.toUpperCase()+"%' OR e.wb LIKE '%"+search.toUpperCase()+"%' OR e.inputCode LIKE '%"+search.toUpperCase()+"%') "+
			" OR s.clinic IN (SELECT c.id FROM Clinic c WHERE c.clinicName LIKE '%"+search+"%' OR c.clinicPiyin LIKE '%"+search.toUpperCase()+"%' OR c.clinicWb LIKE '%"+search.toUpperCase()+"%' OR c.clinicInputcode LIKE '%"+search.toUpperCase()+"%'))";
		}
		hql = hql+" ORDER BY s.modelDoctor,s.modelMidday";
		List<RegisterSchedulemodel> modelList=super.findByObjectProperty(hql, null);
		if(modelList!=null && modelList.size()>0){
			return modelList;
		}
		return null;
	}

	@Override
	public List<RegisterSchedulemodel> getScheduleByidsAndDeptIdAndRq(String ids,String deptId,int week) {
		ids = ids.replaceAll(",", "','");
		String hql="FROM RegisterSchedulemodel s WHERE s.del_flg = 0  AND s.stop_flg = 0 AND s.id in('"+ids+"') AND s.modelWorkdept = '"+deptId+"' AND s.modelWeek = "+week+"";
		List<RegisterSchedulemodel> modelList=super.findByObjectProperty(hql, null);
		if(modelList!=null && modelList.size()>0){
			return modelList;
		}
		return null;
	}

	/**  
	 *  
	 * @Description：  查询科室下的员工
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> getEmployeeByDeptId(RegisterSchedulemodel model) {
		String hql=" FROM SysEmployee e WHERE (e.deptId='"+model.getSearch()+"' or e.userId in (select t.userId from UserLoginDept t "
				+ "where t.deptId= '"+model.getSearch()+"' )) AND e.stop_flg = 0 AND e.del_flg = 0 ";
		if(model.getModelClass()==1){//查询医师
			hql += " AND e.type= '1' ";
		}
		List<SysEmployee> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}

	@Override
	public List<RegisterSchedulemodel> findOldScheduleModel(String id) {
		String sql = "select t.createtime as createTime from t_register_schedulemodel t where t.model_id ='"+id+"'";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("createTime",Hibernate.TIMESTAMP);
		List<RegisterSchedulemodel> list = query.setResultTransformer(Transformers.aliasToBean(RegisterSchedulemodel.class)).list();
		return list;
	}

}
