package cn.honry.oa.patmenumanager.service;

import java.util.List;

import cn.honry.base.bean.model.OaMenuRight;
import cn.honry.oa.menumanager.vo.MenuVo;

public interface PatMenuRightService {

	/**
	 * 
	 * <p>跟去栏目id查询全部栏目权限</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月21日 下午5:29:10 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月21日 下午5:29:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	List<OaMenuRight>  findAllByMenuid(String id);
}
