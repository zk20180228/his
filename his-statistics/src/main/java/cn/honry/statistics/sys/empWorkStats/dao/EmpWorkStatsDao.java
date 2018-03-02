package cn.honry.statistics.sys.empWorkStats.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.sys.empWorkStats.vo.EmpWorkStatsVo;

@SuppressWarnings({"all"})
public interface EmpWorkStatsDao extends EntityDao<RegisterInfo>{
	
	/**  
	 * @Description:门诊科室下拉框
	 * @Author：ldl
	 * @CreateDate：2016-06-23
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> deptCombobox();
	
	/**  
	 * @Description:坐诊医生下拉框
	 * @Author：ldl
	 * @CreateDate：2016-06-23
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param ids : 挂号科室
	 */
	List<SysEmployee> empCombobox(String ids);
	
	/**  
	 * @Description:查询列表
	 * @Author：ldl
	 * @CreateDate：2016-06-23
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:beginTime 开始时间
	 * @param:endTime 结束时间
	 * @param:dept 科室
	 * @param:emp 人员
	 */
	List<EmpWorkStatsVo> queryList(String beginTime, String endTime,String dept, String emp);
	
	/**
	 * @Description 查询列表
	 * @author  marongbin
	 * @createDate： 2016年12月21日 下午3:53:38 
	 * @modifier 
	 * @modifyDate：
	 * @param beginTime
	 * @param endTime
	 * @param dept
	 * @param emp
	 * @return: List<EmpWorkStatsVo>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<EmpWorkStatsVo> queryListNow(List<String> tnl,String beginTime, String endTime,String dept, String emp,String menuType,String rows,String page);
	/**
	 * 
	 * 
	 * <p>坐诊医生工作量统计总条数 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月12日 下午2:41:44 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月12日 下午2:41:44 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tnl
	 * @param beginTime
	 * @param endTime
	 * @param dept
	 * @param emp
	 * @param menuType
	 * @return:
	 *
	 */
	int getPage(List<String> tnl,String beginTime, String endTime,String dept, String emp,String menuType);
}
