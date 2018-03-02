package cn.honry.statistics.deptstat.dischargeStatistics.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;
import cn.honry.statistics.deptstat.dischargeStatistics.dao.DischargeStatisticsDao;
import cn.honry.statistics.deptstat.dischargeStatistics.service.DischargeStatisticsService;
import cn.honry.statistics.deptstat.dischargeStatistics.vo.DischargeStatisticsVo;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
@Service("dischargeStatisticsService")
@Transactional
@SuppressWarnings({ "all" })
public class DischargeStatisticsServiceImpl implements
		DischargeStatisticsService {
	
	@Autowired
	@Qualifier(value = "dischargeStatisticsDao")
	private DischargeStatisticsDao dischargeStatisticsDao;	
	@Override
	public List<DischargeStatisticsVo> queryIllness() {
		return dischargeStatisticsDao.queryIllness();
	}

	/**  
	 * 出院病种统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<DischargeStatisticsVo> queryDischargeStat(String startTime,String endTime,String deptCode,String page, String rows, String menuAlias) {
		boolean flag=new MongoBasicDao().isCollection("CYBZTJCX");
		List<DischargeStatisticsVo> list=new ArrayList<DischargeStatisticsVo>();
		if(flag){
			list=dischargeStatisticsDao.queryDischargeStatForDB(startTime,endTime,deptCode,page,rows,menuAlias);
		}else{
			list=dischargeStatisticsDao.queryDischargeStat(startTime,endTime,deptCode,page,rows,menuAlias);
		}
		return list;
	}

	/**  
	 * 出院病种统计 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalDischargeStat(String startTime,String endTime,String deptCode,String page, String rows, String menuAlias) {
		boolean flag=new MongoBasicDao().isCollection("CYBZTJCX");
		List<DischargeStatisticsVo> list=new ArrayList<DischargeStatisticsVo>();
		if(flag){
		}else{
			return dischargeStatisticsDao.getTotalDischargeStat(startTime,endTime,deptCode,page,rows,menuAlias);
		}
		return list.size();
	}
}
