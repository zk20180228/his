package cn.honry.oa.documentManage.service;

import java.util.ArrayList;
import java.util.List;

import cn.honry.base.bean.model.DocManage;
import cn.honry.base.bean.model.SysDepartment;

public interface DocumentManageService {

	/* 获取文档管理表中所有的数据*/
	List<DocManage> queryDcoManage(DocManage docManage1, String rows, String page);

	/* 删除文档中的数据*/
	void deleteDcoManage(String id);

	/* 修改文档中的数据*/
	void updateDcoManage(DocManage docManage);

	/* 添加文档中的数据*/
	void insertDocument(DocManage docManage);

	/* 获取文档管理表中回显的数据*/
	DocManage getListDocument(String id);

	/* 根据文档名称查询数据*/
	ArrayList<DocManage> getListByName(String documentName);

	/* 根据科室查询数据*/
	ArrayList<DocManage> getListByDeptName(String deptCode);

	/* 根据上传人查询数据*/
	ArrayList<DocManage> getListByUserName(String queryName);

	
}
