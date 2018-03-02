package cn.honry.patinent.account.dao;

import java.util.List;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.bean.model.PatientAccountdetail;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface AccountrepayDetailDAO extends EntityDao<PatientAccountrepaydetail>{
	/**
	 * @Description:获取患者账户支付表信息列表带分页
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @param page 当前第几页
	 * @param rows 当前页显示条数
	 * @param accountId 账户表id
	 * @param ishis 是否为历史预存金
	 * @return list
	**/
	List<PatientAccountrepaydetail> getPage(PatientAccountrepaydetail entity, String page, String rows,String accountId,int ishis,int detailAccounttype);
	/**
	 * @Description:获取患者账户支付表信息列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @param accountId 账户表id
	 * @param ishis 是否为历史预存金
	 * @return int
	**/
	int getTotal(PatientAccountrepaydetail entity,String accountId,int ishis,int detailAccounttype);

	/**
	 * @Description:统计子表信息
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	double totalAccount(String accountId);

	/**
	 * @Description:根据主表账户id查询支付表子表列表信息
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> listRepayDetail(String accountId);

	/**
	 * @Description:修改预存金为历史预存金
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void updateIshis(String accountId);
	/**
	 * @Description:根据父id 物理删除
	 * @Author：  lt
	 * @CreateDate： 2015-11-10
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void delByParentId(String id);
}
