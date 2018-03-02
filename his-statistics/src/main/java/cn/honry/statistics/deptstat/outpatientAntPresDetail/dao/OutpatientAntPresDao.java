package cn.honry.statistics.deptstat.outpatientAntPresDetail.dao;

import java.util.List;
import java.util.Map;

import cn.honry.inner.statistics.outpatientAntPresDetail.vo.OutpatientAntVo;

public interface OutpatientAntPresDao {
	/**
	 * 
	 * 
	 * <p>从mongoDB中查询门诊抗菌药物处方比例 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月7日 上午9:36:49 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月7日 上午9:36:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param searchBegin 开始时间
	 * @param searchEnd 结束时间
	 * @param deptCodes 科室code
	 * @return:
	 *
	 */
	public Map<String,Object> queryList(String searchBegin,String searchEnd,String deptCodes,String menuAlias,String rows,String page);
	/**
	 * 
	 * 
	 * <p>从oracle数据库查询门诊抗菌药物处方比例 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月10日 上午9:45:34 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月10日 上午9:45:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tnL 分区表名
	 * @param searchBegin 开始时间
	 * @param searchEnd 结束时间
	 * @param deptCodes 科室
	 * @param menuAlias 栏目名
	 * @return:
	 *
	 */
	public List<OutpatientAntVo> queryList(String tnL,String searchBegin,String searchEnd,String deptCodes,String menuAlias);
	
}
