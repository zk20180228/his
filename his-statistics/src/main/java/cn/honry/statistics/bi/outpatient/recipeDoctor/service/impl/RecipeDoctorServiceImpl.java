package cn.honry.statistics.bi.outpatient.recipeDoctor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.outpatient.recipeDoctor.dao.RecipeDoctorDao;
import cn.honry.statistics.bi.outpatient.recipeDoctor.service.RecipeDoctorService;
import cn.honry.statistics.bi.outpatient.recipeDoctor.vo.BiOptFeedetailVo;
import cn.honry.utils.JSONUtils;


@Service("recipeDoctorService")
@Transactional(rollbackFor={Throwable.class})
@SuppressWarnings({ "all" })
public class RecipeDoctorServiceImpl implements RecipeDoctorService{
	
	/***
	 * 注入本类dao
	 */
	@Autowired
	@Qualifier(value = "recipeDoctorDao")
	private RecipeDoctorDao recipeDoctorDao;

	@Override
	public String queryData(BiOptFeedetailVo vo) {
		List<BiOptFeedetailVo> list = recipeDoctorDao.queryData(vo);
		
		
		
		return JSONUtils.toJson(list);
	}
	
	
	
	
}
