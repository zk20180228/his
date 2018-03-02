package cn.honry.oa.patInformation.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InformationAttachDownAuth;
import cn.honry.oa.information.dao.InformationAttachDownAuthDao;
import cn.honry.oa.patInformation.dao.PatInformationAttachDownAuthDao;
import cn.honry.oa.patInformation.service.PatInformationAttachDownAuthService;

@Transactional
@Service("patInformationAttachDownAuthService")
public class PatInformationAttachDownAuthServiceImpl implements PatInformationAttachDownAuthService {
	@Autowired
	@Qualifier("patInformationAttachDownAuthDao")
	private PatInformationAttachDownAuthDao patInformationAttachDownAuthDao;
	@Override
	public InformationAttachDownAuth get(String arg0) {
		return patInformationAttachDownAuthDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOrUpdate(InformationAttachDownAuth arg0) {
		patInformationAttachDownAuthDao.save(arg0);
	}

	@Override
	public List<InformationAttachDownAuth> getAuthByMenuID(String infoid) {
		Map<String,InformationAttachDownAuth> map =  new HashMap<String, InformationAttachDownAuth>();
		List<InformationAttachDownAuth> list = patInformationAttachDownAuthDao.getAuthByMenuID(infoid,true);
		for (InformationAttachDownAuth auth : list) {
			if(!map.containsKey(auth.getFileURL())){
				map.put(auth.getFileURL(), auth);
			}
		}
		List<InformationAttachDownAuth> list1 = new ArrayList<InformationAttachDownAuth>();
		for(Entry<String, InformationAttachDownAuth> m : map.entrySet()){
			list1.add(m.getValue());
		}
		return list1;
	}

	@Override
	public void delAuth(String menuId) {
		patInformationAttachDownAuthDao.delAuth(menuId);
	}
	
}
