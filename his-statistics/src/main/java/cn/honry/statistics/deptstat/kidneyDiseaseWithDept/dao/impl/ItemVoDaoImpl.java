package cn.honry.statistics.deptstat.kidneyDiseaseWithDept.dao.impl;

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

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.statistics.deptstat.internalCompare2.vo.FicDeptVO;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.dao.ItemVoDao;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.ItemVo;

@Repository("itemVoDao")
@SuppressWarnings({ "all" })
public class ItemVoDaoImpl extends HibernateEntityDao<ItemVo> implements ItemVoDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 
	 * 通过科室查询内容
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月2日 下午8:21:57 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月2日 下午8:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ItemVo quertItemVo(List<String> tnLs,final String begin,final String end,String deptCode) {
		if(tnLs==null || tnLs.size()==0){
			return new ItemVo();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" (select count(1) from "+tnLs.get(0)+" t where");
		sb.append(" t.out_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.out_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append("and t.dept_code ='"+deptCode+"' ");
		}
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 ) as chuYUNum,");
		sb.append(" (select count(1) from "+tnLs.get(0)+" t where");
		sb.append(" t.in_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.in_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append("and t.dept_code ='"+deptCode+"' ");
		}
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 ) as ruYuNum,");
		sb.append(" (select nvl(avg(trunc(t.out_date) - trunc(t.in_date)),0) from "+tnLs.get(0)+" t where");
		sb.append(" t.out_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.out_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append("and t.dept_code ='"+deptCode+"' ");
		}
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 ) as avgInYuDays,");
		sb.append(" (select count(1) from t_business_hospitalbed t where t.stop_flg = 0 and t.del_flg = 0 ) as beds,");
		sb.append(" (select count(1) from t_business_hospitalbed t where t.bed_state = '4' and t.stop_flg = 0 and t.del_flg = 0 ) as bedUsed,");
		sb.append(" (select count(1) from "+tnLs.get(1)+" t where");
		sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append("and t.dept_code ='"+deptCode+"' ");
		}
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 ) as workNum,");
		sb.append(" (select nvl(sum(t.tot_cost),0) from "+tnLs.get(2)+" t where");
		sb.append(" t.BALANCE_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.BALANCE_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append("and t.INHOS_DEPTCODE ='"+deptCode+"' ");
		}
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 ) as zhuCost,");
		sb.append(" (select nvl(sum(t.tot_cost),0) from "+tnLs.get(3)+" t where");
		sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append("and t.DOCT_DEPT ='"+deptCode+"' ");
		}
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 ) as menCost from dual");
		ItemVo vo = (ItemVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public ItemVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("ruYuNum",Hibernate.INTEGER)
						   .addScalar("chuYUNum",Hibernate.INTEGER)
						   .addScalar("beds",Hibernate.INTEGER)
						   .addScalar("bedUsed",Hibernate.INTEGER)
						   .addScalar("avgInYuDays",Hibernate.DOUBLE)
						   .addScalar("workNum",Hibernate.INTEGER)
						   .addScalar("menCost",Hibernate.DOUBLE)
						   .addScalar("zhuCost",Hibernate.DOUBLE);
				queryObject.setParameter("begin", begin);
				queryObject.setParameter("end", end);
				return (ItemVo) queryObject.setResultTransformer(Transformers.aliasToBean(ItemVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	/**  
	 * 
	 * 查所有门诊、住院、病区的真实科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午3:16:30 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午3:16:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<FicDeptVO> queryFicDeptVO() {
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT Distinct s.DEPT_CODE as deptCode, s.DEPT_NAME as deptName ");
		sb.append("from T_DEPARTMENT s right join T_FICTITIOUS_CONTACT f on f.DEPT_CODE = s.DEPT_CODE ");
		sb.append("where  f.STOP_FLG = 0 and f.DEL_FLG = 0 and f.type in ('C','I','N')");
		List<FicDeptVO> voList = (List<FicDeptVO>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<FicDeptVO> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("deptCode",Hibernate.STRING);
				queryObject.addScalar("deptName",Hibernate.STRING);
				return queryObject.setResultTransformer(Transformers.aliasToBean(FicDeptVO.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<FicDeptVO>();
	}
	
}
