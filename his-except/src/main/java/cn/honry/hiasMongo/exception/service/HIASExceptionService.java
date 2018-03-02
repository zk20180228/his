package cn.honry.hiasMongo.exception.service;

import net.sf.json.JSONArray;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.service.BaseService;

public interface HIASExceptionService extends BaseService<RecordToHIASException>{
	/**
	 * 保存异常信息
	 * 调用示例
	 * @Author：hedong
	 * @CreateDate：2017-03-23
     * @version 1.0
	 * @param hiasException 异常记录实体
	 * @param e  异常对象
	 */
    void saveExceptionInfoToMongo(RecordToHIASException hiasException,Exception e);
    /**
     * 分页查询异常信息
     * @param page	起始页数 
     * @param rows	数据列数 
     * @param recordToHIASException 异常记录实体
     */
    JSONArray getHIASExceptionByPage(String page, String rows, RecordToHIASException hiasException);
    Long getTotalByPage(RecordToHIASException hiasException);
    void startDeal(String id);
    void endDeal(String id);
}
