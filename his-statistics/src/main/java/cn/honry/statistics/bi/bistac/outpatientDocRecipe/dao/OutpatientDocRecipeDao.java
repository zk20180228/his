package cn.honry.statistics.bi.bistac.outpatientDocRecipe.dao;

import java.util.List;

import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.OutpatientDocRecipeVo;

public interface OutpatientDocRecipeDao{
	List<OutpatientDocRecipeVo> MZYSKDGZ(String beginDate, String endDate, Integer type);
}
