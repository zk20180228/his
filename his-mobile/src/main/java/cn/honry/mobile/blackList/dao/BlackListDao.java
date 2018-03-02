package cn.honry.mobile.blackList.dao;

import java.util.List;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.dao.EntityDao;

public interface BlackListDao extends EntityDao<MBlackList>{


	/** 根据查询条件获得总条数
	* @param blackList 封装了查询条件的blackList
	* @return Integer
	* @author zxl
	* @date 2017年6月20日
	*/
	Integer getTotal(MBlackList blackList) throws Exception;

	/** 根据查询条件获得一页数据
	* @param rows
	* @param page
	* @param blackList 封装了查询条件的blackList
	* @return List<MWhiteList>
	* @author zxl
	* @date 2017年6月20日
	*/
	List<MBlackList> getList(String rows, String page, MBlackList blackList) throws Exception;
	

	/** 通过用户账户获得黑名单信息
	* @param mBlackList
	* @return MBlackList  黑名单信息实体
	* @author zxl 
	* @date 2017年6月20日
	*/
	MBlackList findBlackByUserAccunt(MBlackList mBlackList) throws Exception;
	

	/** 通过用户账户及分类删除黑名单信息
	* @param mBlackList 
	* @author zxl 
	* @date 2017年6月20日
	*/
	void delBlackByUserAccount(MBlackList mBlack) throws Exception;
	
	/** 通过用户id获得黑名单信息(查看页面经过渲染不是全字段)
	* @param id 版本ID
	* @return MBlackList  黑名单信息实体
	* @author zxl 
	* @date 2017年6月20日
	*/
	MBlackList getMBlackById(String id);

	/** 删除黑名单信息
	* @param MBlackList
	* @return 
	* @author zxl 
	* @date 2017年6月20日
	*/
	void delBlackById(MBlackList mBlackList);
}
