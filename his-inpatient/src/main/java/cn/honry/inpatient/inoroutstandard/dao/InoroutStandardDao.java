package cn.honry.inpatient.inoroutstandard.dao;

import java.util.List;

import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.inoroutstandard.vo.StandardVO;

public interface InoroutStandardDao extends EntityDao<InoroutStandard> {
	List<InoroutStandard> getStandardList(String code);
	List<StandardVO> getAllStandard();
}
