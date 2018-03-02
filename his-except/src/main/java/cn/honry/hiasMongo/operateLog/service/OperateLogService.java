package cn.honry.hiasMongo.operateLog.service;

import net.sf.json.JSONArray;

public interface OperateLogService {
	JSONArray getOperateLogByPage(String page, String rows, String operateName, String ud);
	Long getTotalByPage(String operateName);
}
