package cn.honry.oa.repository.categ.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RepositoryCateg;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.repository.categ.dao.RepositoryCategDao;
import cn.honry.oa.repository.categ.service.RepositoryCategService;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Transactional
@Service("repositoryCategService")
public class RepositoryCategServiceImpl implements RepositoryCategService {
	
	@Autowired
	@Qualifier("repositoryCategDao")
	private RepositoryCategDao repositoryCategDao;
	@Override
	public void saveCateg(RepositoryCateg cate) {
		cate.setId(null);
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(department!=null){
			cate.setCreateDept(department.getDeptCode());
		}
		cate.setCreateTime(new Date());
		cate.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		cate.setDel_flg(0);
		cate.setUpdateTime(new Date());
		cate.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		repositoryCategDao.save(cate);
	}

	@Override
	public void updateCateg(RepositoryCateg cate) {
		RepositoryCateg categ = repositoryCategDao.get(cate.getId());
		categ.setDiseaseCode(cate.getDiseaseCode());
		categ.setDiseaseName(cate.getDiseaseName());
		categ.setCode(cate.getCode());
		categ.setName(cate.getName());
		categ.setUpdateTime(new Date());
		categ.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		repositoryCategDao.update(categ);
	}

	@Override
	public List<RepositoryCateg> getCateg(String deptCode, String page,
			String rows, String name,String nodeType) {
		return repositoryCategDao.getCateg(deptCode, page, rows, name,nodeType);
	}

	@Override
	public int getCategTotal(String deptCode, String name,String nodeType) {
		return repositoryCategDao.getCategTotal(deptCode, name,nodeType);
	}

	@Override
	public void delCate(String cateid) {
		RepositoryCateg categ = repositoryCategDao.get(cateid);
		categ.setDel_flg(1);
		categ.setDeleteTime(new Date());
		categ.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		repositoryCategDao.update(categ);
	}

	@Override
	public RepositoryCateg get(String id) {
		return repositoryCategDao.get(id);
	}

	@Override
	public Map<String, String> checkCode(String code) {
		Map<String, String> map = new HashMap<String, String>();
		RepositoryCateg categ = repositoryCategDao.checkCode(code);
		if(categ!=null&&StringUtils.isNotBlank(categ.getId())){
			map.put("resCode", "error");
		}else{
			map.put("resCode", "success");
		}
		return map;
	}

	@Override
	public List<TreeJson> getCategTree() {
		List<TreeJson> treejson = new ArrayList<TreeJson>();
		Map<String ,List<RepositoryCateg>> map = new HashMap<String, List<RepositoryCateg>>();
		List<RepositoryCateg> allCate = repositoryCategDao.getAllCate();
		String key = "";
		for (RepositoryCateg cate : allCate) {
			key = cate.getDiseaseCode()+"_"+cate.getDiseaseName();
			if(map.containsKey(key)){
				List<RepositoryCateg> list = map.get(key);
				list.add(cate);
				map.put(key, list);
			}else{
				List<RepositoryCateg> list = new ArrayList<RepositoryCateg>();
				list.add(cate);
				map.put(key, list);
			}
		}
		for (Entry<String, List<RepositoryCateg>> m : map.entrySet()) {
			TreeJson treep = new TreeJson();
			String[] keys = m.getKey().split("_");
			treep.setId(keys[0]);
			treep.setText(keys[1]);
			treep.setNodeType("1");
			List<RepositoryCateg> value = m.getValue();
			List<TreeJson> treechil = new ArrayList<TreeJson>();
			for (RepositoryCateg cate : value) {
				TreeJson treec = new TreeJson();
				treec.setId(cate.getCode());
				treec.setText(cate.getName());
				treec.setNodeType("0");
				treechil.add(treec);
			}
			treep.setChildren(treechil);
			treejson.add(treep);
		}
		List<TreeJson> returnlist = new ArrayList<TreeJson>();
		TreeJson pat = new TreeJson();
		pat.setId("root");
		pat.setText("知识库分类");
		pat.setNodeType("root");
		pat.setChildren(treejson);
		returnlist.add(pat);
		return returnlist;
	}

}
