package cn.honry.statistics.bi.disease.diagnose.dao;

import java.util.List;

import cn.honry.statistics.bi.disease.diagnose.vo.DiagnoseVo;

public interface DiagnoseCountDao {

	List<DiagnoseVo> diagnoseList(String page, String rows, String feature);

	Long diagnoseTotal(String feature);

	List featureMap();
	

}
