package cn.honry.statistics.operation.operationDept.dao.impl;
/***
 * 手术科室汇总DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OperationApply;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.MenuVO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVo;
import cn.honry.statistics.operation.operationDept.dao.OperationDeptDao;
import cn.honry.statistics.operation.operationDept.vo.OpDeptDetailVo;
import cn.honry.statistics.operation.operationDept.vo.OpDeptTotalVo;
import cn.honry.statistics.operation.operationDept.vo.OpDoctorDetailVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("operationDeptDao")
@SuppressWarnings({"all"})
public class OperationDeptDaoImpl extends HibernateEntityDao<OperationCostVo> implements OperationDeptDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	/**
	 * 根据条件手术医生明细
	 * @Description:根据条件手术医生明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OpDoctorDetailVo> getOpDoctorDetailVo(String beginTime, String endTime, String opcDept,String execDept,String opDoctor, String page,
			String rows, String identityCard) {
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isBlank(beginTime)){
			beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			 endTime=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beginTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",beginTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<OpDoctorDetailVo>();
		}
		if(tnL.size()>1){
			sb.append(" select tt.opDoctorDept,tt.opDoctor,tt.inpatientNo,tt.preDate,"
					+ "tt.name,tt.itemName,tt.execDept,tt.feeOperCode,tt.totCost  from (");
		}
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" UNION ALL ");
			}
			sb.append(" SELECT ");
			sb.append(" C.DOC_DPCD as opDoctorDept,");
			sb.append(" C.OPS_DOCD as opDoctor,");
			sb.append(" C.OPERATIONDATE as preDate,");
			sb.append(" C.PATIENT_NO as inpatientNo,");
			sb.append(" C.NAME as name, ");
			sb.append(" (select listagg(ITEM_NAME, '，') within");
			sb.append(" group(");
			sb.append(" order by null ) from t_Operation_Item it where it.operation_id=c.operation_id)as itemName,");
			sb.append(" SUM( D").append(i).append(".TOT_COST) as  totCost, ");
			sb.append(" D").append(i).append(".FEEOPER_DEPTCODE as execDept,");
			sb.append(" D").append(i).append(".FEE_OPERCODE as feeOperCode");
			sb.append(" from t_Operation_Record C,").append(tnL.get(i)).append(" D").append(i);
			if(StringUtils.isNotBlank(identityCard)){
				sb.append( " ,t_patient p ");
			}
			sb.append(" WHERE   C.OPERATION_ID = D").append(i).append(".OPERATION_ID");
			sb.append(" AND C.YNVALID=1");
			if(StringUtils.isNotBlank(beginTime)){
				sb.append(" AND D").append(i).append(".FEE_DATE >= TO_DATE(:beginTime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(endTime)){
				sb.append(" AND D").append(i).append(".FEE_DATE  < TO_DATE(:endTime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(execDept)){
				sb.append(" AND C.EXEC_DEPT in (:execDept )");
			}
			if(StringUtils.isNotBlank(opcDept)){
				sb.append(" AND C.DOC_DPCD in (:opcDept)");
			}
			if(StringUtils.isNotBlank(opDoctor)){
				sb.append(" AND C.OPS_DOCD in (:opDoctor)");
			}
			if(StringUtils.isNotBlank(identityCard)){
				sb.append( " and p.MEDICALRECORD_ID=C.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:identityCard ");
			}
			sb.append(" AND D").append(i).append(".TRANS_TYPE = '1'");
			sb.append(" AND EXISTS (");
			sb.append(" SELECT 'X' FROM T_CHARGE_MINFEETOSTAT FEESTAT");
			sb.append(" WHERE ");
			sb.append(" FEESTAT.MINFEE_CODE=D").append(i).append(".FEE_CODE");
			sb.append(" AND FEESTAT.FEE_STAT_CODE = '10' ");
			sb.append(" AND FEESTAT.REPORT_CODE = 'ZY01'");
			sb.append(" )");
			sb.append(" GROUP BY ");
			sb.append(" C.DOC_DPCD,");
			sb.append(" C.OPS_DOCD,");
			sb.append("  C.OPERATIONDATE,");
			sb.append(" C.OPERATION_ID,");
			sb.append(" C.PATIENT_NO,");
			sb.append(" C.NAME, ");
			sb.append(" D").append(i).append(".FEEOPER_DEPTCODE,");
			sb.append(" D").append(i).append(".FEE_OPERCODE");
		}
		if(tnL.size()>1){
			sb.append(" ) tt");
			//hedong 20170228 添加排序（手术时间降序）
			sb.append(" ORDER BY preDate desc ");
		}
		String hql="";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(!"a".equals(rows)){
			StringBuffer bufferRows = new StringBuffer(sb.toString());
			bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
			bufferRows.append(") tab WHERE ROWNUM <= :bag ) WHERE rn > :start ");
			hql=bufferRows.toString();
			if(StringUtils.isNotBlank(execDept)){
				   paraMap.put("execDept", Arrays.asList(execDept.split(",")));
			   }
			if(StringUtils.isNotBlank(beginTime)){
				paraMap.put("beginTime", beginTime);
			}
			if(StringUtils.isNotBlank(endTime)){
				paraMap.put("endTime",DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(endTime),1))) );
			}
			if(StringUtils.isNotBlank(opDoctor)){
				paraMap.put("opDoctor",Arrays.asList(opDoctor.split(",")));
			}
			if(StringUtils.isNotBlank(opcDept)){
				paraMap.put("opcDept", Arrays.asList(opcDept.split(",")));
			}
			if(StringUtils.isNotBlank(identityCard)){
				paraMap.put("identityCard", identityCard);
			}
			final int start = Integer.parseInt(page == null ? "1" : page);
			final int count = Integer.parseInt(rows == null ? "20" : rows);
			paraMap.put("start", (start - 1) * count);
			paraMap.put("bag", start * count);
		}else{
			hql=sb.toString();
			if(StringUtils.isNotBlank(execDept)){
				   paraMap.put("execDept", Arrays.asList(execDept.split(",")));
			   }
			if(StringUtils.isNotBlank(beginTime)){
				paraMap.put("beginTime", beginTime);
			}
			if(StringUtils.isNotBlank(endTime)){
				paraMap.put("endTime",DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(endTime),1))) );
			}
			if(StringUtils.isNotBlank(opDoctor)){
				paraMap.put("opDoctor",Arrays.asList(opDoctor.split(",")));
			}
			if(StringUtils.isNotBlank(opcDept)){
				paraMap.put("opcDept", Arrays.asList(opcDept.split(",")));
			}
			if(StringUtils.isNotBlank(identityCard)){
				paraMap.put("identityCard", identityCard);
			}
		}
		
		List<OpDoctorDetailVo> voList =  namedParameterJdbcTemplate.query(hql,paraMap,new RowMapper<OpDoctorDetailVo>() {
			@Override
			public OpDoctorDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OpDoctorDetailVo vo = new OpDoctorDetailVo();
				vo.setOpDoctorDept(rs.getString("opDoctorDept"));
				vo.setOpDoctor(rs.getString("opDoctor"));
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setPreDate(rs.getTimestamp("preDate"));
				vo.setName(rs.getString("name"));
				vo.setItemName(rs.getString("itemName"));
				vo.setExecDept(rs.getString("execDept"));
				vo.setFeeOperCode(rs.getString("feeOperCode"));
				vo.setTotCost(rs.getDouble("totCost"));
				
				return vo;
		}});
		return voList;			
	}
	/**
	 * 根据条件查询手术科室汇总
	 * @Description:根据条件查询手术科室汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OpDeptTotalVo> getOpDeptTotalVo(String beginTime, String endTime, String opcDept,String execDept, String page,
			String rows) {
		
		List<OpDeptTotalVo> opDeptTotalVoList = new ArrayList<OpDeptTotalVo>();
		if(StringUtils.isNotBlank(opcDept)){
			//执行ke
			opDeptTotalVoList = this.getOpDeptTotalVo2(beginTime, endTime, opcDept,execDept, page, rows);
		}else{
				opDeptTotalVoList = this.getOpDeptTotalVo2(beginTime, endTime,"", execDept, page, rows);
		}

		return opDeptTotalVoList;
	}
	/**
	 * @Description:根据条件查询科室汇总中间 方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   execDept 执行科室   page 当前页数   rows 分页条数opcDept 手术医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OpDeptTotalVo> getOpDeptTotalVo2(String beginTime, String endTime,String opcDept, String execDept, String page,
			String rows) {
		String [] opcDepts=opcDept.split(",");
		String [] execDepts=execDept.split(",");
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isBlank(beginTime)){
			beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			 endTime=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beginTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",beginTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<OpDeptTotalVo>();
		}
		
			sb.append(" select tt.DOC_DPCD as opDoctorDept,tt.zqsl,tt.zqje,tt.ptsl,tt.ptje,tt.jzsl,tt.jzje,tt.grsl,tt.grje,"
					+ "(tt.zqsl+tt.ptsl+tt.jzsl+tt.grsl)as hjsl,(tt.zqje+tt.ptje+ tt.jzje+tt.grje)as hjje,(0) as ltje,"
					+ "round(decode((tt.zqje+tt.ptje+ tt.jzje+tt.grje), 0, 0,(tt.zqje+tt.ptje+ tt.jzje+tt.grje) / (tt.zqsl+tt.ptsl+tt.jzsl+tt.grsl)),4) as pjje from (");
			sb.append(" SELECT AA.DOC_DPCD,");
			sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '0', OPERATION_ID, NULL)) zqsl,");
			sb.append(" NVL(SUM(DECODE(OPS_KIND, '0', TOT_COST, 0)), 0) zqje,");
			sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '1', OPERATION_ID, NULL)) ptsl,");
			sb.append(" NVL(SUM(DECODE(OPS_KIND, '1', TOT_COST, 0)), 0) ptje,");
			sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '2', OPERATION_ID, NULL)) jzsl,");
			sb.append(" NVL(SUM(DECODE(OPS_KIND, '2', TOT_COST, 0)), 0) jzje,");
			sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '3', OPERATION_ID, NULL)) grsl,");
			sb.append(" NVL(SUM(DECODE(OPS_KIND, '3', TOT_COST, 0)), 0) grje");
			sb.append(" FROM (");
		for(int i=0;i<tnL.size();i++){
			
			if(i>0){
				sb.append(" UNION ALL ");
			}
			
			sb.append(" SELECT RCD.OPERATION_ID,");
			sb.append(" RCD.DOC_DPCD,");
			sb.append(" RCD.OPS_KIND,");
			sb.append(" FEEINFO.TOTCOST TOT_COST");
			sb.append(" FROM T_OPERATION_RECORD RCD,");
			sb.append(" (SELECT TT.OPERATION_ID, SUM(TOT) TOTCOST");
			sb.append(" FROM (SELECT FEE").append(i).append(".OPERATION_ID,");
			sb.append("  FEE").append(i).append(".TOT_COST as tot");
			sb.append(" FROM ").append(tnL.get(i)).append(" FEE").append(i);
			sb.append("  WHERE EXISTS");
			sb.append(" (SELECT 'x'");
			sb.append(" FROM T_CHARGE_MINFEETOSTAT FEESTAT");
			sb.append(" WHERE FEESTAT.MINFEE_CODE = FEE").append(i).append(".FEE_CODE");
			sb.append(" AND FEESTAT.FEE_STAT_CODE = '10'");
			sb.append(" AND FEESTAT.REPORT_CODE = 'ZY01')");
			sb.append(" AND (FEE").append(i).append(".OPERATION_ID IS NOT NULL OR");
			sb.append(" FEE").append(i).append(".OPERATION_ID != '')");
			
			if(StringUtils.isNotBlank(beginTime)){
				sb.append(" AND FEE").append(i).append(".FEE_DATE >=to_date(:beginTime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(endTime)){
				sb.append(" AND FEE").append(i).append(".FEE_DATE <to_date(:endTime, 'yyyy-MM-dd')");
			}
			 
			sb.append(" GROUP BY FEE").append(i).append(".OPERATION_ID,FEE").append(i).append(".TOT_COST) TT");
			sb.append(" GROUP BY TT.OPERATION_ID) FEEINFO");
			sb.append(" WHERE RCD.OPERATION_ID = FEEINFO.OPERATION_ID");
			if(StringUtils.isNotBlank(execDept)){
				sb.append(" AND RCD.EXEC_DEPT in (:execDept) ");
			} 
			sb.append("  AND RCD.YNVALID = 1");
			if(StringUtils.isNotBlank(opcDept)){
				sb.append("  AND RCD.DOC_DPCD in (:opcDept) ");
			}
			
		}
			sb.append(" ) AA");
			sb.append(" GROUP BY AA.DOC_DPCD");
			sb.append(" ) tt");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("DEL_FLG", 0);
		if(StringUtils.isNotBlank(execDept)){
			   paraMap.put("execDept", Arrays.asList(execDepts));
		   }
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime",DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(endTime),1))) );
		}
		if(StringUtils.isNotBlank(opcDept)){
			paraMap.put("opcDept",Arrays.asList(opcDepts) );
		}
		List<OpDeptTotalVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<OpDeptTotalVo>() {
			@Override
			public OpDeptTotalVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OpDeptTotalVo vo = new OpDeptTotalVo();
				vo.setOpDoctorDept(rs.getString("opDoctorDept"));
				vo.setZqsl(rs.getInt("zqsl"));
				vo.setZqje(rs.getDouble("zqje"));
				vo.setPtsl(rs.getInt("ptsl"));
				vo.setPtje(rs.getDouble("ptje"));
				vo.setJzsl(rs.getInt("jzsl"));
				vo.setJzje(rs.getDouble("jzje"));
				vo.setGrsl(rs.getInt("grsl"));
				vo.setGrje(rs.getDouble("grje"));
				vo.setHjsl(rs.getInt("hjsl"));
				vo.setHjje(rs.getDouble("hjje"));
				vo.setLtje(rs.getDouble("ltje"));
				vo.setPjje(rs.getDouble("pjje"));
				return vo;
		}});
		
		return voList;
	}
	/**
	 * 获取最大最小时间
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @return StatVo
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	private StatVo findMaxMin() {
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT MAX(mn.FEE_DATE) AS eTime ,MIN(mn.FEE_DATE) AS sTime FROM T_INPATIENT_ITEMLIST_NOW mn ");
		final String  sql=sb.toString();
		StatVo sta=(StatVo)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return sta;
	}

	/**
	 * @Description:根据条件查询科室明细中间 方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间 
	 * @param:execDept 执行科室  
	 * @param:page 当前页数  
	 * @param:rows 分页条数 
	 * @param:opcDept 医生科室
	 * @param:opDoctor手术医生
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OpDeptDetailVo> getOpDeptDetailVo2(String beginTime, String endTime,String opcDept, String execDept, String opDoctor,
			String page, String rows) {
		String [] opcDepts=opcDept.split(",");
		//执行科室
		String [] execDepts=execDept.split(",");
		if(StringUtils.isBlank(beginTime)){
			beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			 endTime=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beginTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",beginTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<OpDeptDetailVo>();
		}
		String sql=this.getOpDeptDetailHql(beginTime, endTime, opcDept, execDept, opDoctor,tnL);
		StringBuffer bufferRows = new StringBuffer(sql);
		bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		bufferRows.append(") tab WHERE ROWNUM <= :bag ) WHERE rn > :start ");
		String hql=bufferRows.toString();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(execDept)){
			   paraMap.put("execDept", Arrays.asList(execDepts));
		   }
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime",DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(endTime),1))) );
		}
		if(StringUtils.isNotBlank(opDoctor)){
			paraMap.put("opDoctor", Arrays.asList(opDoctor.split(",")));
		}
		if(StringUtils.isNotBlank(opcDept)){
			paraMap.put("opcDept", Arrays.asList(opcDepts));
		}
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("start", (start - 1) * count);
		paraMap.put("bag", start * count);
		List<OpDeptDetailVo> voList =  namedParameterJdbcTemplate.query(hql,paraMap,new RowMapper<OpDeptDetailVo>() {
			@Override
			public OpDeptDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OpDeptDetailVo vo = new OpDeptDetailVo();
				vo.setOpDoctorDept(rs.getString("opDoctorDept"));
				vo.setOpDoctor(rs.getString("opDoctor"));
				vo.setZqsl(rs.getInt("zqsl"));
				vo.setZqje(rs.getDouble("zqje"));
				vo.setPtsl(rs.getInt("ptsl"));
				vo.setPtje(rs.getDouble("ptje"));
				vo.setJzsl(rs.getInt("jzsl"));
				vo.setJzje(rs.getDouble("jzje"));
				vo.setGrsl(rs.getInt("grsl"));
				vo.setGrje(rs.getDouble("grje"));
				vo.setHjsl(rs.getInt("hjsl"));
				vo.setHjje(rs.getDouble("hjje"));
				vo.setLtje(rs.getDouble("ltje"));
				
				return vo;
		}});
		return voList;
		
	}
	/**
	 * @Description:按医生科室分组查询手术申请信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationApply> getOpApplyByDept() {
		String sql="select a.EXEC_DEPT as execDept from t_operation_apply a   where a.stop_flg = 0 and a.del_flg = 0  and a.EXEC_DEPT is not null group by a.EXEC_DEPT";
		SQLQuery query=getSession().createSQLQuery(sql).addScalar("execDept");
		List<OperationApply> datas=query.setResultTransformer(Transformers.aliasToBean(OperationApply.class)).list();
		if(datas.size()>0&&datas!=null){
			return datas;
		}
		return new ArrayList<OperationApply>();
	}
	/**
	 * @Description:按医生科室，医生分组查询手术申请信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationApply> getOpApplyByDocDept() {
		String sql="select a.op_doctordept as opDoctordept,a.op_doctor as opDoctor from t_operation_apply a where a.stop_flg = 0 and a.del_flg = 0 group by a.op_doctordept,a.op_doctor";
		SQLQuery query=getSession().createSQLQuery(sql).addScalar("opDoctordept").addScalar("opDoctor");
		List<OperationApply> datas=query.setResultTransformer(Transformers.aliasToBean(OperationApply.class)).list();
		if(datas.size()>0&&datas!=null){
			return datas;
		}
		return new ArrayList<OperationApply>();
	}
	/**
	 * @Description:查询该科室下的手术申请记录
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationApply> findOpApplyByDocDept(String execDept) {
		String sql="select a.op_doctor as opDoctor from t_operation_apply a where a.stop_flg = 0 and a.del_flg = 0 and a.op_doctordept= '"+execDept+"' group by a.op_doctor";
		SQLQuery query=getSession().createSQLQuery(sql).addScalar("opDoctor");
		List<OperationApply> datas=query.setResultTransformer(Transformers.aliasToBean(OperationApply.class)).list();
		if(datas.size()>0&&datas!=null){
			return datas;
		}
		return new ArrayList<OperationApply>();
		
	}
	
	/**
	 * @Description:查询该医生的手术申请记录
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationApply>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationApply> findOpApplyByDoc(String opDoctor) {
		String sql="select a.op_doctordept as opDoctorDept from t_operation_apply a where a.stop_flg = 0 and a.del_flg = 0 and a.op_doctor= '"+opDoctor+"' group by a.op_doctordept";
		SQLQuery query=getSession().createSQLQuery(sql).addScalar("opDoctorDept");
		List<OperationApply> datas=query.setResultTransformer(Transformers.aliasToBean(OperationApply.class)).list();
		if(datas.size()>0&&datas!=null){
			return datas;
		}
		return new ArrayList<OperationApply>();
		
	}
	
	/**
	 *根据条件查询手术科室明细
	 * @Description:根据条件查询手术科室明细
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OpDeptDetailVo> getOpDeptDetailVo(String beginTime, String endTime,String opcDept, String execDept,String opDoctor,
			String page, String rows) {
		List<OpDeptDetailVo> listDeptDetail = new ArrayList<OpDeptDetailVo>();
		List<OpDeptDetailVo> listDeptDetail2 = new ArrayList<OpDeptDetailVo>();
		List<OperationApply> list = new ArrayList<OperationApply>();
		listDeptDetail=this.getOpDeptDetailVo2(beginTime, endTime, opcDept,execDept, opDoctor, page, rows);
		return listDeptDetail;
	}
	/**
	 * @Description:将科室明细的hql语句封装成一个方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   execDept 执行科室 
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public String getOpDeptDetailHql(String beginTime, String endTime,String opcDept, String execDept,String opDoctor,List<String> tnL) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select tt.DOC_DPCD as opDoctorDept, tt.OPS_DOCD as opDoctor,tt.zqsl,tt.zqje,tt.ptsl,tt.ptje,tt.jzsl,tt.jzje,tt.grsl,tt.grje,"
				+ "(tt.zqsl+tt.ptsl+tt.jzsl+tt.grsl)as hjsl,(tt.zqje+tt.ptje+ tt.jzje+tt.grje)as hjje,(0) as ltje,"
				+ "round(decode((tt.zqje+tt.ptje+ tt.jzje+tt.grje), 0, 0,(tt.zqje+tt.ptje+ tt.jzje+tt.grje) / (tt.zqsl+tt.ptsl+tt.jzsl+tt.grsl)),4) as pjje from (");
		sb.append(" SELECT AA.DOC_DPCD,AA.OPS_DOCD,");
		sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '0', OPERATION_ID, NULL)) zqsl,");
		sb.append(" NVL(SUM(DECODE(OPS_KIND, '0', TOT_COST, 0)), 0) zqje,");
		sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '1', OPERATION_ID, NULL)) ptsl,");
		sb.append(" NVL(SUM(DECODE(OPS_KIND, '1', TOT_COST, 0)), 0) ptje,");
		sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '2', OPERATION_ID, NULL)) jzsl,");
		sb.append(" NVL(SUM(DECODE(OPS_KIND, '2', TOT_COST, 0)), 0) jzje,");
		sb.append(" COUNT(DISTINCT DECODE(OPS_KIND, '3', OPERATION_ID, NULL)) grsl,");
		sb.append(" NVL(SUM(DECODE(OPS_KIND, '3', TOT_COST, 0)), 0) grje");
		sb.append(" FROM (");
	for(int i=0;i<tnL.size();i++){
		
		if(i>0){
			sb.append(" UNION ALL ");
		}
		
		sb.append(" SELECT RCD.OPERATION_ID,");
		sb.append(" RCD.DOC_DPCD,RCD.OPS_DOCD,");
		sb.append(" RCD.OPS_KIND,");
		sb.append(" FEEINFO.TOTCOST TOT_COST");
		sb.append(" FROM T_OPERATION_RECORD RCD,");
		sb.append(" (SELECT TT.OPERATION_ID, SUM(TOT) TOTCOST");
		sb.append(" FROM (SELECT FEE").append(i).append(".OPERATION_ID,");
		sb.append("  FEE").append(i).append(".TOT_COST as tot");
		sb.append(" FROM ").append(tnL.get(i)).append(" FEE").append(i);
		sb.append("  WHERE EXISTS");
		sb.append(" (SELECT 'x'");
		sb.append(" FROM T_CHARGE_MINFEETOSTAT FEESTAT");
		sb.append(" WHERE FEESTAT.MINFEE_CODE = FEE").append(i).append(".FEE_CODE");
		sb.append(" AND FEESTAT.FEE_STAT_CODE = '10'");
		sb.append(" AND FEESTAT.REPORT_CODE = 'ZY01')");
		sb.append(" AND (FEE").append(i).append(".OPERATION_ID IS NOT NULL OR");
		sb.append(" FEE").append(i).append(".OPERATION_ID != '')");
		
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" AND FEE").append(i).append(".FEE_DATE >=to_date(:beginTime, 'yyyy-MM-dd')");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" AND FEE").append(i).append(".FEE_DATE <to_date(:endTime, 'yyyy-MM-dd')");
		}
		 
		sb.append(" GROUP BY FEE").append(i).append(".OPERATION_ID,FEE").append(i).append(".TOT_COST) TT");
		sb.append(" GROUP BY TT.OPERATION_ID) FEEINFO");
		sb.append(" WHERE RCD.OPERATION_ID = FEEINFO.OPERATION_ID");
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" AND RCD.EXEC_DEPT in (:execDept) ");
		} 
		sb.append("  AND RCD.YNVALID = 1");
		if(StringUtils.isNotBlank(opcDept)){
			sb.append("  AND RCD.DOC_DPCD in (:opcDept )");
		}
		if(StringUtils.isNotBlank(opDoctor)){
			sb.append("  AND RCD.OPS_DOCD in (:opDoctor) ");
		}
	}
		sb.append(" ) AA");
		sb.append(" GROUP BY AA.DOC_DPCD,AA.OPS_DOCD");
		sb.append(" ) tt");
		return sb.toString();
	}
	/**
	 * 根据条件手术医生明细总记录数
	 * @Description:根据条件手术医生明细总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getOpDoctorTotal(String beginTime, String endTime, String opcDept,String execDept,String opDoctor, String identityCard) {
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isBlank(beginTime)){
			beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			 endTime=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beginTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",beginTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  0;
		}
		if(tnL.size()>1){
			sb.append(" select tt.opDoctorDept,tt.opDoctor,tt.inpatientNo,tt.preDate,"
					+ "tt.name,tt.itemName,tt.execDept,tt.feeOperCode,tt.totCost  from (");
		}
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" UNION ALL ");
			}
			sb.append(" SELECT ");
			sb.append(" C.DOC_DPCD as opDoctorDept,");
			sb.append(" C.OPS_DOCD as opDoctor,");
			sb.append(" C.OPERATIONDATE as preDate,");
			sb.append(" C.PATIENT_NO as inpatientNo,");
			sb.append(" C.NAME as name, ");
			sb.append(" (select listagg(ITEM_NAME, ',') within");
			sb.append(" group(");
			sb.append(" order by null ) from t_Operation_Item it where it.operation_id=c.operation_id)as itemName,");
			sb.append(" SUM( D").append(i).append(".TOT_COST) as  totCost, ");
			sb.append(" D").append(i).append(".FEEOPER_DEPTCODE as execDept,");
			sb.append(" D").append(i).append(".FEE_OPERCODE as feeOperCode");
			sb.append(" from t_Operation_Record C,").append(tnL.get(i)).append(" D").append(i);
			if(StringUtils.isNotBlank(identityCard)){
				sb.append( "  ,t_patient p  ");
			}
			sb.append(" WHERE   C.OPERATION_ID = D").append(i).append(".OPERATION_ID");
			sb.append(" AND C.YNVALID=1");
			if(StringUtils.isNotBlank(beginTime)){
				sb.append(" AND D").append(i).append(".FEE_DATE >= TO_DATE(:beginTime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(endTime)){
				sb.append(" AND D").append(i).append(".FEE_DATE  < TO_DATE(:endTime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(execDept)){
				sb.append(" AND C.EXEC_DEPT in (:execDept) ");
			}
			if(StringUtils.isNotBlank(opcDept)){
				sb.append(" AND C.DOC_DPCD in (:opcDept)");
			}
			if(StringUtils.isNotBlank(opDoctor)){
				sb.append(" AND C.DOC_DPCD in (:opDoctor)");
			}
			if(StringUtils.isNotBlank(identityCard)){
				sb.append( "  and p.MEDICALRECORD_ID=C.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:identityCard ");
			}
			sb.append(" AND D").append(i).append(".TRANS_TYPE = '1'");
			sb.append(" AND EXISTS (");
			sb.append(" SELECT 'X' FROM T_CHARGE_MINFEETOSTAT FEESTAT");
			sb.append(" WHERE ");
			sb.append(" FEESTAT.MINFEE_CODE=D").append(i).append(".FEE_CODE");
			sb.append(" AND FEESTAT.FEE_STAT_CODE = '10' ");
			sb.append(" AND FEESTAT.REPORT_CODE = 'ZY01'");
			sb.append(" )");
			sb.append(" AND (C.DOC_DPCD = 'ALL' OR 'ALL' = 'ALL')");
			sb.append(" AND (C.OPS_DOCD = 'ALL' OR 'ALL' = 'ALL')");
			sb.append(" GROUP BY ");
			sb.append(" C.DOC_DPCD,");
			sb.append(" C.OPS_DOCD,");
			sb.append("  C.OPERATIONDATE,");
			sb.append(" C.OPERATION_ID,");
			sb.append(" C.PATIENT_NO,");
			sb.append(" C.NAME, ");
			sb.append(" D").append(i).append(".FEEOPER_DEPTCODE,");
			sb.append(" D").append(i).append(".FEE_OPERCODE");
			
		}
		if(tnL.size()>1){
			sb.append(" ) tt");
			//hedong 20170228 添加排序（手术时间降序）
			sb.append(" ORDER BY preDate desc ");
		}
		
		StringBuffer bufferTotal = new StringBuffer(sb.toString());
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		Map<String,Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(execDept)){
			   paraMap.put("execDept",Arrays.asList(execDept.split(",")));
		   }
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime",DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(endTime),1))) );
		}
		if(StringUtils.isNotBlank(opDoctor)){
			paraMap.put("opDoctor",Arrays.asList(opDoctor.split(",")));
		}
		if(StringUtils.isNotBlank(opcDept)){
			paraMap.put("opcDept", Arrays.asList(opcDept.split(",")));
		}
		if(StringUtils.isNotBlank(identityCard)){
			paraMap.put("identityCard", identityCard);
		}
		int total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
		return total;
	}
	/**
	 * 根据条件查询手术科室明细(总条数)
	 * @Description:根据条件查询手术科室明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getOpDeptDetailTotal(String beginTime, String endTime,String opcDept,
			String execDept, String opDoctor) {
		if(StringUtils.isBlank(beginTime)){
			beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			 endTime=DateUtils.getDate();
		}
		String [] opcDepts=opcDept.split(",");
		String [] execDepts=execDept.split(",");
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beginTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",beginTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		String sql=this.getOpDeptDetailHql(beginTime, endTime,opcDept, execDept, opDoctor,tnL);
		StringBuffer bufferTotal = new StringBuffer(sql);
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		String hql=bufferTotal.toString();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(execDept)){
			   paraMap.put("execDept", Arrays.asList(execDepts));
		   }
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime",DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(endTime),1))) );
		}
		if(StringUtils.isNotBlank(opDoctor)){
			paraMap.put("opDoctor",opDoctor);
		}
		if(StringUtils.isNotBlank(opcDept)){
			paraMap.put("opcDept", Arrays.asList(opcDepts));
		}
		int total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
		return total;
	}
	/**
	 * 手术科室汇总>手术医生明细报表打印
	 * @Description:导出手术医生明细
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @param:deptMap 部门map
	 * @param:empMap 员工表Map
	 * @param:identityCard 身份证号
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OpDoctorDetailVo> getOpDoctorDetailToReport(String beginTime, String endTime, String opcDept,
			String execDept, String opDoctor, final Map<String, String> deptMap, final Map<String, String> empMap, String identityCard) {
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isBlank(beginTime)){
			beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			 endTime=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beginTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",beginTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<OpDoctorDetailVo>();
		}
		if(tnL.size()>1){
			sb.append(" select tt.opDoctorDept,tt.opDoctor,tt.inpatientNo,tt.preDate,"
					+ "tt.name,tt.itemName,tt.execDept,tt.feeOperCode,tt.totCost  from (");
		}
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" UNION ALL ");
			}
			sb.append(" SELECT ");
			sb.append(" C.DOC_DPCD as opDoctorDept,");
			sb.append(" C.OPS_DOCD as opDoctor,");
			sb.append(" C.OPERATIONDATE as preDate,");
			sb.append(" C.PATIENT_NO as inpatientNo,");
			sb.append(" C.NAME as name, ");
			sb.append(" (select listagg(ITEM_NAME, ',') within");
			sb.append(" group(");
			sb.append(" order by null ) from t_Operation_Item it where it.operation_id=c.operation_id)as itemName,");
			sb.append(" SUM( D").append(i).append(".TOT_COST) as  totCost, ");
			sb.append(" D").append(i).append(".FEEOPER_DEPTCODE as execDept,");
			sb.append(" D").append(i).append(".FEE_OPERCODE as feeOperCode");
			sb.append(" from t_Operation_Record C,").append(tnL.get(i)).append(" D").append(i);
			if(StringUtils.isNotBlank(identityCard)){
				sb.append( "  ,t_patient p  ");
			}
			sb.append(" WHERE   C.OPERATION_ID = D").append(i).append(".OPERATION_ID");
			sb.append(" AND C.YNVALID=1");
			if(StringUtils.isNotBlank(beginTime)){
				sb.append(" AND D").append(i).append(".FEE_DATE >= TO_DATE(:beginTime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(endTime)){
				sb.append(" AND D").append(i).append(".FEE_DATE  < TO_DATE(:endTime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(execDept)){
				sb.append(" AND C.EXEC_DEPT in (:execDept )");
			}
			if(StringUtils.isNotBlank(opcDept)){
				sb.append(" AND C.DOC_DPCD in (:opcDept)");
			}
			if(StringUtils.isNotBlank(opDoctor)){
				sb.append(" AND C.DOC_DPCD in (:opDoctor)");
			}
			if(StringUtils.isNotBlank(identityCard)){
				sb.append( "  and p.MEDICALRECORD_ID=C.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:identityCard ");
			}
			sb.append(" AND D").append(i).append(".TRANS_TYPE = '1'");
			sb.append(" AND EXISTS (");
			sb.append(" SELECT 'X' FROM T_CHARGE_MINFEETOSTAT FEESTAT");
			sb.append(" WHERE ");
			sb.append(" FEESTAT.MINFEE_CODE=D").append(i).append(".FEE_CODE");
			sb.append(" AND FEESTAT.FEE_STAT_CODE = '10' ");
			sb.append(" AND FEESTAT.REPORT_CODE = 'ZY01'");
			sb.append(" )");
			
			sb.append(" AND (C.DOC_DPCD = 'ALL' OR 'ALL' = 'ALL')");
			sb.append(" AND (C.OPS_DOCD = 'ALL' OR 'ALL' = 'ALL')");
			
			sb.append(" GROUP BY ");
			sb.append(" C.DOC_DPCD,");
			sb.append(" C.OPS_DOCD,");
			sb.append("  C.OPERATIONDATE,");
			sb.append(" C.OPERATION_ID,");
			sb.append(" C.PATIENT_NO,");
			sb.append(" C.NAME, ");
			sb.append(" D").append(i).append(".FEEOPER_DEPTCODE,");
			sb.append(" D").append(i).append(".FEE_OPERCODE");
		}
		if(tnL.size()>1){
			sb.append(" ) tt");
			//hedong 20170228 添加排序（手术时间降序）
			sb.append(" ORDER BY preDate desc");
		}
		   String hql="";
		   Map<String, Object> paraMap = new HashMap<String, Object>();
			hql=sb.toString();
			if(StringUtils.isNotBlank(execDept)){
				   paraMap.put("execDept", Arrays.asList(execDept.split(",")));
			   }
			if(StringUtils.isNotBlank(beginTime)){
				paraMap.put("beginTime", beginTime);
			}
			if(StringUtils.isNotBlank(endTime)){
				paraMap.put("endTime",DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(endTime),1))) );
			}
			if(StringUtils.isNotBlank(opDoctor)){
				paraMap.put("opDoctor",Arrays.asList(opDoctor.split(",")));
			}
			if(StringUtils.isNotBlank(opcDept)){
				paraMap.put("opcDept", Arrays.asList(opcDept.split(",")));
			}
			if(StringUtils.isNotBlank(identityCard)){
				paraMap.put("identityCard", identityCard);
			}
		List<OpDoctorDetailVo> voList =  namedParameterJdbcTemplate.query(hql,paraMap,new RowMapper<OpDoctorDetailVo>() {
			@Override
			public OpDoctorDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OpDoctorDetailVo vo = new OpDoctorDetailVo();
				
				vo.setTotCost(rs.getDouble("totCost"));
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setPreDate(rs.getTimestamp("preDate"));
				vo.setName(rs.getString("name"));
				vo.setItemName(rs.getString("itemName"));
				
				vo.setOpDoctorDept(deptMap.get(rs.getString("opDoctorDept")));
				vo.setExecDept(deptMap.get(rs.getString("execDept")));
				vo.setOpDoctor(empMap.get(rs.getString("opDoctor")));
				vo.setFeeOperCode(empMap.get(rs.getString("feeOperCode")));
				
				return vo;
		}});
		return voList;
	}
	
	/**
	 * @Description 查询科室信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<MenuListVO> querysysDeptment() {
		List<MenuListVO> depts=new ArrayList<MenuListVO>();
		List<MenuVO> voList = new ArrayList<MenuVO>();
		StringBuffer sb=new StringBuffer();
		sb.append(" from SysDepartment cs  where cs.del_flg = 0 and  cs.deptType in ('C', 'I') ");
		List<SysDepartment> list=super.find(sb.toString(), null);
		for(SysDepartment sysDep : list){
			MenuVO vo=new MenuVO();
			vo.setId(sysDep.getDeptCode());
			vo.setName(sysDep.getDeptName());
			vo.setType(sysDep.getDeptType());
			voList.add(vo);
		}
		
		String[] arr=new String[]{"C-门诊","I-住院"};
		for(int i=0;i<arr.length;i++){
			String[] arr1=arr[i].split("-");
			MenuListVO d=new MenuListVO();
			d.setParentMenu(arr1[1]);
			List<MenuVO> rs=new ArrayList<MenuVO>();
			for(MenuVO v:voList){
				if(arr1[0].equals(v.getType())){
					rs.add(v);
				}				
			}
			d.setMenus(rs);
			depts.add(d);
		}
		return depts;
	
	}
	
	/**
	 * @Description 查询医生信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param deptCode科室code
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<MenuListVO> queryDoctor(String deptCode) {
		StringBuffer sb=new StringBuffer();
		List<MenuListVO> depts=new ArrayList<MenuListVO>();
		StringBuffer buffer = new StringBuffer("SELECT distinct ");
		buffer.append("e.DEPT_ID as deptId, ");
		buffer.append("d.DEPT_TYPE as type, ");
		buffer.append("e.EMPLOYEE_JOBNO as jobNo, ");
		buffer.append("e.EMPLOYEE_NAME as name, ");
		buffer.append("e.EMPLOYEE_PINYIN as pinyin, ");
		buffer.append("e.EMPLOYEE_WB as wb, ");
		buffer.append("e.EMPLOYEE_INPUTCODE as inputCode ");
		buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_EMPLOYEE e");
		buffer.append(" INNER JOIN ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_DEPARTMENT d");
		buffer.append(" on e.DEPT_ID=d.DEPT_ID");
		buffer.append(" left join t_department_contact f on f.DEPT_ID=e.DEPT_ID ");
		buffer.append(" WHERE e.STOP_FLG=0");
		buffer.append(" AND e.DEL_FLG=0 and e.EMPLOYEE_TYPE='1'");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append(" and (e.DEPT_id='"+deptCode+"' or f.PARDEPT_ID in(select t.id from t_department_contact t"
					+ " where t.dept_code = '"+deptCode+"'))");
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<MenuVO> voList = namedParameterJdbcTemplate.query(buffer.toString(),paraMap, new RowMapper<MenuVO>(){
			@Override
			public MenuVO mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				MenuVO vo = new MenuVO();
				vo.setRelativeId(rs.getString("deptId"));
				vo.setType(rs.getString("type"));
				vo.setId(rs.getString("jobNo"));
				vo.setName(rs.getString("name"));
				vo.setInputCode(rs.getString("inputCode"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setWb(rs.getString("wb"));
				return vo;
			}
		});
		List<MenuListVO> doctors=new ArrayList<MenuListVO>();
		String[] arr=new String[]{"C-门诊","I-住院","F-财务","L-后勤","PI-药库","T-医技(终端)","0-其它","D-机关(部门)","P-药房","N-护士站","S-科研","O-其他","OP-手术","U-自定义"};
		
		for(int i=0;i<arr.length;i++){
			String[] arr1=arr[i].split("-");
			MenuListVO d=new MenuListVO();
			d.setParentMenu(arr1[1]);
			List<MenuVO> rs=new ArrayList<MenuVO>();
			for(MenuVO v:voList){
				if(arr1[0].equals(v.getType())){
					rs.add(v);
				}				
			}
			d.setMenus(rs);
			doctors.add(d);
		}
		if(doctors!=null&&doctors.size()>0){
			return doctors;
		}
		return new ArrayList<MenuListVO>();
	}
	
}