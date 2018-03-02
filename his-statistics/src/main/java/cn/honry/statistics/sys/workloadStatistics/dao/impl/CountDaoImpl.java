package cn.honry.statistics.sys.workloadStatistics.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.deptWorkCount.vo.DeptWorkCountVo;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.sys.workloadStatistics.dao.CountDao;
import cn.honry.statistics.sys.workloadStatistics.vo.CountVo;
import cn.honry.statistics.sys.workloadStatistics.vo.GradeVo;
import cn.honry.statistics.sys.workloadStatistics.vo.TriageVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("countDAO")
@SuppressWarnings({"all"})
public class CountDaoImpl extends HibernateEntityDao<RegisterInfo> implements CountDao{
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
		
	@Override
	public List<Registration> findinfo(String seldept,String Stime,String Etime) {
		
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(seldept)){
			sb.append(" select  distinct d.dept_name      as deptName, d.dept_code      as deptCode,  r.reglevl_code   as reglevlCode,r.reg_triagetype as TriageType, ");
			sb.append(" r.reg_fee  as regFee,r.doct_code      as doctCode , r.reg_date     as  regDate  from  T_DEPARTMENT d ");
			sb.append(" left join   t_register_main r on d.dept_id=r.dept_code   and r.stop_flg = 0  and r.del_flg = 0 ");
		}else{
			sb.append(" select   distinct d.dept_name   as deptName,d.dept_code   as deptCode,r.reglevl_code   as reglevlCode,r.reg_triagetype as TriageType, ");
			sb.append(" r.reg_fee     as regFee, r.doct_code    as doctCode , r.reg_date     as  regDate ");
			sb.append(" from t_register_main r  left join T_DEPARTMENT d on r.dept_code= d.dept_id  and d.del_flg=0 and d.stop_flg=0 ");
			sb.append(" where r.del_flg=0 and r.stop_flg=0 ");
		}
	 
		if(StringUtils.isNotBlank(Stime) && StringUtils.isNotBlank(Etime)){
			sb.append(" and TO_CHAR(r.REG_DATE, 'yyyy-MM-dd') "+ "BETWEEN '"+Stime+"' and '"+Etime+"' ");
		}
		
		if(StringUtils.isNotBlank(seldept)){
			sb.append("  where  d.del_flg=0 and d.stop_flg=0 and d.dept_code in ("+seldept+") ");
		}
		sb.append(" order by d.dept_name ");
	
		Query query = this.getSession().createSQLQuery(sb.toString())
				.addScalar("deptName").addScalar("deptCode").addScalar("reglevlCode").addScalar("TriageType")
				.addScalar("regFee",Hibernate.DOUBLE).addScalar("doctCode").addScalar("regDate",Hibernate.DATE);
		List<Registration> list = query.setResultTransformer(Transformers.aliasToBean(Registration.class)).list();
	
