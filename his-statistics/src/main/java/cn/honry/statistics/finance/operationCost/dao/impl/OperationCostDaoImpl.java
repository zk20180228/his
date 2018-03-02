package cn.honry.statistics.finance.operationCost.dao.impl;
/***
 * 手术费用汇总DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.MenuVO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.operationCost.dao.OperationCostDao;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVo;

@Repository("operationCostDao")
@SuppressWarnings({"all"})
public class OperationCostDaoImpl extends HibernateEntityDao<OperationCostVo> implements OperationCostDao{
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
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
	/**  
	 * 
	 * 手术费用汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationCostVo> OperationCostVo(String beganTime, String endTime,
			String inpatientNo, String execDept,String page,String rows,List<String> tnL, String identityCard) {
		String execDepts = execDept.replace(",", "','");
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OperationCostVo>();
		}
		long ss = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * from (");
		sb.append(" select ROWNUM AS n, medicalrecordId, name,totCost,feeDate ,execDeptName, execDept from (");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" UNION aLL ");
			}
			
		sb.append("select p.MEDICALRECORD_ID as medicalrecordId, t").append(i).append(".name as name,sum(t").append(i).append(".tot_cost) as totCost,t").append(i).append(".fee_date as feeDate, ");
		sb.append(" t").append(i).append(".EXECUTE_DEPTNAME as execDeptName,");
		sb.append(" t").append(i).append(".execute_deptcode as execDept");
		sb.append(" from ").append(tnL.get(i)).append(" t").append(i);
		if("T_INPATIENT_FEEINFO_now".equals(tnL.get(i))){
			sb.append( " inner join  T_INPATIENT_INFO_now p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
		}else{
			sb.append( " inner join  T_INPATIENT_INFO p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append(" and p.CERTIFICATES_NO='"+identityCard+"' ");
		}
		if(StringUtils.isNotBlank(inpatientNo)){
			sb.append(" and p.MEDICALRECORD_ID ='"+inpatientNo+"' ");
		}
		sb.append(" where t").append(i).append(".fee_code in (select minfee_code from T_CHARGE_MINFEETOSTAT b where fee_stat_code= '10' and report_code = 'ZY01')");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and trunc(t").append(i).append(".fee_date,'dd') >= to_date('"+beganTime+"','yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and trunc(t").append(i).append(".fee_date,'dd') <= to_date('"+endTime+"','yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and t").append(i).append(".execute_deptcode in( '").append(execDepts).append("' )");
		}
		
		sb.append(" group by  p.MEDICALRECORD_ID, name, execute_deptcode,fee_date, EXECUTE_DEPTNAME ");
		}
		
		sb.append(" ) where rownum <= :page * :rows ");
		sb.append(" order by feeDate");
		sb.append(")  where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<OperationCostVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<OperationCostVo>() {
			@Override
			public OperationCostVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OperationCostVo vo = new OperationCostVo();
				vo.setMedicalrecordId(rs.getString("medicalrecordId"));
				vo.setName(rs.getString("name"));
				vo.setTotCost(rs.getDouble("totCost"));
				vo.setFeeDate(rs.getTimestamp("feeDate"));
				vo.setExecDept(rs.getString("execDept"));
				return vo;
		}});
		
		return voList;
		
	}

	/**
	 * @Description:查询所有科室
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<SysDepartment>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysDepartment> depMentList() {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> depList = super.find(hql, null);
		if(depList!=null&&depList.size()>0){
			return depList;
		}
		return new ArrayList<SysDepartment>();
	}
	/**
	 * @Description:根据条件查询手术费用汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getTotal(String beganTime, String endTime, String inpatientNo, String execDept) {
		StringBuffer sb = new StringBuffer();
		sb.append("select inpatient_no as inpatientNo, name as name, execute_deptcode as execDept,sum(tot_cost) as totCost, fee_date as feeDate ");
		sb.append(" from T_INPATIENT_FEEINFO");
		sb.append(" where fee_code in (select minfee_code from T_CHARGE_MINFEETOSTAT b where fee_stat_code= '10' and report_code = 'ZY01')");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and to_char(fee_date,'yyyy-MM-dd') >= '"+beganTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(fee_date,'yyyy-MM-dd') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and execute_deptcode in( '"+Arrays.asList(execDept.split(","))+"') ");
		}
		if(StringUtils.isNotBlank(inpatientNo)){
			sb.append(" and inpatient_no ='"+inpatientNo+"' ");
		}
		sb.append("group by inpatient_no, name, execute_deptcode, fee_date order by fee_date");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inpatientNo").addScalar("name").addScalar("execDept")
								.addScalar("totCost",Hibernate.DOUBLE).addScalar("feeDate",Hibernate.DATE);
		List<OperationCostVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OperationCostVo.class)).list();
		return list.size();
	}

	/**
	 * @Description:根据条件查询所有手术费用汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationCostVo> allOperationCostVo(String beganTime,
			String endTime, String patientNo, String execDept) {
		StringBuffer sb = new StringBuffer();
		sb.append("select inpatient_no as inpatientNo, name as name, execute_deptcode as execDept,sum(tot_cost) as totCost, fee_date as feeDate ");
		sb.append(" from T_INPATIENT_FEEINFO");
		sb.append(" where fee_code in (select minfee_code from T_CHARGE_MINFEETOSTAT b where fee_stat_code= '10' and report_code = 'ZY01')");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and to_char(fee_date,'yyyy-MM-dd') >= '"+beganTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(fee_date,'yyyy-MM-dd') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and execute_deptcode = '"+execDept+"' ");
		}
		if(StringUtils.isNotBlank(patientNo)){
			sb.append(" and inpatient_no='"+patientNo+"' ");
		}
		sb.append("group by inpatient_no, name, execute_deptcode, fee_date order by fee_date");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inpatientNo").addScalar("name").addScalar("execDept")
								.addScalar("totCost",Hibernate.DOUBLE).addScalar("feeDate",Hibernate.DATE);
		List<OperationCostVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OperationCostVo.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<OperationCostVo>();
	}

	/**
	 * @Description:获取最大最小时间
	 * @Author: zhangjin
	 * @CreateDate: 2016年12月30日 
	 * @param:b
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public StatVo findMaxMin() {
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT MAX(mn.fee_date) AS eTime ,MIN(mn.fee_date) AS sTime FROM T_INPATIENT_FEEINFO_now mn ");
		final String  sql=sb.toString();
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return vo;
	}

	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:36:23 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:36:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<MenuListVO> getDeptList() {
		List<MenuListVO> depts=new ArrayList<MenuListVO>();
		List<MenuVO> voList = new ArrayList<MenuVO>();
		List<SysDepartment> sysDepList =deptInInterService.queryAllDept();
		for(SysDepartment sysDep : sysDepList){
			MenuVO vo=new MenuVO();
			vo.setId(sysDep.getDeptCode());
			vo.setName(sysDep.getDeptName());
			vo.setType(sysDep.getDeptType());
			voList.add(vo);
		}
		String[] arr=new String[]{"C-门诊","I-住院","N-护士站","OP-手术"};
			
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
	 * 
	 * 手术费用汇总(分页总条数)
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public int queryOperationCostTotal(String beganTime, String endTime,
			String inpatientNo, String execDept, List<String> tnL,
			String identityCard) {
		String execDepts = execDept.replace(",", "','");
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		long ss = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		sb.append("select count(1) from( ");
		sb.append(" select medicalrecordId from (");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" UNION aLL ");
			}
			
		sb.append("select p.MEDICALRECORD_ID as medicalrecordId, t").append(i).append(".name as name,sum(t").append(i).append(".tot_cost) as totCost,t").append(i).append(".fee_date as feeDate, ");
		sb.append(" t").append(i).append(".EXECUTE_DEPTNAME as execDeptName,");
		sb.append(" t").append(i).append(".execute_deptcode as execDept");
		sb.append(" from ").append(tnL.get(i)).append(" t").append(i);
		if("T_INPATIENT_FEEINFO_now".equals(tnL.get(i))){
			sb.append( " inner join  T_INPATIENT_INFO_now p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
		}else{
			sb.append( " inner join  T_INPATIENT_INFO p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append(" and p.CERTIFICATES_NO='"+identityCard+"' ");
		}
		if(StringUtils.isNotBlank(inpatientNo)){
			sb.append(" and p.MEDICALRECORD_ID ='"+inpatientNo+"' ");
		}
		sb.append(" where t").append(i).append(".fee_code in (select minfee_code from T_CHARGE_MINFEETOSTAT b where fee_stat_code= '10' and report_code = 'ZY01')");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and trunc(t").append(i).append(".fee_date,'dd') >= to_date('"+beganTime+"','yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and trunc(t").append(i).append(".fee_date,'dd') <= to_date('"+endTime+"','yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and t").append(i).append(".execute_deptcode in( '").append(execDepts).append("' )");
		}
		
		sb.append(" group by  p.MEDICALRECORD_ID, name, execute_deptcode,fee_date, EXECUTE_DEPTNAME ");
		}
		
		sb.append(" ) ");
		sb.append("  order by feeDate");
		sb.append(")");
		
		 return jdbcTemplate.queryForObject(sb.toString(), java.lang.Integer.class);
	}
	
	/**
	 * @Description:根据条件查询手术费用汇总(导出及打印)
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； inpatientNo 住院流水号； execDept 执行科室 identityCard 身份证号
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @param  
	 */
	@Override
	public List<OperationCostVo> queryOperationCostOther(String beganTime, String endTime, String inpatientNo,
			String execDept, List<String> tnL, String identityCard) {
		String execDepts = execDept.replace(",", "','");
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OperationCostVo>();
		}
		long ss = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		sb.append(" select  medicalrecordId, name,totCost,feeDate ,execDeptName, execDept from (");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" UNION aLL ");
			}
			
		sb.append("select p.MEDICALRECORD_ID as medicalrecordId, t").append(i).append(".name as name,sum(t").append(i).append(".tot_cost) as totCost,t").append(i).append(".fee_date as feeDate, ");
		sb.append(" t").append(i).append(".EXECUTE_DEPTNAME as execDeptName,");
		sb.append(" t").append(i).append(".execute_deptcode as execDept");
		sb.append(" from ").append(tnL.get(i)).append(" t").append(i);
		if("T_INPATIENT_FEEINFO_now".equals(tnL.get(i))){
			sb.append( " inner join  T_INPATIENT_INFO_now p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
		}else{
			sb.append( " inner join  T_INPATIENT_INFO p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append(" and p.CERTIFICATES_NO='"+identityCard+"' ");
		}
		if(StringUtils.isNotBlank(inpatientNo)){
			sb.append(" and p.MEDICALRECORD_ID ='"+inpatientNo+"' ");
		}
		sb.append(" where t").append(i).append(".fee_code in (select minfee_code from T_CHARGE_MINFEETOSTAT b where fee_stat_code= '10' and report_code = 'ZY01')");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and trunc(t").append(i).append(".fee_date,'dd') >= to_date('"+beganTime+"','yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and trunc(t").append(i).append(".fee_date,'dd') <= to_date('"+endTime+"','yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and t").append(i).append(".execute_deptcode in( '").append(execDepts).append("' )");
		}
		
		sb.append(" group by  p.MEDICALRECORD_ID, name, execute_deptcode,fee_date, EXECUTE_DEPTNAME ");
		}
		
		sb.append(" )  ");
		sb.append(" order by feeDate");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<OperationCostVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),new RowMapper<OperationCostVo>() {
			@Override
			public OperationCostVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OperationCostVo vo = new OperationCostVo();
				vo.setMedicalrecordId(rs.getString("medicalrecordId"));
				vo.setName(rs.getString("name"));
				vo.setTotCost(rs.getDouble("totCost"));
				vo.setFeeDate(rs.getTimestamp("feeDate"));
				vo.setExecDept(rs.getString("execDeptName"));
				
				return vo;
		}});
		return voList;
	}

}
