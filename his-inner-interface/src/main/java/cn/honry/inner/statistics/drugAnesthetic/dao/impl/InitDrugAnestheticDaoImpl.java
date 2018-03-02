package cn.honry.inner.statistics.drugAnesthetic.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javassist.expr.NewArray;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.drugAnesthetic.dao.InitDrugAnestheticDao;
import cn.honry.inner.statistics.drugAnesthetic.vo.Anestheticvo;
import cn.honry.inner.statistics.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("initDrugAnestheticDao")
@SuppressWarnings("all")
public class InitDrugAnestheticDaoImpl extends HibernateDaoSupport implements InitDrugAnestheticDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	private final String[] T_DRUG_OUTSTORE={"T_DRUG_OUTSTORE_NOW","T_DRUG_OUTSTORE"};//药品出库记录表T_OUTPATIENT_FEEDETAIL
	private final String[] T_REGISTER_MAIN={"T_REGISTER_MAIN_NOW","T_REGISTER_MAIN"};//门诊挂号：挂号主表T_OUTPATIENT_FEEDETAIL
	private final String[] T_INPATIENT_INFO={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};//住院主表T_OUTPATIENT_FEEDETAIL
	private final String[] T_OUTPATIENT_FEEDETAIL={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};//处方明细表T_OUTPATIENT_FEEDETAIL
	private final String[] T_INPATIENT_MEDICINELIST={"T_INPATIENT_MEDICINELIST_NOW","T_INPATIENT_MEDICINELIST"};//住院药品明细表T_OUTPATIENT_FEEDETAIL
	private final String MZ="MZ";
	private final String ZY="ZY";
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	private final DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	/**
	 * 麻醉精神药品统计(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZJSYPTJ_DAY(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> list1=wordLoadDocDao.returnInTables(begin, end, T_DRUG_OUTSTORE, ZY);
			List<String> list2=wordLoadDocDao.returnInTables(begin, end, T_REGISTER_MAIN, MZ);
			List<String> list3=wordLoadDocDao.returnInTables(begin, end, T_INPATIENT_INFO, ZY);
			List<String> list4=wordLoadDocDao.returnInTables(begin, end, T_OUTPATIENT_FEEDETAIL, MZ);
			List<String> list5=wordLoadDocDao.returnInTables(begin, end, T_INPATIENT_MEDICINELIST, ZY);
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select di.drug_commonname as drugName,dout.deptName as deptName,dout.drugedName as name,dout.drugSpec as drugSpec,dout.drugPack as drugPack,");
			sqlBuffer.append("dout.drugedDate as drugedDate,dout.meark as meark,dout.drugType as drugType,dout.deptCode as deptCode, ");
			sqlBuffer.append("patient.medicalrecordid as pno,patient.patientName as patientName,doct.doctName as doctName,doct.doctCode as doctCode ,");
			sqlBuffer.append("case when di.show_flag = 1 then trunc(dout.num,2) when di.show_flag = 0 then trunc(dout.num / di.drug_packagingnum,2) end as num ");
			sqlBuffer.append("from (");
			/**
			 * 出库记录
			 */
			sqlBuffer.append("select outo.id as id,outo.drug_storage_name as deptName,outo.drug_storage_code as deptCode,outo.druged_name as drugedName,");
			sqlBuffer.append("outo.specs as drugSpec,outo.pack_unit as drugPack,outo.drug_type as drugType,outo.out_num as num,outo.out_date as drugedDate,");
			sqlBuffer.append("outo.remark as meark,outo.drug_code as drug_code,outo.op_type as op_type,outo.recipe_no as recipe_no,outo.sequence_no as sequence_no,outo.get_person as get_person,");
			sqlBuffer.append("outo.pack_unit as packUnit,outo.min_unit as minUnit ");
			sqlBuffer.append("from "+list1.get(0)+" outo ");
			sqlBuffer.append(" ) dout ");
			/**
			 * 病历号、患者姓名
			 */
			sqlBuffer.append("left join (");
			sqlBuffer.append("select rmo.medicalrecordid as medicalrecordid,rmo.clinic_code as get_person,rmo.patient_name as patientName ");
			sqlBuffer.append("from "+list2.get(0)+" rmo ");
			sqlBuffer.append("union all ");
			sqlBuffer.append("select iio.medicalrecord_id as medicalrecordid,iio.inpatient_no as get_person,iio.patient_name as patientName ");
			sqlBuffer.append("from "+list3.get(0)+" iio ");
			sqlBuffer.append(" ) patient on dout.get_person = patient.get_person ");
			/**
			 * 开方医师
			 */
			sqlBuffer.append("left join (");
			sqlBuffer.append("select ofo.recipe_no as recipe_no,ofo.sequence_no as sequence_no,ofo.doct_code as doctCode,ofo.doct_codename as doctName ");
			sqlBuffer.append("from "+list4.get(0)+" ofo ");
			sqlBuffer.append("union all ");
			sqlBuffer.append("select imo.recipe_no as recipe_no,imo.sequence_no as sequence_no,imo.recipe_doccode as doctCode,imo.recipe_docname as doctName ");
			sqlBuffer.append("from "+list5.get(0)+" imo ");
			sqlBuffer.append(" ) doct on dout.recipe_no = doct.recipe_no and dout.sequence_no = doct.sequence_no ");
			/**
			 * 药品信息
			 */
			sqlBuffer.append("left join t_drug_info di on dout.drug_code = di.drug_code ");
			/**
			 * 查询条件
			 */
			sqlBuffer.append("where dout.op_type in ('1', '3', '4', '5') and di.drug_nature in ('S','P') ");
			sqlBuffer.append("and dout.drugedDate >= to_date(:begin, 'yyyy-mm-dd hh24:mi:ss') and dout.drugedDate < to_date(:end, 'yyyy-mm-dd hh24:mi:ss') ");
			SQLQuery queryObject = getSession().createSQLQuery(sqlBuffer.toString());
			queryObject.addScalar("deptName",Hibernate.STRING)
					   .addScalar("deptCode",Hibernate.STRING)
					   .addScalar("pno",Hibernate.STRING)
					   .addScalar("patientName",Hibernate.STRING)
					   .addScalar("doctName",Hibernate.STRING)
					   .addScalar("doctCode",Hibernate.STRING)
					   .addScalar("drugName",Hibernate.STRING)
					   .addScalar("drugSpec",Hibernate.STRING)
					   .addScalar("drugPack",Hibernate.STRING)
					   .addScalar("num",Hibernate.DOUBLE)
					   .addScalar("drugedDate",Hibernate.STRING)
					   .addScalar("meark",Hibernate.STRING)
					   .addScalar("name",Hibernate.STRING)
					   .addScalar("drugType",Hibernate.STRING);
			
			queryObject.setParameter("begin", begin);
			queryObject.setParameter("end", end);
			List<Anestheticvo> list =queryObject.setResultTransformer(Transformers.aliasToBean(Anestheticvo.class)).list();
			DBObject query = new BasicDBObject();
			query.put("selectTime", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias, query);//删除原来的数据
			if(list!=null && list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(Anestheticvo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("selectTime", date);
					obj.append("deptName", vo.getDeptName());
					obj.append("deptCode", vo.getDeptCode());
					obj.append("pno", vo.getPno());
					obj.append("patientName", vo.getPatientName());
					obj.append("doctName", vo.getDoctName());
					obj.append("doctCode", vo.getDoctCode());
					obj.append("drugName", vo.getDrugName());
					obj.append("drugSpec", vo.getDrugSpec());
					obj.append("drugPack", vo.getDrugPack());
					obj.append("num", vo.getNum());
					obj.append("drugedDate", vo.getDrugedDate());
					obj.append("meark", vo.getMeark());
					obj.append("name", vo.getName());
					obj.append("drugType", vo.getDrugType());
					voList.add(obj);
				 }
				new MongoBasicDao().insertDataByList(menuAlias, voList);//添加新数据
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
			end=df.format(ca.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}
}
