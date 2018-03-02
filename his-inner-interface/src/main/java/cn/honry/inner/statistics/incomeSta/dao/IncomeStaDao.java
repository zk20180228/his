package cn.honry.inner.statistics.incomeSta.dao;

import java.util.List;

import cn.honry.inner.statistics.incomeSta.vo.IncomeStatisticsVO;

public interface IncomeStaDao {
	List<IncomeStatisticsVO> queryVo(String date);
	List<String> queryNameList();
}
