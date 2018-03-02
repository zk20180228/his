package cn.honry.inner.statistics.outpatientAntPresDetail.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.outpatientAntPresDetail.dao.OutpatientAntDao;
import cn.honry.inner.statistics.outpatientAntPresDetail.vo.OutpatientAntVo;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;

@Repository("outpatientAntDao")
@SuppressWarnings("all")
public class OutpatientAntDaoImpl extends  HibernateEntityDao<OutpatientAntVo> implements OutpatientAntDao {
	
	private final String[] outpatientFee={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};//处方明细表
	private final String MZ="MZ";
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjDao")
	private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
	
	
	@Override
	public void init_MZKJYWCFBL(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, outpatientFee, MZ);
		if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer(1500);
			buffer.append("Select out.dept as dept,out.docName As docName,out.drugCfs As drugCfs,out.drugKjcfs As drugKjcfs,");
			buffer.append("out.drugBl As drugBl,out.ygbl As ygbl,out.equel As equel, out.name as name from( ");
			buffer.append("select (select aa.employee_name from t_employee aa ");
			buffer.append("where aa.employee_jobno =tt.ysgh) as docName,");//--医生姓名,
			buffer.append("to_char(tt.cfs) as drugCfs,");//--药物处方数
			buffer.append(" to_char(tt.kjcfs) as drugKjcfs,");//--抗菌药物处方数
			buffer.append(" to_char(tt.bl * 100, 'FM999,999,999,990.00') as drugBl,");//--抗菌药物处方比例
			buffer.append("case when tt.ygbl is null then null else to_char(tt.ygbl * 100 ,'FM999,999,999,990.00') end as ygbl,");//--院规比例
			buffer.append("case when tt.ygbl is null or tt.kjcfs = 0 or cfs = 0 then  null");
			buffer.append(" else to_char((tt.ygbl-tt.bl) * 100, 'FM999,999,999,990.00') end as equel,");//--对比
			buffer.append("tt.name as name,tt.dept as dept ");//--时间
			buffer.append("from (select f.ysgh, f.cfs, f.kjcfs,  round(f.kjcfs / f.cfs, 4) bl,");
			buffer.append("(select e.kjcfbl from t_drug_proportion e where e.bqbm=f.dept) ygbl,f.name as name,f.dept as dept ");
			buffer.append("from (select t.ysgh ysgh, count(distinct(t.cfh)) cfs,");
			buffer.append("count(distinct(case  when t.kjbz = 1 then ");
			buffer.append("t.cfh end)) kjcfs,t.name as name,t.dept as dept ");
			buffer.append(" from ( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union ");
				}
				buffer.append("select to_char(a"+i+".fee_date,'yyyy-mm-dd') as name ,a"+i+".REG_DPCD as dept,a"+i+".recipe_no cfh, a"+i+".doct_code ysgh,");
				buffer.append(" case when a"+i+".item_code in (select a.drug_code");
				buffer.append(" from t_drug_info a where (a.DRUG_NAMEINPUTCODE like '01%' ");// --自定义码01 02 05开头的是抗菌药物
				buffer.append(" or a.DRUG_NAMEINPUTCODE like '02%' or a.DRUG_NAMEINPUTCODE like '05%')");
				buffer.append(" and a.drug_type = 'P') and a"+i+".use_name in ('P.O', 'ivgtt', 'iv', 'im',");
				buffer.append("'微量泵泵入', '输液泵泵入') then 1 end kjbz ");
				
				buffer.append("from "+tnL.get(i)+" a"+i+" ");
				
				buffer.append("where a"+i+".fee_date >=TO_DATE('"+begin+"','yyyy-mm-dd hh24:mi:ss')");
				buffer.append(" and a"+i+".fee_date <=TO_DATE('"+end+"','yyyy-mm-dd hh24:mi:ss') ");
				buffer.append("and a"+i+".drug_flag = '1' and a"+i+".trans_type = '1' and a"+i+".pay_flag = '1' ");
				buffer.append("and a"+i+".doct_code in (select c.employee_jobno from t_employee c ");
			}
			buffer.append(")) t group by t.ysgh,t.name,t.dept ) f) tt ");
			buffer.append(") out");
			List<OutpatientAntVo> list=this.getSession().createSQLQuery(buffer.toString())
					.addScalar("docName").addScalar("drugCfs").addScalar("drugKjcfs")
					.addScalar("drugBl").addScalar("ygbl").addScalar("equel").addScalar("name").addScalar("dept")
					.setResultTransformer(Transformers.aliasToBean(OutpatientAntVo.class)).list();
			
			DBObject query = new BasicDBObject();
			query.put("name", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_TOTAL_DAY", query);//删除原来的数据
			
			if(list!=null && list.size()>0){
				 List<DBObject> userList = new ArrayList<DBObject>();
					for(OutpatientAntVo vo:list){
							BasicDBObject obj = new BasicDBObject();
							obj.append("docName", vo.getDocName());//医生姓名
							obj.append("drugCfs", vo.getDrugCfs());//药物处方数
							obj.append("drugKjcfs",vo.getDrugKjcfs());//抗菌药物处方数
							obj.append("drugBl", vo.getDrugBl());//抗菌药物处方比例
							obj.append("ygbl", vo.getYgbl());//院规比例
							obj.append("equel", vo.getEquel());//对比
							obj.append("name", vo.getName());//时间
							obj.append("dept", vo.getDept());//科室
							userList.add(obj);
					}
				new MongoBasicDao().insertDataByList(menuAlias+"_TOTAL_DAY", userList);
		}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_TOTAL_DAY", list, date);
	}
		
	}

}
