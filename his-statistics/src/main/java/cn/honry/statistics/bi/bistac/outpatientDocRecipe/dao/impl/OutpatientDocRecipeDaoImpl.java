package cn.honry.statistics.bi.bistac.outpatientDocRecipe.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.mapping.Array;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.business.Page;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.dao.OutpatientDocRecipeDao;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.OutpatientDocRecipeVo;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("outpatientDocRecipeDao")
@SuppressWarnings({ "all" })
public class OutpatientDocRecipeDaoImpl extends HibernateDaoSupport implements OutpatientDocRecipeDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	private final String[] outFee={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};	
	@Resource(name = "client")
	private Client client;
	
	@Value("${es.register_main.index}")
	private String register_main_index;
	
	@Value("${es.register_main.type}")
	private String register_main_type;

	@Override
	public List<OutpatientDocRecipeVo> MZYSKDGZ(String beginDate, String endDate, Integer type) {
		List<String> maintnl= wordLoadDocDao.returnInTables(beginDate, endDate, outFee, "MZ");
		StringBuffer sb = new StringBuffer();
		if(maintnl!=null||maintnl!=null){
			sb.append("select s1.docCode docCode,s1.deptCode deptCode,s1.peopleNum peopleNum,s1.recipeNum recipeNum,s2.feeName feeName,s2.feeCost feeCost,s1.totalCost totalCost,s1.areaCode areaCode from( " );
			for(int i=0,len=maintnl.size();i<len;i++){
				if(i>0){
					sb.append(" Union All ");
					sb.append("select s1.docCode docCode,s1.deptCode deptCode,s1.peopleNum peopleNum,s1.recipeNum recipeNum,s2.feeName feeName,s2.feeCost feeCost,s1.totalCost totalCost,s1.areaCode areaCode from( " );
				}
				sb.append("select t"+i+".DOCT_CODE docCode,t"+i+".reg_dpcd deptCode, d.dept_area_code areaCode,count(distinct t"+i+".card_no) peopleNum,count(distinct t"+i+".RECIPE_NO) recipeNum,sum(t"+i+".tot_cost) totalCost" );
				sb.append(" from "+maintnl.get(i)+" t"+i+",t_department d,t_Business_Dictionary  di,t_Charge_Minfeetostat  m where t"+i+".fee_date >= to_date(:beginDate, 'yyyy-MM-dd hh24:mi:ss')");
				sb.append(" and t"+i+".fee_date < to_date(:endDate, 'yyyy-MM-dd hh24:mi:ss') and t"+i+".reg_dpcd = d.dept_code and di.Code_Encode = m.Minfee_Code and t"+i+".fee_code = di.code_encode");
				sb.append(" and di.Code_Type = 'drugMinimumcost'  and m.Report_Code = 'MZ01'");
		        sb.append(" group by t"+i+".reg_dpcd,t"+i+".DOCT_CODE,d.dept_area_code) s1," );
		        sb.append(" (Select m.fee_stat_name feeName,sum(t"+i+".tot_cost) feeCost,t"+i+".DOCT_CODE code,t"+i+".reg_dpcd deptCode from t_Business_Dictionary d,t_Charge_Minfeetostat m,"+maintnl.get(i)+" t"+i+"" );
		        sb.append(" Where d.Code_Encode = m.Minfee_Code and t"+i+".fee_code = d.code_encode and d.Code_Type = 'drugMinimumcost' and m.Report_Code = 'MZ01'");
		        sb.append("and t"+i+".fee_date >=to_date(:beginDate, 'yyyy-MM-dd hh24:mi:ss') and t"+i+".fee_date <to_date(:endDate, 'yyyy-MM-dd hh24:mi:ss')group by t"+i+".reg_dpcd,t"+i+".DOCT_CODE,m.fee_stat_name) s2" );
		        sb.append("  where s1.docCode = s2.code and s1.deptCode= s2.deptCode " );
			}
			sb.append("order by deptCode");
//			sb.append("select t.DOCT_CODE docCode,t.reg_dpcd deptCode,count(distinct t.card_no) peopleNum,count(distinct t.RECIPE_NO) recipeNum,sum(t.tot_cost) totalCost" );
//			sb.append(" from t_outpatient_feedetail_now t,t_department d where t.fee_date >= to_date(:beginDate, 'yyyy-MM-dd hh24:mi:ss') and t.fee_date < to_date(:endDate, 'yyyy-MM-dd hh24:mi:ss') and t.reg_dpcd = d.dept_code");
//	        sb.append(" group by t.DOCT_CODE, t.reg_dpcd,d.dept_area_code) s1," );
//	        sb.append(" (Select m.fee_stat_name feeName,sum(t.tot_cost) feeCost,t.DOCT_CODE code from t_Business_Dictionary d,t_Charge_Minfeetostat m,t_outpatient_feedetail_now t" );
//	        sb.append(" Where d.Code_Encode = m.Minfee_Code and t.fee_code = d.code_encode and d.Code_Type = 'drugMinimumcost' and m.Report_Code = 'MZ01'");
//	        sb.append("and t.fee_date >=to_date(:beginDate, 'yyyy-MM-dd hh24:mi:ss')and t.fee_date <to_date(:endDate, 'yyyy-MM-dd hh24:mi:ss')group by m.fee_stat_name, t.DOCT_CODE) s2" );
//	        sb.append("  where s1.docCode = s2.code" );
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
}
