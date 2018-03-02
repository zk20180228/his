package cn.honry.oa.menumanager.dao;

import java.util.List;

import cn.honry.base.bean.model.OaMenuRight;
import cn.honry.base.dao.EntityDao;

public interface MenuRightDao extends EntityDao<OaMenuRight>  {

	/**
	 * 
	 * <p>根据menucode获取所有权限</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月28日 下午7:02:40 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月28日 下午7:02:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<OaMenuRight>
	 *
	 */
	List<OaMenuRight> findAllByMenuid(String code);

}
