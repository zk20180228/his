package cn.honry.patinent.patientBlack.service;

import java.util.List;

import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.service.BaseService;

public interface PatientBlackService  extends BaseService<PatientBlackList>{
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:17  
	 *
	 */
	int getTotal(PatientBlackList patientBlack);

	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:48  
	 * @version 1.0
	 *
	 */
	List<PatientBlackList> getPage(String page,String rows,PatientBlackList patientBlack);
	/**  
	 *  
	 * @Description：  删除
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:48  
	 * @version 1.0
	 * @param reason 
	 *
	 */
	void del(String ids, String reason);
	/**  
	 *  
	 * @Description：保存
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:48  
	 * @version 1.0
	 *
	 */
	void save(PatientBlackList entity);
}
