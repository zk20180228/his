package cn.honry.mobile.ofBatchPush.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.mobile.ofBatchPush.dao.OfBatchPushDao;
import cn.honry.mobile.ofBatchPush.service.OfBatchPushService;

@Service("ofBatchPushService")
public class OfBatchPushServiceImpl implements OfBatchPushService{

	@Resource
	private OfBatchPushDao ofBatchPushDao;

	@Override
	public List<OFBatchPush> getOfBatchPushList(String rows, String page,
			String queryName) {
		return ofBatchPushDao.getOfBatchPushList(rows, page, queryName) ;
	}

	@Override
	public Integer getOfBatchPushCount(String queryName) {
		return ofBatchPushDao.getOfBatchPushCount(queryName) ;
	}

	@Override
	public void delOfBatchPush(String ids) {
		ofBatchPushDao.delOfBatchPush(ids);
		
	}
}
