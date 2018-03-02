package cn.honry.hiasMongo.exception.dao;

import net.sf.json.JSONArray;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.dao.EntityDao;

public interface HIASExceptionDao extends EntityDao<RecordToHIASException>{
	void saveExceptionInfoToMongo(RecordToHIASException hiasException,
			Exception e);
	
	JSONArray getHIASExceptionByPage(String page, String rows, RecordToHIASException hiasException);
	Long getTotalByPage(RecordToHIASException hiasException);
	void startDeal(String id);
	void endDeal(String id);
}
