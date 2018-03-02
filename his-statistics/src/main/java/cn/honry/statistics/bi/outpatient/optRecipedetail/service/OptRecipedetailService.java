package cn.honry.statistics.bi.outpatient.optRecipedetail.service;

import java.util.List;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface OptRecipedetailService extends BaseService<BiRegister>{
	/**
	 * 查询所有科室
	 * @author hmz
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept();

	/**
	 * 查询指标信息并处理数据
	 * @author hmz
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	
	

	String querytOptRecipeDatagrid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue);
	/**
	 *  页面初始化查询科室
	 * @author hmz
	 * @return
	 */
	List<BiBaseOrganization> queryDeptForBiPublic(String string);



}
