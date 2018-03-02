package cn.honry.outpatient.triage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.outpatient.triage.dao.RegisterTriageDAO;
import cn.honry.outpatient.triage.service.RegisterTriageService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.reflect.TypeToken;

@Service("registerTriageService")
@Transactional
@SuppressWarnings({ "all" })
public class RegisterTriageServiceImpl implements RegisterTriageService{
	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "registerTriageDAO")
	private RegisterTriageDAO registerTriageDAO;
	//继承过来的方法
	@Override
	public void removeUnused(String id) {
		
	}
	//继承过来的方法
	@Override
	public RegistrationNow get(String id) {
		return registerTriageDAO.get(id);
	}
	//继承过来的方法
	@Override
	public void saveOrUpdate(RegistrationNow entity) {
		if (StringUtils.isNotBlank(entity.getId())) {
			registerTriageDAO.update(entity);
		}else {
			registerTriageDAO.save(entity);
		}
	}
	
	/**  
	 * @Description：  科室及医生树
	 * @Author：liudelin
	 * @CreateDate：2015-11-27 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<TreeJson> deptTreeTriage() {
		List<TreeJson> treeDepar = new ArrayList<TreeJson>(); 
		String code = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String name = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();
		String maddiy =null;
		if(StringUtils.isBlank(code)){
			return treeDepar;
		}
		DepartmentContact departmentContact = registerTriageDAO.queryContact(code);
		List<DepartmentContact> departmentContactList = registerTriageDAO.queryContactList(departmentContact.getId());
		TreeJson pTreeJson = new TreeJson();
		pTreeJson.setId(code);
		pTreeJson.setText(name);
		Map<String,String> attributesgen = new HashMap<String, String>();
		attributesgen.put("type","1");
		pTreeJson.setAttributes(attributesgen);
		treeDepar.add(pTreeJson);
		if(departmentContactList!=null && departmentContactList.size()>0){
			for(DepartmentContact contact:departmentContactList){
				TreeJson treeChJson = new TreeJson();
				treeChJson.setId(contact.getDeptId());
				treeChJson.setText(contact.getDeptName());
				Map<String,String> attributesCh = new HashMap<String, String>();
				attributesCh.put("pid",code);
				attributesCh.put("type","2");
				treeChJson.setAttributes(attributesCh);
				treeDepar.add(treeChJson);
				List<RegisterScheduleNow> scheduleList = registerTriageDAO.queryRegisterSchedule(contact.getDeptCode());
				if(scheduleList!=null&&scheduleList.size()>0){
					treeChJson.setState("closed");
					for(RegisterScheduleNow modl:scheduleList){
						if(modl.getMidday()==1){
							maddiy="上午";
						}else if(modl.getMidday()==2){
							maddiy="下午";
						}else{
							maddiy="晚上";
						}
						TreeJson treeTwJson = new TreeJson();
						treeTwJson.setId(modl.getDoctor() + "_" +modl.getMidday());
						SysEmployee employee = registerTriageDAO.queryEmployee(modl.getDoctor());
						treeTwJson.setText(employee.getName()+" "+maddiy);
						Map<String,String> attributesTw = new HashMap<String, String>();
						attributesTw.put("pid",contact.getDeptId());
						attributesTw.put("type","3");
						attributesTw.put("midday",modl.getMidday().toString());
						treeTwJson.setAttributes(attributesTw);
						treeDepar.add(treeTwJson);
					}
				}else{
					treeChJson.setState("open");
				}
			}
		}
		
		return TreeJson.formatTree(treeDepar);
	}
	
	/**  
	 * @Description：  分页患者列表
	 * @Author：liudelin
	 * @CreateDate：2015-11-27 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotal(RegistrationNow registration) {
		return registerTriageDAO.getTotal(registration);
	}
	/**  
	 * @Description：  分页患者列表（记录）
	 * @Author：liudelin
	 * @CreateDate：2015-11-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<RegistrationNow> getPage(RegistrationNow registration, String page,String rows) {
		return registerTriageDAO.getPage(registration,page, rows);
	}
	/**分页患者列表
	 * @Description: 分页患者列表
	 * @param registerInfo
	 * @param page
	 * @param rows
	 * @param flag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息 2登记查询时使用，查询未分诊的患者
	 * @return 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月26日
	 * @Version: V 1.0
	 */
	@Override
	public List<RegistrationNow> getPage(RegistrationNow registration, String page, String rows, String flag) {
		return registerTriageDAO.getPage(registration, page, rows, flag);
	}
	/**分页患者列表
	 * @Description: 分页患者列表
	 * @param registerInfo
	 * @param flag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息 2登记查询时使用，查询未分诊的患者
	 * @return 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月26日
	 * @Version: V 1.0
	 */
	@Override
	public int getTotal(RegistrationNow registration, String flag) {
		return registerTriageDAO.getTotal(registration, flag);
	}
	/** 得到系统参数患者是否先登记后分诊
	 * @Description: 得到系统参数患者是否先登记后分诊
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月18日
	 * @Version: V 1.0
	 */
	@Override
	public Integer getmzfzdj() {
		return registerTriageDAO.getmzfzdj();
	}
	/**登记患者信息
	 * @Description: 登记患者信息 
	 * @param infoJson 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月27日
	 * @Version: V 1.0
	 */
	@Override
	public void saveTriagePatient(String infoJson)throws Exception {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//获取当前登录人
		List<RegistrationNow> list = null;
		list = JSONUtils.fromJson(infoJson, new TypeToken<List<RegistrationNow>>(){});
		for(RegistrationNow registration : list){
			RegistrationNow now = registerTriageDAO.get(registration.getId());
			now.setSeeOptimize(registration.getSeeOptimize());//优先级别
			now.setTriageType(registration.getTriageType());//分诊类型
			now.setTriageFlag(1);//分诊标志
			now.setTriageOpcd(userId);
			now.setTriageDate(new Date());
			now.setUpdateTime(new Date());
			now.setUpdateUser(userId);
			registerTriageDAO.update(now);
		}
	}
	/**确定当前登陆科室是否为可分诊分诊台(不是分诊台返回0 是无维护科室的分诊台返回1 是有维护科室的分诊台返回2)
	 * @Description: 确定当前登陆科室是否为可分诊分诊台
	 * @Author: dutianliang
	 * @CreateDate: 2016年5月13日
	 * @Version: V 1.0
	 */
	@Override
	public String queryDept() {
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		DepartmentContact departmentContact = registerTriageDAO.queryContact(deptid);
		if(departmentContact == null || StringUtils.isBlank(departmentContact.getId())){
			return "0";
		}
		List<DepartmentContact> departmentContactList = registerTriageDAO.queryContactList(departmentContact.getId());
		if(departmentContactList == null || departmentContactList.size() == 0){
			return "1";
		}
		return "2";
	}
	
	/**保存分诊信息
	 * @Description: 保存分诊信息
	 * @param registration 分诊实体
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月27日
	 * @Version: V 1.0
	 */
	@Override
	public void saveTriagePatient(RegistrationNow registration) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//获取当前登录人
		RegistrationNow register = registerTriageDAO.get(registration.getId());
		if(registration.getSeeOptimize() != null){
			register.setTriageFlag(1);
			register.setTriageOpcd(userId);
			register.setTriageDate(new Date());
			register.setSeeOptimize(registration.getSeeOptimize());
		}
		if(registration.getTriageType() != null){
			register.setTriageFlag(1);
			register.setTriageOpcd(userId);
			register.setTriageDate(new Date());
			register.setTriageType(registration.getTriageType());
		}
		register.setUpdateTime(new Date());
		register.setUpdateUser(userId);
		registerTriageDAO.clear();
		registerTriageDAO.update(register);
		
	}
	/** 通过门诊号查询患者挂号信息
	* @Title: queryTriagePatientByClinicCode 
	* @Description: 通过门诊号查询患者挂号信息
	* * @param queryNo
	* @author dtl 
	* @date 2016年11月3日
	*/
	@Override
	public RegistrationNow queryTriagePatientByClinicCode(String queryNo) {
		return registerTriageDAO.queryTriagePatientByClinicCode(queryNo);
	}
	
	
}
