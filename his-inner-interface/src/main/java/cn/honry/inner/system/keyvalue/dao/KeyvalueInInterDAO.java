package cn.honry.inner.system.keyvalue.dao;

import javax.print.DocFlavor.STRING;

import cn.honry.base.bean.model.SysKeyvalue;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface KeyvalueInInterDAO extends EntityDao<SysKeyvalue>{

	/**  
	 *  
	 * @Description：  获得流水号
	 * @Author：aizhonghua
	 * @CreateDate：2015-5-26 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-5-26 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getVal(String string);

	/**
	 * @Description:根据登录人科室编号 和标志“入库单据号”查询value值
	 * @Author：  lt
	 * @CreateDate： 2015-7-22
	 * @param @param deptId
	 * @param @param flag
	 * @param @return   
	 * @return String  
	 * @version 1.0
	**/
	int getVal(String deptId, String flag,String currentYearMonth);
	
	
	/**
	 * @Description: 根据登录人科室编号 当前时间 和标志“入库单据号”查询完整的值
	 * @param deptId 登录人科室编号
	 * @param flag 标志
	 * @param currentYearMonth 当前时间 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月13日
	 * @Version: V 1.0
	 */
	String getTotalVal(String deptId, String flag,String currentYearMonth);

}
