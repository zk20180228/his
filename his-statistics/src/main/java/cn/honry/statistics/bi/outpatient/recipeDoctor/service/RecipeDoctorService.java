package cn.honry.statistics.bi.outpatient.recipeDoctor.service;

import cn.honry.statistics.bi.outpatient.recipeDoctor.vo.BiOptFeedetailVo;

@SuppressWarnings({ "all" })
public interface RecipeDoctorService {

	String queryData(BiOptFeedetailVo vo);
}
