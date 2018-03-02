package cn.honry.oa.repository.info.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RepositoryInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.repository.info.dao.RepositoryInfoDao;
import cn.honry.oa.repository.info.service.RepositoryInfoService;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.UUIDGenerator;

@Transactional
@Service("repositoryInfoService")
public class RepositoryInfoServiceImpl implements RepositoryInfoService {
	
	@Autowired
	@Qualifier("repositoryInfoDao")
	private RepositoryInfoDao repositoryInfoDao;

	@Override
	public List<RepositoryInfo> getPublicInfo(String code, String name,
			String page, String rows,String nodeType) {
		return repositoryInfoDao.getRepositoryInfo(code, name, 1, 0, page, rows,0,nodeType);
	}
	
	@Override
	public int getPublicInfoTotal(String code, String name,String nodeType) {
		return repositoryInfoDao.getRepositoryInfoTotal(code, name, 1, 0,0,nodeType);
	}
	
	@Override
	public List<RepositoryInfo> getPersonalInfo(String code, String name,
			String page, String rows) {
		return repositoryInfoDao.getRepositoryInfo(code, name, 0, 0, page, rows,1,null);
	}

	@Override
	public int getPersonalInfoTotal(String code, String name) {
		return repositoryInfoDao.getRepositoryInfoTotal(code, name, 0, 0,1,null);
	}
	
	@Override
	public List<RepositoryInfo> getPersonalCollectionInfo(String code,
			String name, String page, String rows) {
		return repositoryInfoDao.getRepositoryInfo(code, name, 0, 1, page, rows,0,null);
	}
	
	@Override
	public int getPersonalCollectionInfoTotal(String code, String name) {
		return repositoryInfoDao.getRepositoryInfoTotal(code, name, 0, 1,0,null);
	}

	@Override
	public void savePersonalCollection(String infoid) {
		RepositoryInfo info = repositoryInfoDao.get(infoid);
		RepositoryInfo newinfo = new RepositoryInfo();
		try {
			BeanUtils.copyProperties(info, newinfo);
			newinfo.setId(null);
			newinfo.setIsOvert(0);
			newinfo.setIsCollect(1);
			newinfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
			if(department != null){
				newinfo.setCreateDept(department.getDeptCode());
			}
			newinfo.setCreateTime(new Date());
			newinfo.setUpdateTime(new Date());
			newinfo.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			repositoryInfoDao.save(newinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveOrUpdateInfo(RepositoryInfo info) {
		SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String createDept = "";
		if(department != null){
			createDept = department.getDeptCode();
		}
		if(StringUtils.isBlank(info.getId())){
			String contentid = UUIDGenerator.getUUID();
			repositoryInfoDao.insertIntoMongo(info.getContentHtml(), contentid);
			info.setContent(contentid);
			info.setCreateDept(createDept);
			info.setCreateTime(new Date());
			info.setCreateUser(account);
			info.setUpdateTime(new Date());
			info.setUpdateUser(account);
			info.setId(null);
			repositoryInfoDao.save(info);
		}else{
			RepositoryInfo repositoryInfo = repositoryInfoDao.get(info.getId());
			repositoryInfo.setUpdateTime(new Date());
			repositoryInfo.setUpdateUser(account);
			repositoryInfo.setAttach(info.getAttach());
			repositoryInfo.setAttachName(info.getAttachName());
			repositoryInfo.setCategCode(info.getCategCode());
			repositoryInfo.setCategName(info.getCategName());
			repositoryInfo.setKeyWord(info.getKeyWord());
			repositoryInfo.setName(info.getName());
			repositoryInfo.setOrigin(info.getOrigin());
			repositoryInfo.setIsOvert(info.getIsOvert());
			repositoryInfo.setRemark(info.getRemark());
			repositoryInfo.setPubFlg(info.getPubFlg());
			repositoryInfoDao.deleteFromMongo(info.getContent());
			repositoryInfoDao.insertIntoMongo(info.getContentHtml(), info.getContent());
			repositoryInfoDao.update(repositoryInfo);
		}
		
	}

	@Override
	public RepositoryInfo getInfoByid(String id) {
		RepositoryInfo info = repositoryInfoDao.get(id);
		String content = repositoryInfoDao.getContentFromMongo(info.getContent());
		info.setContentHtml(content);
		return info;
	}

	@Override
	public void delInfo(String infoid) {
		String[] split = infoid.split(",");
		for (String id : split) {
			RepositoryInfo info = repositoryInfoDao.get(id);
			info.setDel_flg(1);
			info.setDeleteTime(new Date());
			info.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			repositoryInfoDao.save(info);
		}
	}

	@Override
	public void updateViews(String infoid) {
		repositoryInfoDao.updateViews(infoid);
	}
	
}
