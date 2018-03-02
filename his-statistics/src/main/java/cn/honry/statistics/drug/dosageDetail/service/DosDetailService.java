package cn.honry.statistics.drug.dosageDetail.service;

import java.util.List;

import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.dosageDetail.vo.DetailVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface DosDetailService extends BaseService<StoRecipe>{
		
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
	 */
	List<DetailVo> queryDetail(String typeView,String typeValue,String beginDate,String endDate,String param,String code);
	
	
	
	/***
	 * 导出的验证 ？？？
	 * @Title: export 
	 * @author  WFJ
	 * @createDate ：2016年6月25日
	 * @param list
	 * @param fUtil
	 * @return FileUtil
	 * @return typeView 视图
	 * @version 1.0
	 */
	FileUtil export(List<DetailVo> list, FileUtil fUtil,String typeView)throws Exception;
	
	
}
