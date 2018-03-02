package cn.honry.except.RecordToHIASException.service;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.service.BaseService;
/**  
 * @Author：hedong
 * @CreateDate：2017-03-14
 * @version 1.0
 * @remark:平台异常信息记录
 */
public interface RecordToHIASExceptionService extends BaseService<RecordToHIASException>{
	/**
	 * 保存异常信息
	 * 调用示例
	 * @Author：hedong
	 * @CreateDate：2017-03-14
     * @version 1.0
	 * @param hiasException 异常记录实体
	 * @param e  异常对象
	 */
    void saveExceptionInfo(RecordToHIASException hiasException,Exception e);
}
