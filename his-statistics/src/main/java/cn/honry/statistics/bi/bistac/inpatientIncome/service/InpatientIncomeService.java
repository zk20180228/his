package cn.honry.statistics.bi.bistac.inpatientIncome.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.bi.bistac.inpatientIncome.vo.InpatientIncomeVo;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;

public interface InpatientIncomeService {
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
	public List<InpatientIncomeVo> queryInpatientIncomeVo(String time1,String time2) throws Exception;
	/**  
	 * 
	 * 住院收入分析（new）
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:45:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:45:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryOutpatientUseMedicVo(String time) throws Exception;
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
	public OutpatientUseMedicVo queryOneOutpatientUseMedicVo(String time,String deptName) throws Exception;
	/**  
	 * 
	 * 将科室的name渲染成code
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午11:58:33 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午11:58:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public  Map<String, String> querydeptCodeAndNameMap() throws Exception;
	/**
	 * 住院收入分析 elasticsearch实现
	 * @param time1
	 * @param time2
	 * @return
	 * @throws Exception
	 */
	List<InpatientIncomeVo> queryInpatientIncomeVoFromES(String time1,String time2) throws Exception;
	
	/**  
	 * 
	 *  从mongodb中查询住院收入分析表
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月18日 下午3:28:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月18日 下午3:28:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientUseMedicVo> queryVoByMongo(String time) throws Exception;
	/**  
	 * 
	 * 通过时间和科室从mongodb中查询住院收入分析表
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月18日 下午3:28:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月18日 下午3:28:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientUseMedicVo queryOneVoByMongo(String time,String deptName) throws Exception;
}
