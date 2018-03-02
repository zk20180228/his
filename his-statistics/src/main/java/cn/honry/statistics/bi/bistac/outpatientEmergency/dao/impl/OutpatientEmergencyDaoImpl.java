package cn.honry.statistics.bi.bistac.outpatientEmergency.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers.CalendarDeserializer;

import cn.honry.inner.statistics.internalCompare1.vo.InternalCompare1Vo;
import cn.honry.statistics.bi.bistac.outpatientEmergency.dao.OutpatientEmergencyDao;
import cn.honry.statistics.bi.bistac.outpatientEmergency.vo.OutpatientEmergencyVo;
import freemarker.template.SimpleDate;

@Repository("outpatientEmergencyDao")
@SuppressWarnings({ "all" })
public class OutpatientEmergencyDaoImpl extends HibernateDaoSupport implements OutpatientEmergencyDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public OutpatientEmergencyVo getDataInfoByTime(String date, List<String> tnL) throws Exception {
		Calendar c = Calendar.getInstance();
		Date beforeDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		c.setTime(beforeDate);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+1);
		String afterDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		StringBuffer sb = new StringBuffer();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("sDate", date+" 00:00:00");
		paraMap.put("eDate", afterDate+" 00:00:00");
		date = date+" 00:00:00";
		afterDate = afterDate+" 00:00:00";
		if(tnL!=null||tnL!=null){
			sb.append("select s1.WSNum,s2.FSDCNum,s3.JXWXYNum,s4.JFTNum,s5.HXDGRNum,s6.NXGBBNum,s7.JFZNum,s8.GXBNum,s9.JJZDNum,s10.GXYNum,s11.QTNum from" );
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					sb.append(" Union All ");
					sb.append("select s1.WSNum,s2.FSDCNum,s3.JXWXYNum,s4.JFTNum,s5.HXDGRNum,s6.NXGBBNum,s7.JFZNum,s8.GXBNum,s9.JJZDNum,s10.GXYNum,s11.QTNum from " );
				}
				sb.append("(select count(t"+i+".CARD_NO) WSNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code in ('8031', '8051')  and t"+i+".reg_date "
						+ "between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s1,");
				sb.append("(select count(t"+i+".CARD_NO) FSDCNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code in ('8034', '8054')  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s2,");
				sb.append("(select count(t"+i+".CARD_NO) JXWXYNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code = '8032'  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s3,");
				sb.append("(select count(t"+i+".CARD_NO) JFTNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code = '8052'  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s4,");
				sb.append("(select count(t"+i+".CARD_NO) HXDGRNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code in ('8043', '8063')  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s5,");
				sb.append("(select count(t"+i+".CARD_NO) NXGBBNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code in ('8036', '8099', '8056')  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s6,");
				sb.append("(select count(t"+i+".CARD_NO) JFZNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code in ('8030', '8046')  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s7,");
				sb.append("(select count(t"+i+".CARD_NO) GXBNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code = '8035'  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s8,");
				sb.append("(select count(t"+i+".CARD_NO) JJZDNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code = '0923'  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s9,");
				sb.append("(select count(t"+i+".CARD_NO) GXYNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code = '8055'  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s10,");
				sb.append("(select count(t"+i+".CARD_NO) QTNum "
						+ "from "+tnL.get(i)+" t"+i+""
						+ " where t"+i+".dept_code in (select d.dept_code from T_DEPARTMENT d where d.dept_name like '%急诊%' and d.dept_type = 'C'"
						+ "and d.dept_code not in ('8031', '8051', '8034', '8054', '8032', '8052', '8043','8063', '8036', '8099', '8056', '8030', '8046', '8035','0923', '8055')"
						+ "and d.del_flg = 0 and d.stop_flg = 0)  and t"+i+".reg_date between to_date('"+date+"', 'yyyy-MM-dd hh24:mi:ss') and "
						+ "to_date('"+afterDate+"', 'yyyy-MM-dd hh24:mi:ss')) s11");
			}
			
		}
		List<OutpatientEmergencyVo> voList = namedParameterJdbcTemplate.query(sb.toString(), paraMap, new RowMapper<OutpatientEmergencyVo>(){

			@Override
			public OutpatientEmergencyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OutpatientEmergencyVo vo  = new OutpatientEmergencyVo();
				vo.setWSNum(rs.getString("WSNum"));
				vo.setFSDCNum(rs.getString("FSDCNum"));
				vo.setJXWXYNum(rs.getString("JXWXYNum"));
				vo.setJFTNum(rs.getString("JFTNum"));
				vo.setHXDGRNum(rs.getString("HXDGRNum"));
				vo.setNXGBBNum(rs.getString("NXGBBNum"));
				vo.setJFZNum(rs.getString("JFZNum"));
				vo.setGXBNum(rs.getString("GXBNum"));
				vo.setJJZDNum(rs.getString("JJZDNum"));
				vo.setGXYNum(rs.getString("GXYNum"));
				vo.setQTNum(rs.getString("QTNum"));
				return vo;
			}
			
		});
		long totalNum =0;
		OutpatientEmergencyVo outpatient = new OutpatientEmergencyVo();
		if(voList!=null && voList.size()>0){
			outpatient = voList.get(0);
			totalNum = Long.parseLong(outpatient.getWSNum())+ Long.parseLong(outpatient.getFSDCNum())+ Long.parseLong(outpatient.getJXWXYNum())
			+ Long.parseLong(outpatient.getJFTNum())+ Long.parseLong(outpatient.getHXDGRNum())+ Long.parseLong(outpatient.getNXGBBNum())
			+ Long.parseLong(outpatient.getJFZNum())+ Long.parseLong(outpatient.getGXBNum())+ Long.parseLong(outpatient.getJJZDNum())
			+ Long.parseLong(outpatient.getGXYNum())+ Long.parseLong(outpatient.getQTNum());
			outpatient.setTotalNum(totalNum+"");
		}
		return outpatient;
	}
}
