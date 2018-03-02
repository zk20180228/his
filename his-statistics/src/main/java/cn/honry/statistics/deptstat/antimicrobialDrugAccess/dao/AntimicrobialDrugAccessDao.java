package cn.honry.statistics.deptstat.antimicrobialDrugAccess.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;

@SuppressWarnings({"all"})
public interface AntimicrobialDrugAccessDao{
	/** 
	* @Description: 查询所有员工类型
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	List<AntimicrobialDrugAccessVo> queryType();
	/** 
	* @Description: 分页条查询员工权限
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
	* @Description: 从mongondb查询
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	List<AntimicrobialDrugAccessVo> queryOperationProportionFromDB(List<String> dept,
			String page, String rows,String menuAlias);

}
