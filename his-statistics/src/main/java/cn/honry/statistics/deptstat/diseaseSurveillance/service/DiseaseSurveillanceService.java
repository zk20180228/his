package cn.honry.statistics.deptstat.diseaseSurveillance.service;

import java.util.List;

import cn.honry.statistics.deptstat.diseaseSurveillance.vo.DiseaseSurveillanceVo;


public interface DiseaseSurveillanceService {

	//床位使用情况统计
	List<DiseaseSurveillanceVo> queryDiseaseSurveillance( );
	
}
