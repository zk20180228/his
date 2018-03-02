package cn.honry.statistics.bi.outpatientDrugRatio.dao.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.bi.outpatientDrugRatio.dao.OutpatientDrugRatioDAO;
import cn.honry.statistics.bi.outpatientDrugRatio.vo.DrugRatioVo;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeNameVo;
import cn.honry.statistics.util.dateVo.DateVo;
@Repository("outpatientDrugRatioDAO")
@SuppressWarnings({ "all" })
public class OutpatientDrugRatioDAOImpl extends HibernateEntityDao<BiInpatientInfo>  implements OutpatientDrugRatioDAO{
	
	private static final int List = 0;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<DrugRatioVo> queryOutpatientDrugRatio(
			String[] diArrayKey,List<Map<String, List<String>>> list,int dateType, DateVo datevo) {
		//查询时间
		StringBuilder sql=new StringBuilder();//sql语句的StringBuffer对象
		StringBuilder order=new StringBuilder();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
	    sql.append(" select ");
		//遍历数组，去匹配所选择的维度拼接sql和order
		for(int i=0;i<diArrayKey.length;i+=2){
			if("EXEC_DPCD".equals(diArrayKey[i])){
				sql.append(" a.EXEC_DPCD as deptDimensionality");
				order.append(diArrayKey[i]);
			}
			
			//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=(diArrayKey.length-2)){
				order.append(",");
			}
			sql.append(",");
		}
		
		sql.append(" count(a.card_no) as outpatientSum, ");
		sql.append(" sum(a.TOT_COST) as outpatientFee, ");
		sql.append(" sum(e.TOT_COST) as drugFee ,"); 
		sql.append("  trunc(sum(e.TOT_COST)/sum(a.TOT_COST),2) as conRatio, ");
        sql.append("  trunc(sum(e.TOT_COST)/count(a.card_no),2) as perCapitaCost, ");
		
		
		
		if (dateType == 1) {

			// 同比修正版	
		    sql.append("trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b  where b.drug_flag = '1' and to_number(replace((to_char(a.fee_date, 'yyyy')), '/', '')) - 1=to_number((replace(to_char(b.fee_date, 'yyyy'), '/', '')))   and a.EXEC_DPCD =b.EXEC_DPCD)),2) as rose,");
			// 环比修正版
			sql.append("trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where  b.drug_flag = '1' and to_number(replace((to_char(a.fee_date, 'yyyy')), '/', '')) - 1=to_number((replace(to_char(b.fee_date, 'yyyy'), '/', '')))  and a.EXEC_DPCD =b.EXEC_DPCD)),2) as chain,");
			sql.append("         to_char(a.fee_date,'yyyy') as timeChose");
		} else if (dateType == 2  ) {
			sql.append(" trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where b.drug_flag='1' and   to_number((replace(to_char(b.fee_date, 'yyyy'),'/', '')))= to_number(replace((to_char(a.fee_date, 'yyyy')),'/', '')) - 1 and to_number((replace(to_char(b.fee_date, 'q'), '/', '')))=to_number(replace((to_char(a.fee_date, 'q')), '/', ''))  and  b.EXEC_DPCD=a.EXEC_DPCD  )), 2) as rose,");
						
			if(datevo.getQuarter1()==1&&datevo.getQuarter2()==1){
				sql.append("  trunc( (sum(e.TOT_COST) / sum((select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where  b.drug_flag='1'and to_number(replace((to_char(a.fee_date, 'yyyy')), '/', ''))-1 =to_number((replace(to_char(b.fee_date, 'yyyy'), '/', '')))  and to_number((replace(to_char(b.fee_date, 'mm'), '/', ''))) in(10,11,12) and b.drug_flag = '1' and a.EXEC_DPCD =b.EXEC_DPCD))),2) as chain,");
			
			}else if(datevo.getQuarter1()==1&&datevo.getQuarter2()!=1){
				sql.append("  trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where b.drug_flag='1' and ( to_char(a.fee_date, 'q') ='1'  and to_number(replace((to_char(a.fee_date, 'yyyy')),  '/',''))-1  = to_number((replace(to_char(b.fee_date, 'yyyy'), '/', ''))) and to_number((replace(to_char(b.fee_date, 'mm'), '/',''))) in(10,11,12) and   b.EXEC_DPCD=a.EXEC_DPCD  ) or(  ");
				sql.append(" to_char(a.fee_date, 'q') ! ='1'  and to_number(replace((to_char(a.fee_date, 'yyyy')), '/',''))  = to_number((replace(to_char(b.fee_date, 'yyyy'),'/', '')))  and to_number((replace(to_char(b.fee_date, 'mm'),'/', ''))) in (to_number(to_char(a.fee_date, 'q')) * 3 - 3,to_number(to_char(a.fee_date, 'q')) * 3 - 4,to_number(to_char(a.fee_date, 'q')) * 3 - 5) and  b.EXEC_DPCD=a.EXEC_DPCD ) )),  2) as chain,");
			}else{
				sql.append("  trunc( (sum(e.TOT_COST) / sum((select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where  b.drug_flag='1'and  to_number(replace((to_char(a.fee_date, 'yyyy')), '/', ''))=to_number((replace(to_char(b.fee_date, 'yyyy'), '/', '')))  and to_number((replace(to_char(b.fee_date, 'mm'), '/', ''))) in(to_number(to_char(a.fee_date, 'q'))*3-3 , to_number(to_char(a.fee_date, 'q'))*3-4 , to_number(to_char(a.fee_date, 'q'))*3-5) and b.drug_flag = '1' and a.EXEC_DPCD =b.EXEC_DPCD))),2) as chain,");
			}
			sql.append("  to_char(to_char(a.fee_date,'yyyy')||'/'||to_char(a.fee_date,'q')) as timeChose ");
		}else  if(dateType == 3){
			
			sql.append("trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where b.drug_flag = '1' and  to_number(replace((to_char(a.fee_date, 'yyyy/mm')), '/', '')) - 100 =to_number((replace(to_char(b.fee_date, 'yyyy/mm'), '/', '')))  and a.EXEC_DPCD =b.EXEC_DPCD )),2) as rose,");
            sql.append("trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where b.drug_flag = '1' and to_number((replace(to_char(add_months(b.fee_date ,1), 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(a.fee_date, 'yyyy/mm')), '/', ''))  and a.EXEC_DPCD =b.EXEC_DPCD)),2) as chain,");
			sql.append("         to_char(a.fee_date,'yyyy/mm') as timeChose");
			
		} else if (dateType == 4) {
			
			sql.append("trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where b.drug_flag = '1' and  to_number(replace((to_char(a.fee_date, 'yyyy/mm/dd')), '/', '')) - 10000=to_number((replace(to_char(b.fee_date, 'yyyy/mm/dd'), '/', '')))   and a.EXEC_DPCD =b.EXEC_DPCD )),2) as rose,");
			sql.append("trunc((sum(e.TOT_COST) /(select sum(b.TOT_COST) from v_OPT_FEEDETAIL_DrugRatio b where  b.drug_flag = '1' and  to_number((replace(to_char(b.fee_date+1, 'yyyy/mm/dd'), '/', ''))) = to_number(replace((to_char(a.fee_date, 'yyyy/mm/dd')), '/', ''))  and a.EXEC_DPCD =b.EXEC_DPCD )),2) as chain,");
			sql.append("         to_char(a.fee_date,'yyyy/mm/dd') as timeChose");
		}
		sql.append("  from v_OPT_FEEDETAIL_DrugRatio a  ");
		sql.append("	left join v_OPT_FEEDETAIL_DrugRatio e on e.id = a.id   and e.drug_flag = '1' ");
		sql.append("	 where 1=1  ");
		
