package cn.honry.mobile.ofoffline.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MOfoffline;
import cn.honry.mobile.ofoffline.dao.OfofflineDao;
import cn.honry.mobile.ofoffline.service.OfofflineService;

@Service("ofofflineService")
public class OfofflineServiceImpl implements OfofflineService{

	@Resource
	private OfofflineDao ofofflineDao;
	
	@Override
	public List<MOfoffline> getOfofflineList(String rows, String page,
			String queryName) {
		return ofofflineDao.getOfofflineList(rows, page,queryName);
	}

	@Override
	public Integer getOfofflineCount(String queryName) {
		return ofofflineDao.getOfofflineCount(queryName);
	}

	@Override
	public void delOfoffline(String ids) {
		ofofflineDao.delOfoffline(ids);
	}

}
