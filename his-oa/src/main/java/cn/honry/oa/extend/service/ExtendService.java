package cn.honry.oa.extend.service;

import java.util.List;

import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.oa.extend.vo.RecordVo;

@SuppressWarnings({"all"})
public interface ExtendService {

	public List<RecordVo> queryLeaveComplete(String type,String topFlow);

	public OaBpmProcess getProcessbyBusinessKeyOrId(String id);

	public OaKVRecord getRecordByBusinessKeyAndFlowCode(String topFlow,String leaveCode);

	public void saveRecord(OaKVRecord record);

	public OaKVRecord getRecordByBusinessKey(String businessKey);

}
