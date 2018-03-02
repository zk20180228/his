package cn.honry.inner.statistics.outpatientDocRecipe.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.inner.statistics.outpatientDocRecipe.dao.InnerOutpatientDocRecipeDao;
import cn.honry.inner.statistics.outpatientDocRecipe.vo.OutpatientDocRecipeVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Repository("innerOutpatientDocRecipeDao")
@SuppressWarnings({ "all" })
public class InnerOutpatientDocRecipeDaoImpl  extends HibernateDaoSupport  implements InnerOutpatientDocRecipeDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	private final String[] outFee={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};
	@Override
	public List<OutpatientDocRecipeVo> MZYSKDGZ(String beginDate, String endDate) {
		List<String> maintnl= wordLoadDocDao.returnInTables(beginDate, endDate, outFee, "MZ");
		StringBuffer sb = new StringBuffer();
		if(maintnl!=null||maintnl!=null){
			sb.append("select s1.docCode docCode,s1.deptCode deptCode,s1.peopleNum peopleNum,s1.recipeNum recipeNum,s2.feeName feeName,s2.feeCost feeCost,s1.totalCost totalCost,s1.areaCode areaCode from( " );
			for(int i=0,len=maintnl.size();i<len;i++){
				if(i>0){
					sb.append(" Union All ");
				}
				sb.append("select t"+i+".DOCT_CODE docCode,t"+i+".reg_dpcd deptCode, d.dept_area_code areaCode,count(distinct t"+i+".card_no) peopleNum,count(distinct t"+i+".RECIPE_NO) recipeNum,sum(t"+i+".tot_cost) totalCost" );
				sb.append(" from "+maintnl.get(i)+" t"+i+",t_department d,t_Business_Dictionary  di,t_Charge_Minfeetostat  m where t"+i+".fee_date >= to_date(:beginDate, 'yyyy-MM-dd hh24:mi:ss')");
				sb.append(" and t"+i+".fee_date < to_date(:endDate, 'yyyy-MM-dd hh24:mi:ss') and t"+i+".reg_dpcd = d.dept_code and di.Code_Encode = m.Minfee_Code and t0.fee_code = di.code_encode");
				sb.append(" and di.Code_Type = 'drugMinimumcost'  and m.Report_Code = 'MZ01'");
		        sb.append(" group by t"+i+".DOCT_CODE, t"+i+".reg_dpcd,d.dept_area_code) s1," );
		        sb.append(" (Select m.fee_stat_name feeName,sum(t"+i+".tot_cost) feeCost,t"+i+".DOCT_CODE code,t"+i+".reg_dpcd deptCode from t_Business_Dictionary d,t_Charge_Minfeetostat m,"+maintnl.get(i)+" t"+i+"" );
		        sb.append(" Where d.Code_Encode = m.Minfee_Code and t"+i+".fee_code = d.code_encode and d.Code_Type = 'drugMinimumcost' and m.Report_Code = 'MZ01'");
		        sb.append("and t"+i+".fee_date >=to_date(:beginDate, 'yyyy-MM-dd hh24:mi:ss') and t"+i+".fee_date <to_date(:endDate, 'yyyy-MM-dd hh24:mi:ss')group by m.fee_stat_name, t"+i+".DOCT_CODE,t"+i+".reg_dpcd) s2" );
		        sb.append("  where s1.docCode = s2.code and s1.deptCode= s2.deptCode order by  s1.deptCode" );
			}
		}
        List<OutpatientDocRecipeVo> list=super.getSession().createSQLQuery(sb.toString())
        		.addScalar("docCode").addScalar("deptCode").addScalar("areaCode")
        		.addScalar("peopleNum",Hibernate.INTEGER).addScalar("recipeNum",Hibernate.INTEGER)
        		.addScalar("feeName").addScalar("feeCost",Hibernate.DOUBLE).addScalar("totalCost",Hibernate.DOUBLE)
        		.setParameter("beginDate", beginDate).setParameter("endDate", endDate)
				.setResultTransformer(Transformers.aliasToBean(OutpatientDocRecipeVo.class)).list();
        
        if(list!=null && list.size()>0){
        	return list;
        }
        return new ArrayList<OutpatientDocRecipeVo>();
	}

	@Override
	public void saveMongoLog(MongoLog mong) {
		this.getHibernateTemplate().save(mong);
	}
	
	
}
