package cn.honry.oa.activiti.delegateHistory.service;

import java.util.List;

import cn.honry.base.bean.model.OaDelegateHistory;
import cn.honry.base.service.BaseService;

/**
 * 代理记录service接口
 * @author luyanshou
 *
 */
public interface OaDelegateHistoryService extends
		BaseService<OaDelegateHistory> {

	void saveOrUpdateList(List<OaDelegateHistory> list);
}
