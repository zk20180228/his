package cn.honry.statistics.bi.inpatient.inpatientAvgFee.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.inpatientAvgFee.dao.InpatientAvgFeeDAO;
import cn.honry.statistics.bi.inpatient.inpatientAvgFee.vo.InpatientAvgFeeVo;
import cn.honry.statistics.util.dateVo.DateVo;
@Repository("inpatientAvgFeeDAO")
@SuppressWarnings({ "all" })
public class InpatientAvgFeeDAOImpl extends HibernateEntityDao<BiInpatientInfo> implements InpatientAvgFeeDAO{
	private static final int List = 0;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<InpatientAvgFeeVo> queryInpatientAvgFee(String[] diArrayKey,List<Map<String,List<String>>> list,int dateType,DateVo datevo ) {
				//天数
				int num=365;
				String sqldate=null;	
				//通过判断时间维度种类确定天数和
				if(dateType==1){
					num=365;
					String date1=datevo.getYear1()+"";
					String date2=datevo.getYear2()+"";//
					sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR between '"+date1+"'  and '"+date2+"'";
				}else if(dateType==2){
					String date1=datevo.getYear1()+"/"+datevo.getQuarter1();
					String date2=datevo.getYear2()+"/"+datevo.getQuarter2();
					num=90;
					sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR between '"+date1+"'  and '"+date2+"' ";
				}else if(dateType==3){
					num=30;
					String date1=datevo.getYear1()+"/"+datevo.getMonth1();
					String date2=datevo.getYear2()+"/"+datevo.getMonth2();
					sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR_MONTH between '"+date1+"' and '"+date2+"' ";
				}else if(dateType==4){
					num=1;
					String date11=datevo.getYear1()+"/"+datevo.getMonth1();
					String date12=datevo.getYear2()+"/"+datevo.getMonth2();
					String date21=datevo.getDay1()+"";
					String date22=datevo.getDay2()+"";
					sqldate="select  d.ID_KEY from BI_DATE  d where d.YEAR_MONTH between '"+date11+"' and '"+date12+"' and d.day_of_month between '"+date21+"' and '"+date22+"'";
				}
				//查询时间
				StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
				StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
			    sql.append(" select ");
				//遍历数组，去匹配所选择的维度拼接sql和order
				for(int i=0;i<diArrayKey.length;i+=2){
					if("dept_code".equals(diArrayKey[i])){
						sql.append(" t.INHOS_DEPTCODE as deptCode");
						order.append("t.INHOS_DEPTCODE ");
					}
					//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
					if(i!=(diArrayKey.length-2)){
						order.append(",");
					}
					sql.append(",");
				}
				sql.append("	         count(t.RECIPE_NO) as inSum, ");
				sql.append("	       TRUNC(sum(t.TOT_COST)*1./count(t.RECIPE_NO),2) as perFee, ");
				if(dateType==1){
					sql.append("         to_char(t.FEE_DATE,'yyyy') as timeChose");
				}else if(dateType==2){
					sql.append("        to_char(to_char(t.FEE_DATE,'yyyy')||'/'||to_char(t.FEE_DATE,'q')) as timeChose ");
				}else if(dateType==3){
					sql.append("         to_char(t.FEE_DATE,'yyyy/mm') as timeChose");
				}else if(dateType==4){
					sql.append("         to_char(t.FEE_DATE,'yyyy/mm/dd') as timeChose");
				}
				sql.append("	  from VIEW_INPATIENT_FEEINFO t where 1=1 ");
				if(dateType==1){
					sql.append(" and to_char(t.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
				}else if(dateType==2){
					sql.append(" and to_char(t.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.FEE_DATE,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
				}else if(dateType==3){
					sql.append(" and to_char(t.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.FEE_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
				}else if(dateType==4){
					sql.append(" and to_char(t.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.FEE_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
					sql.append(" and to_char(t.FEE_DATE,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
				}
				//遍历数组，添加查询条件（匹配所选择的维度拼接）
				for(int i=0;i<diArrayKey.length;i+=2){
					if("dept_code".equals(diArrayKey[i])){
//						sql.append(" and t.EXECUTE_DEPTCODE  in ("+list.get(i).get(diArrayKey[i])+")");
//						sql.append(" and d.dept_code  in ('8751','8131','8030')");
						String strings = list.get(i).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
						sql.append(" and t.INHOS_DEPTCODE in  ('"+strings+"')");
					}
				}
				sql.append("	 group by  ");
				sql.append(order.toString());
				sql.append(",");
				//对于时间的排序放在最后
				if(dateType==1){
					sql.append("         to_char(t.FEE_DATE,'yyyy') ");
				}else if(dateType==2){
					sql.append("         to_char(t.FEE_DATE,'yyyy')  ");
					sql.append("        ,to_char(t.FEE_DATE,'q')   ");
				}else if(dateType==3){
					sql.append("         to_char(t.FEE_DATE,'yyyy/mm') ");
				}else if(dateType==4){
					sql.append("         to_char(t.FEE_DATE,'yyyy/mm/dd')");
				}
				sql.append("	 order by ");
				sql.append(order.toString());
				sql.append(",");
				//对于时间的排序放在最后
				if(dateType==1){
					sql.append("         to_char(t.FEE_DATE,'yyyy') ");
				}else if(dateType==2){
					sql.append("         to_char(to_char(t.FEE_DATE,'yyyy')||'/'||to_char(t.FEE_DATE,'q'))   ");
				}else if(dateType==3){
					sql.append("         to_char(t.FEE_DATE,'yyyy/mm') ");
				}else if(dateType==4){
					sql.append("         to_char(t.FEE_DATE,'yyyy/mm/dd')");
				}
				SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
				for(int i=0;i<diArrayKey.length;i+=2){
					if("dept_code".equals(diArrayKey[i])){
						queryObject1.addScalar("deptCode");
					}										
				}
				queryObject1.addScalar("inSum",Hibernate.DOUBLE).addScalar("perFee",Hibernate.DOUBLE).addScalar("timeChose");
				List<InpatientAvgFeeVo> bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(InpatientAvgFeeVo.class)).list();
				if(bdl!=null){
					return bdl;
				}
				return new ArrayList<InpatientAvgFeeVo>();
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
