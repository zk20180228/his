package cn.honry.statistics.bi.outpatient.recipeDoctor.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.recipeDoctor.dao.RecipeDoctorDao;
import cn.honry.statistics.bi.outpatient.recipeDoctor.vo.BiOptFeedetailVo;

@Repository("recipeDoctorDao")
@SuppressWarnings({ "all" })
public class RecipeDoctorDaoImpl extends HibernateEntityDao implements RecipeDoctorDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<BiOptFeedetailVo> queryData(BiOptFeedetailVo vo) {
		StringBuffer sBuffer = new StringBuffer();
//TODO 原生SQL
//		--按照科室进行统计处方数和处方金额
//		select t.reg_dpcd as 科室id,
//		       (select dept.dept_name from t_department dept where dept_id = t.reg_dpcd) as 科室name,
//		      (select sum(count(distinct tail.recipe_no))
//		                  from bi_opt_feedetail tail
//		                 where tail.reg_dpcd = t.reg_dpcd
//		                 and to_char(tail.fee_date,'yyyy-mm-dd hh24:mm:ss') > '2016-05-08 00:00:00'
//		                 group by tail.recipe_no) as 处方数,      
//		       (select sum(tail.tot_cost)
//		          from bi_opt_feedetail tail
//		         where tail.reg_dpcd = t.reg_dpcd 
//		         and to_char(tail.fee_date,'yyyy-mm-dd hh24:mm:ss') > '2016-05-08 00:00:00') as 处方金额,
//		        CONCAT(to_char('2','990.99'),'%') as 同比,
//		       CONCAT(to_char('3','990.99'),'%') as 环比
//		  from bi_opt_feedetail t
//		  where to_char(t.fee_date,'yyyy-mm-dd hh24:mm:ss') > '2016-05-08 00:00:00'
//		  --where to_char(t.open_advice_date,'yyyy-mm-dd hh24:mm:ss') > 0
//		 group by t.reg_dpcd
		
		sBuffer.append("select t.reg_dpcd as 科室id,");
		sBuffer.append(" (select dept.dept_name from t_department dept where dept_id = t.reg_dpcd) as 科室name,");
		sBuffer.append(" (select sum(count(distinct tail.recipe_no))");
		sBuffer.append(" ");
		sBuffer.append(" ");
		sBuffer.append(" ");
		sBuffer.append(" ");
		sBuffer.append(" ");
		sBuffer.append(" ");
		
		
		
		
		return null;
	}

	
	
	
}
