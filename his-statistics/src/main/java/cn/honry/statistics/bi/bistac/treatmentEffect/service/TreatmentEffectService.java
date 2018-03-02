package cn.honry.statistics.bi.bistac.treatmentEffect.service;

import java.util.List;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;

/**   
*  
* @className：ChargeBill
* @description：住院患者费用清单service
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
public interface TreatmentEffectService extends BaseService<TreatmentEffectVo>{
	/**
	 * @Description:查询当前在院人数
	 * @Author：  zpty
	 * @CreateDate： 2017-3-28
	 * @version 1.0
	**/
	int queryInPeople();

	/**
	 * 查询各科室治疗效果的人数
	 * @author zpty
	 * @CreateDate：2017-03-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<TreatmentEffectVo> queryUserRecord(String yearSelect);

	
	/**
	 * 
	 * @Description:根据年份，从mongodb中查数据
	 * @param date :年份，如：2017
	 * @return 返回封装TreatmentEffectVo的list集合
	 * List<TreatmentEffectVo>
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月11日 上午11:31:26
	 */
	public List<TreatmentEffectVo> queryUserRecordByMongo(String date);
}
