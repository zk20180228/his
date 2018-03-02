package cn.honry.statistics.bi.outpatient.recipeDoctor.dao;


import java.util.List;

import cn.honry.statistics.bi.outpatient.recipeDoctor.vo.BiOptFeedetailVo;

@SuppressWarnings({ "all" })
public interface RecipeDoctorDao{
	
	List<BiOptFeedetailVo> queryData(BiOptFeedetailVo vo);
}
