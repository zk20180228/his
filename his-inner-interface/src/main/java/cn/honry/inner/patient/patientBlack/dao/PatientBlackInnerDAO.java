package cn.honry.inner.patient.patientBlack.dao;

import java.util.List;

import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface PatientBlackInnerDAO extends EntityDao<PatientBlackList>{
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
	 * @Description：  患者黑名单添加患者
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-10-25 上午3:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	void seveBlackPatient(PatientBlackList patientBlackList);
}
