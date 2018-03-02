package cn.honry.oa.hospitalFileManager.service.Imp;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.HospitalFileManager;
import cn.honry.oa.hospitalFileManager.dao.FileManageDao;
import cn.honry.oa.hospitalFileManager.service.FileManageService;
import cn.honry.utils.ShiroSessionUtils;

@Service("fileManageService")
public class FileServiceImp implements FileManageService{

	@Autowired
	@Qualifier(value = "fileManageDao")
	private FileManageDao fileManageDao;

	/* 获取档案表中数据 */
	@Override
	public List<HospitalFileManager> getList(
			HospitalFileManager hospitalFileManager, String page, String rows) {
		return fileManageDao.getList(hospitalFileManager, page,  rows);
	}

	/* 保存档案表中数据 */
	@Override
	public void insertFile(HospitalFileManager hospitalFileManager) {
		// TODO Auto-generated method stub
		hospitalFileManager.setUpdateTime(new Date());
		hospitalFileManager.setCreateTime(new Date());
		hospitalFileManager.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
		hospitalFileManager.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
		hospitalFileManager.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
		hospitalFileManager.setDel_flg(0);
		hospitalFileManager.setStop_flg(0);
		fileManageDao.insertFile(hospitalFileManager);
	}

	/* 删除档案表中数据 */
	@Override
	public void deleteFile(String ids) {
		String[] s = ids.split(",");
		for (String id : s) {
			fileManageDao.deleteFile(id);
		}
	}

	/* 回显修改中的数据 */
	@Override
	public HospitalFileManager getListFile(String id) {
		// TODO Auto-generated method stub
		return fileManageDao.getListFile(id);
	}

	/* 修改表中的数据 */
	@Override
	public void updateFile(HospitalFileManager hospitalFileManager) {
		// TODO Auto-generated method stub
		fileManageDao.updateFile(hospitalFileManager);
	}

	

}
