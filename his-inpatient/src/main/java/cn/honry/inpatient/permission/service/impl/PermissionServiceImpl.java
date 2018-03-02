package cn.honry.inpatient.permission.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPermission;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.inpatient.permission.dao.PermissionDao;
import cn.honry.inpatient.permission.service.PermissionService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

/**   
* @className：PermissionServiceImpl
* @description：  医嘱授权serviceImpl
* @author：tuchuanjiang
* @createDate：2015-12-21 下午19：50  
* @version 1.0
 */
@Service("permissionService")
@Transactional
@SuppressWarnings({ "all" })
public class PermissionServiceImpl implements PermissionService{
	
	@Autowired
	@Qualifier(value = "permissionDao")
	private PermissionDao permissionDao;
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeDAO;
	
	public void setEmployeeDAO(EmployeeInInterDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientPermission get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientPermission entity) {
		
	}
	/**  
	 * @Description：通过住院流水号查询医嘱授权历史记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientPermission> queryListById(String no) throws Exception {
		List<InpatientPermission> inpatientPerMission = permissionDao.queryListById(no);
		return inpatientPerMission;
	}
	/**  
	 * @Description：通过Id删除医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 上午11:50  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public void delById(String ids)throws Exception {
		String [] idsArr=ids.split(",");
		permissionDao.delById(idsArr, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	}
	/**  
	 * @Description：通过病历号 查询医嘱授权记录信息
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 15 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientPermission queryPermissionById(String inpatientNo)throws Exception {
		return permissionDao.queryPermissionById(inpatientNo);
	}
	/**  
	 * @Description：保存或修改医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public void saveOrUpdateList(InpatientPermission inpatientPermission) throws Exception {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		List<SysEmployee> sysemp = employeeDAO.findEmpByUserAccunt(userId);
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		inpatientPermission.setHospitalId(HisParameters.CURRENTHOSPITALID);
		inpatientPermission.setAreaCode(inprePayDAO.getDeptArea(deptId));
		if(StringUtils.isBlank(inpatientPermission.getId())){//添加
			inpatientPermission.setId(null);
			inpatientPermission.setDel_flg(0);
			inpatientPermission.setCreateUser(userId);
			inpatientPermission.setCreateDept(deptId);
			inpatientPermission.setCreateTime(new Date());
			inpatientPermission.setOperDate(new Date());
			inpatientPermission.setOperCode(sysemp.get(0).getJobNo());
			String s = UUID.randomUUID().toString(); 
			String no=s.substring(0, 19);
			inpatientPermission.setPermissionNo(no);
			
			permissionDao.save(inpatientPermission);
			OperationUtils.getInstance().conserve(null,"医嘱授权记录保存","INSERT INTO","T_INPATIENT_CONSULTATION",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			inpatientPermission.setUpdateUser(userId);
			inpatientPermission.setUpdateTime(new Date());
			inpatientPermission.setOperDate(new Date());
			inpatientPermission.setOperCode(sysemp.get(0).getJobNo());
			permissionDao.update(inpatientPermission);
			OperationUtils.getInstance().conserve(null,"医嘱授权记录修改","UPDATE","T_INPATIENT_CONSULTATION",OperationUtils.LOGACTIONINSERT);
		}
	}
	@Override
	public List<InpatientInfoNow> queryByMedicall(String medicalNo)throws Exception {
		return permissionDao.queryByMedicall(medicalNo);
	}
	@Override
	public String queryDeptType(String id)throws Exception {
		return permissionDao.queryDeptType(id);
	}
	@Override
	public List<SysDepartment> queryDeptByType(String state, String q)throws Exception {
		return permissionDao.queryDeptByType(state,q);
	}
	@Override
	public Patient queryPatientInfo(String mid)throws Exception {
		return permissionDao.queryPatientInfo(mid);
	}
	
	@Override
	public List<SysEmployee> queryLoginUserDept(String departmentId)throws Exception {
		return permissionDao.queryLoginUserDept(departmentId);
	}
	@Override
	public int getQueryListByIdCount(String inno)throws Exception {
		return permissionDao.getQueryListByIdCount(inno);
	}
	@Override
	public List<SysEmployee> employeeCombobox(String departmentId, String q)throws Exception {
		return permissionDao.employeeCombobox(departmentId,q);
	}
}
