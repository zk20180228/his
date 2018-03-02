package cn.honry.portal.procedure.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.portal.procedure.dao.ProcedureDao;
/**
 * 存储冗余字段Dao的实现
 * @author hzr
 *
 */
@Repository("procedureDao")
@SuppressWarnings({ "all" })
public class ProcedureDaoImpl implements ProcedureDao {

	//扩展工具类,支持参数名传参
		@Resource
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public Map<String, Object> gettime(String table) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("table", table);
		String sql="select to_char(max(t.createtime)+1,'yyyy/mm/dd') as maxDateStr, to_char(min(t.createtime),'yyyy/mm/dd') as minDateStr "
				+ "  from "+table+" t";
		
		map = namedParameterJdbcTemplate.queryForMap(sql.toString(), pMap);
		return map;
	}

	@Override
	public void getupdate(String table,  String maxDateStr, String minDateStr) {
		StringBuffer sb = new StringBuffer();
		sb.append("call ");
		sb.append(" "+table+"('"+minDateStr+"','"+maxDateStr+"') ");
		namedParameterJdbcTemplate.getJdbcOperations().execute(sb.toString());
		/*namedParameterJdbcTemplate.getJdbcOperations().execute("{call "+table+"(?,?) }",new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.setString(1, minDateStr);
                cs.setString(2, maxDateStr);
                cs.execute();
                return null;
            }
        });*/
	}
}
