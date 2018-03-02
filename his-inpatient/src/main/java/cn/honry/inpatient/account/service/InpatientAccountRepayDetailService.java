package cn.honry.inpatient.account.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientAccountrepaydetail;
import cn.honry.base.service.BaseService;

public interface InpatientAccountRepayDetailService extends BaseService<InpatientAccountrepaydetail>{

	/**
	 * @Description:获取住院账户支付表信息列表带分页
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @version 1.0
	 * @param entity 实体类
	 * @param page 当前第几页
	 * @param rows 当前页显示条数
	 * @param accountId 账户表id
	 * @param ishis 是否为历史预存金
	 * @return list
	**/
	List<InpatientAccountrepaydetail> getPage(String page, String rows,InpatientAccountrepaydetail entity,String accountId,int ishis);
	/**
	 * @Description:获取住院账户支付表信息列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @version 1.0
	 * @param entity 实体类
	 * @param accountId 账户表id
	 * @param ishis 是否为历史预存金
	 * @return int
	**/
	int getTotal(InpatientAccountrepaydetail entity,String accountId,int ishis);
	/**
	 * @Description:把预存金变为历史预存金
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param accountId   
	 * @return void  
	 * @version 1.0
	**/
	void updateIshis(String accountId);
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	void delByParentId(String id);


}
