package cn.honry.inner.mobile.dbFile.service;

import cn.honry.base.bean.model.MContactDBVersion;
import cn.honry.base.service.BaseService;


public interface ContactDBFileService  extends BaseService<MContactDBVersion>{
    void updateDataToDBFile(String finalPath) throws Exception;
	MContactDBVersion selectNewestVersion() throws Exception;
}
