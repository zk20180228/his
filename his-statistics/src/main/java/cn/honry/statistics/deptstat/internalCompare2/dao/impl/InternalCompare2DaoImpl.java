package cn.honry.statistics.deptstat.internalCompare2.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.internalCompare2.dao.InternalCompare2DAO;
import cn.honry.statistics.deptstat.internalCompare2.vo.FicDeptVO;
import cn.honry.statistics.deptstat.internalCompare2.vo.InternalCompare2Vo;
import cn.honry.utils.NumberUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("internalCompare2DAO")
@SuppressWarnings({ "all" })
public class InternalCompare2DaoImpl extends HibernateEntityDao<InternalCompare2Vo> implements InternalCompare2DAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	// 基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	// 扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/***
	 * 拼接sql
	 */
	private String querySql(List<String> tnLMZ1, List<String> tnLMZ2, List<String> tnLZY1, List<String> tnLZY2) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select dept  dept,totlCostMZ1 yearedMZ,totlCostMZ1 yearMZ,totlCostZY1 yearedZY,totlCostZY2 yearZY from (");
		for (int i = 0; i < tnLMZ1.size(); i++) {
			if (i > 0) {
				buffer.append(" Union All ");
			}
			buffer.append("select t" + i + ".REG_DPCDNAME as dept,sum(t" + i
					+ ".tot_cost) as totlCostMZ1, 0 as totlCostMZ2, 0 as totlCostZY1, 0 as totlCostZY2 from "
					+ tnLMZ1.get(i) + " t" + i + " ");
			buffer.append("where (t" + i
					+ ".fee_date between to_date(:start1,'yyyy-MM-dd') and to_date(:end1,'yyyy-MM-dd')) and ");
			buffer.append("t" + i + ".REG_DPCD in(:deptList)  group by t" + i + ".REG_DPCDNAME ");
		}
		buffer.append(" union all ");
		for (int i = 0; i < tnLMZ2.size(); i++) {
			if (i > 0) {
				buffer.append(" Union All ");
			}
			buffer.append("select t" + i + ".REG_DPCDNAME as dept,0,sum(t" + i + ".tot_cost) as totlCostMZ2,0,0 from "
					+ tnLMZ2.get(i) + " t" + i + " ");
			buffer.append("where (t" + i
					+ ".fee_date between to_date(:start2,'yyyy-MM-dd') and to_date(:end2,'yyyy-MM-dd')) and ");
			buffer.append("t" + i + ".REG_DPCD in(:deptList)  group by t" + i + ".REG_DPCDNAME ");
		}
		buffer.append(" union all ");
		for (int i = 0; i < tnLZY1.size(); i++) {
			if (i > 0) {
				buffer.append(" Union All ");
			}
			buffer.append("select t" + i + ".RECIPE_DEPTNAME as dept,0,0,sum(t" + i
					+ ".tot_cost) as totlCostZY1,0  from " + tnLZY1.get(i) + " t" + i + " ");
			buffer.append("where (t" + i
					+ ".BALANCE_DATE between to_date(:start1,'yyyy-MM-dd') and to_date(:end1,'yyyy-MM-dd')) and ");
			buffer.append("t" + i + ".RECIPE_DEPTCODE in(:deptList) group by t" + i + ".RECIPE_DEPTNAME ");
		}
		buffer.append(" union all ");
		for (int i = 0; i < tnLZY1.size(); i++) {
			if (i > 0) {
				buffer.append(" Union All ");
			}
			buffer.append("select t" + i + ".RECIPE_DEPTNAME as dept,0,0,0,sum(t" + i
					+ ".tot_cost) as totlCostZY2 from " + tnLZY1.get(i) + " t" + i + " ");
			buffer.append("where (t" + i
					+ ".BALANCE_DATE between to_date(:start2,'yyyy-MM-dd') and to_date(:end2,'yyyy-MM-dd')) and ");
			buffer.append("t" + i + ".RECIPE_DEPTCODE in(:deptList)  group by t" + i + ".RECIPE_DEPTNAME");
		}
		buffer.append(" ) ");
		return buffer.toString();
	}

	@Override
	public void initMZZYTotalByDayOrHours(List<String> maintnl, List<String> tnL, String begin, String end,
			Integer hours)  throws Exception{
		if (tnL != null || maintnl != null) {
			StringBuffer buffer = new StringBuffer(2000);
			buffer.append(
					"select b.deptCode as deptCode,b.deptName as dept,sum(b.value) AS totalMZ,sum(b.value1) AS totalZY,b.sTime1 AS feeDate,b.classType AS classType from(");
			for (int i = 0, len = maintnl.size(); i < len; i++) {
				if (i > 0) {
					buffer.append(" Union All ");
				}
				buffer.append("select t" + i + ".REG_DPCD AS deptCode,t" + i + ".REG_DPCDNAME AS deptName,sum(t" + i
						+ ".tot_cost) AS value,0 AS value1, cast('MZ' as varchar(2)) AS classType,");
				buffer.append("to_char(t" + i + ".fee_date,'yyyy-MM') AS sTime1 ");
				buffer.append("from " + maintnl.get(i) + " t" + i + " ");
				buffer.append("where t"+i+".PAY_FLAG = 1 ");
				buffer.append("and t"+i+".REG_DPCD in  "
						+ " (select ttt.dept_code ");
				buffer.append("from t_department_contact ttt ");
				buffer.append(" where ttt.del_flg = 0 and ttt.pardept_id in ");
				buffer.append(" (select tt.id   from t_department_contact tt ");
				buffer.append(" where tt.reference_type = '01' ");
				buffer.append("and tt.dept_code in  (select t.dept_code ");
				buffer.append(" from t_fictitious_contact t ");
				buffer.append("where (t.fict_code in ('2073', '2072') and ");
				buffer.append(" t.del_flg = 0)))) ");
				
				buffer.append("group by t" + i + ".REG_DPCDNAME,t" + i + ".fee_date,t" + i + ".REG_DPCD ");
			}
			if (tnL != null && maintnl != null && tnL.size() > 0 && maintnl.size() > 0) {
				buffer.append(" union All ");
			}
			for (int i = 0, len = tnL.size(); i < len; i++) {
				if (i > 0) {
					buffer.append(" Union All ");
				}
				buffer.append(
						"select n" + i + ".RECIPE_DEPTCODE AS deptCode,n" + i + ".RECIPE_DEPTNAME AS deptName,0,sum(n" + i
								+ ".tot_cost) AS value1,cast('ZY' as varchar2(2)) as classType,");
				buffer.append("to_char(n" + i + ".fee_date,'yyyy-MM') AS sTime1 ");
				buffer.append("from " + tnL.get(i) + " n" + i + " ");
				buffer.append("where  ");
				buffer.append("n" + i + ".RECIPE_DEPTCODE in(select t.dept_code from t_fictitious_contact t where t.fict_code in('2073','2072'))  ");
				buffer.append("group by n" + i + ".RECIPE_DEPTNAME,n" + i + ".fee_date,n" + i + ".RECIPE_DEPTCODE ");
			}
			buffer.append(") b group by b.deptCode,b.deptName,b.sTime1,b.classType");
			List<InternalCompare2Vo> list = super.getSession().createSQLQuery(buffer.toString()).addScalar("dept")
					.addScalar("deptCode").addScalar("totalMZ", Hibernate.BIG_DECIMAL).addScalar("totalZY", Hibernate.BIG_DECIMAL).addScalar("feeDate")
					.addScalar("classType").setResultTransformer(Transformers.aliasToBean(InternalCompare2Vo.class))
					.list();
			if (list != null && list.size() > 0) {
				List<DBObject> userList = new ArrayList<DBObject>();
				for (InternalCompare2Vo vo : list) {
					if (null != vo.getTotalMZ() || null != vo.getTotalZY()) {
						if (hours != null) {
							BasicDBObject bdObject = new BasicDBObject();
							bdObject.append("feeDate", vo.getFeeDate());
							bdObject.append("deptCode", vo.getDeptCode());
							bdObject.append("classType", vo.getClassType());
							DBCursor cursor = new MongoBasicDao().findAlldata("YYNKYXBHNEYXBDBBT", bdObject);
						}
						BasicDBObject bdObject1 = new BasicDBObject();
						Document document1 = new Document();
						document1.append("feeDate", vo.getFeeDate());
						document1.append("deptCode", vo.getDeptCode());
						document1.append("classType", vo.getClassType());
						Document document = new Document();
						document1.append("dept", vo.getDept());
						document1.append("deptCode", vo.getDeptCode());
						document.append("totalMZ", vo.getTotalMZ().doubleValue());// 金额
						document.append("totalZY", vo.getTotalZY().doubleValue());// 月份
						document.append("feeDate", vo.getFeeDate());// 日期
						document.append("classType", vo.getClassType());// 分类MZ门诊
						new MongoBasicDao().update("YYNKYXBHNEYXBDBBT", document1, document, true);

					}
				}
			}
		}
	}

	@Override
	public List<InternalCompare2Vo> queryinternalCompare2list(String timed, String Stime, String deptCode1List,
			List<String> tnLMZ1, List<String> tnLMZ2, List<String> tnLZY1, List<String> tnLZY2) throws Exception{
		String sql = querySql(tnLMZ1, tnLMZ1, tnLZY1, tnLZY2);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		// 下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推
		cal.set(Calendar.MONTH, Integer.parseInt(timed.split("-")[1]));
		// 调到上个月
		cal.add(Calendar.MONTH, -1);
		// 得到一个月最最后一天日期(31/30/29/28)
		int MaxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar cal1 = Calendar.getInstance();
		// 下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推
		cal1.set(Calendar.MONTH, Integer.parseInt(Stime.split("-")[1]));
		// 调到上个月
		cal1.add(Calendar.MONTH, -1);
		// 得到一个月最最后一天日期(31/30/29/28)
		int MaxDay1 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (StringUtils.isNotBlank(deptCode1List)) {
			paraMap.put("deptList", Arrays.asList(deptCode1List.split(",")));
		}
		if (StringUtils.isNotBlank(timed)) {
			paraMap.put("start1", timed.split("-")[0] + "-" + "01-01");
			paraMap.put("end1", timed + "-" + MaxDay);

		}
		if (StringUtils.isNotBlank(Stime)) {
			paraMap.put("start2", Stime.split("-")[0] + "-" + "01-01");
			paraMap.put("end2", Stime + "-" + MaxDay1);
		}
		List<InternalCompare2Vo> voList = namedParameterJdbcTemplate.query(sql, paraMap,
				new RowMapper<InternalCompare2Vo>() {

					@Override
					public InternalCompare2Vo mapRow(ResultSet rs, int rowNum) throws SQLException {
						InternalCompare2Vo vo = new InternalCompare2Vo();
						vo.setDept(rs.getString("dept"));
						vo.setYearedMZ(rs.getObject("yearedMZ") == null ? null : rs.getDouble("yearedMZ"));
						vo.setYearMZ(rs.getObject("yearMZ") == null ? null : rs.getDouble("yearMZ"));
						vo.setYearedZY(rs.getObject("yearedZY") == null ? null : rs.getDouble("yearedZY"));
						vo.setYearZY(rs.getObject("yearZY") == null ? null : rs.getDouble("yearZY"));
						return vo;
					}
				});

		if (voList != null && voList.size() > 0) {
			for (InternalCompare2Vo comVo : voList) {
				comVo.setYearedTol(comVo.getYearedMZ() + comVo.getYearedZY());
				comVo.setYearTol(comVo.getYearMZ() + comVo.getYearZY());
				comVo.setIncreaseTol(comVo.getYearTol() - comVo.getYearedTol());
				comVo.setRateTol(comVo.getYearedTol()==0?0:comVo.getIncreaseTol() / comVo.getYearedTol());
				comVo.setIncreaseMZ(comVo.getYearMZ() - comVo.getYearedMZ());
				comVo.setRateMZ(comVo.getYearedMZ()==0?0:comVo.getIncreaseMZ() / comVo.getYearedMZ());
				comVo.setIncreaseZY(comVo.getYearZY() - comVo.getYearedZY());
				comVo.setRateZY(comVo.getYearedZY()==0?0:comVo.getIncreaseZY() / comVo.getYearedZY());
			}
		}
		return voList;
	}

	@Override
	public List<InternalCompare2Vo> queryForDBSque(String timed, String stime, String deptCode1List) {
		//查询虚拟科室对应的的住院科室信息
		String sql="select d.dept_area_code as district ,d.dept_area_name  as districtName ,d.dept_code as deptCode from t_department d right join  t_fictitious_contact t on t.dept_code=d.dept_code where t.fict_code='"+deptCode1List+"'";
		List<FicDeptVO> listDep=this.getSession().createSQLQuery(sql).addScalar("deptCode")
				.addScalar("district",Hibernate.INTEGER).addScalar("districtName")
				.setResultTransformer(Transformers.aliasToBean(FicDeptVO.class)).list();
		
		//内科医学部和内二医学部  只显示住院科室  根据科室关系查询住院科室对应的门诊科室  门诊科室收入显示住院下门诊科室收入
		StringBuffer buffer=new StringBuffer(200);
		buffer.append("select ttt.dept_code as ficCode,zy.code as ficName ");//ficCode 门诊科室,ficName 住院科室
		buffer.append("from t_department_contact ttt inner join ( select tt.id as ids ,tt.dept_code as code ");
		buffer.append("from t_department_contact tt where tt.reference_type='01' and tt.dept_code in ");
		buffer.append("(select t.dept_code   from t_fictitious_contact t ");
		buffer.append(" where (t.fict_code in ('"+deptCode1List+"') and t.del_flg = 0))) zy  on ttt.pardept_id=zy.ids  where ttt.del_flg=0 ");
		List<FicDeptVO> query=this.getSession().createSQLQuery(buffer.toString())
				.addScalar("ficCode").addScalar("ficName")
				.setResultTransformer(Transformers.aliasToBean(FicDeptVO.class)).list();
		Map<String,String> InAndOutDept= new LinkedHashMap<String,String>();
		//住院和门诊科室对应  把门诊科室门诊收入并入住院门诊收入列进行显示
		for(FicDeptVO inOut:query){
			InAndOutDept.put(inOut.getFicName(), inOut.getFicCode());
		}
		//存放查询科室和院区的映射
		Map<String,String> map=new LinkedHashMap<String,String>();
		for(FicDeptVO listFic:listDep){
			Integer dis=listFic.getDistrict();
			if(1==dis){
				map.put(listFic.getDeptCode(), "河医院区");
			}else if(2==dis){
				map.put(listFic.getDeptCode(), "郑东院区");
			}else if(3==dis){
				map.put(listFic.getDeptCode(), "惠济院区");
			}
		}
	
		
		BasicDBObject bdObject = new BasicDBObject();
		String mzDept;//门诊科室
		if(map.size()>0){//拼接门诊住院所有科室
			BasicDBList deptList=new BasicDBList();
			for(String dept:map.keySet()){
				mzDept=InAndOutDept.get(dept);//如果住院科室下有对应的门诊科室 进行合并查询
				if(StringUtils.isNotBlank(mzDept)){
					deptList.add(new BasicDBObject("deptCode",mzDept));//门诊科室
				}
				deptList.add(new BasicDBObject("deptCode",dept));//住院科室
			}
			bdObject.put("$or", deptList);
		}
			bdObject.append("feeDate", stime);
			DBCursor cursor = new MongoBasicDao().findAlldata("YYNKYXBHNEYXBDBBT_MONTH_NOW", bdObject);
			DBObject dbCursor;
			Map<String,InternalCompare2Vo> deptMap=new LinkedHashMap<String,InternalCompare2Vo>();
			while (cursor.hasNext()) {//取数据
				dbCursor = cursor.next();
				InternalCompare2Vo vo = new InternalCompare2Vo();
				String deptName=(String)dbCursor.get("dept");//科室名称
				String deptCode=(String)dbCursor.get("deptCode");//科室code
				vo.setDept(deptName);
				vo.setDeptCode(deptCode);
				/**总收入数据**/
				Double yearedTol=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("yearedTol")/10000,2));
				Double yearTol=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("yearTol")/10000,2));
				Double increaseTol=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("increaseTol")/10000,2));
				Double rateTol=(Double)dbCursor.get("rateTol");
				vo.setYearedTol(yearedTol);
				vo.setYearTol(yearTol);
				if(yearedTol==0d&&yearTol==0d){
					rateTol=0d;
				}
				vo.setIncreaseTol(increaseTol);
				vo.setRateTol(rateTol);
				/**门诊数据**/
				Double yearedMZ=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("yearedMZ")/10000,2));
				Double yearMZ=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("yearMZ")/10000,2));
				Double increaseMZ=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("increaseMZ")/10000,2));
				Double rateMZ=(Double)dbCursor.get("rateMZ");
				vo.setYearedMZ(yearedMZ);
				vo.setYearMZ(yearMZ);
				if(yearedMZ==0d&&yearMZ==0d){
					rateMZ=0d;
				}
				vo.setIncreaseMZ(increaseMZ);
				vo.setRateMZ(rateMZ);
				/**住院数据**/
				Double yearedZY=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("yearedZY")/10000,2));
				Double yearZY=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("yearZY")/10000,2));
				Double rateZY=(Double)dbCursor.get("rateZY");
				Double increaseZY=Double.valueOf(NumberUtil.init().format((Double)dbCursor.get("increaseZY")/10000,2));
				vo.setYearedZY(yearedZY);
				vo.setYearZY(yearZY);
				if(yearedZY==0d&&yearZY==0d){
					rateZY=0d;
				}
				vo.setRateZY(rateZY);
				vo.setIncreaseZY(increaseZY);
				deptMap.put(deptCode, vo);
		}
		//key住院科室 
		for(String key:InAndOutDept.keySet()){
			InternalCompare2Vo zyvo=deptMap.get(key);//住院科室vo
			mzDept=InAndOutDept.get(key);//对应的门诊科室
			InternalCompare2Vo mzvo=deptMap.get(key);//门诊科室vo
			deptMap.remove(mzDept);//去除门诊科室
			//门诊科室金额放到住院门诊列
			zyvo.setYearedMZ(zyvo.getYearedMZ()+mzvo.getYearedMZ());
			zyvo.setYearMZ(zyvo.getYearMZ()+mzvo.getYearMZ());
			zyvo.setIncreaseMZ(zyvo.getIncreaseMZ()+mzvo.getIncreaseMZ());
			zyvo.setRateMZ(zyvo.getYearedMZ()==0.00?zyvo.getYearMZ()==0.00?0:100d:Double.valueOf(NumberUtil.init().format(zyvo.getIncreaseMZ()*100/zyvo.getYearedMZ(),2)));
			deptMap.put(key, zyvo);
		}
		//存放院区数据的map
		Map<String,InternalCompare2Vo> disMap=new HashMap<String,InternalCompare2Vo>(){  
			private static final long serialVersionUID = -5702975305622575036L;
		{
			InternalCompare2Vo hxyq=new InternalCompare2Vo();
			hxyq.setDept("河医院区");
			hxyq.setDeptCode("河医院区");
			put("河医院区",hxyq);
			InternalCompare2Vo zdyq=new InternalCompare2Vo();
			zdyq.setDept("郑东院区");
			zdyq.setDeptCode("郑东院区");
			put("郑东院区",zdyq);
			InternalCompare2Vo hjyq=new InternalCompare2Vo();
			hjyq.setDept("惠济院区");
			hjyq.setDeptCode("惠济院区");
			put("惠济院区",hjyq);
		}};
		for(String key:deptMap.keySet()){//计算院区合计
			InternalCompare2Vo vo=deptMap.get(key);//科室
			mzDept=map.get(vo.getDeptCode());//
			InternalCompare2Vo disVo=disMap.get(mzDept);//院区
			//门诊
			disVo.setYearedMZ(disVo.getYearedMZ()+vo.getYearedMZ());
			disVo.setYearMZ(disVo.getYearMZ()+vo.getYearMZ());
			disVo.setIncreaseMZ(disVo.getIncreaseMZ()+vo.getIncreaseMZ());
			//住院
			disVo.setYearedZY(disVo.getYearedZY()+vo.getYearedZY());
			disVo.setYearZY(disVo.getYearZY()+vo.getYearZY());
			disVo.setIncreaseZY(disVo.getIncreaseZY()+vo.getIncreaseZY());
			//总收入
			disVo.setYearedTol(disVo.getYearedTol()+vo.getYearedTol());
			disVo.setYearTol(disVo.getYearTol()+vo.getYearTol());
			disVo.setIncreaseTol(disVo.getIncreaseTol()+vo.getIncreaseTol());
			disMap.put(mzDept, disVo);
		}
		disMap=space(disMap);
		InternalCompare2Vo disVo=new InternalCompare2Vo();//总合计
		disVo.setDeptCode("总合计");//总合计
		disVo.setDept("总合计");
		for(String key:disMap.keySet()){//计算院区合计
			InternalCompare2Vo vo=disMap.get(key);//科室
			//门诊
			disVo.setYearedMZ(Double.valueOf(NumberUtil.init().format(disVo.getYearedMZ()+vo.getYearedMZ(),2)));
			disVo.setYearMZ(Double.valueOf(NumberUtil.init().format(disVo.getYearMZ()+vo.getYearMZ(),2)));
			disVo.setIncreaseMZ(Double.valueOf(NumberUtil.init().format(disVo.getIncreaseMZ()+vo.getIncreaseMZ(),2)));
			disVo.setRateMZ(Double.valueOf(NumberUtil.init().format(disVo.getRateMZ()+vo.getRateMZ()/3,2)));
			//住院
			disVo.setYearedZY(Double.valueOf(NumberUtil.init().format(disVo.getYearedZY()+vo.getYearedZY(),2)));
			disVo.setYearZY(Double.valueOf(NumberUtil.init().format(disVo.getYearZY()+vo.getYearZY(),2)));
			disVo.setIncreaseZY(Double.valueOf(NumberUtil.init().format(disVo.getIncreaseZY()+vo.getIncreaseZY(),2)));
			disVo.setRateZY(Double.valueOf(NumberUtil.init().format(disVo.getRateZY()+vo.getRateZY()/3,2)));
			//总收入
			disVo.setYearedTol(Double.valueOf(NumberUtil.init().format(disVo.getYearedTol()+vo.getYearedTol(),2)));
			disVo.setYearTol(Double.valueOf(NumberUtil.init().format(disVo.getYearTol()+vo.getYearTol(),2)));
			disVo.setIncreaseTol(Double.valueOf(NumberUtil.init().format(disVo.getIncreaseTol()+vo.getIncreaseTol(),2)));
			disVo.setRateTol(Double.valueOf(NumberUtil.init().format(disVo.getRateTol()+vo.getRateTol()/3,2)));
		}
		
		List<InternalCompare2Vo> listResult = new ArrayList<InternalCompare2Vo>();//返回结果list
		listResult.add(disVo);
		listResult=returnList(disMap,listResult);
		listResult=returnList(deptMap,listResult);
		
		return listResult;
	}
	protected List<InternalCompare2Vo> returnList(Map<String,InternalCompare2Vo> map,List<InternalCompare2Vo> list) {
		for(String key:map.keySet()){
			list.add(map.get(key));
		}
		return list;
	}
	protected Map<String,InternalCompare2Vo> space(Map<String,InternalCompare2Vo> map){
		for(String key:map.keySet()){
			InternalCompare2Vo vo=map.get(key);
			vo.setYearedMZ(Double.valueOf(NumberUtil.init().format(vo.getYearedMZ(),2)));
			vo.setYearMZ(Double.valueOf(NumberUtil.init().format(vo.getYearMZ(),2)));
			vo.setIncreaseMZ(Double.valueOf(NumberUtil.init().format(vo.getIncreaseMZ(),2)));
			
			vo.setYearedZY(Double.valueOf(NumberUtil.init().format(vo.getYearedZY(),2)));
			vo.setYearZY(Double.valueOf(NumberUtil.init().format(vo.getYearZY(),2)));
			vo.setIncreaseZY(Double.valueOf(NumberUtil.init().format(vo.getIncreaseZY(),2)));
			
			vo.setYearedTol(Double.valueOf(NumberUtil.init().format(vo.getYearedTol(),2)));
			vo.setYearTol(Double.valueOf(NumberUtil.init().format(vo.getYearTol(),2)));
			vo.setIncreaseTol(Double.valueOf(NumberUtil.init().format(vo.getIncreaseTol(),2)));
			
			vo.setRateMZ(vo.getYearedMZ()==0.00?vo.getYearMZ()==0.00?0.00:100d:Double.valueOf(NumberUtil.init().format(vo.getIncreaseMZ()/vo.getYearedMZ()*100,2)));
			vo.setRateZY(vo.getYearedZY()==0.00?vo.getYearZY()==0.00?0.00:100d:Double.valueOf(NumberUtil.init().format(vo.getIncreaseZY()/vo.getYearedZY()*100,2)));
			vo.setRateTol(vo.getYearedTol()==0.00?vo.getYearTol()==0.00?0.00:100d:Double.valueOf(NumberUtil.init().format(vo.getIncreaseTol()/vo.getYearedTol()*100,2)));
			map.put(key, vo);
		}
		return map;
	}
	
}
