package cn.honry.oa.repository.basedata.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RepositoryBaseData;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.repository.basedata.dao.RepositoryBaseDataDao;
import cn.honry.oa.repository.basedata.service.ReposirotyBaseDataService;
import cn.honry.utils.ShiroSessionUtils;
@Transactional
@SuppressWarnings({ "all" })
@Service("reposirotyBaseDataService")
public class ReposirotyBaseDataServiceImpl implements ReposirotyBaseDataService {

	@Autowired
	@Qualifier("repositoryBaseDataDao")
	private RepositoryBaseDataDao repositoryBaseDataDao;
	@Override
	public Map<String,String> saveBaseData(RepositoryBaseData data) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("resCode", "success");
		String createDept = "";
		SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		String createUser = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(department!=null){
			createDept = department.getDeptCode();
		}
		if (StringUtils.isBlank(data.getId())) {
			data.setCreateDept(createDept);
			data.setCreateTime(new Date());
			data.setCreateUser(createUser);
			data.setId(null);
			data.setUpdateTime(new Date());
			data.setUpdateUser(createUser);
			repositoryBaseDataDao.save(data);
			map.put("resMsg", "保存成功!");
		}else{
			RepositoryBaseData baseData = repositoryBaseDataDao.get(data.getId());
			baseData.setInterpretation(data.getInterpretation());
			baseData.setRemark(data.getRemark());
			baseData.setTerm(data.getTerm());
			baseData.setUpdateTime(new Date());
			baseData.setUpdateUser(createUser);
			repositoryBaseDataDao.update(baseData);
			map.put("resMsg", "修改成功!");
		}
		return map;
	}

	@Override
	public void delBaseData(String ids) {
			String[] id = ids.split(",");
			for (String string : id) {
				RepositoryBaseData data = repositoryBaseDataDao.get(string);
				data.setDel_flg(1);
				data.setDeleteTime(new Date());
				data.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				repositoryBaseDataDao.update(data);
			}
	}

	@Override
	public List<RepositoryBaseData> queryBaseData(String name, String page,
			String rows) {
		return repositoryBaseDataDao.getBaseData(name, page, rows);
	}

	@Override
	public int queryBaseDataTotal(String name) {
		return repositoryBaseDataDao.getBaseDataTotal(name);
	}

	@Override
	public RepositoryBaseData getByid(String id) {
		return repositoryBaseDataDao.get(id);
	}

}
