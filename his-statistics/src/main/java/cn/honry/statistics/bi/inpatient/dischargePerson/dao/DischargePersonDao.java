package cn.honry.statistics.bi.inpatient.dischargePerson.dao;

import java.util.List;

import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;

@SuppressWarnings({"all"})
public interface DischargePersonDao extends EntityDao<BiInpatientInfo>{

	/**
	 * 根据年度查询出院人次
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	List<DischargePersonVo> queryDischargePersonList(String deptCode,
			String years, String quarters, String months, String days);
	
	/**
	 * 查询所有科室
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept(String type);
	
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
