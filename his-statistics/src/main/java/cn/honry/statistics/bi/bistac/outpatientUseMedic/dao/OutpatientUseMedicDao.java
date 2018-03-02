package cn.honry.statistics.bi.bistac.outpatientUseMedic.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;

public interface OutpatientUseMedicDao extends EntityDao<OutpatientUseMedicVo>{
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
	public OutpatientUseMedicVo queryCost(List<String> tnL,String begin,String end);
	
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
	public List<OutpatientUseMedicVo> queryStatisticsCost(List<String> tnL,String begins, String ends);
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
	public List<OutpatientUseMedicVo> queryMedicationDays(List<String> tnL,String begin, String end);
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
	public List<OutpatientUseMedicVo> queryDoctCost(List<String> tnL, String begin,String end);
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
	public List<OutpatientUseMedicVo> queryDeptCost(List<String> tnL, String begin,String end);
	
	/**   
	 * 
	 * 获取门诊处方明细表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月12日 下午5:33:37 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月12日 下午5:33:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientUseMedicVo findMaxMin();
}
