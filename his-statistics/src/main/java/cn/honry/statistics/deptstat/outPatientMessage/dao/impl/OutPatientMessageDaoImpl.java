package cn.honry.statistics.deptstat.outPatientMessage.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;
import cn.honry.statistics.deptstat.outPatientMessage.dao.OutPatientMessageDao;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
import cn.honry.statistics.finance.chargeBill.vo.ChargeBillVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("outPatientMessageDao")
@SuppressWarnings({ "all" })
public class OutPatientMessageDaoImpl extends HibernateEntityDao<OutPatientMessageVo> implements OutPatientMessageDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**  
	 * 
	 * 出院患者信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<OutPatientMessageVo> queryOutPatientMessage(List<String> tnL,String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OutPatientMessageVo>();
		}
		final StringBuffer sql = new StringBuffer(1500);
		
		sql.append("select * from(select rownum rn, t.inpatientNo inpatientNo,t.patientName patientName,(select d.code_name from t_business_dictionary d where d.code_encode=t.sex and d.ext_c1='性别') sex,t.age age,");
		sql.append("(t.age||t.ageunit) ageunit,t.bedName bedName,t.docName docName,");
		sql.append("t.nurseName nurseName,t.inDate inDate,t.outDate outDate,b.DIAG_OUTSTATE outState,b.DIAG_NAME diagName,t.pactCode pactCode,t.deptCode deptCode from ( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sql.append(" UNION all ");
			}
			sql.append("SELECT ").append(" rm").append(i).append(".medicalrecord_id as inpatientNo,rm").append(i)
			.append(".patient_name as patientName,rm").append(i)
			.append(".report_sex as sex,rm").append(i)
			.append(".report_age as age,rm").append(i)
			.append(".report_ageunit as ageunit,rm").append(i)
			.append(".bed_name as bedName,rm").append(i)
			.append(".charge_doc_name as docName,rm").append(i)
			.append(".duty_nurse_name as nurseName,rm").append(i)
			.append(".in_date as inDate,rm").append(i)
			.append(".out_date as outDate,")
			.append("rm").append(i).append(".pact_code as pactCode,")
			.append("rm").append(i).append(".Dept_Name as deptCode ");
			sql.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append(" WHERE rm").append(i).append(".IN_STATE = 'O' ");
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sql.append(" and rm").append(i).append(".out_date >=  to_date('"+startTime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
				sql.append(" and rm").append(i).append(".out_date <=  to_date('"+endTime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(deptCode)){
				sql.append(" and rm").append(i).append(".dept_code in ('"+deptCode.replace(",", "','")+"') ");
			}else{
				sql.append(" and rm"+i+".dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
		}//group by t.DIAG_OUTSTATE,t.DIAG_NAME,t.inpatient_no
		sql.append(" ) t  left join ( select* from (select t.DIAG_OUTSTATE,t.DIAG_NAME,t.inpatient_no, row_number() over(partition by t.inpatient_no order by t.updatetime desc) rn from t_business_diagnose t where  t.out_date>=to_date('"+startTime+"' ,'yyyy-MM-dd hh24:mi:ss') and  t.out_date<=to_date('"+endTime+"' ,'yyyy-MM-dd hh24:mi:ss') ) L where L.rn='1'  ) b on b.inpatient_no=t.inpatientNo ");
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		sql.append("where rownum <=").append(start+"*"+count).append(") L where L.rn>=").append((start-1)+"*"+count);
		
		SQLQuery query=getSession().createSQLQuery(sql.toString())
				.addScalar("inpatientNo").addScalar("patientName")
				.addScalar("sex").addScalar("age",Hibernate.INTEGER).addScalar("ageunit")
				.addScalar("bedName").addScalar("docName")
				.addScalar("nurseName").addScalar("inDate",Hibernate.TIMESTAMP).addScalar("outDate",Hibernate.TIMESTAMP)
				.addScalar("outState",Hibernate.INTEGER).addScalar("pactCode")
				.addScalar("diagName").addScalar("deptCode");
		List<OutPatientMessageVo> OutPatientMessageVoList=query.setResultTransformer(Transformers.aliasToBean(OutPatientMessageVo.class)).list();
		if(OutPatientMessageVoList!=null&&OutPatientMessageVoList.size()>0){
			return OutPatientMessageVoList;
		}
		return new ArrayList<OutPatientMessageVo>();
	}
	
	/**  
	 * 
	 * 出院患者信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public int getTotalOutPatientMessage(List<String> tnL,String startTime,String endTime,String deptCode,String menuAlias) {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sql = new StringBuffer(1500);
		sql.append("select count(1) outState from ( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sql.append(" UNION ALL ");
			}
			sql.append("SELECT ").append(" rm").append(i).append(".inpatient_no as inpatientNo FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append("  WHERE rm").append(i).append(".IN_STATE = 'O' ");
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sql.append(" and rm").append(i).append(".out_date >=  to_date('"+startTime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
				sql.append(" and rm").append(i).append(".out_date <=  to_date('"+endTime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(deptCode)&&!"1".equals(deptCode)){
				sql.append(" and rm").append(i).append(".dept_code in ('"+deptCode.replace(",", "','")+"') ");
			}else{
				sql.append(" and rm"+i+".dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
		}
		sql.append(")");
		SQLQuery query=getSession().createSQLQuery(sql.toString()).addScalar("outState",Hibernate.INTEGER);
		List<OutPatientMessageVo> OutPatientMessageVoList=query.setResultTransformer(Transformers.aliasToBean(OutPatientMessageVo.class)).list();
		if(OutPatientMessageVoList!=null&&OutPatientMessageVoList.size()>0){
			return OutPatientMessageVoList.get(0).getOutState();
		}
		return 0;
	}

	@Override
	public List<OutPatientMessageVo> queryOutPatientMessageForDB(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		
		BasicDBObject bdObject = new BasicDBObject();
		List<OutPatientMessageVo> list=new ArrayList<OutPatientMessageVo>();
		if(StringUtils.isNotBlank(deptCode)){
			BasicDBList deptList=new BasicDBList();
			String[] deptArr=deptCode.split(",");
			for(int i=0,len=deptArr.length;i<len;i++){
				deptList.add(new BasicDBObject("deptCode", deptArr[i]));
			}
			bdObject.append("$or", deptList);
		}
		DBCursor cursor = new MongoBasicDao().findAlldata("CYHZXXCX", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			OutPatientMessageVo vo=new  OutPatientMessageVo();
			 dbCursor = cursor.next();
			 vo.setInpatientNo((String)dbCursor.get("inpatientNo")); 
			 vo.setPatientName((String)dbCursor.get("patientName")); 
			 vo.setSex((String)dbCursor.get("sex")); 
			 vo.setAge((Integer) dbCursor.get("age"));
			 vo.setBedName((String)dbCursor.get("bedName")); 
			 vo.setDocName((String)dbCursor.get("docName")); 
			 vo.setNurseName((String)dbCursor.get("nurseName")); 
			 vo.setInDate((Date) dbCursor.get("inDate")); 
			 vo.setOutDate((Date)dbCursor.get("outDate")); 
			 vo.setOutState((Integer)dbCursor.get("outState")); 
			 vo.setPactCode((String)dbCursor.get("pactCode")); 
			 vo.setDiagName((String)dbCursor.get("diagName")); 
			list.add(vo);
			}
		return list;
	}
}
