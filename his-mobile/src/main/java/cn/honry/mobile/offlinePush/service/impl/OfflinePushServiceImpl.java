package cn.honry.mobile.offlinePush.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MOfofflinepush;
import cn.honry.mobile.offlinePush.dao.OfflinePushDao;
import cn.honry.mobile.offlinePush.service.OfflinePushService;

@Service("offlinePushService")
public class OfflinePushServiceImpl implements OfflinePushService{

	@Resource
	private OfflinePushDao offlinePushDao;

	@Override
	public List<MOfofflinepush> getOfflineMesList(String rows, String page,
			String queryName) {
		return offlinePushDao.getOfflineMesList(rows, page, queryName);
	}

	@Override
	public Integer getOfflineMesCount(String queryName) {
		return offlinePushDao.getOfflineMesCount(queryName);
	}

	@Override
	public void delOfflineMes(String ids) {
		offlinePushDao.delOfflineMes(ids);
		
	}
}
