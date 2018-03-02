package cn.honry.except.RecordToHIASException.dao;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.dao.EntityDao;
/**  
 * @Author：hedong
 * @CreateDate：2017-03-14
 * @version 1.0
 * @remark:平台异常信息记录
 */
public interface RecordToHIASExceptionDao extends EntityDao<RecordToHIASException>{
	void saveExceptionInfo(RecordToHIASException hiasException, Exception e);
}
