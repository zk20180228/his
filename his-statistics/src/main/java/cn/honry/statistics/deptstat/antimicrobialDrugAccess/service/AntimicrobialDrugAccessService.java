package cn.honry.statistics.deptstat.antimicrobialDrugAccess.service;

import java.util.List;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface AntimicrobialDrugAccessService {
	/** 
	* @Description: 查询所有员工类型
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	List<AntimicrobialDrugAccessVo> queryType();
	/** 
	* @Description: 条件分页查询员工权限
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	List<AntimicrobialDrugAccessVo> queryAntimicrobialDrugAccess(String dept,
			 String page, String rows,String menuAlias);
	/** 
	* @Description: 查询总记录数
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	int queryAntimicrobialDrugAccessTotal(String dept,String menuAlias);
	/** 
	* @Description: 导出
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	FileUtil exportList(List<AntimicrobialDrugAccessVo> list, FileUtil fUtil);
	/** 
	* @Description: 从mongondb查询
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	List<AntimicrobialDrugAccessVo> queryAntimicrobialDrugAccessFromDB(List<String> codeList,
			String page, String rows,String menuAlias);

}
