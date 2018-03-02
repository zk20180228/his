package cn.honry.statistics.bi.inpatient.hospitalizationInformation.service;

import java.util.List;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface HospitalizationInformationService extends BaseService<BiRegister>{
	/**
	 * 查询所有科室
	 * @author tuchuanjiang
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	List<BiBaseOrganization> queryAllDept();
	
	/**
	 * 获取地域渲染map
	 * @return
	 */
	List<BIBaseDistrict> getHome();
	/**
	 * 查询列表数据
	 * @param timeone
	 * @param timetwo
	 * @param nameString
	 * @param type
	 * @return
	 */
	String querytDatagrid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue);

	/**
	 * 年龄 ：峰值 (只查询当前日期)
	 * 根据出生日期处理
	 */
//	String getAgePeakValue();
	
	/**
	 *	查询统计图数据 
	 *2016年7月26日10:06:34
	 */
//	Map<String, Object> queryStatDate(String typeString,String nameString);

	/**
	 * 2016年8月19日15:14:43 页面初始化查询科室
	 */
	public List<BiBaseOrganization> queryDeptForBiPublic(String deptType);

}
