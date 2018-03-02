package cn.honry.inner.outpatient.grade.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.service.BaseService;


/**  
 *  
 * @className：GradeService 
 * @Description：  挂号级别Service
 * @Author：wujiao
 * @CreateDate：2015-6-18 下午01:44:48  
 * @Modifier：wujiao
 * @ModifyDate：2015-6-18 下午01:44:48  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface GradeInInterService extends BaseService<RegisterGrade>{

	
	/**  
	 *  
	 * @Description：  挂号级别分页查询 - 获得列表
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterGrade> getPage(String page, String rows,RegisterGrade registerGrade);

	/**  
	 *  
	 * @Description：  挂号级别分页查询 - 获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterGrade registerGrade);
	
	/**  
	 *  
	 * @Description：  挂号级别获得下拉列表
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterGrade> getCombobox(String time,String q);
	
	

}
