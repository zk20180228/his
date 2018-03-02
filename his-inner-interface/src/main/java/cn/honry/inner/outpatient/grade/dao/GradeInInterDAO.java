package cn.honry.inner.outpatient.grade.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * 内部接口: GradeInInterDAO 
 * @Description：  挂号级别DAO 
 * @Author：tangfeishuai
 * @CreateDate：2016-7-7 下午01:49:41  
 * @Modifier：tangfeishuai
 * @ModifyDate：2016-7-7 下午01:49:41  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface GradeInInterDAO extends EntityDao<RegisterGrade>{
	
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
	List<RegisterGrade> getPage(RegisterGrade entity, String page, String rows);

	/**  
	 *  
	 * @Description：  挂号级别分页查询 - 获得统计总条数
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterGrade entity);
	
	/**  
	 *  
	 * @Description：  挂号级别 - 获得下拉列表
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
