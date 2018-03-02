package cn.honry.inner.statistics.outpatientDocRecipe.service;

public interface InnerOutpatientDocRecipeService {
	/** 
	* @Description: 初始化门诊医生开单工作量
	* @param beginDate
	* @param endDate
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月6日
	*/
	void init_MZYSKDGZL(String menuAlias,String type,String beginDate);

}
