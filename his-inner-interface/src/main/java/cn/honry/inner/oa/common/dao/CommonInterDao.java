package cn.honry.inner.oa.common.dao;

import java.util.List;

import cn.honry.base.bean.model.OaCommon;
import cn.honry.base.dao.EntityDao;

public interface CommonInterDao extends EntityDao<OaCommon> {

	List<OaCommon> findMyCommon(String account, String tableCode) throws Exception;

	void delCommonById(String s) throws Exception;

	OaCommon findById(String id) throws Exception;

	List<OaCommon> findFrom(String account);
	/**
	 * 根据流程ID查询流程创建人
	 * 
	 * @param id
	 * @return
	 */
	String queryCreateUserNameById(String id);
}
