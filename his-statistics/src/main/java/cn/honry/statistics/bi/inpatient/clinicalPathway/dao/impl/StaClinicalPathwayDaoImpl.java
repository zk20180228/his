package cn.honry.statistics.bi.inpatient.clinicalPathway.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.clinicalPathway.dao.StaClinicalPathwayDao;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.ClinicalPathVo;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.InOutDetail;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.analysisClinicalVo;
import cn.honry.utils.DateUtils;

@Repository("staClinicalPathwayDao")
@SuppressWarnings({ "all" })
public class StaClinicalPathwayDaoImpl extends HibernateEntityDao<CpMasterIndex> implements StaClinicalPathwayDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public void test() {
		System.out.println("dao");
	}

	@Override
	public List<ClinicalPathVo> inOutList(String page, String rows,
			String sTime, String eTime, String deptCodeTopL, String inOrOutTopL) {
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):15;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM ( ");
		
		sb.append(" select t.dept_code as deptCode,t.cp_id as nameCode,count(1) as num from t_cp_master_index t   ");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 ");
		if(StringUtils.isNotBlank(deptCodeTopL)){
			sb.append(" and t.dept_code = '"+deptCodeTopL+"'");
		}
		if(StringUtils.isNotBlank(inOrOutTopL)){
			sb.append("and t.outpath_flag = '"+inOrOutTopL+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by t.cp_id,t.dept_code ");
		
		sb.append(") row_ WHERE ROWNUM <= "+p*r+" ) WHERE rownum_ > "+(p-1)*r);
		
		SQLQuery query = super.getSession().createSQLQuery(sb.toString()).addScalar("deptCode").addScalar("nameCode").addScalar("num",Hibernate.BIG_DECIMAL);
		List list = query.setResultTransformer(Transformers.aliasToBean(ClinicalPathVo.class)).list();
		return list;
	}

	@Override
	public Integer inOutNum(String sTime, String eTime, String deptCodeTopL,
			String inOrOutTopL) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from ( ");
		sb.append(" select t.dept_code as deptCode,t.cp_id as nameCode,count(1) as num from t_cp_master_index t   ");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 ");
		if(StringUtils.isNotBlank(deptCodeTopL)){
			sb.append(" and t.dept_code = '"+deptCodeTopL+"'");
		}
		if(StringUtils.isNotBlank(inOrOutTopL)){
			sb.append("and t.outpath_flag = '"+inOrOutTopL+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by t.cp_id,t.dept_code ");
		sb.append(" )");
		
		Object count=getSession().createSQLQuery(sb.toString()).uniqueResult();
		return Integer.valueOf((count==null?0:count).toString());
	}

	@Override
	public List<ClinicalPathVo> notEntryList(String page, String rows,
			String sTime, String eTime, String deptCodeBL) {
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):15;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM ( ");
		
		sb.append(" select e.department as deptCode,t.cp_id as nameCode,count(1) as num   ");
		sb.append(" from t_path_apply t left join t_employee_extend e ");
		sb.append(" on t.approval_user = e.employee_jobno ");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 and t.execute_date is null ");
		if(StringUtils.isNotBlank(deptCodeBL)){
			sb.append(" and e.department_code = '"+deptCodeBL+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by e.department,t.cp_id ");
		
		sb.append(") row_ WHERE ROWNUM <= "+p*r+" ) WHERE rownum_ > "+(p-1)*r);
		
		SQLQuery query = super.getSession().createSQLQuery(sb.toString()).addScalar("deptCode").addScalar("nameCode").addScalar("num",Hibernate.BIG_DECIMAL);
		List list = query.setResultTransformer(Transformers.aliasToBean(ClinicalPathVo.class)).list();
		return list;
	}

	@Override
	public Integer notEntryNum(String sTime, String eTime, String deptCodeBL) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from ( ");
		
		sb.append(" select e.department as deptCode,t.cp_id as nameCode,count(1) as num   ");
		sb.append(" from t_path_apply t left join t_employee_extend e ");
		sb.append(" on t.approval_user = e.employee_jobno ");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 and t.execute_date is null ");
		if(StringUtils.isNotBlank(deptCodeBL)){
			sb.append(" and e.department_code = '"+deptCodeBL+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by e.department,t.cp_id ");
		
		sb.append(" )");
		Object count=getSession().createSQLQuery(sb.toString()).uniqueResult();
		return Integer.valueOf((count==null?0:count).toString());
	}

	@Override
	public List<ClinicalPathVo> variationOutList(String page, String rows,
			String sTime, String eTime, String deptCodeTR, String variationTR) {
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):15;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM ( ");
		
		sb.append(" select t.DEPT_CODE as deptCode,v.VARIATION_CODE as nameCode,count(1) as num from t_cp_variation v   ");
		sb.append(" left join t_cp_master_index t ");
		sb.append(" on v.INPATIENT_NO = t.Inpatient_No ");
		sb.append(" where v.stop_flg = 0 and v.del_flg = 0 ");
		if(StringUtils.isNotBlank(deptCodeTR)){
			sb.append(" and t.DEPT_CODE = '"+deptCodeTR+"'");
		}
		if(StringUtils.isNotBlank(variationTR)){
			sb.append(" and v.variation_direction = '"+variationTR+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(v.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by t.DEPT_CODE,v.VARIATION_CODE ");
		
		sb.append(") row_ WHERE ROWNUM <= "+p*r+" ) WHERE rownum_ > "+(p-1)*r);
		
		SQLQuery query = super.getSession().createSQLQuery(sb.toString()).addScalar("deptCode").addScalar("nameCode").addScalar("num",Hibernate.BIG_DECIMAL);
		List list = query.setResultTransformer(Transformers.aliasToBean(ClinicalPathVo.class)).list();
		return list;
	}

	@Override
	public Integer variationOutNum(String sTime, String eTime,
			String deptCodeTR, String variationTR) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from ( ");
		
		sb.append(" select t.DEPT_CODE as deptCode,v.VARIATION_CODE as nameCode,count(1) as num from t_cp_variation v   ");
		sb.append(" left join t_cp_master_index t ");
		sb.append(" on v.INPATIENT_NO = t.Inpatient_No ");
		sb.append(" where v.stop_flg = 0 and v.del_flg = 0  ");
		if(StringUtils.isNotBlank(deptCodeTR)){
			sb.append(" and t.DEPT_CODE = '"+deptCodeTR+"'");
		}
		if(StringUtils.isNotBlank(variationTR)){
			sb.append(" and v.variation_direction = '"+variationTR+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(v.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by t.DEPT_CODE,v.VARIATION_CODE ");
		
		sb.append(" )");
		Object count=getSession().createSQLQuery(sb.toString()).uniqueResult();
		return Integer.valueOf((count==null?0:count).toString());
	}

	@Override
	public List<InOutDetail> inOutDetailList(String page, String rows,
			String sTime, String eTime, String deptCodeBR, String inOrOutBR,
			String sexCode) {
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):15;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM ( ");
		
		sb.append(" select i.DEPT_CODE as deptCode,t.INPATIENT_NO as inpatientNo,i.patient_name as inpatientName,   ");
		sb.append(" i.report_sex as sexCode,i.report_age as age,i.report_ageunit as ageUnit, ");
		sb.append(" t.cp_id as cpId,t.createtime as inPathTime,t.outpathtime as outPathTime,t.OUTP_TYPE_CODE as outpTypeCode from t_cp_master_index t ");
		sb.append(" inner join t_inpatient_info_now i ");
		sb.append(" on t.INPATIENT_NO = i.Inpatient_No ");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 ");
		if(StringUtils.isNotBlank(deptCodeBR)){
			sb.append(" and i.DEPT_CODE = '"+deptCodeBR+"'");
		}
		if(StringUtils.isNotBlank(inOrOutBR)){
			sb.append("and t.outpath_flag = '"+inOrOutBR+"'");
		}
		if(StringUtils.isNotBlank(sexCode)){
			sb.append("and i.report_sex = '"+sexCode+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		
		sb.append(") row_ WHERE ROWNUM <= "+p*r+" ) WHERE rownum_ > "+(p-1)*r);
		
		List<InOutDetail> list = jdbcTemplate.query(sb.toString(), new RowMapper<InOutDetail>(){
			@Override
			public InOutDetail mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InOutDetail vo = new InOutDetail();
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setInpatientName(rs.getString("inpatientName"));
				vo.setSexCode(rs.getString("sexCode"));
				vo.setAge(rs.getString("age")+rs.getString("ageUnit"));
				vo.setCpId(rs.getString("cpId"));
				String inPathTime = "";
				if(rs.getTimestamp("inPathTime")!=null){
					inPathTime = DateUtils.formatDateY_M_D(rs.getTimestamp("inPathTime"));
				}
				vo.setInPathTime(inPathTime);
				String outPathTime = "";
				if(rs.getTimestamp("outPathTime")!=null){
					outPathTime = DateUtils.formatDateY_M_D(rs.getTimestamp("outPathTime"));
				}
				vo.setOutPathTime(outPathTime);
				vo.setOutpTypeCode(rs.getString("outpTypeCode"));
				return vo;
			}
		});
		return list;
	}

	@Override
	public Integer inOutDetailNum(String sTime, String eTime,
			String deptCodeBR, String inOrOutBR, String sexCode) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select count(1) from t_cp_master_index t ");
		sb.append(" inner join t_inpatient_info_now i ");
		sb.append(" on t.INPATIENT_NO = i.Inpatient_No ");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 ");
		if(StringUtils.isNotBlank(deptCodeBR)){
			sb.append(" and i.DEPT_CODE = '"+deptCodeBR+"'");
		}
		if(StringUtils.isNotBlank(inOrOutBR)){
			sb.append("and t.outpath_flag = '"+inOrOutBR+"'");
		}
		if(StringUtils.isNotBlank(sexCode)){
			sb.append("and i.report_sex = '"+sexCode+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.createtime, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		
		Object count=getSession().createSQLQuery(sb.toString()).uniqueResult();
		return Integer.valueOf((count==null?0:count).toString());
	}

	@Override
	public List<analysisClinicalVo> analysisClinicalList(String page,
			String rows, String sTime, String eTime, String deptCode,
			String betterCode,String cureCode) {
		
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):15;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM ( ");
		
		sb.append(" select t.apply_code as deptCode,");
		sb.append(" count(1) as totalNum,");
		sb.append(" sum(decode(t.apply_type,1,1,0)) as inNum,");
		sb.append(" sum(decode(t.apply_type,2,1,0)) as outNum,");
		sb.append(" count(v.id) as vaiviationNum,");
		sb.append(" sum(decode(m.OUTP_TYPE_CODE,"+betterCode+",1,0)) as betterNum,");
		sb.append(" sum(decode(m.OUTP_TYPE_CODE,"+cureCode+",1,0)) as cureNum");
		sb.append(" from t_path_apply t left join t_cp_master_index m on m.inpatient_no = t.inpatient_no");
		sb.append(" left join t_cp_variation v on t.inpatient_no = v.inpatient_no");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append(" and t.Apply_Code = '"+deptCode+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.apply_date, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by t.Apply_Code");
		
		sb.append(") row_ WHERE ROWNUM <= "+p*r+" ) WHERE rownum_ > "+(p-1)*r);

		final DecimalFormat df = new DecimalFormat("##0.00"); 
		
		List<analysisClinicalVo> list = jdbcTemplate.query(sb.toString(), new RowMapper<analysisClinicalVo>(){
			@Override
			public analysisClinicalVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				analysisClinicalVo vo = new analysisClinicalVo();
				vo.setDeptCode(rs.getString("deptCode"));
				int intTotal = rs.getInt("totalNum");
				vo.setTotalNum(intTotal);
				Double douTotal = (double) intTotal;
				Double douInNum = rs.getDouble("inNum");
				vo.setInNum(douInNum);
				vo.setInRare(df.format(rs.getDouble("inNum")/douTotal*100)+"%");
				vo.setOutRare(df.format(rs.getDouble("outNum")/douTotal*100)+"%");
				Double vRare = rs.getDouble("vaiviationNum")/douTotal;
				vo.setVaiviationRare(df.format(vRare*100)+"%");
				vo.setOverRare(df.format((1-vRare)*100)+"%");
				if(douInNum==0){
					vo.setBetterRare("--");
					vo.setCureRare("--");
				}else{
					vo.setBetterRare(df.format(rs.getDouble("betterNum")/douInNum*100)+"%");
					vo.setCureRare(df.format(rs.getDouble("cureNum")/douInNum*100)+"%");
				}
				return vo;
			}
		});
		return list;
	}

	@Override
	public Integer analysisClinicalNum(String sTime, String eTime,
			String deptCode, String betterCode,String cureCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from ( ");
		
		sb.append(" select t.apply_code as deptCode,");
		sb.append(" count(1) as totalNum,");
		sb.append(" sum(decode(t.apply_type,1,1,0)) as inNum,");
		sb.append(" sum(decode(t.apply_type,2,1,0)) as outNum,");
		sb.append(" count(v.id) as vaiviationNum,");
		sb.append(" sum(decode(m.OUTP_TYPE_CODE,"+betterCode+",1,0)) as betterNum,");
		sb.append(" sum(decode(m.OUTP_TYPE_CODE,"+cureCode+",1,0)) as cureNum");
		sb.append(" from t_path_apply t left join t_cp_master_index m on m.inpatient_no = t.inpatient_no");
		sb.append(" left join t_cp_variation v on t.inpatient_no = v.inpatient_no");
		sb.append(" where t.stop_flg = 0 and t.del_flg = 0 ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append(" and t.Apply_Code = '"+deptCode+"'");
		}
		if(StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)){
			sb.append(" and TO_CHAR(t.apply_date, 'yyyy-MM-dd') "+ "BETWEEN '"+sTime+"' and '"+eTime+"' ");
		}
		sb.append(" group by t.Apply_Code");
		
		sb.append(" )");
		Object count=getSession().createSQLQuery(sb.toString()).uniqueResult();
		return Integer.valueOf((count==null?0:count).toString());
	}

}
