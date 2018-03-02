package cn.honry.mobile.updateVersion.dao;

import java.util.List;

import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.dao.EntityDao;

public interface UpdateVersionDao  extends EntityDao<MApkVersion>{

	/** 根据查询条件获得总条数
	* @param pageUtil 封装了查询条件的pageUtil
	* @return Integer
	* @author zxl
	* @date 2017年6月20日
	*/
	Integer getTotal(MApkVersion mApkVersion) throws Exception;

	/** 根据查询条件获得一页数据
	* @param pageUtil 封装了查询条件的pageUtil
	* @return List<MApkVersion>
	* @author zxl
	* @date 2017年6月20日
	*/
	List<MApkVersion> getList(MApkVersion mApkVersion, String rows, String page) throws Exception;

}
