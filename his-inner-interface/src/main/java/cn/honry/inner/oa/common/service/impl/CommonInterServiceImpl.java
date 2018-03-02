package cn.honry.inner.oa.common.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.OaCommon;
import cn.honry.inner.oa.common.dao.CommonInterDao;
import cn.honry.inner.oa.common.service.CommonInterService;

@Service("commonInterService")
public class CommonInterServiceImpl implements CommonInterService {
	
	@Autowired
	@Qualifier(value = "commonInterDao")
	private CommonInterDao commonInterDao; 

	@Override
	public void saveOrUpddateCommon(OaCommon oaCommon) {
		if (StringUtils.isBlank(oaCommon.getId())) {
			oaCommon.setId(null);
		}
		commonInterDao.save(oaCommon);
	}

	@Override
	public List<OaCommon> findMyCommon(String account , String tableCode) throws Exception {
		return commonInterDao.findMyCommon(account , tableCode);
	}

	@Override
	public void delCommon(String ids) throws Exception {
		String[] id = ids.split(",");
		for (String s : id) {
			commonInterDao.delCommonById(s);
		}
		
	}

	@Override
	public OaCommon findById(String id) throws Exception {
		return commonInterDao.findById(id);
	}

	@Override
	public List<OaCommon> findFrom(String account) {
		return commonInterDao.findFrom(account);
	}
	

}
