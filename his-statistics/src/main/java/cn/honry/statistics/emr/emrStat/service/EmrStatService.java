package cn.honry.statistics.emr.emrStat.service;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;
import cn.honry.statistics.emr.emrStat.vo.EmrBaseVo;
import cn.honry.utils.FileUtil;
/***
 * 电子病历统计service层
 * @Description:
 * @author: dutianliang
 * @CreateDate: 2017年11月13日
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface EmrStatService extends BaseService<AdmissionStatisticsVo>{

	/**  
	 * 
	 * <p>查询数据</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年11月13日 下午3:04:27 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年11月13日 下午3:04:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 *
	 */
	String getJson(String beginTime, String endTime);
	/**  
	 * 
	 * <p> </p>
	 * @Author dutianliang
	 * @CreateDate 2017年11月25日 上午11:05:26
	 * @Modifier dutianliang
	 * @ModifyDate 2017年11月25日 上午11:05:26
	 * @ModifyRmk 
	 * @version V1.0
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * type 1输血，2危重，3死亡，4检验检查，5手术，6抗生素,7全部
	 * @param page
	 * @param rows
	 *
	 */
	List<EmrBaseVo> getList(String beginTime, String endTime, int type,
			String page, String rows);
	/**  
	 * 
	 * <p> </p>
	 * @Author dutianliang
	 * @CreateDate 2017年11月25日 上午11:05:26
	 * @Modifier dutianliang
	 * @ModifyDate 2017年11月25日 上午11:05:26
	 * @ModifyRmk 
	 * @version V1.0
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param type 1输血，2危重，3死亡，4检验检查，5手术，6抗生素,7全部
	 *
	 */
	Integer getCount(String beginTime, String endTime, int type);
}
