package cn.honry.outpatient.preregister.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.preregister.dao.PreregisterDao;
import cn.honry.outpatient.preregister.service.PreregisterService;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("preregisterService")
@Transactional
@SuppressWarnings({ "all" })
public class PreregisterServiceImpl implements PreregisterService{
	
	
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;//医院参数dao
	@Autowired
	@Qualifier(value = "reregisterDAO")
	private PreregisterDao reregisterDAO;
	@Autowired
	private KeyvalueInInterDAO keyvalueInInterDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterPreregisterNow get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(RegisterPreregisterNow entity) {
		
		
		
	}

	@Override
	public List<RegisterPreregisterNow> getPage(String page, String rows,
			RegisterPreregisterNow registerPreregister) {
		return reregisterDAO.getPage(registerPreregister, page, rows);
	}

	@Override
	public int getTotal(RegisterPreregisterNow registerPreregister) {
		return reregisterDAO.getTotal(registerPreregister);
	}

	@Override
	public void saveOrUpdatePreregister(RegisterPreregisterNow registerPreregister) {
		//User user = WebUtils.getSessionUser();
		if(registerPreregister.getId()==null||"".equals(registerPreregister.getId())){//保存
			Integer value = keyvalueInInterDAO.getVal("RegisterPreregister");
			registerPreregister.setPreregisterNo(DateUtils.getStringYear()+DateUtils.getStringMonth()+DateUtils.getStringDay()+count(6,value.toString().length())+value);
			registerPreregister.setId(null);
			registerPreregister.setCreateTime(new Date());
			registerPreregister.setDel_flg(0);
			registerPreregister.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			registerPreregister.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			registerPreregister.setCreateTime(DateUtils.getCurrentTime());
			if(registerPreregister.getStop_flg()==null){
				registerPreregister.setStop_flg(0);
			}
			OperationUtils.getInstance().conserve(null,"预约挂号","INSERT INTO","T_REGISTER_PREREGISTER_NOW",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			registerPreregister.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			registerPreregister.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(registerPreregister.getId(),"预约挂号","UPDATE","T_REGISTER_PREREGISTER_NOW",OperationUtils.LOGACTIONUPDATE);

		}

		

		reregisterDAO.save(registerPreregister);
		
	}
	
	public String count(Integer length,Integer dlen){
		String cV = "";
		Integer wS = length - dlen;
		if(wS>0){
			for (int i = 1; i <= wS; i++) {
				cV += "0";
			}
		}
		return cV;
	}

	@Override
	public void del(String id) {
		reregisterDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id,"预约挂号","UPDATE","T_REGISTER_PREREGISTER",OperationUtils.LOGACTIONDELETE);
	}
	
	
	
	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> deptCombobox(String q) {
		return reregisterDAO.deptCombobox(q);
	}
	/**  
	 * @Description：  挂号科室（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> getDeptCom(String time,String q) {
		return reregisterDAO.getDeptCom(time,q);
	}
	/**  
	 * @Description：  挂号人员（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<EmpScheduleVo> getEmpCom(String page, String rows,String time, String gradeid,
			String deptid,String name) {
		return reregisterDAO.getEmpCom(page,rows,time,gradeid,deptid,name);
	}

	@Override
	public String getParameterByCode(String reserveTime) {
		String time = parameterInnerDAO.getParameterByCode("reserveTime");
		return time;
	}

	@Override
	public List<SysEmployee> getPageSys(String page, String rows,String name,String deptId,String grade) {
		return reregisterDAO.getPageSys( page, rows,name, deptId, grade);
	}

	@Override
	public int getTotalSys(String name) {
		return reregisterDAO.getTotalSys(name);
	}
	@Override
	public int getTotalSys(String name,String deptId,String grade) {
		return reregisterDAO.getTotalSys(name, deptId, grade);
	}
	/**  
	 * @Description：  挂号人员（下拉框）通过预约时间的-总条数
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 */

	@Override
	public int getTotalemp(String time, String gradeid, String deptid,String name) {
		return reregisterDAO.getTotalemp(time,gradeid,deptid,name);
	}
	/**  
	 *  
	 * @Description：  医生工作量统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 5:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  number身份证号
	 * @param  date预约时间
	 */
	@Override
	public String queryPreregisterByMid(String number, String dept, String date) {
		String flag=reregisterDAO.queryPreregisterByMid(number,dept,date);
		
		return flag; 
	}

	/**  
	 *  
	 * @Description：  根据就诊卡ID查询患者是否在患者黑名单中
	 * @Author：zpty
	 * @CreateDate：2016-5-26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getPatientCount(String idcardNo) {
		return reregisterDAO.getPatientCount(idcardNo);
	}
}
