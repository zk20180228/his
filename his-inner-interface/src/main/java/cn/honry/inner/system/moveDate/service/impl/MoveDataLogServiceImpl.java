package cn.honry.inner.system.moveDate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.inner.system.moveDate.dao.MoveDataLogDAO;
import cn.honry.inner.system.moveDate.service.MoveDataLogService;

/** 数据迁移日志ServiceImpl  
* @ClassName: MoveDataLogServiceImpl 
* @Description: 数据迁移日志ServiceImpl  
* @author dtl
* @date 2016年12月9日
*  
*/
@Service("moveDataLogService")
@Transactional
@SuppressWarnings({ "all" })
public class MoveDataLogServiceImpl implements MoveDataLogService{

	@Autowired
	@Qualifier(value = "moveDataLogDAO")
	private MoveDataLogDAO moveDataLogDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public MoveDataLog get(String arg0) {
		return moveDataLogDAO.get(arg0);
	}

	@Override
	public void saveOrUpdate(MoveDataLog arg0) {
		moveDataLogDAO.save(arg0);
	}

	@Override
	public MoveDataLog queryMoveDataLog(Integer optType, Integer dateType,
			String tableName, String dataDate) {
		return moveDataLogDAO.queryMoveDataLog(optType, dateType, tableName, dataDate);
	}
	
	
		
}

