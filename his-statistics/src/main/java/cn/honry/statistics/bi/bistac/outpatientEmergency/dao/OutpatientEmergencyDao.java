package cn.honry.statistics.bi.bistac.outpatientEmergency.dao;

import java.util.List;

import cn.honry.statistics.bi.bistac.outpatientEmergency.vo.OutpatientEmergencyVo;

public interface OutpatientEmergencyDao{

	OutpatientEmergencyVo getDataInfoByTime(String date, List<String> tnL)throws Exception;
}
