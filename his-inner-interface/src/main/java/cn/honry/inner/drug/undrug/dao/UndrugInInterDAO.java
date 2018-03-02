package cn.honry.inner.drug.undrug.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface UndrugInInterDAO extends EntityDao<DrugUndrug>{
	/**  
	 *  
	 * @Description：查询所有不是删除的记录
	 *@Author：zpty
	 * @CreateDate：2015-8-19 
	 * @version 1.0
	 *
	 */
	List<DrugUndrug> getInfo();
	
	/**  
	 *  
	 * @Description：  获得煎药方式
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午02:57:21  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午02:57:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	DrugUndrug getUnDrugByName(String name);
	
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(DrugUndrug undrug);
	
	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugUndrug> getPage(String page, String rows, DrugUndrug undrug);
	
	/**
	 * 非药品
	 * @author wujiao
	 */
	List<DrugUndrug> likeUndrugMap();
	
	/**  
	 *  
	 * @Description：  根据最小费用查询
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午06:10:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午06:10:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugUndrug> queryUndrugByDrugminimumcost(String id);

	/**  
	 * 
	 * @Author: aizhonghua
	 * @CreateDate: 2016年9月1日 下午3:37:42 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年9月1日 下午3:37:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	DrugUndrug getCode(String itemCode);
	
	/**  
	 * @Description： 查询非药品下 非组套的明细数据
	 * @Author：GH
	 * @CreateDate：2016年11月8日10:33:57
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	 List<DrugUndrug> queryNotTackUndrug(String page,String rows,String notTackUndrug);
}
