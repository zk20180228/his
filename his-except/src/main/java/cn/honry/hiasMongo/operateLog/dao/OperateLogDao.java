package cn.honry.hiasMongo.operateLog.dao;

import net.sf.json.JSONArray;

public interface OperateLogDao {
	JSONArray getOperateLogByPage(String page, String rows, String operateName, String ud);
	Long getTotalByPage(String operateName);
}
