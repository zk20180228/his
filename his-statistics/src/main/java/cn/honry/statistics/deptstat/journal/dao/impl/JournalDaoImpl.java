package cn.honry.statistics.deptstat.journal.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.logging.Loggers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.inner.vo.AreaVo;
import cn.honry.statistics.deptstat.journal.dao.JournalDao;
import cn.honry.statistics.deptstat.journal.vo.JournalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;


@Repository("journalDao")
@SuppressWarnings({"all"})
public class JournalDaoImpl implements JournalDao{
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	Logger logger=Logger.getLogger(JournalDaoImpl.class);
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Resource
	private CodeInInterDAO innerCodeDao;
	@Override
	public List<String> queryDepts(String dept)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select d.dept_name from t_department d where d.dept_code in (:dept) order by d.dept_code");
		String[] deptsStrings = dept.split(",");
		List<String> deptsList = new ArrayList<String>();
		for (String str : deptsStrings) {
			deptsList.add(str);
		}
		map.put("dept", deptsList);
		List<String> deptList = namedParameterJdbcTemplate.queryForList(sql.toString(), map, java.lang.String.class);
		logger.info(map);
		if (deptList != null && deptList.size() > 0) {
			return deptList;
		}
		return new ArrayList<String>();
	}	
	@Override
	public List<Integer> queryInOutNum(String sTime, String eTime, String dept,
			int i)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		if(i - 1 == 0){//入院人数
			sql.append("select nvl(t.num,0) from t_department d left join (select t.dept_code,count(1) as num");
			sql.append(" from t_inpatient_info_now t ");
			sql.append(" where t.in_date <= to_date(:eTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and t.in_date >= to_date(:sTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and t.dept_code in(:dept) and t.del_flg = 0 and t.stop_flg = 0");
			sql.append(" group by t.dept_code");
			sql.append(" ) t on d.dept_code = t.dept_code where d.dept_code in(:dept) order by d.dept_code");
			logger.info("入院人数");
		}else if(i - 2 == 0){//转入人数
			sql.append("select nvl(t.num,0) from t_department d left join (select t.new_dept_code,count(1) as num from (");
			sql.append(" select told.new_dept_code from t_inpatient_shiftapply told");
			sql.append(" where told.confirm_date <= to_date(:eTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and told.confirm_date >= to_date(:sTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and told.new_dept_code in(:dept) and told.shift_state = '2' and told.del_flg = 0 and told.stop_flg = 0");
			sql.append(" union all");
			sql.append(" select tnow.new_dept_code from t_inpatient_shiftapply_now tnow");
			sql.append(" where tnow.confirm_date <= to_date(:eTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and tnow.confirm_date >= to_date(:sTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and tnow.new_dept_code in(:dept) and tnow.shift_state = '2' and tnow.del_flg = 0 and tnow.stop_flg = 0) t");
			sql.append(" group by t.new_dept_code");
			sql.append(" )t on d.dept_code = t.new_dept_code where d.dept_code in(:dept) order by d.dept_code");
			logger.info("转入人数");
		}else {//转出人数
			sql.append("select nvl(t.num,0) from t_department d left join (select t.old_dept_code,count(1) as num from (");
			sql.append(" select told.old_dept_code from t_inpatient_shiftapply told");
			sql.append(" where told.confirm_date <= to_date(:eTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and told.confirm_date >= to_date(:sTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and told.old_dept_code in(:dept) and told.shift_state = '2' and told.del_flg = 0 and told.stop_flg = 0");
			sql.append(" union all");
			sql.append(" select tnow.old_dept_code from t_inpatient_shiftapply_now tnow");
			sql.append(" where tnow.confirm_date <= to_date(:eTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and tnow.confirm_date >= to_date(:sTime, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and tnow.old_dept_code in(:dept) and tnow.shift_state = '2' and tnow.del_flg = 0 and tnow.stop_flg = 0) t");
			sql.append(" group by t.old_dept_code");
			sql.append(" )t on d.dept_code = t.old_dept_code where d.dept_code in(:dept) order by d.dept_code");
			logger.info("转出人数");
		}
		map.put("eTime", eTime);
		map.put("sTime", sTime);
		String[] deptsStrings = dept.split(",");
		List<String> deptList = new ArrayList<String>();
		for (String str : deptsStrings) {
			deptList.add(str);
		}
		map.put("dept", deptList);
		List<Integer> numList = namedParameterJdbcTemplate.queryForList(sql.toString(), map, java.lang.Integer.class);
		logger.info(map);
		logger.info(numList);
		if (numList != null && numList.size() > 0) {
			return numList;
		}
		return new ArrayList<Integer>();
	}
	@Override
	public List<Integer> queryNowNum(String time, String dept)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
			sql.append("select nvl(t.num,0) from t_department d left join (select t.dept_code,count(1) as num");
			sql.append(" from t_inpatient_info_now t ");
			sql.append(" where t.in_date <= to_date(:time, 'yyyy-mm-dd HH24:mi:ss')");
			sql.append(" and t.dept_code in(:dept) and t.in_state != 'O' and t.del_flg = 0 and t.stop_flg = 0");
			sql.append(" group by t.dept_code");
			sql.append(" ) t on d.dept_code = t.dept_code where d.dept_code in(:dept) order by d.dept_code");
		map.put("time", time);
		String[] deptsStrings = dept.split(",");
		List<String> deptList = new ArrayList<String>();
		for (String str : deptsStrings) {
			deptList.add(str);
		}
		map.put("dept", deptList);
		List<Integer> numList = namedParameterJdbcTemplate.queryForList(sql.toString(), map, java.lang.Integer.class);
		logger.info(map);
		logger.info(numList);
		if (numList != null && numList.size() > 0) {
			return numList;
		}
		return new ArrayList<Integer>();
	}
	@Override
	public List<Integer> queryBedNum(String dept, int i)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String inWhere;
		if(i - 1 ==0){//实占床位
			logger.info("实占床位");
			inWhere = " and bed.bed_state = '4'";
		}else if (i - 2 == 0) {// 开放床位 
			logger.info("开放床位 ");
			inWhere = " and bed.bed_state != '8' and bed.bed_organ = '4'";
		}else if (i - 3 == 0) {// 加床
			logger.info("加床");
			inWhere = " and bed.bed_state = '3' and bed.bed_organ = '2'";
		}else{// 空床 
			logger.info("空床");
			inWhere = " and bed.bed_state = '7'";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select nvl(n.num,0) from t_department d left join (select w.nusCode,w.dept_id, sum(num) as num");
		sql.append(" from (select bed.bedward_id, count(1) as num");
		sql.append(" from t_business_hospitalbed bed");
		sql.append(" where bed.del_flg = 0 and bed.stop_flg = 0");
		sql.append(inWhere);
		sql.append(" group by bed.bedward_id) b,");
		sql.append("(select ward.bedward_id, d.dept_code as nusCode,d.dept_id");
		sql.append(" from t_business_bedward ward,");
		sql.append(" (select t1.dept_code, c.dept_code as dept_id");
		sql.append(" from (select t.pardept_id, t.dept_code");
		sql.append(" from t_department_contact t");
		sql.append(" where t.dept_code in(:dept)  and t.reference_type = '03') c,");
		sql.append(" t_department_contact t1");
		sql.append(" where t1.id = c.pardept_id) d");
		sql.append(" where ward.stop_flg = 0 and ward.del_flg = 0 and ward.bedward_nursestation = d.dept_code) w");
		sql.append(" where b.bedward_id = w.bedward_id group by w.nusCode,w.dept_id) n on d.dept_code = n.dept_id");
		sql.append(" where d.dept_code in(:dept) order by d.dept_code");
		String[] deptsStrings = dept.split(",");
		List<String> deptList = new ArrayList<String>();
		for (String str : deptsStrings) {
			deptList.add(str);
		}
		map.put("dept", deptList);
		List<Integer> numList = namedParameterJdbcTemplate.queryForList(sql.toString(), map, java.lang.Integer.class);
		logger.info(map);
		logger.info(numList);
		if (numList != null && numList.size() > 0) {
			return numList;
		}
		return new ArrayList<Integer>();
	}
	@Override
	public List<Integer> queryCriticallyOrGrateOneNum(String eTime,
			String dept, int i)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		String inWhere = "";
		if(i - 1 == 0){//危重病人
			logger.info("危重病人");
			inWhere = " and t.patient_status in ('1','2')";
		}else if (i - 2 == 0) {//一级护理
			logger.info("一级护理");
			inWhere = " and t.tend = '一级护理'";
		}
		sql.append("select nvl(t.num,0) from t_department d left join (select t.dept_code,count(1) as num");
		sql.append(" from t_inpatient_info_now t");
		sql.append(" where t.in_date <= to_date(:eTime, 'yyyy-mm-dd HH24:mi:ss')");
		sql.append(" and t.dept_code in(:dept) and t.in_state != 'O' and t.del_flg = 0 and t.stop_flg = 0");
		sql.append(inWhere);
		sql.append(" group by t.dept_code");
		sql.append(" ) t on d.dept_code = t.dept_code where d.dept_code in(:dept) order by d.dept_code");
		map.put("eTime", eTime);
		String[] deptsStrings = dept.split(",");
		List<String> deptList = new ArrayList<String>();
		for (String str : deptsStrings) {
			deptList.add(str);
		}
		map.put("dept", deptList);
		List<Integer> numList = namedParameterJdbcTemplate.queryForList(sql.toString(), map, java.lang.Integer.class);
		logger.info(map);
		logger.info(numList);
		if (numList != null && numList.size() > 0) {
			return numList;
		}
		return new ArrayList<Integer>();
	}
	@Override
	public List<String> getAllInpateintDeptCode() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select d.dept_code from t_department d where d.dept_type = 'I' and d.del_flg = 0 and d.stop_flg = 0");
		List<String> deptList = namedParameterJdbcTemplate.queryForList(sql.toString(), map, java.lang.String.class);
		logger.info(map);
		logger.info(deptList);
		if (deptList != null && deptList.size() > 0) {
			return deptList;
		}
		return new ArrayList<String>();
	}
	@Override
	public String getMaxOutDateFromOnLineInpateint() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select max(t.out_date) from t_inpatient_info_now t where t.in_state = 'O'";
		String maxDate = namedParameterJdbcTemplate.queryForObject(sql, map, java.lang.String.class);
		if(StringUtils.isBlank(maxDate)){
			return null;
		}
		return maxDate;
	}
	@Override
	public List<Integer> queryOutNum(String sTime, String eTime, String dept,
			String tableName) throws Exception {
		logger.info("出院人数");
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select nvl(t.num,0) from t_department d left join (select t.dept_code,count(1) as num");
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" t ");
		sql.append(" where t.in_date <= to_date(:eTime, 'yyyy-mm-dd HH24:mi:ss')");
		sql.append(" and t.in_date >= to_date(:sTime, 'yyyy-mm-dd HH24:mi:ss')");
		sql.append(" and t.dept_code in(:dept) and t.in_state = 'O' and t.del_flg = 0 and t.stop_flg = 0");
		sql.append(" group by t.dept_code");
		sql.append(" ) t on d.dept_code = t.dept_code where d.dept_code in(:dept) order by d.dept_code");
		map.put("eTime", eTime);
		map.put("sTime", sTime);
		String[] deptsStrings = dept.split(",");
		List<String> deptList = new ArrayList<String>();
		for (String str : deptsStrings) {
			deptList.add(str);
		}
		map.put("dept", deptList);
		List<Integer> numList = namedParameterJdbcTemplate.queryForList(sql.toString(), map, java.lang.Integer.class);
		logger.info(map);
		logger.info(numList);
		if (numList != null && numList.size() > 0) {
			return numList;
		}
		return new ArrayList<Integer>();
	}
	@Override
	public List<JournalVo> queryDayReport(String begin,
			String depts, String menuAlias, String campus) throws Exception {
//		String[] campusArr=null;
//		List<String> sqlColumn=new ArrayList<String>();
		StringBuffer buffer=new StringBuffer(1500);
		//TODO 勿删 暂留
//		if(StringUtils.isNotBlank(campus)){//院区授权  查询院区
//			List<BusinessDictionary> list=innerCodeDao.getDictionary("hospitalArea");//查询院区
//			Map<String,String> map=new HashMap<String,String>();
//			for(BusinessDictionary vo:list){
//				map.put(vo.getEncode(), vo.getName());
//			}
//			campusArr=campus.split(",");
//			for(String vo:campusArr){
//				sqlColumn.add("cast('"+map.get(vo)+"' as varchar2(20)) deptName,sum(1) as total");
//			}
//			buffer.append("select L.deptName deptName,sum(L.total) total,sum(L.oldNum) oldNum,sum(L.inNum) inNum,");
//			buffer.append("sum(L.exInNum) exInNum,sum(L.exOutNum) exOutNum,sum(L.outNum) outNum,sum(L.nowNum) nowNum,sum(L.realBedNum) realBedNum,");
//			buffer.append("sum(L.openBedNum) openBedNum,sum(L.rateOfBed) rateOfBed,sum(L.criticallyNum) criticallyNum,sum(L.grateOneNum) grateOneNum,");
//			buffer.append("sum(L.extraBedNum) extraBedNum,sum(L.emptyBedNum) emptyBedNum from (");
//		}else{
//			sqlColumn.add("(select c.dept_name from t_department c where c.dept_code=t.dept_code) deptName,sum(1) as total");
//		}
//		
//		for(int i=0,len=sqlColumn.size();i<len;i++){
//			if(i>0){
//				buffer.append(" union all ");
//			}
//			buffer.append("select "+sqlColumn.get(i)+" ,");//--科室 
//			buffer.append("sum(t.beginning_num) oldNum,");//--原有人数
//			buffer.append("sum(t.in_normal+t.in_emergency) inNum,");//--入院
//			buffer.append("sum(t.in_transfer_inner) exInNum,");//--转入
//			buffer.append("sum(t.OUT_TRANSFER_INNER) exOutNum,");//--转出
//			buffer.append("sum(t.out_normal) outNum,");//--出院
//			buffer.append("sum(t.end_num) nowNum,");//--现有
//			buffer.append("sum(t.bed_stand+t.bed_add-t.bed_free) realBedNum,");//--实占床位
//			buffer.append("sum(t.bed_stand) openBedNum,");//--开放床位
//			buffer.append("sum( t.bed_rate) rateOfBed,");//--病床使用率
//			buffer.append("sum(t.danger_num+t.heavy_num) criticallyNum,");//--危重病人
//			buffer.append("sum(t.onelvcarenum) grateOneNum,");//--一级护理
//			buffer.append("sum(t.bed_add) extraBedNum,");//--加床
//			buffer.append("sum(t.bed_free) emptyBedNum ");//--空床
//			buffer.append("from t_inpatient_dayreport t where t.dept_code in ");
//			
//			if(StringUtils.isNotBlank(campus)){//如果院区不为空  查询院区下的授权的科室
//				buffer.append("(select d.dept_code as code from t_department d  where d.hospital_id="+campusArr[i]+") ");
//			} else if (StringUtils.isNotBlank(depts)){//如果科室不为空  查询选中科室
//				buffer.append("('"+depts.replace(",", "','")+"')");
//			} else {//查询授权科室
//				buffer.append("("+dataJurisInInterDAO.getJurisDeptSql("menuAlias","user")+")");
//			}
//			
//			buffer.append(" and t.date_stat=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') ");
//			
//		}
//		if(StringUtils.isBlank(campus)){
//			buffer.append(" group by grouping sets(t.dept_code,'')");
//		}else{
//			buffer.append(" ) L group by grouping sets( L.deptName,'')");
//		}
		
		String[] campusArr=null;
		List<String> sqlColumn=new ArrayList<String>();
		
		String formatter=null;
		if(StringUtils.isBlank(depts)){//院区授权  查询院区
		List<BusinessDictionary> list=innerCodeDao.getDictionary("hospitalArea");//查询院区
		Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary vo:list){
				map.put(vo.getEncode(), vo.getName());
			}
			if(StringUtils.isNotBlank(campus)){
				campusArr=campus.split(",");
				for(String vo:campusArr){
					sqlColumn.add("cast('"+map.get(vo)+"' as varchar2(20)) dept_code,");
				}
				formatter="nvl(g.dept_code,'合计')";
			}else{
				List<AreaVo> area =dataJurisInInterDAO.getDataJurisAreaList(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(), menuAlias);
				if(area!=null){
					for(int i=0,len=area.size();i<len;i++){
						campusArr[i]=area.get(i).getCode();
					}
					for(String vo:campusArr){
						sqlColumn.add("cast('"+map.get(vo)+"' as varchar2(20)) dept_code,");
					}
					formatter="nvl(g.dept_code,'合计')";
				}else{
					sqlColumn.add("cg.dept_code dept_code,");
					
					formatter="nvl((select pp.dept_name from t_department pp where pp.dept_code=g.dept_code),'合计')";
				}
			}
		}else{
			sqlColumn.add("cg.dept_code dept_code,");
			
			formatter="nvl((select pp.dept_name from t_department pp where pp.dept_code=g.dept_code),'合计')";
		}
		
		
		buffer.append("select replace("+formatter+",'病区','') deptName,");
		buffer.append("sum(decode(g.date_stat, trunc(to_date('"+begin+"', 'YYYY-MM-DD hh24:mi:ss')), g.beginning_num, 0)) oldNum,");
		buffer.append("(sum(g.in_normal)) inNum,");
		buffer.append("sum(g.in_transfer) exInNum,");
		buffer.append("sum(g.out_transfer) exOutNum,");
		buffer.append("sum(g.out_normal) outNum,");
		buffer.append("sum(decode(g.date_stat, trunc(to_date('"+begin+"', 'YYYY-MM-DD hh24:mi:ss')),g.end_num,0)) nowNum,");
		buffer.append("sum(g.end_num - g.QIGUANQIEKAINUM) realBedNum,");
		buffer.append("sum(g.QIGUANQIEKAINUM) hangBedDays,");
		buffer.append("sum(decode(g.date_stat,trunc(to_date('"+begin+"', 'YYYY-MM-DD hh24:mi:ss')),g.bed_stand,0)) openBedNum,");
		buffer.append("decode(sum(g.bed_stand),0,0,round(100 * sum(g.end_num - g.QIGUANQIEKAINUM) / sum(g.bed_stand),1)) rateOfBed,");
		buffer.append("sum(g.heavy_num+g.danger_num) criticallyNum,");
		buffer.append("sum(g.ONELVCARENUM) grateOneNum,");
		buffer.append("sum(case when (g.end_num - g.QIGUANQIEKAINUM - g.bed_stand) > 0 then (g.end_num - g.QIGUANQIEKAINUM - g.bed_stand) else 0 end) extraBedNum,");
		buffer.append("sum(case when g.bed_stand - (g.end_num-g.QIGUANQIEKAINUM) > 0 then g.bed_stand - (g.end_num-g.QIGUANQIEKAINUM) else 0 end) emptyBedNum from ( ");
		
		for(int i=0,len=sqlColumn.size();i<len;i++){
			
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select "+sqlColumn.get(i)+"cg.date_stat date_stat,abs(cg.beginning_num) beginning_num,abs(cg.in_normal) in_normal,abs(cg.in_transfer) in_transfer,");
			buffer.append("abs(cg.out_transfer) out_transfer,abs(cg.out_normal) out_normal,abs(abs(cg.beginning_num)+abs(cg.in_normal)+abs(cg.in_transfer)-abs(cg.out_normal)-abs(cg.out_transfer)) end_num,cg.QIGUANQIEKAINUM QIGUANQIEKAINUM,");
			buffer.append("cg.bed_stand bed_stand,cg.heavy_num heavy_num,cg.danger_num danger_num,cg.ONELVCARENUM ONELVCARENUM  from t_inpatient_dayreport cg where  ");
			if(StringUtils.isNotBlank(depts)){
				buffer.append("cg.dept_code in('").append(depts.replace(",", "','")).append("') ");
			}else if(StringUtils.isNotBlank(campus)){//如果院区不为空  查询院区下的授权的科室
				buffer.append("cg.dept_code in(select d.dept_code as code from t_department d  where d.hospital_id="+campusArr[i]+") ");
			}else{
				buffer.append("cg.dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
			}
			buffer.append("and cg.date_stat >= trunc(to_date('"+begin+" 00:00:00','yyyy-MM-dd HH24:mi:ss')) ");
			buffer.append("and cg.date_stat <= trunc(to_date('"+begin+" 23:59:59','yyyy-MM-dd HH24:mi:ss')) ");
			if(StringUtils.isBlank(depts)&&StringUtils.isNoneBlank(campusArr)){
				buffer.append(" union all ");//返回格式
				
				buffer.append("select "+sqlColumn.get(i)+"to_date('"+begin+" 00:00:00','yyyy-mm-dd HH24:mi:ss') date_stat,0 beginning_num,0 in_normal,0 in_transfer,");
				buffer.append("0 out_transfer,0 out_normal,0 end_num,0 QIGUANQIEKAINUM,");
				buffer.append("0 bed_stand,0 heavy_num,0 danger_num,0 ONELVCARENUM  from t_inpatient_dayreport cg ");
			}
		}
		buffer.append(" ) g ");
		buffer.append("group by rollup(g.dept_code) ");
		Map<String,String> map=new HashMap<String,String>();
		
		List<JournalVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<JournalVo>() {
			@Override
			public JournalVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				JournalVo vo=new JournalVo();
				vo.setDeptName(rs.getString("deptName"));
				vo.setOldNum(rs.getInt("oldNum"));
				vo.setInNum(rs.getInt("inNum"));
				vo.setExInNum(rs.getInt("exInNum"));
				vo.setExOutNum(rs.getInt("exOutNum"));
				vo.setOutNum(rs.getInt("outNum"));
				vo.setNowNum(rs.getInt("nowNum"));
				vo.setRealBedNum(rs.getInt("realBedNum"));
				vo.setOpenBedNum(rs.getInt("openBedNum"));
				vo.setRateOfBed(rs.getDouble("rateOfBed"));
				vo.setCriticallyNum(rs.getInt("criticallyNum"));
				vo.setGrateOneNum(rs.getInt("grateOneNum"));
				vo.setExtraBedNum(rs.getInt("extraBedNum"));
				vo.setEmptyBedNum(rs.getInt("emptyBedNum"));
//				vo.setTotal(rs.getInt("total"));
				vo.setHangBedDays(rs.getInt("hangBedDays"));
				return vo;
		}});
		if(list.size()>0){
			int len=list.size();
			list.get(len-1).setDeptName("合计");
			return list;
		}
		return new ArrayList<JournalVo>();
	}
}
