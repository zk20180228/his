package cn.honry.inpatient.diagnose.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDiagnose;
import cn.honry.base.bean.model.BusinessDiagnoseMedicare;
import cn.honry.inpatient.diagnose.dao.DiagnoseDAO;
import cn.honry.inpatient.diagnose.dao.DiagnoseMedicareDAO;
import cn.honry.inpatient.diagnose.service.DiagnoseMedicareService;
import cn.honry.utils.ShiroSessionUtils;
@Service("diagnoseMedicareService")
@Transactional
@SuppressWarnings({ "all" })
public class DiagnoseMedicareServiceImpl implements DiagnoseMedicareService{
	
	@Autowired
	@Qualifier(value = "diagnoseMedicareDAO")
	private DiagnoseMedicareDAO diagnoseMedicareDAO;
	@Autowired
	@Qualifier(value = "diagnoseDAO")
	private DiagnoseDAO diagnoseDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public BusinessDiagnoseMedicare get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(BusinessDiagnoseMedicare entity){
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
	
		
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser(userId);
			entity.setCreateDept(deptId);
			entity.setCreateTime(new Date());
			diagnoseMedicareDAO.save(entity);
		}else{
			String id=entity.getId();
			BusinessDiagnoseMedicare entity1=diagnoseMedicareDAO.getval(id);
			BusinessDiagnose entity2=null;
			if(null==entity1.getId()){
				entity2= diagnoseDAO.getval(id);
				entity2.setDel_flg(1);
				diagnoseDAO.save(entity2);
				id=null;
			}
			entity1.setId(id);
			entity1.setInpatientNo(entity.getInpatientNo());
			entity1.setDiagKind(entity.getDiagKind());
			entity1.setIcdCode(entity.getIcdCode());
			entity1.setDiagName(entity.getDiagName());
			entity1.setDoctName(entity.getDoctName());
			entity1.setDiagDate(entity.getDiagDate());
			entity1.setMainFlay(entity.getMainFlay());
			if(null!=entity2){
				entity1.setCreateDept(entity2.getCreateDept());
				entity1.setCreateTime(entity2.getCreateTime());
				entity1.setCreateUser(entity2.getCreateUser());
			}
			entity1.setDel_flg(0);
			diagnoseMedicareDAO.save(entity1);
		}
		
		
	}


}
