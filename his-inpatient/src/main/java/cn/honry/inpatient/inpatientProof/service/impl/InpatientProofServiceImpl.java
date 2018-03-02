package cn.honry.inpatient.inpatientProof.service.impl;

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

import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.outpatient.register.service.RegisterInfoInInterService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.inpatientProof.dao.InpatientProofDao;
import cn.honry.inpatient.inpatientProof.service.InpatientProofService;
import cn.honry.inpatient.inpatientProof.vo.PoorfBill;
import cn.honry.inpatient.inpatientProof.vo.proReg;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;


@Service("inpatientProofService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientProofServiceImpl implements InpatientProofService{
	
	
	@Autowired
	@Qualifier(value = "inpatientProofDAO")
	private InpatientProofDao inpatientProofDAO;
	@Autowired
	private EmployeeInInterService employeeService;  

	@Autowired
	private RegisterInfoInInterService registerInfoService;
	@Override
	public void removeUnused(String id) {
		
	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}
	@Override
	public InpatientProof get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientProof entity) {
		
	}


	public InpatientProof getProof(String id)  throws Exception {
		return inpatientProofDAO.getProof(id);
	}
	/**  
	 * @Description：  查询树
	 *@Author：wujiao
	 * @CreateDate：2015-6-25 上午3:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 上午3:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<TreeJson> findTree(String deptId,String type) throws Exception {
		List<TreeJson> treeDepar = new ArrayList<TreeJson>(); 
		List<SysEmployee> EmployeeList = employeeService.getEmployeeListByDeptId(deptId);
		if(EmployeeList!=null){
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			pTreeJson.setText("住院证明信息");
			treeDepar.add(pTreeJson);
			for(SysEmployee sysEmployee : EmployeeList){
				TreeJson treeJson = new TreeJson();
				treeJson.setId(sysEmployee.getId());
				treeJson.setText(sysEmployee.getName());
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid", "1");
				attributes.put("isNo","0");
				attributes.put("buttondz","0");
				treeJson.setAttributes(attributes);
				treeDepar.add(treeJson);
				List<RegisterInfo> registerInfoList = registerInfoService.getInfoByEmployeeId(sysEmployee.getId(),type);
				if(registerInfoList!=null){
					for(RegisterInfo RegisterInfo : registerInfoList){
						TreeJson treeChJson = new TreeJson();
						treeChJson.setId(RegisterInfo.getId());
						treeChJson.setText(RegisterInfo.getPatientId().getPatientName());
						Map<String,String> attributesCh = new HashMap<String, String>();
						attributesCh.put("pid",sysEmployee.getId());
						attributesCh.put("isNo","1");
						attributes.put("buttondz","1");
						treeChJson.setAttributes(attributesCh);
						treeDepar.add(treeChJson);
					}
				}
			}
		}
		return treeDepar;
	}
	/**  
	 *  
	 * @Description：  添加修改
	 *@Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrUpdatePreregister(InpatientProof inpatientProof) throws Exception {
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		inpatientProof.setAreaCode(inprePayDAO.getDeptArea(dept.getDeptCode()));//医院code
		inpatientProof.setHospitalId(HisParameters.CURRENTHOSPITALID);//院区
		if(StringUtils.isBlank(inpatientProof.getId())){//保存
			inpatientProof.setId(null);
			inpatientProof.setCreateTime(new Date());
			inpatientProof.setCreateUser(user.getAccount());
			inpatientProof.setCreateDept(dept.getDeptCode());
			inpatientProof.setDel_flg(0);
			if(inpatientProof.getStop_flg()==null){
				inpatientProof.setStop_flg(0);
				
			}
			OperationUtils.getInstance().conserve(null,"住院证明","INSERT INTO","T_INPATIENT_PROOF",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			inpatientProof.setUpdateUser("");
			inpatientProof.setUpdateUser(user.getAccount());
			inpatientProof.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(inpatientProof.getId(),"住院证明","UPDATE","T_INPATIENT_PROOF",OperationUtils.LOGACTIONUPDATE);

		}
				
		inpatientProofDAO.save(inpatientProof);
		
	}
	
	/**  
	 * @Description：  根据挂号记录ID查询患者信息
	 *@Author：tcj
	 * @CreateDate：2015-12-3
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	@Transactional(readOnly=true)
	public RegistrationNow queryMedicalrecordId(String medicalrecordId)  throws Exception {
		RegistrationNow registerInfo = inpatientProofDAO.queryMedicalrecordId(medicalrecordId);
		return registerInfo;
	}
	
	/**  
	 * @Description：  根据患者号查询患者信息和挂号信息
	 *@Author：tcj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<proReg> queryInfoListHis(String medicalrecordId) throws Exception {
		 String d= DateUtils.formatDateY_M_D(new Date());
		 Date now = DateUtils.parseDate(d, DateUtils.DATE_FORMAT);
		 List<proReg> lst = inpatientProofDAO.queryInfoListHis(medicalrecordId,now);
		 return lst;
	}

	/**  
	 * @Description：  查询全部住院证明
	 *@Author：zhenglin
	 * @CreateDate：2015-12-17
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	public List<InpatientProof> allProofInfo() throws Exception {
		
		return inpatientProofDAO.allProofInfo();
	}
	/**
	 * 根据病历号查询患者当日的挂号记录
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @return
	 */
	@Override
	public List<RegistrationNow> queryDateInfo(String medicalrecordId) throws Exception {
		return inpatientProofDAO.queryDateInfo(medicalrecordId);
	}
	/**
	 * 根据病历号查询患者当日的开证记录
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @return
	 */
	@Override
	public List<InpatientProof> queryProofDate(String medicalrecordId) throws Exception {
		String d= DateUtils.formatDateY_M_D(new Date());
		Date now = DateUtils.parseDate(d, DateUtils.DATE_FORMAT);
		return inpatientProofDAO.queryProofDate(medicalrecordId,now);
	}
	/**
	 * 查询病区
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	@Override
	public List<SysDepartment> querybingqu(String id,String q) throws Exception {
		return inpatientProofDAO.querybingqu(id,q);
	}

	@Override
	public String queryInpatientInfo(String zjhm) throws Exception {
		return inpatientProofDAO.queryInpatientInfo(zjhm);
	}

	@Override
	public List<Patient> queryByNumber(String medicalrecordId) throws Exception {
		return inpatientProofDAO.queryByNumber(medicalrecordId);
	}

	@Override
	public List<InpatientProof> searchProofByResinfo(String medicalrecordId,String regisNo) throws Exception {
		return inpatientProofDAO.searchProofByResinfo(medicalrecordId,regisNo);
	}

	@Override
	public List<SysDepartment> queryInHosDept(String param) throws Exception {
		return inpatientProofDAO.queryInHosDept(param);
	}

	@Override
	public String queryShoufeiState(String no) throws Exception {
		return inpatientProofDAO.queryShoufeiState(no);
	}

	@Override
	public String queryPatientStateBymz(String medicalrecordId) throws Exception {
		return inpatientProofDAO.queryPatientStateBymz(medicalrecordId);
	}
	
	@Override
	public List<SysDepartment> queryKeshi(String departmentCode, String q) throws Exception {
		return inpatientProofDAO.queryKeshi(departmentCode,q);
	}

	@Override
	public List<PoorfBill> queryProofByIds(String ids) throws Exception {
		return inpatientProofDAO.queryProofByIds(ids);
	}

}


