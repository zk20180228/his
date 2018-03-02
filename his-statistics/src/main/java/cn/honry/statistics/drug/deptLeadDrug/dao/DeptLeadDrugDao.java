package cn.honry.statistics.drug.deptLeadDrug.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;

@SuppressWarnings({"all"})
public interface DeptLeadDrugDao extends EntityDao<DrugApplyout>{
	/***
	 *  查询各科室领药记录
	 *@author tuchuanjiang
	 *@createDate ：2016年6月25日
	 *@version 1.0
	 */
	List<DrugApplyoutNow> queryTableList(List<String> tnL,String startTime, String endTime,
			String drugDept, String drugxz, String drugName,String page,String rows) throws Exception;
	/***
	 *  查询各科室领药记录数
	 *@author tuchuanjiang
	 *@createDate ：2016年6月25日
	 *@version 1.0
	 */
	int queryTableListToatl(List<String> tnL, String stime, String etime,
			String drugDept, String drugxz, String drugName) throws Exception;

	/***
	 *  查询科室list
	 * @author  tuchuanjiang
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	List<SysDepartment> querydrugDept() throws Exception;
	/***
	 *  查询药品名称list
	 * @author  tuchuanjiang
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	List<DrugInfo> querydrugName() throws Exception;
	/***
	 *  查询最大最小时间
	 * @author  tuchuanjiang
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	StatVo findMaxMin() throws Exception;
	
}
