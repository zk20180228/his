package cn.honry.oa.information.service.impl;

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
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.information.dao.InformationAttachDownAuthDao;
import cn.honry.oa.information.service.InformationAttachDownAuthService;

@Transactional
@Service("informationAttachDownAuthService")
public class InformationAttachDownAuthServiceImpl implements InformationAttachDownAuthService {
	@Autowired
	@Qualifier("informationAttachDownAuthDao")
	private InformationAttachDownAuthDao informationAttachDownAuthDao;
	@Override
	public InformationAttachDownAuth get(String arg0) {
		return informationAttachDownAuthDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOrUpdate(InformationAttachDownAuth arg0) {
		informationAttachDownAuthDao.save(arg0);
	}

	@Override
	public List<InformationAttachDownAuth> getAuthByMenuID(String infoid) {
		Map<String,InformationAttachDownAuth> map =  new HashMap<String, InformationAttachDownAuth>();
		List<InformationAttachDownAuth> list = informationAttachDownAuthDao.getAuthByMenuID(infoid,true);
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
		informationAttachDownAuthDao.delAuth(menuId);
	}
	
}
