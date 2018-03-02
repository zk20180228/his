package cn.honry.statistics.bi.inpatient.dischargePerson.service;

import java.util.List;

import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;


public interface DischargePersonService extends BaseService<BiInpatientInfo>{
	
	/**
	 * 查询所有科室
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept(String type);
	/**
	 * 根据年度查询出院人次
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	List<DischargePersonVo> queryDischargePersonList(String deptCode,
			String years, String quarters, String months, String days);
	
	
	/**
	 * 加载全部
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	List<DischargePersonVo> loadPersonList(String type);
	
	/**
	 * 查询条形统计图同比环比
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	List<DischargePersonVo> loadBarPersonList(String string, String deptCode,
			String years, String quarters, String months, String days);
}
