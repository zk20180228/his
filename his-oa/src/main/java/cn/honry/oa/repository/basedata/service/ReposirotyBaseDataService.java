package cn.honry.oa.repository.basedata.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RepositoryBaseData;

public interface ReposirotyBaseDataService {
	Map<String,String> saveBaseData(RepositoryBaseData data);
	void delBaseData(String ids);
	List<RepositoryBaseData> queryBaseData(String name ,String page,String rows);
	int queryBaseDataTotal(String name );
	RepositoryBaseData getByid(String id);
}
