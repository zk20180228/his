package cn.honry.statistics.emr.emrStat.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;
import cn.honry.statistics.emr.emrStat.vo.EmrBaseVo;
/***
 * 用药统计DAO层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@SuppressWarnings("all")
public interface EmrStatDAO extends EntityDao<AdmissionStatisticsVo>{
	
	/**  
	 * 
	 * <p>查询数量 type：1输血，2危重，3死亡，4检验检查，5手术，6抗生素</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年11月13日 下午3:23:25 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年11月13日 下午3:23:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param type 1输血，2危重，3死亡，4检验检查，5手术，6抗生素
	 *
	 */
	Integer getNum(String beginTime, String endTime, int type);

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
