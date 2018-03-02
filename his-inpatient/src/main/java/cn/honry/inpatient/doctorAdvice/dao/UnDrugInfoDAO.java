package cn.honry.inpatient.doctorAdvice.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.dao.EntityDao;

public interface UnDrugInfoDAO extends EntityDao<DrugUndrug>{
	/**  
	 *  
	 * @Description： 查询非药品信息总条数
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-26   
	 * @version 1.0
	 *
	 */
	int getTotal(DrugUndrug undrug);
	/**  
	 *  
	 * @Description： 查询非药品信息
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-26   
	 * @version 1.0
	 *
	 */
	List<DrugUndrug> queryUndrugInfo(String page,String rows,DrugUndrug undrug);
	/**  
	 *  
	 * @Description： 查询非药品信息-页面渲染
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-26   
	 * @version 1.0
	 *
	 */
	List<DrugUndrug> queryUndrugInfo();
	
	/**
	 * 
	 * 
	 * <p>加载所有非药品名称下拉 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:49:59 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	List queryAllUndrug();
}
