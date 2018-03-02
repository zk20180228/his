package cn.honry.inner.statistics.internalCompare2.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.internalCompare2.dao.InternalCompare2Dao;
import cn.honry.inner.statistics.internalCompare2.vo.InternalCompare2Vo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.utils.DateUtils;
@Repository("internalCompare2Dao")
@SuppressWarnings("all")
public class InternalCompare2DaoImpl extends  HibernateEntityDao<InternalCompare2Vo> implements InternalCompare2Dao {
	private final String[] inpatientFee={"T_INPATIENT_FEEINFO_NOW","T_INPATIENT_FEEINFO"};
	private final String[] outFee={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	@Override
	public void init_YYNKYXBHNEYXBDBBT(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		String begin=date+"-01 00:00:00";
		String end=returnEndTime(date);
		String prevDate=DateUtils.formatDateY_M(DateUtils.addYear(DateUtils.parseDateY_M(date),-1));
		String prevBegin=prevDate+"-01 00:00:00";
		String prevEnd=returnEndTime(prevDate);
		
		List<String> maintnl= wordLoadDocDao.returnInTables(begin, end, outFee, "MZ");
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientFee, "ZY");
		
		List<String> prevMain= wordLoadDocDao.returnInTables(prevBegin, prevEnd, outFee, "MZ");
		List<String> prevTnL=wordLoadDocDao.returnInTables(prevBegin, prevEnd, inpatientFee, "ZY");
		
		if ((tnL != null &&tnL.size()>0)&&(maintnl != null&&maintnl.size()>0)&&(prevTnL != null &&prevTnL.size()>0)&&(prevMain != null&&prevMain.size()>0)) {
			menuAlias+="_MONTH_NOW";
			StringBuffer buffer = new StringBuffer(2000);
			buffer.append("select deptCode,(select t.dept_name from t_department t where t.dept_code = deptCode and rownum<2 and t.stop_flg=0 and t.del_flg=0 ) dept,");//科室
			buffer.append("zy yearZY,prezy yearedZY,(zy-prezy) increaseZY,round(decode(prezy,0,100,(zy-prezy)*100/prezy),2) rateZY,");//住院
			buffer.append("mz yearMZ,premz yearedMZ,(mz-premz) increaseMZ,round(decode(premz,0,100,(mz-premz)*100/premz),2) rateMZ,");//门诊
			buffer.append("(zy+mz) yearTol,(prezy+premz) yearedTol, (zy+mz-prezy-premz) increaseTol,round(decode((prezy - premz), 0,100,to_number(zy + mz - prezy - premz)*100 /to_number(prezy + premz)),2) rateTol ");//总收入
			buffer.append("from (select de.deptCode,nvl(zy.value,0) zy,nvl(prezy.value,0) prezy,nvl(premz.value,0) premz,nvl(mz.value,0) mz ");
			buffer.append("from ( ");
			for (int i = 0, len = maintnl.size(); i < len; i++) {//当年门诊数据
				if (i > 0) {
					buffer.append(" Union All ");
				}
				buffer.append("select t"+i+".REG_DPCD AS deptCode, sum(t"+i+".tot_cost) AS value ");
				buffer.append("from "+maintnl.get(i)+" t"+i);
				buffer.append(" where  ");
				buffer.append(" t"+i+".fee_date >=to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and t"+i+".fee_date <=to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and t"+i+".PAY_FLAG = 1 group by t"+i+".REG_DPCD ");
			}
			buffer.append(") mz,(");
			for (int i = 0, len = prevMain.size(); i < len; i++) {//上年门诊数据
				if (i > 0) {
					buffer.append(" Union All ");
				}
				buffer.append("select t"+i+".REG_DPCD AS deptCode, sum(t"+i+".tot_cost) AS value ");
				buffer.append("from "+prevMain.get(i)+" t"+i);
				buffer.append(" where  ");
				buffer.append(" t"+i+".fee_date >=to_date('"+prevBegin+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and t"+i+".fee_date <=to_date('"+prevEnd+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and t"+i+".PAY_FLAG = 1 group by t"+i+".REG_DPCD ");
			}
			buffer.append(") premz,(");
			for (int i = 0, len = tnL.size(); i < len; i++) {
				if (i > 0) {
					buffer.append(" Union All ");
				}
				buffer.append("select n"+i+".RECIPE_DEPTCODE AS deptCode,sum(n"+i+".tot_cost) AS value ");
				buffer.append("from "+tnL.get(i)+" n"+i);
				buffer.append(" where ");
				buffer.append("n"+i+".fee_date >=to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and n"+i+".fee_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("group by  n"+i+".RECIPE_DEPTCODE ");
			}
			buffer.append(") zy,(");
			for (int i = 0, len = prevTnL.size(); i < len; i++) {
				if (i > 0) {
					buffer.append(" Union All ");
				}
				buffer.append("select n"+i+".RECIPE_DEPTCODE AS deptCode,sum(n"+i+".tot_cost) AS value ");
				buffer.append("from "+prevTnL.get(i)+" n"+i);
				buffer.append(" where ");
				buffer.append("n"+i+".fee_date >=to_date('"+prevBegin+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and n"+i+".fee_date <= to_date('"+prevEnd+"', 'yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("group by  n"+i+".RECIPE_DEPTCODE ");
			}
			buffer.append(") prezy,(select t.dept_code deptCode from t_department t  ) de ");
			buffer.append(" where de.deptCode=prezy.deptCode(+) and de.deptCode=premz.deptCode(+) and de.deptCode=mz.deptCode(+)and de.deptCode=zy.deptCode(+)");
			
			buffer.append(") ");
			
 			List<InternalCompare2Vo> list = super.getSession().createSQLQuery(buffer.toString()).addScalar("dept")
					.addScalar("deptCode")
					.addScalar("yearedTol",Hibernate.DOUBLE).addScalar("yearTol",Hibernate.DOUBLE).addScalar("increaseTol",Hibernate.DOUBLE).addScalar("rateTol",Hibernate.DOUBLE)
					.addScalar("yearedMZ",Hibernate.DOUBLE).addScalar("yearMZ",Hibernate.DOUBLE).addScalar("increaseMZ",Hibernate.DOUBLE).addScalar("rateMZ",Hibernate.DOUBLE)
					.addScalar("yearedZY",Hibernate.DOUBLE).addScalar("yearZY",Hibernate.DOUBLE).addScalar("increaseZY",Hibernate.DOUBLE).addScalar("rateZY",Hibernate.DOUBLE)
					
					.setResultTransformer(Transformers.aliasToBean(InternalCompare2Vo.class))
					.list();
			
			DBObject query = new BasicDBObject();
			query.put("feeDate", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias, query);//删除原来的数据
			
			if (list != null && list.size() > 0) {
				List<DBObject> userList = new ArrayList<DBObject>();
				for (InternalCompare2Vo vo : list) {
						BasicDBObject bdObject1 = new BasicDBObject();
						bdObject1.append("dept", vo.getDept());
						bdObject1.append("deptCode", vo.getDeptCode());
						bdObject1.append("feeDate",date);
						//总收入数据
						bdObject1.append("yearedTol", vo.getYearedTol());
						bdObject1.append("yearTol", vo.getYearTol());
						bdObject1.append("increaseTol", vo.getIncreaseTol());
						bdObject1.append("rateTol", vo.getRateTol());
						//门诊数据
						bdObject1.append("yearedMZ", vo.getYearedMZ());
						bdObject1.append("yearMZ", vo.getYearMZ());
						bdObject1.append("increaseMZ", vo.getIncreaseMZ());
						bdObject1.append("rateMZ", vo.getRateMZ());
						
						bdObject1.append("yearedZY", vo.getYearedZY());
						bdObject1.append("yearZY", vo.getYearZY());
						bdObject1.append("increaseZY", vo.getIncreaseZY());
						bdObject1.append("rateZY", vo.getRateZY());
						
						userList.add(bdObject1);
				}
				new MongoBasicDao().insertDataByList(menuAlias, userList);
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias, list, date);
		}
 		
	}
	public String returnEndTime(String date){
		String end=null;
		date=date.substring(0,7)+"-01";
		Calendar ca=Calendar.getInstance(Locale.CHINESE);
		try {
			ca.setTime(df.parse(date));
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DATE, -1);
			end=df.format(ca.getTime())+" 23:59:59";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}
	
	
}
