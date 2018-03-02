package cn.honry.oa.extend.dao;

import java.util.List;

import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.oa.extend.vo.RecordVo;

@SuppressWarnings({"all"})
public interface ExtendDao {
	
	public List<RecordVo> queryLeaveComplete(String type,String topFlow);

	public OaBpmProcess getProcessbyBusinessKey(String id);

	public OaKVRecord getRecordByBusinessKeyAndFlowCode(String topFlow,String leaveCode);

	public void saveRecord(OaKVRecord record);

	public OaKVRecord getRecordByBusinessKey(String businessKey);

	public OaBpmProcess getProcessbyId(String id);

}
