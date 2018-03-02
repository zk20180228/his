package cn.honry.inpatient.cordon.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.cordon.dao.CordonDAO;
import cn.honry.inpatient.cordon.vo.CordonVo;
import cn.honry.utils.HisParameters;
/**
 * 护士站患者警戒线DAO实现类
 * @author  lyy
 * @createDate： 2016年4月1日 下午5:47:30 
 * @modifier lyy
 * @modifyDate：2016年4月1日 下午5:47:30
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Repository(value="cordonDAO")
@SuppressWarnings({"all"})
public class CordonDAOImpl  extends HibernateEntityDao<InpatientInfo> implements CordonDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 病区与护士站树
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午4:22:31 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午4:22:31 
	 * @param deptId 护士站病区Id 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> findTree(String deptId) {
		String hql ="From SysDepartment where deptType='N' and del_flg=0 and stop_flg=0 and id in (?)";
		List<SysDepartment> listDeptContact=super.find(hql, deptId);
		if(listDeptContact!=null&&listDeptContact.size()>0){
			return listDeptContact;
		}
		return new ArrayList<SysDepartment>();
	}
	/**
	 * 连表查询数据(总条数)
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:14:43 
	 * @modifier lyy,liujl
	 * @modifyDate：2015年12月15日 下午5:14:43  2016-6-6 上午09:56:35  
	 * @param entity 患者警戒线虚拟实体  deptId 登录科室 修改获取记录总条数方法
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotal(CordonVo entity,String deptId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select info.inpatient_id as id "
				+ "from t_inpatient_info_now info "
				+ " where info.nurse_cell_code ='"+deptId+"' and info.in_state='I'");
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getPatientName())){
				sql.append(" and (info.idcard_no LIKE '%"+entity.getPatientName()+"%' or info.PATIENT_NAME like '%"+entity.getPatientName()+"%'" +
						" or info.Medicalrecord_Id LIKE '%"+entity.getPatientName()+"%' "+
						" or info.inpatient_no LIKE '%"+entity.getPatientName()+"%' "+
						" or info.bedinfo_id LIKE '%"+entity.getPatientName()+"%' or info.CERTIFICATES_NO like '%"+entity.getPatientName()+"%')");
			}
		}
		
		return super.getSqlTotal(sql.toString());
	}
	/**
	 * 连表查询分页list数据
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:13:11 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午5:13:11  
	 * @param entity 患者警戒线虚拟实体  page 当前页   rows 当前页条数  deptId 登录科室
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<CordonVo> getPage(CordonVo entity, String page, String rows,String deptId) {
		StringBuilder sql=this.jsonSql(entity, deptId);
		String patientName = entity.getPatientName();
		Query query = this.getSession().createSQLQuery(sql.toString());
		((SQLQuery) query).addScalar("id").addScalar("inpatientNo").addScalar("patientName").addScalar("deptCode").addScalar("deptName").
		addScalar("nurseCellCode").addScalar("nurseCellName").addScalar("alterType").addScalar("moneyAlert",Hibernate.DOUBLE).
		addScalar("alterBegin",Hibernate.TIMESTAMP).addScalar("alterEnd",Hibernate.TIMESTAMP).
		addScalar("totCost",Hibernate.DOUBLE).addScalar("freeCost",Hibernate.DOUBLE).addScalar("prepayCost",Hibernate.DOUBLE)
		.addScalar("bedName").setParameter("deptId", deptId);	
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getPatientName())){
				query.setParameter("patientName","%"+patientName+"%");
			}
		}
		query.setResultTransformer(Transformers.aliasToBean(CordonVo.class));
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<CordonVo> list = query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<CordonVo>();
	}
	/**
	 * 患者警戒线（护士站的）查询方法
	 * @author  lyy
	 * @createDate： 2016年4月28日 下午3:15:50 
	 * @modifier lyy
	 * @modifyDate：2016年4月28日 下午3:15:50
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	private StringBuilder jsonSql(CordonVo entity,String deptId){
		StringBuilder sql = new StringBuilder();
		String patientName = entity.getPatientName();
		sql.append("select info.inpatient_id as id,"
				+ "info.medicalrecord_id as inpatientNo,"
				+ "info.patient_name as patientName,"
				+ "info.dept_code as deptCode,"
				+ "info.dept_name as deptName,"
				+ "info.nurse_cell_code as nurseCellCode,"
				+ "info.nurse_cell_name as nurseCellName,"
				+ "info.alter_type as alterType,"
				+ "info.money_alert as moneyAlert,"
				+ "info.alter_begin as alterBegin,"
				+ "info.alter_end as alterEnd,"
				+ "nvl(info.tot_cost,0) as totCost,"
				+ "nvl(info.free_cost,0) as freeCost,"
				+ "nvl(info.prepay_cost,0.00) as prepayCost,info.BED_NAME as bedName "
				+ "from t_inpatient_info_now info "
				+ " where info.nurse_cell_code =:deptId and info.in_state ='I'");
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getPatientName())){
				sql.append(" and (info.idcard_no LIKE :patientName or info.PATIENT_NAME like :patientName" +
						" or info.Medicalrecord_Id LIKE :patientName "+
						" or info.inpatient_no LIKE :patientName "+
						" or info.bedinfo_id LIKE :patientName or info.CERTIFICATES_NO like :patientName)");
			}
		}
		return sql;
	}
	/**
	 * 根据患者id去查住院主表的信息
	 * @author  lyy
	 * @createDate： 2016年4月1日 下午5:46:49 
	 * @modifier lyy
	 * @modifyDate：2016年4月1日 下午5:46:49 
	 * @param：   id 患者主键
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientInfoNow getInpatientInfo(String id) {
		return this.getHibernateTemplate().get(InpatientInfoNow.class, id);
	}
	/**
	 * 根据病区id统计该病区下的患者
	 * @author  lyy
	 * @createDate： 2016年4月1日 下午5:45:24 
	 * @modifier lyy,liujl
	 * @modifyDate：2016年4月1日 下午5:45:24 2016-6-6 上午09:56:35  
	 * @param： id 病区主键 id 
	 * @modifyRmk：  修改获取记录总条数方法
	 * @version 1.0
	 */
	@Override
	public int countDept(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select info.patient_name as pName "
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info_now info "
				+ " where info.nurse_cell_code='"+id+"' and info.in_state='I'	");
		return super.getSqlTotal(sql.toString());
	}
}
