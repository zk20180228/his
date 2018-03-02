package cn.honry.inpatient.inoroutstandard.dao;

import java.util.List;

import cn.honry.base.bean.model.InoroutStandardDetail;
import cn.honry.base.dao.EntityDao;

public interface InoroutStandardDetialDao extends EntityDao<InoroutStandardDetail> {
	List<InoroutStandardDetail> getStandardDetialList(String code,String versionNO);
}
