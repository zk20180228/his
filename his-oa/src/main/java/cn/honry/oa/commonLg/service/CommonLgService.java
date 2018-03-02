package cn.honry.oa.commonLg.service;

import java.util.List;

import cn.honry.base.bean.model.OaCommon;
import cn.honry.oa.commonLg.vo.CommonVo;

public interface CommonLgService {
	/**
	 * 
	 * 
	 * <p>保存或修改方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月29日 上午11:35:58 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月29日 上午11:35:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOrUpddateCommon(OaCommon oaCommon);

	/**
	 * 
	 * <p>根据用户账户和表单id查找常用语</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月29日 上午11:36:25 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月29日 上午11:36:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<OaCommon>
	 *
	 */
	List<OaCommon> findMyCommon(String account, String tableCode) throws Exception;

	/**
	 * 
	 * <p>根据常用语id删除常用语</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月29日 上午11:37:25 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月29日 上午11:37:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	void delCommon(String ids) throws Exception;

	/**
	 * 
	 * <p>根据常用语id查找常用语</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月29日 上午11:37:25 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月29日 上午11:37:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	OaCommon findById(String id) throws Exception;

	/**
	 * <p>bootstrop使用数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月29日 上午11:39:34 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月29日 上午11:39:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<OaCommon>
	 *
	 */
	List<OaCommon> findFrom(String account);

	/**
	 * <p>用于签名时候选择</p>
	 * @Author: yuke
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<CommonVo>
	 *
	 */
	List<CommonVo> findCommon(String account, String tableCode);

}
