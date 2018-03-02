package cn.honry.statistics.bi.statisticalSetting.service;

import java.util.List;

import cn.honry.base.bean.model.BiDimensionSet;
import cn.honry.base.bean.model.BiStatSet;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.statisticalSetting.vo.VoshowList;
import cn.honry.statistics.bi.statisticalSetting.vo.VtableName;

public interface StatisticalSettingService extends BaseService<BiStatSet>{
	/**
	 * 查询统计图表列表
	 * @param BiStatSet
	 * @param page
	 * @param rows
	 * @return
	 */
	List<BiStatSet> queryBiStatSetList(BiStatSet BiStatSet,String page,String rows);
	/**
	 * 查询统计图表总数
	 * @param BiStatSet
	 * @return
	 */
	int queryBiStatSetTotal(BiStatSet BiStatSet);
	/**
	 * 查询所有的表名
	 * @return
	 */
	List<VtableName> querytablename();
	/**
	 * 根据表名查询该表的所有字段
	 * @return
	 */
	List<VtableName> queryColumnname(String tableName);
	/**
	 * 查询维度表的所有
	 * @return
	 */
	List<BiDimensionSet> queryBiDimensionSet(String dimensionNumber);
	/**
	 * 保存指标 、 分段
	 * @param biSubsectionSetJson
	 * @param biIndexSetJson
	 * @param dimensionNumber
	 */
	void saveIndexOrSubsection(String biSubsectionSetJson,String biIndexSetJson,String dimensionNumber);
	/**
	 * 查询视图的字段的名称
	 * @param viewName
	 * @return
	 */
	List<VtableName> queryViewColumnName(String viewName);
	/**
	 * 查询合并表的数据
	 */
	List<Object> queryObject();
	/**
	 * 查询合并表的数据
	 * @return
	 */
	List<VoshowList> queryListShowList();
}
