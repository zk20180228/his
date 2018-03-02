package cn.honry.finance.medicinelist.dao;

import java.util.List;

import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface StoRecipeDAO extends EntityDao<StoRecipe>{

	/**
	 * @Description:根据处方号，出库申请分类，有效状态!=无效，处方状态!=发药 查询处方调剂头表
	 * @author: lt
	 * @CreateDate:2016-1-16
	 * @version:1.0
	 */
	StoRecipe getByParameter(String recipeNo);

	/**
	 * @Description:根据处方号list，出库申请分类，有效状态!=无效，处方状态!=发药 查询处方调剂头表
	 * @author: lt
	 * @CreateDate:2016-1-20
	 * @version:1.0
	 */
	List<StoRecipe> getByParameter(List<String> recipeNoList);

}
