package cn.honry.statistics.bi.bistac.temporary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.bistac.temporary.dao.HistoryRecordsDAO;
import cn.honry.statistics.bi.bistac.temporary.service.HistoryRecordsService;
import cn.honry.statistics.bi.bistac.temporary.vo.HistoryRecordsInfoVo;

/**  
 *  
 * @className：InpatientInfoServiceImpl
 * @Description： 住院患者信息
 * @Author：gaotiantian
 * @CreateDate：2017-3-31 下午02:05:31  
 * @Modifier：gaotiantian
 * @ModifyDate：2017-3-31 下午02:05:31 
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@Service("HistoryRecordsService")
@Transactional
@SuppressWarnings({ "all" })
public class HistoryRecordsServiceImpl implements HistoryRecordsService {
	
	@Autowired
	@Qualifier(value = "HistoryRecordsDAO")
	private HistoryRecordsDAO HistoryRecordsDAO;

	@Override
	public List<HistoryRecordsInfoVo> getHistoryRecordsInfo(String clinicNo) {
		// TODO Auto-generated method stub
		return HistoryRecordsDAO.getHistoryRecordsInfo(clinicNo);
	}

}

