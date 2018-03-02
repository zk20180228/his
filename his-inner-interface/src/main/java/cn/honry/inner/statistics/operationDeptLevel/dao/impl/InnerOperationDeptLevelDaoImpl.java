package cn.honry.inner.statistics.operationDeptLevel.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.operationDeptLevel.dao.InnerOperationDeptLevelDao;
import cn.honry.inner.statistics.operationDeptLevel.vo.OperationDeptLevel;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Repository("innerOperationDeptLevelDao")
public class InnerOperationDeptLevelDaoImpl implements
		InnerOperationDeptLevelDao {
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public void initOperationDeptLeve(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		String startTime=date+" 00:00:00";
		String endTime=date+" 23:23:23";
		
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  deptName,levelA,levelB,levelC,levelD,sum as sumLevel,transOut,cast('"+date+"' as varchar2(10)) deptDate, sumAndTransOut FROM( ");
		sql.append("Select deptName As deptName, levelA As levelA, levelB As levelB, levelC As levelC,levelD As levelD, sum As sum,transOut As transOut,"
				+ "sumAndTransOut As sumAndTransOut "
				+ "From (select decode(AA.dept_code,'sum','sum',(select pp.DEPT_NAME from t_department pp where pp.DEPT_CODE= AA.dept_code)) deptName,"
				+ "AA.levelA,levelB,levelC,levelD,sum sum,decode(AA.dept_code,'sum',transOut2,transOut)  transOut,"
				+ "decode(AA.dept_code,'sum',(sum+transOut2),(sum+transOut))  sumAndTransOut  "
				+ "from (select nvl(dept_code,'sum') dept_code,"
				+ "sum(decode(levell, '一级', 1, 0)) levelA,"
				+ "sum(decode(levell, '二级', 1, 0)) levelB,"
				+ "sum(decode(levell, '三级', 1, 0)) levelC,"
				+ "sum(decode(levell, '四级', 1, 0)) levelD,"
				+ "count(1) sum,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg "
				+ "where cg.date_stat>=TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.date_stat<=TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss') and cg.dept_code=cc.dept_code ) transOut,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg "
				+ "where cg.date_stat>=TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.date_stat<=TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss')  ) transOut2 "
				+ "from (select AA.dept_Code, BB.* "
				+ "from (select cg.inpatient_no, cg.dept_code "
				+ "from t_emr_base cg where cg.out_date >= TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss') "
				+ "and cg.out_date <= TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.case_stus in ('3', '4') and cg.operation_code is not null) AA "
				+ "left join (select * from (select BB.*, row_number() over(partition by BB.inpatient_no order by BB.oper_type desc) as row_index "
				+ "from (select p.inpatient_no, p.oper_type, fun_splitstring(p.operation_cnname, '|', 1) name1,"
				+ " fun_splitstring(p.operation_cnname,  '|', 2) levell, p.operation_code, p.FIR_DOCD from t_operation_detail p) BB) AA "
				+ " where AA.row_index = 1) BB on AA.inpatient_no = BB.inpatient_no) cc group by cc.dept_code) AA ");
			sql.append( ") )"); 
		List<OperationDeptLevel> list =  namedParameterJdbcTemplate.query(sql.toString(),new HashMap<String,String>(),new RowMapper<OperationDeptLevel>() {
			@Override
			public OperationDeptLevel mapRow(ResultSet rs, int rowNum)throws SQLException {
				OperationDeptLevel vo = new OperationDeptLevel();
				vo.setDeptName(rs.getString("deptName"));
				vo.setLevelA(rs.getString("levelA"));
				vo.setLevelB(rs.getString("levelB"));
				vo.setLevelC(rs.getString("levelC"));
				vo.setLevelD(rs.getString("levelD"));
				vo.setSumLevel(rs.getString("sumLevel"));
				vo.setTransOut(rs.getString("transOut"));
				vo.setSumAndTransOut(rs.getString("sumAndTransOut"));
				vo.setDeptDate(rs.getString("deptDate"));
				return vo;
			}
			
		});
		
		DBObject query = new BasicDBObject();
		query.put("deptDate", date);//移除数据条件
		new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
		
		if(list.size()>0){
			for(OperationDeptLevel vo:list){
				if(StringUtils.isBlank(vo.getTransOut())){
					vo.setTransOut("0");
				}
				if(StringUtils.isBlank(vo.getLevelA())){
					vo.setLevelA("0");
				}
				if(StringUtils.isBlank(vo.getLevelB())){
					vo.setLevelB("0");
				}
				if(StringUtils.isBlank(vo.getLevelC())){
					vo.setLevelC("0");
				}
				if(StringUtils.isBlank(vo.getLevelD())){
					vo.setLevelD("0");
				}
				if(StringUtils.isBlank(vo.getSumLevel())){
					vo.setSumLevel("0");
				}
				if(StringUtils.isBlank(vo.getSumAndTransOut())){
					vo.setSumAndTransOut("0");
				}
				Document document1 = new Document();
				document1.append("deptDate",vo.getDeptDate() );//统计时间
				document1.append("deptName", vo.getDeptName());//科室名称
				Document document = new Document();
				document.append("deptDate",vo.getDeptDate() );//统计时间
				document.append("deptName", vo.getDeptName());//科室名称
				
				document.append("levelA", vo.getLevelA());
				document.append("levelB",  vo.getLevelB());
				document.append("levelC",  vo.getLevelC());
				document.append("levelD",  vo.getLevelD());
				
				document.append("sumLevel", vo.getSumLevel());
				document.append("transOut", vo.getTransOut());
				document.append("sumAndTransOut", vo.getSumAndTransOut());
				
				new MongoBasicDao().update(menuAlias+"_DAY", document1, document, true);
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias, list, date);
		}
		
	}

}
