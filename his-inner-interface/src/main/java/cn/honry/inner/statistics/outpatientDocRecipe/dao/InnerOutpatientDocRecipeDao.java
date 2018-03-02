package cn.honry.inner.statistics.outpatientDocRecipe.dao;

import java.util.List;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.inner.statistics.outpatientDocRecipe.vo.OutpatientDocRecipeVo;

public interface InnerOutpatientDocRecipeDao {

	List<OutpatientDocRecipeVo> MZYSKDGZ(String beginDate, String endDate);

	void saveMongoLog(MongoLog mong);}
