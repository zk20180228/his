package cn.honry.outpatient.cpway.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.outpatient.cpway.dao.CPWayDao;
import cn.honry.outpatient.cpway.service.CPWayService;
import cn.honry.outpatient.cpway.vo.CPWayVo;
import cn.honry.outpatient.cpway.vo.ComboxVo;
import cn.honry.outpatient.cpway.vo.PatientVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("cPWayService")
public class CPWayServiceImpl  implements CPWayService{

	@Resource
	private CPWayDao cPWayDao; 
	public void setcPWayDao(CPWayDao cPWayDao) {
		this.cPWayDao = cPWayDao;
	}
	
	
	@Override
	public List<ComboxVo> inpatientDeptTree(String deptName) {
		
		return cPWayDao.inpatientDeptTree(deptName);
	}

	@Override
	public List<ComboxVo> patientList(String id) {
		
		return cPWayDao.patientList(id);
	}

	@Override
	public List<CPWayVo> cPWayPatientList(String page, String rows, String id) {
		
		return cPWayDao.cPWayPatientList(page, rows, id);
	}

	@Override
	public Integer cPWayPatientCount(String id) {
		
		return cPWayDao.cPWayPatientCount(id);
	}

	@Override
	public void addCPWayPatient(CPWayVo cPWayVo) {
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();

		String id=UUID.randomUUID().toString().replace("-", "");
		cPWayVo.setId(id);
		String sqPrefix=DateUtils.formatDateY_M_D(new Date()).replace("-", "");
		cPWayVo.setApply_no(sqPrefix+cPWayDao.getApply_no());//t_path_apply_seq由这个序列号生成
		cPWayVo.setApply_status("0");
		cPWayVo.setApply_doct_code(employee.getJobNo());
		String now=DateUtils.formatDateY_M_D_H_M_S(new Date());
		cPWayVo.setApply_date(now);
		cPWayVo.setUpdateTime(now);
		cPWayVo.setUpdateUser(employee.getJobNo());
		cPWayVo.setHospital_id("1");
		
		cPWayVo.setArea_code(cPWayDao.getArea_code(cPWayVo.getApply_code()));
		cPWayVo.setCreateUser(employee.getJobNo());
		cPWayVo.setCreateTime(now);;
		
		
		cPWayDao.addCPWayPatient(cPWayVo);
	}

	@Override
	public Integer checkIsAdd(String inpatient_no) {
		
		return cPWayDao.checkIsAdd(inpatient_no);
	}

	@Override
	public PatientVo findPatient(String inpatient_no) {
		
		return cPWayDao.findPatient(inpatient_no);
	}

	@Override
	public List<ComboxVo> cPWList() {
		
		return cPWayDao.cPWList();
	}

	@Override
	public List<ComboxVo> findVersionList(String cPWId) {
		
		return cPWayDao.findVersionList(cPWId);
	}

	@Override
	public void approveApply(String cPWAppId, String apply_status) {
		
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		CPWayVo vo = new CPWayVo();
		vo.setApply_status(apply_status);
		if(employee!=null){
			vo.setApproval_user(employee.getJobNo());
			vo.setUpdateUser(employee.getJobNo());
		}
		String now=DateUtils.formatDateY_M_D_H_M_S(new Date());
		vo.setApproval_date(now);
		vo.setExecute_date(now);
		vo.setUpdateTime(now);
		
		cPWayDao.approveApply(cPWAppId, vo);
	}

	@Override
	public void delCPWayPatient(String cPWAppId) {
		
		cPWayDao.delCPWayPatient(cPWAppId);
	}


}
