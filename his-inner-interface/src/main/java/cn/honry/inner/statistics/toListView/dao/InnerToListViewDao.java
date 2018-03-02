package cn.honry.inner.statistics.toListView.dao;

import java.util.List;

import cn.honry.inner.statistics.toListView.vo.ToListViewVo;
import cn.honry.inner.statistics.totalIncomeCount.vo.MapVo;


public interface InnerToListViewDao {
	ToListViewVo queryVo(String date);
	List<MapVo> queryPieVO(String date);
}
