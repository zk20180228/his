package cn.honry.statistics.drug.dosageDetail.dao;

import java.util.List;

import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.drug.dosageDetail.vo.DetailVo;

@SuppressWarnings({"all"})
public interface DosDetailDao extends EntityDao<StoRecipe>{
	
	/***
	 * 相应科室类型的集合
	 * @Title: deptForType 
	 * @author  WFJ
	 * @createDate ：2016年6月23日
	 * @param type 科室类型
	 * @return List<SysDepartment>
	 * @version 1.0
	 */
	List<SysDepartment> deptForType(String type);
	
	/***
	 * 
	 * @Title: queryDetail 
	 * @author  WFJ
	 * @createDate ：2016年6月23日
	 * @param typeView 视图方式 0药房_终端显示 ；1药房_人员显示；2人员检索
	 * @param typeValue value值
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param param 配发药标识，1配药，2发药
	 * @return List<StoRecipe>
	 * @version 1.0
	 * @param feedetialPartitionName 
	 */
	List<DetailVo> queryDetail0(List<String> feedetialPartitionName, String typeValue,String beginDate,String endDate,String param,String code,String typeView);
	
	/***
	 * 视图方式  1药房_人员显示；
	 * @param feedetialPartitionName 
	 */
	List<DetailVo> queryDetail1(List<String> feedetialPartitionName, String typeValue,String beginDate,String endDate,String param,String code);
	
	/***
	 * 视图方式  2人员检索
	 * @param feedetialPartitionName 
	 */
	List<DetailVo> queryDetail2(List<String> feedetialPartitionName, String typeValue,String beginDate,String endDate,String param,String code);
	
	/***
	 * 渲染员工
	 * @Title: queryEmployee 
	 * @author  WFJ
	 * @createDate ：2016年6月23日
	 * @param empID
	 * @return String
	 * @version 1.0
	 */
	String queryEmployee(String empID);
}
