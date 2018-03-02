package cn.honry.oa.documentManage.service.Imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.DocManage;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.documentManage.dao.DocumentManageDao;
import cn.honry.oa.documentManage.service.DocumentManageService;

@Service("documentManageService")
public class DocumentManageServiceImp implements DocumentManageService{

	@Autowired
	@Qualifier(value = "documentManageDao")
	private DocumentManageDao documentManageDao;

	/* 获取文档管理表中所有的数据*/
	@Override
	public List<DocManage> queryDcoManage(DocManage docManage1, String rows, String page) {
		// TODO Auto-generated method stub
		return documentManageDao.queryDcoManage(docManage1,rows, page);
	}

	/* 删除文档中的数据*/
	@Override
	public void deleteDcoManage(String ids) {
		// TODO Auto-generated method stub
		String[] id = ids.split(",");
		for (String string : id) {
			documentManageDao.deleteDcoManage(string);
		}
	}

	/* 修改文档中的数据*/
	@Override
	public void updateDcoManage(DocManage docManage) {
		// TODO Auto-generated method stub
		documentManageDao.updateDcoManage(docManage);
	}

	/* 添加文档中的数据*/
	@Override
	public void insertDocument(DocManage docManage) {
		// TODO Auto-generated method stub
		documentManageDao.insertDocument(docManage);
	}

	/* 获取文档管理表中回显的数据*/
	@Override
	public DocManage getListDocument(String id) {
		// TODO Auto-generated method stub
		return documentManageDao.getListDocument(id);
	}

	/* 根据文档名称查询数据*/
	@Override
	public ArrayList<DocManage> getListByName(String documentName) {
		// TODO Auto-generated method stub
		return documentManageDao.getListByName(documentName);
	}

	/* 根据科室查询数据*/
	@Override
	public ArrayList<DocManage> getListByDeptName(String deptCode) {
		// TODO Auto-generated method stub
		return documentManageDao.getListByDeptName(deptCode);
	}

	/* 根据科室查询数据*/
	@Override
	public ArrayList<DocManage> getListByUserName(String queryName) {
		// TODO Auto-generated method stub
		return documentManageDao.getListByUserName(queryName);
	}


}
