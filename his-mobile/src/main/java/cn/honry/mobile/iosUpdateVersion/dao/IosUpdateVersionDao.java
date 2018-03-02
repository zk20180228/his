package cn.honry.mobile.iosUpdateVersion.dao;

import java.util.List;

import cn.honry.base.bean.model.MIosApkVersion;
import cn.honry.base.dao.EntityDao;

public interface IosUpdateVersionDao  extends EntityDao<MIosApkVersion>{

	/** 根据查询条件获得总条数
	* @param pageUtil 封装了查询条件的pageUtil
	* @return Integer
	* @author zxl
	* @date 2017年6月20日
	*/
	Integer getTotal(MIosApkVersion mIosApkVersion) throws Exception;

	/** 根据查询条件获得一页数据
	* @param pageUtil 封装了查询条件的pageUtil
	* @return List<MApkVersion>
	* @author zxl
	* @date 2017年6月20日
	*/
	List<MIosApkVersion> getList(MIosApkVersion mIosApkVersion, String rows, String page) throws Exception;

}
