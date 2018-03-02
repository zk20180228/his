package cn.honry.statistics.bi.statisticalSetting.dao;

import java.util.List;

import cn.honry.base.bean.model.BiDimensionSet;
import cn.honry.base.bean.model.BiIndexSet;
import cn.honry.base.bean.model.BiStatSet;
import cn.honry.base.bean.model.BiSubsectionSet;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.statisticalSetting.vo.VoshowList;
import cn.honry.statistics.bi.statisticalSetting.vo.VtableName;

public interface StatisticalSettingDAO extends EntityDao<BiStatSet>{
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
	 * 根据维度编号查询分段记录
	 * @param dimensionNumber
	 * @return
	 */
	List<BiSubsectionSet> queryBiSubsectionSet(String dimensionNumber);
	/**
	 * 根据维度编号查询指标
	 * @param dimensionNumber
	 * @return
	 */
	List<BiIndexSet> queryBiIndexSet(String dimensionNumber);
	/**
	 * 动态创建表
	 */
	void createTable(String field,List<String> list,String polymerization);
	/**
	 * 根据组别号和维度编号查询   指标
	 */
	List<BiIndexSet> quertIndexFiled(String dimensionNumber,String groupId);
	/**
	 * 根据组别号和维度编号查询   分段 
	 */
	List<BiSubsectionSet> quertSubsectionFiled(String dimensionNumber,String groupId);
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
