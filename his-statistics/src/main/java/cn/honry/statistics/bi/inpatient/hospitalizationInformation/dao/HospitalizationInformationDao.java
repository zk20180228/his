package cn.honry.statistics.bi.inpatient.hospitalizationInformation.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.vo.HospitalizationInformationVo;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface HospitalizationInformationDao extends EntityDao<BiInpatientInfo>{
	/**
	 * 查询所有科室
	 * @author Gengh
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	 List<BiBaseOrganization> queryAllDept();

	 List<HospitalizationInformationVo> querytDatagrid(String [] diArrayKey,List<Map<String,List<String>>> list,int datetype,DateVo datevo);
	/**
	 * 年龄 ：峰值
	 * 根据出生日期处理
	 */
	// List<HospitalizationInformationVo> getAgePeakValue();

	/**
	 * 统计图数据
	 * @param dateType  同比OR环比
	 * @return
	 */
//	List<HospitalizationInformationVo> querytStatData(String timeString,String dateType,String nameString);
	
	/**
	 * 地域渲染 map
	 */
	List<BIBaseDistrict> getHome();
	/**
	 * 
	 * @param deptType
	 * @return
	 */
	public List<BiBaseOrganization> queryDeptForBiPublic(String deptType);
}
