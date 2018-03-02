package cn.honry.oa.hospitalFileManager.dao;

import java.util.List;

import cn.honry.base.bean.model.HospitalFileManager;

public interface FileManageDao {

	/* 获取档案表中数据 */
	List<HospitalFileManager> getList(HospitalFileManager hospitalFileManager,
			String page, String rows);

	/* 保存档案表中数据 */
	void insertFile(HospitalFileManager hospitalFileManager);

	/* 删除档案表中数据 */
	void deleteFile(String id);

	/* 回显修改中的数据 */
	HospitalFileManager getListFile(String id);

	/* 修改表中的数据 */
	void updateFile(HospitalFileManager hospitalFileManager);

	

}
