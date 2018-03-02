package cn.honry.inpatient.info.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.info.dao.InpatientBedInfoDAO;
import cn.honry.inpatient.info.service.InpatientBedInfoService;
import cn.honry.utils.ShiroSessionUtils;

@Service("inpatientBedInfoService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientBedInfoServiceImpl implements InpatientBedInfoService{
	@Autowired
	private InpatientBedInfoDAO inpatientBedInfoDAO;
	
	
	@Override
	public void removeUnused(String id) {
		inpatientBedInfoDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	}

	@Override
	public InpatientBedinfoNow get(String id)  {
		return inpatientBedInfoDAO.get(id);
	}

	@Override
	public void saveOrUpdate(InpatientBedinfoNow entity) {
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser("");
			entity.setCreateDept("");
			entity.setCreateTime(new Date());
		}else{
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
		}
		inpatientBedInfoDAO.save(entity);
		if(entity.getId()==null){
			OperationUtils.getInstance().conserve(null,"住院登记信息管理","INSERT INTO","T_INPATIENT_INFO",OperationUtils.LOGACTIONINSERT);
		}else{
			OperationUtils.getInstance().conserve(entity.getId(),"住院登记信息管理","UPDATE","T_INPATIENT_INFO",OperationUtils.LOGACTIONUPDATE);
		}
	}

	@Override
	public InpatientBedinfoNow getByBedId(String bedId) throws Exception {
		return inpatientBedInfoDAO.getByBedId(bedId);
	}

	@Override
	public List<InpatientBedinfoNow> list() throws Exception {
		return inpatientBedInfoDAO.list();
	}

	@Override
	public List<BusinessHospitalbed> bedlist() throws Exception {
		return inpatientBedInfoDAO.bedlist();
	}

	@Override
	public List<DepartmentContact> deplist() throws Exception {
		return inpatientBedInfoDAO.deplist();
	}

	@Override
	public List<BusinessContractunit> reglist() throws Exception {
		return inpatientBedInfoDAO.reglist();
	}

	@Override
	public List<SysEmployee> houseDoclist(String id) throws Exception {
		return inpatientBedInfoDAO.bedinfolist(id);
	}


}
