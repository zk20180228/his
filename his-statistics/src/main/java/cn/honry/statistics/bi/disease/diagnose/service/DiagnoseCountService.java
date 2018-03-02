package cn.honry.statistics.bi.disease.diagnose.service;

import java.util.List;

import cn.honry.statistics.bi.disease.diagnose.vo.DiagnoseVo;

public interface DiagnoseCountService {

	List<DiagnoseVo> diagnoseList(String page, String rows, String feature);

	Long diagnoseTotal(String feature);
	
	public List featureMap();
	

}
