package cn.honry.outpatient.recipedetail.service;

import java.util.List;

import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;


public interface RecipedetailService extends BaseService<OutpatientRecipedetail>{

	/**  
	 *  
	 * @Description：  分类组套树
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-7 上午10:42:41  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 上午10:42:41  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> getSortStackTree()throws Exception;

	/**  
	 *  
	 * @Description：  添加
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-13 下午05:26:55  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-13 下午05:26:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveRecipedetail(OutpatientRecipedetail recipedetail,String recNonHerMedJson,String recHerMedJson);
	/**  
	 *  
	 * @Description：  历史医嘱
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List query(String entity);
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @version 1.0
	 * @return
	 */
	int getTotal(OutpatientRecipedetail entiry);
	/**  
	 *  
	 * @Description：  历史医嘱时间
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List  queryDate();
	
}
