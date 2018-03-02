package cn.honry.statistics.deptstat.inpatientStatistics.dao.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.outpatientAntPresDetail.vo.OutpatientAntVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.vo.UndrugDataVo;
import cn.honry.statistics.deptstat.inpatientCount.dao.InpatientCountDao;
import cn.honry.statistics.deptstat.inpatientStatistics.dao.InpatientStatisticsDao;
import cn.honry.statistics.deptstat.inpatientStatistics.vo.InpatientStatisticsVo;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;

@Repository("inpatientStatisticsDao")
@SuppressWarnings({"all"})
public class InpatientStatisticsDaoImpl extends HibernateEntityDao<InpatientInfoNow> implements InpatientStatisticsDao{
	
	 // 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Override
	public List<SysDepartment> queryDeptCodeName() {
		String sql="select t.dept_code deptCode,t.dept_name deptName from t_department t";
		List<SysDepartment> list = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("deptName").setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		return list;
	}

	@Override
	public List<InpatientStatisticsVo> queryDataList(List<String> tnL,
			String firstData, String endData, String code,String flag) {
		
		String f=code;
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		
		StringBuffer sbf=new StringBuffer(400);
		if("K".equals(flag)){
			code="'"+code.replace(",", "','")+"'";
			sbf.append("select smart.total1 total,DECODE(nvl(big.total1,0), 0,'--', to_char(trunc(smart.total1 * 100 /big.total1, 2), '000.00')) gender,smart.deptCode code from (");
			sbf.append("select sum(num) total1,deptCode deptCode from  ( "); 
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					sbf.append(" union all ");
				}
				sbf.append(" select count(1) num,t.dept_code deptCode  from "+tnL.get(i)+" t where t.in_date <=to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') ");
				sbf.append(" and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss') and (t.dept_code in ("+code+")) group by t.dept_code ");
			}
			sbf.append(" union all ");
			sbf.append(" select 0 num,t.dept_code deptCode  from t_inpatient_info t where t.dept_code in("+code+") group by t.dept_code ) group by deptCode ) smart left join (select sum(num) total1,deptCode deptCode from  (");
			
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					sbf.append(" union all ");
				}
				sbf.append(" select count(1) num,t.dept_code deptCode  from "+tnL.get(i)+" t where t.in_date <=to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') ");
				sbf.append(" and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss')  group by t.dept_code ");
			}
			sbf.append(") group by deptCode) big on big.deptCode=smart.deptCode order by smart.deptCode asc ");
		}else if(flag.startsWith("Y")){
			char area=flag.charAt(1);
			sbf.append("select nvl(smart.total1,0) total,DECODE(nvl(big.total1,0), 0,'--', to_char(trunc(smart.total1 * 100 /big.total1, 2), '000.00')) gender,cast('"+area+"' as varchar(2)) code from (");
			sbf.append("select sum(num) total1 from  ( "); 
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					sbf.append(" union all ");
				}
				sbf.append(" select count(1) num  from "+tnL.get(i)+" t where t.in_date <=to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') ");
				sbf.append(" and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss') and (t.dept_code in (select t.dept_code deptCode from t_department t where t.dept_area_code='"+area+"')) group by t.dept_code ");
			}
			sbf.append(" )  ) smart , (select sum(num) total1 from (");
			
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					sbf.append(" union all ");
				}
				sbf.append(" select count(1) num  from "+tnL.get(i)+" t where t.in_date <=to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') ");
				sbf.append(" and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss')  ");
			}
			sbf.append(")) big  ");
		}
		List<InpatientStatisticsVo> list = this.getSession().createSQLQuery(sbf.toString()).addScalar("total",Hibernate.INTEGER).addScalar("gender").addScalar("code")
		.setResultTransformer(Transformers.aliasToBean(InpatientStatisticsVo.class)).list();
		if(list!=null&&list.size()>0){
//			InpatientStatisticsVo vo=list.get(0);
//			if("Y".equals(flag)){
//				vo.setCode(f);
//			}else{
//				vo.setCode(code);
//			}
			return list;
		}
		return new ArrayList<InpatientStatisticsVo>();
	}

	@Override
	public List<SysDepartment> queryAreaCodeName() {
		String sql="select t.code_encode deptCode,t.code_name  deptName from t_business_dictionary t where t.code_type='hospitalArea'";
		List<SysDepartment> list = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("deptName")
				.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		return list;
	}

	@Override
	public String queryDeptByAreaCode(String code) {
		String sql="select t.dept_code deptCode from t_department t where t.dept_area_code='"+code+"'";
		List<SysDepartment> list = this.getSession().createSQLQuery(sql).addScalar("deptCode")
				.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		String c="";
		if(list!=null&&list.size()>0){
			for(SysDepartment vo:list){
				c+="'"+vo.getDeptCode()+"',";
			}
		}
		c=c.substring(0,c.length()-1);
		return c;
	}
	@Override
	public String queryDeptByAreaCodes(String code) {
		String sql="select t.dept_code deptCode from t_department t where t.dept_area_code in('"+code+"')";
		List<SysDepartment> list = this.getSession().createSQLQuery(sql).addScalar("deptCode")
				.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		StringBuffer buffer=new StringBuffer();
		if(list!=null&&list.size()>0){
			for(SysDepartment vo:list){
				buffer.append(vo.getDeptCode()).append(",");
			}
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}

    //子字符串modelStr在字符串str中第count次出现时的下标  
    private int getFromIndex(String str, String modelStr, Integer count) {  
        //对子字符串进行匹配  
            Matcher slashMatcher = Pattern.compile(modelStr).matcher(str);  
        int index = 0;  
            //matcher.find();尝试查找与该模式匹配的输入序列的下一个子序列  
           while(slashMatcher.find()) {  
            index++;  
            //当modelStr字符第count次出现的位置  
            if(index == count){  
               break;  
            }  
        }  
            //matcher.start();返回以前匹配的初始索引。  
           return slashMatcher.start();  
    }
    
   //初始化住院人数统计数据
	@Override
	public void saveOrUpdateToDB(String firstData, String endData,Integer type) throws Exception {
		String time="";
		if(type==1){//日
			time=firstData;
		}else if(type==2){//月
			time=firstData.substring(0, 7);
		}else if(type==3){//年
			time=firstData.substring(0, 4);
		}
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		String sql="select pp.dept_code code,sum(num) total,(select sum(num1) from (select count(1) num1 from t_inpatient_info t where "
				+ "t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss')"
				+ " union all select count(1) num1 from t_inpatient_info_now t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') "
				+ "and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss')  union all select count(1) num1  from t_inpatient_info_now t "
				+ "where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.in_state='I') ) totals from (select count(1) num,dept_code "
				+ "from t_inpatient_info t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"',"
				+ " 'yyyy-mm-dd HH24:mi:ss') group by t.dept_code union all select count(1) num,t.dept_code from t_inpatient_info_now t where t.in_date <= "
				+ "to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss') group by t.dept_code "
				+ "union all select count(1) num,t.dept_code  from t_inpatient_info_now t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss')"
				+ " and t.in_state='I' group by t.dept_code)pp group by pp.dept_code";
		List<InpatientStatisticsVo> list = this.getSession().createSQLQuery(sql).addScalar("code").addScalar("total",Hibernate.INTEGER).addScalar("totals",Hibernate.INTEGER)
		.setResultTransformer(Transformers.aliasToBean(InpatientStatisticsVo.class)).list();
		
		DBObject query = new BasicDBObject();
		query.put("workdate", time);//移除数据条件
		
		if(type==1){//日
			new MongoBasicDao().remove("ZYRSTJ_DAY", query);//删除原来的数据
		}else if(type==2){//月
			new MongoBasicDao().remove("ZYRSTJ_MONTH", query);//删除原来的数据
		}else if(type==3){//年
			new MongoBasicDao().remove("ZYRSTJ_YEAR", query);//删除原来的数据
		}
		
		if(list!=null && list.size()>0){
			List<DBObject> userList = new ArrayList<DBObject>();
			for(InpatientStatisticsVo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("total", vo.getTotal());//医生姓名
					obj.append("totals", vo.getTotals());//药物处方数
					obj.append("deptCode",vo.getCode());//抗菌药物处方数
					obj.append("workdate",time);//抗菌药物处方比例
					userList.add(obj);
			}
			if(type==1){//日
				new MongoBasicDao().insertDataByList("ZYRSTJ_DAY", userList);
			}else if(type==2){//月
				new MongoBasicDao().insertDataByList("ZYRSTJ_MONTH", userList);
			}else if(type==3){//年
				new MongoBasicDao().insertDataByList("ZYRSTJ_YEAR", userList);
			}
		}
		SimpleDateFormat mat=new SimpleDateFormat("yyyy-MM-dd");
		if(type==1){//日
			wordLoadDocDao.saveMongoLog(mat.parse(firstData), "ZYRSTJ_DAY", list, time);
		}else if(type==2){//月
			wordLoadDocDao.saveMongoLog(mat.parse(firstData), "ZYRSTJ_MONTH", list, time);
		}else if(type==3){//年
			wordLoadDocDao.saveMongoLog(mat.parse(firstData), "ZYRSTJ_YEAR", list, time);
		}
	}

	@Override
	public List<InpatientStatisticsVo> queryDataListFromDB(String time,
			 String collection,String code, String flag) throws Exception{
		String c=code;
		
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBList mongoDeptList = new BasicDBList();
		List<InpatientStatisticsVo> list=new ArrayList<InpatientStatisticsVo>();
		if("K".equals(flag)){
			List<String> codeList=new ArrayList<String>();
			bdObject.append("workdate", time);
			String[] codeStr=code.split(",");
			for(String cod:codeStr){
				codeList.add(cod);
				mongoDeptList.add(new BasicDBObject("deptCode",cod));
			}
			bdObject.put("$or", mongoDeptList);
			Collections.sort(codeList);
			int len=codeList.size();
			DBCursor cursor = new MongoBasicDao().findAlldataBySort(collection,bdObject,"deptCode",1);
			DBObject dbCursor;
			int count=0;
			if(!cursor.hasNext()){
				for(int i=count;i<len;i++){
						InpatientStatisticsVo vo=new  InpatientStatisticsVo();
						vo.setTotal(0);
						vo.setTotals(0);
						vo.setCode(codeList.get(count));
						vo.setGender("0.00");
						list.add(vo);
				}
				return list;
			}
			while(cursor.hasNext()){
				InpatientStatisticsVo voOne=new  InpatientStatisticsVo();
				dbCursor = cursor.next();
				Integer total =(Integer)dbCursor.get("total") ;//科室人数
				Integer totals=(Integer)dbCursor.get("totals");//全院人数
				String deptCd=(String)dbCursor.get("deptCode");//科室
				if(!codeList.get(count).equals(deptCd)){
					brea:for(int i=count;i<len;i++){
						if(!codeList.get(count).equals(deptCd)){
							InpatientStatisticsVo vo=new  InpatientStatisticsVo();
							vo.setTotal(0);
							vo.setTotals(0);
							vo.setCode(codeList.get(count));
							vo.setGender("0.00");
							list.add(vo);
							count++;
						}else{
							break brea;
						}
						
					}
				}
				voOne.setTotal(total);
				voOne.setTotals(totals);
				voOne.setCode(deptCd);
				DecimalFormat df = new DecimalFormat("######0.00");
			    String gender=totals==0?"--":""+(df.format((double)total*100/totals));
			    voOne.setGender(gender);
				list.add(voOne);
				
				count++;
			}
			return list;
		}else if(flag.startsWith("Y")){
			 String[] arr = code.split(",");
			 Integer t=0;
			 Integer ts=0;
			 for(String dcode:arr){
				 mongoDeptList.add(new BasicDBObject("deptCode",dcode));
			 }
			 bdObject.put("$or", mongoDeptList);
		 	bdObject.append("workdate", time);
			DBCursor cursor = new MongoBasicDao().findAlldata(collection,bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
				Integer total =(Integer)dbCursor.get("total") ;//科室人数
				Integer totals=(Integer)dbCursor.get("totals");//全院人数
				
				t+=total;
				ts=totals;
			}
			 InpatientStatisticsVo voOne=new  InpatientStatisticsVo();
			 voOne.setTotal(t);
			 voOne.setTotals(ts);
			 DecimalFormat df = new DecimalFormat("######0.00");
			 String gender=ts==0?"--":""+(df.format((double)t*100/ts));
			 voOne.setGender(gender);
			 voOne.setCode(c);
			 list.add(voOne);
			 return list;
		}
		return new ArrayList<InpatientStatisticsVo>();
	}  
}
