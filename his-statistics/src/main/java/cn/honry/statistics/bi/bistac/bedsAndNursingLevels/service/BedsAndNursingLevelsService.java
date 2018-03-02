package cn.honry.statistics.bi.bistac.bedsAndNursingLevels.service;

import java.util.List;

import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.vo.BedsAndNursingLevelsVo;


public interface BedsAndNursingLevelsService {

	//床位使用情况统计
	List<BedsAndNursingLevelsVo> queryBeds( ) throws Exception;
	
	//护理级别情况统计
	List<BedsAndNursingLevelsVo> queryNursingLevels( ) throws Exception;

	
}
