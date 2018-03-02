package cn.honry.patinent.account.dao;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.bean.model.PatientAccountdetail;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface AccountDetailDAO extends EntityDao<PatientAccountdetail>{
	/**
	 * @Description:获取患者账户操作记录表信息列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @return 
	**/
	List<PatientAccountdetail> getPage(PatientAccountdetail entity, String page, String rows,String accountId);
	/**
	 * @Description:获取患者账户操作记录表信息列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @return 
	**/
	int getTotal(PatientAccountdetail entity,String accountId);
	/**
	 * @Description:按ParentId 物理删除
	 * @Author：  lt
	 * @CreateDate： 2015-11-10
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void delByParentId(String id);
	
	/**
	 * @Description:根据领取人，发票类型，使用状态查询
	 * @Author：  wj
	 * @CreateDate： 2015-12-8
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param emp 
	 * @return 
	**/
	
	FinanceInvoice findbyall(String emp, String type);
	/**
	 * @Description:根据用户id查员工
	 * @Author：  wj
	 * @CreateDate： 2015-12-8
	 * @ModifyRmk：  
	 * @version 1.0
	 * @return 
	**/
	
	SysEmployee findEmpByUserid(String id);
	/**
	 * @Description:根据员工id查询所在的财务分组
	 * @Author：  wj
	 * @CreateDate： 2016-1-19
	 * @ModifyRmk：  
	 * @version 1.0
	 * @return 
	**/
	FinanceUsergroup findFinanceByempId(String id);
	
}
