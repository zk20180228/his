package cn.honry.outpatient.triage.service;

import java.util.List;

import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

public interface RegisterTriageService extends BaseService<RegistrationNow>{
	/**  
	 * @Description：  科室及医生树
	 * @Author：liudelin
	 * @CreateDate：2015-11-27 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<TreeJson> deptTreeTriage();
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
	 * @CreateDate：2015-11-27 下午05:12:16  
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
	 * @param registration
	 * @param page
	 * @param rows
	 * @param flag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息 2登记查询时使用，查询未分诊的患者
	 * @return 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月26日
	 * @Version: V 1.0
	 */
	List<RegistrationNow> getPage(RegistrationNow registration, String page, String rows,String flag);
	/**分页患者列表
	 * @Description: 分页患者列表
	 * @param registration
	 * @param flag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息
	 * @return 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月25日
	 * @Version: V 1.0
	 */
	int getTotal(RegistrationNow registration, String flag);
	
	/**登记患者信息
	 * @Description: 登记患者信息 
	 * @param infoJson 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月27日
	 * @Version: V 1.0
	 */
	void saveTriagePatient(String infoJson)throws Exception;

	/**确定当前登陆科室是否为可分诊分诊台(不是分诊台返回0 是无维护科室的分诊台返回1 是有维护科室的分诊台返回2)
	 * @Description: 确定当前登陆科室是否为可分诊分诊台
	 * @Author: dutianliang
	 * @CreateDate: 2016年5月13日
	 * @Version: V 1.0
	 */
	String queryDept();
	
	/**保存分诊信息
	 * @Description: 保存分诊信息
	 * @param registration 分诊实体
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月27日
	 * @Version: V 1.0
	 */
	void saveTriagePatient(RegistrationNow registration);
	
	/** 通过门诊号查询患者挂号信息
	* @Title: queryTriagePatientByClinicCode 
	* @Description: 通过门诊号查询患者挂号信息
	* * @param queryNo
	* @author dtl 
	* @date 2016年11月3日
	*/
	RegistrationNow queryTriagePatientByClinicCode(String queryNo);
}
