package cn.honry.oa.commonLg.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.OaCommon;
import cn.honry.oa.commonLg.dao.CommonLgDao;
import cn.honry.oa.commonLg.service.CommonLgService;
import cn.honry.oa.commonLg.vo.CommonVo;

@Service("commonLgService")
public class CommonLgServiceImpl implements CommonLgService {
	
	@Autowired
	@Qualifier(value = "commonLgDao")
	private CommonLgDao commonLgDao; 

	@Override
	public void saveOrUpddateCommon(OaCommon oaCommon) {
		if (StringUtils.isBlank(oaCommon.getId())) {
			oaCommon.setId(null);
		}
		commonLgDao.save(oaCommon);
	}

	@Override
	public List<OaCommon> findMyCommon(String account , String tableCode) throws Exception {
		return commonLgDao.findMyCommon(account , tableCode);
	}

	@Override
	public void delCommon(String ids) throws Exception {
		String[] id = ids.split(",");
		for (String s : id) {
			commonLgDao.delCommonById(s);
		}
		
	}

	@Override
	public OaCommon findById(String id) throws Exception {
		return commonLgDao.findById(id);
	}

	@Override
	public List<OaCommon> findFrom(String account) {
		return commonLgDao.findFrom(account);
	}

	@Override
	public List<CommonVo> findCommon(String account, String tableCode) {
		return commonLgDao.findCommon(account,tableCode);
	}

}
