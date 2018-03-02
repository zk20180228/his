package cn.honry.statistics.bi.outpatient.outpatientFeeType.dao.impl;

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

import cn.honry.base.bean.model.District;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.dao.OutpatientFeeTypeDAO;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.vo.OutpatientFeeTypeVo;
import cn.honry.statistics.util.dateVo.DateVo;

/***
 * 门诊收费类型分析
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Repository("outpatientFeeTypeDAO")
@SuppressWarnings({ "all" })
public class OutpatientFeeTypeDAOImpl extends HibernateEntityDao<OutpatientFeeTypeVo> implements OutpatientFeeTypeDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	public List<District> getDistrictList(Integer ld,String parId){
		String hql="FROM District d WHERE d.validFlag=1 " ;
		if(ld>0){
			hql+=" and d.level="+ld;
		}
		if(ld==1){			
		}else{
			hql+=" AND d.parentId='"+parId+"'";
		}
		hql+=" ORDER BY d.path";
		List<District> disList = super.find(hql, null);
		if(disList!=null&&disList.size()>0){
			return disList;
		}
		return null;
	}
	@Override
	public List<OutpatientFeeTypeVo> queryOutpatientFeeTypeVo(String[] diArrayKey,
			List<Map<String, List<String>>> list, int dateType, DateVo datevo) {
				//查询时间
				StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
				StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
			    sql.append(" select ");
				//遍历数组，去匹配所选择的维度拼接sql和order
				for(int i=0;i<diArrayKey.length;i+=2){
					if("doct_dept".equals(diArrayKey[i])){
						sql.append(" bof.doct_dept as doctDept");
						order.append(diArrayKey[i]);
					}
					if("patient_nativeplace".equals(diArrayKey[i])){
						sql.append(" bbp.patient_nativeplace as address");
						order.append(diArrayKey[i]);
					}
					if("patient_age".equals(diArrayKey[i])){
						sql.append(" vrm.patient_age as age");
						sql.append(" ,vrm.patient_ageunit as ageUnit");
						order.append("vrm.patient_age");
						order.append(",vrm.patient_ageunit");
					}
					if("employee_education_code".equals(diArrayKey[i])){
						sql.append(" bbp.EMPLOYEE_EDUCATION_CODE as degree");
						order.append(diArrayKey[i]);
					}
					if("mode_code".equals(diArrayKey[i])){
						sql.append(" bfp.mode_code as payway");
						order.append(diArrayKey[i]);
					}
					//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
					if(i!=(diArrayKey.length-1)){
						order.append(",");
					}
					sql.append(",");
				} 
				sql.append("	       sum(bof.TOT_COST) as totCost,");
				sql.append("	       trunc(100*round(COUNT(bof.tot_cost)/SUM(COUNT(bof.tot_cost)) OVER(),4),2) as scale, ");
				if(dateType==1){
					sql.append("         to_char(bof.FEE_DATE,'yyyy') as timeChose");
				}else if(dateType==2){
					sql.append("          to_char(to_char(bof.FEE_DATE,'yyyy')||'/'||to_char(bof.FEE_DATE,'q')) as timeChose");
				}else if(dateType==3){
					sql.append("          to_char(bof.FEE_DATE,'yyyy/mm') as timeChose");
				}else if(dateType==4){
					sql.append("         to_char(bof.FEE_DATE,'yyyy/mm/dd') as timeChose");
				}
				/*sql.append("	 from  v_outpatient_recipedetail bof   where 1=1 ");
				sql.append(" ,V_PATIENT bbp , V_BUSINESS_PAYMODE bfp where bof.patient_key = bbp.id_key");
				sql.append("	  and bof.invoice_no = bfp.INVOICE_NO and bof.Cancel_Flag = 1 and bof.PAY_FLAG = 1 and bof.TRANS_TYPE = 1   ");*/
				sql.append("	 from  v_outpatient_feedetail    bof    ");
				sql.append(" ,v_patient  bbp , v_business_paymode   bfp ,v_regster_main  vrm where bof.patient_no = bbp.medicalrecord_id");
				sql.append("	  and bof.invoice_no = bfp.INVOICE_NO and bof.clinic_code = vrm.clinic_code and bof.Cancel_Flag = 1 and bof.PAY_FLAG = 1 and bof.TRANS_TYPE = 1   and bfp.TRANS_TYPE = 1 ");
				if(dateType==1){
					sql.append(" and to_char(bof.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
				}else if(dateType==2){
					sql.append(" and to_char(bof.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(bof.FEE_DATE,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'");
				}else if(dateType==3){
					sql.append(" and to_char(bof.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(bof.FEE_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
				}else if(dateType==4){
					sql.append(" and to_char(bof.FEE_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(bof.FEE_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
					sql.append(" and to_char(bof.FEE_DATE,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
				}
				//遍历数组，添加查询条件（匹配所选择的维度拼接）
				for(int i=0;i<diArrayKey.length;i+=2){
					if("doct_dept".equals(diArrayKey[i])){
						StringBuilder value=new StringBuilder();
						for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
							if(!"".equals(value.toString())){
								value.append(",");
							}
							value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
						}
						sql.append(" and  bof.doct_dept  in ("+value.toString()+")");
					}
					if("patient_nativeplace".equals(diArrayKey[i])){
						StringBuilder value=new StringBuilder();
						for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
							if(!"".equals(value.toString())){
								value.append(",");
							}
							value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
						}
						sql.append(" and   bbp.patient_nativeplace  in ("+value.toString()+")");
					}
					if("patient_age".equals(diArrayKey[i])){
						StringBuilder value=new StringBuilder();
						for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
							if(j==0){
								String age=list.get(i/2).get(diArrayKey[i]).get(j);
								String agenum=age.substring(0,age.length()-1);
								String agennit=age.substring(age.length()-1,age.length());
								if(agenum.contains("-"))	{
									String []ageArr=agenum.split("-");
									sql.append(" and (vrm.patient_age between '"+ageArr[0]+"' and '"+ageArr[1]+"'  and vrm.patient_ageunit = '"+agennit+"')");
								}else{
									sql.append(" and (vrm.patient_age = '"+agenum+"' and vrm.patient_ageunit = '"+agennit+"' )");
								}
							}else{
								String age=list.get(i/2).get(diArrayKey[i]).get(j);
								String agenum=age.substring(0,age.length()-1);
								String agennit=age.substring(age.length()-1,age.length());
								if(agenum.contains("-"))	{
									String []ageArr=agenum.split("-");
									sql.append(" or (vrm.patient_age between '"+ageArr[0]+"' and '"+ageArr[1]+"'  and vrm.patient_ageunit = '"+agennit+"')");
								}else{
									sql.append(" or (vrm.patient_age = '"+agenum+"' and vrm.patient_ageunit = '"+agennit+"' )");
								}
							}
								
						}
					}
					if("employee_education_code".equals(diArrayKey[i])){
						StringBuilder value=new StringBuilder();
						for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
							if(!"".equals(value.toString())){
								value.append(",");
							}
							value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
						}
						sql.append(" and   bbp.employee_education_code  in ("+value.toString()+")");
					}
					if("mode_code".equals(diArrayKey[i])){
						StringBuilder value=new StringBuilder();
						for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
							if(!"".equals(value.toString())){
								value.append(",");
							}
							value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
						}
						sql.append(" and   bfp.mode_code  in ("+value.toString()+")");
					}
				}
				sql.append("	 group by  ");
				sql.append(order.toString());
				/*if(diArrayKey.length>3){
					sql.append(",");
				}*/
				//对于时间的排序放在最后
				if(dateType==1){
					sql.append("         to_char(bof.FEE_DATE,'yyyy') ");
				}else if(dateType==2){
					sql.append("         to_char(bof.FEE_DATE,'yyyy') ");
					sql.append("         ,to_char(bof.FEE_DATE,'q') ");
				}else if(dateType==3){
					sql.append("         to_char(bof.FEE_DATE,'yyyy/mm') ");
				}else if(dateType==4){
					sql.append("         to_char(bof.FEE_DATE,'yyyy/mm/dd')");
				}
				sql.append("	 order by ");
				sql.append(order.toString());
				/*if(diArrayKey.length>3){
					sql.append(",");
				}*/
				//对于时间的排序放在最后
				if(dateType==1){
					sql.append("         to_char(bof.FEE_DATE,'yyyy') ");
				}else if(dateType==2){
					sql.append("         to_char(bof.FEE_DATE,'yyyy') ");
					sql.append("         ,to_char(bof.FEE_DATE,'q') ");
				}else if(dateType==3){
					sql.append("         to_char(bof.FEE_DATE,'yyyy/mm') ");
				}else if(dateType==4){
					sql.append("         to_char(bof.FEE_DATE,'yyyy/mm/dd')");
				}
				SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
				for(int i=0;i<diArrayKey.length;i+=2){
					if("doct_dept".equals(diArrayKey[i])){
						queryObject1.addScalar("doctDept");
					}
					if("patient_nativeplace".equals(diArrayKey[i])){
						queryObject1.addScalar("address");
					}
					if("patient_age".equals(diArrayKey[i])){
						queryObject1.addScalar("age");
						queryObject1.addScalar("ageUnit");
					}
					if("employee_education_code".equals(diArrayKey[i])){
						queryObject1.addScalar("degree");
					}
					if("mode_code".equals(diArrayKey[i])){
						queryObject1.addScalar("payway");
					}
				}
				queryObject1.addScalar("totCost",Hibernate.DOUBLE).addScalar("scale",Hibernate.DOUBLE).addScalar("timeChose");
				List<OutpatientFeeTypeVo> bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OutpatientFeeTypeVo.class)).list();
				if(bdl!=null){
					return bdl;
				}
				return new ArrayList<OutpatientFeeTypeVo>();
	}
}
