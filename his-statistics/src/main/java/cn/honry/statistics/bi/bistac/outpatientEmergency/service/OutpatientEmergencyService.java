package cn.honry.statistics.bi.bistac.outpatientEmergency.service;

import cn.honry.statistics.bi.bistac.outpatientEmergency.vo.OutpatientEmergencyVo;

public interface OutpatientEmergencyService {

	OutpatientEmergencyVo getDataInfoByTime(String sTime) throws Exception ;
}
