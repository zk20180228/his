package cn.honry.statistics.bi.bistac.outpatientUseMedic.service;


import java.util.List;

import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;

public interface OutpatientUseMedicService {
	/**  
	 * 
	 * 门诊药品收入 和 门诊总收入
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月12日 下午5:33:37 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月12日 下午5:33:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientUseMedicVo queryCost(String date);
	/**  
	 * 
	 * 从mongodb中查询门诊药品收入 和 门诊总收入
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月11日 下午3:03:15 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月11日 下午3:03:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientUseMedicVo queryCostByMongo(String date);
	/**  
	 * 
	 * 最近12月的人均药费
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryStatisticsCost(String startTime, String endTime);
	/**  
	 * 
	 * 从mongodb中查询门诊月药品金额，用药数量，人次
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午4:08:17 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午4:08:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryStatisticsCostByMongo(String date);
	/**  
	 * 
	 * 最近12月的人均要用药天数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryMedicationDays(String startTime, String endTime);
	/**  
	 * 
	 * 从mongodb中查询最近12月的人均要用药天数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午8:19:30 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午8:19:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientUseMedicVo queryMedicationDaysByMongo(String date);
	/**  
	 * 
	 * 医生用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryDoctCost(String date);
	/**  
	 * 
	 * 从mongodb中查询医生用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 上午11:48:41 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 上午11:48:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryDoctCostByMongo(String date);
	/**  
	 * 
	 * 科室用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryDeptCost(String date);
	/**  
	 * 
	 * 从mongodb中查询科室用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午2:36:51 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午2:36:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryDeptCostByMongo(String date);
	
	/**  
	 * 
	 * 初始化数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月8日 下午2:35:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月8日 下午2:35:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public void init_MZYYJK(String begin,String end,Integer type) throws Exception;
}
