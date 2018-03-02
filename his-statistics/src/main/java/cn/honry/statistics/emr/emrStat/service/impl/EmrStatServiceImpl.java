package cn.honry.statistics.emr.emrStat.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.net.aso.b;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.drug.admissionStatistics.dao.AdmissionStatisticsDAO;
import cn.honry.statistics.drug.admissionStatistics.service.AdmissionStatisticsService;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;
import cn.honry.statistics.emr.emrStat.dao.EmrStatDAO;
import cn.honry.statistics.emr.emrStat.service.EmrStatService;
import cn.honry.statistics.emr.emrStat.vo.EmrBaseVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.JSONUtils;

/**  
 * 
 * <p>电子病历统计</p>
 * @Author: dutianliang
 * @CreateDate: 2017年11月13日 下午3:04:48 
 * @Modifier: dutianliang
 * @ModifyDate: 2017年11月13日 下午3:04:48 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Service("emrStatService")
@Transactional
@SuppressWarnings({ "all" })
public class EmrStatServiceImpl implements EmrStatService{
	
	/** 汇总 **/
	@Autowired
	@Qualifier(value = "emrStatDAO")
	private EmrStatDAO emrStatDAO;
	
	@Override
	public AdmissionStatisticsVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(AdmissionStatisticsVo arg0) {
		
	}

	@Override
	public String getJson(String beginTime, String endTime) {
		Integer blood = emrStatDAO.getNum(beginTime, endTime, 1);//输血
		Integer sick = emrStatDAO.getNum(beginTime, endTime, 2);//危重
		Integer death = emrStatDAO.getNum(beginTime, endTime, 3);//死亡
		Integer check = emrStatDAO.getNum(beginTime, endTime, 4);//检查
		Integer opretion = emrStatDAO.getNum(beginTime, endTime, 5);//手术
		Integer antibiotic = emrStatDAO.getNum(beginTime, endTime, 6);//抗生素
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sum", blood + sick + death + check + opretion + antibiotic);
		map.put("blood", blood);
		map.put("sick", sick);
		map.put("death", death);
		map.put("check", check);
		map.put("opretion", opretion);
		map.put("antibiotic", antibiotic);
		String json = JSONUtils.toJson(map);
		return json;
	}


	@Override
	public List<EmrBaseVo> getList(String beginTime, String endTime, int type,
			String page, String rows) {
		return emrStatDAO.getList(beginTime, endTime, type, page, rows);
	}

	@Override
	public Integer getCount(String beginTime, String endTime, int type) {
		return emrStatDAO.getCount(beginTime, endTime, type);
	}

}