		if(dateType==1){
			sql.append(" and to_char(a.fee_date,'yyyy') between '"+(datevo.getYear1())+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append(" and to_char(a.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(a.fee_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
		}else if(dateType==3){
			sql.append(" and to_char(a.fee_date,'yyyy') between '"+(datevo.getYear1())+"' and '"+datevo.getYear2()+"'   ");
		   sql.append(" and to_char(a.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append(" and to_char(a.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(a.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and to_char(a.fee_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
		
		//遍历数组，添加查询条件（匹配所选择的维度拼接）
		int n=0;
		for(int i=0;i<diArrayKey.length;i+=2){
			if("exec_dpcd".equalsIgnoreCase(diArrayKey[i])){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(n).get(diArrayKey[i]).size();j++){
					if(!"".equals(value.toString())){
						value.append(",");
					}
					value.append("'"+list.get(n).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and a.exec_dpcd in ("+value.toString()+")");
			}
			n++;
		}
		sql.append("	 group by  ");
		sql.append("a."+order.toString());
		sql.append(",");
		//对于时间的排序放在最后
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(a.fee_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(a.fee_date,'yyyy')  ");
			sql.append("        ,to_char(a.fee_date,'q')   ");
		}else if(dateType==3){
			sql.append("         to_char(a.fee_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(a.fee_date,'yyyy/mm/dd')");
		}
		sql.append("	 order by ");
		sql.append("a."+order.toString());
		
		SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if("exec_dpcd".equalsIgnoreCase(diArrayKey[i])){
				queryObject1.addScalar("deptDimensionality");
			}
		}
		queryObject1.addScalar("outpatientSum",Hibernate.DOUBLE).addScalar("outpatientFee",Hibernate.DOUBLE)
				.addScalar("drugFee",Hibernate.DOUBLE).addScalar("conRatio",Hibernate.DOUBLE)
				.addScalar("perCapitaCost",Hibernate.DOUBLE).addScalar("rose",Hibernate.DOUBLE)
				.addScalar("chain",Hibernate.DOUBLE).addScalar("timeChose");
		List<DrugRatioVo> bdl=bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(DrugRatioVo.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<DrugRatioVo>();
	}

	
	@Override
	public List<BiBaseOrganization> queryDeptForBiPublic(String deptType) {
		String sql="select o.ORG_CODE as orgCode , o.ORG_NAME as orgName from BI_BASE_ORGANIZATION o";
		//判断参数deptType科室类型是否有值
		if(StringUtils.isNotBlank(deptType)){
			if(deptType.indexOf(",")!=-1){
				deptType= deptType.replace(",", "','");
				sql += " where o.org_kind_code in ('"+deptType+"') and o.org_Code <>'0000001' and o.org_Code <> '0000002'";
			}else{
				sql +=" where o.org_kind_code= '"+deptType+"' and o.org_Code <>'0000001' and o.org_Code <> '0000002'";
			}
			
		}
		SQLQuery queryObject =this.getSession().createSQLQuery(sql);
		queryObject.addScalar("orgCode").addScalar("orgName");
		List<BiBaseOrganization> bdl=bdl = queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<BiBaseOrganization>();
	}


}
