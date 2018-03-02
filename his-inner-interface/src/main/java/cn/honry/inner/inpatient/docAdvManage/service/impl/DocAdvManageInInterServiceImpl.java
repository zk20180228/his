package cn.honry.inner.inpatient.docAdvManage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.inner.inpatient.docAdvManage.dao.DocAdvManageInInterDAO;
import cn.honry.inner.inpatient.docAdvManage.service.DocAdvManageInInterService;
import cn.honry.inner.inpatient.docAdvManage.vo.ProInfoInInterVo;
@Service("docAdvManageInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class DocAdvManageInInterServiceImpl implements DocAdvManageInInterService{
	@Autowired
	@Qualifier(value = "docAdvManageInInterDAO")
	private DocAdvManageInInterDAO docAdvManageDAO;
	@Override
	public void removeUnused(String id) {
			
	}

	@Override
	public InpatientOrder get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientOrder entity) {
		
	}


	@Override
	public List<ProInfoInInterVo> querySysInfo(String name,String type,String sysTypeName,String id) {		
		List<ProInfoInInterVo> sysInfoList = docAdvManageDAO.querySysInfo(name,type,sysTypeName,id);
		return sysInfoList;
	}

	
}
