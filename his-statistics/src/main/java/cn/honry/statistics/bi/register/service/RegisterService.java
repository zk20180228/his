package cn.honry.statistics.bi.register.service;

import java.util.List;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface RegisterService extends BaseService<BiRegister>{

	/**
	 * 查询所有科室
	 * @author hanzurong
	 * @createDate：2016/7/28
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept();
	/**
	 * 查询所有医师级别
	 * @author hanzurong
	 * @createDate：2016/7/28
	 * @version 1.0
	 */
	List<BiRegisterGrade> queryAllGrade();
	/**
	 * 查询列表数据
	 * @author hanzurong
	 * @createDate：2016/7/28
	 * @version 1.0
	 * @param dateString 
	 * @param dateString 
	 */
	String queryregisterid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue);
	/**
	 * 查询统计图数据
	 * @author hanzurong
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	String queryStatDate(String timeString, String nameString);
	/**
	 * 查询科室code、name
	 * @author hanzurong
	 * @return
	 */
	List<BiBaseOrganization> queryDeptForBiPublic();
	/**
	 * 查询挂号级别code、name
	 * @author hanzurong
	 * @return
	 */
	List<BiBaseOrganization> queryreglevlForBiPublic();
}