		return list;
		
	}
	
	/**
	 * @Description 获取挂号科室
	 * @author  marongbin
	 * @createDate： 2016年12月1日 下午4:27:34 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public List<Registration> registerDept(List<String> tnl,final String seldept,final String Stime,final String Etime){
		if(tnl==null&&tnl.size()<0){
			return new ArrayList<Registration>();
		}
	
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tnl.size(); i++) {
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append(" select distinct DEPT_CODE as deptCode, DEPT_NAME as deptName from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnl.get(i));
			sb.append("  where stop_flg=0 and del_flg=0 ");
			if(StringUtils.isNotBlank(seldept)){
				sb.append(" and DEPT_CODE in (:seldept) ");
			}
			if(StringUtils.isNotBlank(Stime)&&StringUtils.isNotBlank(Etime)){
				sb.append(" and trunc(REG_DATE,'dd') between to_date(:Stime,'yyyy-MM-dd') And to_date(:Etime,'yyyy-MM-dd')  ");
			}
		}
		List<Registration> list = (List<Registration>)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)throws HibernateException, SQLException {
				
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(StringUtils.isNotBlank(seldept)){
					String[] deptCodes = seldept.split(",");
					query.setParameterList("seldept", deptCodes);
				}
				if(StringUtils.isNotBlank(Etime)&&StringUtils.isNotBlank(Stime)){
					query.setParameter("Etime", Etime).setParameter("Stime", Stime);
				}
				
				query.addScalar("deptCode").addScalar("deptName");
				
				return query.setResultTransformer(Transformers.aliasToBean(Registration.class)).list();
			}
		});
		
		if(list!=null&&list.size()>0){
			return list;
		}
	
		return new ArrayList<Registration>();
	}
	
	/**
	 * @Description 查询科室下的统计信息
	 * @author  marongbin
	 * @createDate： 2016年12月1日 下午8:39:45 
	 * @modifier zhangkui
	 * @modifyDate：2017-07-03
	 * @param：  
	 * @modifyRmk： 代码优化 
	 * @version 1.0
	 */
	public List<CountVo> getCount(Map<String, String> grade,Map<String, String> triage,final String deptCode,List<String> tnl,final String Stime,final String Etime,String page,String rows,String menuAlias){
		StringBuffer sb = new StringBuffer();
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):50;
		
		String sql = this.getSql(grade, triage, deptCode, tnl, Stime, Etime,menuAlias);
		sb.append(" select deptname,zrnum,zrmoney,zgnum,zgmoney,fzrnum,fzrmoney,hsnum,hsmoney,pznum,pzmoney,yznum,yzmoney,jznum,jzmoney,yynum,yymoney,ghnum,ghmoney,fznum,fzmoney,ybnum,"
				+ "ybmoney,gjzmzjnum,gjzmzjmoney,zyysnum,zyysmoney,zmzjnum,zmzjmoney,ybysnum,ybysmoney,jsnum,jsmoney,szmzjnum,szmzjmoney from ( ");
		sb.append(" select deptname, sum(zrnum) as zrnum,sum(zrmoney) as zrmoney,sum(zgnum) as zgnum,sum(zgmoney) as zgmoney, sum(fzrnum) as fzrnum, sum(fzrmoney) as fzrmoney,"
				+ " sum(gjzmzjnum) as gjzmzjnum, sum(gjzmzjmoney) as gjzmzjmoney,sum(zyysnum) as zyysnum,sum(zyysmoney) as zyysmoney,sum(zmzjnum) as zmzjnum,sum(zmzjmoney) as zmzjmoney,"
				+ " sum(ybysnum) as ybysnum, sum(ybysmoney) as ybysmoney,sum(jsnum) as jsnum,sum(jsmoney) as jsmoney,sum(szmzjnum) as szmzjnum,sum(szmzjmoney) as szmzjmoney,"
				+ " sum(hsnum) as hsnum,sum(hsmoney) as hsmoney,sum(pznum) as pznum,sum(pzmoney) as pzmoney, sum(yznum) as yznum, sum(yzmoney) as yzmoney, "
				+ " sum(jznum) as jznum,sum(jzmoney) as jzmoney,sum(yynum) as yynum,sum(yymoney) as yymoney, sum(ghnum) as ghnum, sum(ghmoney) as ghmoney,"
				+ " sum(fznum) as fznum,sum(fzmoney) as fzmoney,sum(ybnum) as ybnum,sum(ybmoney) as ybmoney,rownum rn from (");
		sb.append(sql);
		sb.append(" ) tab Where Rownum<=").append(p*r).append(" Group By Deptname,Rownum Order By Rownum) Where rn>").append((p-1)*r);
		
		List<String> deptcodes = Arrays.asList(deptCode.split(","));
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("Stime", Stime);
		paraMap.put("Etime", DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(Etime),1))));
		paraMap.put("deptCode",deptcodes );
		List<CountVo> count =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<CountVo>() {
			@Override
			public CountVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CountVo vo = new CountVo();
				vo.setDeptname(rs.getString("deptname"));
				vo.setFzmoney(rs.getDouble("fzmoney"));
				vo.setFznum(rs.getInt("fznum"));
				vo.setFzrmoney(rs.getDouble("fzrmoney"));
				vo.setFzrnum(rs.getInt("fzrnum"));
				vo.setGhmoney(rs.getDouble("ghmoney"));
				vo.setGhnum(rs.getInt("ghnum"));
				vo.setHsmoney(rs.getDouble("hsmoney"));
				vo.setHsnum(rs.getInt("hsnum"));
				vo.setJzmoney(rs.getDouble("jzmoney"));
				vo.setJznum(rs.getInt("jznum"));
				vo.setPzmoney(rs.getDouble("pzmoney"));
				vo.setPznum(rs.getInt("pznum"));
				vo.setYbmoney(rs.getDouble("ybmoney"));
				vo.setYbnum(rs.getInt("ybnum"));
				vo.setYymoney(rs.getDouble("yymoney"));
				vo.setYynum(rs.getInt("yynum"));
				vo.setYzmoney(rs.getDouble("yzmoney"));
				vo.setYznum(rs.getInt("yznum"));
				vo.setZgmoney(rs.getDouble("zgmoney"));
				vo.setZgnum(rs.getInt("zgnum"));
				vo.setZrmoney(rs.getDouble("zrmoney"));
				vo.setZrnum(rs.getInt("zrnum"));
				vo.setGjzmzjnum(rs.getInt("gjzmzjnum"));
				vo.setGjzmzjmoney(rs.getDouble("gjzmzjmoney"));
				vo.setZyysnum(rs.getInt("zyysnum"));
				vo.setZyysmoney(rs.getDouble("zyysmoney"));
				vo.setZmzjnum(rs.getInt("zmzjnum"));
				vo.setZmzjmoney(rs.getDouble("zmzjmoney"));
				vo.setYbysnum(rs.getInt("ybysnum"));
				vo.setYbysmoney(rs.getDouble("ybysmoney"));
				vo.setJsnum(rs.getInt("jsnum"));
				vo.setJsmoney(rs.getDouble("jsmoney"));
				vo.setSzmzjnum(rs.getInt("szmzjnum"));
				vo.setSzmzjmoney(rs.getDouble("szmzjmoney"));
			
				return vo;
		}});
		
		if(count!=null&&count.size()>0){
		
			return count;
		}
		
		return new ArrayList<CountVo>();
	}
	
	public String getSql(Map<String, String> grade,Map<String, String> triage, String deptCode,List<String> tnl,String Stime, String Etime,String menuAlias){
				
				//挂号级别  {主任医师=02, 国家级知名专家=37, 副主任医师=03, 住院医师=38, 知名专家=01, 一般医师=05, 讲师=23, 省级知名专家=36, 视力费=07, 主治医师=04}
				//挂号分诊类型  0 急诊；1优诊；2 预约；3过号；4平诊；5复诊；9隐蔽
				//主任医师
				String zrysCode = StringUtils.isNotBlank(grade.get(HisParameters.STARDEPTWORKLOADTITLEZRYS)) ? grade.get(HisParameters.STARDEPTWORKLOADTITLEZRYS):"";
				
				//医师
				String ysCode = StringUtils.isNotBlank(grade.get(HisParameters.STARDEPTWORKLOADTITLEYBYS)) ? grade.get(HisParameters.STARDEPTWORKLOADTITLEYBYS):"";
				
				//副主任医师
				String fzrysCode = StringUtils.isNotBlank(grade.get(HisParameters.STARDEPTWORKLOADTITLEFZRYS)) ? grade.get(HisParameters.STARDEPTWORKLOADTITLEFZRYS):"";
				
				//主治医师
				String zzCode = StringUtils.isNotBlank(grade.get(HisParameters.STARDEPTWORKLOADTITLEZZYS)) ? grade.get(HisParameters.STARDEPTWORKLOADTITLEZZYS):"";
				
				//急诊
				String jzCode = StringUtils.isNotBlank(triage.get(HisParameters.STARDEPTWORKLOADTRIAGEJZ)) ? triage.get(HisParameters.STARDEPTWORKLOADTRIAGEJZ):"";
				
				//优诊
				String yzCode = StringUtils.isNotBlank(triage.get(HisParameters.STARDEPTWORKLOADTRIAGEYZ)) ? triage.get(HisParameters.STARDEPTWORKLOADTRIAGEYZ):"";
				
				//预约
				String yyCode = StringUtils.isNotBlank(triage.get(HisParameters.STARDEPTWORKLOADTRIAGEYY)) ? triage.get(HisParameters.STARDEPTWORKLOADTRIAGEYY):"";
				
				//过号
				String ghCode = StringUtils.isNotBlank(triage.get(HisParameters.STARDEPTWORKLOADTRIAGEGH)) ? triage.get(HisParameters.STARDEPTWORKLOADTRIAGEGH):"";
				
				//平诊
				String pzCode = StringUtils.isNotBlank(triage.get(HisParameters.STARDEPTWORKLOADTRIAGEPZ)) ? triage.get(HisParameters.STARDEPTWORKLOADTRIAGEPZ):"";
				
				//复诊
				String fzCode = StringUtils.isNotBlank(triage.get(HisParameters.STARDEPTWORKLOADTRIAGEFZ)) ? triage.get(HisParameters.STARDEPTWORKLOADTRIAGEFZ):"";
				
				//隐蔽
				String ybCode = StringUtils.isNotBlank(triage.get(HisParameters.STARDEPTWORKLOADTRIAGEYB)) ? triage.get(HisParameters.STARDEPTWORKLOADTRIAGEYB):"";
				
				StringBuffer sb = new StringBuffer();
				
				sb.append(" select deptname, sum(zrnum) as zrnum,sum(zrmoney) as zrmoney,sum(zgnum) as zgnum,sum(zgmoney) as zgmoney, sum(fzrnum) as fzrnum, sum(fzrmoney) as fzrmoney, ");
				sb.append(" sum(gjzmzjnum) as gjzmzjnum, sum(gjzmzjmoney) as gjzmzjmoney,sum(zyysnum) as zyysnum,sum(zyysmoney) as zyysmoney,sum(zmzjnum) as zmzjnum,sum(zmzjmoney) as zmzjmoney, ");
				sb.append(" sum(ybysnum) as ybysnum, sum(ybysmoney) as ybysmoney,sum(jsnum) as jsnum,sum(jsmoney) as jsmoney,sum(szmzjnum) as szmzjnum,sum(szmzjmoney) as szmzjmoney, ");
				sb.append(" sum(hsnum) as hsnum,sum(hsmoney) as hsmoney,sum(pznum) as pznum,sum(pzmoney) as pzmoney, sum(yznum) as yznum, sum(yzmoney) as yzmoney, ");
				sb.append(" sum(jznum) as jznum,sum(jzmoney) as jzmoney,sum(yynum) as yynum,sum(yymoney) as yymoney, sum(ghnum) as ghnum, sum(ghmoney) as ghmoney, ");
				sb.append(" sum(fznum) as fznum,sum(fzmoney) as fzmoney,sum(ybnum) as ybnum,sum(ybmoney) as ybmoney  ");
				sb.append(" from( ");
				for (int i = 0; i < tnl.size(); i++) {
					if(i!=0){
						sb.append(" UNION ALL ");
					}
					sb.append(" select m.Dept_Name deptname,");
				
					//国际知名专家
					sb.append("DECODE(m.REGLEVL_CODE,'37',Count(1)) gjzmzjnum,");
					sb.append(" DECODE(m.REGLEVL_CODE,'37',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) gjzmzjmoney,");
					
					//住院医师
					sb.append("DECODE(m.REGLEVL_CODE,'38',Count(1)) zyysnum,");
					sb.append(" DECODE(m.REGLEVL_CODE,'38',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) zyysmoney,");
					
					//知名专家
					sb.append("DECODE(m.REGLEVL_CODE,'01',Count(1)) zmzjnum,");
					sb.append(" DECODE(m.REGLEVL_CODE,'01',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) zmzjmoney,");
					
					//一般医师
					sb.append("DECODE(m.REGLEVL_CODE,'05',Count(1)) ybysnum,");
					sb.append(" DECODE(m.REGLEVL_CODE,'05',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) ybysmoney,");
					
					//讲师
					sb.append("DECODE(m.REGLEVL_CODE,'23',Count(1)) jsnum,");
					sb.append(" DECODE(m.REGLEVL_CODE,'23',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) jsmoney,");
					
					//省知名专家
					sb.append("DECODE(m.REGLEVL_CODE,'36',Count(1)) szmzjnum,");
					sb.append(" DECODE(m.REGLEVL_CODE,'36',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) szmzjmoney,");
					
					//主任医师数量
					sb.append("DECODE(m.REGLEVL_CODE,'").append(zrysCode).append("',Count(1)) zrnum,");
					
					//主任医师金额
					sb.append(" DECODE(m.REGLEVL_CODE,'").append(zrysCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) zrmoney,");
					
					//医师数量
					sb.append(" DECODE(m.REGLEVL_CODE,'").append(ysCode).append("',Count(1)) zgnum,");
					
					//医师金额
					sb.append(" DECODE(m.REGLEVL_CODE,'").append(ysCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) zgmoney,");
					
					//副主任医师
					sb.append(" DECODE(m.REGLEVL_CODE,'").append(fzrysCode).append("',Count(1)) fzrnum,");
					
					//副主任医师金额
					sb.append(" DECODE(m.REGLEVL_CODE,'").append(fzrysCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) fzrmoney,");
					
					//主治医师
					sb.append(" DECODE(m.REGLEVL_CODE,'").append(zzCode).append("',Count(1)) hsnum,");
					sb.append(" DECODE(m.REGLEVL_CODE,'").append(zzCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) hsmoney,");
					
					//平诊
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(pzCode).append("',Count(1)) pznum,");
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(pzCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) pzmoney,");
					
					//优诊
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(yzCode).append("',Count(1)) yznum,");
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(yzCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) yzmoney,");
					
					//急诊
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(jzCode).append("',Count(1)) jznum,");
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(jzCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) jzmoney,");
					
					//预约
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(yyCode).append("',Count(1)) yynum,");
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(yyCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) yymoney,");
					
					//过号
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(ghCode).append("',Count(1)) ghnum,");
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(ghCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) ghmoney,");
					
					//复诊
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(fzCode).append("',Count(1)) fznum,");
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(fzCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) fzmoney,");
					
					//隐蔽
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(ybCode).append("',Count(1)) ybnum,");
					sb.append(" DECODE(m.REG_TRIAGETYPE,'").append(ybCode).append("',SUM(m.REG_FEE+m.CHCK_FEE+m.DIAG_FEE+m.OTH_FEE+m.BOOK_FEE)) ybmoney ");
					
					//合计 在service层代码里进行计算，在这里计算太慢
					sb.append(" from ").append(tnl.get(i)).append(" m ");
					sb.append(" WHERE m.IN_STATE=0 ");
					if(StringUtils.isNotBlank(deptCode)&&!"all".equals(deptCode)){
						sb.append("and m.Dept_Code in (:deptCode) ");
					}else{
						sb.append("and m.Dept_Code in (").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
					}
					if(StringUtils.isNotBlank(Stime)){
						sb.append(" AND m.REG_DATE>=TO_DATE(:Stime, 'yyyy-MM-dd') ");
					}
					if(StringUtils.isNotBlank(Etime)){
						sb.append(" AND m.REG_DATE<TO_DATE(:Etime, 'yyyy-MM-dd') ");
					}
					sb.append(" GROUP BY m.Dept_Name,m.REGLEVL_CODE,m.REG_TRIAGETYPE  ");
				}
				sb.append(" ) GROUP BY deptname ORDER BY deptname ");
		
				return sb.toString();
	}
	
	public Map<String,String> getGrade(){
		
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT g.GRADE_CODE as gradeCode,d.CODE_NAME as codeName FROM T_REGISTER_GRADE g ");
		sb.append(" LEFT JOIN T_BUSINESS_DICTIONARY d ON g.GRADE_TITLE = d.CODE_ENCODE ");
		sb.append(" WHERE d.CODE_TYPE = 'title' ");
		List<GradeVo> list = namedParameterJdbcTemplate.query(sb.toString(), new RowMapper<GradeVo>() {
			@Override
			public GradeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				GradeVo vo = new GradeVo();
				vo.setCodeName(rs.getString("codeName"));
				vo.setGradeCode(rs.getString("gradeCode"));
				return vo;
			}
		});
		
		for (GradeVo g : list) {
			map.put(g.getCodeName(), g.getGradeCode());
		}
	
		return map;
	}
	
	public Map<String,String> getTriage(){
		
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT d.CODE_ENCODE as codeEncode,d.CODE_NAME as codeName FROM T_BUSINESS_DICTIONARY d WHERE d.CODE_TYPE = 'triage'");
		List<TriageVo> list = namedParameterJdbcTemplate.query(sb.toString(), new RowMapper<TriageVo>() {
			@Override
			public TriageVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TriageVo vo = new TriageVo();
				vo.setCodeEncode(rs.getString("codeEncode"));
				vo.setCodeName(rs.getString("codeName"));
				
				return vo;
			}
		});
		
		for (TriageVo t : list) {
			map.put(t.getCodeName(), t.getCodeEncode());
		}
		
		return map;
	}
	
	@Override
	public List<SysDepartment> finddept() {
		
		String hql=" FROM SysDepartment d  where d.stop_flg=0 and d.del_flg=0";
		List<SysDepartment> deptList=super.find(hql, null);
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}	
		
		return new ArrayList<SysDepartment>();
	}
	
	/**
	 * 医生级别查询
	 * 
	 * @date 2016-04-28
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<RegisterGrade> findgrade() {
		
		String hql=" FROM RegisterGrade r where r.stop_flg=0 and r.del_flg=0";
		List<RegisterGrade> etriageList=super.find(hql, null);
		if(etriageList!=null && etriageList.size()>0){
		
			return etriageList;
		}	
		
		return new ArrayList<RegisterGrade>();
	}
	
	@Override
	public int getTotal(Map<String, String> grade,Map<String, String> triage, String deptCode,List<String> tnl,String Stime, String Etime,String menuAlias){
		StringBuffer sb = new StringBuffer();
		String sql = this.getSql(grade, triage, deptCode, tnl, Stime, Etime,menuAlias);
		sb.append(" select count(1) from( ").append(sql).append(")");
		List<String> deptcodes = Arrays.asList(deptCode.split(","));
	
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("Stime", Stime);
		paraMap.put("Etime", DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(Etime),1))));
		paraMap.put("deptCode",deptcodes );
		
		return namedParameterJdbcTemplate.queryForObject(sb.toString(),paraMap,java.lang.Integer.class );
	}
	
	@Override
	public List<CountVo> findInfo(String dept, String Stime, String Etime,String page, String rows, String menuAlias) {
		
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):50;
		
		//医师字段暂无值，取00  jsnum   hjnum 
		StringBuffer sb = new StringBuffer();
		sb.append(" select deptname,zrnum,zrmoney,fzrnum,fzrmoney,hsmoney,hsnum,pznum,pzmoney,yznum,yzmoney,jznum,jzmoney, ");
		sb.append(" yynum,yymoney,ghnum,ghmoney,fznum,fzmoney,ybnum,ybmoney,gjzmzjnum,gjzmzjmoney,zyysnum, ");
		sb.append(" zyysmoney,zmzjnum,zmzjmoney,ybysnum,ybysmoney,szmzjnum,szmzjmoney ");
		sb.append(" from (select tab1.*,rownum rn from( ");
		sb.append(" select distinct tab.DEPT_CODE deptname,");
		sb.append(" sum(tab.COUNTS_02) zrnum,sum(tab.COST_02) zrmoney,sum(tab.COUNTS_03) fzrnum,sum(tab.COST_03) fzrmoney,sum(tab.COUNTS_04) hsmoney,sum(tab.COST_04) hsnum,sum(tab.COUNTS_PZ) pznum,sum(tab.COST_PZ) pzmoney, ");
		sb.append(" sum(tab.COUNTS_YZ) yznum,sum(tab.COST_YZ) yzmoney,sum(tab.COUNTS_JZ) jznum,sum(tab.COST_JZ) jzmoney,sum(tab.COUNTS_YY) yynum,sum(tab.COST_YY) yymoney,sum(tab.COUNTS_GH) ghnum,sum(tab.COST_GH) ghmoney, ");
		sb.append(" sum(tab.COUNTS_FZ) fznum,sum(tab.COST_FZ) fzmoney,sum(tab.COUNTS_YB) ybnum,sum(tab.COST_YB) ybmoney,sum(tab.COUNTS_37) gjzmzjnum,sum(tab.COST_37) gjzmzjmoney,sum(tab.COUNTS_38) zyysnum,sum(tab.COST_38) zyysmoney, ");
		sb.append(" sum(tab.COUNTS_01) zmzjnum,sum(tab.COST_01) zmzjmoney,sum(tab.COUNTS_05) ybysnum,sum(tab.COST_05) ybysmoney,sum(tab.COUNTS_36) szmzjnum,sum(tab.COST_36) szmzjmoney ");
		sb.append(" from ( ");
		sb.append(" select t.*,rownum rn from t_static_deptjob t where  ");
		sb.append(" t.REG_DATE >= :Stime ");
		sb.append(" and t.REG_DATE < :Etime ");
		
		if(StringUtils.isNotBlank(dept)&&!"all".equals(dept)){
			sb.append(" and t.DEPT_CODE in(:dept) ");
		}else{
			sb.append(" and t.DEPT_CODE in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
		}
		
		sb.append(" ) tab group by tab.DEPT_CODE ");
		sb.append(" ) tab1  where rownum <:end) where rn>=:start ");
		
		List<String> deptcodes = Arrays.asList(dept.split(","));
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("Stime", Stime);
		paraMap.put("Etime", DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(Etime),1))));
		paraMap.put("dept",deptcodes );
		paraMap.put("menutype",menuAlias );
		paraMap.put("start",(p-1)*r);
		paraMap.put("end",p*r);
		paraMap.put("usercode",ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		List<CountVo> list = namedParameterJdbcTemplate.query(sb.toString(),paraMap, new RowMapper<CountVo>() {
			@Override
			public CountVo mapRow(ResultSet rs, int arg1) throws SQLException {
				CountVo vo = new CountVo();
				vo.setZrnum(rs.getInt("zrnum"));
				vo.setZrmoney(rs.getDouble("zrmoney"));
				vo.setFzrmoney(rs.getDouble("fzrmoney"));
				vo.setFzrnum(rs.getInt("fzrnum"));
				vo.setHsmoney(rs.getDouble("hsmoney"));
				vo.setHsnum(rs.getInt("hsnum"));
				vo.setPzmoney(rs.getDouble("pzmoney"));
				vo.setPznum(rs.getInt("pznum"));
				vo.setYzmoney(rs.getDouble("yzmoney"));
				vo.setYznum(rs.getInt("yznum"));
				vo.setJzmoney(rs.getDouble("jzmoney"));
				vo.setJznum(rs.getInt("jznum"));
				vo.setYymoney(rs.getDouble("yymoney"));
				vo.setYynum(rs.getInt("yynum"));
				vo.setGhmoney(rs.getDouble("ghmoney"));
				vo.setGhnum(rs.getInt("ghnum"));
				vo.setFzmoney(rs.getDouble("fzmoney"));
				vo.setFznum(rs.getInt("fznum"));
				vo.setYbmoney(rs.getDouble("ybmoney"));
				vo.setYbnum(rs.getInt("ybnum"));
				vo.setGjzmzjmoney(rs.getDouble("gjzmzjmoney"));
				vo.setGjzmzjnum(rs.getInt("gjzmzjnum"));
				vo.setZyysmoney(rs.getDouble("zyysmoney"));
				vo.setZyysnum(rs.getInt("zyysnum"));
				vo.setZmzjmoney(rs.getDouble("zmzjmoney"));
				vo.setZmzjnum(rs.getInt("zmzjnum"));
				vo.setYbysnum(rs.getInt("ybysnum"));
				vo.setYbysmoney(rs.getDouble("ybysmoney"));
				vo.setSzmzjmoney(rs.getDouble("szmzjmoney"));
				vo.setSzmzjnum(rs.getInt("szmzjnum"));
				vo.setDeptname(rs.getString("deptname"));
			
				return vo;
			}
		});
		
		if(list!=null&&list.size()>0){
		
			return list;
		}
		
		return new ArrayList<CountVo>();
	}
	
	@Override
	public int findInfoTotal(String dept, String Stime, String Etime,String page, String menuAlias) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct DEPT_CODE  from t_static_deptjob t ");
		
		if(StringUtils.isNotBlank(Stime)){
			sb.append(" where t.REG_DATE >= '"+Stime+"'");
		}
		
		if(StringUtils.isNotBlank(Etime)){
			sb.append(" and t.REG_DATE < '"+Etime+"'");
		}
		
		if(StringUtils.isNotBlank(dept)&&!"all".equals(dept)){
			String replace = dept.replace(",", "','");
			sb.append(" and t.DEPT_CODE in('"+replace+"') ");
		}else{
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			StringBuffer buffer = new StringBuffer();
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			buffer.append("SELECT d.DEPT_CODE FROM T_DEPARTMENT d ");
			buffer.append("WHERE d.DEL_FLG = 0 AND d.STOP_FLG = 0 AND ");
			buffer.append("((SELECT md.JURIS_TYPE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = '").append(user.getAccount()).append("' AND md.MENU_ALIAS = '").append(menuAlias).append("' AND rownum = 1) = 1 AND ");
			buffer.append("(SELECT md.JURIS_CODE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = '").append(user.getAccount()).append("' AND md.MENU_ALIAS = '").append(menuAlias).append("' AND rownum = 1) = 'ALL') ");
			buffer.append("OR((SELECT md.JURIS_TYPE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = '").append(user.getAccount()).append("' AND md.MENU_ALIAS = '").append(menuAlias).append("' AND rownum = 1) = 1 AND d.DEPT_AREA_CODE IN( ");
			buffer.append("SELECT md.JURIS_CODE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = '").append(user.getAccount()).append("' AND md.MENU_ALIAS = '").append(menuAlias).append("')) ");
			buffer.append("OR((SELECT md.JURIS_TYPE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = '").append(user.getAccount()).append("' AND md.MENU_ALIAS = '").append(menuAlias).append("' AND rownum = 1) = 2 AND d.DEPT_CODE IN ( ");
			buffer.append("SELECT md.JURIS_CODE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = '").append(user.getAccount()).append("' AND md.MENU_ALIAS = '").append(menuAlias).append("')) ");
			sb.append(" and t.DEPT_CODE in(").append(buffer.toString()).append(") ");
		}
	
		return this.getSqlTotal(sb.toString());
	}
	
	/**
	 * @Description:根据开始时间，结束时间，科室编号，查询科室的工作量
	 * @param dept 科室编号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param page 查询的页码
	 * @param rows 每页显示的记录数
	 * @param menuAlias 栏目的别名
	 * @exception:Exception
	 * @author: zhangkui
	 * @throws Exception 
	 * @time:2017年6月28日 上午9:35:12
	 */
	public Map<String,Object> listDeptWorkCountByMongo(String deptCode,String sTime, String eTime, String page, String rows) throws Exception {
		Map<String,Object> hashMap = new HashMap<String,Object>();//最终返回的数据，处理重复数据后
		ArrayList<DeptWorkCountVo> list = new ArrayList<DeptWorkCountVo>();//存放从mongo中查询出的所有不重复的数据,元素由去重后的map中得到
		
		//这段时间内的相同科室结果相加去重
		HashMap<String,DeptWorkCountVo> map = new HashMap<String, DeptWorkCountVo>();//key：code  value:对应的vo 
		
		//由于查询的时间不固定，所以查询时，分年，月，日三种情况行查询
		Map<String, List<String>> dateMap = ResultUtils.getDate(sTime, eTime);
		List<String> dayList = dateMap.get("day");//存放日的集合
		List<String> monthList = dateMap.get("month");//存放月的集合
		List<String> yearList = dateMap.get("year");//存放月的集合
		
		//构建mongo 的查询的条件{$and:[{"deptCode":{$in:["8151","8020"]}},{$or:[{regDate:"2015-12-01"},{regDate:"2015-12-02"}]}}]}
		BasicDBObject andDBObject=null;
		BasicDBList     andDBList=null;
		
		BasicDBObject orDeptObject=null;
		BasicDBList   orDeptDbList=null;
		BasicDBObject deptCodeDBObject=null;
		
		BasicDBObject orRegObject=null;
		BasicDBList   orRegDbList=null;		
		
		if(StringUtils.isNotBlank(deptCode)){
			 orDeptObject=new BasicDBObject();
			 orDeptDbList=new BasicDBList();
			 deptCodeDBObject= new BasicDBObject();
			 String[] deptCodes = deptCode.split(",");
			 for(String s :deptCodes){
				 orDeptDbList.add(s);
			 }
			 orDeptObject.append("$in", orDeptDbList);
			 deptCodeDBObject.append("deptCode", orDeptObject);
		} else {
			hashMap.put("total", 0);
			hashMap.put("rows",new ArrayList<DeptWorkCountVo>());
			return hashMap;
		}
		
		String collectionName=null;
		for(int i=1;i<=3;i++){
			 andDBObject=new BasicDBObject();
			 andDBList=new BasicDBList();
			 orRegObject=new BasicDBObject();
			 orRegDbList=new BasicDBList();
			if(i==1){//读日表数据
				if(dayList!=null&&dayList.size()>0){
					collectionName="KSGZLTJ_DAY";
					for(String day : dayList){
						orRegDbList.add(new BasicDBObject("regDate",day));
					}
				}else{
					continue;
				}
			}
			
			if(i==2){//读月表数据
				if(monthList!=null&&monthList.size()>0){
					collectionName="KSGZLTJ_MONTH";
					for(String month:monthList){
						orRegDbList.add(new BasicDBObject("regDate",month));
					}
				}else{
					continue;
				}
			}
			
			if(i==3){//读年表数据
				if(yearList!=null&&yearList.size()>0){
					collectionName="KSGZLTJ_YEAR";
					for(String year:yearList){
						orRegDbList.add(new BasicDBObject("regDate",year));
					}	
				}else{
					continue;
				}
				
			}
			
			orRegObject.append("$or", orRegDbList);
			
			if(orDeptObject!=null){
				andDBList.add(deptCodeDBObject);
			}
			andDBList.add(orRegObject);
			
			andDBObject.append("$and", andDBList);
			DBCursor dayCursor = new MongoBasicDao().findAlldata(collectionName, andDBObject);
			
			if(dayCursor!=null){
				while(dayCursor.hasNext()){
					DBObject dayC = dayCursor.next();
					String dcode=dayC.get("deptCode").toString();//虽然页面不用，但是后边的数据处理，其中分组的一个条件就是deptCode
					String json = dayC.get("value").toString();
					DeptWorkCountVo v = JSONUtils.fromJson(json, DeptWorkCountVo.class);
					v.setDeptCode(dcode);
					DeptWorkCountVo vo = map.get(dcode);
					if(vo!=null){
						vo.setGjjzmzjcount(vo.getGjjzmzjcount()+v.getGjjzmzjcount());
						vo.setGjjzmzjmoney(vo.getGjjzmzjmoney()+v.getGjjzmzjmoney());
						vo.setSjzmzjcount(vo.getSjzmzjcount()+v.getSjzmzjcount());
						vo.setSjzmzjmoney(vo.getSjzmzjmoney()+v.getSjzmzjmoney());
						vo.setZmzjcount(vo.getZmzjcount()+v.getZmzjcount());
						vo.setZmzjmoney(vo.getZmzjmoney()+v.getZmzjmoney());
						vo.setJscount(vo.getJscount()+v.getJscount());
						vo.setJsmoney(vo.getJsmoney()+v.getJsmoney());
						vo.setFjscount(vo.getFjscount()+v.getFjscount());
						vo.setFjsmoney(vo.getFjsmoney()+v.getFjsmoney());
						vo.setJymzcount(vo.getJymzcount()+v.getJymzcount());
						vo.setJymzmoney(vo.getJymzmoney()+v.getJymzmoney());
						vo.setYbyscount(vo.getYbyscount()+v.getYbyscount());
						vo.setYbysmoney(vo.getYbysmoney()+v.getYbysmoney());
						vo.setZzyscount(vo.getZzyscount()+v.getZzyscount());
						vo.setZzysmoney(vo.getZzysmoney()+v.getZzysmoney());
						vo.setLnyzcount(vo.getLnyzcount()+v.getLnyzcount());
						vo.setLnyzmoney(vo.getLnyzmoney()+v.getLnyzmoney());
						vo.setSlzcfcount(vo.getSlzcfcount()+v.getSlzcfcount());
						vo.setSlzcfmoney(vo.getSlzcfmoney()+v.getSlzcfmoney());
						vo.setJmjskcount(vo.getJmjskcount()+v.getJmjskcount());
						vo.setJmjskmoney(vo.getJmjskmoney()+v.getJmjskmoney());
						vo.setJzcount(vo.getJzcount()+v.getJzcount());
						vo.setJzmoney(vo.getJzmoney()+v.getJzmoney());
						vo.setYzcount(vo.getYzcount()+v.getYzcount());
						vo.setYzmoney(vo.getYzmoney()+v.getYzmoney());
						vo.setYycount(vo.getYycount()+v.getYycount());
						vo.setYymoney(vo.getYymoney()+v.getYymoney());
						vo.setGhcount(vo.getGhcount()+v.getGhcount());
						vo.setGhmoney(vo.getGhmoney()+v.getGhmoney());
						vo.setFzcount(vo.getFzcount()+v.getFzcount());
						vo.setFzmoney(vo.getFzmoney()+v.getFzmoney());
						vo.setPzcount(vo.getPzcount()+v.getPzcount());
						vo.setPzmoney(vo.getPzmoney()+v.getPzmoney());
						vo.setTotalcount(vo.getTotalcount()+v.getTotalcount());
						vo.setTotalmoney(vo.getTotalmoney()+v.getTotalmoney());
					}else{
						DeptWorkCountVo deptWorkCountVo = new DeptWorkCountVo();
						BeanUtils.copyProperties(deptWorkCountVo, v);//实现对象的copy
					
						map.put(dcode, deptWorkCountVo);
					}
				}
				
			}
		}
		
		list.addAll(map.values());
		if(list.size()<=0){
			hashMap.put("total", 0);
			hashMap.put("rows", new ArrayList<DeptWorkCountVo>());
		
			return hashMap;
		}
		
		Integer p=(page==null?1:Integer.parseInt(page));//页号
		Integer r=(rows==null?50:Integer.parseInt(rows));//每页显示的记录数
		
		//模拟分页
		int total= list.size();
		int totalPages=0;//总页数,向上取整
		if(total%r==0){
			totalPages=total/r;
		}else{
			totalPages=total/r+1;
		}
		int beginIndex=0;//查询的起始行
		int endIndex=0;//查询的结束行	
		if(p<totalPages){
			beginIndex=(p-1)*r;
			endIndex=p*r-1;//角标从0开始
		}
		if(p==totalPages){
			beginIndex=(p-1)*r;
			endIndex=list.size()-1;//查询页等于当前页时，查询的结束是集合的最后角标
		}
		if(p>totalPages||p<0){
			hashMap.put("total", 0);
			hashMap.put("rows", new ArrayList<DeptWorkCountVo>());
			return hashMap;
		}
		
		//List<deptWorkCountVo> subList = list2.subList(beginIndex, endIndex);当只有一个元素的时候，这个方法不好用了，包头不包尾，头，尾相同，都不包
		List<DeptWorkCountVo> subList =new ArrayList<DeptWorkCountVo>();
		for(int i=beginIndex;i<=endIndex;i++){
			Integer otherCount = list.get(i).getTotalcount()-list.get(i).getGjjzmzjcount()-
					list.get(i).getSjzmzjcount()-list.get(i).getZmzjcount()-list.get(i).getJscount()-
					list.get(i).getFjscount()-list.get(i).getJymzcount()-list.get(i).getYbyscount()-
					list.get(i).getZzyscount()-list.get(i).getLnyzcount()-list.get(i).getSlzcfcount()-list.get(i).getJmjskcount();
			list.get(i).setOthercount(otherCount);
			Double othermoney = list.get(i).getTotalmoney()-list.get(i).getGjjzmzjmoney()-
					list.get(i).getSjzmzjmoney()-list.get(i).getZmzjmoney()-list.get(i).getJsmoney()-
					list.get(i).getFjsmoney()-list.get(i).getJymzmoney()-list.get(i).getYbysmoney()-
					list.get(i).getZzysmoney()-list.get(i).getLnyzmoney()-list.get(i).getSlzcfmoney()-list.get(i).getJmjskmoney();
			list.get(i).setOthermoney(othermoney);
			subList.add(list.get(i));
		}
		//排一下序
		Collections.sort(subList, new Comparator<DeptWorkCountVo>() {
			@Override
			public int compare(DeptWorkCountVo v1, DeptWorkCountVo v2) {
				
				return v1.getDeptName().compareTo(v2.getDeptName());
			}
		});
		
		hashMap.put("total", total);
		hashMap.put("rows",subList);
		return hashMap;
	}

}
