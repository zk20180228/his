package cn.honry.inner.statistics.outpatientAntPresDetail.dao;

/**
 * 
 * 
 * <p>门诊抗菌药物处方比例在线更新预处理DAO </p>
 * @Author: XCL
 * @CreateDate: 2017年7月6日 下午5:55:33 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月6日 下午5:55:33 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface OutpatientAntDao {
	/**
	 * 
	 * 
	 * <p>门诊抗菌药物处方比例在线更新预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月6日 下午5:56:45 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月6日 下午5:56:45 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目名
	 * @param type 预处理类型 目前只支持天
	 * @param date: yyyy-MM-dd
	 *
	 */
	public void init_MZKJYWCFBL(String menuAlias, String type, String date);
}
