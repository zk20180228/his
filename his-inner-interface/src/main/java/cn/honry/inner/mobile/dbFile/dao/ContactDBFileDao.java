package cn.honry.inner.mobile.dbFile.dao;

import cn.honry.base.bean.model.MContactDBVersion;
import cn.honry.base.dao.EntityDao;

public interface ContactDBFileDao extends EntityDao<MContactDBVersion>{

	MContactDBVersion selectNewestVersion() throws Exception;

	void updateNullDBAdress(MContactDBVersion mcontactdbversion)throws Exception;

}
