package cn.honry.patinent.patientBlack.dao;

import java.util.List;

import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface PatientBlackDAO extends EntityDao<PatientBlackList>{
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:17  
	 *
	 */
	int getTotal(PatientBlackList patientBlackList);

	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:48  
	 * @version 1.0
	 *
	 */
	List<PatientBlackList> getPage(String page,String rows,PatientBlackList patientBlackList);
	
	/**  
	 *  
	 * @Description：  删除
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:48  
	 * @version 1.0
	 * @param reason 
	 *
	 */
	String del(String ids, String reason);
}
