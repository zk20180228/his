package cn.honry.statistics.deptstat.diseaseSurveillance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.diseaseSurveillance.dao.DiseaseSurveillanceDao;
import cn.honry.statistics.deptstat.diseaseSurveillance.service.DiseaseSurveillanceService;
import cn.honry.statistics.deptstat.diseaseSurveillance.vo.DiseaseSurveillanceVo;

@Service("diseaseSurveillanceService")
@Transactional
@SuppressWarnings({"all"})
public class DiseaseSurveillanceServiceImpl implements DiseaseSurveillanceService{
	@Autowired
	@Qualifier(value = "diseaseSurveillanceDao")
	private DiseaseSurveillanceDao diseaseSurveillanceDao;
	/**  
	 * 
	 * 重点疾病监测汇总
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<DiseaseSurveillanceVo> queryDiseaseSurveillance() {
		return diseaseSurveillanceDao.queryDiseaseSurveillance();
	}
}
