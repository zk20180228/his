package cn.honry.statistics.bi.bistac.inpatientIncome.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.inpatientIncome.vo.InpatientIncomeVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;

@SuppressWarnings({"all"})
public interface InpatientIncomeDao extends EntityDao<InpatientIncomeVo>{
	/**  
	 * 
	 * 住院收入分析
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月20日 上午11:10:02 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月20日 上午11:10:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<InpatientIncomeVo> queryInpatientIncomeVo(List<String> list) throws Exception;
	/**  
	 * 
	 *  住院收入分析（new）
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:49:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:49:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryOutpatientUseMedicVo(List<String> tnL,List<String> tnL2,String begin,String end) throws Exception;
	/**  
	 * 
	 *  住院收入分析（new）,根据时间和科室查
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:49:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:49:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientUseMedicVo queryOneOutpatientUseMedicVo(List<String> tnL,List<String> tnL2,String begin,String end,String deptName) throws Exception;

	/**  
	 * 
	 * 获取住院结算表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月20日 下午1:54:38 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月20日 下午1:54:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public InpatientIncomeVo findMaxMin() throws Exception;
}
