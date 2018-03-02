package cn.honry.finance.outpatientAccount.dao;

import java.util.List;

import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.dao.EntityDao;

/**
 * 患者账户操作流水表
 * @author  wangfujun
 * @date 创建时间：2016年3月30日 下午4:47:04
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@SuppressWarnings({"all"})
public interface AccountrecordDAO extends EntityDao<OutpatientAccountrecord>{
	
	/***
	 * 根据患者账户主键id,查询患者账户操作明细
	 * accountID ：患者账户主键id
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	List<OutpatientAccountrecord> queryDatailed(String accountID,String menuAlias,String page,String rows);
	
	/***
	 * 患者账户操作明细，总条数
	 * accountID	：患者账户主键id
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月5日
	 * @version 1.0
	 * @param
	 * @since
	 */
	int getTotal(String accountID,String menuAlias);
	
}
