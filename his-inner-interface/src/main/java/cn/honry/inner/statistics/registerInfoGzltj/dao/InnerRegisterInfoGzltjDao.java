package cn.honry.inner.statistics.registerInfoGzltj.dao;

import java.util.List;

import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInnerVo;

public interface InnerRegisterInfoGzltjDao {

	/**
	 * 查询当天的统计数据
	 * @param date
	 * @return
	 */
	List<RegisterInnerVo> queryRegister(String date);
	
	/**
	 * 保存方法
	 * @param obj
	 */
	void save(Object obj);
}
