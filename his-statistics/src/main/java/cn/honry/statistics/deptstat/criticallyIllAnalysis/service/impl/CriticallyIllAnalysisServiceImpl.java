package cn.honry.statistics.deptstat.criticallyIllAnalysis.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.dao.CriticallyIllAnalysisDao;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.service.CriticallyIllAnalysisService;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.vo.CriticallyIllAnalysisVo;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;
import cn.honry.statistics.deptstat.outPatientMessage.dao.OutPatientMessageDao;
import cn.honry.statistics.deptstat.outPatientMessage.service.OutPatientMessageService;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("criticallyIllAnalysisService")
@Transactional
@SuppressWarnings({"all"})
public class CriticallyIllAnalysisServiceImpl implements CriticallyIllAnalysisService{
	@Autowired
	@Qualifier(value = "criticallyIllAnalysisDao")
	private CriticallyIllAnalysisDao criticallyIllAnalysisDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/**  
	 * 重症患者占比分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */

	@Override
	public List<CriticallyIllAnalysisVo> queryCriticallyIllAnalysis(String startTime,String endTime,String deptCode,String menuAlias) {
		boolean flag=new MongoBasicDao().isCollection("ZZHZZBFX");
		List<CriticallyIllAnalysisVo> list=new ArrayList<CriticallyIllAnalysisVo>();
		if(flag){
			list=criticallyIllAnalysisDao.queryCriticallyIllAnalysisForDB(startTime,endTime,deptCode,menuAlias);
		}else{
			return criticallyIllAnalysisDao.queryCriticallyIllAnalysis(startTime,endTime,deptCode,menuAlias);
		}
		return list;
	}
	
}
