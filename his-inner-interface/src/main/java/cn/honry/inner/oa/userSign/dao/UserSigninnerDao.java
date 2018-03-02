package cn.honry.inner.oa.userSign.dao;

import java.util.List;

import cn.honry.base.bean.model.OaUserSign;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface UserSigninnerDao extends EntityDao<OaUserSign>{
	/**
	 * @Description:通过员工号查询员工所能使用的签名
	 * @Author: donghe
	 * @CreateDate: 2017年7月20日
	 * @param:account-员工号
	 * @return:List<OaUserSign>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<OaUserSign> queryOaUserSigns(String account,String signType);
}
