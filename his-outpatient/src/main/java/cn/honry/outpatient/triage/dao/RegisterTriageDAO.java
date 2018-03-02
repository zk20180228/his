package cn.honry.outpatient.triage.dao;

import java.util.List;

import cn.honry.base.bean.model.Clinic;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface RegisterTriageDAO  extends EntityDao<RegistrationNow>{
	/**  
	 * @Description：  科室间关系
	 * @Author：liudelin
	 * @CreateDate：2015-11-30下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<DepartmentContact> queryContactList(String id);
	/**  
	 * @Description：  科室ID之间的转化
	 * @Author：liudelin
	 * @CreateDate：2015-11-30下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	DepartmentContact queryContact(String parDeptId);
	/**  查询医生
	* @Title: queryEmployee 
	* @Description:  查询医生
	* @param jobNo 医生工作号
	* @author dtl 
	* @date 2016年10月20日
	*/
	SysEmployee queryEmployee(String jobNo);
	/**  
	 * @Description：  分页患者列表
	 * @Author：liudelin
	 * @CreateDate：2015-11-27 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	int getTotal(RegistrationNow registration);
	/**  
	 * @Description：  分页患者列表（记录）
	 * @Author：liudelin
	 * @CreateDate：2015-11-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegistrationNow> getPage(RegistrationNow registration, String page,String rows);
	
	/** 得到系统参数患者是否先登记后分诊
	 * @Description: 得到系统参数患者是否先登记后分诊
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月18日
	 * @Version: V 1.0
	 */
	Integer getmzfzdj();
	/**分页患者列表
	 * @Description: 分页患者列表
	 * @param registerInfo
	 * @param page
	 * @param rows
	 * @param flagflag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息
	 * @return 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月26日
	 * @Version: V 1.0
	 */
	List<RegistrationNow> getPage(RegistrationNow registration, String page, String rows,String flag);
	/**分页患者列表
	 * @Description: 分页患者列表
	 * @param registerInfo
	 * @param flag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息 2登记查询时使用，查询未分诊的患者 2登记查询时使用，查询未分诊的患者
	 * @return 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月25日
	 * @Version: V 1.0
	 */
	int getTotal(RegistrationNow registration, String flag);
	/**  根据门诊科室code查询下属诊室关系
	* @Title: queryClinicList  
	* @Description:  根据门诊科室code查询下属诊室关系 
	* @param deptCode门诊科室code
	* @author dtl 
	* @date 2016年10月20日
	*/
	List<Clinic> queryClinicList(String deptCode);
	/**  
	 * @Description：  根据科室查询科室下当天的排版
	 * @Author：liudelin
	 * @CreateDate：2015-11-30下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegisterScheduleNow> queryRegisterSchedule(String deptId);
	
	/** 通过门诊号查询患者挂号信息
	* @Title: queryTriagePatientByClinicCode 
	* @Description: 通过门诊号查询患者挂号信息
	* * @param queryNo
	* @author dtl 
	* @date 2016年11月3日
	*/
	RegistrationNow queryTriagePatientByClinicCode(String queryNo);
	
	/** 更新挂号信息
	* @Title: updateTriage 
	* @param registration
	* @author dtl 
	* @date 2016年11月21日
	*/
	void updateTriage(RegistrationNow registration);
	

}
