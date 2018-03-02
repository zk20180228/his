package cn.honry.statistics.bi.bistac.treatmentEffect.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;

/**   
*  
* @className：TreatmentEffect
* @description：治疗效果数据分析dao
* @author：zpty
* @createDate：2017-03-28  
* @modifyRmk：  
* @version 1.0
 */
public interface TreatmentEffectDao extends EntityDao<TreatmentEffectVo>{
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
	List<TreatmentEffectVo> queryUserRecord(List<String> tnL, final String date, final String deptName, final String begin, final String end);
	/**
	 * 获取业务表中最大及最小时间
	 * @Author zxh
	 * @time 2017年4月7日
	 * @return
	 */
	TreatmentEffectVo findMaxMin();
	
	
	
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
