package cn.honry.mobile.whiteList.dao;

import java.util.List;

import cn.honry.base.bean.model.MWhiteList;
import cn.honry.base.dao.EntityDao;

public interface WhiteListDao extends EntityDao<MWhiteList>{

	/** 根据查询条件获得总条数
	* @param mWhiteList 封装了查询条件的mWhiteList
	* @return Integer
	* @author zxl
	* @date 2017年6月20日
	*/
	Integer getTotal(MWhiteList mWhiteList) throws Exception;

	/** 根据查询条件获得一页数据
	* @param rows
	* @param page
	* @param mWhiteList 封装了查询条件的mWhiteList
	* @return List<MWhiteList>
	* @author zxl
	* @date 2017年6月20日
	*/
	List<MWhiteList> getList(String rows, String page, MWhiteList mWhiteList) throws Exception;

	/** 通过用户账户获得白名单信息
	* @param mWhiteList
	* @return MWhiteList  白名单信息实体
	* @author zxl 
	* @date 2017年6月20日
	*/
	MWhiteList findWhiteByUserAccunt(MWhiteList mWhiteList) throws Exception;

	/** 通过用户账户及分类删除白名单信息
	* @param mWhiteList 
	* @author zxl 
	* @date 2017年6月20日
	*/
	void delWhiteByUserAccount(MWhiteList mWhite) throws Exception;
	
	/** 通过用户id获得白名单信息(查看页面)
	* @param id 版本ID
	* @return mWhiteList  黑名单信息实体
	* @author zxl 
	* @date 2017年6月20日
	*/
	MWhiteList getMWhiteById(String id);

	/** 删除白名单信息
	* @param MWhiteList
	* @return 
	* @author zxl 
	* @date 2017年6月20日
	*/
	void delWhiteById(MWhiteList mWhiteList);
}
