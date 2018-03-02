package cn.honry.oa.extend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.oa.extend.dao.ExtendDao;
import cn.honry.oa.extend.service.ExtendService;
import cn.honry.oa.extend.vo.RecordVo;

@Service("extendService")
@Transactional
@SuppressWarnings({ "all" })
public class ExtendServiceImpl implements ExtendService{

	@Autowired
	@Qualifier(value = "extendDao")
	private ExtendDao extendDao;

	@Override
	public List<RecordVo> queryLeaveComplete(String type,String topFlow) {
		return extendDao.queryLeaveComplete(type,topFlow);
	}

	@Override
	public OaBpmProcess getProcessbyBusinessKeyOrId(String id) {
		OaBpmProcess process = extendDao.getProcessbyBusinessKey(id);
		if(process!=null){
			return process;
		}
		return extendDao.getProcessbyId(id);
	}

	@Override
	public OaKVRecord getRecordByBusinessKeyAndFlowCode(String topFlow,String leaveCode) {
		return extendDao.getRecordByBusinessKeyAndFlowCode(topFlow,leaveCode);
	}

	@Override
	public void saveRecord(OaKVRecord record) {
		extendDao.saveRecord(record);
	}

	@Override
	public OaKVRecord getRecordByBusinessKey(String businessKey) {
		return extendDao.getRecordByBusinessKey(businessKey);
	}

}
