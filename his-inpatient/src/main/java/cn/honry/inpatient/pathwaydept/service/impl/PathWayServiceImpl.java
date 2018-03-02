package cn.honry.inpatient.pathwaydept.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.CpDept;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inpatient.pathwaydept.dao.PathWayDao;
import cn.honry.inpatient.pathwaydept.service.PathWayService;
import cn.honry.utils.ShiroSessionUtils;
@Service("pathWayService")
@Transactional
@SuppressWarnings({"all"})
public class PathWayServiceImpl implements PathWayService {
	@Autowired
	@Qualifier("pathWayDao")
	private PathWayDao pathWayDao;
	
	@Override
	public CpDept getByid(String id) {
		return pathWayDao.get(id);
	}

	@Override
	public List<CpDept> getcpDept(String page, String rows, String deptCode,
			String name) {
		return pathWayDao.getcpDept(page, rows, deptCode, name);
	}

	@Override
	public int getcpDeptTotal(String deptCode, String name) {
		return pathWayDao.getcpDeptTotal(deptCode, name);
	}

	@Override
	public void saveOrUpdateCpDept(CpDept info) {
		String deptCode = "";
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(department!=null){
			deptCode = department.getDeptCode();
		}
		if(StringUtils.isBlank(info.getId())){
			info.setId(null);
			info.setCreateDept(deptCode);
			info.setCreateTime(new Date());
			info.setCreateUser(account);
			info.setUpdateTime(new Date());
			info.setUpdateUser(account);
			pathWayDao.save(info);
		}else{
			CpDept cpDept = pathWayDao.get(info.getId());
			cpDept.setCustomCode(info.getCustomCode());
			cpDept.setDeptCode(info.getDeptCode());
			cpDept.setDeptName(info.getDeptName());
			cpDept.setInputCode(info.getInputCode());
			cpDept.setInputCodeWB(info.getInputCodeWB());
			cpDept.setVersionNo(info.getVersionNo());
			pathWayDao.update(cpDept);
		}

	}

	@Override
	public List<CpWay> getCpWay() {
		return pathWayDao.getCpWay();
	}

}
