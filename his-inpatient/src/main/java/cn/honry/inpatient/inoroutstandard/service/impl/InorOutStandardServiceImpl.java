package cn.honry.inpatient.inoroutstandard.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InoroutStandardDetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inpatient.inoroutstandard.dao.InoroutStandardDao;
import cn.honry.inpatient.inoroutstandard.dao.InoroutStandardDetialDao;
import cn.honry.inpatient.inoroutstandard.service.InorOutStandardService;
import cn.honry.inpatient.inoroutstandard.vo.StandardVO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("inorOutStandardService")
@Transactional
@SuppressWarnings({"all"})
public class InorOutStandardServiceImpl implements
		InorOutStandardService {
	@Autowired
	@Qualifier("inoroutStandardDao")
	private InoroutStandardDao inoroutStandardDao;
	@Autowired
	@Qualifier("inoroutStandardDetialDao")
	private InoroutStandardDetialDao inoroutStandardDetialDao;
	@Override
	public InoroutStandard getStandard(String id) {
		return inoroutStandardDao.get(id);
	}
	@Override
	public InoroutStandardDetail getStandardDetial(String id) {
		return inoroutStandardDetialDao.get(id);
	}
	@Override
	public void saveStandard(InoroutStandard arg0) {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptCode = "";
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(department != null){
			deptCode = department.getDeptCode();
		}
		if(arg0!=null && StringUtils.isNotBlank(arg0.getId())){//更新
			InoroutStandard standard = inoroutStandardDao.get(arg0.getId());
			standard.setCustomCode(arg0.getCustomCode());
			standard.setInputCode(arg0.getInputCode());
			standard.setInputCodeWb(inoroutStandardDao.getSpellCode(arg0.getStandName()));
			standard.setStandCode(arg0.getStandCode());
			standard.setStandName(arg0.getStandName());
			standard.setStandVersionNo(arg0.getStandVersionNo());
			standard.setUpdateTime(new Date());
			standard.setUpdateUser(account);
			standard.setStop_flg(arg0.getStop_flg());
			inoroutStandardDao.update(standard);
		}else{
			arg0.setId(null);
			arg0.setInputCodeWb(inoroutStandardDao.getSpellCode(arg0.getStandName()));
			arg0.setCreateDept(deptCode);
			arg0.setCreateTime(new Date());
			arg0.setCreateUser(account);
			arg0.setHospitalId(HisParameters.CURRENTHOSPITALID);
			arg0.setAreaCode(HisParameters.CURRENTHOSPITALCODE);
			inoroutStandardDao.save(arg0);
		}
		
	}
	@Override
	public void saveStandardDetial(InoroutStandardDetail detail) {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptCode = "";
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(department != null){
			deptCode = department.getDeptCode();
		}
		if(detail != null && StringUtils.isNotBlank(detail.getId())){
			InoroutStandardDetail standardDetail = inoroutStandardDetialDao.get(detail.getId());
			standardDetail.setFlag(detail.getFlag());
			standardDetail.setAssessName(detail.getAssessName());
			standardDetail.setAssessValue(detail.getAssessValue());
			standardDetail.setUpdateTime(new Date());
			standardDetail.setUpdateUser(account);
			inoroutStandardDetialDao.update(standardDetail);
		}else{
			detail.setId(null);
			detail.setCreateDept(deptCode);
			detail.setCreateTime(new Date());
			detail.setCreateUser(account);
			detail.setUpdateTime(new Date());
			detail.setUpdateUser(account);
			inoroutStandardDetialDao.save(detail);
		}
		
	}
	@Override
	public List<InoroutStandard> getStandardList(String code) {
		return inoroutStandardDao.getStandardList(code);
	}
	@Override
	public List<InoroutStandardDetail> getStandardDetialList(String code,
			String versionNO) {
		return inoroutStandardDetialDao.getStandardDetialList(code, versionNO);
	}
	@Override
	public TreeJson getSandardTree() {
		TreeJson rootTree = new TreeJson();
		List<TreeJson> childTree = new ArrayList<TreeJson>();
		List<StandardVO> list = inoroutStandardDao.getAllStandard();
		TreeJson tree = null;
		for (StandardVO ino : list) {
			tree = new TreeJson();
			tree.setId(ino.getStandCode());
			tree.setText(ino.getStandName());
			childTree.add(tree);
		}
		rootTree.setChildren(childTree);
		rootTree.setText("标准");
		rootTree.setId("root");
		return rootTree;
	}
	@Override
	public void delStandard(String ids) {
		if(StringUtils.isNotBlank(ids)){
			String[] strings = ids.split(",");
			for (String string : strings) {
				InoroutStandard standard = inoroutStandardDao.get(string);
				standard.setDeleteTime(new Date());
				standard.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				standard.setDel_flg(1);
				inoroutStandardDao.update(standard);
			}
		}
		
	}
	@Override
	public void delStandardDetail(String ids) {
		if(StringUtils.isNotBlank(ids)){
			String[] strings = ids.split(",");
			for (String string : strings) {
				InoroutStandardDetail detail = inoroutStandardDetialDao.get(string);
				detail.setDel_flg(1);
				detail.setDeleteTime(new Date());
				detail.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				inoroutStandardDetialDao.save(detail);
			}
		}
		
	}
	
	
}
