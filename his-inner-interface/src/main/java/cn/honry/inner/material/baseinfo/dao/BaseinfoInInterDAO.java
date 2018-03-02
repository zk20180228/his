package cn.honry.inner.material.baseinfo.dao;

import java.util.List;

import cn.honry.base.bean.model.MatBaseinfo;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：BaseinfoInInterDAO
 * @Description：  物资分类字典
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface BaseinfoInInterDAO extends EntityDao<MatBaseinfo>{
	
	/**
	 * @Description： 根据物资编码查询物资字典信息
	 * @param itemCode 物资编码
	 */
	List<MatBaseinfo> queryByItemCode(String itemCode);

}
